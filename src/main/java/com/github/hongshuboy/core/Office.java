package com.github.hongshuboy.core;

import com.github.hongshuboy.office.Word;
import com.github.hongshuboy.office.impl.WordImpl;
/**
 * Word word = Office.getWordHandler();
 *
 * word.addSource(new FileSource("D:\\poi\\poi_template.docx")); //must be the first step
 * word.addConfig(new MyConfig());
 *
 * word.transformParagraphs();
 * word.transformTables();
 * word.transformPictures();
 *
 * word.addSink(new FileSink("D:\\poi\\poi_out.docx"));
 *
 * word.save();    //must be the final step
 * System.out.println("transform success!");
 */
public class Office {
    public static Word getWordHandler(){
        return new WordImpl();
    }
}
