package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ResumableUploadSessionPart;

import java.util.List;

public class FileStagingSessionPartBulkResponse extends VaultResponse {

    @JsonProperty("data")
    public List<ResumableUploadSessionPart> getData() {
        return (List<ResumableUploadSessionPart>) this.get("data");
    }

    public void setData(List<ResumableUploadSessionPart> data) {
        this.set("data", data);
    }
}
