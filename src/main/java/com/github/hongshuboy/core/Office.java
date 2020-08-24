package com.github.hongshuboy.core;

import com.github.hongshuboy.office.Word;
import com.github.hongshuboy.office.impl.WordImpl;
/**
 * 查看使用样例WordTest
 */
public class Office {
    public static Word getWordHandler(){
        return new WordImpl();
    }
}
