package com.github.hongshuboy.office;

import com.github.hongshuboy.usermodel.WordPicture;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 配置类，对Word的变量定义都在这里
 * 实现该接口后，可以实现多种配置源的接入，如实现一个JDBCConfig，可从数据库读取配置
 *
 * @author hongshuboy
 */
public interface Config {
    /**
     * EL配置，支持表格
     */
    default Map<String, String> getElMap() {
        return Collections.emptyMap();
    }

    /**
     * 插入表格的数据，Key为表格名，Value为插入的数据
     */
    default Map<String, List<String[]>> getTableData() {
        return Collections.emptyMap();
    }

    /**
     * 要插入的图片，Key为EL名，Value为图片信息
     */
    default Map<String, WordPicture> getPictures() {
        return Collections.emptyMap();
    }
}
