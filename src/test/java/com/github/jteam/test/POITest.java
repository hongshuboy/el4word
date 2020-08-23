package com.github.jteam.test;

import com.github.hongshuboy.lang.EL4J;
import com.github.hongshuboy.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 所有操作指令，如替换${a}，或表格名定义tableAlias#column与加粗指令#b:column，都必须在一个域中
 * 请先在TXT文本中键入这些指令，之后完整复制到word文件中，不要手敲，否则可能出现无法匹配的异常
 *
 * @author wp
 * 2020-08-21 09:16
 */
public class POITest {
    private static final String B = "#b";
    private static final String SHAPE = "#";
    private static final String DOLLAR = "$";

    /**
     * 替换表格对象方法
     *
     * @param document  docx解析对象
     * @param elMap     需要替换的信息集合
     * @param tableData 需要插入的表格信息集合
     */
    public static void transformTable(XWPFDocument document, Map<String, String> elMap,
                                      Map<String, List<String[]>> tableData) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            //只处理行数大于等于1的表格，且不循环表头
            if (table.getRows().size() >= 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (table.getText() != null && table.getText().contains(DOLLAR)) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, elMap);
                } else {
                    insertTable(table, tableData);
                }
            }
        }
    }

    /**
     * 遍历并替换表格
     *
     * @param rows  表格行对象
     * @param elMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> elMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (cell.getText() != null && cell.getText().contains(DOLLAR)) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(EL4J.replaceByEL(run.getText(0), elMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据
     *
     * @param table     需要插入数据的表格
     * @param tableData table的数据集合
     */
    private static void insertTable(XWPFTable table, Map<String, List<String[]>> tableData) {
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        //获取表头的第一个cell，匹配表格名称
        String tableName = prepareTableName(rows);
        List<String[]> tableList = tableData.get(tableName);
        //创建行,根据需要插入的数据添加新行
        for (int i = 1; i < tableList.size(); i++) {
            table.createRow();
        }
        for (int i = 1; i < rows.size(); i++) {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                String text = tableList.get(i - 1)[j];
                XWPFRun run = cell.addParagraph().createRun();
                if (cell.getParagraphs().size() > 1) {
                    cell.removeParagraph(0);
                }
                if (text.contains(B)) {
                    run.setText(text.split(":")[1], 0);
                    run.setBold(true);
                } else {
                    run.setText(text, 0);
                }
            }
        }

    }

    private static String prepareTableName(List<XWPFTableRow> rows) {
        String title = rows.get(0).getTableCells().get(0).getText();
        if (title.contains(SHAPE)) {
            //表头只准有一个段落（这里表头指第一行第一列）
            XWPFTableCell cell = rows.get(0).getTableCells().get(0);
            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (!StringUtils.notEmpty(text)) {
                        continue;
                    }
                    if (text.contains(SHAPE)) {
                        run.setText(text.substring(text.indexOf(SHAPE) + 1), 0);
                    }
                }
            }
        } else {
            throw new RuntimeException("插入模式下，必须规定表格名称，如t1#title");
        }
        //return table name
        return title.split(SHAPE)[0];
    }

    private void test11() {
        StringUtils.notEmpty(SHAPE);
    }

    @Test
    public void test1() throws Exception {
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("利用卫星遥感实时监测结果，结合每种热点网格的报警规则，通过时空趋势研判识别，对达到报警标准的热点污染网格发布报警信息");
        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.setText("2020年7月，广东省共监测到网格报警次数XX次，报警网格XX个。排名前三分别是XX市，报警次数XX，报警网格XX；XX市，报警次数XX，报警网格XX；XX市，报警次数XX，报警网格XX。");
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(
                new File("D:\\poi\\document.docx"));
        document.write(out);
        out.close();
        System.out.println(
                "createdocument.docx written successully");
    }

    /**
     * 最普通的测试，替换
     * 今天天气是小雨，你那里呢，小兰
     */
    @Test
    public void poiRead1() throws Exception {
//        String inPath = "D:\\poi\\test_in.docx";
        String inPath = "D:\\poi\\poi_read.docx";
//        String inPath = "D:\\poi\\poi_read_all.docx";
//        String outPath = "D:\\poi\\test_out.docx";
        String outPath = "D:\\poi\\-poi_write.docx";


        Map<String, String> elMap = new HashMap<>();
        elMap.put("y", "2020");
        elMap.put("m", "8");
        elMap.put("d", "21");
        elMap.put("count", "3306");
        elMap.put("name", "小兰");
        elMap.put("weather", "晴天");
        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{"#b:A0123", "烟台", "#b:蓬莱", "678", "2020-08-21", "1", "pm2.5", "1"});
        list.add(new String[]{"A0123", "烟台", "芝罘", "678", "2020-08-21", "1", "pm2.5", "3"});
        ArrayList<String[]> list2 = new ArrayList<>();
        list2.add(new String[]{"1", "2", "3", "4", "5", "#b:6"});
        Map<String, List<String[]>> tableData = new HashMap<>();
        tableData.put("t1", list);
        tableData.put("t2", list2);

        //读入word文件
        FileInputStream inputStream = new FileInputStream(new File(inPath));
        XWPFDocument document = new XWPFDocument(inputStream).getXWPFDocument();
        //核心，进行文字的转换
        transformWord(document, elMap);
        //进行表格的操作
        transformTable(document, elMap, tableData);
        //写出word文件
        FileOutputStream outputStream = new FileOutputStream(new File(outPath));
        document.write(outputStream);
        outputStream.close();
        inputStream.close();
    }

    /**
     * 将word文件中的${xx}变量替换
     *
     * @param elMap    替换的规则
     * @param document 传入原docx的document对象
     * @throws IOException
     */
    private void transformWord(XWPFDocument document, Map<String, String> elMap) throws IOException {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        paragraphs.forEach((paragraph) -> {
            if (paragraph.getText() != null && paragraph.getText().contains(DOLLAR)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    run.setText(EL4J.replaceByEL(run.getText(0), elMap), 0);
                }
            }
        });
    }

    @Test
    public void test2() {
        String template = "${day}天气是${weather}，你那里呢，${name}";
        Map<String, String> params = new HashMap<>();
        prepareParam(params);
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{\\w+}").matcher(template);
        while (m.find()) {
            String param = m.group();
            String replace = params.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, replace == null ? "[" + param.substring(2, param.length() - 1) + "]" : replace);
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }

    @Test
    public void test3() {
        String s = EL4J.replaceByEL("${day}天气是${weather}，你那里呢，${name}", prepareParam(new HashMap<>()));
        System.out.println(s);
    }

    private Map<String, String> prepareParam(Map<String, String> params) {
        params.put("day", "今天");
        params.put("weather", "小雨");
        params.put("name", "小兰");
        return params;
    }
}
