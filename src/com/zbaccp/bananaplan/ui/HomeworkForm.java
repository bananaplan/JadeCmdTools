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
    private JButton btnShowDiffDetails;

    public HomeworkForm() {
        if (Config.classIndex != Config.CLASS_OTHER) {
            this.lblDepth.setForeground(Color.GRAY);
            this.txtDepth.setEnabled(false);
        }

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAnalysis();
            }
        });

        btnShowDiff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDiff(false);
            }
        });

        lsResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showDiff(false);
                }
            }
        });

        btnShowDiffDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDiff(true);
            }
        });
    }

    private void showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(new JLabel(), "选择");
        File file = chooser.getSelectedFile();

        if (file != null) {
            if (file.isDirectory()) {
                txtPath.setText(file.getAbsolutePath());
            }
        }
    }

    private void startAnalysis() {
        String path = txtPath.getText();
        String depth = txtDepth.getText();
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

        String exclude = txtExclude.getText();
        if (!exclude.equals("")) {
            String[] list = exclude.split("\\s+");
            for (int i = 0; i < list.length; i++) {
                Config.CODE_EXCLUDE_LIST.add(list[i]);
            }
        }

        list = MainForm.app.homeworkAnalysis(path, masterDepth);

        // set the result list to JList component
        listModel = new DefaultListModel();

        for (TheSame same : list) {
            listModel.addElement(same.toString());
        }

        lsResult.setModel(listModel);
        Config.initIEList();
    }

    private void showDiff(boolean useInnerDiff) {
        int index = lsResult.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(null, "请选择分析结果列表中的记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            TheSame same = list.get(index);

            if (useInnerDiff) {
                ShowDiffDetailsForm.show(same);
            } else {
                Runtime.getRuntime().exec("java -jar lib/JMeld-2.1.jar \"" + same.master1File.getAbsolutePath() + "\" \"" + same.master2File.getAbsolutePath() + "\"");
            }
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
