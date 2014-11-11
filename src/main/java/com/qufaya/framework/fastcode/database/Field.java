package com.qufaya.framework.fastcode.database;

import com.qufaya.framework.fastcode.template.Utils;

public class Field {

    private String name;

    private JavaType type;

    private String comment;

    public Field() {
    }

    public Field(Column column) {
        this.name = Utils.columnNameToFieldName(column.getName());
        this.comment = column.getComment();
        this.type = JavaType.fromSqlType(column.getType());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public JavaType getType() {
        return type;
    }

    public void setType(JavaType type) {
        this.type = type;
    }

}
