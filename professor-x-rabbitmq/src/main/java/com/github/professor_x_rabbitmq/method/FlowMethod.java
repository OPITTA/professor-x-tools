/**
 * 欢迎浏览和修改代码，有任何想法可以email我
 */
package com.github.professor_x_rabbitmq.method;

import com.github.professor_x_core.annotation.Test;
import com.github.professor_x_core.interfaces.Method;
import com.github.professor_x_core.util.Logger;
import com.github.professor_x_rabbitmq.constent.SourceType;
import static com.github.professor_x_rabbitmq.constent.SourceType.FILE;
import static com.github.professor_x_rabbitmq.constent.SourceType.SEQUENCE;
import com.github.professor_x_rabbitmq.model.Configure;
import com.github.professor_x_rabbitmq.model.Request;
import com.github.professor_x_rabbitmq.util.RabbitmqTemplate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 510655387@qq.com
 */
@Test(name = "FlowMethod")
public class FlowMethod extends Method {

    @Override
    public void exec(Object params) throws Exception {
        List<Request> requests = Configure.getConfigure().getFlow();
        switch (SourceType.fromName(Configure.getConfigure().getSource().getType())) {
            case FILE: {
                List<String> values = (List<String>) params;
                Logger.info(values.toString());
                for (Request request : requests) {
                    if (request.getData() != null && !request.getData().isEmpty()) {
                        for (Map.Entry<String, Object> e : request.getData().entrySet()) {
                            String k = e.getKey();
                            String v = String.valueOf(e.getValue());
                            if (!v.isEmpty()) {
                                if (v.charAt(0) == '%') {
                                    Integer index = Integer.valueOf(v.substring(1, v.length()));
                                    request.getData().put(k, values.get(index));
                                }
                            }
                        }
                    }
//                    JmsUtils.getInstance(Configure.getConfigure().getJms()).send(request.getDestination(), request.getData());
                }
                break;
            }
            case SEQUENCE: {
                Logger.info((Integer) params);
                for (Request request : requests) {
//                    JmsUtils.getInstance(Configure.getConfigure().getJms()).send(request.getDestination(), request.getData());
                }
                break;
            }
            default: {
                Logger.info("不能识别的参数来源");
            }
        }
    }

}
