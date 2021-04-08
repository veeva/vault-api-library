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

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/objects/documents/{doc_id}/lock
 */
public class DocumentLockResponse extends VaultResponse {

	@JsonProperty("lock")
	public Lock getLock() {
		return (Lock) this.get("lock");
	}

	public void setLock(Lock lock) {
		this.set("lock", lock);
	}

	public static class Lock extends VaultModel {

		@JsonProperty("locked_by__v")
		public Integer getLockedBy() {
			return this.getInteger("locked_by__v");
		}

		public void setLockedBy(Integer lockedBy) {
			this.set("locked_by__v", lockedBy);
		}

		@JsonProperty("locked_date__v")
		public String getLockedDate() {
			return this.getString("locked_date__v");
		}

		public void setLockedDate(String lockedDate) {
			this.set("locked_date__v", lockedDate);
		}
	}
}