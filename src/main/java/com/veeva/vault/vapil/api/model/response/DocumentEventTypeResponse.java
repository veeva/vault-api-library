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
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/metadata/objects/documents/events
 */
public class DocumentEventTypeResponse extends VaultResponse {

	@JsonProperty("events")
	public List<Event> getEvents() {
		return (List<Event>) this.get("events");
	}

	@JsonProperty("events")
	public void setEvents(List<Event> events) {
		this.set("events", events);
	}

	public static class Event extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("subtypes")
		public List<SubType> getSubtypes() {
			return (List<SubType>) this.get("subtypes");
		}

		@JsonProperty("subtypes")
		public void setSubtypes(List<SubType> subtypes) {
			this.set("subtypes", subtypes);
		}

		public static class SubType extends VaultModel {

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			@JsonProperty("label")
			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			@JsonProperty("name")
			public void setName(String name) {
				this.set("name", name);
			}

			@JsonProperty("value")
			public String getValue() {
				return this.getString("value");
			}

			@JsonProperty("value")
			public void setValue(String value) {
				this.set("value", value);
			}

		}
	}
}