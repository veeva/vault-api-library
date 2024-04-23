package com.veeva.vault.vapil.api.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class VaultObjectPageLayout extends VaultModel {

    @JsonProperty("name")
    public String getName() { return this.getString("name"); }

    public void setName(String name) { this.set("name", name);}

    @JsonProperty("label")
    public String getLabel() { return this.getString("label"); }

    public void setLabel(String label) { this.set("label", label); }

    @JsonProperty("object_type")
    public String getObjectType() { return this.getString("object_type"); }

    public void setObjectType(String objectType) { this.set("object_type", objectType); }

    @JsonProperty("url")
    public String getUrl() { return this.getString("url"); }

    public void setUrl(String url) { this.set("url", url); }

    @JsonProperty("object")
    public String getObject() { return  this.getString("object"); }

    public void setObject(String object) { this.set("object", object); }

    @JsonProperty("created_date")
    public String getCreatedDate() { return this.getString("created_date"); }

    public void setCreatedDate(String createdDate) { this.set("created_date", createdDate); }

    @JsonProperty("last_modified_date")
    public String getLastModifiedDate() { return this.getString("last_modified_date"); }

    public void setLastModifiedDate(String lastModifiedDate) { this.set("last_modified_date", lastModifiedDate); }

    @JsonProperty("layout_rules")
    public List<PageLayoutRule> getLayoutRules() { return (List<PageLayoutRule>) this.get("layout_rules"); }

    public void setLayoutRules(List<PageLayoutRule> layoutRules) {this.set("layout_rules", layoutRules);}

    @JsonProperty("sections")
    public List<PageLayoutSection> getSections() { return (List<PageLayoutSection>) this.get("sections"); }

    public void setSections(List<PageLayoutSection> sections) { this.set("sections", sections);}


    public static class PageLayoutRule extends VaultModel {

        @JsonProperty("evaluation_order")
        public Integer getEvaluationOrder() { return this.getInteger("evaluation_order"); }

        public void setEvaluationOrder(Integer evaluationOrder) { this.set("evaluation_order", evaluationOrder); }

        @JsonProperty("fields_to_hide")
        public List<String> getFieldsToHide() { return this.getListString("fields_to_hide"); }

        public void setFieldsToHide(List<String> fieldsToHide) { this.set("fields_to_hide", fieldsToHide); }

        @JsonProperty("sections_to_hide")
        public List<String> getSectionsToHide() { return this.getListString("sections_to_hide"); }

        public void setSectionsToHide(List<String> sectionsToHide) { this.set("sections_to_hide", sectionsToHide); }

        @JsonProperty("controls_to_hide")
        public List<String> getControlsToHide() { return this.getListString("controls_to_hide"); }

        public void setControlsToHide(List<String> controlsToHide) { this.set("controls_to_hide", controlsToHide); }

        @JsonProperty("expression")
        public String getExpression() { return this.getString("expression"); }

        public void setExpression(String expression) { this.set("expression", expression); }
    }

    public static class PageLayoutSection extends VaultModel {

        @JsonProperty("name")
        public String getName() { return this.getString("name"); }

        public void setName(String name) { this.set("name", name); }

        @JsonProperty("title")
        public String getTitle() { return this.getString("title"); }

        public void setTitle(String title) { this.set("title", title); }

        @JsonProperty("type")
        public String getType() { return this.getString("type"); }

        public void setType(String type) { this.set("type", type); }

        @JsonProperty("help_content")
        public String getHelpContent() { return this.getString("help_content"); }

        public void setHelpContent(String helpContent) { this.set("help_content", helpContent); }

        @JsonProperty("show_in_lifecycle_states")
        public List<String> getShowInLifecycleStates() { return this.getListString("show_in_lifecycle_states"); }

        public void setShowInLifecycleStates(List<String> showInLifecycleStates) { this.set("show_in_lifecycle_states", showInLifecycleStates); }

        @JsonProperty("properties")
        public List<LayoutProperty> getProperties() { return (List<LayoutProperty>) this.get("properties"); }

        public void setProperties(List<LayoutProperty> properties) { this.set("properties", properties); }

        public static class LayoutProperty extends VaultModel {

            @JsonProperty("layout_type")
            public String getLayoutType() { return this.getString("layout_type"); }

            public void setLayoutType(String layoutType) { this.set("layout_type", layoutType); }

            @JsonProperty("items")
            public List<PropertyItem> getItems() { return (List<PropertyItem>) this.get("items"); }

            public void setItems(List<PropertyItem> items) { this.set("items", items); }

            @JsonProperty("relationship")
            public String getRelationship() { return this.getString("relationship"); }

            public void setRelationship(String relationship) { this.set("relationship", relationship); }

            @JsonProperty("related_object")
            public String getRelatedObject() { return this.getString("related_object"); }

            public void setRelatedObject(String relatedObject) { this.set("related_object", relatedObject); }

            @JsonProperty("prevent_record_create")
            public Boolean getPreventRecordCreate() { return this.getBoolean("prevent_record_create"); }

            public void setPreventRecordCreate(Boolean preventRecordCreate) { this.set("prevent_record_create", preventRecordCreate); }

            @JsonProperty("modal_create_record")
            public Boolean getModalCreateRecord() { return this.getBoolean("modal_create_record"); }

            public void setModalCreateRecord(Boolean modalCreateRecord) { this.set("modal_create_record", modalCreateRecord); }

            @JsonProperty("criteria_vql")
            public String getCriteriaVql() { return this.getString("criteria_vql"); }

            public void setCriteriaVql(String criteriaVql) { this.set("criteria_vql", criteriaVql); }

            @JsonProperty("columns")
            public List<LayoutColumn> getColumns() { return (List<LayoutColumn>) this.get("columns"); }

            public void setColumns(List<LayoutColumn> columns) { this.set("columns", columns); }


            public static class PropertyItem extends VaultModel {

                @JsonProperty("type")
                public String getType() { return this.getString("type"); }

                public void setType(String type) { this.set("type", type);}

                @JsonProperty("reference")
                public String getReference() { return this.getString("reference"); }

                public void setReference(String reference) { this.set("reference", reference); }
            }

            public static class LayoutColumn extends VaultModel{

                @JsonProperty("reference")
                public String getReference() { return this.getString("reference"); }

                public void setReference(String reference) { this.set("reference", reference); }

                @JsonProperty("width")
                public String getWidth() { return this.getString("width"); }

                public void setWidth(String width) { this.set("width", width); }

            }
        }

    }



}
