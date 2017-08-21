package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.bean.TheSame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bananaplan on 2017/8/18.
 */
public class HomeworkForm {
    private static JFrame frame;

    private ArrayList<TheSame> list;
    private DefaultListModel listModel;

    private JPanel panelMain;
    private JTextField txtPath;
    private JButton btnSelect;
    private JTextField txtDepth;
    private JButton btnStart;
    private JLabel lblDepth;
    private JList lsResult;
    private JButton btnShowDiff;
    private JTextField txtExclude;

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
                list = MainForm.app.homeworkAnalysis(path, masterDepth);

                // set the result list to JList component
                if (listModel == null) {
                    listModel = new DefaultListModel();
                }

                for (TheSame same : list) {
                    listModel.addElement(same.toString());
                }

                lsResult.setModel(listModel);
            }
        });

        btnShowDiff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDiff();
            }
        });

        lsResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showDiff();
                }
            }
        });
    }

    private void showDiff() {
        int index = lsResult.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(null, "请选择分析结果列表中的记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            TheSame same = list.get(index);
            Runtime.getRuntime().exec("java -jar lib/JMeld-2.1.jar \"" + same.master1File.getAbsolutePath() + "\" \"" + same.master2File.getAbsolutePath() + "\"");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void show() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("作业分析");
        frame.setContentPane(new HomeworkForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(720, 560);
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }

}
