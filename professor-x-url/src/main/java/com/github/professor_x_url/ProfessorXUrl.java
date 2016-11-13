package com.github.professor_x_url;

import com.github.professor_x_core.ProfessorXCore;
import com.github.professor_x_core.util.FileSource;
import com.github.professor_x_core.util.Logger;
import com.github.professor_x_core.util.SequenceSource;
import com.github.professor_x_core.web.APIClient;
import com.github.professor_x_url.constent.SourceType;
import com.github.professor_x_url.model.Configure;
import com.github.professor_x_url.model.Login;
import com.github.professor_x_url.model.Professor;
import com.github.professor_x_url.model.Request;
import com.github.professor_x_url.model.Source;
import com.github.professor_x_url.util.HttpClientUtils;
import com.github.professor_x_url.util.ShutdownHook;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author 510655387@qq.com
 */
public class ProfessorXUrl {

    public static void main(String... args) throws IOException, URISyntaxException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(ProfessorXUrl.class.getResourceAsStream("/professor_x_url.json")));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Configure configure = objectMapper.readValue(sb.toString(), Configure.class);
        Logger.info(objectMapper.writeValueAsString(configure));
        Configure.setConfigure(configure);
        Login login = configure.getLogin();
        if (login != null) {
            Logger.info(HttpClientUtils.getInstance().curl(login.getMethod(), login.getUrl(), null, login.getData()));
            Logger.info("login 模块启动");
            Thread.sleep(2000);
        }
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
        Source source = configure.getSource();
        switch (SourceType.fromName(source.getType())) {
            case FILE: {
                if (apic != null) {
                    ProfessorXCore.bootstrap(new FileSource(source.getFilename(), source.getDiv(), source.getIndexs(), source.getSize()), apic, args);
                } else {
                    ProfessorXCore.bootstrap(new FileSource(source.getFilename(), source.getDiv(), source.getIndexs(), source.getSize()), args);
                }
                break;
            }
            case SEQUENCE: {
                if (apic != null) {
                    ProfessorXCore.bootstrap(new SequenceSource(source.getSize()), apic, args);
                } else {
                    ProfessorXCore.bootstrap(new SequenceSource(source.getSize()), args);
                }
                break;
            }
            default: {
                Logger.info("source 模块是必须设置的");
            }
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}
