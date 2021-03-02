package com.github.hongshuboy.usermodel;

import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongshuboy
 * 2020-08-24 10:34
 * @see PictureType#PICTURE_TYPE_EMF
 * @see PictureType#PICTURE_TYPE_WMF
 * @see PictureType#PICTURE_TYPE_PICT
 * @see PictureType#PICTURE_TYPE_JPEG
 * @see PictureType#PICTURE_TYPE_PNG
 * @see PictureType#PICTURE_TYPE_DIB
 */
public class WordPicture {
    /**
     * bytes will convert to inputStream
     */
    private byte[] bytes;
    /**
     * unnecessary field
     */
    private String fileName;

    /**
     * width pixels
     */
    private int widthPx;

    /**
     * height pixels
     */
    private int heightPx;

    /**
     * @see PictureType#PICTURE_TYPE_EMF
     * @see PictureType#PICTURE_TYPE_WMF
     * @see PictureType#PICTURE_TYPE_PICT
     * @see PictureType#PICTURE_TYPE_JPEG
     * @see PictureType#PICTURE_TYPE_PNG
     * @see PictureType#PICTURE_TYPE_DIB
     */
    private int pictureType;

    private WordPicture() {
    }

    /**
     * 构造器，用户操作此类只需要使用构造器
     *
     * @param inputStream 图片的输入流，将被自动关闭
     * @param fileName    图片的文件名，可随意输入
     * @param widthPx     图片的宽度，单位像素
     * @param heightPx    图片的高度，单位像素
     * @param pictureType 图片的类型，建议使用{@link PictureType}
     */
    public WordPicture(InputStream inputStream, String fileName, int widthPx, int heightPx, int pictureType) {
        setBytes(inputStream);
        this.fileName = fileName;
        this.widthPx = widthPx;
        this.heightPx = heightPx;
        this.pictureType = pictureType;
    }

    public static WordPicture of(InputStream inputStream, String fileName, int widthPx, int heightPx, int pictureType) {
        return new WordPicture(inputStream, fileName, widthPx, heightPx, pictureType);
    }

    public void setBytes(InputStream inputStream) {
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public String getFileName() {
        return fileName;
    }

    public int getWidthPx() {
        return widthPx;
    }

    public int getHeightPx() {
        return heightPx;
    }

    public int getPictureType() {
        return pictureType;
    }
}
