package org.swasth.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.MapUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.zookeeper3.org.apache.zookeeper.proto.ErrorResponse;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.kafka.common.metrics.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swasth.exception.PipelineException;
import org.swasth.job.BaseJobConfig;
import org.swasth.job.BaseProcessFunction;
import org.swasth.service.AuditService;
import org.swasth.util.*;

import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BaseDispatcherFunction extends BaseProcessFunction<Map<String, Object>, Map<String, Object>> {

    private final Logger logger = LoggerFactory.getLogger(BaseDispatcherFunction.class);
    private PostgresConnectionConfig config;
    PostgresConnect postgresConnect = new PostgresConnect(config);
    private final AuditService auditService = new AuditService();

    private BaseJobConfig baseJobConfig;
    private DispatcherUtil dispatcherUtil;

    private Map<String, Object> payload;

    public BaseDispatcherFunction() throws Exception {
    }


    public void open(Configuration parameters) throws Exception {
        PostgresConnect postgresConnect = new PostgresConnect(new PostgresConnectionConfig(config.user, config.password, config.database, config.host, config.port, config.maxConnections))
        auditService = new AuditService(baseJobConfig);
    }

    @Override
    public void processElement(Map event, ProcessFunction.Context context, Metrics metrics) throws UnsupportedEncodingException {

    }

    public void close() throws Exception {
        super.close();
        postgresConnect.closeConnection();
    }

    public Map getPayload(String payloadRefId) throws SQLException {
        System.out.println("Fetching payload from postgres for mid: " + payloadRefId);
        logger.info("Fetching payload from postgres for mid: " + payloadRefId);
        String postgresQuery = String.format("SELECT data FROM %s WHERE mid = '%s'", baseJobConfig.postgresTable, payloadRefId);
        PreparedStatement preparedStatement = postgresConnect.getConnection().prepareStatement(postgresQuery);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String payload = resultSet.getString(1);
                return JSONUtil.deserialize(payload, Map.class);
            } else {
                throw new Exception("Payload not found for the given reference id: " + payloadRefId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            preparedStatement.close();
        }
    }

    public void audit(Map<String,Object> event,ProcessFunction<>, Metrics metrics) throws JsonProcessingException {
        auditService.indexAudit(createAuditRecord(event));
        context.output(config.auditOutputTag, JSONUtil.serialize(createAuditLog(event)));
        metrics.incCounter(config.auditEventsCount);
    }

    public Map<String,Object> createErrorMap(Option error){
        Map<String,Object> errorMap = new HashMap<>();
        errorMap.put("code",error.get.code.get);
        errorMap.put("message",error.get.message.get);
        errorMap.put("trace",error.get.trace.get);
        return errorMap;
    }

    public void dispatchErrorResponse(Map<String,Object> event, Option error, String correlationId, String payloadRefId, Map<String,Object> senderCtx, ProcessFunction[Map<String,Object>], util.Map[String, AnyRef]]#Context, Metrics metrics) throws JsonProcessingException {
         Map<String,Object> protectedMap = new HashMap<>();
        //Update sender code
        protectedMap.put(Constants.HCX_SENDER_CODE, BaseJobConfig.hcxRegistryCode);
        //Update recipient code
        protectedMap.put(Constants.HCX_RECIPIENT_CODE, getProtocolStringValue(event,Constants.HCX_SENDER_CODE));
        //Keep same correlationId
        protectedMap.put(Constants.HCX_CORRELATION_ID, getProtocolStringValue(event,Constants.HCX_CORRELATION_ID));
        //Generate new UUID for each request processed by HCX Gateway
        protectedMap.put(Constants.API_CALL_ID, UUID.randomUUID());
        //Keep same work flow id if it exists in the incoming event
        if(!getProtocolStringValue(event,Constants.WORKFLOW_ID).isEmpty())
            protectedMap.put(Constants.WORKFLOW_ID, getProtocolStringValue(event,Constants.WORKFLOW_ID));
        //Update error details
        protectedMap.put(Constants.ERROR_DETAILS,createErrorMap(error));
        //Update status
        protectedMap.put(Constants.HCX_STATUS,Constants.ERROR_STATUS);
        System.out.println("Payload: " + protectedMap);
        DispatcherResult result = dispatcherUtil.dispatch(senderCtx, JSONUtil.serialize(protectedMap));
        if(result.retry) {
            logger.info("Error while dispatching error response: " + result.error.get.message.get);
            metrics.incCounter(metric = BaseJobConfig.dispatcherRetryCount);
        }
    }

    public void processElement(Map<String,Object> event, context: ProcessFunction[util.Map[String, AnyRef], util.Map[String, AnyRef]]#Context,Metrics metrics){
        //TODO Make changes here for handling redirect requests with flat JSON objects
        String correlationId = getProtocolStringValue(event,Constants.HCX_CORRELATION_ID);
        String  payloadRefId = event.get(Constants.MID).toString();
        // TODO change cdata to context after discussion.
        Map<String, Object> cdata = (Map<String, Object>) event.getOrDefault(Constants.CDATA, new HashMap<>());
        Map<String, Object> senderCtx = ((Map<String, Object>) cdata.getOrDefault(Constants.SENDER, new HashMap<>()));
        Map<String,Object> recipientCtx = ((Map<String, Object>) cdata.getOrDefault(Constants.RECIPIENT, new HashMap<>()));

        try {
            if (MapUtils.isEmpty(senderCtx)) {
                System.out.println("sender context is empty for mid: " + payloadRefId);
                logger.warn("sender context is empty for mid: " + payloadRefId);
                //Audit the record if sender context is empty
                audit(event, context, metrics);
            } else if (MapUtils.isEmpty(recipientCtx)) {
                System.out.println("recipient context is empty for mid: " + payloadRefId);
                logger.warn("recipient context is empty for mid: " + payloadRefId);
                //Send on_action request back to sender when recipient context is missing
                val errorResponse = ErrorResponse(new Option(Constants.RECIPIENT_ERROR_CODE), new Option(Constants.RECIPIENT_ERROR_MESSAGE), Option(Constants.RECIPIENT_ERROR_LOG))
                dispatchErrorResponse(event, ValidationResult(true, Option(errorResponse)).error, correlationId, payloadRefId, senderCtx, context, metrics)
            } else {
                System.out.println("sender and recipient available for mid: " + payloadRefId);
                logger.info("sender and recipient available for mid: " + payloadRefId);
                val validationResult = validate(event);
                if (!validationResult.status) {
                    metrics.incCounter(metric = config.dispatcherValidationFailedCount)
                    audit(event, context, metrics);
                    dispatchErrorResponse(event, validationResult.error, correlationId, payloadRefId, senderCtx, context, metrics)
                }

                if (validationResult.status) {
                    metrics.incCounter(metric = config.dispatcherValidationSuccessCount);
                    payload = getPayload(payloadRefId);
                    val payloadJSON = JSONUtil.serialize(payload);
                    val result = dispatcherUtil.dispatch(recipientCtx, payloadJSON);
                    logger.info("result::" + result);
                    //Adding updatedTimestamp for auditing
                    event.put(Constants.UPDATED_TIME, Calendar.getInstance().getTime());
                    if (result.success) {
                        updateDBStatus(payloadRefId, Constants.DISPATCH_STATUS);
                        setStatus(event, Constants.DISPATCH_STATUS);
                        metrics.incCounter(metric = BaseJobConfig.dispatcherSuccessCount);
                    }
                    if (result.retry) {
                        int retryCount = 0 ;
                        if (event.containsKey(Constants.RETRY_INDEX))
                            retryCount = (Integer) event.get(Constants.RETRY_INDEX);
                        if (!BaseJobConfig.allowedEntitiesForRetry.contains(getEntity(event.get(Constants.ACTION).toString())) || retryCount == BaseJobConfig.maxRetry) {
                            dispatchError(payloadRefId, event, result, correlationId, senderCtx, context, metrics);
                        } else if (retryCount < baseJobConfig.maxRetry) {
                            updateDBStatus(payloadRefId, Constants.REQ_RETRY);
                            setStatus(event, Constants.QUEUED_STATUS);
                            metrics.incCounter(metric = config.dispatcherRetryCount);
                            System.out.println("Event is updated for retrying..");
                        }
                    }
                    if (!result.retry && !result.success) {
                        dispatchError(payloadRefId, event, result, correlationId, senderCtx, context, metrics);
                    }
                    audit(event, context, metrics);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (PipelineException pipelineException){
                ErrorResponse errorResponse = ErrorResponse(Option(ex.code), Option(ex.message), Option(ex.trace));
                dispatchErrorResponse(event, ValidationResult(true, Option(errorResponse)).error, correlationId, payloadRefId, senderCtx, context, metrics);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getEntity(String path) {
        if (path.contains("status")) {
            return "status";
        } else if (path.contains("on_search")) {
            return "searchresponse";
        } else if (path.contains("search")) {
            return "search";
        } else {
            String[] str = path.split("/");
            return str[str.length - 2];
        }
    }



    private Boolean executeDBQuery(String query) throws Exception {
        PreparedStatement preparedStatement = postgresConnect.getConnection().prepareStatement(query);
        try {
           return preparedStatement.execute();
        } catch(Exception e){
            throw new Exception();
        } finally {
            preparedStatement.close();
        }
    }

    private void updateDBStatus(String payloadRefId,String status) throws Exception {
        String query = String.format(BaseJobConfig.postgresTable, status, System.currentTimeMillis(), payloadRefId);
        executeDBQuery(query);
    }

    @Override
    public List<String> metricsList() {
        return Arrays.asList(
                baseJobConfig.dispatcherSuccessCount,
                BaseJobConfig.dispatcherFailedCount,
                BaseJobConfig.dispatcherRetryCount,
                BaseJobConfig.dispatcherValidationFailedCount,
                BaseJobConfig.dispatcherValidationSuccessCount,
                BaseJobConfig.auditEventsCount
        );
    }

    public Map<String,Object> createAuditRecord(Map<String,Object> event) throws JsonProcessingException {
        Map<String,Object> audit = new HashMap<>();
        audit.put(Constants.EID, Constants.AUDIT);
        audit.put(Constants.HCX_RECIPIENT_CODE,getProtocolStringValue(event,Constants.HCX_RECIPIENT_CODE));
        audit.put(Constants.HCX_SENDER_CODE,getProtocolStringValue(event,Constants.HCX_SENDER_CODE));
        audit.put(Constants.API_CALL_ID,getProtocolStringValue(event,Constants.API_CALL_ID));
        audit.put(Constants.HCX_CORRELATION_ID,getProtocolStringValue(event,Constants.HCX_CORRELATION_ID));
        audit.put(Constants.WORKFLOW_ID,getProtocolStringValue(event,Constants.WORKFLOW_ID));
        audit.put(Constants.HCX_TIMESTAMP,getProtocolStringValue(event,Constants.HCX_TIMESTAMP));
        audit.put(Constants.ERROR_DETAILS,getProtocolMapValue(event,Constants.ERROR_DETAILS));
        audit.put(Constants.DEBUG_DETAILS,getProtocolMapValue(event,Constants.DEBUG_DETAILS));
        audit.put(Constants.MID,event.get(Constants.MID));
        audit.put(Constants.ACTION,event.get(Constants.ACTION));
        audit.put(Constants.HCX_STATUS,getProtocolStringValue(event,Constants.HCX_STATUS));
        audit.put(Constants.REQUESTED_TIME,event.get(Constants.ETS));
        audit.put(Constants.UPDATED_TIME,event.getOrDefault(Constants.UPDATED_TIME, Calendar.getInstance().getTime()));
        audit.put(Constants.ETS, Calendar.getInstance().getTime());
        audit.put(Constants.SENDER_ROLE, getCDataListValue(event, Constants.SENDER, Constants.ROLES));
        audit.put(Constants.RECIPIENT_ROLE, getCDataListValue(event, Constants.RECIPIENT, Constants.ROLES));
        audit.put(Constants.SENDER_NAME, getCDataStringValue(event, Constants.SENDER, Constants.PARTICIPANT_NAME));
        audit.put(Constants.RECIPIENT_NAME, getCDataStringValue(event, Constants.RECIPIENT, Constants.PARTICIPANT_NAME));
        audit.put(Constants.SENDER_PRIMARY_EMAIL, getCDataStringValue(event, Constants.SENDER, Constants.PRIMARY_EMAIL));
        audit.put(Constants.RECIPIENT_PRIMARY_EMAIL, getCDataStringValue(event, Constants.RECIPIENT, Constants.PRIMARY_EMAIL));
        audit.put(Constants.PAYLOAD, removeSensitiveData(payload));
        return audit;
    }


    public Map<String,Object> createAuditLog(Map<String,Object> event){
        Map<String,Object> audit = new HashMap<>();
        audit.put(Constants.EID, Constants.AUDIT);
        audit.put(Constants.ETS, Calendar.getInstance().getTime());
        audit.put(Constants.MID, event.get(Constants.MID));
        audit.put(Constants.OBJECT,new HashMap<>(){{
            put(Constants.ID, getProtocolStringValue(event,Constants.HCX_CORRELATION_ID));
            put(Constants.TYPE, getEntity((String)event.get(Constants.ACTION)));
        }});
        audit.put(Constants.CDATA, new HashMap<>(){{
            put(Constants.ACTION,event.get(Constants.ACTION));
            Map<String,Object> headers = (Map<String, Object>) event.get(Constants.HEADERS);
            Map<String,Object> protocol = (Map<String, Object>) headers.get(Constants.PROTOCOL);
            putAll(protocol);
        }});
        audit.put(Constants.EDATA,new HashMap<>(){{
            put(Constants.STATUS, getProtocolStringValue(event,Constants.HCX_STATUS));
        }});
        return audit;
    }

    public String removeSensitiveData(Map<String,Object> payload) throws JsonProcessingException {
        if (payload.containsKey(Constants.PAYLOAD)) {
            List<String> modifiedPayload = new ArrayList<>(Arrays.asList(payload.get(Constants.PAYLOAD).toString().split("\\.")));
            // remove encryption key
            modifiedPayload.remove(1);
            // remove ciphertext
            modifiedPayload.remove(2);
            String[] payloadValues = modifiedPayload.toArray(new String[modifiedPayload.size()]);
            StringBuilder sb = new StringBuilder();
            for (String value : payloadValues) {
                sb.append(value).append(".");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
           return JSONUtil.serialize(payload);
        }
    }


    public DispatcherResult dispatchRecipient(String baseSenderCode,  String action, Map<String,Object> parsedPayload) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String,Object> recipientDetails = fetchDetails(baseSenderCode);
        Map<String,Object> recipientContext = createRecipientContext(recipientDetails, action);
        Map<String,Object> updatedPayload = new HashMap<>();
        //TODO Remove this and use the utility for modifying the ciphertext
        updatedPayload.put(Constants.PAYLOAD,JSONUtil.createPayloadByValues(parsedPayload));
        return dispatcherUtil.dispatch(recipientContext, JSONUtil.serialize(updatedPayload));
    }


    public String getEmptyCipherText() {
        //TODO write logic here for fetching ciphertext value, as of now sending base 64 encoded string of an empty string
        String emptyCiphertext = "IiI=";
        return emptyCiphertext;
    }

}

