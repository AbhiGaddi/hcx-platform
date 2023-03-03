package org.swasth.job;

import com.google.gson.Gson;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.common.metrics.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swasth.cache.DedupEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseDeduplication {

    Logger logger = LoggerFactory.getLogger(BaseDeduplication.class);
    String uniqueEventMetricCount = "unique-event-count";
    String duplicateEventMetricCount = "duplicate-event-count";
    Gson gson = new Gson();


    static <T, R> void deDup(String key, T event, ProcessFunction<T, R>.Context context, OutputTag<R> successOutputTag,
                             OutputTag<R> duplicateOutputTag, String flagName, DedupEngine deDupEngine, Metrics metrics) {
        if (null != key && !deDupEngine.isUniqueEvent(key)) {
            logger.debug(String.format("Event with mid: %s is duplicate", key));
            metrics.incCounter(duplicateEventMetricCount);
            context.output(duplicateOutputTag, updateFlag(event, flagName, true));
        } else {
            if (key != null) {
                logger.debug(String.format("Adding mid: %s to Redis", key));
                deDupEngine.storeChecksum(key);
            }
            metrics.incCounter(uniqueEventMetricCount);
            logger.debug(String.format("Pushing the event with mid: %s for further processing", key));
            context.output(successOutputTag, updateFlag(event, flagName, false));
        }
    }

    static <T, R> R updateFlag(T event, String flagName, boolean value) {
        Map<String, Boolean> flags = new HashMap<>();
        flags.put(flagName, value);
        if (event instanceof String) {
            Map<String, Object> eventMap = gson.fromJson((String) event, HashMap.class);
            eventMap.put("flags", flags);
            return (R) eventMap;
        } else {
            ((Map<String, Object>) event).put("flags", flags);
            return (R) event;
        }
    }

    static List<String> deduplicationMetrics() {
        return List.of(uniqueEventMetricCount, duplicateEventMetricCount);
    }
}
