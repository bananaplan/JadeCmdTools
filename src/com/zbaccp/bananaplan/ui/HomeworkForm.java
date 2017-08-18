package com.zbaccp.bananaplan.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by bananaplan on 2017/8/18.
 */
public class HomeworkForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JButton button1;

    public HomeworkForm() {

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    if (file.isDirectory()) {
                        System.out.println("文件夹:" + file.getAbsolutePath());
                    } else if (file.isFile()) {
                        System.out.println("文件:" + file.getAbsolutePath());
                    }
                }
            }
        });
    }

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
