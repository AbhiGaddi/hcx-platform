package org.swasth.util;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class HttpUtil {

    private RequestConfig requestConfig;
    private HttpRequestRetryHandler retryHandler;
    private CloseableHttpClient httpClient;

    public HttpUtil() {

        this.requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setConnectTimeout(5 * 1000) // TODO: load from config
                .setConnectionRequestTimeout(5 * 1000) // TODO: load from config
                .setSocketTimeout(60 * 1000) // TODO: load from config
                .build();

        this.retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                System.out.println("HTTP retry request execution count " + executionCount);
                if (executionCount > 3) { // TODO: load from config
                    return false;
                } else {
                    // wait a second before retrying again
                    try {
                        Thread.sleep(1000); // TODO: load from config
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        };

        this.httpClient = HttpClientBuilder.create().setRetryHandler(retryHandler)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}