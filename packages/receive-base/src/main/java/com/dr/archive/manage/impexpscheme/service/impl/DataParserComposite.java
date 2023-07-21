package com.dr.archive.manage.impexpscheme.service.impl;

import com.dr.archive.formMap.service.DataParser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据处理集合
 *
 * @author dr
 */
public class DataParserComposite implements DataParser {

    final Collection<DataParser> dataParsers;

    public DataParserComposite(Collection<DataParser> dataParsers) {
        this.dataParsers = dataParsers;
    }


    @Override
    public boolean canHandle(String mine) {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.canHandle(mine)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getFileSuffix(String mine) {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.canHandle(mine)) {
                return dataParser.getFileSuffix(mine);
            }
        }
        return null;
    }

    @Override
    public String[] readKeys(Path source, String mine) throws IOException {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.canHandle(mine)) {
                return dataParser.readKeys(source, mine);
            }
        }
        return new String[]{};
    }

    @Override
    public Iterator<Map<String, Object>> readData(Path source, String mine) throws IOException {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.canHandle(mine)) {
                return dataParser.readData(source, mine);
            }
        }
        return Collections.emptyIterator();
    }

    @Override
    public void writeData(String[] keys, Iterator<Map<String, Object>> data, String mine, Path target) throws IOException {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.canHandle(mine)) {
                dataParser.writeData(keys, data, mine, target);
                return;
            }
        }
    }
}
