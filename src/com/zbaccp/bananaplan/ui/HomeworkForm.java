package com.zbaccp.bananaplan.ui;

import javax.swing.*;

/**
 * Created by bananaplan on 2017/8/18.
 */
public class HomeworkForm {
    private static JFrame frame;

    private JPanel panelMain;

    public static void show() {
        if (frame == null) {
            frame = new JFrame("作业分析");
            frame.setContentPane(new HomeworkForm().panelMain);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setSize(600, 400);
        }

        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }

}
