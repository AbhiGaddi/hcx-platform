package org.swasth.function;

import org.apache.commons.collections.MapUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.kafka.common.metrics.Metrics;
import org.swasth.job.BaseJobConfig;
import org.swasth.job.BaseProcessFunction;
import org.swasth.util.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SubscriptionEnrichmentFunction extends BaseProcessFunction<M, M1> {
    private final BaseJobConfig config;
    private final TypeInformation<String> stringTypeInfo;

    public SubscriptionEnrichmentFunction(BaseJobConfig config, TypeInformation<String> stringTypeInfo) {
        this.config = config;
        this.stringTypeInfo = stringTypeInfo;
    }

    @Override
    public void open(Configuration parameters) {
    }

    public void processElement(Map<String, Object> event, ProcessFunction<Map<String, Object>, Map<String, Object>>.Context context, Metrics metrics) throws UnsupportedEncodingException {
        String senderCode = (String) event.get(Constants.HCX_SENDER_CODE);
        String recipientCode = (String) event.get(Constants.HCX_RECIPIENT_CODE);
        String action = (String) event.get(Constants.ACTION);
        System.out.println("Sender: " + senderCode + " : Recipient: " + recipientCode + " : Action: " + action);

        Map<String, Object> result = new HashMap<>();

        // Fetch the sender and receiver details from registry or cache
        Map<String, Object> sender = fetchDetails(senderCode);
        if (!sender.isEmpty()) {
            Map<String, Object> enrichedSender = createSenderContext(sender, action);
            if (MapUtils.isNotEmpty(enrichedSender)) {
                result.put(Constants.SENDER, enrichedSender);
            }
        }

        Map<String, Object> recipient = fetchDetails(recipientCode);
        if (!recipient.isEmpty()) {
            Map<String, Object> enrichedRecipient = createRecipientContext(recipient, action);
            if (MapUtils.isNotEmpty(enrichedRecipient)) {
                result.put(Constants.RECIPIENT, enrichedRecipient);
            }
        }

        if (MapUtils.isNotEmpty(result)) {
            event.put(Constants.CDATA, result);
        }
        context.output(config.getEnrichedSubscriptionsOutputTag(), event);
    }

    @Override
    public List<String> metricsList() {
        return Collections.emptyList();
    }

}
