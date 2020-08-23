package com.github.hongshuboy.office;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface Source {
    /**
     * 获取doc文档
     */
    XWPFDocument getDocument();
}
