/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.veeva.vault.vapil.api.request.DocumentLifecycleRequest;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

/**
 * Documents or Binders request type for document requests
 *
 * @see DocumentLifecycleRequest
 * @see DocumentRoleRequest
 */
public enum DocumentRequestType {
	BINDERS("binders"),
	DOCUMENTS("documents");

	private String value;

	DocumentRequestType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
