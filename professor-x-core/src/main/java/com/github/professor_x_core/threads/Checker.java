package com.github.professor_x_core.threads;

import com.github.professor_x_core.model.Report;
import com.github.professor_x_core.service.TaskPoolService;
import com.github.professor_x_core.util.Logger;

/**
 * 状态检测线程
 *
 * @author 510655387@qq.com
 */
public class Checker implements Runnable {

    private static long CD = 1 * 1000;

    private Checker() {
    }

    public static void start() {
        start(-1);
    }

    private static final TaskPoolService taskPool = TaskPoolService.getInstance();
    public int CONFIRM_NUMBER = 0;
    public static int CONFIRM_NUMBER_MAX = 2;

    public static void start(long cd) {
        if (cd > 0) {
            Checker.CD = cd;
        }
        Logger.info("状态检测线程启动");
        Checker checker = new Checker();
        Thread thread = new Thread(checker, "状态检测线程");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(CD);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            int size = taskPool.size();
            if (CONFIRM_NUMBER >= CONFIRM_NUMBER_MAX) {
                Logger.info("正在关闭工作线程");
                Worker.close();
                Logger.info("状态检测线程关闭");
                long endTime = System.currentTimeMillis();
                Report report = Report.getInstance();
                int total = report.getTotal();
                Logger.info(String.format("样本大小 = %s (个), 并发数 = %s (个), 响应时间 = %s/%s/%.2f (最小/最大/平均 ms), 吞吐量 = %.2f (个/秒), 错误率 = %.2f 百分号",
                        total,
                        report.getConcurrent(),
                        report.getMinCostTime(),
                        report.getMaxCostTime(),
                        report.getConcurrenceCostTime().get() * 1.0 / total,
                        (total * 1.0 / (endTime - report.getStartTime())) * 1000,
                        (report.getErrorNumber() * 1.0 / total) * 100));
                return;
            }
            if (size == 0) {
                CONFIRM_NUMBER++;
            }
            Logger.info("飘过");
        }
    }
}
