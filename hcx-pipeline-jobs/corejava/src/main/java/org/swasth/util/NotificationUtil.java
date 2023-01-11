package org.swasth.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.swasth.util.Constants.TOPIC_CODE;

public class NotificationUtil {


   public static List<Map<String,Object>> notificationList = null;
   public static List<String> topicCodeList = new ArrayList<>();


    private void loadNotifications(String networkPath, String participantPath, String workflowPath) throws IOException {
        List<Map<String, Object>> networkList = YamlUtil.convertYaml(getClass().getClassLoader().getResourceAsStream(networkPath), List.class);
        List<Map<String, Object>> participantList = YamlUtil.convertYaml(getClass().getClassLoader().getResourceAsStream(participantPath), List.class);
        List<Map<String, Object>> workflowList = YamlUtil.convertYaml(getClass().getClassLoader().getResourceAsStream(workflowPath), List.class);
        notificationList = Stream.of(networkList, participantList, workflowList).flatMap(Collection::stream).collect(Collectors.toList());
        notificationList.forEach(obj -> topicCodeList.add((String) obj.get(TOPIC_CODE)));

    }

    public static boolean isValidCode(String code) {
        return topicCodeList.contains(code);
    }

    public Map<String,Object> getNotification(String code)  {
        Map<String,Object> notifications = new HashMap<>();
        Optional<Map<String, Object>> result = notificationList.stream().filter(obj -> obj.get(TOPIC_CODE).equals(code)).findFirst();
        if (result.isPresent()) notifications = result.get();
        return notifications;
    }


}
