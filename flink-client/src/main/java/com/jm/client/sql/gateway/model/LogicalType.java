/*
 * Flink SQL Gateway REST API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v2/1.18-SNAPSHOT
 * Contact: user@flink.apache.org
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.jm.client.sql.gateway.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jm.client.sql.gateway.invoker.JSON;

import java.io.IOException;
import java.util.*;

/**
 * LogicalType
 */

public class LogicalType {
    public static final String SERIALIZED_NAME_CHILDREN = "children";
    @SerializedName(SERIALIZED_NAME_CHILDREN)
    private List<LogicalType> children;

    public static final String SERIALIZED_NAME_NULLABLE = "nullable";
    @SerializedName(SERIALIZED_NAME_NULLABLE)
    private Boolean nullable;

    public static final String SERIALIZED_NAME_TYPE_ROOT = "typeRoot";
    @SerializedName(SERIALIZED_NAME_TYPE_ROOT)
    private LogicalTypeRoot typeRoot;

    public LogicalType() {
    }

    public LogicalType children(List<LogicalType> children) {
        this.children = children;
        return this;
    }

    public LogicalType addChildrenItem(LogicalType childrenItem) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(childrenItem);
        return this;
    }

    /**
     * Get children
     *
     * @return children
     **/
    //@javax.annotation.Nullable
    public List<LogicalType> getChildren() {
        return children;
    }

    public void setChildren(List<LogicalType> children) {
        this.children = children;
    }


    public LogicalType nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * Get nullable
     *
     * @return nullable
     **/
    //@javax.annotation.Nullable
    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }


    public LogicalType typeRoot(LogicalTypeRoot typeRoot) {
        this.typeRoot = typeRoot;
        return this;
    }

    /**
     * Get typeRoot
     *
     * @return typeRoot
     **/
    //@javax.annotation.Nullable
    public LogicalTypeRoot getTypeRoot() {
        return typeRoot;
    }

    public void setTypeRoot(LogicalTypeRoot typeRoot) {
        this.typeRoot = typeRoot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogicalType logicalType = (LogicalType) o;
        return Objects.equals(this.children, logicalType.children) &&
                Objects.equals(this.nullable, logicalType.nullable) &&
                Objects.equals(this.typeRoot, logicalType.typeRoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, nullable, typeRoot);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LogicalType {\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
        sb.append("    nullable: ").append(toIndentedString(nullable)).append("\n");
        sb.append("    typeRoot: ").append(toIndentedString(typeRoot)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    public static HashSet<String> openapiFields;
    public static HashSet<String> openapiRequiredFields;

    static {
        // a set of all properties/fields (JSON key names)
        openapiFields = new HashSet<String>();
        openapiFields.add("children");
        openapiFields.add("nullable");
        openapiFields.add("typeRoot");

        // a set of required properties/fields (JSON key names)
        openapiRequiredFields = new HashSet<String>();
    }

    /**
     * Validates the JSON Element and throws an exception if issues found
     *
     * @param jsonElement JSON Element
     * @throws IOException if the JSON Element is invalid with respect to LogicalType
     */
    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null) {
            if (!LogicalType.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
                throw new IllegalArgumentException(String.format("The required field(s) %s in LogicalType is not found in the empty JSON string", LogicalType.openapiRequiredFields.toString()));
            }
        }

        Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
        // check to see if the JSON string contains additional fields
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (!LogicalType.openapiFields.contains(entry.getKey())) {
                throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `LogicalType` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
            }
        }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        if (jsonObj.get("children") != null && !jsonObj.get("children").isJsonNull()) {
            JsonArray jsonArraychildren = jsonObj.getAsJsonArray("children");
            if (jsonArraychildren != null) {
                // ensure the json data is an array
                if (!jsonObj.get("children").isJsonArray()) {
                    throw new IllegalArgumentException(String.format("Expected the field `children` to be an array in the JSON string but got `%s`", jsonObj.get("children").toString()));
                }

                // validate the optional field `children` (array)
                for (int i = 0; i < jsonArraychildren.size(); i++) {
                    LogicalType.validateJsonElement(jsonArraychildren.get(i));
                }
                ;
            }
        }
        // validate the optional field `typeRoot`
        if (jsonObj.get("typeRoot") != null && !jsonObj.get("typeRoot").isJsonNull()) {
            LogicalTypeRoot.validateJsonElement(jsonObj.get("typeRoot"));
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!LogicalType.class.isAssignableFrom(type.getRawType())) {
                return null; // this class only serializes 'LogicalType' and its subtypes
            }
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
            final TypeAdapter<LogicalType> thisAdapter
                    = gson.getDelegateAdapter(this, TypeToken.get(LogicalType.class));

            return (TypeAdapter<T>) new TypeAdapter<LogicalType>() {
                @Override
                public void write(JsonWriter out, LogicalType value) throws IOException {
                    JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                    elementAdapter.write(out, obj);
                }

                @Override
                public LogicalType read(JsonReader in) throws IOException {
                    JsonElement jsonElement = elementAdapter.read(in);
                    validateJsonElement(jsonElement);
                    return thisAdapter.fromJsonTree(jsonElement);
                }

            }.nullSafe();
        }
    }

    /**
     * Create an instance of LogicalType given an JSON string
     *
     * @param jsonString JSON string
     * @return An instance of LogicalType
     * @throws IOException if the JSON string is invalid with respect to LogicalType
     */
    public static LogicalType fromJson(String jsonString) throws IOException {
        return JSON.getGson().fromJson(jsonString, LogicalType.class);
    }

    /**
     * Convert an instance of LogicalType to an JSON string
     *
     * @return JSON string
     */
    public String toJson() {
        return JSON.getGson().toJson(this);
    }
}

