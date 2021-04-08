/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /vobjects/{object_name}/{object_record_id}/actions
 * <br>
 * GET /vobjects/{object_name}/{object_record_id}/actions/{action_name}
 */
public class ObjectRecordActionResponse extends VaultResponse {

	/*
	 *  Both calls return same data but the JSON structure is different
	 *  between the two. This combines the two for a consistent set of
	 *  properties to return:
	 *  - A JSON array of size one is returned
	 *  - A single JSON object is returned
	 *
	 *  Note that no getter is defined to allow "root" level properties
	 *  to wrap around the response data, making it easier for calling
	 *  code.
	 */

	@JsonProperty("data")
	public List<Action> getData() {
		return (List<Action>) this.get("data");
	}

	public void setData(List<Action> data) {
		this.set("data", data);
	}

	public static class Action extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("links")
		public List<Link> getLinks() {
			return (List<Link>) this.get("links");
		}

		public void setLinks(List<Link> links) {
			this.set("links", links);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class Link extends VaultModel {

		@JsonProperty("accept")
		public String getAccept() {
			return this.getString("accept");
		}

		public void setAccept(String accept) {
			this.set("accept", accept);
		}

		@JsonProperty("href")
		public String getHref() {
			return this.getString("href");
		}

		public void setHref(String href) {
			this.set("href", href);
		}

		@JsonProperty("method")
		public String getMethod() {
			return this.getString("method");
		}

		public void setMethod(String method) {
			this.set("method", method);
		}

		@JsonProperty("rel")
		public String getRel() {
			return this.getString("rel");
		}

		public void setRel(String rel) {
			this.set("rel", rel);
		}
	}
}