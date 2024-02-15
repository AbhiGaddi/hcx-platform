package org.swasth.dp.core.util


import org.yaml.snakeyaml.Yaml

import java.io.{ByteArrayInputStream, IOException}
import java.util
class NotificationUtil {

  var notifications: util.List[util.Map[String, AnyRef]] = null

  var topicCodes: util.List[String] = new util.ArrayList[String]()

  loadNotifications()

  @throws[IOException]
  private def loadNotifications(): Unit = {
    notifications = YamlUtil.convertYaml(new ByteArrayInputStream("[ { \"topic_code\": \"notif-participant-onboarded\", \"title\": \"Participant Onboarding\", \"description\": \"Notification about new participant joining the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been successfully onboarded on ${hcx_name} on ${DDMMYYYY}. Transactions relating ${participant_name} can be initiated using ${participant_code}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-de-boarded\", \"title\": \"Participant De-boarding\", \"description\": \"Notification about new participant leaving the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been deboarded from ${hcx_name} on ${DDMMYYYY}. Platform will not support the transaction relating ${participant_name}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-new-network-feature-added\", \"title\": \"New Feature Support\", \"description\": \"Notification about new feature launch for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now supports $(feature_code) on its platform. All participants can now initiate transactions relating to $(feature_code).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-network-feature-removed\", \"title\": \"End of support for old feature\", \"description\": \"Notification about removing an old feature for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(feature_code) on its platform.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-protocol-version-support-ended\", \"title\": \"End of support for old protocol version\", \"description\": \"Notification about ending support for an older version of the protocol by the HCX.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(version_code) on its platform. All participants are requested to upgrade to $(version_code) or above to transact on $(hcx_name).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-gateway-downtime\", \"title\": \"Network maintenance/Downtime\", \"description\": \"Notification about planned downtime/maintenance of the gateway switch.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} will be facing downtime from ${DDMMYYYY} to ${DDMMYYYY} due to planned maintenance. Sorry for inconvenience and please plan your operations accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-gateway-policy-sla-change\", \"title\": \"Policy change - SLA\", \"description\": \"Notification about the policy changes about the SLAs for the participant in the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} is changing the SLA policy for the $(Usecase_code) going forward. Please plan your operations accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-policy-security-update\", \"title\": \"Policy change - Security & Privacy\", \"description\": \"Notification about the data security & privacy standards for the participant in the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} is now compliant with latest FHIR security and privacy protocols. Please update security and privacy  protocols accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-compliance-expiry\", \"title\": \"Compliance expiry\", \"description\": \"Notification about the compliance certificate expiration for the participant in the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} compliance certificate will be expiring on ${DDMMYYYY}. Please renew your compliance certificate before ${DDMMYYYY}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-subscription-expiry\", \"title\": \"Subscription expiry/renew\", \"description\": \"Notification about the notification subscription expiration for the participant in the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} notification subscription to ${participant_name} will be expiring on ${DDMMYYYY}. Please renew your subscription before ${DDMMYYYY} to continue receiving notifications.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-encryption-key-expiry\", \"title\": \"Encryption Key expiration\", \"description\": \"Notification about the encryption key expiration for the participant in the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} encryption key will be expiring on ${DDMMYYYY}. Please renew your encryption key before ${DDMMYYYY} to carry on operating on HCX.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-system-downtime\", \"title\": \"System maintenance/Downtime\", \"description\": \"Notification about participant system downtime/maintenance\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} will be facing downtime from ${DDMMYYYY} to ${DDMMYYYY} due to planned maintenance. Sorry for inconvenience and please plan your operations accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-new-protocol-version-support\", \"title\": \"Support for new version of protocol\", \"description\": \"Notification about participant system supporting new version of the HCX protocol.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} now supports $(version_code) on its platform. All participants are requested to upgrade to $(version_code) or above to transact with ${participant_name}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-terminology-version-support\", \"title\": \"Support for prescribed terminologies\", \"description\": \"Notification about participant system supporting particular format of terminologies of the HCX protocol.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} now supports $(Master_list_version) on its platform. All participants are requested to upgrade to $(Master_list_version) or above to transact with ${participant_name}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-remove-protocol-feature\", \"title\": \"End of life for an existing capability\", \"description\": \"Notification about participant system ending/discontinuing a particular feature of the HCX protocol.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} now does not support $(feature_code) on its platform.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-product-change-support\", \"title\": \"New insurance plans/products, end of support for existing products/plans\", \"description\": \"Notification about participant system adding new product or a particular feature of the HCX protocol.\", \"allowed_senders\": [ \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} now supports $(feature_code) on its platform. All participants can now initiate transactions relationg to $(feature_code).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-policy-update\", \"title\": \"Policy Compliance changes  - terms of services\", \"description\": \"Notification about participant system changing terms of the services.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} is changing the service terms for policy for the $(Usecase_code) going forward. Please plan your operations accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-policy-sla-update\", \"title\": \"Policy Compliance changes  - SLAs\", \"description\": \"Notification about participant system SLA of the services.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Participant\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} is changing the SLA policy for the $(Usecase_code) going forward. Please plan your operations accordingly.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-workflow-update\", \"title\": \"Workflow update\", \"description\": \"Notification about coverage eligibility, predetermination, preauth, claim and paymentnotice workflow updates.\", \"allowed_senders\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has updated a ${entity_type} request with correlation id: ${correlation_id} status to ${status}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-admission-case\", \"title\": \"Admission case - For a particular disease\", \"description\": \"Notification about patient admission for a particular disease\", \"allowed_senders\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", ], \"allowed_recipients\": [ \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has initiated a insurance claim workflow on admission for ${Disease_code}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-claim-particular-disease\", \"title\": \"Claim initiation for a particular disease\", \"description\": \"Notification about workflow updates for claim initiation for a particular disease..\", \"allowed_senders\": [ \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has initiated a insurance claim workflow on admission for ${Disease_code}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-claim-status-update\", \"title\": \"Notifications about status change of claims of a patient\", \"description\": \"Notification about workflow updates on a identified policy.\", \"allowed_senders\": [ \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has updated the status for ${policy_ID} to ${Status}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-coverage-eligibility-change\", \"title\": \"Notifications about change in coverage eligibility of a patient\", \"description\": \"Notification about change in coverage eligibility on a identified policy.\", \"allowed_senders\": [ \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has updated the coverage eligibility for ${policy_ID}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-claim-reimbursement\", \"title\": \"Notifications about reimbursements of claim for a patient\", \"description\": \"Notification about claim reimbursement..\", \"allowed_senders\": [ \"payor\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has approved the reimbursement payment for ${policy_ID}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-claim-reimbursement-inactive\", \"title\": \"Notifications about reimbursements of claim for a patient\", \"description\": \"Notification about claim reimbursement..\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Workflow\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has approved the reimbursement payment for ${policy_ID}.\\\"}\", \"status\": \"Inactive\" }, { \"topic_code\": \"notif-subscription-update\", \"title\": \"Subscription Update Notification\", \"description\": \"Notification about subscription update.\", \"allowed_senders\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has updated the subscription: ${subscription_id}, updated properties are: ${properties}\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-encryption-cert-expired\", \"title\": \"Encryption certificate expiry\", \"description\": \"Notification about encryption certificate expiry\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"payor\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Targeted\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"Your encryption certificate got expired, please update your details with new certificate.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-onboarded\", \"title\": \"Participant Onboarding\", \"description\": \"Notification about new participant joining the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been successfully onboarded on ${hcx_name} on ${DDMMYYYY}. Transactions relating ${participant_name} can be initiated using ${participant_code}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-de-boarded\", \"title\": \"Participant De-boarding\", \"description\": \"Notification about new participant leaving the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been deboarded from ${hcx_name} on ${DDMMYYYY}. Platform will not support the transaction relating ${participant_name}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-new-network-feature-added\", \"title\": \"New Feature Support\", \"description\": \"Notification about new feature launch for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now supports $(feature_code) on its platform. All participants can now initiate transactions relating to $(feature_code).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-network-feature-removed\", \"title\": \"End of support for old feature\", \"description\": \"Notification about removing an old feature for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(feature_code) on its platform.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-protocol-version-support-ended\", \"title\": \"End of support for old protocol version\", \"description\": \"Notification about ending support for an older version of the protocol by the HCX.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(version_code) on its platform. All participants are requested to upgrade to $(version_code) or above to transact on $(hcx_name).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-certificate-revocation\", \"title\": \"Certificate Revocation Notification\", \"description\": \"Notification about planned downtime/maintenance of the gateway switch.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} will be facing downtime from ${DDMMYYYY} to ${DDMMYYYY} due to planned maintenance. Sorry for inconvenience and please plan your operations accordingly.\\\"}\", \"status\": \"Active\" } , { \"topic_code\": \"notif-participant-onboarded\", \"title\": \"Participant Onboarding\", \"description\": \"Notification about new participant joining the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been successfully onboarded on ${hcx_name} on ${DDMMYYYY}. Transactions relating ${participant_name} can be initiated using ${participant_code}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-participant-de-boarded\", \"title\": \"Participant De-boarding\", \"description\": \"Notification about new participant leaving the ecosystem.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${participant_name} has been deboarded from ${hcx_name} on ${DDMMYYYY}. Platform will not support the transaction relating ${participant_name}.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-new-network-feature-added\", \"title\": \"New Feature Support\", \"description\": \"Notification about new feature launch for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now supports $(feature_code) on its platform. All participants can now initiate transactions relating to $(feature_code).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-network-feature-removed\", \"title\": \"End of support for old feature\", \"description\": \"Notification about removing an old feature for the participants on the network.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(feature_code) on its platform.\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-protocol-version-support-ended\", \"title\": \"End of support for old protocol version\", \"description\": \"Notification about ending support for an older version of the protocol by the HCX.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} now does not support $(version_code) on its platform. All participants are requested to upgrade to $(version_code) or above to transact on $(hcx_name).\\\"}\", \"status\": \"Active\" }, { \"topic_code\": \"notif-invalid-certificate\", \"title\": \"Invalid Certificate Notification\", \"description\": \"Notification about planned downtime/maintenance of the gateway switch.\", \"allowed_senders\": [ \"HIE/HIO.HCX\" ], \"allowed_recipients\": [ \"payor\", \"provider\", \"provider.hospital\", \"provider.clinic\", \"provider.practitioner\", \"provider.diagnostics\", \"provider.pharmacy\", \"agency.tpa\", \"agency.regulator\", \"research\", \"member.isnp\", \"agency.sponsor\", \"HIE/HIO.HCX\" ], \"type\": \"Broadcast\", \"category\": \"Network\", \"priority\": 1, \"template\": \"{\\\"message\\\": \\\"${hcx_name} will be facing downtime from ${DDMMYYYY} to ${DDMMYYYY} due to planned maintenance. Sorry for inconvenience and please plan your operations accordingly.\\\"}\", \"status\": \"Active\"}]".getBytes()), classOf[util.List[util.Map[String, AnyRef]]])
    notifications.forEach((obj: util.Map[String, AnyRef]) => topicCodes.add(obj.get(Constants.TOPIC_CODE).asInstanceOf[String]))
  }

  def isValidCode(code: String): Boolean = topicCodes.contains(code)

  def getNotification(code: String): util.Map[String, AnyRef] = {
    var notification = new util.HashMap[String, AnyRef]
    val result = notifications.stream.filter((obj: util.Map[String, AnyRef]) => obj.get(Constants.TOPIC_CODE) == code).findFirst
    if (result.isPresent) notification =  result.get.asInstanceOf[util.HashMap[String, AnyRef]]
    notification
  }

}

