package com.dr.archive.manage.impexpscheme.service.impl;

import com.dr.archive.formMap.service.DataParser;
import com.healthmarketscience.jackcess.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * access操作类
 * TODO 这个实现类有点麻烦
 *
 * @author dr
 */
@Component
public class AccessDataParser implements DataParser {
    final MediaType mediaType = MediaType.parseMediaType("application/x-msaccess");

    @Value("${archive.common.access.tableName:archive}")
    String tableName = "archive";

    @Override
    public boolean canHandle(String mine) {
        return mediaType.includes(MediaType.parseMediaType(mine));
    }

    @Override
    public String getFileSuffix(String mine) {
        return "accdb";
    }

    @Override
    public String[] readKeys(Path source, String mine) throws IOException {
        try (Database database = new DatabaseBuilder(source).setFileFormat(Database.FileFormat.V2010).create()) {
            Set<String> tableNames = database.getTableNames();
            //TODO 这里用默认的表名
            String tableName = this.tableName;
            if (tableNames.size() == 1) {
                tableName = tableNames.iterator().next();
            }
            Table table = database.getTable(tableName);
            return table.getColumns().stream().map(Column::getName).toArray(String[]::new);
        }
    }


    @Override
    public Iterator<Map<String, Object>> readData(Path source, String mine) throws IOException {
        //TODO database获取表名失败
        try (Database database = new DatabaseBuilder(source).setFileFormat(Database.FileFormat.V2010).create()) {
            Set<String> tableNames = database.getTableNames();
            //TODO 这里用默认的表名
            String tableName = this.tableName;
            if (tableNames.size() == 1) {
                tableName = tableNames.iterator().next();
            }
            Table table = database.getTable(tableName);
            return new TableIterator(database, table);
        }
    }

    @Override
    public void writeData(String[] keys, Iterator<Map<String, Object>> data, String mine, Path target) throws IOException {
        try (Database database = new DatabaseBuilder(target).setFileFormat(Database.FileFormat.V2010).create()) {
            TableBuilder builder = new TableBuilder(tableName);
            for (String key : keys) {
                builder.addColumn(new ColumnBuilder(key, DataType.TEXT));
            }
            Table table = builder.toTable(database);
            while (data.hasNext()) {
                Map<String, Object> map = data.next();
                table.addRowFromMap(map);
            }
        }
    }

    static class TableIterator implements Iterator<Map<String, Object>>, Closeable {
        final Database database;
        final Table table;
        final Iterator<Row> iterator;
        final String[] keys;

        public TableIterator(Database database, Table table) {
            this.database = database;
            this.table = table;
            iterator = table.iterator();
            keys = table.getColumns().stream().map(Column::getName).toArray(String[]::new);
        }

        @Override
        public void close() throws IOException {
            database.close();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map<String, Object> next() {
            Row row = iterator.next();
            Map<String, Object> map = new HashMap<>(row.size());
            for (String key : keys) {
                map.put(key, row.getString(key));
            }
            return map;
        }
    }
}
