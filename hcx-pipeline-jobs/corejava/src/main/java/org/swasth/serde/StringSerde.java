package org.swasth.serde;

import com.google.gson.Gson;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class StringSerde implements KafkaDeserializationSchema<String> , KafkaSerializationSchema<String> {

    private final String topic;
    private final Optional<String> key;

    public StringSerde(String topic, Optional<String> key) {
        this.topic = topic;
        this.key = key;
    }

    @Override
    public boolean isEndOfStream(String s) {
        return false;
    }

    @Override
    public String deserialize(ConsumerRecord<byte[], byte[]> record) {
        return new String(record.value(), StandardCharsets.UTF_8);
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return TypeExtractor.getForClass(String.class);
    }


    public ProducerRecord<byte[], byte[]> serialize(String element, Long timestamp) {
        String out = new Gson().toJson(element);
        return key.map(kafkaKey -> new ProducerRecord<>(topic, kafkaKey.getBytes(StandardCharsets.UTF_8), out.getBytes(StandardCharsets.UTF_8)))
                .orElse(new ProducerRecord<>(topic, out.getBytes(StandardCharsets.UTF_8)));
    }
}
