package com.zbaccp.bananaplan.bean;

import java.io.File;

/**
 * Created by bananaplan on 2017/8/18.
 */
public class TheSame {
    public String master1;
    public String master2;
    public File master1File;
    public File master2File;
    public int similar;

    public String toString() {
        return master1 + "：" + master1File.getName() + "  <->  " + master2 + "：" + master2File.getName() + "，相似度：" + similar;
    }
}
