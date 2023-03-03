package org.swasth.job;

import java.io.Serializable;
import java.util.*;

import com.typesafe.config.Config;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.api.scala.OutputTag;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;


public class BaseJobConfig implements Serializable {


    public static Config config;
    public  String jobName;
    public BaseJobConfig(Config config, String jobName) {
        BaseJobConfig.config = config;
        this.jobName = jobName;
    }

    private static final long serialVersionUID = -4515020556926788923L;
    private static final TypeInformation<String> METRIC_TYPE_INFO = TypeExtractor.getForClass(String.class);
    private static final TypeInformation<Map> MAP_TYPE_INFO = TypeExtractor.getForClass(Map.class);
    private static final TypeInformation<Object> OBJECT_TYPE_INFO = TypeExtractor.getForClass(Object.class);






    // Default output configurations

    public static final OutputTag<Map<String, Object>> ENRICHED_OUTPUT_TAG = OutputTag.apply("enriched-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<Map<String, Object>> DISPATCHER_OUTPUT_TAG = OutputTag.apply("dispatched-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<Map<String, Object>> ENRICHED_SUBSCRIPTIONS_OUTPUT_TAG = OutputTag.apply("enriched-subscription-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<String> AUDIT_OUTPUT_TAG = OutputTag.apply("audit-events", TypeInformation.of(new TypeHint<String>() {}));

    // Producers
    public static final String AUDITPRODUCER  = "audit-events-sink";

    // Default job metrics

    public static final String DISPATCHER_SUCCESS_COUNT = "dispatcher-success-count";
    public static final String DISPATCHER_VALIDATION_FAILED_COUNT = "dispatcher-validation-failed-count";
    public static final String DISPATCHER_VALIDATION_SUCCESS_COUNT = "dispatcher-validation-success-count";
    public static final String DISPATCHER_FAILED_COUNT = "dispatcher-failed-count";
    public static final String DISPATCHER_RETRY_COUNT = "dispatcher-retry-count";
    public static final String AUDIT_EVENTS_COUNT = "audit-events-count";
    public static final String kafkaBrokerServers = config.getString("kafka.broker-servers");
    public static final Integer kafkaProducerLingerMs = config.getInt("kafka.producer.linger.ms");
    public static final Integer kafkaProducerBatchSize = config.getInt("kafka.producer.batch.size");
    public static final String groupId = config.getString("kafka.groupId");
    public static final Integer kafkaProducerMaxRequestSize = config.getInt("kafka.producer.max-request-size");
    
    public static final Optional<String> kafkaAutoOffsetReset = if(.hasPath("kafka.auto.offset.reset")) Option(config.getS
                                                                                                                       ("kafka.auto.offset.reset")) else None




    public Properties kafkaConsumerProperties(){
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaBrokerServers);
        properties.setProperty("group.id", groupId);
        properties.setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        kafkaAutoOffsetReset.map { properties.setProperty("auto.offset.reset", _) }
        return properties;
    }

    public Properties kafkaProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerServers);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, new Integer(kafkaProducerLingerMs));
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, new Integer(kafkaProducerBatchSize));
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, new Integer(kafkaProducerMaxRequestSize));
        return  properties;
    }


}





