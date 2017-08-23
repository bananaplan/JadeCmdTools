package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.bean.TheSame;
import com.zbaccp.bananaplan.util.FileUtil;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

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
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;

    public ShowDiffDetailsForm(TheSame same) {
        lblMaster1.setText(same.master1 + " > " + same.master1File.getName());
        lblMaster2.setText(same.master2 + " > " + same.master2File.getName());

        txtMaster1.setText(FileUtil.readAll(same.master1File.getAbsolutePath()));
        txtMaster2.setText(FileUtil.readAll(same.master2File.getAbsolutePath()));

        scrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollPane2.getVerticalScrollBar().setValue(e.getValue());
            }
        });

        scrollPane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollPane1.getVerticalScrollBar().setValue(e.getValue());
            }
        });
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
