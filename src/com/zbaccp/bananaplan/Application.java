package com.zbaccp.bananaplan;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zbaccp.bananaplan.bean.Student;
import com.zbaccp.bananaplan.handler.FileHandler;
import com.zbaccp.bananaplan.util.FileUtil;
import com.zbaccp.bananaplan.util.SimilarityAnalysis;

public class Application {
    private Scanner input = new Scanner(System.in);

    /**
     * ���ڷ���¼������ҵû����HashMap
     */
    private HashMap<String, String> videoMap = null;
    private HashMap<String, ArrayList<File>> videoSimilarMap = null;
    private HashMap<String, ArrayList<File>> homeworkMap = null;

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
            System.out.println("1. ѡ��༶");
            System.out.println("0. �˳�����");
            System.out.println("----------------------------------------");
            System.out.print("��ѡ��");

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
                        System.out.println("�˵�ѡ�����������ѡ��");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("����������");
                input.next();
            }
        } while (isGoOn);

        System.out.println("�����˳�...");
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

            System.out.println(Config.CLASS_OTHER + ". �����༶");
            System.out.println("0. ����");
            System.out.println("----------------------------------------");
            System.out.print("��ѡ��༶��ţ�");

            try {
                int menuId = input.nextInt();
                if (menuId == 0) {
                    isGoOn = false;
                } else if (menuId <= Config.classNameList.size()) {
                    Config.initStudentList(menuId - 1);
                    showOptMenu();
                } else if (menuId == Config.CLASS_OTHER) {
                    Config.classIndex = Config.CLASS_OTHER;
                    showOptMenu();
                } else {
                    System.out.println("�༶���ѡ�����������ѡ��");
                }
            } catch (InputMismatchException e) {
                System.out.println("����������");
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
                System.out.println("1. ���˳齱");
                System.out.println("2. �ϻ�������");
            }

            System.out.println("3. ��ҵ����");
            System.out.println("4. ����ѧԱѡ�����");
            System.out.println("0. ����");
            System.out.println("----------------------------------------");
            System.out.print("��ѡ��");

            try {
                menuId = input.nextInt();

                if (Config.classIndex == Config.CLASS_OTHER) {
                    if (menuId > 0 && menuId < 3) {
                        menuId = -1;
                    }
                }

                switch (menuId) {
                    case 0:
                        System.out.println("������һ��");
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
                        System.out.println("�˵�ѡ�����������ѡ��");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("����������");
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

                    // ��ѧ��������ɾ��ѧ������
                    Student.removeStudent(list, stu.id);

                    if (stuCount == 1) {
                        list = null;
                        System.out.println("ȫ������!");
                        break;
                    }
                }
            }

            System.out.println();
            System.out.print("�Ƿ������y/n��");
            yes = input.next();
        } while (!yes.equals("n"));

        return list;
    }

    private void progRace() {
        String title = null;

        do {
            Scanner input = new Scanner(System.in);

            System.out.println();
            System.out.print("��������⣨����n�˳�����");
            title = input.nextLine();

            if (title != null && !title.equals("n")) {
                Date startTime = new Date();
                int stuId = -1;

                int[] idArray = new int[Config.classStuList.size()];
                do {
                    try {
                        System.out.println();
                        System.out.print("����������ϻ���ѧԱ��ţ�����0�˳�����");
                        stuId = input.nextInt();

                        if (stuId > 0) {
                            Student stu = Student.getStudent(Config.classStuList, stuId);
                            if (stu != null) {
                                if (saveProgRace(title, startTime, stu, idArray)) {
                                    break;
                                }
                            } else {
                                System.out.println("�޴�ѧԱ");
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("����������");
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
                System.out.println(stu.name + " ���ύ");
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
                fileUtil.writeLine("���\t����\t��ʱ\t���ʱ��");
            }

            Date cur = new Date();
            int t = (int) (cur.getTime() - start.getTime()) / 1000;
            int minutes = t / 60;
            int seconds = t % 60;

            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(cur);

            fileUtil.writeLine(stu.id + "\t" + stu.name + "\t" + minutes + "��" + seconds + "��" + "\t" + time);

            for (int i = 0; i < idArray.length; i++) {
                if (idArray[i] == 0) {
                    idArray[i] = stu.id;

                    if (i == Config.classStuList.size() - 1) {
                        System.out.println("ȫ�����");
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
     * ��̴�������ȡδ���ѧԱ��Ϣ
     *
     * @param idArray �����ѧԱid����
     * @return ƴ�Ӻ���ַ���
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
                    sb.append(stu.id + "\t" + stu.name + "\tδ���\r\n");
                }
            }
        }

        if (sb.length() > 0) {
            sb.insert(0, "���\t����\t״̬\r\n");
        }

        return sb.toString();
    }

    /**
     * ��ҵ����
     */
    private void homework() {
        Scanner input = new Scanner(System.in);

        System.out.print("��������ҵ���ڵ�·����");
        String path = input.nextLine();

        if (path == null || path.equals("")) {
            System.out.println("·������Ϊ��");
            return;
        }

        path = path.replace('\\', '/');

        int masterDeepth = 0;
        if (Config.classIndex == Config.CLASS_OTHER) {
            System.out.print("��������ѧԱ�����������ļ��е���ȣ�Ĭ������0����");
            try {
                masterDeepth = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("����������");
                input.next();
                return;
            }
        }

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
            className = "�����༶";
        }

        String destPath = "report/" + className + "/" + date;
        String videoPath = destPath + "/video.txt";
        String homeworkPath = destPath + "/homework.txt";

        videoMap = new HashMap<String, String>();
        videoSimilarMap = new HashMap<String, ArrayList<File>>();
        homeworkMap = new HashMap<String, ArrayList<File>>();

        dfs(path, destPath, FileUtil.getDirName(path), masterDeepth, 0, homeworkHandler);
//        bfs(path, destPath, masterDeepth, homeworkHandler);

        // ����¼�����������д���ļ�
        doVideoCheck(videoPath);
        videoMap = null;
        videoSimilarMap = null;

        // ������ҵ
        doHomeworkCheck(homeworkPath);
        homeworkMap = null;
    }

    /**
     * ���¼��
     *
     * @param path �������������ļ�·��
     */
    private void doVideoCheck(String path) {
        if (!videoMap.isEmpty()) {
            int index = 0;
            String[][] logs = new String[videoMap.size()][];

            for (Map.Entry<String, String> entry : videoMap.entrySet()) {
                String detail = entry.getValue();
                String size = detail.substring(0, detail.indexOf("MB"));
                String line = entry.getKey() + "\t" + entry.getValue();
                logs[index++] = new String[]{size, line};
            }

            FileUtil fileUtil = new FileUtil(path, true);

            // ����¼���ļ��Ĵ�С��������
            for (int i = 0; i < logs.length; i++) {
                for (int j = 0; j < logs.length - 1 - i; j++) {
                    if (Integer.parseInt(logs[j][0]) > Integer.parseInt(logs[j + 1][0])) {
                        String[] temp = logs[j];
                        logs[j] = logs[j + 1];
                        logs[j + 1] = temp;
                    }
                }

                System.out.println(logs[logs.length - 1 - i][1]);
                fileUtil.writeLine(logs[logs.length - 1 - i][1]);
            }

            // ���û��¼����ѧԱ
            if (Config.classIndex != Config.CLASS_OTHER) {
                for (int i = 0; i < Config.classStuList.size(); i++) {
                    Student stu = Config.classStuList.get(i);
                    if (stu != null && videoMap.get(stu.name) == null) {
                        System.out.println(stu.name + "\t" + "û��¼��");
                        fileUtil.writeLine(stu.name + "\t" + "û��¼��");
                    }
                }
            }

            // ���¼����Ϯ��ѧԱ
            ArrayList<String> list = walkSimilarFiles(videoSimilarMap);

            for (int i = 0; i < list.size(); i++) {
                String result = list.get(i);
                System.out.println(result);
                fileUtil.writeLine(result);
            }

            System.out.println("\nд���ļ��ɹ���" + path + " -> �ϲ���ļ�¼\n");
            fileUtil.close();

        } else {
            System.out.println("û���ҵ�����");
        }
    }

    /**
     * ������ҵ
     *
     * @param path �������������ļ�·��
     */
    private void doHomeworkCheck(String path) {
        FileUtil fileUtil = new FileUtil(path, true);

        // ���û����ҵ��ѧԱ
        if (Config.classIndex != Config.CLASS_OTHER) {
            for (int i = 0; i < Config.classStuList.size(); i++) {
                Student stu = Config.classStuList.get(i);
                if (stu != null && homeworkMap.get(stu.name) == null) {
                    System.out.println(stu.name + "\t" + "û����ҵ");
                    fileUtil.writeLine(stu.name + "\t" + "û����ҵ");
                }
            }

            System.out.println();
        }

        // �������ƶȷ���
        ArrayList<String> list = walkSimilarFiles(homeworkMap);

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int similar1 = Integer.parseInt(o1.substring(o1.lastIndexOf('��') + 1));
                int similar2 = Integer.parseInt(o2.substring(o2.lastIndexOf('��') + 1));
                if (similar1 > similar2) {
                    return -1;
                } else if (similar1 < similar2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        // �������ƶȷ��������list
        for (int i = 0; i < list.size(); i++) {
            String result = list.get(i);
            System.out.println(result);
            fileUtil.writeLine(result);
        }

        System.out.println();
        System.out.println("д���ļ��ɹ���" + path);
        fileUtil.close();
    }

    /**
     * ����map����������ļ����ƶ�
     * @param map
     * @return
     */
    private ArrayList<String> walkSimilarFiles(HashMap<String, ArrayList<File>> map) {
        ArrayList<String> list = new ArrayList<String>();

        Iterator<Map.Entry<String, ArrayList<File>>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, ArrayList<File>> entry1 = it1.next();
            String master = entry1.getKey();

            Iterator<Map.Entry<String, ArrayList<File>>> it2 = map.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, ArrayList<File>> entry2 = it2.next();
                String master2 = entry2.getKey();

                if (!master.equals(master2)) {
                    ArrayList<String> resultList = checkSimilar(master, entry1.getValue(), master2, entry2.getValue());
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
     * ���������ƶ�
     *
     * @param master1 ѧԱ����
     * @param list1   ��һ��������������
     * @param master2 ѧԱ����
     * @param list2   �ڶ���������������
     * @return �������ƶȽ����
     */
    private ArrayList<String> checkSimilar(String master1, ArrayList<File> list1, String master2, ArrayList<File> list2) {
        ArrayList<String> list = new ArrayList<String>();
        SimilarityAnalysis analysis = new SimilarityAnalysis();

        for (int i = 0; i < list1.size(); i++) {
            File file1 = list1.get(i);

            for (int j = 0; j < list2.size(); j++) {
                File file2 = list2.get(j);

                int similar = 0;
                if (file1.getName().equals(file2.getName())) {
                    similar += 5;
                }

                if (file1.lastModified() == file2.lastModified()) {
                    similar += 100;
                }

                if (inExtList(file1.getName(), 0) && inExtList(file2.getName(), 0)) {
                    if (similar < 100) {
                        String content1 = FileUtil.readAll(file1.getAbsolutePath());
                        String content2 = FileUtil.readAll(file2.getAbsolutePath());

                        if (content1.equals(content2)) {
                            similar += 100;
                        } else if (content1.equalsIgnoreCase(content2)) {
                            similar += 95;
                        }

                        if (similar < 95) {
                            String content1Tidy = content1.replaceAll("\\r|\\n|\\s", "");
                            String content2Tidy = content2.replaceAll("\\r|\\n|\\s", "");

                            if (content1Tidy.equals(content2Tidy)) {
                                similar += 90;
                            } else if (content1Tidy.equalsIgnoreCase(content2Tidy)) {
                                similar += 85;
                            } else {
                                // ���ƶȷ���
                                similar += analysis.check(content1, content2);
                            }
                        }
                    }
                }

                if (similar >= 60) {
                    list.add(master1 + "��" + file1.getName() + "  <->  " + master2 + "��" + file2.getName() + "�����ƶȣ�" + similar);
                }
            }
        }

        return list;
    }

    /**
     * ��ȡѡ�����
     */
    private void extractSelectAnswer() {
        Scanner input = new Scanner(System.in);

        System.out.print("�������ļ�������·����");
        String path = input.nextLine();

        int masterDeepth = 0;
        if (Config.classIndex == Config.CLASS_OTHER) {
            System.out.print("��������ѧԱ�����������ļ��е���ȣ�Ĭ������0����");
            masterDeepth = input.nextInt();
        }

//        dfs(path, path, FileUtil.getDirName(path), masterDeepth, 0, copySelectAnswerHandler);
        bfs(path, path, masterDeepth, copySelectAnswerHandler);
    }

    /**
     * �Ƿ����ļ���չ����ѡ�б���
     * @param name �ļ�������չ��
     * @param type 0��Դ���ļ���1����Ƶ�ļ�
     * @return �Ƿ�ƥ��
     */
    private boolean inExtList(String name, int type) {
        if (name == null || name.equals("")) {
            return false;
        }

        int index = name.lastIndexOf('.');
        if (index == -1) {
            return false;
        }

        String ext = name.substring(index);

        String[] extList = null;

        switch (type) {
            case 1:
                extList = Config.VIDEO_EXT_LIST;
                break;
            default:
                extList = Config.CODE_EXT_INCLUDE_LIST;
                break;
        }

        for (int i = 0; i < extList.length; i++) {
            if (ext.trim().equalsIgnoreCase(extList[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * ¼������ҵ�����Ļص�
     */
    private FileHandler homeworkHandler = new FileHandler() {
        @Override
        public void callback(String destPath, String master, File file) {
            String fileName = file.getName();
            HashMap<String, ArrayList<File>> map = null;

            if (inExtList(fileName, 1)) {
                int fileSize = (int) (file.length() / 1024 / 1024);
                String fileModifiedTime = new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date(file.lastModified()));
                String fileDetail = file.getName() + " [" + fileModifiedTime + " " + fileSize + "MB" + "]";

                if (!videoMap.containsKey(master)) {
                    videoMap.put(master, fileSize + "MB" + "\t" + fileDetail);
                } else {
                    String[] log = videoMap.get(master).split("\t");
                    int totalSize = Integer.parseInt(log[0].substring(0, log[0].indexOf("MB")));
                    videoMap.put(master, (totalSize + fileSize) + "MB" + "\t" + log[1] + " + " + fileDetail);
                }

                map = videoSimilarMap;

            } else if (inExtList(fileName, 0)) {
                // �ų� C# ��Ŀ�� bin��obj��Properties �ļ����ڵ��ļ�
                if (fileName.endsWith(".cs") || fileName.endsWith(".txt")) {
                    if (file.getParentFile().getParent().replace("\\", "/").endsWith("/bin") || file.getParentFile().getParent().replace("\\", "/").endsWith("/obj") || file.getParent().replace("\\", "/").endsWith("/Properties")) {
                        return;
                    }
                }

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
     * ��ȡѧԱѡ����𰸵�excel��ѧԱ���ļ��еĻص�
     */
    private FileHandler copySelectAnswerHandler = new FileHandler() {
        @Override
        public void callback(String destPath, String master, File file) {
            String fileName = file.getName();
            if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                String fileExt = fileName.substring(fileName.lastIndexOf('.'));
                destPath = destPath + "/ѧԱ��";

                File dest = new File(destPath);
                if (!dest.exists()) {
                    if (!dest.mkdir()) {
                        return;
                    }
                }

                destPath = destPath + "/" + master + fileExt;
                dest = new File(destPath);
                FileUtil.copyFile(file, dest);

                System.out.println("д���ļ��ɹ���" + destPath);
            }
        }
    };

    /**
     * ������ȱ����ļ�
     *
     * @param path         �ļ���·��
     * @param destPath     Ҫд���Ŀ���ļ���·�����ò�������Ϊnull
     * @param master       ��ǰ�ļ�����������
     * @param masterDeepth ���������������ļ��е����
     * @param curDeepth    ��ǰ�ļ��е����
     * @param handler      �ص������ļ�����
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
     * ������ȱ����ļ�
     *
     * @param path         �ļ���·��
     * @param masterDeepth ���������������ļ��е����
     * @param handler      �ص������ļ�����
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
