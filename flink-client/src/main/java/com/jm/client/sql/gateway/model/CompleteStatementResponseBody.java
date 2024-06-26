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
 * CompleteStatementResponseBody
 */
public class CompleteStatementResponseBody {
    public static final String SERIALIZED_NAME_CANDIDATES = "candidates";
    @SerializedName(SERIALIZED_NAME_CANDIDATES)
    private List<String> candidates;

    public CompleteStatementResponseBody() {
    }

    public CompleteStatementResponseBody candidates(List<String> candidates) {
        this.candidates = candidates;
        return this;
    }

    public CompleteStatementResponseBody addCandidatesItem(String candidatesItem) {
        if (this.candidates == null) {
            this.candidates = new ArrayList<>();
        }
        this.candidates.add(candidatesItem);
        return this;
    }

    /**
     * Get candidates
     *
     * @return candidates
     **/
    //@javax.annotation.Nullable
    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompleteStatementResponseBody completeStatementResponseBody = (CompleteStatementResponseBody) o;
        return Objects.equals(this.candidates, completeStatementResponseBody.candidates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidates);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CompleteStatementResponseBody {\n");
        sb.append("    candidates: ").append(toIndentedString(candidates)).append("\n");
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
        openapiFields.add("candidates");

        // a set of required properties/fields (JSON key names)
        openapiRequiredFields = new HashSet<String>();
    }

    /**
     * Validates the JSON Element and throws an exception if issues found
     *
     * @param jsonElement JSON Element
     * @throws IOException if the JSON Element is invalid with respect to CompleteStatementResponseBody
     */
    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null) {
            if (!CompleteStatementResponseBody.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
                throw new IllegalArgumentException(String.format("The required field(s) %s in CompleteStatementResponseBody is not found in the empty JSON string", CompleteStatementResponseBody.openapiRequiredFields.toString()));
            }
        }

        Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
        // check to see if the JSON string contains additional fields
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (!CompleteStatementResponseBody.openapiFields.contains(entry.getKey())) {
                throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `CompleteStatementResponseBody` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
            }
        }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        // ensure the optional json data is an array if present
        if (jsonObj.get("candidates") != null && !jsonObj.get("candidates").isJsonNull() && !jsonObj.get("candidates").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `candidates` to be an array in the JSON string but got `%s`", jsonObj.get("candidates").toString()));
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!CompleteStatementResponseBody.class.isAssignableFrom(type.getRawType())) {
                return null; // this class only serializes 'CompleteStatementResponseBody' and its subtypes
            }
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
            final TypeAdapter<CompleteStatementResponseBody> thisAdapter
                    = gson.getDelegateAdapter(this, TypeToken.get(CompleteStatementResponseBody.class));

            return (TypeAdapter<T>) new TypeAdapter<CompleteStatementResponseBody>() {
                @Override
                public void write(JsonWriter out, CompleteStatementResponseBody value) throws IOException {
                    JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                    elementAdapter.write(out, obj);
                }

                @Override
                public CompleteStatementResponseBody read(JsonReader in) throws IOException {
                    JsonElement jsonElement = elementAdapter.read(in);
                    validateJsonElement(jsonElement);
                    return thisAdapter.fromJsonTree(jsonElement);
                }

            }.nullSafe();
        }
    }

    /**
     * Create an instance of CompleteStatementResponseBody given an JSON string
     *
     * @param jsonString JSON string
     * @return An instance of CompleteStatementResponseBody
     * @throws IOException if the JSON string is invalid with respect to CompleteStatementResponseBody
     */
    public static CompleteStatementResponseBody fromJson(String jsonString) throws IOException {
        return JSON.getGson().fromJson(jsonString, CompleteStatementResponseBody.class);
    }

    /**
     * Convert an instance of CompleteStatementResponseBody to an JSON string
     *
     * @return JSON string
     */
    public String toJson() {
        return JSON.getGson().toJson(this);
    }
}

