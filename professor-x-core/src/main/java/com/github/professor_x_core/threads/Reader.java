package com.github.professor_x_core.threads;

import com.github.professor_x_core.interfaces.Source;
import com.github.professor_x_core.util.Logger;

/**
 * 单线程读取测试数据
 *
 * @author 510655387@qq.com
 */
public class Reader implements Runnable {

    private static Source source;

    private Reader() {
    }

    public static void start(Source source) {
        Reader.source = source;
        Logger.info("读线程启动");
        Reader reader = new Reader();
        Thread thread = new Thread(reader, "读线程");
        thread.start();
    }

    @Override
    public void run() {
        Reader.source.read();
        Logger.info("读线程关闭");
    }

}
