package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SCIMUser extends VaultModel {
	// static schema variables from Vault
	private static final String SCHEMA_VAULT_USER = "urn:ietf:params:scim:schemas:extension:veevavault:2.0:User";

	@JsonProperty(SCHEMA_VAULT_USER)
	public SchemaUserObject getSchemaUserObject() {
		return (SchemaUserObject) get(SCHEMA_VAULT_USER);
	}

	public void setSchemaUserObject(SchemaUserObject schemaUserObject) {
		this.set(SCHEMA_VAULT_USER, schemaUserObject);
	}

	@JsonProperty("active")
	public Boolean getActive() {
		return this.getBoolean("active");
	}

	public void setActive(Boolean active) {
		this.set("active", active);
	}

	@JsonProperty("displayName")
	public String getDisplayName() {
		return this.getString("displayName");
	}

	public void setDisplayName(String displayName) {
		this.set("displayName", displayName);
	}

	@JsonProperty("emails")
	public List<Email> getEmails() {
		return (List<Email>) get("emails");
	}

	public void setEmails(List<Email> emails) {
		this.set("emails", emails);
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

	@JsonProperty("meta")
	public Meta getMeta() {
		return (Meta) get("meta");
	}

	public void setMeta(Meta meta) {
		this.set("meta", meta);
	}

	@JsonProperty("name")
	public Name getName() {
		return (Name) get("name");
	}

	public void setName(Name name) {
		this.set("name", name);
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
		return (List<Schema>) get("schemaExtensions");
	}

	public void setSchemaExtensions(List<Schema> schemaExtensions) {
		this.set("schemaExtensions", schemaExtensions);
	}

	@JsonProperty("schemas")
	public List<String> getSchemas() {
		return (List<String>) get("schemas");
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

	public boolean hasSchemaErrors() {
		List<String> schemas = getSchemas();
		if (schemas == null || schemas.size() == 0) return true;
		return false;
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

	public static class Email extends VaultModel {
		public Email() {
		}

		public Email(String type, String value) {
			this.setType(type);
			this.setValue(value);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("value")
		public String getValue() {
			return this.getString("value");
		}

		public void setValue(String value) {
			this.set("value", value);
		}
	}

	public static class Name extends VaultModel {

		public Name() {
		}

		public Name(String givenName, String familyName) {
			this.setFamilyName(familyName);
			this.setGivenName(givenName);
		}

		@JsonProperty("familyName")
		public String getFamilyName() {
			return this.getString("familyName");
		}

		public void setFamilyName(String familyName) {
			this.set("familyName", familyName);
		}

		@JsonProperty("formatted")
		public String getFormatted() {
			return this.getString("formatted");
		}

		public void setFormatted(String formatted) {
			this.set("formatted", formatted);
		}

		@JsonProperty("givenName")
		public String getGivenName() {
			return this.getString("givenName");
		}

		public void setGivenName(String givenName) {
			this.set("givenName", givenName);
		}

		@JsonProperty("honorificPrefix")
		public String getHonorificPrefix() {
			return this.getString("honorificPrefix");
		}

		public void setHonorificPrefix(String honorificPrefix) {
			this.set("honorificPrefix", honorificPrefix);
		}

		@JsonProperty("honorificSuffix")
		public String getHonorificSuffix() {
			return this.getString("honorificSuffix");
		}

		public void setHonorificSuffix(String honorificSuffix) {
			this.set("honorificSuffix", honorificSuffix);
		}

		@JsonProperty("middleName")
		public String getMiddleName() {
			return this.getString("middleName");
		}

		public void setMiddleName(String middleName) {
			this.set("middleName", middleName);
		}

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

	public static class SchemaUserObject extends VaultModel {

		@JsonProperty("activationDate")
		public String getActivationDate() {
			return this.getString("activationDate");
		}

		public void setActivationDate(String activationDate) {
			this.set("activationDate", activationDate);
		}

		@JsonProperty("createdBy")
		public SchemaUserObject.ReferenceValue getCreatedBy() {
			return (ReferenceValue) get("createdBy");
		}

		public void setCreatedBy(ReferenceValue createdBy) {
			set("createdBy", createdBy);
		}

		@JsonProperty("domainAdmin")
		public Boolean getDomainAdmin() {
			return this.getBoolean("domainAdmin");
		}

		public void setDomainAdmin(Boolean domainAdmin) {
			this.set("domainAdmin", domainAdmin);
		}

		@JsonProperty("extendedAttributes")
		public List<SCIMAttribute> getExtendedAttributes() {
			return (List<SCIMAttribute>) get("extendedAttributes");
		}

		public void setExtendedAttributes(List<SCIMAttribute> extendedAttributes) {
			set("extendedAttributes", extendedAttributes);
		}

		@JsonProperty("favoriteDocNewComment")
		public Boolean getFavoriteDocNewComment() {
			return this.getBoolean("favoriteDocNewComment");
		}

		public void setFavoriteDocNewComment(Boolean favoriteDocNewComment) {
			this.set("favoriteDocNewComment", favoriteDocNewComment);
		}

		@JsonProperty("favoriteDocNewContent")
		public Boolean getFavoriteDocNewContent() {
			return this.getBoolean("favoriteDocNewContent");
		}

		public void setFavoriteDocNewContent(Boolean favoriteDocNewContent) {
			this.set("favoriteDocNewContent", favoriteDocNewContent);
		}

		@JsonProperty("favoriteDocNewStatus")
		public Boolean getFavoriteDocNewStatus() {
			return this.getBoolean("favoriteDocNewStatus");
		}

		public void setFavoriteDocNewStatus(Boolean favoriteDocNewStatus) {
			this.set("favoriteDocNewStatus", favoriteDocNewStatus);
		}

		@JsonProperty("globalId")
		public String getGlobalId() {
			return this.getString("globalId");
		}

		public void setGlobalId(String globalId) {
			this.set("globalId", globalId);
		}

		@JsonProperty("lastLogin")
		public String getLastLogin() {
			return this.getString("lastLogin");
		}

		public void setLastLogin(String lastLogin) {
			this.set("lastLogin", lastLogin);
		}

		@JsonProperty("lastModifiedBy")
		public SchemaUserObject.ReferenceValue getLastModifiedBy() {
			return (ReferenceValue) get("lastModifiedBy");
		}

		public void setLastModifiedBy(ReferenceValue lastModifiedBy) {
			set("lastModifiedBy", lastModifiedBy);
		}

		@JsonProperty("licenseType")
		public SchemaUserObject.ReferenceValue getLicenseType() {
			return (ReferenceValue) get("licenseType");
		}

		public void setLicenseType(ReferenceValue licenseType) {
			set("licenseType", licenseType);
		}

		@JsonProperty("lifecycle")
		public String getLifecycle() {
			return this.getString("lifecycle");
		}

		public void setLifecycle(String lifecycle) {
			this.set("lifecycle", lifecycle);
		}

		@JsonProperty("preferredCurrency")
		public String getPreferredCurrency() {
			return this.getString("preferredCurrency");
		}

		public void setPreferredCurrency(String preferredCurrency) {
			this.set("preferredCurrency", preferredCurrency);
		}

		@JsonProperty("productAnnouncementEmails")
		public Boolean getProductAnnouncementEmails() {
			return this.getBoolean("productAnnouncementEmails");
		}

		public void setProductAnnouncementEmails(Boolean productAnnouncementEmails) {
			this.set("productAnnouncementEmails", productAnnouncementEmails);
		}

		@JsonProperty("securityPolicy")
		public SchemaUserObject.ReferenceValue getSecurityPolicy() {
			return (ReferenceValue) get("securityPolicy");
		}

		public void setSecurityPolicy(ReferenceValue securityPolicy) {
			set("securityPolicy", securityPolicy);
		}

		@JsonProperty("securityProfile")
		public SchemaUserObject.ReferenceValue getSecurityProfile() {
			return (ReferenceValue) get("securityProfile");
		}

		public void setSecurityProfile(ReferenceValue securityProfile) {
			set("securityProfile", securityProfile);
		}

		@JsonProperty("sendWelcomeEmail")
		public Boolean getSendWelcomeEmail() {
			return this.getBoolean("sendWelcomeEmail");
		}

		public void setSendWelcomeEmail(Boolean sendWelcomeEmail) {
			this.set("sendWelcomeEmail", sendWelcomeEmail);
		}

		@JsonProperty("systemAvailabilityEmails")
		public Boolean getSystemAvailabilityEmails() {
			return this.getBoolean("systemAvailabilityEmails");
		}

		public void setSystemAvailabilityEmails(Boolean systemAvailabilityEmails) {
			this.set("systemAvailabilityEmails", systemAvailabilityEmails);
		}

		@JsonIgnore
		public boolean hasExtendedAttributeErrors() {
			List<SCIMAttribute> extendedAttributes = getExtendedAttributes();
			if (extendedAttributes == null || extendedAttributes.size() == 0) return true;
			return false;
		}

		public static class ReferenceValue extends VaultModel {

			@JsonProperty("$ref")
			public String getRef() {
				return this.getString("$ref");
			}

			public void setRef(String ref) {
				this.set("$ref", ref);
			}

			@JsonProperty("value")
			public String getValue() {
				return this.getString("value");
			}

			public void setValue(String value) {
				this.set("value", value);
			}
		}
	}

}
