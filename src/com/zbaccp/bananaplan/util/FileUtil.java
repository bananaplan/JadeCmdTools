package com.zbaccp.bananaplan.util;

import info.monitorenter.cpdetector.io.*;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtil {
    private String filePath;
    private BufferedReader reader;
    private BufferedWriter writer;

    public FileUtil(String filePath) {
        if (filePath != null && !filePath.equals("")) {
            this.filePath = filePath.replace('\\', '/');
        }
    }

    public FileUtil(String filePath, boolean newFile) {
        this(filePath);
        delete();
    }

    private BufferedReader openReader() {
        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        InputStream is = null;
        InputStreamReader isr = null;

        try {
            String encoding = detectEcoding(file);
            System.out.println(encoding);

            is = new FileInputStream(file);

            if (encoding == null) {
                isr = new InputStreamReader(is);
            } else {
                isr = new InputStreamReader(is, encoding);
            }

            reader = new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return reader;
    }

    private BufferedWriter openWriter(boolean append) {
        if (filePath == null || filePath.equals("")) {
            return null;
        }

        int index = filePath.lastIndexOf('/');
        if (index != -1) {
            String dirPath = filePath.substring(0, index);
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream os = null;
        OutputStreamWriter osw = null;

        try {
            os = new FileOutputStream(file, append);
            osw = new OutputStreamWriter(os);
            writer = new BufferedWriter(osw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return writer;
    }

    private BufferedWriter openWriter() {
        return openWriter(true);
    }

    public String readLine() {
        String line = null;

        if (reader == null) {
            reader = openReader();
        }

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    public boolean writeLine(String line) {
        if (line != null && !line.equals("")) {
            if (!line.endsWith("\r\n")) {
                line = line + "\r\n";
            }
        } else {
            return false;
        }

        if (writer == null) {
            writer = openWriter();
        }

        try {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean write(String content, boolean append) {
        if (content == null || content.equals("")) {
            return false;
        }

        if (writer == null) {
            writer = openWriter(append);
        }

        try {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean delete() {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }

        return false;
    }

    private void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWriter() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        closeReader();
        closeWriter();
    }

    public static boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     */
    public static void copyFile(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDirName(String path) {
        path = path.replace('\\', '/');
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static String getFileName(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');

        if (index != -1) {
            return name.substring(0, index);
        } else {
            return name;
        }
    }

    public static String getExtName(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');

        if (index != -1) {
            return name.substring(index);
        } else {
            return null;
        }
    }

    public static String readAll(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("文件不存在，或者不是文件");
            return null;
        }

        byte[] data = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String content = null;
        try {
            String encoding = detectEcoding(path);

            if (encoding == null) {
                encoding = "utf-8";
            }

            content = new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 检测文件字符编码
     * @param path 文件路径
     * @return 字符编码
     */
    private static String detectEcoding(String path) {
        return detectEcoding(new File(path));
    }

    private static String detectEcoding(File file) {
        // Create the proxy:
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance(); // A singleton.

        // Add the implementations of info.monitorenter.cpdetector.io.ICodepageDetector:
        // This one is quick if we deal with unicode codepages:
        detector.add(new ByteOrderMarkDetector());
        // The first instance delegated to tries to detect the meta charset attribut in html pages.
        detector.add(new ParsingDetector(false)); // be verbose about parsing.
        // This one does the tricks of exclusion and frequency detection, if first implementation is
        // unsuccessful:
        detector.add(JChardetFacade.getInstance()); // Another singleton.
        detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.

        // Work with the configured proxy:
        java.nio.charset.Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURL());
        } catch (IOException e) {
            return null;
        }

        if (charset != null) {
            // Open the document in the given code page:
//            try {
//                Reader reader = new InputStreamReader(new FileInputStream(file), charset);
//            } catch (FileNotFoundException e) {
//                return null;
//            }

            return charset.name();
        }

        return null;
    }

}
