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
 * RowData
 */

public class RowData {
  public static final String SERIALIZED_NAME_ARITY = "arity";
  @SerializedName(SERIALIZED_NAME_ARITY)
  private Integer arity;

  public static final String SERIALIZED_NAME_ROW_KIND = "rowKind";
  @SerializedName(SERIALIZED_NAME_ROW_KIND)
  private RowKind rowKind;

  public RowData() {
  }

  public RowData arity(Integer arity) {
    this.arity = arity;
    return this;
  }

   /**
   * Get arity
   * @return arity
  **/
  //@javax.annotation.Nullable
  public Integer getArity() {
    return arity;
  }

  public void setArity(Integer arity) {
    this.arity = arity;
  }


  public RowData rowKind(RowKind rowKind) {
    this.rowKind = rowKind;
    return this;
  }

   /**
   * Get rowKind
   * @return rowKind
  **/
  //@javax.annotation.Nullable
  public RowKind getRowKind() {
    return rowKind;
  }

  public void setRowKind(RowKind rowKind) {
    this.rowKind = rowKind;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RowData rowData = (RowData) o;
    return Objects.equals(this.arity, rowData.arity) &&
        Objects.equals(this.rowKind, rowData.rowKind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(arity, rowKind);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RowData {\n");
    sb.append("    arity: ").append(toIndentedString(arity)).append("\n");
    sb.append("    rowKind: ").append(toIndentedString(rowKind)).append("\n");
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
    openapiFields.add("arity");
    openapiFields.add("rowKind");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Element and throws an exception if issues found
  *
  * @param jsonElement JSON Element
  * @throws IOException if the JSON Element is invalid with respect to RowData
  */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!RowData.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in RowData is not found in the empty JSON string", RowData.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!RowData.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `RowData` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `rowKind`
      if (jsonObj.get("rowKind") != null && !jsonObj.get("rowKind").isJsonNull()) {
        RowKind.validateJsonElement(jsonObj.get("rowKind"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!RowData.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'RowData' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<RowData> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(RowData.class));

       return (TypeAdapter<T>) new TypeAdapter<RowData>() {
           @Override
           public void write(JsonWriter out, RowData value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public RowData read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of RowData given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of RowData
  * @throws IOException if the JSON string is invalid with respect to RowData
  */
  public static RowData fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, RowData.class);
  }

 /**
  * Convert an instance of RowData to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

