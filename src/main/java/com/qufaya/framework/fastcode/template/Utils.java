package com.qufaya.framework.fastcode.template;

import java.util.ArrayList;
import java.util.List;

import com.qufaya.framework.fastcode.database.Column;
import com.qufaya.framework.fastcode.database.Field;
import com.qufaya.framework.fastcode.database.Index;
import com.qufaya.framework.fastcode.database.JavaType;
import com.qufaya.framework.fastcode.database.PrimaryKey;
import com.qufaya.framework.fastcode.database.SqlType;
import com.qufaya.framework.fastcode.database.Table;

public class Utils {

    /**
     * 
     */
    private static final String ENCODING = "UTF-8";

    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    public static String columnNameToFieldName(String name) {
        int l = name.length();
        StringBuilder stringBuilder = new StringBuilder(l);
        boolean flag = false;
        for (int i = 0; i < l; i++) {
            char ch = name.charAt(i);

            if (ch == '_') {
                flag = true;
                continue;
            }

            if (flag) {
                stringBuilder.append(Character.toUpperCase(ch));
                flag = false;
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }

    public static String fieldNameToColumnName(String name) {
        int l = name.length();
        StringBuilder stringBuilder = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            char ch = name.charAt(i);

            if (Character.isUpperCase(ch)) {
                stringBuilder.append('_').append(Character.toLowerCase(ch));
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }

    public static String tableNameToClassName(String name) {
        return capitalize(columnNameToFieldName(name));
    }

    public static JavaType columnTypeToJavaType(SqlType type) {
        return JavaType.fromSqlType(type);
    }

    public static String javaTypeToColumnType(String type) {
        return null;
    }

    public static String getterName(String name, JavaType javaType) {
        if (javaType == JavaType.BOOLEAN) {
            return "is" + capitalize(name);
        } else {
            return "get" + capitalize(name);
        }
    }

    public static String setterName(String name) {
        return "set" + capitalize(name);
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding
     * underscore.
     * 
     * @param name the string containing original name
     * @return the converted name
     */
    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    public static Field columnToField(Column column) {
        return new Field(column);
    }

    public static List<Field> columnsToFields(List<Column> columns) {
        List<Field> fields = new ArrayList<Field>(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            fields.add(columnToField(columns.get(i)));
        }
        return fields;
    }

    public static String getLiteFieldsString(Table table) {
        List<Column> columns = table.getColumns();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (column.isAutoIncrement()) {
                continue;
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(column.getName()).append("`");
        }
        return stringBuilder.toString();
    }

    public static String getLiteParamsString(Table table) {
        List<Column> columns = table.getColumns();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (column.isAutoIncrement()) {
                continue;
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(":").append(column.getName());
        }
        return stringBuilder.toString();
    }

    public static String getAutoIncrementFieldsString(Table table) {
        List<Column> columns = table.getColumns();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (!column.isAutoIncrement()) {
                continue;
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(column.getName()).append("`");
        }
        return stringBuilder.toString();
    }

    public static String getPrimaryKeyName(PrimaryKey primaryKey) {
        List<Column> columns = primaryKey.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (stringBuilder.length() > 0) {
                stringBuilder.append("_");
            }
            stringBuilder.append(column.getName().replace("_", ""));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String getMethodName(PrimaryKey primaryKey) {
        List<Column> columns = primaryKey.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (stringBuilder.length() > 0) {
                stringBuilder.append("And");
            }
            stringBuilder.append(tableNameToClassName(column.getName()));
        }
        return stringBuilder.toString();
    }

    public static String getWhereByPrimaryKey(PrimaryKey primaryKey) {
        List<Column> columns = primaryKey.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append("`").append(column.getName()).append("`=:")
                    .append(column.getName());
        }
        return stringBuilder.toString();
    }

    public static String getIndexName(Index index) {
        List<Column> columns = index.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (stringBuilder.length() > 0) {
                stringBuilder.append("_");
            }
            stringBuilder.append(column.getName().replace("_", ""));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String getMethodName(Index index) {
        List<Column> columns = index.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (stringBuilder.length() > 0) {
                stringBuilder.append("And");
            }
            stringBuilder.append(tableNameToClassName(column.getName()));
        }
        return stringBuilder.toString();
    }

    public static String getWhereByIndex(Index index) {
        List<Column> columns = index.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append("`").append(column.getName()).append("`=:")
                    .append(column.getName());
        }
        return stringBuilder.toString();
    }

    public String getParamList(Index index) {
        List<Column> columns = index.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }

            String fieldName = columnNameToFieldName(column.getName());
            String fieldType = JavaType.fromSqlType(column.getType()).getName();
            stringBuilder.append(fieldType).append(" ").append(fieldName);
        }
        return stringBuilder.toString();
    }

    public String getParamList(PrimaryKey index) {
        List<Column> columns = index.getMembers();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }

            String fieldName = columnNameToFieldName(column.getName());
            String fieldType = JavaType.fromSqlType(column.getType()).getName();
            stringBuilder.append(fieldType).append(" ").append(fieldName);
        }
        return stringBuilder.toString();
    }

    public String getUpdateStatement(Table table) {
        List<Column> columns = table.getColumns();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {

            if (i == 0) {
                stringBuilder.append("set ");
            }

            Column column = columns.get(i);
            if (column.isAutoIncrement()) {
                continue;
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(column.getName()).append("`").append("=:")
                    .append(column.getName());
        }
        return stringBuilder.toString();
    }
}
