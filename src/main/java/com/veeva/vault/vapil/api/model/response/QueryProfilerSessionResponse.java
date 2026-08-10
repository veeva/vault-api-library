/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.QueryProfilerSession;

public class QueryProfilerSessionResponse extends VaultResponse {

	@JsonProperty("data")
	public QueryProfilerSession getData() {
		return (QueryProfilerSession) this.get("data");
	}

	public void setData(QueryProfilerSession data) {
		this.set("data", data);
	}

}
