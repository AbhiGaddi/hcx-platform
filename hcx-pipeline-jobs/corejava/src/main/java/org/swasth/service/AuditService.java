package org.swasth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.swasth.util.Constants;
import org.swasth.util.ElasticSearchUtil;
import org.swasth.util.JSONUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class AuditService {
    Logger logger = (Logger) LogManager.getLogger(AuditService.class);
    public  static  ElasticSearchUtil esUtil;

    public void indexAudit(Map<String, Object> auditEvent) {
        try {
            String settings = "{ \"index\": { } }";
            String mappings = "{ \"properties\": { \"eid\": { \"type\": \"text\" }, \"x-hcx-sender_code\": { \"type\": \"keyword\" }, \"x-hcx-recipient_code\": { \"type\": \"keyword\" }, \"x-hcx-api_call_id\": { \"type\": \"keyword\" }, \"x-hcx-correlation_id\": { \"type\": \"keyword\" }, \"x-hcx-workflow_id\": { \"type\": \"keyword\" }, \"x-hcx-timestamp\": { \"type\": \"date\" }, \"mid\": { \"type\": \"keyword\" }, \"action\": { \"type\": \"keyword\" }, \"x-hcx-status\": { \"type\": \"keyword\" }, \"ets\": { \"type\": \"long\" }, \"requestTimeStamp\": { \"type\": \"long\" }, \"updatedTimestamp\": { \"type\": \"long\" }, \"x-hcx-error_details\": { \"type\": \"object\" }, \"x-hcx-debug_details\": { \"type\": \"object\" }, \"senderRole\": { \"type\": \"keyword\" }, \"recipientRole\": { \"type\": \"keyword\" }, \"payload\": { \"type\": \"text\" }, \"topic_code\": { \"type\": \"keyword\" }, \"senderName\": { \"type\": \"keyword\" }, \"recipientName\": { \"type\": \"keyword\" }, \"senderPrimaryEmail\": { \"type\": \"keyword\" }, \"recipientPrimaryEmail\": { \"type\": \"keyword\" }, \"subscription_id\": { \"type\": \"keyword\" }, \"subscription_status\": { \"type\": \"keyword\" }, \"x-hcx-notification_headers\": { \"type\": \"object\" } } }";
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(config.timeZone));
            cal.setTime((Date) auditEvent.get(Constants.ETS));
            String indexName = config.auditIndex + "_" + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.WEEK_OF_YEAR);
            String mid = (String) auditEvent.get(Constants.MID);
            esUtil.addIndex(settings, mappings, indexName, config.auditAlias);
            esUtil.addDocumentWithIndex(JSONUtil.serialize(auditEvent), indexName, mid);
            System.out.println("Audit document created for mid: " + mid);
        } catch (IOException e) {
            logger.error("Error while processing event :: " + auditEvent + " :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
