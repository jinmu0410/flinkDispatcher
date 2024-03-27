package com.jm.flink.base.utils;


import com.jm.flink.constant.FlinkDoConstants;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * @author tasher @ClassName JsonModifyUtil.java @Description TODO
 * @createTime 2022/04/11
 */
public class JsonStringUtil {

    public static String jsonValueReplace(String json, HashMap<String, String> parameter) {
        for (String item : parameter.keySet()) {
            if (json.contains("${" + item + "}")) {
                json = json.replace("${" + item + "}", parameter.get(item));
            }
        }
        return json;
    }

    /**
     * 将命令行中的参数转化为HashMap保存
     *
     * @param command
     * @return java.util.HashMap<java.lang.String, java.lang.String>
     * @author tasher
     * @created 2022/4/11
     */
    public static HashMap<String, String> commandTransform(String command) {
        HashMap<String, String> parameter = new HashMap<>();
        String[] split = StringUtils.split(command, FlinkDoConstants.RUN_COMMA_SYMBOL);
        for (String item : split) {
            String[] temp = item.split(FlinkDoConstants.RUN_EQUAL_SYMBOL);
            parameter.put(temp[0], temp[1]);
        }
        return parameter;
    }
}
