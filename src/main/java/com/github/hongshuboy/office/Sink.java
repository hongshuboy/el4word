package com.github.hongshuboy.office;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * @author hongshuboy
 */
public interface Sink {
    /**
     * 存储转换好的文档
     *
     * @param document
     */
    void storeDocument(XWPFDocument document);
}
