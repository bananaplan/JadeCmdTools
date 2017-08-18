package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bananaplan on 2017/8/17.
 */
public class ClassSelectForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JComboBox cboClassName;
    private JButton btnOk;

    public ClassSelectForm() {
        loadClassList();

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClassSelectForm.this.cboClassName.getSelectedIndex() > 0) {
                    int index = ClassSelectForm.this.cboClassName.getSelectedIndex() - 1;

                    if (ClassSelectForm.this.cboClassName.getSelectedItem().toString().equals("其他班级")) {
                        Config.classIndex = Config.CLASS_OTHER;
                    } else {
                        Config.initStudentList(index);
                    }

                    ClassSelectForm.frame.dispose();
                }
            }
        });
    }

    private void loadClassList() {
        Config.init();
        this.cboClassName.removeAllItems();

        this.cboClassName.addItem("请选择班级");
        if (Config.classNameList != null) {
            for (String name : Config.classNameList) {
                this.cboClassName.addItem(name);
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
