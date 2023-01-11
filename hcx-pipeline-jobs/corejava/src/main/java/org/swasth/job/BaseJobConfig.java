package org.swasth.job;

import com.typesafe.config.Config;
import java.io.Serializable;
import java.util.*;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.api.scala.OutputTag;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

public class BaseJobConfig implements Serializable {

    private static final long serialVersionUID = -4515020556926788923L;
    private static final TypeInformation<String> METRIC_TYPE_INFO = TypeExtractor.getForClass(String.class);
    private static final TypeInformation<Map> MAP_TYPE_INFO = TypeExtractor.getForClass(Map.class);
    private static final TypeInformation<Object> OBJECT_TYPE_INFO = TypeExtractor.getForClass(Object.class);

    private final Config config ;
    private final String jobName;


    public BaseJobConfig(Config config, String jobName) {
        this.config = config;
        this.jobName = jobName;
    }

    // Default output configurations

    public static final OutputTag<Map<String, Object>> ENRICHED_OUTPUT_TAG = OutputTag.of("enriched-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<Map<String, Object>> DISPATCHER_OUTPUT_TAG = OutputTag.of("dispatched-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<Map<String, Object>> ENRICHED_SUBSCRIPTIONS_OUTPUT_TAG = OutputTag.of("enriched-subscription-events", TypeInformation.of(new TypeHint<Map<String, Object>>() {}));
    public static final OutputTag<String> AUDIT_OUTPUT_TAG = OutputTag.of("audit-events", TypeInformation.of(new TypeHint<String>() {}));

    // Producers
    public static final String AUDITPRODUCER  = "audit-events-sink";

    // Default job metrics

    public static final String DISPATCHER_SUCCESS_COUNT = "dispatcher-success-count";
    public static final String DISPATCHER_VALIDATION_FAILED_COUNT = "dispatcher-validation-failed-count";
    public static final String DISPATCHER_VALIDATION_SUCCESS_COUNT = "dispatcher-validation-success-count";
    public static final String DISPATCHER_FAILED_COUNT = "dispatcher-failed-count";
    public static final String DISPATCHER_RETRY_COUNT = "dispatcher-retry-count";
    public static final String AUDIT_EVENTS_COUNT = "audit-events-count";

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





