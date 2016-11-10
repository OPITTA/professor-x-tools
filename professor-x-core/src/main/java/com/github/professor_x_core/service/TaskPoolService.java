package com.github.professor_x_core.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author xin.cao@100credit.com
 */
public class TaskPoolService {

    private static TaskPoolService taskPool;

    private TaskPoolService() {
    }

    public static TaskPoolService getInstance() {
        if (taskPool == null) {
            taskPool = new TaskPoolService();
        }
        return taskPool;
    }

    private final BlockingQueue<Object> tasks = new LinkedBlockingDeque<Object>();

    public Object remove() throws InterruptedException {
        return tasks.take();
    }

    public void add(Object ps) throws InterruptedException {
        tasks.put(ps);
    }

    public int size() {
        return tasks.size();
    }
}
