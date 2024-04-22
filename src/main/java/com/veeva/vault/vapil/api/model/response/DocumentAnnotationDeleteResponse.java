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
 * Model for the delete document annotations response
 */
public class DocumentAnnotationDeleteResponse extends VaultResponse {
    @JsonProperty("data")
    public List<DeletedAnnotation> getData() {
        return (List<DeletedAnnotation>) this.get("data");
    }

    public void setData(List<DeletedAnnotation> data) {
        this.set("data", data);
    }

    public static class DeletedAnnotation extends VaultModel {

        @JsonProperty("document_id__v")
        public String getDocumentId() {
            return this.getString("document_id__v");
        }

        public void setDocumentId(String documentId) {
            this.set("document_id__v", documentId);
        }

        @JsonProperty("major_version__v")
        public Integer getMajorVersion() {
            return this.getInteger("major_version__v");
        }

        public void setMajorVersion(Integer majorVersion) {
            this.set("major_version__v", majorVersion);
        }

        @JsonProperty("minor_version__v")
        public Integer getMinorVersion() {
            return this.getInteger("minor_version__v");
        }

        public void setMinorVersion(Integer minorVersion) {
            this.set("minor_version__v", minorVersion);
        }

        @JsonProperty("annotation_id__v")
        public Integer getAnnotationId() {
            return this.getInteger("annotation_id__v");
        }

        public void setAnnotationId(Integer annotationId) {
            this.set("annotation_id__v", annotationId);
        }

        @JsonProperty("errors")
        public List<Error> getErrors() {
            return (List<Error>) this.get("errors");
        }

        public void setErrors(List<Error> errors) {
            this.set("errors", errors);
        }

        public static class Error extends VaultModel {

            @JsonProperty("type")
            public String getType() {
                return this.getString("type");
            }

            public void setType(String type) {
                this.set("type", type);
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