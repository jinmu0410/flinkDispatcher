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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Column
 */
public class Column {
    public static final String SERIALIZED_NAME_COMMENT = "comment";
    @SerializedName(SERIALIZED_NAME_COMMENT)
    private String comment;

    public static final String SERIALIZED_NAME_DATA_TYPE = "dataType";
    @SerializedName(SERIALIZED_NAME_DATA_TYPE)
    private DataType dataType;

    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName(SERIALIZED_NAME_NAME)
    private String name;

    public static final String SERIALIZED_NAME_PERSISTED = "persisted";
    @SerializedName(SERIALIZED_NAME_PERSISTED)
    private Boolean persisted;

    public static final String SERIALIZED_NAME_PHYSICAL = "physical";
    @SerializedName(SERIALIZED_NAME_PHYSICAL)
    private Boolean physical;

    public Column() {
    }

    public Column comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Get comment
     *
     * @return comment
     **/
    //@javax.annotation.Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Column dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    /**
     * Get dataType
     *
     * @return dataType
     **/
    //@javax.annotation.Nullable
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }


    public Column name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    //@javax.annotation.Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Column persisted(Boolean persisted) {
        this.persisted = persisted;
        return this;
    }

    /**
     * Get persisted
     *
     * @return persisted
     **/
    //@javax.annotation.Nullable
    public Boolean getPersisted() {
        return persisted;
    }

    public void setPersisted(Boolean persisted) {
        this.persisted = persisted;
    }


    public Column physical(Boolean physical) {
        this.physical = physical;
        return this;
    }

    /**
     * Get physical
     *
     * @return physical
     **/
    //@javax.annotation.Nullable
    public Boolean getPhysical() {
        return physical;
    }

    public void setPhysical(Boolean physical) {
        this.physical = physical;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Column column = (Column) o;
        return Objects.equals(this.comment, column.comment) &&
                Objects.equals(this.dataType, column.dataType) &&
                Objects.equals(this.name, column.name) &&
                Objects.equals(this.persisted, column.persisted) &&
                Objects.equals(this.physical, column.physical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, dataType, name, persisted, physical);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Column {\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    persisted: ").append(toIndentedString(persisted)).append("\n");
        sb.append("    physical: ").append(toIndentedString(physical)).append("\n");
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
        openapiFields.add("comment");
        openapiFields.add("dataType");
        openapiFields.add("name");
        openapiFields.add("persisted");
        openapiFields.add("physical");

        // a set of required properties/fields (JSON key names)
        openapiRequiredFields = new HashSet<String>();
    }

    /**
     * Validates the JSON Element and throws an exception if issues found
     *
     * @param jsonElement JSON Element
     * @throws IOException if the JSON Element is invalid with respect to Column
     */
    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null) {
            if (!Column.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
                throw new IllegalArgumentException(String.format("The required field(s) %s in Column is not found in the empty JSON string", Column.openapiRequiredFields.toString()));
            }
        }

        Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
        // check to see if the JSON string contains additional fields
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (!Column.openapiFields.contains(entry.getKey())) {
                throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `Column` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
            }
        }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        if ((jsonObj.get("comment") != null && !jsonObj.get("comment").isJsonNull()) && !jsonObj.get("comment").isJsonPrimitive()) {
            throw new IllegalArgumentException(String.format("Expected the field `comment` to be a primitive type in the JSON string but got `%s`", jsonObj.get("comment").toString()));
        }
        // validate the optional field `dataType`
        if (jsonObj.get("dataType") != null && !jsonObj.get("dataType").isJsonNull()) {
            DataType.validateJsonElement(jsonObj.get("dataType"));
        }
        if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
            throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!Column.class.isAssignableFrom(type.getRawType())) {
                return null; // this class only serializes 'Column' and its subtypes
            }
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
            final TypeAdapter<Column> thisAdapter
                    = gson.getDelegateAdapter(this, TypeToken.get(Column.class));

            return (TypeAdapter<T>) new TypeAdapter<Column>() {
                @Override
                public void write(JsonWriter out, Column value) throws IOException {
                    JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                    elementAdapter.write(out, obj);
                }

                @Override
                public Column read(JsonReader in) throws IOException {
                    JsonElement jsonElement = elementAdapter.read(in);
                    validateJsonElement(jsonElement);
                    return thisAdapter.fromJsonTree(jsonElement);
                }

            }.nullSafe();
        }
    }

    /**
     * Create an instance of Column given an JSON string
     *
     * @param jsonString JSON string
     * @return An instance of Column
     * @throws IOException if the JSON string is invalid with respect to Column
     */
    public static Column fromJson(String jsonString) throws IOException {
        return JSON.getGson().fromJson(jsonString, Column.class);
    }

    /**
     * Convert an instance of Column to an JSON string
     *
     * @return JSON string
     */
    public String toJson() {
        return JSON.getGson().toJson(this);
    }
}

