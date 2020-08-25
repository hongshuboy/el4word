package com.github.hongshuboy.office.impl;

import com.github.hongshuboy.office.Sink;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 将文档写出到磁盘
 *
 * @author hongshuboy
 */
public class FileSink implements Sink {
    private String outPath;

    private FileSink() {
    }

    public FileSink(String outPath) {
        this.outPath = outPath;
    }

    @Override
    public void storeDocument(XWPFDocument document) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outPath))) {
            document.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
