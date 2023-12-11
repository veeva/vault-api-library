package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

public class ResumableUploadSession extends VaultModel {
    @JsonProperty("created_date")
    public String getCreatedDate() {
        return getString("created_date");
    }

    public void setCreatedDate(String createdDate) {
        this.set("created_date", createdDate);
    }

    @JsonProperty("expiration_date")
    public String getExpirationDate() {
        return getString("expiration_date");
    }

    public void setExpirationDate(String expirationDate) {
        this.set("expiration_date", expirationDate);
    }

    @JsonProperty("owner")
    public Long getOwner() {
        return getLong("owner");
    }

    public void setOwner(Long owner) {
        this.set("owner", owner);
    }

    @JsonProperty("id")
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        this.set("id", id);
    }

    @JsonProperty("last_uploaded_date")
    public String getLastUploadedDate() {
        return getString("last_uploaded_date");
    }

    public void setLastUploadedDate(String lastUploadedDate) {
        this.set("last_uploaded_date", lastUploadedDate);
    }

    @JsonProperty("path")
    public String getPath() {
        return getString("path");
    }

    public void setPath(String path) {
        this.set("path", path);
    }

    @JsonProperty("size")
    public Long getSize() {
        return getLong("size");
    }

    public void setSize(Long size) {
        this.set("size", size);
    }

    @JsonProperty("uploaded_parts")
    public Integer getUploadedParts() {
        return getInteger("uploaded_parts");
    }

    public void setUploadedParts(Integer uploadedParts) {
        this.set("uploaded_parts", uploadedParts);
    }

    @JsonProperty("uploaded")
    public Long getUploaded() {
        return getLong("uploaded");
    }

    public void setUploaded(Long uploaded) {
        this.set("uploaded", uploaded);
    }

    @JsonProperty("name")
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        this.set("name", name);
    }

    @JsonProperty("overwrite")
    public Boolean getOverwrite() {
        return getBoolean("overwrite");
    }

    public void setOverwrite(Boolean overwrite) {
        this.set("overwrite", overwrite);
    }
}
