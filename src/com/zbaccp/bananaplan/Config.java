package com.zbaccp.bananaplan;

import java.io.File;
import java.util.ArrayList;

import com.zbaccp.bananaplan.bean.Student;
import com.zbaccp.bananaplan.util.FileUtil;

public class Config {
    public final static double VERSION = 1.0;

    public final static int CLASS_OTHER = -1;

    public final static ArrayList<String> VIDEO_EXT_LIST = new ArrayList<String>();
    public final static ArrayList<String> CODE_EXT_INCLUDE_LIST = new ArrayList<String>();
    public final static ArrayList<String> CODE_EXCLUDE_LIST = new ArrayList<String>();

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

    public static void initIEList() {
        VIDEO_EXT_LIST.clear();
        VIDEO_EXT_LIST.add(".lxe");

        CODE_EXT_INCLUDE_LIST.clear();
        CODE_EXT_INCLUDE_LIST.add(".java");
        CODE_EXT_INCLUDE_LIST.add(".cs");
        CODE_EXT_INCLUDE_LIST.add(".html");
        CODE_EXT_INCLUDE_LIST.add(".htm");
        CODE_EXT_INCLUDE_LIST.add(".txt");

        CODE_EXCLUDE_LIST.clear();
        CODE_EXCLUDE_LIST.add("bin");
        CODE_EXCLUDE_LIST.add("obj");
        CODE_EXCLUDE_LIST.add("Properties");
        CODE_EXCLUDE_LIST.add(".Designer.cs");
    }
}
