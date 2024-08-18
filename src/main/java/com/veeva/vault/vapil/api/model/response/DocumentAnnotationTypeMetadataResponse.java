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
 * Model for the document annotation response
 */
public class DocumentAnnotationTypeMetadataResponse extends VaultResponse {

    @JsonProperty("data")
    public AnnotationTypeMetadata getData() {
        return (AnnotationTypeMetadata) this.get("data");
    }

    @JsonProperty("data")
    public void setData(AnnotationTypeMetadata data) {
        this.set("data", data);
    }

    public static class AnnotationTypeMetadata extends VaultModel {
        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("allows_replies")
        public Boolean getAllowsReplies() {
            return this.getBoolean("allows_replies");
        }

        @JsonProperty("allows_replies")
        public void setAllowsReplies(Boolean allowsReplies) {
            this.set("allows_replies", allowsReplies);
        }

        @JsonProperty("allowed_placemark_types")
        public List<String> getAllowedPlacemarkTypes() {
            return (List<String>) this.get("allowed_placemark_types");
        }

        @JsonProperty("allowed_placemark_types")
        public void setAllowedPlacemarkTypes(List<String> allowedPlacemarkTypes) {
            this.set("allowed_placemark_types", allowedPlacemarkTypes);
        }

        @JsonProperty("allowed_reference_types")
        public List<String> getAllowedReferenceTypes() {
            return (List<String>) this.get("allowed_reference_types");
        }

        @JsonProperty("allowed_reference_types")
        public void setAllowedReferenceTypes(List<String> allowedReferenceTypes) {
            this.set("allowed_reference_types", allowedReferenceTypes);
        }

        @JsonProperty("fields")
        public List<AnnotationField> getFields() {
            return (List<AnnotationField>) this.get("fields");
        }

        @JsonProperty("fields")
        public void setFields(List<AnnotationField> fields) {
            this.set("fields", fields);
        }

        public static class AnnotationField extends VaultModel {
            @JsonProperty("name")
            public String getName() {
                return this.getString("name");
            }

            @JsonProperty("name")
            public void setName(String name) {
                this.set("name", name);
            }

            @JsonProperty("type")
            public String getType() {
                return this.getString("type");
            }

            @JsonProperty("type")
            public void setType(String type) {
                this.set("type", type);
            }

            @JsonProperty("system_managed")
            public Boolean getSystemManaged() {
                return this.getBoolean("system_managed");
            }

            @JsonProperty("system_managed")
            public void setSystemManaged(Boolean systemManaged) {
                this.set("system_managed", systemManaged);
            }

            @JsonProperty("value_set")
            public List<String> getValueSet() {
                return (List<String>) this.get("value_set");
            }

            @JsonProperty("value_set")
            public void setValueSet(List<String> valueSet) {
                this.set("value_set", valueSet);
            }
        }

    }
}