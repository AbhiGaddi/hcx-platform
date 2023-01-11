package org.swasth.util;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.zookeeper3.org.apache.zookeeper.proto.ErrorResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.swasth.job.BaseJobConfig;

public class DispatcherUtil {

    private final BaseJobConfig config;
    private final JWTUtil jwtUtil;
    private final CloseableHttpClient httpClient;

    public DispatcherUtil(BaseJobConfig config) {
        this.config = config;
        this.jwtUtil = new JWTUtil((Config) config);
        this.httpClient = new HttpUtil().getHttpClient();
    }

    public DispatcherResult dispatch(Map<String, Object> ctx, String payload) {
        String url = (String) ctx.get("endpoint_url");
        Map<String, String> headers = (Map<String, String>) ctx.getOrDefault("headers", Map.of());
        System.out.println("URL: " + url);
        CloseableHttpResponse response = null;
        try {
            if (StringUtils.isNotEmpty(url)) {
                HttpPost httpPost = new HttpPost(url);
                headers.forEach((k, v) -> httpPost.addHeader(k, v));
                httpPost.setEntity(new StringEntity(payload));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + jwtUtil.generateHCXGatewayToken());
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("statusCode: " + statusCode);
                if (config.getSuccessCodes().contains(statusCode)) {
                    return new DispatcherResult(true, statusCode, null, false);
                } else if (config.getErrorCodes().contains(statusCode)) {
                    ErrorResponse errorResponse = errorMessageProcess(response);
                    return new DispatcherResult(false, statusCode, errorResponse, false);
                } else {
                    ErrorResponse errorResponse = errorMessageProcess(response);
                    return new DispatcherResult(false, statusCode, errorResponse, true);
                }
            } else { // As url is null, no need to retry
                return new DispatcherResult(false, 0, null, false);
            }
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    Constants.RECIPIENT_ERROR)
        }
    }
}
