package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

public class PackageLog extends VaultModel {

    @JsonProperty("created_date__v")
    public String getCreatedDate() {
        return this.getString("created_date__v");
    }

    public void setCreatedDate(String createdDate) {
        this.set("created_date__v", createdDate);
    }

    @JsonProperty("filename")
    public String getFilename() {
        return this.getString("filename");
    }

    public void setFilename(String filename) {
        this.set("filename", filename);
    }

    @JsonProperty("url")
    public String getUrl() {
        return this.getString("url");
    }

    public void setUrl(String url) {
        this.set("url", url);
    }
}
