package com.lit.commons.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射工具类
 * User : liulu
 * Date : 2016-10-2 9:48
 */
public class ClassUtils {

    /**
     * 获取指定对象的属性值
     *
     * @param field
     * @param obj
     * @return
     */
    public static Object getFieldValue(Field field, Object obj) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取对象的属性值失败：" + field.getName(), e);
        }
    }

    /**
     * 设置指定对象的属性值
     *
     * @param field
     * @param obj
     * @param value
     */
    public static void setFieldValue(Field field, Object obj, Object value) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("设置对象的属性值失败：" + field.getName(), e);
        }
    }

    /**
     * 执行方法
     *
     * @param method 要执行的方法
     * @param obj    执行方法的对象
     * @param value  执行方法的参数
     * @return
     */
    public static Object invokeMethod(Method method, Object obj, Object... value) {
        methodAccessible(method);
        try {
            return method.invoke(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Method 调用失败 " + (method == null ? "null" : method.getName()), e);
        }
    }

    /**
     * 设置method访问权限
     *
     * @param method
     */
    public static void methodAccessible(Method method) {
        if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    /**
     * 初始化实例
     *
     * @param clazz
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("根据class创建实例失败:" + (clazz == null ? "null" : clazz.getName()), e);
        }
    }

    /**
     * 初始化实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(String clazz) {

        try {
            Class<?> loadClass = getDefaultClassLoader().loadClass(clazz);
            return loadClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("根据class创建实例失败:" + clazz, e);
        }
    }

    /**
     * 加载类
     *
     * @param clazz
     * @return
     */
    public static Class<?> loadClass(String clazz) {
        try {
            return getDefaultClassLoader().loadClass(clazz);
        } catch (Exception e) {
            throw new RuntimeException("根据class名称加载class失败:" + clazz, e);
        }
    }

    /**
     * 当前线程的classLoader
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
