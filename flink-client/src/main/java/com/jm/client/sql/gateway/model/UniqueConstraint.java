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
 * UniqueConstraint
 */

public class UniqueConstraint {
  public static final String SERIALIZED_NAME_COLUMNS = "columns";
  @SerializedName(SERIALIZED_NAME_COLUMNS)
  private List<String> columns;

  public static final String SERIALIZED_NAME_ENFORCED = "enforced";
  @SerializedName(SERIALIZED_NAME_ENFORCED)
  private Boolean enforced;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private ConstraintType type;

  public UniqueConstraint() {
  }

  public UniqueConstraint columns(List<String> columns) {
    this.columns = columns;
    return this;
  }

  public UniqueConstraint addColumnsItem(String columnsItem) {
    if (this.columns == null) {
      this.columns = new ArrayList<>();
    }
    this.columns.add(columnsItem);
    return this;
  }

   /**
   * Get columns
   * @return columns
  **/
  //@javax.annotation.Nullable
  public List<String> getColumns() {
    return columns;
  }

  public void setColumns(List<String> columns) {
    this.columns = columns;
  }


  public UniqueConstraint enforced(Boolean enforced) {
    this.enforced = enforced;
    return this;
  }

   /**
   * Get enforced
   * @return enforced
  **/
  //@javax.annotation.Nullable
  public Boolean getEnforced() {
    return enforced;
  }

  public void setEnforced(Boolean enforced) {
    this.enforced = enforced;
  }


  public UniqueConstraint name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  //@javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public UniqueConstraint type(ConstraintType type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  //@javax.annotation.Nullable
  public ConstraintType getType() {
    return type;
  }

  public void setType(ConstraintType type) {
    this.type = type;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UniqueConstraint uniqueConstraint = (UniqueConstraint) o;
    return Objects.equals(this.columns, uniqueConstraint.columns) &&
        Objects.equals(this.enforced, uniqueConstraint.enforced) &&
        Objects.equals(this.name, uniqueConstraint.name) &&
        Objects.equals(this.type, uniqueConstraint.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(columns, enforced, name, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UniqueConstraint {\n");
    sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
    sb.append("    enforced: ").append(toIndentedString(enforced)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
    openapiFields.add("columns");
    openapiFields.add("enforced");
    openapiFields.add("name");
    openapiFields.add("type");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Element and throws an exception if issues found
  *
  * @param jsonElement JSON Element
  * @throws IOException if the JSON Element is invalid with respect to UniqueConstraint
  */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!UniqueConstraint.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in UniqueConstraint is not found in the empty JSON string", UniqueConstraint.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!UniqueConstraint.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UniqueConstraint` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // ensure the optional json data is an array if present
      if (jsonObj.get("columns") != null && !jsonObj.get("columns").isJsonNull() && !jsonObj.get("columns").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `columns` to be an array in the JSON string but got `%s`", jsonObj.get("columns").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      // validate the optional field `type`
      if (jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull()) {
        ConstraintType.validateJsonElement(jsonObj.get("type"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UniqueConstraint.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UniqueConstraint' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UniqueConstraint> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UniqueConstraint.class));

       return (TypeAdapter<T>) new TypeAdapter<UniqueConstraint>() {
           @Override
           public void write(JsonWriter out, UniqueConstraint value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UniqueConstraint read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of UniqueConstraint given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of UniqueConstraint
  * @throws IOException if the JSON string is invalid with respect to UniqueConstraint
  */
  public static UniqueConstraint fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UniqueConstraint.class);
  }

 /**
  * Convert an instance of UniqueConstraint to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

