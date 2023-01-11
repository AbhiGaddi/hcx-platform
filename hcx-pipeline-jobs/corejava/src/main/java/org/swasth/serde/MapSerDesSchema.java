package org.swasth.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.swasth.util.JSONUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapSerDesSchema implements KafkaDeserializationSchema<Map>, KafkaSerializationSchema<Map<String, Object>> {

    private static final long serialVersionUID = -3224825136576915426L;

    private final String topic;
    private final Optional<String> key;

    public MapSerDesSchema(String topic, Optional<String> key) {
        this.topic = topic;
        this.key = key;
    }

    @Override
    public boolean isEndOfStream(Map nextElement) {
        return false;
    }

    @Override
    public Map<String,Object> deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<>() {
        };
        Map<String, Object> recordMap = JSONUtil.deserialize(record.value(), typeReference);
        return recordMap;
    }

    @Override
    public TypeInformation<Map> getProducedType() {
        return TypeExtractor.getForClass(Map.class);
    }

    public ProducerRecord<byte[], byte[]> serialize(Map<String, Object> element, Long timestamp) {
        String out = new Gson().toJson(element);
        return key.map(kafkaKey -> new ProducerRecord<>(topic, kafkaKey.getBytes(StandardCharsets.UTF_8), out.getBytes(StandardCharsets.UTF_8)))
                .orElse(new ProducerRecord<>(topic, out.getBytes(StandardCharsets.UTF_8)));
    }
}

