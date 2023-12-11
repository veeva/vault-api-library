package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ResumableUploadSessionPart;

public class FileStagingSessionPartResponse extends VaultResponse {

    @JsonProperty("data")
    public ResumableUploadSessionPart getData() {
        return (ResumableUploadSessionPart) this.get("data");
    }

    public void setData(ResumableUploadSessionPart data) {
        this.set("data", data);
    }
}
