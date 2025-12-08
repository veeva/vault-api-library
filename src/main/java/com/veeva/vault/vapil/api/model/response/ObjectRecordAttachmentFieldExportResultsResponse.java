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
 * GET /api/{version}/vobjects/{object_name}/attachment_fields/actions/export/{job_id}/results
 */
public class ObjectRecordAttachmentFieldExportResultsResponse extends VaultResponse {

    @JsonProperty("data")
    public Data getData() {
        return (Data) this.get("data");
    }

    public void setData(Data data) {
        this.set("data", data);
    }

    public static class Data extends VaultModel {
        @JsonProperty("name")
        public String getName() {
            return getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("filename")
        public String getFilename() {
            return getString("filename");
        }

        public void setFilename(String filename) {
            this.set("filename", filename);
        }

        @JsonProperty("size")
        public Long getSize() {
            return getLong("size");
        }

        public void setSize(Long size) {
            this.set("size", size);
        }

        @JsonProperty("fileparts")
        public Integer getFileparts() {
            return getInteger("fileparts");
        }

        public void setFileparts(Integer fileparts) {
            this.set("fileparts", fileparts);
        }

        @JsonProperty("filepart_details")
        public List<FilePart> getFilepartDetails() {
            return (List<FilePart>) this.get("filepart_details");
        }

        public void setFilepartDetails(List<FilePart> filepartDetails) {
            this.set("filepart_details", filepartDetails);
        }

        public static class FilePart extends VaultModel {
            @JsonProperty("name")
            public String getName() {
                return getString("name");
            }

            public void setName(String name) {
                this.set("name", name);
            }

            @JsonProperty("filename")
            public String getFilename() {
                return getString("filename");
            }

            public void setFilename(String filename) {
                this.set("filename", filename);
            }

            @JsonProperty("filepart")
            public Integer getFilepart() {
                return getInteger("filepart");
            }

            public void setFilepart(Integer filepart) {
                this.set("filepart", filepart);
            }

            @JsonProperty("size")
            public Long getSize() {
                return getLong("size");
            }

            public void setSize(Long size) {
                this.set("size", size);
            }

            @JsonProperty("url")
            public String getUrl() {
                return getString("url");
            }

            public void setUrl(String url) {
                this.set("url", url);
            }
        }
    }
}