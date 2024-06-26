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

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gets or Sets ResultType
 */
@JsonAdapter(ResultType.Adapter.class)
public enum ResultType {

    NOT_READY("NOT_READY"),

    PAYLOAD("PAYLOAD"),

    EOS("EOS");

    private String value;

    ResultType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ResultType fromValue(String value) {
        for (ResultType b : ResultType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<ResultType> {
        @Override
        public void write(final JsonWriter jsonWriter, final ResultType enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public ResultType read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return ResultType.fromValue(value);
        }
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        String value = jsonElement.getAsString();
        ResultType.fromValue(value);
    }
}

