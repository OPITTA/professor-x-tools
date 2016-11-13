package com.github.professor_x_url.service;

import com.github.professor_x_url.constent.RequestMethod;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author 510655387@qq.com
 */
public class HttpSessionService {

    private static HttpSessionService httpSessionService;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private static Object lock = new Object();

    private HttpSessionService() {
    }

    public static HttpSessionService getInstance() {
        if (httpSessionService == null) {
            synchronized (lock) {
                if (httpSessionService == null) {
                    httpSessionService = new HttpSessionService();
                }
            }
        }
        return httpSessionService;
    }

    public String curl(String requestMethod, String url, Header[] headers, Map<String, Object> params) throws IOException, URISyntaxException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
            }
        }
        HttpResponse httpResponse;
        switch (RequestMethod.fromName(requestMethod)) {
            case GET: {
                URI uri = new URIBuilder(url).addParameters(nameValuePairs).build();
                HttpGet httpGet = new HttpGet(uri);
                if (headers != null) {
                    httpGet.setHeaders(headers);
                }
                httpResponse = httpClient.execute(httpGet);
                break;
            }
            case POST: {
                URI uri = new URIBuilder(url).build();
                HttpPost httpPost = new HttpPost(uri);
                if (headers != null) {
                    httpPost.setHeaders(headers);
                }
                UrlEncodedFormEntity from = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
                httpPost.setEntity(from);
                httpResponse = httpClient.execute(httpPost);
                break;
            }
            default: {
                URI uri = new URIBuilder(url).addParameters(nameValuePairs).build();
                HttpGet httpGet = new HttpGet(uri);
                if (headers != null) {
                    httpGet.setHeaders(headers);
                }
                httpResponse = httpClient.execute(httpGet);
            }
        }
        if (httpResponse != null) {
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        }
        return null;
    }

}
