package com.southwind.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptionParser {
    public static Map<String, String> parseDescription(String description) {
        Map<String, String> details = new HashMap<>();
        String[] lines = description.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\s+", 2); // Split on whitespace, expecting 2 parts
            if (parts.length > 1) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                details.put(key, value);
            }
        }
        return details;
    }

    public static String extractBrandFromChipset(String chipset) {
        // 现在仅寻找品牌名，通常是首个词
        Pattern pattern = Pattern.compile("^(Intel|AMD)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(chipset);
        if (matcher.find()) {
            return matcher.group(1); // 返回找到的品牌名
        }
        return null; // 如果没有找到品牌，则返回null
    }

    public static String parseSSDDescription(String description) {
        Map<String, String> details = new HashMap<>();
        String[] lines = description.split("："); // 分隔符变更为冒号
        for (int i = 0; i < lines.length - 1; i++) {
            String key = lines[i].trim();
            String value = lines[i + 1].trim();

            int nextColon = value.indexOf("：");
            if (nextColon != -1) {
                value = value.substring(0, nextColon);
            }

            if (i == 0) { // 特别处理第一个键，因为它没有前一个冒号来标示开始
                String[] firstEntry = key.split(" ", 2);
                if (firstEntry.length == 2) {
                    details.put(firstEntry[0].trim(), firstEntry[1].trim());
                }
            }

            if (i > 0) {
                // 检查并处理前一个键
                String[] lastLine = lines[i].split(" ");
                key = lastLine[lastLine.length - 1].trim();
            }

            details.put(key, value);
        }
        Pattern pattern = Pattern.compile("^(DDR4|DDR5)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(details.get("内存类型"));
        if (matcher.find()) {
            return matcher.group(1); // 返回找到的内存类型
        }
        return null;
    }

    public static String extractBrandFromMemory(String chipset) {
        // 现在仅寻找内存类型，通常是首个词
        Pattern pattern = Pattern.compile("^(DDR4|DDR5)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(chipset);
        if (matcher.find()) {
            return matcher.group(1); // 返回找到的内存类型
        }
        return null; // 如果没有的内存类型，则返回null
    }

    public static String extractYingPanInterfaceType(String input) {
        // 使用正则表达式匹配接口类型
        Pattern pattern = Pattern.compile("接口类型：(.*?)接口");
        Matcher matcher = pattern.matcher(input);

        // 如果找到匹配项，则返回匹配的接口类型
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "未知"; // 如果未找到匹配项，则返回未知
        }
    }

    public static int compareStrings(String input1, String input2) {
        Set<String> substrings1 = getAllSubstrings(input1);
        Set<String> substrings2 = getAllSubstrings(input2);

        for (String substring : substrings1) {
            if (substrings2.contains(substring)) {
                return 1;
            }
        }

        return -1;
    }

    public static Set<String> getAllSubstrings(String str) {
        Set<String> substrings = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j <= str.length(); j++) {
                substrings.add(str.substring(i, j));
            }
        }
        return substrings;
    }

}
