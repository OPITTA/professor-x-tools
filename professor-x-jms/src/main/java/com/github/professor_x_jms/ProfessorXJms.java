/**
 * 欢迎浏览和修改代码，有任何想法可以email我
 */
package com.github.professor_x_jms;

import com.github.professor_x_core.ProfessorXCore;
import com.github.professor_x_core.util.FileSource;
import com.github.professor_x_core.util.Logger;
import com.github.professor_x_core.util.SequenceSource;
import com.github.professor_x_core.web.APIClient;
import com.github.professor_x_jms.constent.SourceType;
import com.github.professor_x_jms.model.Configure;
import com.github.professor_x_jms.model.Jms;
import com.github.professor_x_jms.model.Professor;
import com.github.professor_x_jms.model.Request;
import com.github.professor_x_jms.model.Source;
import com.github.professor_x_jms.util.JmsUtils;
import com.github.professor_x_jms.util.ShutdownHook;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author 510655387@qq.com
 */
public class ProfessorXJms {

    public static void main(String... args) throws FileNotFoundException, IOException {
        String conf;
        File file;
        List<String> cmd = new ArrayList<String>();
        if (args != null && args.length != 0) {
            if (args.length % 2 != 0) {
                help();
                return;
            } else {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < args.length; i += 2) {
                    params.put(args[i], args[i + 1]);
                    cmd.add(args[i]);
                    cmd.add(args[i + 1]);
                }
                if (params.containsKey("-conf")) {
                    conf = params.get("-conf");
                    file = new File(conf);
                    if (!file.exists()) {
                        help();
                        return;
                    }
                } else {
                    help();
                    return;
                }
            }
        } else {
            help();
            return;
        }
        cmd.add("-m");
        cmd.add("FlowMethod");
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Configure configure = objectMapper.readValue(sb.toString(), Configure.class);
        Logger.info(objectMapper.writeValueAsString(configure));
        Configure.setConfigure(configure);
        Professor professor = configure.getProfessor();
        APIClient apic = null;
        if (professor != null) {
            apic = new APIClient(professor.getHost(), professor.getPort(), professor.getAccount(), professor.getPasswd(), professor.getTopic(), professor.getTitle());
            Logger.info("professor 模块启动");
        }
        List<Request> flow = configure.getFlow();
        if (flow == null) {
            Logger.info("flow 模块是必须设置的");
            return;
        }
        Jms jms = configure.getJms();
        if (jms == null) {
            Logger.info("jms 模块是必须设置的");
            return;
        }
        JmsUtils.getInstance(jms);
        Source source = configure.getSource();
        switch (SourceType.fromName(source.getType())) {
            case FILE: {
                if (apic != null) {
                    ProfessorXCore.bootstrap(new FileSource(source.getFilename(), source.getDiv(), source.getIndexs(), source.getSize()), apic, cmd.toArray(new String[0]));
                } else {
                    ProfessorXCore.bootstrap(new FileSource(source.getFilename(), source.getDiv(), source.getIndexs(), source.getSize()), cmd.toArray(new String[0]));
                }
                break;
            }
            case SEQUENCE: {
                if (apic != null) {
                    ProfessorXCore.bootstrap(new SequenceSource(source.getSize()), apic, cmd.toArray(new String[0]));
                } else {
                    ProfessorXCore.bootstrap(new SequenceSource(source.getSize()), cmd.toArray(new String[0]));
                }
                break;
            }
            default: {
                Logger.info("source 模块是必须设置的");
            }
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    public static void help() {
        ProfessorXCore.help();
        Logger.info("请指定配置文件 : -conf professor_x_jms.json");
    }
}
