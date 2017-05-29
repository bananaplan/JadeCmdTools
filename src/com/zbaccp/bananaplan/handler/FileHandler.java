package com.zbaccp.bananaplan.handler;

import java.io.File;

/**
 * Created by wangbin on 2017/4/1.
 */
public interface FileHandler {
    /**
     * 递归查找文件后，回调处理方法
     * @param destPath 要写入的目标文件夹
     * @param master 文件的主人姓名
     * @param file 当前文件
     */
    public void callback(String destPath, String master, File file);
}
