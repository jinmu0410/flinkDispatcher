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
 * Gets or Sets LogicalTypeRoot
 */
@JsonAdapter(LogicalTypeRoot.Adapter.class)
public enum LogicalTypeRoot {

    CHAR("CHAR"),

    VARCHAR("VARCHAR"),

    BOOLEAN("BOOLEAN"),

    BINARY("BINARY"),

    VARBINARY("VARBINARY"),

    DECIMAL("DECIMAL"),

    TINYINT("TINYINT"),

    SMALLINT("SMALLINT"),

    INTEGER("INTEGER"),

    BIGINT("BIGINT"),

    FLOAT("FLOAT"),

    DOUBLE("DOUBLE"),

    DATE("DATE"),

    TIME_WITHOUT_TIME_ZONE("TIME_WITHOUT_TIME_ZONE"),

    TIMESTAMP_WITHOUT_TIME_ZONE("TIMESTAMP_WITHOUT_TIME_ZONE"),

    TIMESTAMP_WITH_TIME_ZONE("TIMESTAMP_WITH_TIME_ZONE"),

    TIMESTAMP_WITH_LOCAL_TIME_ZONE("TIMESTAMP_WITH_LOCAL_TIME_ZONE"),

    INTERVAL_YEAR_MONTH("INTERVAL_YEAR_MONTH"),

    INTERVAL_DAY_TIME("INTERVAL_DAY_TIME"),

    ARRAY("ARRAY"),

    MULTISET("MULTISET"),

    MAP("MAP"),

    ROW("ROW"),

    DISTINCT_TYPE("DISTINCT_TYPE"),

    STRUCTURED_TYPE("STRUCTURED_TYPE"),

    NULL("NULL"),

    RAW("RAW"),

    SYMBOL("SYMBOL"),

    UNRESOLVED("UNRESOLVED");

    private String value;

    LogicalTypeRoot(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static LogicalTypeRoot fromValue(String value) {
        for (LogicalTypeRoot b : LogicalTypeRoot.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<LogicalTypeRoot> {
        @Override
        public void write(final JsonWriter jsonWriter, final LogicalTypeRoot enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public LogicalTypeRoot read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return LogicalTypeRoot.fromValue(value);
        }
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        String value = jsonElement.getAsString();
        LogicalTypeRoot.fromValue(value);
    }
}

