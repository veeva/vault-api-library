/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the document annotation response
 */
public class DocumentAnnotationResponse extends VaultResponse {

	@JsonProperty("anchorDataList")
	public List<AnchorData> getAnchorDataList() {
		return (List<AnchorData>) this.get("anchorDataList");
	}

	@JsonProperty("anchorDataList")
	public void setAnchorDataList(List<AnchorData> anchorDataList) {
		this.set("anchorDataList", anchorDataList);
	}

	@JsonProperty("failures")
	public Integer getFailures() {
		return this.getInteger("failures");
	}

	@JsonProperty("failures")
	public void setFailures(Integer failures) {
		this.set("failures", failures);
	}

	@JsonProperty("new")
	public Integer getNewCount() {
		return this.getInteger("new");
	}

	@JsonProperty("new")
	public void setNewCount(Integer newCount) {
		this.set("new", newCount);
	}

	@JsonProperty("replies")
	public Integer getReplies() {
		return this.getInteger("replies");
	}

	@JsonProperty("replies")
	public void setReplies(Integer replies) {
		this.set("replies", replies);
	}

	public static class AnchorData extends VaultModel {

		@JsonProperty("anchorId")
		public Integer getAnchorId() {
			return this.getInteger("anchorId");
		}

		@JsonProperty("anchorId")
		public void setAnchorId(Integer anchorId) {
			this.set("anchorId", anchorId);
		}

		@JsonProperty("anchorName")
		public String getAnchorName() {
			return this.getString("anchorName");
		}

		@JsonProperty("anchorName")
		public void setAnchorName(String anchorName) {
			this.set("anchorName", anchorName);
		}

		@JsonProperty("noteId")
		public Long getNoteId() {
			return this.getLong("noteId");
		}

		@JsonProperty("noteId")
		public void setNoteId(Long noteId) {
			this.set("noteId", noteId);
		}

		@JsonProperty("noteAuthor")
		public String getNoteAuthor() {
			return this.getString("noteAuthor");
		}

		@JsonProperty("noteAuthor")
		public void setNoteAuthor(String noteAuthor) {
			this.set("noteAuthor", noteAuthor);
		}

		@JsonProperty("noteTimestamp")
		public String getNoteTimestamp() {
			return this.getString("noteTimestamp");
		}

		@JsonProperty("noteTimestamp")
		public void setNoteTimestamp(String noteTimestamp) {
			this.set("noteTimestamp", noteTimestamp);
		}

		@JsonProperty("pageNumber")
		public Integer getPageNumber() {
			return this.getInteger("pageNumber");
		}

		@JsonProperty("pageNumber")
		public void setPageNumber(Integer pageNumber) {
			this.set("pageNumber", pageNumber);
		}
	}
}