package net.skeyurt.lit.commons.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 命名工具类
 * User : liulu
 * Date : 2016-10-4 16:05
 */
public class NameUtils {

    /**
     * 下划线分割命名转换为驼峰命名
     * @param name
     * @return
     */
    public static String getCamelName(String name) {
        return getCamelName(name, '_');
    }

    /**
     * 获取指定字符分隔的驼峰命名
     * @param name
     * @param delimiter
     * @return
     */
    public static String getCamelName(String name, Character delimiter) {

        if (StringUtils.isBlank(name)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        name = name.toLowerCase();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ( c == delimiter ) {
                i++;
                sb.append(Character.toUpperCase(name.charAt(i)));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名转换为小写下划线分割命名
     * @param name
     * @return
     */
    public static String getUnderLineName (String name) {
        return getLowerDelimiterName(name, "_");
    }

    /**
     * 驼峰命名转换为小写指定分隔符命名
     * @param name
     * @param delimiter
     * @return
     */
    public static String getLowerDelimiterName(String name, String delimiter){
        return getUpperDelimiterName(name, delimiter).toLowerCase();
    }

    /**
     * 驼峰命名转换为大写指定分隔符命名
     * @param name
     * @param delimiter
     * @return
     */
    public static String getUpperDelimiterName(String name, String delimiter) {

        if (StringUtils.isBlank(name)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append(delimiter);
            }
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }


    public static void main(String[] args) {
        System.out.println(getUnderLineName("UserName"));
    }

}
