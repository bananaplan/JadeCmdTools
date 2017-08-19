package com.zbaccp.bananaplan.util;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.bean.TheSame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbin on 2017/4/9.
 */
public class SimilarityAnalysis {

    public SimilarityAnalysis() {

    }

    public String tidy(String text) {
        return text.replaceAll("//.*", "").replaceAll("\\r|\\n", " ").replaceAll("/\\*.*?\\*/", "").replaceAll("\\s+", " ").trim();
    }

    /**
     * 检查代码相似度
     *
     * @param master1 学员姓名
     * @param list1   第一个代码样本集合
     * @param master2 学员姓名
     * @param list2   第二个代码样本集合
     * @return 代码相似度结果集
     */
    public ArrayList<TheSame> checkFilesSimilar(String master1, ArrayList<File> list1, String master2, ArrayList<File> list2) {
        ArrayList<TheSame> list = new ArrayList<TheSame>();

        for (int i = 0; i < list1.size(); i++) {
            File file1 = list1.get(i);

            String content1 = null;
            if (Config.inExtList(file1.getName(), 0)) {
                content1 = FileUtil.readAll(file1.getAbsolutePath());
                if (content1 == null || content1.length() == 0) {
                    continue;
                }
            }

            for (int j = 0; j < list2.size(); j++) {
                File file2 = list2.get(j);

                int similar = 0;
                if (file1.getName().equals(file2.getName())) {
                    similar += 5;
                }

                if (file1.lastModified() == file2.lastModified()) {
                    similar += 100;
                }

                if (Config.inExtList(file2.getName(), 0)) {
                    if (similar < 100) {
                        String content2 = FileUtil.readAll(file2.getAbsolutePath());
                        if (content2 == null || content2.length() == 0) {
                            continue;
                        }

                        // 过滤 UTF-8 with BOM 编码文件开头的一个字符
                        if (content1.charAt(0) == '\uFEFF') {
                            content1 = content1.substring(1);
                        }

                        if (content2.charAt(0) == '\uFEFF') {
                            content2 = content2.substring(1);
                        }

                        if (content1.equals(content2)) {
                            similar += 100;
                        } else if (content1.equalsIgnoreCase(content2)) {
                            similar += 98;
                        }

                        if (similar < 98) {
                            String content1Tidy = content1.replaceAll("\\r|\\n|\\s", "");
                            String content2Tidy = content2.replaceAll("\\r|\\n|\\s", "");

                            if (content1Tidy.equals(content2Tidy)) {
                                similar += 96;
                            } else if (content1Tidy.equalsIgnoreCase(content2Tidy)) {
                                similar += 94;
                            } else {
                                // 相似度分析
                                similar += checkTextSimilar(content1, content2);
                            }
                        }
                    }
                }

                if (similar >= 75) {
                    list.add(new TheSame(master1, file1, master2, file2, similar));
                }
            }
        }

        return list;
    }

    public int checkTextSimilar(String text1, String text2) {
        int similar = 0;

        String content1Tidy = tidy(text1);
        String content2Tidy = tidy(text2);

        if (content1Tidy.equals(content2Tidy)) {
            similar += 92;
        } else if (content1Tidy.equalsIgnoreCase(content2Tidy)) {
            similar += 90;
        } else {
            similar += calcSimilarWords(content1Tidy.split(" "), content2Tidy.split(" "));
        }

        return similar;
    }

    private int calcSimilarWords(String[] words1, String[] words2) {
        int same = 0;

        HashMap<String, Integer> map1 = calcSameWordCount(words1);
        HashMap<String, Integer> map2 = calcSameWordCount(words2);

        int sameCount = 0;
        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            if (map2.containsKey(entry.getKey())) {
                int singleSameCount = entry.getValue() < map2.get(entry.getKey()) ? entry.getValue() : map2.get(entry.getKey());
                sameCount += singleSameCount * 2;
            }
        }

//        System.out.println(words1.length + " - " + words2.length);
//        System.out.println(words1.length + words2.length - sameCount);

        return (int) ((double) sameCount / (words1.length + words2.length) * 0.9 * 100);
    }

    private HashMap<String, Integer> calcSameWordCount(String[] words) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < words.length; i++) {
            int count = 1;
            if (map.get(words[i]) != null) {
                count = map.get(words[i]) + 1;
            }
            map.put(words[i], count);
        }

        return map;
    }

    public static void main(String[] args) {
        SimilarityAnalysis sa = new SimilarityAnalysis();
        sa.calcSimilarWords(new String[]{"int", "num1", "int", "num2"}, new String[]{"int", "num1"});
    }

}
