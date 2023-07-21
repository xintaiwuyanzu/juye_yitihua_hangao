package com.dr.archive.formMap.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;

/**
 * 封装流相关数据解析方法
 *
 * @author dr
 */
public abstract class AbstractStreamDataParser implements DataParser {
    @Override
    public String[] readKeys(Path source, String mine) throws IOException {
        try (InputStream inputStream = Files.newInputStream(source, StandardOpenOption.READ)) {
            return readKeys(inputStream, mine);
        }
    }

    /**
     * 读取指定文件的一条数据的所有key
     *
     * @param source
     * @param mine
     * @return
     * @throws IOException
     */
    protected abstract String[] readKeys(InputStream source, String mine) throws IOException;

    @Override
    public Iterator<Map<String, Object>> readData(Path source, String mine) throws IOException {
        try (InputStream inputStream = Files.newInputStream(source, StandardOpenOption.READ)) {
            return readData(inputStream, mine);
        }
    }

    /**
     * 批量读取数据
     *
     * @param source
     * @param mine
     * @return
     * @throws IOException
     */
    protected abstract Iterator<Map<String, Object>> readData(InputStream source, String mine) throws IOException;

    @Override
    public void writeData(String[] keys, Iterator<Map<String, Object>> data, String mine, Path source) throws IOException {
        try (OutputStream ops = Files.newOutputStream(source, StandardOpenOption.WRITE)) {
            writeData(keys, data, mine, ops);
        }
    }




    /**
     * 批量写数据
     *
     * @param keys
     * @param data
     * @param mine
     * @param target
     * @throws IOException
     */
    protected abstract void writeData(String[] keys, Iterator<Map<String, Object>> data, String mine, OutputStream target) throws IOException;

}
