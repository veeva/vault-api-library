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