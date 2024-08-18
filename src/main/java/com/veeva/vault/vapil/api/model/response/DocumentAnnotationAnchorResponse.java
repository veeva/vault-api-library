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

import java.util.List;

/**
 * Model for the document annotation import response
 */
public class DocumentAnnotationAnchorResponse extends VaultResponse {

	@JsonProperty("anchorDataList")
	public List<AnchorData> getAnchorDataList() {
		return (List<AnchorData>) this.get("anchorDataList");
	}

	@JsonProperty("anchorDataList")
	public void setAnchorDataList(List<AnchorData> anchorDataList) {
		this.set("anchorDataList", anchorDataList);
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