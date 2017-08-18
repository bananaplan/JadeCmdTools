package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bananaplan on 2017/8/16.
 */
public class MainForm {
    public static MainForm instance;
    public static JFrame frame;

    private JPanel panelMain;
    private JPanel panelMenu;
    private JPanel contentPanel;
    private JMenuBar menuBar;
    private JMenu menuTools;
    private JMenu menuConfig;
    private JMenuItem menuItemLucky;
    private JMenuItem menuItemConfigClass;
    private JMenu menuHelp;
    private JMenuItem menuItemHow;
    private JMenuItem menuItemAbout;
    private JMenuItem menuItemPractice;
    private JMenuItem menuItemHomework;
    private JMenuItem menuItemExtract;
    private JMenuItem menuItemChangeClass;
    private JMenuItem menuItemExit;

    public MainForm() {
        instance = this;
        Config.init();

        menuItemLucky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LuckyForm.show();
            }
        });
        menuItemConfigClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigStudentForm.show();
            }
        });
        menuItemChangeClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassSelectForm.show();
            }
        });
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuItemHomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeworkForm.show();
            }
        });
    }

    public static void setMenuEnabled(boolean isEnabled) {
        MainForm.instance.menuItemLucky.setEnabled(isEnabled);
        MainForm.instance.menuItemPractice.setEnabled(isEnabled);
    }

    public static void main(String[] args) {
        frame = new JFrame("JadeGuiTools");
        frame.setContentPane(new MainForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setMenuEnabled(false);
        ClassSelectForm.show();
    }

}
