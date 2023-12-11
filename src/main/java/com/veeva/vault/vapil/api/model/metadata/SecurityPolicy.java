package com.veeva.vault.vapil.api.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SecurityPolicy extends VaultModel {

    @JsonProperty("name")
    public String getName() {return (String) this.get("name");}

    public void setName(String name) {this.set("name", name);}

    @JsonProperty("description")
    public String getDescription() {return (String) this.get("description");}

    public void setDescription(String description) {this.set("description", description);}

    @JsonProperty("properties")
    public List<Property> getProperties() {return (List<Property>) this.get("properties");}

    public void setProperties(List<Property> properties) {this.set("properties", properties);}

    @JsonProperty("objects")
    public List<Object> getObjects() {return (List<Object>) this.get("objects");}

    public void setObjects(List<Object> objects) {this.set("objects", objects);}

    public static class Property extends VaultModel {

        @JsonProperty("name")
        public String getName() {return (String) this.get("name");}

        public void setName(String name) {this.set("name", name);}

        @JsonProperty("type")
        public List<String> getType() {return (List<String>) this.get("type");}

        public void setType(List<String> type) {this.set("type", type);}

        @JsonProperty("description")
        public String getDescription() {return (String) this.get("description");}

        public void setDescription(String description) {this.set("description", description);}

        @JsonProperty("objectTypeReferenced")
        public List<ObjectTypeReference> getObjectTypeReferenced() {return (List<ObjectTypeReference>) this.get("objectTypeReferenced");}

        public void setObjectTypeReferenced(List<ObjectTypeReference> objectTypeReferenced) {this.set("objectTypeReferenced", objectTypeReferenced);}

        public static class ObjectTypeReference extends VaultModel{
            @JsonProperty("type")
            public String getType() {return (String) this.get("type");}

            public void setType(String type) {this.set("type", type);}
        }
    }

    public static class Object extends VaultModel {
        @JsonProperty("name")
        public String getName() {return (String) this.get("name");}

        public void setName(String name) {this.set("name", name);}

        @JsonProperty("description")
        public String getDescription() {return (String) this.get("description");}

        public void setDescription(String description) {this.set("description", description);}

        @JsonProperty("properties")
        public List<Property> getProperties() {return (List<Property>) this.get("properties");}

        public void setProperties(List<Property> properties) {this.set("properties", properties);}

        public static class Property extends VaultModel {
            @JsonProperty("name")
            public String getName() {return (String) this.get("name");}

            public void setName(String name) {this.set("name", name);}

            @JsonProperty("type")
            public List<String> getType() {return (List<String>) this.get("type");}

            public void setType(List<String> type) {this.set("type", type);}

            @JsonProperty("description")
            public String getDescription() {return (String) this.get("description");}

            public void setDescription(String description) {this.set("description", description);}

            @JsonProperty("required")
            public Boolean getRequired() {return this.getBoolean("required");}

            public void setRequired(Boolean required) {this.set("required", required);}

            @JsonProperty("maxValue")
            public Long getMaxValue() {return this.getLong("maxValue");}

            public void setMaxValue(Long maxValue) {this.set("maxValue", maxValue);}

            @JsonProperty("minValue")
            public Long getMinValue() {return this.getLong("minValue");}

            public void setMinValue(Long minValue) {this.set("minValue", minValue);}

            @JsonProperty("editable")
            public Boolean getEditable() {return this.getBoolean("editable");}

            public void setEditable(Boolean editable) {this.set("editable", editable);}

            @JsonProperty("onCreateEditable")
            public Boolean getOnCreateEditable() {return getBoolean("onCreateEditable");}

            public void setOnCreateEditable(Boolean onCreateEditable) {this.set("onCreateEditable", onCreateEditable);}
        }
    }
}
