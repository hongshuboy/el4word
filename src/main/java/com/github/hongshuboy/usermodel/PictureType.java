package com.github.hongshuboy.usermodel;

/**
 * 定义图片类型，{@link org.apache.poi.xwpf.usermodel.Document}
 *
 * @author hongshuboy
 * 2020-08-24 10:34
 */
public interface PictureType {
    /**
     * Extended windows meta file
     */
    int PICTURE_TYPE_EMF = 2;

    /**
     * Windows Meta File
     */
    int PICTURE_TYPE_WMF = 3;

    /**
     * Mac PICT format
     */
    int PICTURE_TYPE_PICT = 4;

    /**
     * JPEG format
     */
    int PICTURE_TYPE_JPEG = 5;

    /**
     * PNG format
     */
    int PICTURE_TYPE_PNG = 6;

    /**
     * Device independent bitmap
     */
    int PICTURE_TYPE_DIB = 7;

    /**
     * GIF image format
     */
    int PICTURE_TYPE_GIF = 8;

    /**
     * Tag Image File (.tiff)
     */
    int PICTURE_TYPE_TIFF = 9;

    /**
     * Encapsulated Postscript (.eps)
     */
    int PICTURE_TYPE_EPS = 10;


    /**
     * Windows Bitmap (.bmp)
     */
    int PICTURE_TYPE_BMP = 11;

    /**
     * WordPerfect graphics (.wpg)
     */
    int PICTURE_TYPE_WPG = 12;
}
