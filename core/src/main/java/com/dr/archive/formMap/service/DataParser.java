package com.dr.archive.formMap.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

/**
 * 用来解析和输出数据
 * <p>
 * 导入导出
 * <p>
 * 备份和恢复使用
 *
 * @author dr
 */
public interface DataParser {
    /**
     * 能否处理指定的文件类型
     *
     * @param mine
     * @return
     */
    boolean canHandle(String mine);

    /**
     * 获取文件后缀
     *
     * @param mine
     * @return
     */
    String getFileSuffix(String mine);


    /**
     * 从指定的路径解析文件的key
     *
     * @param source
     * @param mine
     * @return
     * @throws IOException
     */
    String[] readKeys(Path source, String mine) throws IOException;

    /**
     * 从指定的路径读取数据
     *
     * @param source
     * @param mine
     * @return
     * @throws IOException
     */
    Iterator<Map<String, Object>> readData(Path source, String mine) throws IOException;

    /**
     * 往指定的路径写文件
     *
     * @param keys
     * @param data
     * @param mine
     * @param source
     * @throws IOException
     */
    void writeData(String[] keys, Iterator<Map<String, Object>> data, String mine, Path source) throws IOException;

}
