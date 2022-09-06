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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.SCIMAttribute;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

public class SCIMResponse extends VaultResponse {

	@JsonProperty("active")
	public Boolean getActive() {
		return this.getBoolean("active");
	}

	public void setActive(Boolean active) {
		this.set("active", active);
	}

	@JsonProperty("attributes")
	public List<SCIMAttribute> getAttributes() {
		return (List<SCIMAttribute>) this.get("attributes");
	}

	public void setAttributes(List<SCIMAttribute> attributes) {
		this.set("attributes", attributes);
	}

	@JsonProperty("authenticationSchemes")
	public List<AuthenticationScheme> getAuthenticationSchemes() {
		return (List<AuthenticationScheme>) this.get("authenticationSchemes");
	}

	public void setAuthenticationSchemes(List<AuthenticationScheme> authenticationSchemes) {
		this.set("authenticationSchemes", authenticationSchemes);
	}

	@JsonProperty("bulk")
	public Bulk getBulk() {
		return (Bulk) this.get("bulk");
	}

	public void setBulk(Bulk bulk) {
		this.set("bulk", bulk);
	}

	@JsonProperty("changePassword")
	public ChangePassword getChangePassword() {
		return (ChangePassword) this.get("changePassword");
	}

	public void setChangePassword(ChangePassword changePassword) {
		this.set("changePassword", changePassword);
	}

	@JsonProperty("description")
	public String getDescription() {
		return this.getString("description");
	}

	public void setDescription(String description) {
		this.set("description", description);
	}

	@JsonProperty("displayName")
	public String getDisplayName() {
		return this.getString("displayName");
	}

	public void setDisplayName(String displayName) {
		this.set("displayName", displayName);
	}

	@JsonProperty("documentationUri")
	public String getDocumentationUri() {
		return this.getString("documentationUri");
	}

	public void setDocumentationUri(String documentationUri) {
		this.set("documentationUri", documentationUri);
	}

	@JsonProperty("endpoint")
	public String getEndpoint() {
		return this.getString("endpoint");
	}

	public void setEndpoint(String endpoint) {
		this.set("endpoint", endpoint);
	}

	@JsonProperty("etag")
	public Etag getEtag() {
		return (Etag) this.get("etag");
	}

	public void setEtag(Etag etag) {
		this.set("etag", etag);
	}

	@JsonProperty("filter")
	public Filter getFilter() {
		return (Filter) this.get("filter");
	}

	public void setFilter(Filter filter) {
		this.set("filter", filter);
	}

	@JsonProperty("id")
	public String getId() {
		return this.getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("itemsPerPage")
	public Integer getItemsPerPage() {
		return this.getInteger("itemsPerPage");
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.set("itemsPerPage", itemsPerPage);
	}

	@JsonProperty("meta")
	public Meta getMeta() {
		return (Meta) this.get("meta");
	}

	public void setMeta(Meta meta) {
		this.set("meta", meta);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("patch")
	public Patch getPatch() {
		return (Patch) this.get("patch");
	}

	public void setPatch(Patch patch) {
		this.set("patch", patch);
	}

	@JsonProperty("Resources")
	public List<Resource> getResources() {
		return (List<Resource>) this.get("Resources");
	}

	public void setResources(List<Resource> resources) {
		this.set("Resources", resources);
	}

	@JsonProperty("schema")
	public String getSchema() {
		return this.getString("schema");
	}

	public void setSchema(String schema) {
		this.set("schema", schema);
	}

	@JsonProperty("schemaExtensions")
	private List<Resource.Schema> getSchemaExtensions() {
		return (List<Resource.Schema>) this.get("schemaExtensions");
	}

	public void setSchemaExtensions(List<Resource.Schema> schemaExtensions) {
		this.set("schemaExtensions", schemaExtensions);
	}

	@JsonProperty("schemas")
	public List<String> getSchemas() {
		return (List<String>) this.get("schemas");
	}

	public void setSchemas(List<String> schemas) {
		this.set("schemas", schemas);
	}

	@JsonProperty("sort")
	public Sort getSort() {
		return (Sort) this.get("sort");
	}

	public void setSort(Sort sort) {
		this.set("sort", sort);
	}

	@JsonProperty("startIndex")
	public Integer getStartIndex() {
		return this.getInteger("startIndex");
	}

	public void setStartIndex(Integer startIndex) {
		this.set("startIndex", startIndex);
	}

	@JsonProperty("totalResults")
	public Integer getTotalResults() {
		return this.getInteger("totalResults");
	}

	public void setTotalResults(Integer totalResults) {
		this.set("totalResults", totalResults);
	}

	@JsonIgnore
	public boolean hasAuthenticationSchemeErrors() {
		if (super.hasErrors() || getAuthenticationSchemes() == null || getAuthenticationSchemes().size() == 0)
			return true;
		return false;
	}

	@JsonIgnore
	public boolean hasResourceErrors() {
		if (super.hasErrors() || getResources() == null || getResources().size() == 0) return true;
		return false;
	}

	@JsonIgnore
	public boolean hasAttributeErrors() {
		if (super.hasErrors() || getAttributes() == null || getAttributes().size() == 0) return true;
		return false;
	}

	public static class Resource extends VaultModel {

		@JsonProperty("active")
		public Boolean getActive() {
			return this.getBoolean("active");
		}

		public void setActive(Boolean active) {
			this.set("active", active);
		}

		@JsonProperty("attributes")
		public List<SCIMAttribute> getAttributes() {
			return (List<SCIMAttribute>) this.get("attributes");
		}

		public void setAttributes(List<SCIMAttribute> attributes) {
			this.set("attributes", attributes);
		}

		@JsonProperty("description")
		public String getDescription() {
			return this.getString("description");
		}

		public void setDescription(String description) {
			this.set("description", description);
		}

		@JsonProperty("displayName")
		public String getDisplayName() {
			return this.getString("displayName");
		}

		public void setDisplayName(String displayName) {
			this.set("displayName", displayName);
		}

		@JsonProperty("endpoint")
		public String getEndpoint() {
			return this.getString("endpoint");
		}

		public void setEndpoint(String endpoint) {
			this.set("endpoint", endpoint);
		}

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("locale")
		public String getLocale() {
			return this.getString("locale");
		}

		public void setLocale(String locale) {
			this.set("locale", locale);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("meta")
		public Meta getMeta() {
			return (Meta) this.get("meta");
		}

		public void setMeta(Meta meta) {
			this.set("meta", meta);
		}

		@JsonProperty("preferredLanguage")
		public String getPreferredLanguage() {
			return this.getString("preferredLanguage");
		}

		public void setPreferredLanguage(String preferredLanguage) {
			this.set("preferredLanguage", preferredLanguage);
		}

		@JsonProperty("schema")
		public String getSchema() {
			return this.getString("schema");
		}

		public void setSchema(String schema) {
			this.set("schema", schema);
		}

		@JsonProperty("schemaExtensions")
		public List<Schema> getSchemaExtensions() {
			return (List<Schema>) this.get("schemaExtensions");
		}

		public void setSchemaExtensions(List<Schema> schemaExtensions) {
			this.set("schemaExtensions", schemaExtensions);
		}

		@JsonProperty("schemas")
		public List<String> getSchemas() {
			return (List<String>) this.get("schemas");
		}

		public void setSchemas(List<String> schemas) {
			this.set("schemas", schemas);
		}


		@JsonProperty("timezone")
		public String getTimezone() {
			return this.getString("timezone");
		}

		public void setTimezone(String timezone) {
			this.set("timezone", timezone);
		}

		@JsonProperty("userName")
		public String getUserName() {
			return this.getString("userName");
		}

		public void setUserName(String userName) {
			this.set("userName", userName);
		}

		@JsonIgnore
		public boolean hasAttributeErrors() {
			if (getAttributes() == null || getAttributes().size() == 0) return true;
			return false;
		}

		@JsonIgnore
		public boolean hasSchemaErrors() {
			if (getSchemas() == null || getSchemas().size() == 0) return true;
			return false;
		}

		public static class Schema extends VaultModel {

			@JsonProperty("required")
			public Boolean getRequired() {
				return this.getBoolean("required");
			}

			public void setRequired(Boolean required) {
				this.set("required", required);
			}

			@JsonProperty("schema")
			public String getSchema() {
				return this.getString("schema");
			}

			public void setSchema(String schema) {
				this.set("schema", schema);
			}
		}
	}

	public static class Meta extends VaultModel {

		@JsonProperty("created")
		public String getCreated() {
			return this.getString("created");
		}

		public void setCreated(String created) {
			this.set("created", created);
		}

		@JsonProperty("lastModified")
		public String getLastModified() {
			return this.getString("lastModified");
		}

		public void setLastModified(String lastModified) {
			this.set("lastModified", lastModified);
		}

		@JsonProperty("location")
		public String getLocation() {
			return this.getString("location");
		}

		public void setLocation(String location) {
			this.set("location", location);
		}

		@JsonProperty("resourceType")
		public String getResourceType() {
			return this.getString("resourceType");
		}

		public void setResourceType(String resourceType) {
			this.set("resourceType", resourceType);
		}
	}

	public static class AuthenticationScheme extends VaultModel {

		@JsonProperty("description")
		public String getDescription() {
			return this.getString("description");
		}

		public void setDescription(String description) {
			this.set("description", description);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("primary")
		public Boolean getPrimary() {
			return this.getBoolean("primary");
		}

		public void setPrimary(Boolean primary) {
			this.set("primary", primary);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class Etag extends VaultModel {

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}

	public static class Sort extends VaultModel {

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}

	public static class ChangePassword extends VaultModel {

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}

	public static class Filter extends VaultModel {

		@JsonProperty("maxResults")
		public Integer getMaxResults() {
			return this.getInteger("maxResults");
		}

		public void setMaxResults(Integer maxResults) {
			this.set("maxResults", maxResults);
		}

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}

	public static class Patch extends VaultModel {

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}

	public static class Bulk extends VaultModel {

		@JsonProperty("maxOperations")
		public Integer getMaxOperations() {
			return this.getInteger("maxOperations");
		}

		public void setMaxOperations(Integer maxOperations) {
			this.set("maxOperations", maxOperations);
		}

		@JsonProperty("maxPayloadSize")
		public Integer getMaxPayloadSize() {
			return this.getInteger("maxPayloadSize");
		}

		public void setMaxPayloadSize(Integer maxPayloadSize) {
			this.set("maxPayloadSize", maxPayloadSize);
		}

		@JsonProperty("supported")
		public Boolean getSupported() {
			return this.getBoolean("supported");
		}

		public void setSupported(Boolean supported) {
			this.set("supported", supported);
		}
	}
}
