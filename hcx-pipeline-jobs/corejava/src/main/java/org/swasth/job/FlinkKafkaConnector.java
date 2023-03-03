package org.swasth.job;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.swasth.serde.MapSerDesSchema;
import org.swasth.serde.StringSerde;


import java.io.Serializable;
import java.util.Map;

public class FlinkKafkaConnector implements Serializable {

    public static BaseJobConfig config;


    public SourceFunction<Map<String, Object>> kafkaMapSource(String kafkaTopic) {
        return new FlinkKafkaConsumer<>(kafkaTopic, new MapSerDesSchema(), config.kafkaConsumerProperties());
    }

    public SinkFunction<Map<String, Object>> kafkaMapSink(String kafkaTopic) {
        return new FlinkKafkaProducer<>(kafkaTopic, new MapSerDesSchema(kafkaTopic), config.kafkaProducerProperties(), FlinkKafkaProducer.Semantic.AT_LEAST_ONCE);
    }

    public SourceFunction<String> kafkaStringSource(String kafkaTopic) {
        return new FlinkKafkaConsumer<>(kafkaTopic, new SimpleStringSchema(), config.kafkaConsumerProperties());
    }

    public SinkFunction<String> kafkaStringSink(String kafkaTopic) {
        return new FlinkKafkaProducer<>(kafkaTopic, new StringSerde(kafkaTopic, FlinkKafkaProducer.Semantic.AT_LEAST_ONCE);
    }
}

