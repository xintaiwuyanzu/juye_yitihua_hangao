package com.dr.archive.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 中文分词工具
 *
 * @author dr
 */
public class AnalyzerUtils {

    /**
     * 功能描述: 进行粗粒度分词，match分词自动细粒度分词不适合
     *
     * @param keyWord
     * @return :
     * @author : tzl
     * @date : 2020/7/7 11:04
     */
    public static List<String> separateWord(String keyWord) {
        List<String> keyList = new ArrayList<>();
        try {
            //创建分词对象
            Analyzer anal = new IKAnalyzer(true);
            StringReader reader = new StringReader(keyWord);
            //分词
            TokenStream ts = anal.tokenStream("", reader);
            ts.reset();
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while (ts.incrementToken()) {
                keyList.add(term.toString());
            }
            ts.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyList;
    }

}
