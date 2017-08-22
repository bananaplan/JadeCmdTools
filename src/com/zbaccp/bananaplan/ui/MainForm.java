package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Application;
import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.handler.FileHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bananaplan on 2017/8/16.
 */
public class MainForm {
    public static MainForm instance;
    public static JFrame frame;
    public static Application app;

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
        app = new Application();

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
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "JadeGuiTools 1.0\nCreated by bananaplan. Follow is the source code.\nhttps://github.com/bananaplan/JadeCmdTools", "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        checkUpgrade();
    }

    public static void setMenuEnabled(boolean isEnabled) {
        MainForm.instance.menuItemLucky.setEnabled(isEnabled);
        MainForm.instance.menuItemPractice.setEnabled(isEnabled);
    }

    private void checkUpgrade() {
        String url = "https://raw.githubusercontent.com/bananaplan/JadeCmdTools/master/Changelog.txt";
        String filename = "Changelog.txt";

        download(url, filename, new FileHandler() {
            @Override
            public void callback(String destPath, String master, File file) {
                System.out.println(file.getName());
            }
        });

    }

    private class Download implements Runnable {
        private String url;
        private String filename;
        private FileHandler handler;

        public Download(String url, String filename, FileHandler handler) {
            this.url = url;
            this.filename = filename;
            this.handler = handler;
        }

        @Override
        public void run() {
            BufferedInputStream in = null;
            FileOutputStream fout = null;

            try {
                in = new BufferedInputStream(new URL(url).openStream());
                fout = new FileOutputStream(filename);

                int count;
                final byte data[] = new byte[1024];

                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void download(String url, String filename, FileHandler handler) {
        new Thread(new Download(url, filename, handler)).start();
    }

    public static void main(String[] args) {
        frame = new JFrame("JadeGuiTools");
        frame.setContentPane(new MainForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(960, 680);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setMenuEnabled(false);
        ClassSelectForm.show();
    }

}
