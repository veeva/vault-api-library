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

import java.math.BigDecimal;
import java.util.List;

/**
 * Model for Document Annotation Data
 */
public class DocumentAnnotation extends VaultModel {
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

	@JsonProperty("title__sys")
	public String getTitle() {
		return this.getString("title__sys");
	}

	public void setTitle(String title) {
		this.set("title__sys", title);
	}

	@JsonProperty("type__sys")
	public String getType() {
		return this.getString("type__sys");
	}

	public void setType(String type) {
		this.set("type__sys", type);
	}

	@JsonProperty("state__sys")
	public String getState() {
		return this.getString("state__sys");
	}

	public void setState(String state) {
		this.set("state__sys", state);
	}

	@JsonProperty("reply_count__sys")
	public Integer getReplyCount() {
		return this.getInteger("reply_count__sys");
	}

	public void setReplyCount(Integer replyCount) {
		this.set("reply_count__sys", replyCount);
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
		@JsonProperty("y_coordinate__sys")
		public BigDecimal getYCoordinate() {
			return this.getBigDecimal("y_coordinate__sys");
		}

		public void setYCoordinate(BigDecimal yCoordinate) {
			this.set("y_coordinate__sys", yCoordinate);
		}

		@JsonProperty("x_coordinate__sys")
		public BigDecimal getXCoordinate() {
			return this.getBigDecimal("x_coordinate__sys");
		}

		public void setXCoordinate(BigDecimal xCoordinate) {
			this.set("x_coordinate__sys", xCoordinate);
		}

		@JsonProperty("coordinates__sys")
		public List<BigDecimal> getCoordinates() {
			return (List<BigDecimal>) this.get("coordinates__sys");
		}

		public void setCoordinates(List<BigDecimal> coordinates) {
			this.set("coordinates__sys", coordinates);
		}

		@JsonProperty("text_start_index__sys")
		public Integer getTextStartIndex() {
			return this.getInteger("text_start_index__sys");
		}

		public void setTextStartIndex(Integer textStartIndex) {
			this.set("text_start_index__sys", textStartIndex);
		}

		@JsonProperty("text_end_index__sys")
		public Integer getTextEndIndex() {
			return this.getInteger("text_end_index__sys");
		}

		public void setTextEndIndex(Integer textEndIndex) {
			this.set("text_end_index__sys", textEndIndex);
		}

		@JsonProperty("type__sys")
		public String getType() {
			return this.getString("type__sys");
		}

		public void setType(String type) {
			this.set("type__sys", type);
		}

		@JsonProperty("height__sys")
		public BigDecimal getHeight() {
			return this.getBigDecimal("height__sys");
		}

		public void setHeight(BigDecimal height) {
			this.set("height__sys", height);
		}

		@JsonProperty("width__sys")
		public BigDecimal getWidth() {
			return this.getBigDecimal("width__sys");
		}

		public void setWidth(BigDecimal width) {
			this.set("width__sys", width);
		}

		@JsonProperty("page_number__sys")
		public Integer getPageNumber() {
			return this.getInteger("page_number__sys");
		}

		public void setPageNumber(Integer pageNumber) {
			this.set("page_number__sys", pageNumber);
		}

		@JsonProperty("style__sys")
		public String getStyle() {
			return this.getString("style__sys");
		}

		public void setStyle(String style) {
			this.set("style__sys", style);
		}
	}

}
