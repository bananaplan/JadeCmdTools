package com.zbaccp.bananaplan.handler;

import java.io.File;

/**
 * Created by wangbin on 2017/4/1.
 */
public interface FileHandler {
    /**
     * �ݹ�����ļ��󣬻ص�������
     * @param destPath Ҫд���Ŀ���ļ���
     * @param master �ļ�����������
     * @param file ��ǰ�ļ�
     */
    public void callback(String destPath, String master, File file);
}
