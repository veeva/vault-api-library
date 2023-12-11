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
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.metadata.SecurityPolicy;
import com.veeva.vault.vapil.api.model.metadata.User;

import java.util.List;

public class MetaDataSecurityPolicyResponse extends VaultResponse {

	@JsonProperty("metadata")
	public SecurityPolicy getMetadata() {return (SecurityPolicy) this.get("metadata");}

	public void setMetadata(SecurityPolicy securityPolicy) {this.set("metadata", securityPolicy);}


}
