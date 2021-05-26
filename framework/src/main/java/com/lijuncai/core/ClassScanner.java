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
 * @description: 类扫描工具类, 用于扫描类并将其加载进来
 * @author: lijuncai
 **/
public class ClassScanner {

    /**
     * 扫描指定包路径下的类
     *
     * @param packageName 包名
     * @return List<Class < ?>>,扫描到的类
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        /**
         * 将包名转换为路径
         * 获取默认的系统类加载器
         * 获取包名路径下的资源
         */
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        //遍历每一个获取到的资源
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            //项目会打成jar包，这里处理资源类型是jar包的情况
            if (resource.getProtocol().contains("jar")) {
                //获取jar包的绝对路径
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //将获取到的类加入容器
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else {
                //其他类型资源暂未处理
            }
        }
        return classList;
    }

    /**
     * 获取jar包下的所有类，传入参数为jar包的绝对路径和类的相对路径(用于获取需要的类)
     *
     * @param jarFilePath jar包的绝对路径
     * @param path        类的相对路径
     * @return 类容器List<Class < ?>>,里面存放了获取到的类
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        /**
         * 通过jar包路径获取jarFile
         * 获取jarFile实体:文件
         * 遍历jarFile实体，获取需要的类
         */
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            //获取指定路径下的类
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
