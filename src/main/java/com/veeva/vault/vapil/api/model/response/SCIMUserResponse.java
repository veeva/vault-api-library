/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.common.SCIMUser;

public class SCIMUserResponse extends VaultResponse {

	public SCIMUserResponse() {
		//Method that reads the Binary and returns a SCIM User
		//resources =response = sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, MdlResponse.class);
	}

	/**
	 * List of all SCIM Schema data returned
	 * <p>
	 * GET /api/{version}/scim/v2/Schemas
	 * <p>
	 * GET /api/{version}/scim/v2/ResourceTypes
	 * <p>
	 * GET /api/{version}/scim/v2/ServiceProviderConfig
	 *
	 * @return SCIMUser scim user object
	 */
	@JsonIgnore
	public SCIMUser getUser() {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		try {
			return mapper.readValue(this.getResponse(), SCIMUser.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isSuccessful() {
		return getUser() == null;
	}

	@Override
	public String getResponseStatus() {
		if (isSuccessful()) {
			return VaultResponse.HTTP_RESPONSE_SUCCESS;
		} else {
			return VaultResponse.HTTP_RESPONSE_FAILURE;
		}
	}

	@Override
	public String getResponseMessage() {
		return this.toJsonString();
	}
}


