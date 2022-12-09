package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for email notifications API Response
 * <p>
 * GET /api/{version}/notifications/histories
 */
public class EmailNotificationHistoryResponse extends VaultResponse {

    @JsonProperty("responseDetails")
    public ResponseDetails getResponseDetails() {
        return (ResponseDetails) this.get("responseDetails");
    }

    public void setResponseDetails(ResponseDetails responseDetails) {
        this.set("responseDetails", responseDetails);
    }

    @JsonProperty("data")
    public List<EmailNotification> getData() {
        return (List<EmailNotification>) this.get("data");
    }

    public void setData(List<EmailNotification> emailNotifications) {
        this.set("data", emailNotifications);
    }

    public static class ResponseDetails extends VaultModel {

        @JsonProperty("offset")
        public int getOffset() { return this.getInteger("offset"); }

        public void setOffset(int offset) { this.set("offset", offset); }

        @JsonProperty("limit")
        public Integer getLimit() {
            return this.getInteger("limit");
        }

        public void setLimit(Integer limit) {
            this.set("limit", limit);
        }

        @JsonProperty("size")
        public Integer getSize() {
            return this.getInteger("size");
        }

        public void setSize(Integer size) {
            this.set("size", size);
        }

        @JsonProperty("total")
        public Integer getTotal() {
            return this.getInteger("total");
        }

        public void setTotal(Integer total) {
            this.set("total", total);
        }

        @JsonProperty("previous_page")
        public String getPreviousPage() {
            return this.getString("previous_page");
        }

        public void setPreviousPage(String previousPage) {
            this.set("previous_page", previousPage);
        }

        @JsonProperty("next_page")
        public String getNextPage() {
            return this.getString("next_page");
        }

        public void setNextPage(String nextPage) {
            this.set("next_page", nextPage);
        }

        /**
         * Determine if a next page exists for pagination
         *
         * @return true if a next page exists
         */
        @JsonIgnore
        public boolean hasNextPage() {
            return getNextPage() != null && !getNextPage().isEmpty();
        }

        /**
         * Determine if a previous page exists for pagination
         *
         * @return true if a previous page exists
         */
        @JsonIgnore
        public boolean hasPreviousPage() {
            return getPreviousPage() != null && !getPreviousPage().isEmpty();
        }
    }

    public static class EmailNotification extends VaultModel {

//    -----------------------------------------------------
//    Fields in alphabetical order

        @JsonProperty("document_id")
        public Integer getDocumentId() {
            return getInteger("document_id");
        }

        public void setDocumentId(Integer documentId) {
            this.set("document_id", documentId);
        }

        @JsonProperty("error_message")
        public String getErrorMessage() {
            return getString("error_message");
        }

        public void setErrorMessage(String errorMessage) {
            this.set("error_message", errorMessage);
        }

        @JsonProperty("included_notification_ids")
        public List<Long> getIncludedNotificationIds() {
            return getListLong("included_notification_ids");
        }

        public void setIncludedNotificationIds(List<Integer> includedNotificationIds) {
            this.set("included_notification_ids", includedNotificationIds);
        }

        @JsonProperty("major_version")
        public Integer getMajorVersion() {
            return getInteger("major_version");
        }

        public void setMajorVersion(Integer majorVersion) {
            this.set("major_version", majorVersion);
        }

        @JsonProperty("minor_version")
        public Integer getMinorVersion() {
            return getInteger("minor_version");
        }

        public void setMinorVersion(Integer minorVersion) {
            this.set("minor_version", minorVersion);
        }

        @JsonProperty("notification_id")
        public Integer getNotificationId() {
            return getInteger("notification_id");
        }

        public void setNotificationId(Integer notificationId) {
            this.set("notification_id", notificationId);
        }

        @JsonProperty("object_name")
        public String getObjectName() {
            return getString("object_name");
        }

        public void setObjectName(String objectName) {
            this.set("object_name", objectName);
        }

        @JsonProperty("object_record_id")
        public String getObjectRecordId() {
            return getString("object_record_id");
        }

        public void setObjectRecordId(String objectRecordId) {
            this.set("object_record_id", objectRecordId);
        }

        @JsonProperty("recipient_email")
        public String getRecipientEmail() {
            return getString("recipient_email");
        }

        public void setRecipientEmail(String recipientEmail) {
            this.set("recipient_email", recipientEmail);
        }

        @JsonProperty("recipient_id")
        public Integer getRecipientId() {
            return getInteger("recipient_id");
        }

        public void setRecipientId(Integer recipientId) {
            this.set("recipient_id", recipientId);
        }

        @JsonProperty("recipient_name")
        public String getRecipientName() {
            return getString("recipient_name");
        }

        public void setRecipientName(String recipientName) {
            this.set("recipient_name", recipientName);
        }

        @JsonProperty("send_date")
        public String getSendDate() {
            return getString("send_date");
        }

        public void setSendDate(String sendDate) {
            this.set("send_date", sendDate);
        }

        @JsonProperty("sender_id")
        public Integer getSenderId() {
            return getInteger("sender_id");
        }

        public void setSenderId(Integer senderId) {
            this.set("sender_id", senderId);
        }

        @JsonProperty("sender_name")
        public String getSenderName() {
            return getString("sender_name");
        }

        public void setSenderName(String senderName) {
            this.set("sender_name", senderName);
        }

        @JsonProperty("status")
        public String getString() {
            return getString("status");
        }

        public void setStatus(String status) {
            this.set("status", status);
        }

        @JsonProperty("status_date")
        public String getStatusDate() {
            return getString("status_date");
        }

        public void setStatusDate(String statusDate) {
            this.set("status_date", statusDate);
        }

        @JsonProperty("subject")
        public String getSubject() {
            return getString("subject");
        }

        public void setSubject(String subject) {
            this.set("subject", subject);
        }

        @JsonProperty("task_id")
        public Integer getTaskId() {
            return getInteger("task_id");
        }

        public void setTaskId(Integer taskId) {
            this.set("task_id", taskId);
        }

        @JsonProperty("template_name")
        public String getTemplateName() {
            return getString("template_name");
        }

        public void setTemplateName(String templateName) {
            this.set("template_name", templateName);
        }

        @JsonProperty("workflow_id")
        public Integer getWorkflowId() {
            return getInteger("workflow_id");
        }

        public void setWorkflowId(Integer workflowId) {
            this.set("workflow_id", workflowId);
        }
    }
}