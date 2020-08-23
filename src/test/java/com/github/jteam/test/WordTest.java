package com.github.jteam.test;

import com.github.hongshuboy.core.Office;
import com.github.hongshuboy.office.Word;
import com.github.hongshuboy.office.impl.FileSink;
import com.github.hongshuboy.office.impl.FileSource;
import org.junit.Test;

public class WordTest {

    @Test
    public void test1() {
        Word word = Office.getWordHandler();
        word.addSource(new FileSource("D:\\poi\\poi_read.docx"));
        word.addConfig(new MyConfig());
        word.transformParagraphs();
        word.transformTables();
        word.addSink(new FileSink("D:\\poi\\-poi_write.docx"));
        word.save();
        System.out.println("转换成功");
    }
}
