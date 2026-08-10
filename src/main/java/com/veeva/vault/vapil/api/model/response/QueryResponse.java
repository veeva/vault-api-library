/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectField;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for VQL query API calls
 */
public class QueryResponse extends VaultResponse {

	@JsonProperty("data")
	public List<QueryResult> getData() {
		return (List<QueryResult>) this.get("data");
	}

	public void setData(List<QueryResult> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public boolean isPaginated() {
		if (getResponseDetails() != null) {
			if (getResponseDetails().getPreviousPage() != null || getResponseDetails().getNextPage() != null) {
				return true;
			}

			if (getResponseDetails().getSize() != getResponseDetails().getTotal()) {
				return true;
			}
		}
		return false;
	}

	@JsonProperty("queryDescribe")
	public QueryDescribe getQueryDescribe() {
		return (QueryDescribe) this.get("queryDescribe");
	}

	public void setQueryDescribe(QueryDescribe queryDescribe) {
		this.set("queryDescribe", queryDescribe);
	}

	@JsonProperty("document_properties")
	public List<DocumentProperty> getDocumentProperties() {
		return (List<DocumentProperty>) this.get("document_properties");
	}

	public void setDocumentProperties(List<DocumentProperty> documentProperties) {
		this.set("document_properties", documentProperties);
	}

	@JsonProperty("record_properties")
	public List<RecordProperty> getRecordProperties() {
		return (List<RecordProperty>) this.get("record_properties");
	}

	public void setRecordProperties(List<RecordProperty> recordProperties) {
		this.set("record_properties", recordProperties);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class QueryDescribe extends VaultModel {
		@JsonProperty("object")
		public QueryObject getQueryObject() {
			return (QueryObject) this.get("object");
		}

		public void setQueryObject(QueryObject object) {
			this.set("object", object);
		}

		public static class QueryObject extends VaultModel {

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			public void setName(String name) {
				this.set("name", name);
			}

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("label_plural")
			public String getLabelPlural() {
				return this.getString("label_plural");
			}

			public void setLabelPlural(String labelPlural) {
				this.set("label_plural", labelPlural);
			}
		}


		@JsonProperty("fields")
		public List<VaultObjectField> getFields() {
			return (List<VaultObjectField>) this.get("fields");
		}

		public void setFields(List<VaultObjectField> fields) {
			this.set("fields", fields);
		}
	}

	public static class QueryResult extends VaultModel {
		@JsonIgnore
		public QueryResponse getSubQuery(String fieldName) {
			try {
				if (this.get(fieldName) != null) {
					String json = new ObjectMapper().writeValueAsString(this.get(fieldName));
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					return mapper.readValue(json, QueryResponse.class);
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static class DocumentProperty extends VaultModel {

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("version_id")
		public String getVersionId() {
			return this.getString("version_id");
		}

		public void setVersionId(String versionId) {
			this.set("version_id", versionId);
		}

		@JsonProperty("field_properties")
		public FieldProperties getFieldProperties() {
			return (FieldProperties) this.get("field_properties");
		}

		public void setFieldProperties(FieldProperties fieldProperties) {
			this.set("field_properties", fieldProperties);
		}

		@JsonProperty("permissions")
		public DocumentPermissions getPermissions() {
			return (DocumentPermissions) this.get("permissions");
		}

		public void setPermissions(DocumentPermissions permissions) {
			this.set("permissions", permissions);
		}

		public static class FieldProperties extends VaultModel {

			@JsonProperty("edit")
			public List<String> getEdit() {
				return (List<String>) this.get("edit");
			}

			public void setEdit(List<String> edit) {
				this.set("edit", edit);
			}

			@JsonProperty("read-only")
			public List<String> getReadOnly() {
				return (List<String>) this.get("read-only");
			}

			public void setReadOnly(List<String> readOnly) {
				this.set("read-only", readOnly);
			}
		}

		public static class DocumentPermissions extends VaultModel {

			@JsonProperty("annotate")
			public Boolean getAnnotate() {
				return this.getBoolean("annotate");
			}

			public void setAnnotate(Boolean annotate) {
				this.set("annotate", annotate);
			}

			@JsonProperty("change_coordinator")
			public Boolean getChangeCoordinator() {
				return this.getBoolean("change_coordinator");
			}

			public void setChangeCoordinator(Boolean changeCoordinator) {
				this.set("change_coordinator", changeCoordinator);
			}

			@JsonProperty("change_owner")
			public Boolean getChangeOwner() {
				return this.getBoolean("change_owner");
			}

			public void setChangeOwner(Boolean changeOwner) {
				this.set("change_owner", changeOwner);
			}

			@JsonProperty("create_anchors")
			public Boolean getCreateAnchors() {
				return this.getBoolean("create_anchors");
			}

			public void setCreateAnchors(Boolean createAnchors) {
				this.set("create_anchors", createAnchors);
			}

			@JsonProperty("delete")
			public Boolean getDelete() {
				return this.getBoolean("delete");
			}

			public void setDelete(Boolean delete) {
				this.set("delete", delete);
			}

			@JsonProperty("download_source")
			public Boolean getDownloadSource() {
				return this.getBoolean("download_source");
			}

			public void setDownloadSource(Boolean downloadSource) {
				this.set("download_source", downloadSource);
			}

			@JsonProperty("edit_document")
			public Boolean getEditDocument() {
				return this.getBoolean("edit_document");
			}

			public void setEditDocument(Boolean editDocument) {
				this.set("edit_document", editDocument);
			}

			@JsonProperty("edit_fields")
			public Boolean getEditFields() {
				return this.getBoolean("edit_fields");
			}

			public void setEditFields(Boolean editFields) {
				this.set("edit_fields", editFields);
			}

			@JsonProperty("edit_relationships")
			public Boolean getEditRelationships() {
				return this.getBoolean("edit_relationships");
			}

			public void setEditRelationships(Boolean editRelationships) {
				this.set("edit_relationships", editRelationships);
			}

			@JsonProperty("edit_sharing_settings")
			public Boolean getEditSharingSettings() {
				return this.getBoolean("edit_sharing_settings");
			}

			public void setEditSharingSettings(Boolean editSharingSettings) {
				this.set("edit_sharing_settings", editSharingSettings);
			}

			@JsonProperty("manage_viewable_rendition")
			public Boolean getManageViewableRendition() {
				return this.getBoolean("manage_viewable_rendition");
			}

			public void setManageViewableRendition(Boolean manageViewableRendition) {
				this.set("manage_viewable_rendition", manageViewableRendition);
			}

			@JsonProperty("multi_channel_actions")
			public Boolean getMultiChannelActions() {
				return this.getBoolean("multi_channel_actions");
			}

			public void setMultiChannelActions(Boolean multiChannelActions) {
				this.set("multi_channel_actions", multiChannelActions);
			}

			@JsonProperty("reclassify")
			public Boolean getReclassify() {
				return this.getBoolean("reclassify");
			}

			public void setReclassify(Boolean reclassify) {
				this.set("reclassify", reclassify);
			}

			@JsonProperty("version")
			public Boolean getVersion() {
				return this.getBoolean("version");
			}

			public void setVersion(Boolean version) {
				this.set("version", version);
			}

			@JsonProperty("view_content")
			public Boolean getViewContent() {
				return this.getBoolean("view_content");
			}

			public void setViewContent(Boolean viewContent) {
				this.set("view_content", viewContent);
			}

			@JsonProperty("view_document")
			public Boolean getViewDocument() {
				return this.getBoolean("view_document");
			}

			public void setViewDocument(Boolean viewDocument) {
				this.set("view_document", viewDocument);
			}
		}
	}

	public static class RecordProperty extends VaultModel {

		@JsonProperty("field_additional_data")
		public FieldAdditionalData getFieldAdditionalData() {
			return (FieldAdditionalData)this.get("field_additional_data");
		}

		public void setFieldAdditionalData(FieldAdditionalData fieldAdditionalData) {
			this.set("field_additional_data", fieldAdditionalData);
		}

		@JsonProperty("field_properties")
		public Map<String, List<String>> getFieldProperties() {
			return (Map<String, List<String>>)this.get("field_properties");
		}

		public void setFieldProperties(Map<String, List<String>> fieldProperties) {
			this.set("field_properties", fieldProperties);
		}

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		public static class FieldAdditionalData extends VaultModel {
		}
	}

	public class ResponseDetails extends VaultModel {

		@JsonProperty("next_page")
		public String getNextPage() {
			return this.getString("next_page");
		}

		public void setNextPage(String nextPage) {
			this.set("next_page", nextPage);
		}

		@JsonProperty("pageoffset")
		public Integer getPageOffset() {
			return this.getInteger("pageoffset");
		}

		public void setPageOffset(Integer pageOffset) {
			this.set("pageoffset", pageOffset);
		}

		@JsonProperty("pagesize")
		public Integer getPageSize() {
			return this.getInteger("pagesize");
		}

		public void setPageSize(Integer pageSize) {
			this.set("pagesize", pageSize);
		}

		@JsonProperty("previous_page")
		public String getPreviousPage() {
			return this.getString("previous_page");
		}

		public void setPreviousPage(String previousPage) {
			this.set("previous_page", previousPage);
		}

		@JsonProperty("size")
		public Integer getSize() {
			return this.getInteger("size");
		}

		public void setSize(Integer size) {
			this.set("size", size);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		public void setTotal(Integer total) {
			this.set("total", total);
		}

		/**
		 * Determine if a next page exists for pagination
		 *
		 * @return true if a next page exists
		 */
		@JsonIgnore
		public boolean hasNextPage() {
			return getNextPage() != null && !getNextPage().isEmpty();
		}

		/**
		 * Determine if a previous page exists for pagination
		 *
		 * @return true if a previous page exists
		 */
		@JsonIgnore
		public boolean hasPreviousPage() {
			return getPreviousPage() != null && !getPreviousPage().isEmpty();
		}
	}
}