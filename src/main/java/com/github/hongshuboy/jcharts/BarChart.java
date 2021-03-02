package com.github.hongshuboy.jcharts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Vector;

/**
 * 柱形图
 *
 * @author hongshuboy
 * 2020-08-28 14:07
 */
public class BarChart {
    private static final Font DEFAULT_EXTRA_LARGE_FONT = new Font("楷体", Font.BOLD, 20);
    private static final Font DEFAULT_REGULAR_FONT = new Font("宋书", Font.PLAIN, 15);
    private static final Font DEFAULT_LARGE_FONT = new Font("宋书", Font.PLAIN, 15);

    public ByteArrayInputStream makeBarChart(DefaultCategoryDataset dataset, String title, String x_title, String y_title) throws Exception {
        return makeBarChart(dataset, title, x_title, y_title, DEFAULT_EXTRA_LARGE_FONT, DEFAULT_REGULAR_FONT, DEFAULT_LARGE_FONT);
    }

    public ByteArrayInputStream makeBarChart(DefaultCategoryDataset dataset, String title, String x_title, String y_title,
                                             Font extraLargeFont, Font regularFont, Font largeFont) throws Exception {
        //解决中文乱码问题
        handleCN(extraLargeFont, regularFont, largeFont);
        //标题，X轴标题，Y轴标题，图表方向：水平、垂直，是否显示图例(对于简单的柱状图必须是false)，是否生成工具，是否生成URL链接
        JFreeChart chart = ChartFactory.createBarChart(title, x_title, y_title, dataset, PlotOrientation.VERTICAL, true, false, false);
        return getByteArrayInputStream(chart);
    }

    private ByteArrayInputStream getByteArrayInputStream(JFreeChart chart) throws Exception {
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return createStream(chart);
    }

    /**
     * 解决中文乱码问题
     *
     * @param extraLargeFont 标题字体
     */
    private void handleCN(Font extraLargeFont, Font regularFont, Font largeFont) {
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(extraLargeFont);
        standardChartTheme.setRegularFont(regularFont);
        standardChartTheme.setLargeFont(largeFont);
        ChartFactory.setChartTheme(standardChartTheme);
    }

    private ByteArrayInputStream createStream(JFreeChart chart) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(outputStream, chart, RsdsConstant.chart_width, RsdsConstant.chart_height);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * 折线图
     * <p>
     * 创建图表步骤：<br/>
     * 1：创建数据集合<br/>
     * 2：创建Chart：<br/>
     * 3:设置抗锯齿，防止字体显示不清楚<br/>
     * 4:对柱子进行渲染，<br/>
     * 5:对其他部分进行渲染<br/>
     * 6:使用chartPanel接收<br/>
     *
     * </p>
     */
    //创建折线图图表
    private DefaultCategoryDataset createDataset(List<String> cs, List<Serie> series) {
        // 标注类别
        String[] categories = cs.toArray(new String[0]);
        // 1：创建数据集合
        return createDefaultCategoryDataset(series, categories);
    }

    /**
     * 创建类别数据集合
     */
    private DefaultCategoryDataset createDefaultCategoryDataset(List<Serie> series, String[] categories) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Serie serie : series) {
            String name = serie.getName();
            Vector<Object> data = serie.getData();
            if (data != null && categories != null && data.size() == categories.length) {
                for (int index = 0; index < data.size(); index++) {
                    Object value = data.get(index);
                    if (value instanceof Double) {
                        Double dValue = (Double) data.get(index);
                        dataset.setValue(dValue, name, categories[index]);
                    } else {
                        String sValue = data.get(index) == null ? null : data.get(index).toString();
                        if (isPercent(sValue)) {
                            sValue = sValue.substring(0, sValue.length() - 1);
                        }
                        if (isNumber(sValue)) {
                            dataset.setValue(Double.parseDouble(sValue), name, categories[index]);
                        }
                    }


                }
            }

        }
        return dataset;
    }

    /**
     * 是不是一个%形式的百分比
     */
    private boolean isPercent(String str) {
        return str != null && (str.endsWith("%") && isNumber(str.substring(0, str.length() - 1)));
    }

    /**
     * 是不是一个数字
     */
    private boolean isNumber(String str) {
        return str != null && str.matches("^[-+]?(([0-9]+)((([.]{0})([0-9]*))|(([.]{1})([0-9]+))))$");
    }
}
