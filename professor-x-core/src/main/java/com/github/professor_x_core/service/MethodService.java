package com.github.professor_x_core.service;

import com.github.annotion_scan.AnnotationDetector;
import com.github.annotion_scan.AnnotationDetector.TypeReporter;
import com.github.professor_x_core.annotation.Test;
import com.github.professor_x_core.interfaces.Method;
import com.github.professor_x_core.util.Logger;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author 510655387@qq.com
 */
public class MethodService {

    private Map<String, Method> methods = new HashMap<String, Method>();
    private static MethodService methodService;

    private MethodService() {
        start();
    }

    public static MethodService getInstance() {
        if (MethodService.methodService == null) {
            MethodService.methodService = new MethodService();
        }
        return MethodService.methodService;
    }

    public void start() {
        long begin = System.currentTimeMillis();
        Logger.info("Scanning  annotation, this may take a while, please wait...");
        this.scanForMethods();
        long cost = System.currentTimeMillis() - begin;
        Logger.info(String.format("Finished scan for annotation, found {%s} methods(s), cost={%s} ms", methods.size(), cost));
    }

    /**
     * 扫面所有的Method
     */
    public void scanForMethods() {
        final Map<String, Method> innerMethods = new HashMap<String, Method>();

        /**
         * 内部匿名类
         */
        TypeReporter reporter = new TypeReporter() {
            @Override
            @SuppressWarnings("unchecked")
            public Class<? extends java.lang.annotation.Annotation>[] annotations() {
                return new Class[]{Test.class};
            }

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> annotationClass, String className) {
                try {
                    Class clazz = Class.forName(className);
                    if (!Method.class.isAssignableFrom(clazz)) {
                        throw new IllegalArgumentException("类 <" + className + "> 没有实现接口 Method");
                    }
                    Test testAnnotation = (Test) clazz.getAnnotation(annotationClass);
                    String name = testAnnotation.name();
                    if (name == null || name.isEmpty()) {
                        throw new IllegalArgumentException("类 <" + className + "> 的 Test Annotation 设置不正确，name 属性必须设置");
                    }
                    Method method = (Method) clazz.newInstance();
                    innerMethods.put(name, method);
                    Logger.info("Found Method, name=" + name + ", Method.class=" + className);
                } catch (ClassNotFoundException e) {
                    Logger.info(String.format("没有找到类 <" + className + ">。error=", e.getMessage()));
                    System.exit(1);
                } catch (Exception e) {
                    Logger.info("Test Annotation 使用不正确，请检查。error=" + e.getMessage());
                    System.exit(1);
                }
            }
        };
        final AnnotationDetector cf = new AnnotationDetector(reporter);
        try {
            cf.detect();
            this.methods = Collections.unmodifiableMap(innerMethods);
        } catch (IOException ex) {
            Logger.info(String.format("Faild to scan methods error={%s}", ex.getMessage()));
            System.exit(1);
        }
    }

    /**
     * 获得name 对应的类 Method
     *
     * @param name
     * @return
     */
    public Method getMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        Logger.info(String.format("method which name = {} is not exist", name));
        return null;
    }

    /**
     *
     * @return
     */
    public List<String> getMethodNames() {
        List<String> names = new ArrayList<String>();
        for (Entry<String, Method> e : methods.entrySet()) {
            String name = e.getKey();
            names.add(name);
        }
        return names;
    }
}
