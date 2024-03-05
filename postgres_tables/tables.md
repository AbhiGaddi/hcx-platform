## List of HCX Postgres Tables 

### Table 1 : Payload
### Description : Used to Store the payload data
```
CREATE TABLE IF NOT EXISTS payload(
       mid character varying  NOT NULL,
       data character varying ,
       action character varying,
       status character varying ,
       retrycount int,
       lastupdatedon bigInt,
       CONSTRAINT payload_pkey PRIMARY KEY (mid)
); 
```
### Table 2 : Subscription Table
### Description : Notification subscription
```
CREATE TABLE IF NOT EXISTS subscription (
       subscription_id uuid, 
       subscription_request_id uuid, 
       topic_code character varying, 
       sender_code character varying, 
       recipient_code character varying, 
       subscription_status varchar(15), 
       lastUpdatedOn bigInt, 
       createdon bigInt, 
       expiry bigInt, 
       is_delegated boolean, 
       CONSTRAINT subscription_pkey PRIMARY KEY (sender_code,topic_code,recipient_code), 
       CONSTRAINT subscription_ukey UNIQUE(subscription_id) 
);
```
### Table 3 : Onboard verifier Table
### Description : Onboarding participant verification
```
CREATE TABLE IF NOT EXISTS onboard_verifier ( 
       applicant_email character varying not null, 
       applicant_code character varying not null, 
       verifier_code character varying, 
       status character varying, 
       createdon bigInt, 
       updatedon bigInt, 
       participant_code character varying
);
```
### Table 4 : Onboard invite Table
### Description : Onboarding invite
```
CREATE TABLE IF NOT EXISTS onboard_user_invite_details ( 
       participant_code character varying, 
       user_email character varying, 
       invited_by character varying, 
       invite_status character varying, 
       created_on bigInt,
       updated_on bigInt 
);
```
### Table 5 : Onboard verification table
### Description : Onboarding verification
```
CREATE TABLE IF NOT EXISTS onboard_verification (  
       participant_code character varying not null,
       primary_email character varying,
       primary_mobile character varying, 
       createdon bigInt,
       updatedon bigInt,
       expiry bigInt,
       phone_verified boolean not null,
       email_verified boolean not null,
       status character varying, 
       regenerate_count int,
       last_regenerate_date date,
       attempt_count bigInt, 
       comments character varying, 
       phone_short_url character varying, 
       phone_long_url character varying, 
       onboard_validation_properties json, 
       participant_validation_properties json,
       CONSTRAINT onboard_verification_pkey PRIMARY KEY         (participant_code)
);
```