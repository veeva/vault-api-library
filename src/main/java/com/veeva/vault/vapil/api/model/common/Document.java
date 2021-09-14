/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Model for the Document Data
 */
public class Document extends VaultModel {
	//---------------------------------------------------------------------------------
	//id fields

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("version_id")
	public String getVersionId() {
		return getString("version_id");
	}

	public void setVersionId(String versionId) {
		this.set("version_id", versionId);
	}

	@JsonProperty("major_version_number__v")
	public Integer getMajorVersionNumber() {
		return getInteger("major_version_number__v");
	}

	public void setMajorVersionNumber(Integer majorVersionNumber) {
		this.set("major_version_number__v", majorVersionNumber);
	}

	@JsonProperty("minor_version_number__v")
	public Integer getMinorVersionNumber() {
		return getInteger("minor_version_number__v");
	}

	public void setMinorVersionNumber(Integer minorVersionNumber) {
		this.set("minor_version_number__v", minorVersionNumber);
	}

	//---------------------------------------------------------------------------------
	//All other fields in Alpha Order

	@JsonProperty("annotations_all__v")
	public Integer getAnnotationsAll() {
		return getInteger("annotations_all__v");
	}

	public void setAnnotationsAll(Integer annotationsAll) {
		this.set("annotations_all__v", annotationsAll);
	}

	@JsonProperty("annotations_anchors__v")
	public Integer getAnnotationsAnchors() {
		return getInteger("annotations_anchors__v");
	}

	public void setAnnotationsAnchors(Integer annotationsAnchors) {
		this.set("annotations_anchors__v", annotationsAnchors);
	}

	@JsonProperty("annotations_lines__v")
	public Integer getAnnotationsLines() {
		return getInteger("annotations_lines__v");
	}

	public void setAnnotationsLines(Integer annotationsLines) {
		this.set("annotations_lines__v", annotationsLines);
	}

	@JsonProperty("annotations_links__v")
	public Integer getAnnotationsLinks() {
		return getInteger("annotations_links__v");
	}

	public void setAnnotationsLinks(Integer annotationsLinks) {
		this.set("annotations_links__v", annotationsLinks);
	}

	@JsonProperty("annotations_notes__v")
	public Integer getAnnotationsNotes() {
		return getInteger("annotations_notes__v");
	}

	public void setAnnotationsNotes(Integer annotationsNotes) {
		this.set("annotations_notes__v", annotationsNotes);
	}

	@JsonProperty("annotations_resolved__v")
	public Integer getAnnotationsResolved() {
		return getInteger("annotations_resolved__v");
	}

	public void setAnnotationsResolved(Integer annotationsResolved) {
		this.set("annotations_resolved__v", annotationsResolved);
	}

	@JsonProperty("annotations_unresolved__v")
	public Integer getAnnotationsUnresolved() {
		return getInteger("annotations_unresolved__v");
	}

	public void setAnnotationsUnresolved(Integer annotationsUnresolved) {
		this.set("annotations_unresolved__v", annotationsUnresolved);
	}

	@JsonProperty("archive__v")
	public Boolean getArchive() {
		return getBoolean("archive__v");
	}

	public void setArchive(Boolean archive) {
		this.set("archive__v", archive);
	}

	@JsonIgnore
	public boolean isArchive() {
		return BooleanUtils.isTrue(getArchive());
	}

	@JsonProperty("binder__v")
	public Boolean getBinder() {
		return getBoolean("binder__v");
	}

	public void setBinder(Boolean binder) {
		this.set("binder__v", binder);
	}

	@JsonIgnore
	public boolean isBinder() {
		return BooleanUtils.isTrue(getBinder());
	}

	@JsonProperty("bound_source_major_version__v")
	public Integer getBoundSourceMajorVersion() {
		return getInteger("bound_source_major_version__v");
	}

	public void setBoundSourceMajorVersion(Integer boundSourceMajorVersion) {
		this.set("bound_source_major_version__v", boundSourceMajorVersion);
	}

	@JsonProperty("bound_source_minor_version__v")
	public Integer getBoundSourceMinorVersion() {
		return getInteger("bound_source_minor_version__v");
	}

	public void setBoundSourceMinorVersion(Integer boundSourceMinorVersion) {
		this.set("bound_source_minor_version__v", boundSourceMinorVersion);
	}

	@JsonProperty("classification__v")
	public String getClassification() {
		return getString("classification__v");
	}

	public void setClassification(String classification) {
		this.set("classification__v", classification);
	}

	@JsonProperty("created_by__v")
	public Integer getCreatedBy() {
		return getInteger("created_by__v");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by__v", createdBy);
	}

	@JsonProperty("crosslink__v")
	public Boolean getCrossLink() {
		return getBoolean("crosslink__v");
	}

	public void setCrosslink(Boolean crosslink) {
		this.set("crosslink__v", crosslink);
	}

	@JsonIgnore
	public boolean isCrossLink() {
		return BooleanUtils.isTrue(getCrossLink());
	}

	@JsonProperty("description__v")
	public String getDescription() {
		return getString("description__v");
	}

	public void setDescription(String description) {
		this.set("description__v", description);
	}

	@JsonProperty("document_creation_date__v")
	public String getDocumentCreationDate() {
		return getString("document_creation_date__v");
	}

	public void setDocumentCreationDate(String documentCreationDate) {
		this.set("document_creation_date__v", documentCreationDate);
	}

	@JsonProperty("document_number__v")
	public String getDocumentNumber() {
		return getString("document_number__v");
	}

	public void setDocumentNumber(String documentNumber) {
		this.set("document_number__v", documentNumber);
	}

	@JsonProperty("external_id__v")
	public String getExternalId() {
		return getString("external_id__v");
	}

	public void setExternalId(String externalId) {
		this.set("external_id__v", externalId);
	}

	@JsonProperty("filename__v")
	public String getFilename() {
		return getString("filename__v");
	}

	public void setFilename(String filename) {
		this.set("filename__v", filename);
	}

	@JsonProperty("format__v")
	public String getFormat() {
		return getString("format__v");
	}

	public void setFormat(String format) {
		this.set("format__v", format);
	}

	@JsonProperty("latest_source_minor_version__v")
	public Integer getLatestSourceMinorVersion() {
		return getInteger("latest_source_minor_version__v");
	}

	public void setLatestSourceMinorVersion(Integer latestSourceMinorVersion) {
		this.set("latest_source_minor_version__v", latestSourceMinorVersion);
	}

	@JsonProperty("last_modified_by__v")
	public Integer getLastModifiedBy() {
		return getInteger("last_modified_by__v");
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.set("last_modified_by__v", lastModifiedBy);
	}

	@JsonProperty("latest_source_major_version__v")
	public Integer getLatestSourceMajorVersion() {
		return getInteger("latest_source_major_version__v");
	}

	public void setLatestSourceMajorVersion(Integer latestSourceMajorVersion) {
		this.set("latest_source_major_version__v", latestSourceMajorVersion);
	}

	@JsonProperty("lifecycle__v")
	public String getLifecycle() {
		return getString("lifecycle__v");
	}

	public void setLifecycle(String lifecycle) {
		this.set("lifecycle__v", lifecycle);
	}

	@JsonProperty("link_status__v")
	public List<String> getLinkStatus() {
		return (List<String>) get("link_status__v");
	}

	public void setLinkStatus(List<String> linkStatus) {
		this.set("link_status__v", linkStatus);
	}

	@JsonProperty("locked__v")
	public Boolean getLocked() {
		return getBoolean("locked__v");
	}

	public void setLocked(Boolean locked) {
		this.set("locked__v", locked);
	}

	@JsonIgnore
	public boolean isLocked() {
		return BooleanUtils.isTrue(getLocked());
	}

	@JsonProperty("md5checksum__v")
	public String getMd5checksum() {
		return getString("md5checksum__v");
	}

	public void setMd5checksum(String md5checksum) {
		this.set("md5checksum__v", md5checksum);
	}

	@JsonProperty("name__v")
	public String getName() {
		return getString("name__v");
	}

	public void setName(String name) {
		this.set("name__v", name);
	}

	@JsonProperty("pages__v")
	public Integer getPages() {
		return getInteger("pages__v");
	}

	public void setPages(Integer pages) {
		this.set("pages__v", pages);
	}

	@JsonProperty("size__v")
	public Integer getSize() {
		return getInteger("size__v");
	}

	public void setSize(Integer size) {
		this.set("size__v", size);
	}

	@JsonProperty("source_binding_rule__v")
	public List<String> getSourceBindingRule() {
		return (List<String>) get("source_binding_rule__v");
	}

	public void setSourceBindingRule(List<String> sourceBindingRule) {
		this.set("source_binding_rule__v", sourceBindingRule);
	}

	@JsonProperty("source_document_id__v")
	public Integer getSourceDocumentId() {
		return getInteger("source_document_id__v");
	}

	public void setSourceDocumentId(Integer sourceDocumentId) {
		this.set("source_document_id__v", sourceDocumentId);
	}

	@JsonProperty("source_document_name__v")
	public String getSourceDocumentName() {
		return getString("source_document_name__v");
	}

	public void setSourceDocumentName(String sourceDocumentName) {
		this.set("source_document_name__v", sourceDocumentName);
	}

	@JsonProperty("source_document_number__v")
	public String getSourceDocumentNumber() {
		return getString("source_document_number__v");
	}

	public void setSourceDocumentNumber(String sourceDocumentNumber) {
		this.set("source_document_number__v", sourceDocumentNumber);
	}

	@JsonProperty("source_owner__v")
	public Integer getSourceOwner() {
		return getInteger("source_owner__v");
	}

	public void setSourceOwner(Integer sourceOwner) {
		this.set("source_owner__v", sourceOwner);
	}

	@JsonProperty("source_vault_id__v")
	public Integer getSourceVaultId() {
		return getInteger("source_vault_id__v");
	}

	public void setSourceVaultId(Integer sourceVaultId) {
		this.set("source_vault_id__v", sourceVaultId);
	}

	@JsonProperty("source_vault_name__v")
	public String getSourceVaultName() {
		return getString("source_vault_name__v");
	}

	public void setSourceVaultName(String sourceVaultName) {
		this.set("source_vault_name__v", sourceVaultName);
	}

	@JsonProperty("status__v")
	public String getStatus() {
		return getString("status__v");
	}

	public void setStatus(String status) {
		this.set("status__v", status);
	}

	@JsonProperty("subtype__v")
	public String getSubtype() {
		return getString("subtype__v");
	}

	public void setSubtype(String subtype) {
		this.set("subtype__v", subtype);
	}

	@JsonProperty("suppress_rendition__v")
	public String getSuppressRendition() {
		return getString("suppress_rendition__v");
	}

	public void setSuppressRendition(String suppressRendition) {
		this.set("suppress_rendition__v", suppressRendition);
	}

	@JsonProperty("title__v")
	public String getTitle() {
		return getString("title__v");
	}

	public void setTitle(String title) {
		this.set("title__v", title);
	}

	@JsonProperty("type__v")
	public String getType() {
		return getString("type__v");
	}

	public void setType(String type) {
		this.set("type__v", type);
	}

	@JsonProperty("version_created_by__v")
	public Integer getVersionCreatedBy() {
		return getInteger("version_created_by__v");
	}

	public void setVersionCreatedBy(Integer versionCreatedBy) {
		this.set("version_created_by__v", versionCreatedBy);
	}

	@JsonProperty("version_creation_date__v")
	public String getVersionCreationDate() {
		return getString("version_creation_date__v");
	}

	public void setVersionCreationDate(String versionCreationDate) {
		this.set("version_creation_date__v", versionCreationDate);
	}

	@JsonProperty("version_modified_date__v")
	public String getVersionModifiedDate() {
		return getString("version_modified_date__v");
	}

	public void setVersionModifiedDate(String versionModifiedDate) {
		this.set("version_modified_date__v", versionModifiedDate);
	}
}
