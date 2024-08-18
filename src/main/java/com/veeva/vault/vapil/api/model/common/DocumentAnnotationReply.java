/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for Document Annotation Reply Read Data
 */
public class DocumentAnnotationReply extends VaultModel {
	@JsonProperty("document_version_id__sys")
	public String getDocumentVersionId() {
		return this.getString("document_version_id__sys");
	}

	public void setDocumentVersionId(String documentVersionId) {
		this.set("document_version_id__sys", documentVersionId);
	}

	@JsonProperty("modified_by_user__sys")
	public Integer getModifiedByUser() {
		return this.getInteger("modified_by_user__sys");
	}

	public void setModifiedByUser(Integer modifiedByUser) {
		this.set("modified_by_user__sys", modifiedByUser);
	}

	@JsonProperty("id__sys")
	public Integer getId() {
		return this.getInteger("id__sys");
	}

	public void setId(Integer id) {
		this.set("id__sys", id);
	}

	@JsonProperty("type__sys")
	public String getType() {
		return this.getString("type__sys");
	}

	public void setType(String type) {
		this.set("type__sys", type);
	}

	@JsonProperty("tag_names__sys")
	public List<String> getTagNames() {
		return (List<String>) this.get("tag_names__sys");
	}

	public void setTagNames(List<String> tagNames) {
		this.set("tag_names__sys", tagNames);
	}

	@JsonProperty("state__sys")
	public String getState() {
		return this.getString("state__sys");
	}

	public void setState(String state) {
		this.set("state__sys", state);
	}

	@JsonProperty("created_by_user__sys")
	public Integer getCreatedByUser() {
		return this.getInteger("created_by_user__sys");
	}

	public void setCreatedByUser(Integer createdByUser) {
		this.set("created_by_user__sys", createdByUser);
	}

	@JsonProperty("comment__sys")
	public String getComment() {
		return this.getString("comment__sys");
	}

	public void setComment(String comment) {
		this.set("comment__sys", comment);
	}

	@JsonProperty("created_date_time__sys")
	public String getCreatedDateTime() {
		return this.getString("created_date_time__sys");
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.set("created_date_time__sys", createdDateTime);
	}

	@JsonProperty("modified_date_time__sys")
	public String getModifiedDateTime() {
		return this.getString("modified_date_time__sys");
	}

	public void setModifiedDateTime(String modifiedDateTime) {
		this.set("modified_date_time__sys", modifiedDateTime);
	}

	@JsonProperty("color__sys")
	public String getColor() {
		return this.getString("color__sys");
	}

	public void setColor(String color) {
		this.set("color__sys", color);
	}

	@JsonProperty("placemark")
	public Placemark getPlacemark() {
		return (Placemark) this.get("placemark");
	}

	public void setPlacemark(Placemark placemark) {
		this.set("placemark", placemark);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("offset")
		public Integer getOffset() {
			return this.getInteger("offset");
		}

		public void setOffset(Integer offset) {
			this.set("offset", offset);
		}

		@JsonProperty("limit")
		public Integer getLimit() {
			return this.getInteger("limit");
		}

		public void setLimit(Integer limit) {
			this.set("limit", limit);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		public void setTotal(Integer total) {
			this.set("total", total);
		}

		@JsonProperty("size")
		public Integer getSize() {
			return this.getInteger("size");
		}

		public void setSize(Integer size) {
			this.set("size", size);
		}

		@JsonProperty("next_page")
		public String getNextPage() {
			return this.getString("next_page");
		}

		public void setNextPage(String nextPage) {
			this.set("next_page", nextPage);
		}

		@JsonProperty("previous_page")
		public String getPreviousPage() {
			return this.getString("previous_page");
		}

		public void setPreviousPage(String previousPage) {
			this.set("previous_page", previousPage);
		}
	}

	public static class Placemark extends VaultModel {

		@JsonProperty("reply_position_index__sys")
		public Integer getReplyPositionIndex() {
			return this.getInteger("reply_position_index__sys");
		}

		public void setReplyPositionIndex(Integer replyPositionIndex) {
			this.set("reply_position_index__sys", replyPositionIndex);
		}

		@JsonProperty("type__sys")
		public String getType() {
			return this.getString("type__sys");
		}

		public void setType(String type) {
			this.set("type__sys", type);
		}

		@JsonProperty("reply_parent__sys")
		public Integer getReplyParent() {
			return this.getInteger("reply_parent__sys");
		}

		public void setReplyParent(Integer replyParent) {
			this.set("reply_parent__sys", replyParent);
		}
	}

}
