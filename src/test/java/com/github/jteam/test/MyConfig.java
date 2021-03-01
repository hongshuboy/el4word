package com.github.jteam.test;

import com.github.hongshuboy.jcharts.BarChart;
import com.github.hongshuboy.office.Config;
import com.github.hongshuboy.usermodel.PictureType;
import com.github.hongshuboy.usermodel.WordPicture;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyConfig implements Config {
    @Override
    public Map<String, String> getElMap() {
        Map<String, String> elMap = new HashMap<>();
        elMap.put("y", "2020");
        elMap.put("m", "8");
        elMap.put("d", "21");
        elMap.put("count", "3306");
        elMap.put("name", "小兰");
        elMap.put("weather", "晴天");
        return elMap;
    }

    @Override
    public Map<String, List<String[]>> getTableData() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{"#b:A0123", "美国", "#b:华盛顿", "小雨"});
        list.add(new String[]{"A0124", "台湾", "台北", "晴"});
        list.add(new String[]{"A0125", "日本", "东京", "晴"});
        Map<String, List<String[]>> tableData = new HashMap<>();
        tableData.put("t1", list);//表格1
        ArrayList<String[]> list2 = new ArrayList<>();
        list2.add(new String[]{"1", "2", "3", "4", "5", "#b:6"});
        list2.add(new String[]{"#b:11", "22", "33", "44", "55", "#b:66"});
        tableData.put("t2", list2);//表格2
        return tableData;
    }

    @Override
    public Map<String, WordPicture> getPictures() {
        Map<String, WordPicture> map = new HashMap<>();
        try {
            map.put("img1", WordPicture.of(new FileInputStream("D:\\poi\\spring.jpg"), "flink", 474, 237, PictureType.PICTURE_TYPE_JPEG));
            ByteArrayInputStream inputStream = loadOneChart();
            map.put("img2", WordPicture.of(inputStream, "", 474, 237, PictureType.PICTURE_TYPE_JPEG));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private ByteArrayInputStream loadOneChart() throws Exception {
        BarChart chart = new BarChart();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "x", "A地区");
        dataset.addValue(2, "y", "A地区");
        dataset.addValue(2, "z", "A地区");


        dataset.addValue(1, "x", "B地区");
        dataset.addValue(2, "y", "B地区");
        dataset.addValue(2, "z", "B地区");


        dataset.addValue(1, "x", "C地区");
        dataset.addValue(2, "y", "C地区");
        dataset.addValue(2, "z", "C地区");


        dataset.addValue(1, "x", "D地区");
        dataset.addValue(2, "y", "D地区");
        dataset.addValue(1.5, "z", "D地区");

        return chart.makeBarChart(dataset, "图表标题", "x标题", "y标题");
    }
}
