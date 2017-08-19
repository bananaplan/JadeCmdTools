package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by bananaplan on 2017/8/18.
 */
public class HomeworkForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JTextField txtPath;
    private JButton btnSelect;
    private JTextField txtDepth;
    private JButton btnStart;
    private JLabel lblDepth;

    public HomeworkForm() {
        if (Config.classIndex == Config.CLASS_OTHER) {
            this.lblDepth.setForeground(Color.GRAY);
            this.txtDepth.setEnabled(false);
        }

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setSelectedFile(new File("F:\\work\\学员作业\\0216S1\\作业和录屏\\2017-03-22"));
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    if (file.isDirectory()) {
                        HomeworkForm.this.txtPath.setText(file.getAbsolutePath());
                    } else if (file.isFile()) {
                        System.out.println("文件:" + file.getAbsolutePath());
                    }
                }
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
