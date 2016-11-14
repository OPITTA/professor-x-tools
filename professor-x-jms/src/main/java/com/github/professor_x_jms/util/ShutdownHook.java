/**
 * 欢迎浏览和修改代码，有任何想法可以email我
 */
package com.github.professor_x_jms.util;

import com.github.professor_x_jms.model.Configure;

/**
 *
 * @author 510655387@qq.com
 */
public class ShutdownHook extends Thread {

    @Override
    public void run() {
        super.run();
        JmsUtils.getInstance(Configure.getConfigure().getJms()).close();
    }

}
