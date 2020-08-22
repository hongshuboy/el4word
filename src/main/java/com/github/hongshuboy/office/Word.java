package com.github.hongshuboy.office;

import java.util.List;
import java.util.Map;

/**
 * 常见问题：
 *  1.插入的表格没有边框 - 在读入的模板文件中，将边框样式设置好，选择全部边框
 */
public interface Word {
    /**
     * 读取word的路径设置
     */
    String setInPath();

    /**
     * 将word段落与标题中的${xx}变量替换，替换会保留原格式
     * @param elMap 替换的规则，如${a}，Map Key应为 a ，Value应为要替换的字符
     */
    void transformParagraphs(Map<String, String> elMap);

    /**
     * 将word表格中的${xx}变量替换，若不存在，会转为insert模式，使用tableList进行表格的插入
     * @param elMap 替换的规则
     * @param tableList 若不出在${xx}变量，会使用它进行表格插入
     */
    void transformTable(Map<String, String> elMap,
                        List<String[]> tableList);

}
