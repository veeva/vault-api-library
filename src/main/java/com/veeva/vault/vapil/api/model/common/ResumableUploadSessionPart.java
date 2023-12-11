package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

public class ResumableUploadSessionPart extends VaultModel {

    @JsonProperty("part_number")
    public Integer getPartNumber() {
        return this.getInteger("part_number");
    }

    public void setPartNumber(Integer partNumber) {
        this.set("part_number", partNumber);
    }

    @JsonProperty("size")
    public Integer getSize() {
        return this.getInteger("size");
    }

    public void setSize(Integer size) {
        this.set("size", size);
    }

    @JsonProperty("part_content_md5")
    public String getPartContentMd5() {
        return this.getString("part_content_md5");
    }

    public void setPartContentMd5(String partContentMd5) {
        this.set("part_content_md5", partContentMd5);
    }
}
