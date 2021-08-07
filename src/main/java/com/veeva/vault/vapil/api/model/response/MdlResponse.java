/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/mdl/components/{component_type}.{record_name}
 */
public class MdlResponse extends VaultResponse {
	@JsonProperty("component_type")
	public String getComponentType() {
		return this.getString("component_type");
	}

	public void setComponentType(String componentType) {
		this.set("component_type", componentType);
	}

	@JsonProperty("record_name")
	public String getRecordName() {
		return this.getString("record_name");
	}

	public void setRecordName(String recordName) {
		this.set("record_name", recordName);
	}

	public enum CommandType {
		CREATE("CREATE"),
		RECREATE("RECREATE"),
		RENAME("RENAME"),
		ALTER("ALTER"),
		DROP("DROP"),
		UNDEFINED("UNDEFINED");

		String value;

		CommandType(String value) {
			if (value != null) {
				this.value = value.toUpperCase().trim();
			} else {
				this.value = "UNDEFINED";
			}
		}

		@JsonIgnore
		public String getValue() {
			return value;
		}
	}

	@JsonIgnore
	public CommandType getCommandType() {
		CommandType result = CommandType.UNDEFINED;
		try {
			if (this.getBinaryContent() != null) {
				String mdl = new String(this.getBinaryContent());
				if (!mdl.isEmpty() && (mdl.contains(" "))) {
					String command = mdl.substring(0, mdl.indexOf(" "));
					result = CommandType.valueOf(command);
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
}