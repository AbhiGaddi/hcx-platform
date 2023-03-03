package org.swasth.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.swasth.cache.DataCache;
import org.swasth.service.RegistryService;
import org.swasth.util.Constants;
import org.swasth.util.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseProcessFunction {


    public DataCache registryDataCache;

    public RegistryService registryService;

    public abstract void open(Configuration parameters);

    public abstract void processElement(Map<String, Object> event, ProcessFunction<Map<String, Object>, Map<String, Object>>.Context context, Metrics metrics) throws UnsupportedEncodingException;

    public abstract List<String> metricsList();

    public static String getProtocolStringValue(Map<String, Object> event, String key) {
        Map<String, Object> headers = (Map<String, Object>) event.get(Constants.HEADERS);
        Map<String, Object> protocol = (Map<String, Object>) headers.get(Constants.PROTOCOL);
        return protocol.getOrDefault(key, "").toString();
    }

    public static Map<String, Object> getProtocolMapValue(Map<String, Object> event, String key) {
        Map<String, Object> headers = (Map<String, Object>) event.get(Constants.HEADERS);
        Map<String, Object> protocol = (Map<String, Object>) headers.get(Constants.PROTOCOL);
        return (Map<String, Object>) protocol.getOrDefault(key, new HashMap<>());
    }

    public static ArrayList<String> getCDataListValue(Map<String, Object> event, String participant, String key) {
        Map<String, Object> cData = (Map<String, Object>) event.getOrDefault(Constants.CDATA, new HashMap<>());
        Map<String, Object> participantMap = (Map<String, Object>) cData.getOrDefault(participant, new HashMap<>());
        return (ArrayList<String>) participantMap.getOrDefault(key, new ArrayList<>());
    }

    public static String getCDataStringValue(Map<String, Object> event, String participant, String key) {
        Map<String, Object> cData = (Map<String, Object>) event.getOrDefault(Constants.CDATA, new HashMap<>());
        Map<String, Object> participantMap = (Map<String, Object>) cData.getOrDefault(participant, new HashMap<>());
        return (String) participantMap.getOrDefault(key, "");
    }

    public static void setStatus(Map<String, Object> event, String status) {
        Map<String, Object> headers = (Map<String, Object>) event.get(Constants.HEADERS);
        Map<String, Object> protocol = (Map<String, Object>) headers.get(Constants.PROTOCOL);
        if (protocol.containsKey(Constants.HCX_STATUS)) {
            protocol.put(Constants.HCX_STATUS, status);
        }
    }

    public static String getReplacedAction(String actionStr) {
        String replacedAction = actionStr;
        String[] parts = actionStr.split("/");
        String lastVal = parts[parts.length - 1];
        if (!lastVal.startsWith("on_")) {
            replacedAction = actionStr.replace(lastVal, "on_" + lastVal);
        }
        return replacedAction;
    }

    public Map<String, Object> createSenderContext(Map<String, Object> sender, String actionStr) {
        //Sender Details
        var endpointUrl = sender.getOrDefault(Constants.END_POINT, "").toString();
        if (!StringUtils.isEmpty(endpointUrl)) {
            //If endPointUrl comes with /, remove it as action starts with /
            if (endpointUrl.endsWith("/"))
                endpointUrl = endpointUrl.substring(0, endpointUrl.length() - 1);

            // fetch on_ action for the sender
            String replacedAction = getReplacedAction(actionStr);
            String appendedSenderUrl = endpointUrl.concat(replacedAction);
            sender.put(Constants.END_POINT, appendedSenderUrl);
            return sender;
        } else {
            return new HashMap<>();
        }
    }

    public Map<String, Object> createRecipientContext(Map<String, Object> receiver, String actionStr) {
        //Receiver Details
        String endpointUrl = receiver.get(Constants.END_POINT).toString();
        if (!StringUtils.isEmpty(endpointUrl)) {
            //If endPointUrl comes with /, remove it as action starts with /
            if (endpointUrl.endsWith("/"))
                endpointUrl = endpointUrl.substring(0, endpointUrl.length() - 1);
            String appendedReceiverUrl = endpointUrl.concat(actionStr);
            receiver.put(Constants.END_POINT, appendedReceiverUrl);
            return receiver;
        } else {
            return new HashMap<>();
        }
    }

    public Map<String, Object> fetchDetails(String code) throws UnsupportedEncodingException {
        try {
            if (registryDataCache.isExists(code)) {
                System.out.println("Getting details from cache for :" + code);
                Map<String, Object> mutableMap = registryDataCache.getWithRetry(code);
                return mutableMap;
            } else {
                //Fetch the details from API call
                System.out.println("Could not find the details in cache for code:" + code);
                Map<String, Object> collectionMap = getDetails(code);
                if (!collectionMap.isEmpty())
                    // Add the registry data into cache if it is not empty
                    registryDataCache.hmSet(code, JSONUtil.serialize(collectionMap), config.redisExpires);
                return collectionMap;
            }
        } catch (ServerException | JsonProcessingException e) {
            //In case of issues with redis cache, fetch the details from the registry
            return getDetails(code);
        }
    }

    private Map<String, Object> getDetails(String code) throws UnsupportedEncodingException {
        String key = Constants.PARTICIPANT_CODE;
        String filters = "{\"filters\":{\"" + key + "\":{\"eq\":\"" + code + "\"}}}";
        ArrayList<Map<String, Object>> responseBody = registryService.getParticipantDetails(filters);
        if (!responseBody.isEmpty()) {
            Map<String, Object> collectionMap = responseBody.get(0);
            return collectionMap;
        } else {
            return new HashMap<>();
        }
    }
}


}
