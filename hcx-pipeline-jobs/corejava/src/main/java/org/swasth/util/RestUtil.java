package org.swasth.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestUtil implements Serializable {

    public String get(String url, Optional<Map<String, String>> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        if (headers.isPresent()) {
            headers.get().forEach((headerName, headerValue) -> request.addHeader(headerName, headerValue));
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            InputStream inputStream = entity.getContent();
            String content = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.close();
        }
    }

}
