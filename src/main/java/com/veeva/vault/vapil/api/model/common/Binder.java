/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the Binder meta-data, which is an array
 * of nodes (sections) with parent/child relationships.
 * Each node/section can be another section, a document, or
 * a binder.
 */
public class Binder extends VaultModel {

	@JsonProperty("nodes")
	public List<Node> getNodes() {
		return (List<Node>) this.get("nodes");
	}

	public void setNodes(List<Node> nodes) {
		this.set("nodes", nodes);
	}

	static public class Node extends VaultModel {

		/**
		 * Determine if children nodes exist
		 *
		 * @return true if child nodes exist, false if none
		 */
		public Boolean hasChildren() {
			Boolean b = false;
			List<Node> children = getNodes();
			if (children != null && children.size() > 0)
				b = true;

			return b;
		}

		@JsonProperty("properties")
		public List<BinderSection> getProperties() {
			return (List<BinderSection>) this.get("properties");
		}

		public void setProperties(List<BinderSection> properties) {
			this.set("properties", properties);
		}

		@JsonProperty("nodes")
		public List<Node> getNodes() {
			return (List<Node>) this.get("nodes");
		}

		public void setNodes(List<Node> nodes) {
			this.set("nodes", nodes);
		}

		static public class BinderSection extends VaultModel {

			@JsonProperty("document_id__v")
			public Integer getDocumentId() {
				return getInteger("document_id__v");
			}

			public void setDocumentId(Integer documentId) {
				this.set("document_id__v", documentId);
			}

			@JsonProperty("id")
			public String getId() {
				return getString("id");
			}

			public void setId(String id) {
				this.set("id", id);
			}

			@JsonProperty("name__v")
			public String getName() {
				return getString("name__v");
			}

			public void setName(String name) {
				this.set("name__v", name);
			}

			@JsonProperty("minor_version_number__v")
			public Integer getMinorVersionNumber() {
				return getInteger("minor_version_number__v");
			}

			public void setMinorVersionNumber(Integer minorVersionNumber) {
				this.set("minor_version_number__v", minorVersionNumber);
			}

			@JsonProperty("major_version_number__v")
			public Integer getMajorVersionNumber() {
				return getInteger("major_version_number__v");
			}

			public void setMajorVersionNumber(Integer majorVersionNumber) {
				this.set("major_version_number__v", majorVersionNumber);
			}

			@JsonProperty("order__v")
			public Integer getOrder() {
				return getInteger("order__v");
			}

			public void setOrder(Integer order) {
				this.set("order__v", order);
			}

			@JsonProperty("parent_id__v")
			public String getParentId() {
				return getString("parent_id__v");
			}

			public void setParentId(String parentId) {
				this.set("parent_id__v", parentId);
			}

			@JsonProperty("section_number__v")
			public String getSectionNumber() {
				return getString("section_number__v");
			}

			public void setSectionNumber(String sectionNumber) {
				this.set("section_number__v", sectionNumber);
			}

			@JsonProperty("type__v")
			public String getType() {
				return getString("type__v");
			}

			public void setType(String type) {
				this.set("type__v", type);
			}
		}
	}
}
