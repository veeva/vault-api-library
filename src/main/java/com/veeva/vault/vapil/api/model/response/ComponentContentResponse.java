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
 * Response model for the Component Content Files
 * <p>
 * POST /api/mdl/files
 * POST /api/mdl/components/{component_type}.{record_name}/files
 */
public class ComponentContentResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ComponentContent> getData() {
		return (List<ComponentContent>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<ComponentContent> data) {
		this.set("data", data);
	}

	@JsonProperty("links")
	public List<Link> getLinks() {
		return (List<Link>) this.get("links");
	}

	@JsonProperty("links")
	public void setLinks(List<Link> links) {
		this.set("links", links);
	}

	public static class ComponentContent extends VaultModel {
		@JsonProperty("format__v")
		public String getFormat() {
			return this.getString("format__v");
		}

		@JsonProperty("format__v")
		public void setFormat(String format) {
			this.set("format__v", format);
		}

		@JsonProperty("name__v")
		public String getName() {
			return this.getString("name__v");
		}

		@JsonProperty("name__v")
		public void setName(String name) {
			this.set("name__v", name);
		}

		@JsonProperty("original_name__v")
		public String getOriginalName() {
			return this.getString("original_name__v");
		}

		@JsonProperty("original_name__v")
		public void setOriginalName(String originalName) {
			this.set("original_name__v", originalName);
		}

		@JsonProperty("sha1_checksum__v")
		public String getSha1Checksum() {
			return this.getString("sha1_checksum__v");
		}

		@JsonProperty("sha1_checksum__v")
		public void setSha1Checksum(String sha1Checksum) {
			this.set("sha1_checksum__v", sha1Checksum);
		}

		@JsonProperty("size__v")
		public Integer getSize() {
			return this.getInteger("size__v");
		}

		@JsonProperty("size__v")
		public void setSize(Integer size) {
			this.set("size__v", size);
		}
	}

	public static class Link extends VaultModel {
		@JsonProperty("accept")
		public String getAccept() {
			return this.getString("accept");
		}

		@JsonProperty("accept")
		public void setAccept(String accept) {
			this.set("accept", accept);
		}

		@JsonProperty("href")
		public String getHref() {
			return this.getString("href");
		}

		@JsonProperty("href")
		public void setHref(String href) {
			this.set("href", href);
		}

		@JsonProperty("method")
		public String getMethod() {
			return this.getString("method");
		}

		@JsonProperty("method")
		public void setMethod(String method) {
			this.set("method", method);
		}

		@JsonProperty("rel")
		public String getRel() {
			return this.getString("rel");
		}

		@JsonProperty("rel")
		public void setRel(String rel) {
			this.set("rel", rel);
		}
	}

}