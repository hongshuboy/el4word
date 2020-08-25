package com.github.hongshuboy.office.impl;

import com.github.hongshuboy.office.Source;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 从磁盘中加载Word模板文件，用于转换
 *
 * @author hongshuboy
 */
public class FileSource implements Source {
    private String docPath;

    private FileSource() {
    }

    public FileSource(String docPath) {
        this.docPath = docPath;
    }

    @Override
    public XWPFDocument getDocument() {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(docPath))) {
            return new XWPFDocument(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
