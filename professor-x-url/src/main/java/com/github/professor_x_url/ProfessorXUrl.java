package com.github.professor_x_url;

import com.github.professor_x_core.ProfessorXCore;
import com.github.professor_x_core.util.FileSource;
import com.github.professor_x_core.util.Logger;
import com.github.professor_x_core.util.SequenceSource;
import com.github.professor_x_core.web.APIClient;
import com.github.professor_x_url.model.Configure;
import com.github.professor_x_url.model.Professor;
import com.github.professor_x_url.model.Source;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author 510655387@qq.com
 */
public class ProfessorXUrl {

    public static void main(String... args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(ProfessorXUrl.class.getResourceAsStream("/professor_x_url.json")));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Configure configure = objectMapper.readValue(sb.toString(), Configure.class);
        Logger.info(objectMapper.writeValueAsString(configure));
        Professor professor = configure.getProfessor();
        APIClient apic = new APIClient(professor.getHost(), professor.getPort(), professor.getAccount(), professor.getPasswd(), professor.getTopic(), professor.getTitle());
        Source source = configure.getSource();
        
        if (source.getType().equalsIgnoreCase("file")) {
            ProfessorXCore.bootstrap(new FileSource(source.getFilename(), source.getDiv(), source.getIndexs(), source.getSize()), apic, args);
        } else {
            ProfessorXCore.bootstrap(new SequenceSource(source.getSize()), apic, args);
        }
    }
}
