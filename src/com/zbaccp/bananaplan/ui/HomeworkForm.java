package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.bean.TheSame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
    private JList lsResult;

    public HomeworkForm() {
        if (Config.classIndex != Config.CLASS_OTHER) {
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
                String path = HomeworkForm.this.txtPath.getText();
                String depth = HomeworkForm.this.txtDepth.getText();
                int masterDepth = 0;

                if (path == null || path.equals("")) {
                    JOptionPane.showMessageDialog(null, "请选择作业目录", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (Config.classIndex == Config.CLASS_OTHER) {
                    if (depth == null || depth.equals("")) {
                        JOptionPane.showMessageDialog(null, "请输入学员文件夹的目录深度", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else {
                        try {
                            masterDepth = Integer.parseInt(depth);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "目录深度必须为数字", "提示", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                }

                path = path.replace('\\', '/');
                ArrayList<TheSame> list = MainForm.app.homeworkAnalysis(path, masterDepth);


            }
        });
    }

    public static void show() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("作业分析");
        frame.setContentPane(new HomeworkForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }

}
