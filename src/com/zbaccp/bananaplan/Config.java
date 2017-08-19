package com.zbaccp.bananaplan;

import java.io.File;
import java.util.ArrayList;

import com.zbaccp.bananaplan.bean.Student;
import com.zbaccp.bananaplan.util.FileUtil;

public class Config {
    public final static int CLASS_OTHER = -1;

    public final static String[] VIDEO_EXT_LIST = {".lxe"};
    public final static String[] CODE_EXT_INCLUDE_LIST = {".java", ".cs", ".html", ".htm", ".txt"};
    public final static String[] CODE_EXCLUDE_LIST = {"bin", "obj", "Properties"};

    public static ArrayList<String> classNameList = null;
    public static ArrayList<Student> classStuList = null;

    public static int classIndex = CLASS_OTHER;

    public static void init() {
        File dir = new File("config");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            int length = files.length;
            if (length > 0) {
                classNameList = new ArrayList<String>();

                for (int i = 0; i < length; i++) {
                    File file = files[i];

                    String fileName = file.getName();
                    classNameList.add(fileName.substring(0, fileName.lastIndexOf('.')));
                }
            }
        }
    }

    public static void initStudentList(int index) {
        classIndex = index;
        classStuList = new ArrayList<Student>();

        String path = "config/" + classNameList.get(index) + ".txt";
        FileUtil fileUtil = new FileUtil(path);

        String line = null;
        try {
            while ((line = fileUtil.readLine()) != null) {
                if (!line.equals("")) {
                    String[] info = line.split("\\s+");

                    int id = 0;
                    String name = null;

                    if (info.length == 1) {
                        name = info[0];
                    } else {
                        id = Integer.parseInt(info[0]);
                        name = info[1];
                    }

                    classStuList.add(new Student(id, name));
                }
            }
        } finally {
            fileUtil.close();
        }
    }

    /**
     * 是否在文件扩展名候选列表中
     * @param name 文件名或扩展名
     * @param type 0：源码文件，1：视频文件
     * @return 是否匹配
     */
    public static boolean inExtList(String name, int type) {
        if (name == null || name.equals("")) {
            return false;
        }

        int index = name.lastIndexOf('.');
        if (index == -1) {
            return false;
        }

        String ext = name.substring(index);

        String[] extList = null;

        switch (type) {
            case 1:
                extList = Config.VIDEO_EXT_LIST;
                break;
            default:
                extList = Config.CODE_EXT_INCLUDE_LIST;
                break;
        }

        for (int i = 0; i < extList.length; i++) {
            if (ext.trim().equalsIgnoreCase(extList[i])) {
                return true;
            }
        }

        return false;
    }
}
