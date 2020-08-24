package com.github.hongshuboy.office;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.hongshuboy.office.impl.FileSink;
import com.github.hongshuboy.office.impl.FileSource;

/**
 * 常见问题：
 * 1.插入的表格没有边框 - 在读入的模板文件中，将边框样式设置好，选择全部边框
 */
public interface Word {
    /**
     * 添加读取文件的方式
     *
     * @param source 可自定义，或者是{@link FileSource}
     */
    void addSource(Source source);

    /**
     * 添加存储文件的方式
     *
     * @param sink 可自定义，或使用{@link FileSink}
     */
    void addSink(Sink sink);

    /**
     * 添加替换规则或表格插入等数据
     *
     * @param config 需要自定义，可灵活选择加载方式，如实现一个JDBCConfig
     */
    void addConfig(Config config);

    /**
     * 将word段落与标题中的${xx}变量替换，替换会保留原格式
     */
    void transformParagraphs();

    /**
     * 将word表格中的${xx}变量替换，若不存在，会转为insert模式，使用tableList进行表格的插入
     */
    void transformTables();

    /**
     * 将word段落与标题中的${xx}变量替换为图片，替换会保留原格式（如居中）
     */
    void transformPictures();

    /**
     * 保存文件
     */
    void save();
}
