package com.jm.flink.sql.enums;

public enum ConnectorIdentifiers {

    /**
     * ConnectorIdentifiers
     */
    filesystem("filesystem"),
    mysql("mysql"),
    sqlserver("sqlserver"),
    oracle("oracle"),
    clickhouse("clickhouse"),
    kafka("kafka"),
    upsertkafka("upsert-kafka"),
    postgresql("postgresql"),
    redis("redis"),
    kudu("kudu"),
    hana("hana"),
    ignite("ignite"),
    console("console"),
    hbase14("hbase-1.4"),
    hbase22("hbase-2.2"),
    mysqlcdc("mysql-cdc"),
    oraclecdc("oracle-cdc"),
    postgrescdc("postgres-cdc"),
    sqlservercdc("sqlserver-cdc"),
    db2("db2"),
    mongodbcdc("mongodb-cdc"),
    hudi("hudi"),
    tidbcdc("tidb-cdc");

    ConnectorIdentifiers(String identifier) {
        this.identifier = identifier;
    }

    private final String identifier;

    public static boolean isValid(String identifier) {
        for (ConnectorIdentifiers connectorIdentifiers : ConnectorIdentifiers.values()) {
            if (connectorIdentifiers.getIdentifier().equalsIgnoreCase(identifier)) {
                return true;
            }
        }

        return false;
    }

    public String getIdentifier() {
        return identifier;
    }

}
