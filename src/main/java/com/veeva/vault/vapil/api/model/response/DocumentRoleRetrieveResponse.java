package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.List;

/**
 * Model for responses to Document Role GET requests
 *
 * @see DocumentRoleRequest#retrieveRoles(DocumentRequestType, int)
 * @see DocumentRoleRequest#retrieveRoles(DocumentRequestType, int, String)
 */
public class DocumentRoleRetrieveResponse extends VaultResponse {

	@JsonProperty("documentRoles")
	public List<DocumentRole> getDocumentRoles() {
		return (List<DocumentRole>) this.get("documentRoles");
	}

	public void setDocumentRoles(List<DocumentRole> documentRoles) {
		this.set("documentRoles", documentRoles);
	}

	@JsonProperty("errorCodes")
	public String getErrorCodes() {
		return this.getString("errorCodes");
	}

	public void setErrorCodes(String errorCodes) {
		this.set("errorCodes", errorCodes);
	}

	public static class DocumentRole extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("assignedUsers")
		public List<Long> getAssignedUsers() {
			return (List<Long>) this.get("assignedUsers");
		}

		public void setAssignedUsers(List<Long> assignedUsers) {
			this.set("assignedUsers", assignedUsers);
		}

		@JsonProperty("assignedGroups")
		public List<Long> getAssignedGroups() {
			return (List<Long>) this.get("assignedGroups");
		}

		public void setAssignedGroups(List<Long> assignedGroups) {
			this.set("assignedGroups", assignedGroups);
		}

		@JsonProperty("availableUsers")
		public List<Long> getAvailableUsers() {
			return (List<Long>) this.get("availableUsers");
		}

		public void setAvailableUsers(List<Long> availableUsers) {
			this.set("availableUsers", availableUsers);
		}

		@JsonProperty("availableGroups")
		public List<Long> getAvailableGroups() {
			return (List<Long>) this.get("availableGroups");
		}

		public void setAvailableGroups(List<Long> availableGroups) {
			this.set("availableGroups", availableGroups);
		}

		@JsonProperty("defaultUsers")
		public List<Long> getDefaultUsers() {
			return (List<Long>) this.get("defaultUsers");
		}

		public void setDefaultUsers(List<Long> defaultUsers) {
			this.set("defaultUsers", defaultUsers);
		}

		@JsonProperty("defaultGroups")
		public List<Long> getDefaultGroups() {
			return (List<Long>) this.get("defaultGroups");
		}

		public void setDefaultGroups(List<Long> defaultGroups) {
			this.set("defaultGroups", defaultGroups);
		}
	}
}
