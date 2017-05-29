package com.zbaccp.bananaplan.bean;

import java.util.ArrayList;

public class Student {
    public static ArrayList<Student> list;

    public int id;
    public String name;

    public Student() {
    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Student getStudent(ArrayList<Student> list, int stuId) {
        Student stu = null;

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                stu = list.get(i);
                if (stu != null && stu.id == stuId) {
                    return stu;
                }
            }
        }

        return null;
    }

    /**
     * ����������ѯѧ��
     *
     * @param list       ѧ������
     * @param name       Ҫ��ѯ��ѧ������
     * @param equalsType ƥ�䷽ʽ��0����ȷƥ�� 1����ģ��ƥ�� 2��˫ģ��ƥ��
     * @return ѧ������
     */
    public static Student getStudent(ArrayList<Student> list, String name, int equalsType) {
        if (list != null && name != null && !name.equals("")) {
            Student stu = null;

            for (int i = 0; i < list.size(); i++) {
                stu = list.get(i);

                if (stu != null) {
                    switch (equalsType) {
                        case 0:
                            if (stu.name.equals(name)) {
                                return stu;
                            }
                            break;
                        case 1:
                            if (name.contains(stu.name)) {
                                return stu;
                            }
                            break;
                        case 2:
                            if (name.contains(stu.name) || stu.name.contains(name)) {
                                return stu;
                            }
                            break;
                    }
                }
            }
        }

        return null;
    }

    public static boolean removeStudent(ArrayList<Student> list, int stuId) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Student stu = list.get(i);

                if (stu != null) {
                    if (stu.id == stuId) {
                        list.remove(i);
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
