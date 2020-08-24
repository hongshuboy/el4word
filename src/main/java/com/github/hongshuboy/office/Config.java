package com.github.hongshuboy.office;

import com.github.hongshuboy.usermodel.WordPicture;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 配置类，对Word的变量定义都在这里
 * 实现该接口后，可以实现多种配置源的接入，如实现一个JDBCConfig，可从数据库读取配置
 */
public interface Config {
    /**
     * 获取EL的配置
     */
    default Map<String, String> getElMap(){
        return Collections.emptyMap();
    }

    /**
     * <p>表格插入的数据，Key是表格名，需要在表头的第一列设置 tableName#column
     * 之后工具会用tableName作为Key，对该map取值</p>
     * <p>map的Value是List集合，拿到该集合后，在word中对相应的表格进行插入</p>
     * <p>List中的元素类型是String数组，每一个数组中的元素对应一个word表格中的单元格，
     * 如果使用#b标记的，会被工具解析并且生成为加粗字体，如#b:A0123</p>
     */
    default Map<String, List<String[]>> getTableData(){
        return Collections.emptyMap();
    }

    /**
     * 设置准备插入的图片
     */
    default Map<String, WordPicture> getPictures(){
        return Collections.emptyMap();
    }
}
