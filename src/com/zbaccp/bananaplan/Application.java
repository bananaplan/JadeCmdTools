package com.zbaccp.bananaplan;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zbaccp.bananaplan.bean.CaptureVideo;
import com.zbaccp.bananaplan.bean.Student;
import com.zbaccp.bananaplan.bean.TheSame;
import com.zbaccp.bananaplan.handler.FileHandler;
import com.zbaccp.bananaplan.util.FileUtil;
import com.zbaccp.bananaplan.util.SimilarityAnalysis;

public class Application {
    private Scanner input = new Scanner(System.in);

    /**
     * 用于分析录屏和作业没交的HashMap
     */
    private HashMap<String, ArrayList<File>> videoMap = null;
    public HashMap<String, ArrayList<File>> homeworkMap = null;

    public String videoPath;
    public String homeworkPath;

    public Application() {
        Config.init();
        Config.initFilter();
    }

    public void run() {
        showMainMenu();
    }

    private void showMainMenu() {
        System.out.println("Hi guys, this is a geek tool by bananaplan.");

        int menuId = 0;
        boolean isGoOn = true;
        do {
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("1. 选择班级");
            System.out.println("0. 退出程序");
            System.out.println("----------------------------------------");
            System.out.print("请选择：");

            try {
                menuId = input.nextInt();

                switch (menuId) {
                    case 0:
                        isGoOn = false;
                        break;

                    case 1:
                        showClassList();
                        break;

                    default:
                        System.out.println("菜单选择错误，请重新选择");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("请输入数字");
                input.next();
            }
        } while (isGoOn);

        System.out.println("程序退出...");
    }

    private void showClassList() {
        boolean isGoOn;

        do {
            isGoOn = true;

            System.out.println();
            System.out.println("----------------------------------------");

            for (int i = 0; i < Config.classNameList.size(); i++) {
                String className = Config.classNameList.get(i);
                if (className != null && !className.equals("")) {
                    System.out.println((i + 1) + ". " + className);
                }
            }

            System.out.println(Config.CLASS_OTHER + ". 其他班级");
            System.out.println("0. 返回");
            System.out.println("----------------------------------------");
            System.out.print("请选择班级序号：");

            try {
                int menuId = input.nextInt();
                if (menuId == 0) {
                    isGoOn = false;
                } else if (menuId == Config.CLASS_OTHER) {
                    Config.classIndex = Config.CLASS_OTHER;
                    showOptMenu();
                } else if (menuId <= Config.classNameList.size()) {
                    Config.initStudentList(menuId - 1);
                    showOptMenu();
                } else {
                    System.out.println("班级序号选择错误，请重新选择");
                }
            } catch (InputMismatchException e) {
                System.out.println("请输入数字");
                input.next();
            }

        } while (isGoOn);
    }

    private void showOptMenu() {
        int menuId = -1;

        do {
            System.out.println();
            System.out.println("----------------------------------------");

            if (Config.classIndex != Config.CLASS_OTHER) {
                System.out.println("1. 幸运抽奖");
                System.out.println("2. 上机完成情况");
            }

            System.out.println("3. 作业分析");
            System.out.println("4. 整理学员选择题答案");
            System.out.println("0. 返回");
            System.out.println("----------------------------------------");
            System.out.print("请选择：");

            try {
                menuId = input.nextInt();

                if (Config.classIndex == Config.CLASS_OTHER) {
                    if (menuId > 0 && menuId < 3) {
                        menuId = -1;
                    }
                }

                switch (menuId) {
                    case 0:
                        System.out.println("返回上一级");
                        break;

                    case 1:
                        Student.list = randomStudent(Student.list);
                        break;

                    case 2:
                        progRace();
                        break;

                    case 3:
                        homework();
                        break;

                    case 4:
                        extractSelectAnswer();
                        break;

                    default:
                        System.out.println("菜单选择错误，请重新选择");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("请输入数字");
                input.next();
            }
        } while (menuId != 0);
    }

    private ArrayList<Student> randomStudent(ArrayList<Student> list) {
        if (list == null) {
            list = (ArrayList<Student>) Config.classStuList.clone();
        }

        String yes = null;

        do {
            if (list != null) {
                double r = Math.random();
                int stuCount = list.size();

                if (stuCount > 0) {
                    int index = (int) (r * stuCount);

                    Student stu = list.get(index);
                    System.out.println("id: " + stu.id + ", name: " + stu.name);

                    // 从学生数组中删除学生对象
                    Student.removeStudent(list, stu.id);

                    if (stuCount == 1) {
                        list = null;
                        System.out.println("全部抽完!");
                        break;
                    }
                }
            }

            System.out.println();
            System.out.print("是否继续？y/n：");
            yes = input.next();
        } while (!yes.equals("n"));

        return list;
    }

    private void progRace() {
        String title = null;

        do {
            Scanner input = new Scanner(System.in);

            System.out.println();
            System.out.print("请输入标题（输入n退出）：");
            title = input.nextLine();

            if (title != null && !title.equals("n")) {
                Date startTime = new Date();
                int stuId = -1;

                int[] idArray = new int[Config.classStuList.size()];
                do {
                    try {
                        System.out.println();
                        System.out.print("请输入完成上机的学员编号（输入0退出）：");
                        stuId = input.nextInt();

                        if (stuId > 0) {
                            Student stu = Student.getStudent(Config.classStuList, stuId);
                            if (stu != null) {
                                if (saveProgRace(title, startTime, stu, idArray)) {
                                    break;
                                }
                            } else {
                                System.out.println("无此学员");
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("请输入数字");
                        input.next();
                    }

                } while (stuId != 0);
            }

        } while (!title.equals("n"));
    }

    private boolean saveProgRace(String title, Date start, Student stu, int[] idArray) {
        if (stu == null) {
            return false;
        }

        for (int i = 0; i < idArray.length; i++) {
            if (idArray[i] == stu.id) {
                System.out.println(stu.name + " 已提交");
                return false;
            }
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());

        String dirName = "report/" + Config.classNameList.get(Config.classIndex) + "/" + date;
        File dir = new File(dirName);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return false;
            }
        }

        File file = new File(dirName + "/" + title + ".txt");

        boolean isWriteTitle = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                isWriteTitle = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileUtil fileUtil = new FileUtil(file.getPath());
        FileUtil fileBakUtil = new FileUtil(dirName + "/" + title + ".backup.txt");

        try {
            if (isWriteTitle) {
                fileUtil.writeLine("编号\t姓名\t用时\t完成时间");
            }

            Date cur = new Date();
            int t = (int) (cur.getTime() - start.getTime()) / 1000;
            int minutes = t / 60;
            int seconds = t % 60;

            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(cur);

            fileUtil.writeLine(stu.id + "\t" + stu.name + "\t" + minutes + "分" + seconds + "秒" + "\t" + time);

            for (int i = 0; i < idArray.length; i++) {
                if (idArray[i] == 0) {
                    idArray[i] = stu.id;

                    if (i == Config.classStuList.size() - 1) {
                        System.out.println("全部完成");
                        return true;
                    }

                    break;
                }
            }

        } finally {
            String working = getUnfinished(idArray);
            if (!working.equals("")) {
                fileBakUtil.write(working, false);
            } else {
                fileBakUtil.delete();
            }

            fileUtil.close();
            fileBakUtil.close();
        }

        return false;
    }

    /**
     * 编程大赛，获取未完成学员信息
     *
     * @param idArray 已完成学员id数组
     * @return 拼接后的字符串
     */
    private String getUnfinished(int[] idArray) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < Config.classStuList.size(); i++) {
            Student stu = Config.classStuList.get(i);

            if (stu != null) {
                boolean isFind = false;
                for (int j = 0; j < idArray.length; j++) {
                    if (stu.id == idArray[j]) {
                        isFind = true;
                        break;
                    }
                }

                if (!isFind) {
                    sb.append(stu.id + "\t" + stu.name + "\t未完成\r\n");
                }
            }
        }

        if (sb.length() > 0) {
            sb.insert(0, "编号\t姓名\t状态\r\n");
        }

        return sb.toString();
    }

    /**
     * 作业分析
     */
    private void homework() {
        Scanner input = new Scanner(System.in);

        System.out.print("请输入作业所在的路径：");
        String path = input.nextLine();

        if (path == null || path.equals("")) {
            System.out.println("路径不能为空");
            return;
        }

        int masterDeepth = 0;
        if (Config.classIndex == Config.CLASS_OTHER) {
            System.out.print("请输入以学员姓名命名的文件夹的深度（默认请输0）：");
            try {
                masterDeepth = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("请输入数字");
                input.next();
                return;
            }
        }

        homeworkAnalysis(path, masterDeepth, null);
    }

    public ArrayList<TheSame> homeworkAnalysis(String path, int masterDeepth, ArrayList<String> undoHomework) {
        path = path.replace('\\', '/');

        String date = null;
        Pattern p = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
        Matcher m = p.matcher(path);
        if (m.find()) {
            date = m.group(0);
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        String className = null;
        if (Config.classIndex != Config.CLASS_OTHER) {
            className = Config.classNameList.get(Config.classIndex);
        } else {
            className = "其他班级";
        }

        String destPath = "report/" + className + "/" + date;
        videoPath = destPath + "/video.txt";
        homeworkPath = destPath + "/homework.txt";

        videoMap = new HashMap<String, ArrayList<File>>();
        homeworkMap = new HashMap<String, ArrayList<File>>();

        dfs(path, destPath, FileUtil.getDirName(path), masterDeepth, 0, homeworkHandler);
//        bfs(path, destPath, masterDeepth, homeworkHandler);

        // 分析录屏，并将结果写入文件
        doVideoCheck(videoPath);
        videoMap = null;

        // 分析作业
        ArrayList<TheSame> list = doHomeworkCheck(homeworkPath, undoHomework);
        homeworkMap = null;

        return list;
    }

    /**
     * 检查录屏
     *
     * @param path 分析结果保存的文件路径
     */
    private void doVideoCheck(String path) {
        System.out.println("\n正在分析录屏...");

        if (!videoMap.isEmpty()) {
            // 把 Map 中的数据，封装成 CaptureVideo 录屏信息对象，并转移到 List 中，为下面排序做准备
            ArrayList<CaptureVideo> captureList = new ArrayList<CaptureVideo>();

            for(Map.Entry<String, ArrayList<File>> entry : videoMap.entrySet()) {
                ArrayList<File> files = entry.getValue();
                CaptureVideo capture = new CaptureVideo(entry.getKey(), files);
                captureList.add(capture);
            }

            // 根据录屏文件的大小降序排序
            Collections.sort(captureList, new Comparator<CaptureVideo>() {
                @Override
                public int compare(CaptureVideo o1, CaptureVideo o2) {
                    if (o1.totalLength > o1.totalLength) {
                        return -1;
                    } else if (o1.totalLength < o2.totalLength) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            FileUtil fileUtil = new FileUtil(path, true);

            // 按录屏文件大小，显示输出录屏信息
            String captureInfo = null;
            for (CaptureVideo capture : captureList) {
                captureInfo = capture.toString();
                System.out.println(captureInfo);
                fileUtil.writeLine(captureInfo);
            }

            // 检查没有录屏的学员
            if (Config.classIndex != Config.CLASS_OTHER) {
                if (Config.classStuList.size() > 0) {
                    System.out.println();
                    fileUtil.write("\r\n", true);

                    String info = null;
                    for (int i = 0; i < Config.classStuList.size(); i++) {
                        Student stu = Config.classStuList.get(i);
                        if (stu != null && videoMap.get(stu.name) == null) {
                            info = stu.name + "\t" + "没有录屏";

                            System.out.println(info);
                            fileUtil.writeLine(info);
                        }
                    }
                }
            }

            // 检查录屏抄袭的学员
            ArrayList<TheSame> list = walkSimilarFiles(videoMap);

            if (list.size() > 0) {
                System.out.println();
                fileUtil.write("\r\n", true);

                for (int i = 0; i < list.size(); i++) {
                    TheSame same = list.get(i);
                    System.out.println(same.toString());
                    fileUtil.writeLine(same.toString());
                }
            }

            System.out.println("\n写入文件成功：" + path + " -> 合并后的记录");
            fileUtil.close();

        } else {
            System.out.println("没有找到数据");
        }
    }

    /**
     * 分析作业
     *
     * @param path 分析结果保存的文件路径
     */
    private ArrayList<TheSame> doHomeworkCheck(String path, ArrayList<String> undoHomework) {
        System.out.println("\n正在分析作业，可能耗时较长，请耐心等待...");

        FileUtil fileUtil = new FileUtil(path, true);

        try {
            // 检查没有作业的学员
            if (Config.classIndex != Config.CLASS_OTHER) {
                String info = null;
                for (int i = 0; i < Config.classStuList.size(); i++) {
                    Student stu = Config.classStuList.get(i);
                    if (stu != null && homeworkMap.get(stu.name) == null) {
                        info = stu.name + "\t" + "没交作业";

                        System.out.println(info);
                        fileUtil.writeLine(info);

                        if (undoHomework != null) {
                            undoHomework.add(stu.name);
                        }
                    }
                }

                System.out.println();
            }

            // 代码相似度分析
            ArrayList<TheSame> list = walkSimilarFiles(homeworkMap);

            Collections.sort(list, new Comparator<TheSame>() {
                @Override
                public int compare(TheSame o1, TheSame o2) {
                    if (o1.similar > o2.similar) {
                        return -1;
                    } else if (o1.similar < o2.similar) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            // 遍历相似度分析结果的list
            for (int i = 0; i < list.size(); i++) {
                TheSame same = list.get(i);
                System.out.println(same.toString());
                fileUtil.writeLine(same.toString());
            }

            System.out.println("\n写入文件成功：" + path);

            return list;
        } finally {
            fileUtil.close();
        }
    }

    /**
     * 遍历map，两两检查文件相似度
     *
     * @param map
     * @return
     */
    private ArrayList<TheSame> walkSimilarFiles(HashMap<String, ArrayList<File>> map) {
        ArrayList<TheSame> list = new ArrayList<TheSame>();
        SimilarityAnalysis analysis = new SimilarityAnalysis();

        Iterator<Map.Entry<String, ArrayList<File>>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, ArrayList<File>> entry1 = it1.next();
            String master = entry1.getKey();

            Iterator<Map.Entry<String, ArrayList<File>>> it2 = map.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, ArrayList<File>> entry2 = it2.next();
                String master2 = entry2.getKey();

                if (!master.equals(master2)) {
                    ArrayList<TheSame> resultList = analysis.checkFilesSimilar(master, entry1.getValue(), master2, entry2.getValue());
                    if (resultList != null && !resultList.isEmpty()) {
                        list.addAll(resultList);
                    }
                }
            }

            it1.remove();
        }

        return list;
    }

    /**
     * 提取选择题答案
     */
    private void extractSelectAnswer() {
        Scanner input = new Scanner(System.in);

        System.out.print("请输入文件夹所在路径：");
        String path = input.nextLine();

        int masterDeepth = 0;
        if (Config.classIndex == Config.CLASS_OTHER) {
            System.out.print("请输入以学员姓名命名的文件夹的深度（默认请输0）：");
            masterDeepth = input.nextInt();
        }

//        dfs(path, path, FileUtil.getDirName(path), masterDeepth, 0, copySelectAnswerHandler);
        bfs(path, path, masterDeepth, copySelectAnswerHandler);
    }

    /**
     * 过滤目录和文件
     * @param file 目录或文件的 File 对象
     * @return 是否检查通过
     */
    private boolean doFilter(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        String name = file.getName();

        // 忽略指定目录和文件
        for (int i = 0; i < Config.CODE_EXCLUDE_LIST.size(); i++) {
            String exclude = Config.CODE_EXCLUDE_LIST.get(i);

            if (file.isDirectory()) {
                if (name.equalsIgnoreCase(exclude)) {
                    return false;
                }
            } else {
                if (exclude.indexOf(".") != -1 && name.toLowerCase().indexOf(exclude.toLowerCase()) != -1) {
                    return false;
                }
            }
        }

        if (file.isFile()) {
            if (Config.isVideoFile(name) || Config.isCodeFile(name)) {
                return true;
            }

            return false;
        } else {
            return true;
        }
    }

    /**
     * 录屏和作业分析的回调
     */
    private FileHandler homeworkHandler = new FileHandler() {
        @Override
        public void callback(String destPath, String master, File file) {
            HashMap<String, ArrayList<File>> map = null;

            if (Config.isVideoFile(file.getName())) {
                map = videoMap;
            } else {
                map = homeworkMap;
            }

            if (map != null) {
                ArrayList<File> fileList = null;

                if (!map.containsKey(master)) {
                    fileList = new ArrayList<File>();
                    fileList.add(file);

                    map.put(master, fileList);
                } else {
                    fileList = map.get(master);
                    fileList.add(file);
                }

                map.put(master, fileList);
            }
        }
    };

    /**
     * 提取学员选择题答案的excel到学员答案文件夹的回调
     */
    private FileHandler copySelectAnswerHandler = new FileHandler() {
        @Override
        public void callback(String destPath, String master, File file) {
            String fileName = file.getName();
            if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                String fileExt = fileName.substring(fileName.lastIndexOf('.'));
                destPath = destPath + "/学员答案";

                File dest = new File(destPath);
                if (!dest.exists()) {
                    if (!dest.mkdir()) {
                        return;
                    }
                }

                destPath = destPath + "/" + master + fileExt;
                dest = new File(destPath);
                FileUtil.copyFile(file, dest);

                System.out.println("写入文件成功：" + destPath);
            }
        }
    };

    /**
     * 深度优先遍历文件
     *
     * @param path         文件夹路径
     * @param destPath     要写入的目标文件夹路径，用不到，可为null
     * @param master       当前文件的主人姓名
     * @param masterDeepth 包含主人姓名的文件夹的深度
     * @param curDeepth    当前文件夹的深度
     * @param handler      回调处理文件对象
     */
    private void dfs(String path, String destPath, String master, int masterDeepth, int curDeepth, FileHandler handler) {
        if (path == null || path.equals("")) {
            return;
        }

        path = path.replace('\\', '/');

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        curDeepth++;

        File[] list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            String myMaster = null;
            File file = list[i];

            if (!doFilter(file)) {
                continue;
            }

            if (file.isDirectory()) {
                if (Config.classIndex != Config.CLASS_OTHER) {
                    Student stu = Student.getStudent(Config.classStuList, file.getName(), 2);
                    if (stu != null) {
                        myMaster = stu.name;
                    }
                } else {
                    if (masterDeepth > 0 && masterDeepth == curDeepth) {
                        myMaster = file.getName();
                    }
                }
                dfs(file.getAbsolutePath(), destPath, myMaster != null ? myMaster : master, masterDeepth, curDeepth, handler);
            } else {
                if (Config.classIndex != Config.CLASS_OTHER) {
                    Student stu = Student.getStudent(Config.classStuList, FileUtil.getFileName(file), 2);
                    if (stu != null) {
                        myMaster = stu.name;
                    }
                } else {
                    if (masterDeepth == 0) {
//                        myMaster = FileUtil.getDirName(file.getParent());
                        myMaster = file.getName();
                    }
                }

//                System.out.println((myMaster != null ? myMaster : master) + " -> " + file.getName());
                handler.callback(destPath, myMaster != null ? myMaster : master, file);
            }
        }
    }

    /**
     * 广度优先遍历文件
     *
     * @param path         文件夹路径
     * @param masterDeepth 包含主人姓名的文件夹的深度
     * @param handler      回调处理文件对象
     */
    private void bfs(String path, String destPath, int masterDeepth, FileHandler handler) {
        if (path == null || path.equals("")) {
            return;
        }

        path = path.replace('\\', '/');

        Queue<File> queue = new LinkedList<File>();
        Queue<String> nameQueue = new LinkedList<String>();
        Queue<Integer> deepthQueue = new LinkedList<Integer>();

        File rootDir = new File(path);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return;
        }

        queue.offer(rootDir);
        nameQueue.offer(rootDir.getName());
        deepthQueue.offer(0);

        while (!queue.isEmpty()) {
            File dir = queue.poll();
            String master = nameQueue.poll();
            int curDeepth = deepthQueue.poll();

            curDeepth++;

            File[] list = dir.listFiles();

            for (int i = 0; i < list.length; i++) {
                File file = list[i];
                String myMaster = null;

                if (file.isDirectory()) {
                    if (Config.classIndex != Config.CLASS_OTHER) {
                        Student stu = Student.getStudent(Config.classStuList, file.getName(), 2);
                        if (stu != null) {
                            myMaster = stu.name;
                        }
                    } else {
                        if (masterDeepth > 0 && masterDeepth == curDeepth) {
                            myMaster = file.getName();
                        }
                    }

                    queue.offer(file);
                    nameQueue.offer(myMaster != null ? myMaster : master);
                    deepthQueue.offer(curDeepth);

                } else {
                    if (Config.classIndex != Config.CLASS_OTHER) {
                        Student stu = Student.getStudent(Config.classStuList, FileUtil.getFileName(file), 2);
                        if (stu != null) {
                            myMaster = stu.name;
                        }
                    } else {
                        if (masterDeepth == 0) {
                            myMaster = FileUtil.getDirName(file.getParent());
                        }
                    }

//                    System.out.println((myMaster != null ? myMaster : master) + " -> " + file.getName());
                    handler.callback(destPath, myMaster != null ? myMaster : master, file);
                }
            }
        }
    }

}
