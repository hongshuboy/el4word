package com.github.hongshuboy.usermodel;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类能够将字符串中的变量符号取出，根据HashMap对应的规则，将其替换掉，类似于J2EE的EL表达式
 * 比如传入的字符串为："${day}天气是${weather}，你那里呢，${name}
 * 传入的Map定义了以下规则
 * - elMap.put("day", "今天");
 * - elMap.put("weather", "小雨");
 * - elMap.put("name","小兰");
 * 经过处理，最终将返回这样的结果：今天天气是小雨，你那里呢，小兰
 *
 * @author hongshuboy
 * 2020-08-21 10:09
 */
public class EL4J {

    /**
     * @param template 带有EL表达式的字符
     * @param elMap    替换规则
     */
    public static String replaceByEL(String template, Map<String, String> elMap) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            String replace = elMap.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, replace == null ? "\\${" + param.substring(2, param.length() - 1) + "}" : replace);
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
