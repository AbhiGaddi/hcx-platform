package org.swasth.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

  // APIs
    public static String PARTICIPANT_CREATE = "/participant/create";
    public static String PARTICIPANT_DELETE = "/participant/delete";
    public static String CLAIM_SUBMIT = "/claim/submit";
    public static String CLAIM_ONSUBMIT = "/claim/on_submit";
    public static String NOTIFICATION_SUBSCRIPTION_UPDATE = "/notification/subscription/update";
    public static String NOTIFICATION_NOTIFY = "/notification/notify";
    public static String CDATA = "cdata";
    public static String EDATA = "edata";
    public static String SENDER = "sender";
    public static String RECIPIENT = "recipient";
    public static String PROTECTED = "protected";
    public static String ENCRYPTED_KEY = "encrypted_key";
    public static String IV = "iv";
    public static String CIPHERTEXT = "ciphertext";
    public static String TAG = "tag";
    public static int PAYLOAD_LENGTH = 5;
    public static String MID = "mid";
    public static String PAYLOAD = "payload";
    public static String ETS = "ets";
    public static String ACTION = "action";
    public static String HEADERS = "headers";
    public static String JOSE = "jose";
    public static String PROTOCOL = "protocol";
    public static String DOMAIN = "domain";
    public static String LOG_DETAILS = "log_details";
    public static String HCX_ERROR_DETAILS = "x-hcx-error_details";
    public static String DEBUG_DETAILS = "x-hcx-debug_details";
    public static String CODE = "code";
    public static String MESSAGE = "message";
    public static String TRACE = "trace";
    public static String SUBMITTED = "submitted";
    public static String EID = "eid";
    public static String END_POINT = "endpoint_url";
    public static String STATUS = "status";
    public static String REQUESTED_TIME = "requestTimeStamp";
    public static String UPDATED_TIME = "updatedTimestamp";
    public static String SENDER_ROLE = "senderRole";
    public static String RECIPIENT_ROLE = "recipientRole";
    public static String SENDER_NAME = "senderName";
    public static String RECIPIENT_NAME = "recipientName";
    public static String SENDER_PRIMARY_EMAIL = "senderPrimaryEmail";
    public static String RECIPIENT_PRIMARY_EMAIL = "recipientPrimaryEmail";
    public static String ROLES = "roles";
    public static String RETRY_INDEX = "retryIndex";
    public static String TYPE = "type";
    public static String BROADCAST = "Broadcast";
    public static String RECIPIENT_ID = "recipientId";
    public static String PARTICIPANT_CODE = "participant_code";
    public static String TEMPLATE = "template";
    public static String PARTICIPANTS = "participants";
    public static String PARTICIPANT_SEARCH = "/v1/participant/search";
    public static String NAME = "name";
    public static String AUDIT = "AUDIT";
    public static String TOTAL_DISPATCHES = "totalDispatches";
    public static String SUCCESSFUL_DISPATCHES = "successfulDispatches";
    public static String FAILED_DISPATCHES = "failedDispatches";
    public static String RESULT_DETAILS = "resultDetails";
    public static List<String>  INVALID_STATUS = Arrays.asList("Inactive", "Blocked");
    public static List<String>  StringID_STATUS = Arrays.asList("Created", "Active");
    public static String ACTIVE = "Active";
    public static String ENTITY_TYPE = "entity_type";

    //Event Fields
    public static String HCX_SENDER_CODE = "x-hcx-sender_code";
    public static String HCX_RECIPIENT_CODE = "x-hcx-recipient_code";
    public static String API_CALL_ID = "x-hcx-api_call_id";
    public static String HCX_CORRELATION_ID = "x-hcx-correlation_id";
    public static String WORKFLOW_ID = "x-hcx-workflow_id";
    public static String HCX_TIMESTAMP = "x-hcx-timestamp";
    public static String TIMESTAMP = "timestamp";
    public static String DEBUG_FLAG = "x-hcx-debug_flag";
    public static String HCX_STATUS = "x-hcx-status";
    public static String NOTIFICATION_HEADERS = "x-hcx-notification_headers";
    public static String TOPIC_CODE = "topic_code";
    public static String EXPIRY = "expiry";
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String NOTIFICATION_REQ_ID = "notification_request_id";
    public static String NOTIFICATION_DATA = "notification_data";
    public static String SUBSCRIPTIONS = "subscriptions";
    public static String SUBSCRIPTION_ID = "subscription_id";
    public static String SUBSCRIPTION_REQUEST_ID = "subscription_id";
    public static String RECIPIENT_CODE = "recipient_code";
    public static String SENDER_CODE = "sender_code";
    public static String RECIPIENT_CODES = "recipient_codes";
    public static String RECIPIENT_ROLES = "recipient_roles";
    public static String SUBSCRIPTION_STATUS = "subscription_status";
    public static String ALLOWED_RECIPIENTS = "allowed_recipients";
    public static String CATEGORY = "category";
    public static String NETWORK = "Network";
    public static String ERROR_DETAILS = "error_details";
    public static String ERROR_STATUS = "response.error";
    public static String DISPATCH_STATUS = "request.dispatched";
    public static String QUEUED_STATUS = "request.queued";
    public static String REQ_RETRY = "request.retry";
    public static String INPUT_EVENT = "inputEvent";
    public static String MASTER_DATA = "masterData";
    public static String RESOLVED_TEMPLATE = "resolvedTemplate";
    public static String PARTICIPANT_DETAILS = "participantDetails";
    public static String OBJECT = "object";
    public static String ID = "id";
    public static String PARTICIPANT_NAME = "participant_name";
    public static String PRIMARY_EMAIL = "primary_email";
    public static String HCX_NAME = "hcx_name";
    public static String DDMMYYYY = "DDMMYYYY";
    public static String WORKFLOW = "Workflow";
    public static String PROPERTIES = "properties";
    public static String PROPS = "props";
    public static String RECIPIENT_TYPE = "recipient_type";
    public static String SUBSCRIPTION = "subscription";
    public static String PARTICIPANT_ROLE = "participant_role";
    public static String RECIPIENTS = "recipients";
    public static String CORRELATIONID = "correlation_id";

    // Notification topic codes
    public static String PARTICIPANT_ONBOARD = "notif-participant-onboarded";
    public static String CLAIM_INITIATED = "notif-claim-initiation";
    public static String CLAIM_CLOSURE = "notif-claim-closure";
    public static String SUBSCRIPTION_UPDATE = "notif-subscription-update";

    //Search Fields
    public static String SEARCH_REQUEST = "x-hcx-search";
    public static String SEARCH_RESPONSE = "x-hcx-search_response";
    public static String SEARCH_FILTERS = "filters";
    public static String SEARCH_FILTERS_RECEIVER = "receivers";
    public static String SEARCH_COUNT = "count";
    public static String SEARCH_ENTITY_COUNT = "entity_counts";
    public static String PARTIAL_RESPONSE = "response.partial";
    public static String COMPLETE_RESPONSE = "response.complete";
    public static String REQUEST_INITIATED = "request.initiated";


    public static String OPEN_STATUS = "OPEN";
    public static String RETRY_STATUS = "RETRY";
    public static String CLOSE_STATUS = "CLOSE";
    public static String PARTIAL_STATUS = "PARTIAL";
    public static String FAIL_STATUS = "FAIL";
    //public static String DISPATCH_STATUS = "request.dispatched"
    //public static String ERROR_STATUS = "response.error"

    public static String StringID_RECIPIENT = "ERR_INpublic static StringID_RECIPIENT";
    public static String ERR_NOTIFICATION_EXPIRED = "ERR_NOTIFICATION_EXPIRED";
    public static String RECIPIENT_ERROR_CODE = "ERR_RECIPIENT_NOT_AVAILABLE";
    public static String RECIPIENT_ERROR_MESSAGE = "Please provide correct recipient code";
    public static String RECIPIENT_ERROR_LOG = "Recipient endpoint url is empty";

    // JWT token properties
    public static String JTI = "jti";
    public static String ISS = "iss";
    public static String SUB = "sub";
    public static String IAT = "iat";
    public static String EXP = "exp";
    public static String TYP = "typ";
    public static String JWT = "JWT";
    public static String ALG = "alg";
    public static String RS256 = "RS256";


    public static String NOTIFICATION_SUBSCRIBE = "/notification/subscribe";
    public static String NOTIFICATION_UNSUBSCRIBE = "/notification/unsubscribe";
    public static String NOTIFICATION_ONSUBSCRIBE = "/notification/on_subscribe";
    public static String SENDER_LIST = "sender_list";
    public static String SUBSCRIPTION_MAP = "subscription_map";
}
