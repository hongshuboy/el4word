package com.github.hongshuboy.office.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordImpl {
    private String inPath;
    private XWPFDocument document;

    public String getInPath() {
        return inPath;
    }

    public void setInPath(String inPath) {
        this.inPath = inPath;
    }

    public WordImpl() {
    }
    public WordImpl(String inPath) {
        this.inPath = inPath;
    }
}
