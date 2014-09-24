package com.shurpo.financialassistant.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.*;

public class SelectionBuilder {

    private String table = null;
    private Map<String, String> projectionMap = new HashMap<String, String>();
    private StringBuilder selection = new StringBuilder();
    private List<String> selectionArgs = new ArrayList<String>();

    public SelectionBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException("Valid selection required when including arguments=");
            }
            return this;
        }
        if (this.selection.length() > 0) {
            this.selection.append(" AND ");
        }

        this.selection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(this.selectionArgs, selectionArgs);
        }

        return this;
    }

    public SelectionBuilder table(String table) {
        this.table = table;
        return this;
    }

    private void assertTable() {
        if (this.table == null) {
            throw new IllegalArgumentException("Table not specified");
        }
    }

    public SelectionBuilder mapToTable(String column, String table) {
        projectionMap.put(column, table);
        return this;
    }

    public SelectionBuilder map(String fromColumn, String toClause){
        projectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    public String getSelection(){
        return this.selection.toString();
    }

    public String[] getSelectionArgs(){
        return this.selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    private void mapColumns(String[] columns){
        for(int i = 0; i < columns.length; i++){
            String target = projectionMap.get(i);
            if(target != null){
                columns[i] = target;
            }
        }
    }

    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy){
       return query(db, columns, null, null, orderBy, null);
    }

    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy, String having, String orderBy, String limit){
        assertTable();
        if (columns != null){
            mapColumns(columns);
        }
        return db.query(table, columns, getSelection(), getSelectionArgs(), groupBy, having, orderBy, limit);
    }

    public int update(SQLiteDatabase db, ContentValues values){
        assertTable();
        return db.update(table, values, getSelection(), getSelectionArgs());
    }

    public int delete(SQLiteDatabase db){
        assertTable();
        return db.delete(table, getSelection(), getSelectionArgs());
    }


}
