package org.swasth.job;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.Serializable;
import java.util.Map;

public class FlinkKafkaConnector implements Serializable {

    public static BaseJobConfig config;

    public static SourceFunction<Map<String,Object>> kafkaMapSource(String kafkaTopic){
        new FlinkKafkaConsumer[util.Map[String, AnyRef]](kafkaTopic, new MapDeserializationSchema, config.kafkaConsumerProperties);
    }

}

