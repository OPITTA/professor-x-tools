package com.github.professor_x_core.util;

import com.github.professor_x_core.interfaces.Source;
import com.github.professor_x_core.service.TaskPoolService;

/**
 *
 * @author xin.cao@100credit.com
 */
public class SequenceSource implements Source {

    private static final TaskPoolService taskPool = TaskPoolService.getInstance();
    private final int max;

    public SequenceSource(int max) {
        this.max = max;
    }

    @Override
    public int read() {
        for (int i = 1; i <= max; i++) {
            try {
                taskPool.add(i);
            } catch (InterruptedException ex) {
                return -1;
            }
        }
        return max;
    }
}
