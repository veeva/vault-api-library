package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class OutboundPackageDependenciesResponse extends VaultResponse{

    @JsonProperty("responseDetails")
    public ResponseDetails getResponseDetails() {return (ResponseDetails) this.get("responseDetails");}

    public void setResponseDetails(ResponseDetails responseDetails) {this.set("responseDetails", responseDetails);}

    @JsonProperty("package_dependencies")
    public List<PackageDependency> getPackageDependencies() { return (List<PackageDependency>) this.get("package_dependencies");}

    public void setPackageDependencies(List<PackageDependency> packageDependencies) { this.set("package_dependencies", packageDependencies);}

    public static class ResponseDetails extends VaultModel {

        @JsonProperty("total_dependencies")
        public Integer getTotalDependencies() { return this.getInteger("total_dependencies"); }

        public void setTotalDependencies(Integer totalDependencies) { this.set("total_dependencies", totalDependencies);}

        @JsonProperty("target_vault_id")
        public String getTargetVaultId() { return this.getString("target_vault_id");}

        public void setTargetVaultId(String targetVaultId) { this.set("target_vault_id", targetVaultId);}

        @JsonProperty("package_name")
        public String getPackageName() { return this.getString("package_name"); }

        public void setPackageName(String packageName) { this.set("package_name", packageName); }

        @JsonProperty("package_id")
        public String getPackageId() { return this.getString("package_id"); }

        public void setPackageId(String packageId) { this.set("package_id", packageId); }

        @JsonProperty("package_summary")
        public String getPackageSummary() { return this.getString("package_summary"); }

        public void setPackageSummary(String packageSummary) { this.set("package_summary", packageSummary);}

        @JsonProperty("package_description")
        public String getPackageDescription() { return this.getString("package_description"); }

        public void setPackageDescription(String packageDescription) { this.set("package_description", packageDescription); }

        @JsonProperty("url")
        public String getUrl() { return this.getString("url"); }

        public void setUrl(String url) { this.set("url", url); }
    }

    public static class PackageDependency extends VaultModel {

        @JsonProperty("id")
        public String getId() { return this.getString("id"); }

        public void setId(String id) { this.set("id", id); }

        @JsonProperty("name__v")
        public String getName() { return this.getString("name__v"); }

        public void setName(String name) { this.set("name__v", name); }

        @JsonProperty("component_name__v")
        public String getComponentName() { return this.getString("component_name__v"); }

        public void setComponentName(String componentName) { this.set("component_name__v", componentName); }

        @JsonProperty("component_type__v")
        public String getComponentType() { return this.getString("component_type__v"); }

        public void setComponentType(String componentType) { this.set("component_type__v", componentType); }

        @JsonProperty("referenced_component_name")
        public String getReferencedComponentName() { return this.getString("referenced_component_name"); }

        public void setReferencedComponentName(String referencedComponentName) { this.set("referenced_component_name", referencedComponentName); }

        @JsonProperty("referenced_component_type")
        public String getReferencedComponentType() { return this.getString("referenced_component_type"); }

        public void setReferencedComponentType(String referencedComponentType) { this.set("referenced_component_type", referencedComponentType); }
    }
}
