package com.github.hongshuboy.office;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Sink
 *
 * @author hongshuboy
 * @see com.github.hongshuboy.office.impl.FileSink
 */
public interface Sink {
    /**
     * 存储转换好的文档
     *
     * @param document document
     */
    void storeDocument(XWPFDocument document);
}
