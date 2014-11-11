package com.qufaya.framework.fastcode.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableFactory {

    private static final TableFactory instance = new TableFactory();

    private TableFactory() {
    }

    public static TableFactory getInstance() {
        return instance;
    }

    public Table createTable(Connection connection, String tableName) throws SQLException {

        Table table = new Table();
        table.setName(tableName);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        Map<String, Column> columnsMap = new HashMap<String, Column>();
        List<Column> columnsList = new ArrayList<Column>();

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            int dataType = columns.getInt("DATA_TYPE");
            String autoincrement = columns.getString("IS_AUTOINCREMENT");

            String comment = columns.getString("REMARKS");

            Column column = new Column();
            column.setName(columnName);
            column.setComment(comment);
            column.setType(SqlType.fromDataType(dataType));
            column.setAutoIncrement("YES".equalsIgnoreCase(autoincrement));

            columnsList.add(column);
            columnsMap.put(columnName, column);
        }

        table.setColumns(columnsList);

        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()) {
            String columnName = primaryKeys.getString("COLUMN_NAME");

            ArrayList<Column> arrayList = new ArrayList<Column>();

            Column column = columnsMap.get(columnName);

            arrayList.add(column);

            PrimaryKey key = new PrimaryKey(arrayList);
            table.setPrimaryKey(key);
        }

        ResultSet indexInfo = metaData.getIndexInfo(null, null, tableName, true, false);
        ArrayList<Index> indexesList = new ArrayList<Index>();

        Map<String, List<IndexItem>> indexMap = new LinkedHashMap<String, List<IndexItem>>();
        while (indexInfo.next()) {
            String indexName = indexInfo.getString("INDEX_NAME");

            if ("primary".equalsIgnoreCase(indexName) || indexName == null
                    || indexName.toLowerCase().contains("primary")) {
                continue;
            }

            String columnName = indexInfo.getString("COLUMN_NAME");
            int position = indexInfo.getInt("ORDINAL_POSITION");
            boolean unique = !indexInfo.getBoolean("NON_UNIQUE");

            IndexItem indexItem = new IndexItem();
            indexItem.setIndexName(indexName);
            indexItem.setColumnName(columnName);
            indexItem.setPosition(position);
            indexItem.setUnique(unique);

            List<IndexItem> items = indexMap.get(indexName);
            if (items == null) {
                items = new ArrayList<IndexItem>();
                indexMap.put(indexName, items);
            }
            items.add(indexItem);
        }

        for (Map.Entry<String, List<IndexItem>> entry : indexMap.entrySet()) {
            Collections.sort(entry.getValue());

            IndexItem indexItem = entry.getValue().get(0);
            List<Column> members = new ArrayList<Column>();
            for (IndexItem item : entry.getValue()) {
                Column column = columnsMap.get(item.getColumnName());
                members.add(column);
            }

            Index index = new Index(indexItem.getIndexName(), members, indexItem.isUnique());

            indexesList.add(index);
        }

        table.setIndexes(indexesList);
        return table;

    }

    class IndexItem implements Comparable<IndexItem> {

        private String indexName;

        private String columnName;

        private int position;

        private boolean unique;

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

        public int compareTo(IndexItem o) {
            return Integer.compare(this.position, o.position);
        }

    }
}
