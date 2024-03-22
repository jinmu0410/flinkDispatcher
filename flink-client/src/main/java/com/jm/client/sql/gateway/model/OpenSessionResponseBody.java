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
 * OpenSessionResponseBody
 */

public class OpenSessionResponseBody {
  public static final String SERIALIZED_NAME_SESSION_HANDLE = "sessionHandle";
  @SerializedName(SERIALIZED_NAME_SESSION_HANDLE)
  private String sessionHandle;

  public OpenSessionResponseBody() {
  }

  public OpenSessionResponseBody sessionHandle(String sessionHandle) {
    this.sessionHandle = sessionHandle;
    return this;
  }

   /**
   * Get sessionHandle
   * @return sessionHandle
  **/
  //@javax.annotation.Nullable
  public String getSessionHandle() {
    return sessionHandle;
  }

  public void setSessionHandle(String sessionHandle) {
    this.sessionHandle = sessionHandle;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OpenSessionResponseBody openSessionResponseBody = (OpenSessionResponseBody) o;
    return Objects.equals(this.sessionHandle, openSessionResponseBody.sessionHandle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionHandle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OpenSessionResponseBody {\n");
    sb.append("    sessionHandle: ").append(toIndentedString(sessionHandle)).append("\n");
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
    openapiFields.add("sessionHandle");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Element and throws an exception if issues found
  *
  * @param jsonElement JSON Element
  * @throws IOException if the JSON Element is invalid with respect to OpenSessionResponseBody
  */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!OpenSessionResponseBody.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in OpenSessionResponseBody is not found in the empty JSON string", OpenSessionResponseBody.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!OpenSessionResponseBody.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `OpenSessionResponseBody` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("sessionHandle") != null && !jsonObj.get("sessionHandle").isJsonNull()) && !jsonObj.get("sessionHandle").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `sessionHandle` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sessionHandle").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!OpenSessionResponseBody.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'OpenSessionResponseBody' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<OpenSessionResponseBody> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(OpenSessionResponseBody.class));

       return (TypeAdapter<T>) new TypeAdapter<OpenSessionResponseBody>() {
           @Override
           public void write(JsonWriter out, OpenSessionResponseBody value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public OpenSessionResponseBody read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of OpenSessionResponseBody given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of OpenSessionResponseBody
  * @throws IOException if the JSON string is invalid with respect to OpenSessionResponseBody
  */
  public static OpenSessionResponseBody fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, OpenSessionResponseBody.class);
  }

 /**
  * Convert an instance of OpenSessionResponseBody to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

