package com.github.professor_x_core.util;

import java.util.Date;

/**
 *
 * @author xin.cao@100credit.com
 */
public class Logger {

    public static void info(Object message) {
        System.out.println(String.format("[%s-%s] %s", Thread.currentThread().getName(), new Date().toString(), message));
    }
}
