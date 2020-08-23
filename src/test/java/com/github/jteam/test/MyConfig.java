package com.github.jteam.test;

import com.github.hongshuboy.office.Config;

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
        list.add(new String[]{"#b:A0123", "烟台", "#b:蓬莱", "678", "2020-08-21", "1", "pm2.5", "1"});
        list.add(new String[]{"A0123", "烟台", "芝罘", "678", "2020-08-21", "1", "pm2.5", "3"});
        ArrayList<String[]> list2 = new ArrayList<>();
        list2.add(new String[]{"1", "2", "3", "4", "5", "#b:6"});
        list2.add(new String[]{"#b:11", "22", "33", "44", "55", "#b:66"});
        Map<String, List<String[]>> tableData = new HashMap<>();
        tableData.put("t1", list);
        tableData.put("t2", list2);
        return tableData;
    }
}
