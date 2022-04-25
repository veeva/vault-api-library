package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.PackageCode;
import com.veeva.vault.vapil.api.model.common.PackageLog;
import com.veeva.vault.vapil.api.model.common.PackageStep;

import java.util.List;

public class PackageImportResultsResponse extends VaultResponse {

    @JsonProperty("vaultPackage")
    public VaultImportPackage getVaultPackage() { return (VaultImportPackage) this.get("vaultPackage"); }

    public void setVaultPackage(VaultImportPackage vaultImportPackage) { this.set("vaultPackage", vaultImportPackage); }

    public class VaultImportPackage extends VaultModel {

        @JsonProperty("id")
        public String getId() { return this.getString("id"); }

        public void setId(String id) { this.set("id", id); }

        @JsonProperty("name")
        public String getName() { return this.getString("name"); }

        public void setName(String name) { this.set("name", name); }

        @JsonProperty("status")
        public String getStatus() { return this.getString("status"); }

        public void setStatus(String status) { this.set("status", status); }

        @JsonProperty("package_status")
        public String getPackageStatus() { return this.getString("package_status"); }

        public void setPackageStatus(String package_status) { this.set("package_status", package_status); }

        @JsonProperty("log")
        public List<PackageLog> getLog() { return (List<PackageLog>) this.get("log"); }

        public void setLog(List<PackageLog> log) { this.set("log", log); }

        @JsonProperty("package_steps")
        public List<PackageStep> getPackageSteps() { return (List<PackageStep>) this.get("package_steps"); }

        public void setPackageSteps(List<PackageStep> packageSteps) { this.set("package_steps", packageSteps); }

        @JsonProperty("package_code")
        public List<PackageCode> getPackageCode() { return (List<PackageCode>) this.get("package_code"); }

        public void setPackageCode(List<PackageCode> packageCode) { this.set("package_code", packageCode); }


    }

}
