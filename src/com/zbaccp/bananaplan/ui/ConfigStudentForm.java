package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by bananaplan on 2017/8/17.
 */
public class ConfigStudentForm {
    private static JFrame frame;

    private JPanel panelMain;
    private JButton btnSave;
    private JButton btnCancel;
    private JComboBox cboClassName;
    private JTextArea txtSutdentList;
    private JButton btnDelete;

    public ConfigStudentForm() {
        loadClassList();

        cboClassName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = cboClassName.getSelectedIndex();

                    if (index > 0) {
                        String className = cboClassName.getSelectedItem().toString();

                        String path = "config/" + className + ".txt";
                        String content = FileUtil.readAll(path);

                        if (content != null) {
                            txtSutdentList.setText(content);
                        } else {
                            txtSutdentList.setText("");
                        }
                    } else {
                        if (index == 0) {
                            txtSutdentList.setText("");
                        }
                    }
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = cboClassName.getSelectedItem().toString();
                String content = txtSutdentList.getText();

                if (className.equals("") || content.equals("")) {
                    JOptionPane.showMessageDialog(null, "请设置班级名称和学生列表信息", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String path = "config/" + className + ".txt";

                FileUtil fileUtil = new FileUtil(path);
                try {
                    if (fileUtil.write(content, false)) {
                        loadClassList();
                        JOptionPane.showMessageDialog(null, "设置成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "哦，出错了", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } finally {
                    fileUtil.close();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigStudentForm.frame.dispose();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cboClassName.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "请选择要删除的班级", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "确认",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    String className = cboClassName.getSelectedItem().toString();
                    String path = "config/" + className + ".txt";

                    if (FileUtil.delete(path)) {
                        txtSutdentList.setText("");
                        loadClassList();
                        JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "哦，出错了", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void loadClassList() {
        Config.init();
        this.cboClassName.removeAllItems();

        this.cboClassName.addItem("请输入或选择要编辑的班级名称");
        if (Config.classNameList != null) {
            for (String name : Config.classNameList) {
                this.cboClassName.addItem(name);
            }
        }

        if (ClassSelectForm.instance != null) {
            ClassSelectForm.instance.loadClassList(false);
        }
    }

    public static void show() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("设置班级和学员信息");
        frame.setContentPane(new ConfigStudentForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(MainForm.frame);
        frame.setVisible(true);
    }
}
