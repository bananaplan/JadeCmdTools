package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.bean.Student;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by bananaplan on 2017/8/17.
 */
public class LuckyForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JButton btnLucky;
    private JLabel lblLuckyName;
    private JLabel lblReset;

    private LuckyForm() {
        btnLucky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLucky.setEnabled(false);

                if (Student.list == null) {
                    Student.list = (ArrayList<Student>) Config.classStuList.clone();
                }

                new Thread() {
                    @Override
                    public void run() {
                        int count = 0;
                        while (count++ < 10) {
                            int index = (int) (Math.random() * Config.classStuList.size());
                            lblLuckyName.setText(Config.classStuList.get(index).name);

                            try {
                                sleep(60);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if (Student.list != null) {
                            double r = Math.random();
                            int stuCount = Student.list.size();

                            if (stuCount > 0) {
                                int index = (int) (r * stuCount);

                                Student stu = Student.list.get(index);
                                lblLuckyName.setText(stu.id + " - " + stu.name);

                                // 从学生数组中删除学生对象
                                Student.removeStudent(Student.list, stu.id);
                            } else {
                                Student.list = null;
                                lblLuckyName.setText("全部抽完");
                            }
                        }

                        btnLucky.setEnabled(true);
                    }
                }.start();
            }
        });

        lblReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Student.list = null;
                lblLuckyName.setText("重新开始");
                btnLucky.setEnabled(true);
            }
        });
    }

    public static void show() {
        if (frame == null) {
            frame = new JFrame("幸运抽奖");
            frame.setContentPane(new LuckyForm().panelMain);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(600, 400);
        }

        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }

}
