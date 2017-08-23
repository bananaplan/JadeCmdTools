package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.bean.TheSame;
import com.zbaccp.bananaplan.util.FileUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bananaplan on 2017/8/21.
 */
public class ShowDiffDetailsForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JTextArea txtMaster1;
    private JTextArea txtMaster2;
    private JLabel lblMaster1;
    private JLabel lblMaster2;

    public ShowDiffDetailsForm(TheSame same) {
        lblMaster1.setText(same.master1 + " -> " + same.master1File.getName());
        lblMaster2.setText(same.master2 + " -> " + same.master2File.getName());

        txtMaster1.setText(FileUtil.readAll(same.master1File.getAbsolutePath()));
        txtMaster2.setText(FileUtil.readAll(same.master2File.getAbsolutePath()));
    }

    public static void show(TheSame same) {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("作业分析");
        frame.setContentPane(new ShowDiffDetailsForm(same).panelMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(720, 560);
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
