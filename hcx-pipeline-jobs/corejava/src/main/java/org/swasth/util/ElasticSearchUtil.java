package org.swasth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElasticSearchUtil {

    private final int resultLimit = 100;
    private final RestHighLevelClient esClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);

    public ElasticSearchUtil(String connectionInfo, String indexName, int batchSize) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        this.esClient = createClient(connectionInfo);
    }

    private RestHighLevelClient createClient(String connectionInfo) {
        List<HttpHost> httpHosts = List.of(connectionInfo.split(",")).stream()
                .map(info -> {
                    String host = info.split(":")[0];
                    int port = Integer.parseInt(info.split(":")[1]);
                    return new HttpHost(host, port);
                }).collect(Collectors.toList());

        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]))
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setConnectionRequestTimeout(-1);
                    }
                });
        return new RestHighLevelClient(builder);
    }

    public void addDocumentWithIndex(String document, String indexName, String identifier) throws IOException {
        try {
            Map<String, Object> doc = mapper.readValue(document, new TypeReference<Map<String, Object>>() {
            });
            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.id(identifier);
            var response = esClient.index(indexRequest.source(doc), RequestOptions.DEFAULT);
            logger.info("Added " + response.getId() + " to index " + response.getIndex());
        } catch (IOException e) {
            logger.error("ElasticSearchUtil:: Error while adding document to index :" + indexName + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void addIndex(String settings, String mappings, String indexName, String alias) throws Exception {
        try {
            if(!isIndexExists(indexName)) {
                CreateIndexRequest createRequest = new CreateIndexRequest(indexName);
                if (StringUtils.isNotBlank(alias)) createRequest.alias(new Alias(alias));
                if (StringUtils.isNotBlank(settings)) createRequest.settings(Settings.builder().loadFromSource(settings, XContentType.JSON));
                if (StringUtils.isNotBlank(mappings)) createRequest.mapping(mappings, XContentType.JSON);
                esClient.indices().create(createRequest, RequestOptions.DEFAULT);
            }
        } catch (Exception e) {
            throw new Exception("ElasticSearchUtil :: Error while creating index : " + indexName + " : " + e.getMessage());
        }
    }

    public boolean isIndexExists(String indexName){
        try {
            return esClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }
    }

    public void close() throws Exception {
        if (null != esClient)
            try {
                esClient.close();
            } catch (IOException e) {
                throw new Exception("ElasticSearchUtil :: Error while closing elastic search connection : " + e.getMessage());
            }
    }

}