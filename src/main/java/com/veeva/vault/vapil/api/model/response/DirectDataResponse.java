package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/services/directdata/files
 */
public class DirectDataResponse extends VaultResponse {

    @JsonProperty("data")
    public List<DirectDataItem> getData() {
        return (List<DirectDataItem>) this.get("data");
    }

    public void setData(List<DirectDataItem> data) {
        this.set("data", data);
    }

    @JsonProperty("responseDetails")
    public ResponseDetails getResponseDetails() {
        return (ResponseDetails) this.get("responseDetails");
    }

    public void setResponseDetails(ResponseDetails responseDetails) {
        this.set("responseDetails", responseDetails);
    }

    public static class ResponseDetails extends VaultModel {
        @JsonProperty("total")
        public Integer getTotal() {
            return getInteger("total");
        }

        public void setTotal(Integer total) {
            this.set("total", total);
        }
    }

    public static class DirectDataItem extends VaultModel {
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

        @JsonProperty("extract_type")
        public String getExtractType() {
            return getString("extract_type");
        }

        public void setExtractType(String extractType) {
            this.set("extract_type", extractType);
        }

        @JsonProperty("start_time")
        public String getStartTime() {
            return getString("start_time");
        }

        public void setStartTime(String startTime) {
            this.set("start_time", startTime);
        }

        @JsonProperty("stop_time")
        public String getStopTime() {
            return getString("stop_time");
        }

        public void setStopTime(String stopTime) {
            this.set("stop_time", stopTime);
        }

        @JsonProperty("record_count")
        public Integer getRecordCount() {
            return getInteger("record_count");
        }

        public void setRecordCount(Integer recordCount) {
            this.set("record_count", recordCount);
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
