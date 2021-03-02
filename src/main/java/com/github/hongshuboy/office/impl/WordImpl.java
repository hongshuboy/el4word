package com.github.hongshuboy.office.impl;

import com.github.hongshuboy.office.Config;
import com.github.hongshuboy.office.Sink;
import com.github.hongshuboy.office.Source;
import com.github.hongshuboy.office.Word;
import com.github.hongshuboy.usermodel.EL4J;
import com.github.hongshuboy.usermodel.StringUtils;
import com.github.hongshuboy.usermodel.WordPicture;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * WordImpl
 *
 * @author hongshuboy
 */
public class WordImpl implements Word {
    private static final String B = "#b";
    private static final String SHAPE = "#";
    private static final String DOLLAR = "$";
    private static final String EMPTY = "";
    private XWPFDocument document;
    private Map<String, String> elMap;
    private Map<String, List<String[]>> tableData;
    private Map<String, WordPicture> pictureMap;
    private Source source;
    private Sink sink;

    private static String prepareTableName(List<XWPFTableRow> rows) {
        String title = rows.get(0).getTableCells().get(0).getText();
        if (title.contains(SHAPE)) {
            //第一行第一列只准有一个段落
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

    @Override
    public void addSource(Source source) {
        this.source = source;
    }

    @Override
    public void addSink(Sink sink) {
        this.sink = sink;
    }

    @Override
    public void addConfig(Config config) {
        elMap = config.getElMap();
        tableData = config.getTableData();
        pictureMap = config.getPictures();
    }

    @Override
    public void transformParagraphs() {
        loadDocument();
        transformParagraphs(elMap);
    }

    @Override
    public void transformTables() {
        loadDocument();
        transformTables(elMap, tableData);
    }

    @Override
    public void transformPictures() {
        loadDocument();
        transformPictures(pictureMap);
    }

    private void loadDocument() {
        if (document == null) {
            document = source.getDocument();
        }
    }

    public void transformPictures(Map<String, WordPicture> pictureMap) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        paragraphs.forEach((paragraph) -> {
            if (paragraph.getText() != null && paragraph.getText().contains(DOLLAR)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    WordPicture wordPicture = getWordPicture(pictureMap, run);
                    if (wordPicture != null) {
                        // insert picture
                        run.setText(EMPTY, 0);
                        try (InputStream inputStream = wordPicture.getInputStream()) {
                            run.addPicture(inputStream,
                                    wordPicture.getPictureType(),
                                    wordPicture.getFileName(),
                                    Units.pixelToEMU(wordPicture.getWidthPx()),
                                    Units.pixelToEMU(wordPicture.getHeightPx()));
                        } catch (InvalidFormatException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 匹配当前区域是否可替换为图片
     *
     * @param pictureMap 图片变量
     * @param run        区域
     */
    private WordPicture getWordPicture(Map<String, WordPicture> pictureMap, XWPFRun run) {
        String text = run.getText(0);
        if (StringUtils.notEmpty(text)) {
            for (Map.Entry<String, WordPicture> entry : pictureMap.entrySet()) {
                if (text.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 将word段落与标题中的${xx}变量替换，替换会保留原格式
     *
     * @param elMap 替换的规则，如${a}，Map Key应为 a ，Value应为要替换的字符
     */
    private void transformParagraphs(Map<String, String> elMap) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        paragraphs.forEach((paragraph) -> {
            if (paragraph.getText() != null && paragraph.getText().contains(DOLLAR)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    run.setText(EL4J.replaceByEL(run.getText(0), elMap), 0);
                }
            }
        });
    }

    /**
     * 将word表格中的${xx}变量替换，若不存在，会转为insert模式，使用tableList进行表格的插入
     *
     * @param elMap     替换的规则
     * @param tableData 若不出在${xx}变量，会使用它进行表格插入
     */
    private void transformTables(Map<String, String> elMap, Map<String, List<String[]>> tableData) {
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            //只处理行数大于等于1的表格
            if (table.getRows().size() >= 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (table.getText() != null && table.getText().contains(DOLLAR)) {
                    List<XWPFTableRow> rows = table.getRows();
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
    public void eachTable(List<XWPFTableRow> rows, Map<String, String> elMap) {
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
    private void insertTable(XWPFTable table, Map<String, List<String[]>> tableData) {
        List<XWPFTableRow> rows = table.getRows();
        //获取表头的第一个cell，匹配表格名称
        String tableName = prepareTableName(rows);
        List<String[]> tableList = tableData.get(tableName);
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

    @Override
    public void save() {
        sink.storeDocument(document);
    }
}
