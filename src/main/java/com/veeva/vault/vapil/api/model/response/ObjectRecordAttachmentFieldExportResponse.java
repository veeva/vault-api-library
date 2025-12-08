/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/vobjects/{object_name}/attachment_fields/actions/export
 */
public class ObjectRecordAttachmentFieldExportResponse extends VaultResponse {

    @JsonProperty("data")
    public Data getData() {
        return (Data) this.get("data");
    }

    public void setData(Data data) {
        this.set("data", data);
    }

    public static class Data extends VaultModel {

        @JsonProperty("responseStatus")
        public String getResponseStatus() {
            return this.getString("responseStatus");
        }

        public void setResponseStatus(String responseStatus) {
            this.set("responseStatus", responseStatus);
        }


        @JsonProperty("records")
        public List<ExportResult> getRecords() {
            return (List<ExportResult>) this.get("records");
        }

        public void setRecords(List<ExportResult> records) {
            this.set("records", records);
        }

        @JsonProperty("job_id")
        public String getJobId() {
            return this.getString("job_id");
        }

        public void setJobId(String jobId) {
            this.set("job_id", jobId);
        }

        public static class ExportResult extends VaultModel {
            public String getResponseStatus() {
                return this.getString("responseStatus");
            }

            @JsonProperty("responseStatus")
            public void setResponseStatus(String responseStatus) {
                this.set("responseStatus", responseStatus);
            }

            @JsonProperty("data")
            public ExportDetails getData() {
                return (ExportDetails) this.get("data");
            }

            public void setData(ExportDetails data) {
                this.set("data", data);
            }

            @JsonProperty("warnings")
            public List<Warning> getWarnings() {
                return (List<Warning>) this.get("warnings");
            }

            public void setWarnings(List<Warning> warnings) {
                this.set("warnings", warnings);
            }

            @JsonProperty("errors")
            public List<APIResponseError> getErrors() {
                return (List<APIResponseError>) this.get("errors");
            }

            public void setErrors(List<APIResponseError> errors) {
                this.set("errors", errors);
            }

            public static class ExportDetails extends VaultModel {
                @JsonProperty("id")
                public String getId() {
                    return this.getString("id");
                }

                public void setId(String id) {
                    this.set("id", id);
                }

                @JsonProperty("id_param_value")
                public String getIdParamValue() {
                    return this.getString("id_param_value");
                }

                public void setIdParamValue(String idParamValue) {
                    this.set("id_param_value", idParamValue);
                }
            }

            public static class Warning extends VaultModel {
                @JsonProperty("warning_type")
                public String getWarningType() {
                    return this.getString("warning_type");
                }

                public void setWarningType(String warningType) {
                    this.set("warning_type", warningType);
                }

                @JsonProperty("message")
                public String getMessage() {
                    return this.getString("message");
                }

                public void setMessage(String message) {
                    this.set("message", message);
                }

            }
        }
    }
}