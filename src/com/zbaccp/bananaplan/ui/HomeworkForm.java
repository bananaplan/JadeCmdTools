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
    private ArrayList<String> undoList;
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
    private JProgressBar proBar;
    private JButton btnLog;

    public HomeworkForm() {
        if (Config.classIndex != Config.CLASS_OTHER) {
            this.lblDepth.setForeground(Color.GRAY);
            this.txtDepth.setEnabled(false);
        }

        this.proBar.setVisible(false);

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
        btnLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (MainForm.app.videoPath != null && !MainForm.app.videoPath.equals("")) {
                        if (new File(MainForm.app.videoPath).exists()) {
                            Runtime.getRuntime().exec("notepad " + MainForm.app.videoPath);
                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(null, "没有发现录屏文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

        changeState(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                undoList = new ArrayList<String>();
                list = MainForm.app.homeworkAnalysis(txtPath.getText(), Config.classIndex == Config.CLASS_OTHER ? Integer.parseInt(txtDepth.getText()) : 0, undoList);

                // set the result list to JList component
                listModel = new DefaultListModel();

                for (String undo : undoList) {
                    listModel.addElement(undo + " 没交作业");
                }

                for (TheSame same : list) {
                    listModel.addElement(same.toString());
                }

                lsResult.setModel(listModel);

                changeState(false);
                Config.initFilter();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                double max = 0;
                while (true) {
                    try {
                        if (MainForm.app.homeworkMap != null) {
                            int size = MainForm.app.homeworkMap.size();

                            if (size >= max) {
                                max = size;
                            } else {
                                proBar.setValue((int) ((max - size) / max * 100));
                            }
                        } else {
                            if (max > 0) {
                                break;
                            }
                        }

                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    private void showDiff(boolean useInnerDiff) {
        int index = lsResult.getSelectedIndex();

        if (undoList != null && undoList.size() > 0) {
            index -= undoList.size();
        }

        if (index < 0) {
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

    private void changeState(boolean init) {
        if (init) {
            proBar.setValue(0);
            proBar.setVisible(true);

            btnStart.setEnabled(false);
            btnShowDiff.setEnabled(false);
            btnShowDiffDetails.setEnabled(false);
            btnLog.setEnabled(false);

            if (listModel != null) {
                listModel.removeAllElements();
            }
        } else {
            proBar.setVisible(false);

            btnStart.setEnabled(true);
            btnShowDiff.setEnabled(true);
            btnShowDiffDetails.setEnabled(true);
            btnLog.setEnabled(true);
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
