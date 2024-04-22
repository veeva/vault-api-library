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
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/vobjects/merges/{job_id}/results
 */
public class ObjectRecordMergeResultsResponse extends VaultResponse {

	@JsonProperty("data")
	public Data getData() {
		return (Data) this.get("data");
	}

	public void setData(Data data) {
		this.set("data", data);
	}

	public static class Data extends VaultModel {

		@JsonProperty("merge_sets")
		public List<MergeSet> getMergeSets() {
			return (List<MergeSet>) this.get("merge_sets");
		}

		public void setMergeSets(List<MergeSet> mergeSets) {
			this.set("merge_sets", mergeSets);
		}

		public static class MergeSet extends VaultModel {
			@JsonProperty("duplicate_record_id")
			public String getDuplicateRecordId() {
				return this.getString("duplicate_record_id");
			}

			public void setDuplicateRecordId(String duplicateRecordId) {
				this.set("duplicate_record_id", duplicateRecordId);
			}

			@JsonProperty("main_record_id")
			public String getMainRecordId() {
				return this.getString("main_record_id");
			}

			public void setMainRecordId(String mainRecordId) {
				this.set("main_record_id", mainRecordId);
			}

			@JsonProperty("status")
			public String getStatus() {
				return this.getString("status");
			}

			public void setStatus(String status) {
				this.set("status", status);
			}

			@JsonProperty("error")
			public Error getError() {
				return (Error) this.get("error");
			}

			public void setError(Error error) {
				this.set("error", error);
			}

			public static class Error extends VaultModel {
				
				@JsonProperty("type")
				public String getType() {
					return this.getString("type");
				}

				public void setType(String type) {
					this.set("type", type);
				}

				@JsonProperty("message")
				public String getMessage() {
					return this.getString("message");
				}

				public void setMessage(String message) {
					this.set("message", message);
				}
			}
		}
	}
}