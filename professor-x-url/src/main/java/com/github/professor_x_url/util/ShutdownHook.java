package com.github.professor_x_url.util;

import com.github.professor_x_core.util.Logger;
import java.io.IOException;

/**
 *
 * @author 510655387@qq.com
 */
public class ShutdownHook extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            HttpClientUtils.getInstance().close();
        } catch (IOException ex) {
            Logger.info(ex.getMessage());
        }
    }

}
