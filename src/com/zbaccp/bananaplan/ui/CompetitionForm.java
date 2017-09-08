package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.bean.Student;
import com.zbaccp.bananaplan.util.FileUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bananaplan on 2017/9/4.
 */
public class CompetitionForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JTextField txtTitle;
    private JTextField txtStuId;
    private JButton btnFinish;
    private JComboBox cboList;
    private JTextArea txtResult;
    private JButton btnStart;
    private JLabel lblTime;

    private int[] idArray;
    private Date startTime;
    private String rootPath;
    private String finishPath;
    private String unFinishPath;

    public CompetitionForm() {
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCompetition();
            }
        });
        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFinish();
            }
        });
        txtStuId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFinish();
            }
        });
        cboList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHistory();
            }
        });

        loadHistoryList();
    }

    private void loadHistoryList() {
        this.cboList.addItem("请选择要查看的竞赛记录");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        rootPath = "report/" + Config.classNameList.get(Config.classIndex) + "/" + sdf.format(new Date());

        File dir = new File(rootPath);
        if (dir.exists()) {
            File[] list = dir.listFiles();
            for (File file : list) {
                if (file.isFile()) {
                    String filename = file.getName();
                    if (filename.indexOf(".backup.txt") == -1) {
                        this.cboList.addItem(filename.substring(0, filename.lastIndexOf('.')));
                    }
                }
            }
        }
    }

    private void loadHistory() {
        if (this.cboList.getSelectedIndex() > 0) {
            String title = this.cboList.getSelectedItem().toString();

            String finish = FileUtil.readAll(rootPath + "/" + title + ".txt");
            String unFinish = FileUtil.readAll(rootPath + "/" + title + ".backup.txt");

            StringBuffer sb = new StringBuffer();
            if (finish != null && !finish.equals("")) {
                sb.append(finish);
            }

            if (unFinish != null && !unFinish.equals("")) {
                sb.append("\n");
                sb.append(unFinish);
            }

            this.txtResult.setText(sb.toString());
        } else {
            this.txtResult.setText("");
        }
    }

    private void startCompetition() {
        String title = this.txtTitle.getText();

        if (title == null || title.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入竞赛标题", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean start = true;

        if (idArray != null) {
            if (JOptionPane.showConfirmDialog(null, "是否重新开始竞赛？", "确认",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) {
                start = false;
            }
        }

        if (start) {
            idArray = new int[Config.classStuList.size()];
            startTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            finishPath = rootPath + "/" + title + ".txt";
            unFinishPath = rootPath + "/" + title + ".backup.txt";

            FileUtil.delete(finishPath);
            FileUtil.delete(unFinishPath);

            this.cboList.addItem(title);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (idArray != null) {
                        long totalSeconds = (new Date().getTime() - startTime.getTime()) / 1000;
                        String minutes = String.valueOf(totalSeconds / 60);
                        String seconds = String.valueOf(totalSeconds % 60);
                        lblTime.setText(minutes + " 分 " + seconds + " 秒");
                    }
                }
            }).start();
        }
    }

    private void setFinish() {
        if (idArray == null) {
            JOptionPane.showMessageDialog(null, "请先开始竞赛", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String title = this.txtTitle.getText();
        String id = this.txtStuId.getText();

        if (title == null || title.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入竞赛标题", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (id == null || id.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入学员编号", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int stuId = 0;

        try {
            stuId = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "学员编号必须为数字", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (stuId > 0) {
            this.txtStuId.setText("");
            Student stu = Student.getStudent(Config.classStuList, stuId);

            if (stu != null) {
                boolean isAllFinish = MainForm.app.saveProgRace(title, startTime, stu, idArray);
                String finish = FileUtil.readAll(finishPath);
                String unFinish = FileUtil.readAll(unFinishPath);

                StringBuffer sb = new StringBuffer();

                if (unFinish != null && !unFinish.equals("")) {
                    sb.append(unFinish);
                }

                if (finish != null && !finish.equals("")) {
                    sb.append("\n");
                    sb.append(finish);
                }

                this.txtResult.setText(sb.toString());

                if (isAllFinish) {
                    idArray = null;
                    this.txtTitle.setText("");
                    JOptionPane.showMessageDialog(null, "全部完成", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "无此学员", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void show() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("编码竞赛");
        frame.setContentPane(new CompetitionForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(720, 560);
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }
}
