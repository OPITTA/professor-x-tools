package com.github.professor_x_core.web;

import com.github.professor_x_core.interfaces.Result;
import com.github.professor_x_core.util.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author 510655387@qq.com
 */
public class APIClient implements Result {

    private String host = "localhost";
    private int port = 80;
    private String username = "admin";
    private String passwd = "admin";
    private String title;

    public APIClient(String host, int port, String username, String passwd, String title) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.passwd = passwd;
        this.title = title;
    }

    @Override
    public void output(int concurrency, int total, long minRT, long maxRT, double averageRT, double tps, double errorNumber) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URI uri = new URIBuilder().setScheme("http").setHost(host + ":" + port).setPath("/professor_x_web/do_login").build();
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("account", username));
            nvps.add(new BasicNameValuePair("passwd", passwd));
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(nvps);
            post.setEntity(form);
            CloseableHttpResponse response = client.execute(post);
            boolean ok = false;
            for (Header header : response.getAllHeaders()) {
                if (header.getName().equalsIgnoreCase("Location")) {
                    if (header.getValue().contains("list")) {
                        ok = true;
                    }
                    break;
                }
            }
            if (ok) {
                Logger.info("登陆成功");
            } else {
                Logger.info("登陆失败");
                return;
            }
            uri = new URIBuilder().setScheme("http").setHost(host + ":" + port).setPath("/professor_x_web/report/do_add_data").build();
            HttpPut put = new HttpPut(uri);
            nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("title", title));
            Data data = new Data();
            data.setConcurrency(concurrency);
            data.setMinRt((int) minRT);
            data.setMaxRt((int) maxRT);
            data.setAverageRt((int) averageRT);
            data.setTps((int) tps);
            data.setErrorRate((int) errorNumber);
            ObjectMapper objectMapper = new ObjectMapper();
            nvps.add(new BasicNameValuePair("data", objectMapper.writeValueAsString(data)));
            form = new UrlEncodedFormEntity(nvps);
            put.setEntity(form);
            response = client.execute(put);
            HttpEntity entity = response.getEntity();
            EntityUtils.toString(entity);
        } catch (URISyntaxException e) {
            Logger.info(e.getMessage());
        } catch (IOException e) {
            Logger.info(e.getMessage());
        } catch (ParseException e) {
            Logger.info(e.getMessage());
        }

    }
}
