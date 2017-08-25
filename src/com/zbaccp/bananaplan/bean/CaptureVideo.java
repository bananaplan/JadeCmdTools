package com.zbaccp.bananaplan.bean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bananaplan on 2017/8/24.
 */
public class CaptureVideo {
    /**
     * 谁的录屏
     */
    public String master;

    /**
     * 录屏文件的总大小，以 MB 为单位
     */
    public int totalLength;

    /**
     * 录屏文件集合
     */
    public ArrayList<File> files;

    public CaptureVideo(String master, ArrayList<File> list) {
        this.master = master;
        this.files = list;

        this.totalLength = calcTotalLength();
    }

    /**
     * 计算一个学员所有录屏文件的总大小
     * @return 总大小，单位：MB
     */
    private int calcTotalLength() {
        long total = 0;

        for (File file : files) {
            total += file.length();
        }

        return (int) (total / 1024 / 1024);
    }

    /**
     * 计算一个录屏文件的大小
     * @param file 录屏文件
     * @return 文件大小，单位：MB
     */
    private int calcLength(File file) {
        return (int) (file.length() / 1024 / 1024);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(master);
        sb.append("\t");
        sb.append(totalLength + "MB");
        sb.append("\t");

        for (File file : files) {
            sb.append("[");
            sb.append(file.getName());
            sb.append(" ");
            sb.append(calcLength(file));
            sb.append("MB");
            sb.append(" ");
            sb.append(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date(file.lastModified())));
            sb.append("] ");
        }

        return sb.toString();
    }
}
