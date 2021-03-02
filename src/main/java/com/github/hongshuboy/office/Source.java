package com.github.hongshuboy.office;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Source
 *
 * @author hongshuboy
 * @see com.github.hongshuboy.office.impl.FileSource
 */
public interface Source {
    /**
     * 获取doc文档
     */
    XWPFDocument getDocument();
}
