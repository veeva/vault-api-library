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
public class DocumentAnnotationPlacemarkTypeMetadataResponse extends VaultResponse {

    @JsonProperty("data")
    public PlacemarkTypeMetadata getData() {
        return (PlacemarkTypeMetadata) this.get("data");
    }

    @JsonProperty("data")
    public void setData(PlacemarkTypeMetadata data) {
        this.set("data", data);
    }

    public static class PlacemarkTypeMetadata extends VaultModel {
        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("fields")
        public List<PlacemarkField> getFields() {
            return (List<PlacemarkField>) this.get("fields");
        }

        @JsonProperty("fields")
        public void setFields(List<PlacemarkField> fields) {
            this.set("fields", fields);
        }

        public static class PlacemarkField extends VaultModel {
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