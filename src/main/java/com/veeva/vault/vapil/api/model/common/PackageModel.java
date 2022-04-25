package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for Package data
 */
public class PackageModel extends VaultModel {

    @JsonProperty("name__v")
    @JsonAlias({"name"})
    public String getName() {
        if (this.getFieldNames().contains("name"))
            return getString("name");
        else
            return getString("name__v");
    }

    public void setName(String name) {
        this.set("name__v", name);
    }

    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        this.set("id", id);
    }

    @JsonProperty("checksum__v")
    public String getChecksum() {
        return getString("checksum__v");
    }

    public void setChecksum(String checksum) {
        this.set("checksum__v", checksum);
    }

}
