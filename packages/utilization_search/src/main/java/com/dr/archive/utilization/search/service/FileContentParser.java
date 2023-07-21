package com.dr.archive.utilization.search.service;

import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

/**
 * 解析文件内容并转换为string
 *
 * @author dr
 */
public interface FileContentParser extends Ordered {
    /**
     * 后面追加，国产配体类型可能与正式媒体类型相同，但是后缀不同
     * 能否读取指定的格式
     *
     * @param mediaType 媒体类型
     * @param suffix    文件名称后缀
     * @return
     */
    default boolean accept(MediaType mediaType, String suffix) {
        return accept(mediaType);
    }

    /**
     * 能否读取指定的格式
     *
     * @param mediaType
     * @return
     */
    boolean accept(MediaType mediaType);

    /**
     * 执行数据读取方法
     *
     * @param stream
     * @return
     * @throws IOException
     */
    String read(InputStream stream) throws IOException;

    /**
     * 默认排序室最低
     *
     * @return
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
