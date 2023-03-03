package org.swasth.function;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swasth.dp.core.job.BaseJobConfig;
import org.swasth.dp.core.job.BaseProcessFunction;
import org.swasth.dp.core.job.Metrics;
import org.swasth.dp.core.util.Constants;
import org.swasth.job.BaseJobConfig;
import org.swasth.job.BaseProcessFunction;
import org.swasth.util.Constants;

public class ContextEnrichmentFunction extends BaseProcessFunction<Map<String, Object>, Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextEnrichmentFunction.class);
    private final BaseJobConfig config;
    private final TypeInformation<String> stringTypeInfo;

    public ContextEnrichmentFunction(BaseJobConfig config, TypeInformation<String> stringTypeInfo) {
        super(config);
        this.config = config;
        this.stringTypeInfo = stringTypeInfo;
    }

    @Override
    public void open(Configuration parameters) {
    }

    @Override
    public void processElement(Map<String, Object> event, ProcessFunction<Map<String, Object>, Map<String, Object>>.Context context, Metrics metrics) throws UnsupportedEncodingException {
        String senderCode = getProtocolStringValue(event, Constants.HCX_SENDER_CODE);
        String recipientCode = getProtocolStringValue(event, Constants.HCX_RECIPIENT_CODE);
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

     void fetchDetails() {

            }
    }