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
 * ResolvedExpression
 */

public class ResolvedExpression {
    public static final String SERIALIZED_NAME_CHILDREN = "children";
    @SerializedName(SERIALIZED_NAME_CHILDREN)
    private List<Object> children;

    public static final String SERIALIZED_NAME_OUTPUT_DATA_TYPE = "outputDataType";
    @SerializedName(SERIALIZED_NAME_OUTPUT_DATA_TYPE)
    private DataType outputDataType;

    public static final String SERIALIZED_NAME_RESOLVED_CHILDREN = "resolvedChildren";
    @SerializedName(SERIALIZED_NAME_RESOLVED_CHILDREN)
    private List<ResolvedExpression> resolvedChildren;

    public ResolvedExpression() {
    }

    public ResolvedExpression children(List<Object> children) {
        this.children = children;
        return this;
    }

    public ResolvedExpression addChildrenItem(Object childrenItem) {
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
    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }


    public ResolvedExpression outputDataType(DataType outputDataType) {
        this.outputDataType = outputDataType;
        return this;
    }

    /**
     * Get outputDataType
     *
     * @return outputDataType
     **/
    //@javax.annotation.Nullable
    public DataType getOutputDataType() {
        return outputDataType;
    }

    public void setOutputDataType(DataType outputDataType) {
        this.outputDataType = outputDataType;
    }


    public ResolvedExpression resolvedChildren(List<ResolvedExpression> resolvedChildren) {
        this.resolvedChildren = resolvedChildren;
        return this;
    }

    public ResolvedExpression addResolvedChildrenItem(ResolvedExpression resolvedChildrenItem) {
        if (this.resolvedChildren == null) {
            this.resolvedChildren = new ArrayList<>();
        }
        this.resolvedChildren.add(resolvedChildrenItem);
        return this;
    }

    /**
     * Get resolvedChildren
     *
     * @return resolvedChildren
     **/
    //@javax.annotation.Nullable
    public List<ResolvedExpression> getResolvedChildren() {
        return resolvedChildren;
    }

    public void setResolvedChildren(List<ResolvedExpression> resolvedChildren) {
        this.resolvedChildren = resolvedChildren;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResolvedExpression resolvedExpression = (ResolvedExpression) o;
        return Objects.equals(this.children, resolvedExpression.children) &&
                Objects.equals(this.outputDataType, resolvedExpression.outputDataType) &&
                Objects.equals(this.resolvedChildren, resolvedExpression.resolvedChildren);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, outputDataType, resolvedChildren);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResolvedExpression {\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
        sb.append("    outputDataType: ").append(toIndentedString(outputDataType)).append("\n");
        sb.append("    resolvedChildren: ").append(toIndentedString(resolvedChildren)).append("\n");
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
        openapiFields.add("outputDataType");
        openapiFields.add("resolvedChildren");

        // a set of required properties/fields (JSON key names)
        openapiRequiredFields = new HashSet<String>();
    }

    /**
     * Validates the JSON Element and throws an exception if issues found
     *
     * @param jsonElement JSON Element
     * @throws IOException if the JSON Element is invalid with respect to ResolvedExpression
     */
    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null) {
            if (!ResolvedExpression.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
                throw new IllegalArgumentException(String.format("The required field(s) %s in ResolvedExpression is not found in the empty JSON string", ResolvedExpression.openapiRequiredFields.toString()));
            }
        }

        Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
        // check to see if the JSON string contains additional fields
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (!ResolvedExpression.openapiFields.contains(entry.getKey())) {
                throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ResolvedExpression` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
            }
        }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        // ensure the optional json data is an array if present
        if (jsonObj.get("children") != null && !jsonObj.get("children").isJsonNull() && !jsonObj.get("children").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `children` to be an array in the JSON string but got `%s`", jsonObj.get("children").toString()));
        }
        // validate the optional field `outputDataType`
        if (jsonObj.get("outputDataType") != null && !jsonObj.get("outputDataType").isJsonNull()) {
            DataType.validateJsonElement(jsonObj.get("outputDataType"));
        }
        if (jsonObj.get("resolvedChildren") != null && !jsonObj.get("resolvedChildren").isJsonNull()) {
            JsonArray jsonArrayresolvedChildren = jsonObj.getAsJsonArray("resolvedChildren");
            if (jsonArrayresolvedChildren != null) {
                // ensure the json data is an array
                if (!jsonObj.get("resolvedChildren").isJsonArray()) {
                    throw new IllegalArgumentException(String.format("Expected the field `resolvedChildren` to be an array in the JSON string but got `%s`", jsonObj.get("resolvedChildren").toString()));
                }

                // validate the optional field `resolvedChildren` (array)
                for (int i = 0; i < jsonArrayresolvedChildren.size(); i++) {
                    ResolvedExpression.validateJsonElement(jsonArrayresolvedChildren.get(i));
                }
                ;
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!ResolvedExpression.class.isAssignableFrom(type.getRawType())) {
                return null; // this class only serializes 'ResolvedExpression' and its subtypes
            }
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
            final TypeAdapter<ResolvedExpression> thisAdapter
                    = gson.getDelegateAdapter(this, TypeToken.get(ResolvedExpression.class));

            return (TypeAdapter<T>) new TypeAdapter<ResolvedExpression>() {
                @Override
                public void write(JsonWriter out, ResolvedExpression value) throws IOException {
                    JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                    elementAdapter.write(out, obj);
                }

                @Override
                public ResolvedExpression read(JsonReader in) throws IOException {
                    JsonElement jsonElement = elementAdapter.read(in);
                    validateJsonElement(jsonElement);
                    return thisAdapter.fromJsonTree(jsonElement);
                }

            }.nullSafe();
        }
    }

    /**
     * Create an instance of ResolvedExpression given an JSON string
     *
     * @param jsonString JSON string
     * @return An instance of ResolvedExpression
     * @throws IOException if the JSON string is invalid with respect to ResolvedExpression
     */
    public static ResolvedExpression fromJson(String jsonString) throws IOException {
        return JSON.getGson().fromJson(jsonString, ResolvedExpression.class);
    }

    /**
     * Convert an instance of ResolvedExpression to an JSON string
     *
     * @return JSON string
     */
    public String toJson() {
        return JSON.getGson().toJson(this);
    }
}

