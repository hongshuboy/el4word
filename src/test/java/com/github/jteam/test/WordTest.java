package com.github.jteam.test;

import com.github.hongshuboy.core.Office;
import com.github.hongshuboy.office.Word;
import com.github.hongshuboy.office.impl.FileSink;
import com.github.hongshuboy.office.impl.FileSource;
import org.junit.Test;

public class WordTest {

    /**
     * 测试
     * 测试代码使用到的资源文件在test/java/resources中
     */
    @Test
    public void test1() {
        Word word = Office.getWordHandler();
        word.addSource(new FileSource("D:\\poi\\poi_read.docx")); //must be the first step
        word.addConfig(new MyConfig());
        word.transformParagraphs();
        word.transformTables();
        word.transformPictures();
        word.addSink(new FileSink("D:\\poi\\poi_write.docx"));
        word.save();    //must be the final step
        System.out.println("transform success!");
    }
}
