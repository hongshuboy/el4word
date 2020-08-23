package com.github.hongshuboy.office;

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
    Map<String, String> getElMap();

    /**
     * 表格插入的数据，Key是表格名，需要在表头的第一列设置 tableName#column
     * 之后工具会用tableName作为Key，对该map取值
     * map的Value是List集合，拿到该集合后，在word中对相应的表格进行插入
     */
    Map<String, List<String[]>> getTableData();
}
