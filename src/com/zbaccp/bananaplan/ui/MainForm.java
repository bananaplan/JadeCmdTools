package com.zbaccp.bananaplan.ui;

import javax.swing.*;

/**
 * Created by bananaplan on 2017/8/16.
 */
public class MainForm {

    private JPanel mainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
