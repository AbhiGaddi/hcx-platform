package org.swasth.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.swasth.job.BaseJobConfig;
import org.swasth.util.Constants;
import org.swasth.util.HttpUtil;
import org.swasth.util.JSONUtil;
import scala.io.Source;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
public class RegistryService {

    private final CloseableHttpClient httpClient;
    private final BaseJobConfig config;

    public RegistryService(BaseJobConfig config) {
        this.config = config;
        this.httpClient = new HttpUtil().getHttpClient();
    }

    public ArrayList<Map<String, Object>> getParticipantDetails(String filters) throws UnsupportedEncodingException {
        String payload = "{"entityType":["Organisation"],"filters": " + filters + "}";
        HttpPost httpPost = new HttpPost(config.getHcxApisUrl() + Constants.PARTICIPANT_SEARCH);
        httpPost.setEntity(new StringEntity(payload));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            Map<String, Object> content = JSONUtil.deserialize(
                    Source.fromInputStream(inputStream, "UTF-8").getLines().mkString(),
                    Map.class
            );
            inputStream.close();
            response.close();
            return (ArrayList<Map<String, Object>>) content.get(Constants.PARTICIPANTS);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
