package com.zbaccp.bananaplan.ui;

import com.zbaccp.bananaplan.Application;
import com.zbaccp.bananaplan.Config;
import com.zbaccp.bananaplan.handler.FileHandler;
import com.zbaccp.bananaplan.util.FileUtil;

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
    private JMenu menuHelp;
    private JMenuItem menuItemLucky;
    private JMenuItem menuItemConfigClass;
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
        menuItemHomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeworkForm.show();
            }
        });
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "JadeGuiTools 0.1\nCreated by bananaplan. Open source code on\nhttps://github.com/bananaplan/JadeCmdTools/tree/dev", "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        checkUpgrade();
    }

    public static void setMenuEnabled(boolean isEnabled) {
        MainForm.instance.menuItemLucky.setEnabled(isEnabled);
//        MainForm.instance.menuItemPractice.setEnabled(isEnabled);
    }

    private void checkUpgrade() {
        System.out.println("check upgrade ...");

        String logUrl = "https://raw.githubusercontent.com/bananaplan/JadeCmdTools/dev/Changelog.txt";
        String logFileName = "Changelog.txt";

        startDownload(logUrl, logFileName, logHandler);
    }

    private void startDownload(String url, String filename, FileHandler handler) {
        new Thread(new Download(url, filename, handler)).start();
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
            FileOutputStream fos = null;

            try {
                in = new BufferedInputStream(new URL(url).openStream());
                fos = new FileOutputStream(filename);

                int count;
                final byte data[] = new byte[1024];

                while ((count = in.read(data, 0, 1024)) != -1) {
                    fos.write(data, 0, count);
                }

                handler.callback(null, null, new File(filename));

            } catch (MalformedURLException e) {
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private FileHandler logHandler = new FileHandler() {
        @Override
        public void callback(String destPath, String master, File file) {
            String name = file.getName();

//            FileUtil fileUtil = new FileUtil(name);
//            fileUtil.write(FileUtil.readAll(name).replace("\n", "\r\n"), false);
//            fileUtil.close();

            FileUtil fileUtil = new FileUtil(name);
            String versionInfo = fileUtil.readLine();
            System.out.println(versionInfo);

//            try {
//                double version = Double.parseDouble(firstLine.substring(firstLine.indexOf(':') + 1));
//
//                if (version > Config.VERSION) {
//                    JOptionPane.showMessageDialog(null, "发现新版本, Version: " + version + ", 准备开始下载更新", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//                    String jarUrl = "https://github.com/bananaplan/JadeCmdTools/blob/dev/JadeCmdTools-1.1.jar?raw=true";
//                    final String jarFileName = jarUrl.substring(jarUrl.lastIndexOf('/') + 1, jarUrl.lastIndexOf('?'));
//
//                    startDownload(jarUrl, jarFileName, new FileHandler() {
//                        @Override
//                        public void callback(String destPath, String master, File file) {
//                            FileUtil fileUtil = new FileUtil("run.bat");
//                            fileUtil.write("java -jar " + jarFileName + "\r\npause", false);
//                            fileUtil.close();
//
//                            JOptionPane.showMessageDialog(null, "更新完成，请重新启动程序", "提示", JOptionPane.INFORMATION_MESSAGE);
//                        }
//                    });
//                } else {
//                    System.out.println("no new version.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };

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
