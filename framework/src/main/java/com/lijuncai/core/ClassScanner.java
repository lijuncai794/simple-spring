package com.lijuncai.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @description: ��ɨ�蹤����, ����ɨ���ಢ������ؽ���
 * @author: lijuncai
 **/
public class ClassScanner {

    /**
     * ɨ��ָ����·���µ���
     *
     * @param packageName ����
     * @return List<Class < ?>>,ɨ�赽����
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        /**
         * ������ת��Ϊ·��
         * ��ȡĬ�ϵ�ϵͳ�������
         * ��ȡ����·���µ���Դ
         */
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        //����ÿһ����ȡ������Դ
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            //��Ŀ����jar�������ﴦ����Դ������jar�������
            if (resource.getProtocol().contains("jar")) {
                //��ȡjar���ľ���·��
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //����ȡ�������������
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else {
                //����������Դ��δ����
            }
        }
        return classList;
    }

    /**
     * ��ȡjar���µ������࣬�������Ϊjar���ľ���·����������·��(���ڻ�ȡ��Ҫ����)
     *
     * @param jarFilePath jar���ľ���·��
     * @param path        ������·��
     * @return ������List<Class < ?>>,�������˻�ȡ������
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        /**
         * ͨ��jar��·����ȡjarFile
         * ��ȡjarFileʵ��:�ļ�
         * ����jarFileʵ�壬��ȡ��Ҫ����
         */
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            //��ȡָ��·���µ���
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
