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
 * Model for the following API calls
 * <p>
 * GET /api/{version}/objects/picklists
 * <br>
 * GET /api/{version}/objects/picklists/{picklist_name}
 */
public class PicklistResponse extends VaultResponse {

	@JsonProperty("picklists")
	public List<Picklist> getPicklists() {
		return (List<Picklist>) this.get("picklists");
	}

	public void setPicklists(List<Picklist> picklists) {
		this.set("picklists", picklists);
	}

	public static class Picklist extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("label")
		public String getLabel() {
			return getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("kind")
		public String getKind() {
			return getString("kind");
		}

		public void setKind(String kind) {
			this.set("kind", kind);
		}

		@JsonProperty("usedIn")
		public List<UsedIn> getUsedIn() {
			return (List) get("usedIn");
		}

		public void setUsedIn(List<UsedIn> usedIn) {
			this.set("usedIn", usedIn);
		}

		@JsonProperty("system")
		public Boolean getSystem() {
			return getBoolean("system");
		}

		public void setSystem(Boolean system) {
			this.set("system", system);
		}

		static public class UsedIn extends VaultModel {
			@JsonProperty("objectName")
			public String getObjectName() {
				return getString("objectName");
			}

			public void setObjectName(String objectName) {
				this.set("objectName", objectName);
			}

			@JsonProperty("propertyName")
			public String getPropertyName() {
				return getString("propertyName");
			}

			public void setPropertyName(String propertyName) {
				this.set("propertyName", propertyName);
			}
		}
	}
}