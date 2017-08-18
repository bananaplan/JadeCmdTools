package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bananaplan on 2017/8/17.
 */
public class ClassSelectForm {
    public static ClassSelectForm instance;
    private static JFrame frame;

    private JPanel panelMain;
    private JComboBox cboClassName;
    private JButton btnOk;

    public ClassSelectForm() {
        instance = this;
        loadClassList(true);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClassSelectForm.this.cboClassName.getSelectedIndex() > 0) {
                    int index = ClassSelectForm.this.cboClassName.getSelectedIndex() - 1;

                    if (ClassSelectForm.this.cboClassName.getSelectedItem().toString().equals("其他班级")) {
                        Config.classIndex = Config.CLASS_OTHER;
                        MainForm.setMenuEnabled(false);
                    } else {
                        Config.initStudentList(index);
                        MainForm.setMenuEnabled(true);
                    }

                    ClassSelectForm.frame.dispose();
                }
            }
        });
    }

    public void loadClassList(boolean reloadConfig) {
        if (reloadConfig) {
            Config.init();
        }

        this.cboClassName.removeAllItems();
        this.cboClassName.addItem("请选择班级");

        if (Config.classNameList != null) {
            for (int i = 0; i < Config.classNameList.size(); i++) {
                this.cboClassName.addItem(Config.classNameList.get(i));

                if (Config.classIndex == i) {
                    this.cboClassName.setSelectedIndex(i + 1);
                }
            }
        }

        this.cboClassName.addItem("其他班级");
    }

    public static void show() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("选择班级");
        frame.setContentPane(new ClassSelectForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }
}
