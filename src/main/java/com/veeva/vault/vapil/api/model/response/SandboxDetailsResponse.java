package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SandboxDetailsResponse extends VaultResponse {

    @JsonProperty("data")
    public Sandbox getData() {
        return (Sandbox) this.get("data");
    }

    public void setData(Sandbox data) {
        this.set("data", data);
    }


    public static class Sandbox extends VaultModel {

        @JsonProperty("created_by")
        public Long getCreatedBy() {
            return this.getLong("created_by");
        }

        public void setCreatedBy(Long createdBy) {
            this.set("created_by", createdBy);
        }

        @JsonProperty("created_date")
        public String getCreatedDate() {
            return this.getString("created_date");
        }

        public void setCreatedDate(String createdDate) {
            this.set("created_date", createdDate);
        }

        @JsonProperty("domain")
        public String getDomain() {
            return this.getString("domain");
        }

        public void setDomain(String domain) {
            this.set("domain", domain);
        }

        @JsonProperty("dns")
        public String getDns() {
            return this.getString("dns");
        }

        public void setDns(String dns) {
            this.set("dns", dns);
        }

        @JsonProperty("modified_by")
        public Long getModifiedBy() {
            return this.getLong("modified_by");
        }

        public void setModifiedBy(Long modifiedBy) {
            this.set("modified_by", modifiedBy);
        }

        @JsonProperty("modified_date")
        public String getModifiedDate() {
            return this.getString("modified_date");
        }

        public void setModifiedDate(String modifiedDate) {
            this.set("modified_date", modifiedDate);
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.set("name", name);
        }

        public String getName() {
            return this.getString("name");
        }

        @JsonProperty("pod")
        public String getPod() {
            return this.getString("pod");
        }

        public void setPod(String pod) {
            this.set("pod", pod);
        }

        @JsonProperty("refresh_available")
        public String getRefreshAvailable() {
            return this.getString("refresh_available");
        }

        public void setRefreshAvailable(String refreshAvailable) {
            this.set("refresh_available", refreshAvailable);
        }

        @JsonProperty("release")
        public String getRelease() {
            return this.getString("release");
        }

        public void setRelease(String release) {
            this.set("release", release);
        }

        @JsonProperty("source_vault_id")
        public Integer getSourceVaultId() {
            return this.getInteger("source_vault_id");
        }

        public void setSourceVaultId(int sourceVaultId) {
            this.set("source_vault_id", sourceVaultId);
        }

        @JsonProperty("status")
        public String getStatus() {
            return this.getString("status");
        }

        public void setStatus(String status) {
            this.set("status", status);
        }

        @JsonProperty("type")
        public String getType() {
            return this.getString("type");
        }

        public void setType(String type) {
            this.set("type", type);
        }

        @JsonProperty("vault_id")
        public Integer getVaultId() {
            return this.getInteger("vault_id");
        }

        public void setVaultId(int vaultId) {
            this.set("vault_id", vaultId);
        }

        @JsonProperty("size")
        public String getSize() {
            return this.getString("size");
        }

        public void setSize(String size) {
            this.set("size", size);
        }

        @JsonProperty("entitlements")
        public List<Entitlement> getEntitlements() {
            return (List<Entitlement>) this.get("entitlements");
        }

        public void setEntitlements(List<Entitlement> entitlements) {
            this.set("entitlements", entitlements);
        }
    }

    public static class Entitlement extends VaultModel {

        @JsonProperty("size")
        public String getSize() {
            return this.getString("size");
        }

        public void setSize(String size) {
            this.set("size", size);
        }

        @JsonProperty("available")
        public String getAvailable() {
            return this.getString("available");
        }

        public void setAvailable(String available) {
            this.set("available", available);
        }

        @JsonProperty("allowed")
        public String getAllowed() {
            return this.getString("allowed");
        }

        public void setAllowed(String allowed) {
            this.set("allowed", allowed);
        }

        @JsonProperty("temporary")
        public String getTemporary() {
            return this.getString("temporary");
        }

        public void setTemporary(String temporary) {
            this.set("temporary", temporary);
        }
    }

    public static class Limits extends VaultModel {

        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("used")
        public String getUsed() {
            return this.getString("used");
        }

        public void setUsed(String used) {
            this.set("used", used);
        }

        @JsonProperty("allowed")
        public String getAllowed() {
            return this.getString("allowed");
        }

        public void setAllowed(String allowed) {
            this.set("allowed", allowed);
        }

    }
}


