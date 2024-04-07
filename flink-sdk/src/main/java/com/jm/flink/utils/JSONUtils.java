package com.jm.flink.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author genxin.liu
 * @since 2021/12/13 17:14
 */
public class JSONUtils {

    public JSONUtils() {
        throw new UnsupportedOperationException("Construct JSONUtils");
    }

    /**
     * 对象转JSON
     * {"name":null,"age":12} =>{"name":null,"age":12}
     *
     * @return java.lang.String
     * @author genxin.liu
     * @date 2021/12/13 17:16
     * @param: obj
     */
    public static String toJSONString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * JSON转对象
     *
     * @return T
     * @author genxin.liu
     * @date 2021/12/13 17:19
     * @param: json
     * @param: clazz
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    /**
     * JSON转对象(泛型)
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        return JSONObject.parseObject(json, typeReference);
    }

    /**
     * JSON转集合
     *
     * @return java.util.List<T>
     * @author genxin.liu
     * @date 2021/12/15 10:03
     * @param: json
     * @param: clazz
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSONObject.parseArray(json, clazz);
    }

    /**
     * JSON转Map
     *
     * @param json
     * @param keyClazz
     * @param valClazz
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClazz, Class<V> valClazz) {
        return JSONObject.parseObject(json, new TypeReference<Map<K, V>>() {
        });
    }

    /**
     * <p>
     * 将Object转化为json字符串：默认仅使用值非null的字段，可设置序列化指定字段，映射新的字段名
     * </p>
     *
     * @param obj      要序列化的目标对象, Must no be null
     * @param nameMap  要替换的字段名映射Map, Key is old name of property,Value is the new,May be null or empty
     * @param clazz    要过滤属性的类,May be null or empty
     * @param includes 要保留的属性,May be null or empty
     * @param excludes 要过滤掉的属性,May be null or empty
     * @author jinmu
     * @since v0.8.1
     */
    public static String toJson(Object obj, final Map<String, String> nameMap, Class clazz,
                                String[] includes,
                                String[] excludes) {
        if (obj == null) {
            throw new IllegalArgumentException("Input [obj] is null");
        }

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);

        // Add nameFilter
        if (null != nameMap && !nameMap.keySet().isEmpty()) {

            NameFilter nameFilter = new NameFilter() {
                @Override
                public String process(Object source, String name, Object value) {
                    for (String key : nameMap.keySet()) {
                        if (name.equals(key)) {
                            return nameMap.get(key);
                        }
                    }
                    return name;
                }
            };
            serializer.getNameFilters().add(nameFilter);
        }

        // Add properties
        if (clazz != null) {
            MySimplePropertyPreFilter filter = new MySimplePropertyPreFilter(clazz);
            filter.setIncludes(includes);
            filter.setExcludes(excludes);
            serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
        }

        // TODO
        // serializer.setDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
        serializer.write(obj);
        return out.toString();
    }

    /**
     * <p>
     * 将Object转化为json字符串：默认仅使用值非null的字段，可设置序列化指定字段，映射新的字段名
     * </p>
     *
     * @param obj      要序列化的目标对象, Must no be null
     * @param nameMap  要替换的字段名映射Map, Key is old name of property,Value is the new,Must no be empty
     * @param clazz    要过滤属性的类,Must not be null
     * @param includes 要保留的属性,Must no be empty
     * @author jinmu
     * @since v0.8.1
     */
    public static String toJson(Object obj, final Map<String, String> nameMap, Class clazz,
                                String... includes) {
        if (null == nameMap || nameMap.entrySet().isEmpty() || clazz == null || ArrayUtils
                .isEmpty(includes)) {
            throw new IllegalArgumentException("Input [nameMap,clazz,includes] is null or empty");
        }
        return toJson(obj, nameMap, clazz, includes, null);
    }

    /**
     * <p>
     * 将Object转化为json字符串：映射新的字段名<br> 1、映射对全部对象都有效<br> 2、适合Object为容器类时使用<br>
     * </p>
     *
     * @param obj     要序列化的目标对象, Must no be null
     * @param nameMap 要替换的字段名映射Map, Key is old name of property,Value is the new,Must no be empty
     * @author jinmu
     * @since v0.8.1
     */
    public static String toJson(Object obj, final Map<String, String> nameMap) {
        if (null == nameMap || nameMap.keySet().isEmpty()) {
            throw new IllegalArgumentException("Input [nameMap] is empty");
        }
        return toJson(obj, nameMap, null, null, null);
    }

    /**
     * <p>
     * 将Object转化为json字符串：映射新的字段名<br> 1、指定字段仅对Object本身所属的class有效<br> 2、适合Object为非容器类的简单对象时使用<br>
     * </p>
     *
     * @param obj      要序列化的目标对象, Must no be null
     * @param includes 要保留的属性,May be null or empty
     * @author jinmu
     * @since v0.8.1
     */
    public static String toJson(Object obj, String... includes) {
        return toJson(obj, obj.getClass(), includes, null);
    }

    /**
     * <p>
     * 将Object转化为json字符串：默认仅使用值非null的字段，可设置仅序列化指定类的特定字段<br>
     * </p>
     *
     * @param obj      要序列化的目标对象, Must no be null
     * @param clazz    要过滤属性的类,May be null or empty
     * @param includes 要保留的属性,May be null or empty
     * @author jinmu
     * @since v0.8.1
     */
    public static String toJson(Object obj, Class clazz, String... includes) {
        return toJson(obj, clazz, includes, null);
    }


    /**
     * toJson 将Object转化为json字符串：默认仅使用值非null的字段，可设置仅序列化指定类的特定字段<br>
     *
     * @param obj      要序列化的目标对象, Must no be null
     * @param clazz    要过滤属性的类,May be null or empty
     * @param includes 要保留的属性,May be null or empty
     * @param excludes 要过滤掉的属性,May be null or empty
     * @return java.lang.String
     * @author jinmu
     * @created 2021/4/9
     */
    public static String toJson(Object obj, Class clazz, String[] includes, String[] excludes) {
        if (obj == null) {
            throw new IllegalArgumentException("Input [obj] is null");
        }
        MySimplePropertyPreFilter filter = new MySimplePropertyPreFilter(clazz);
        filter.setIncludes(includes);
        filter.setExcludes(excludes);
        return JSON.toJSONString(obj, filter);
    }

    /**
     * toJson
     *
     * @return java.lang.String
     * @author jinmu
     * @created 2021/4/9
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj);
    }

    /**
     * toBean
     *
     * @return T
     * @author jinmu
     * @param: json
     * @created 2021/4/9
     */
    public static <T> T toBean(String json, Class<?> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        return (T) JSON.parseObject(json, clazz);
    }

    /**
     * toBean
     *
     * @return T
     * @author jinmu
     * @param: json
     * @created 2021/4/9
     */
    public static <T> T toBean(String json, TypeReference type) {
        if (json == null || json.length() == 0) {
            return null;
        }
        return (T) JSON.parseObject(json, type, Feature.IgnoreNotMatch);
    }

    /**
     * json字符串转化为List.
     *
     * @author jinmu
     * @created 2013年12月25日 下午2:52:21
     * @since v0.8.9
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return Collections.emptyList();
        }
        try {
            return JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return Collections.emptyList();
        }
    }

    private ObjectMapper mapper = new ObjectMapper();

    public String objectToJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public <T> T readValue(String jsonStr, Class<T> valueType) throws IOException {
        return mapper.readValue(jsonStr, valueType);
    }

    public static void main(String[] args) {
        System.out.println(111);
    }
}
