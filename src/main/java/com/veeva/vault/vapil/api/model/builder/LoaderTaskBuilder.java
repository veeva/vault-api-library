/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.builder;

import com.veeva.vault.vapil.api.model.common.LoaderTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Model for the Vault Loader entity, since Vault Loader
 * handles multiple object/document entities per request.
 */
public class LoaderTaskBuilder {

	private LoaderTask loaderTask = new LoaderTask();
	private List<ExtractOption> extractOptions = new ArrayList<>();
	protected List<String> fields = new LinkedList<>();
	protected List<String> orderBy = new LinkedList<>();
	protected StringBuilder whereClause = new StringBuilder();
	private Integer maxRows;
	private Integer skip;
	private StringBuilder vqlCriteria = new StringBuilder();

	public LoaderTask build() {
		if (extractOptions.size() > 0) {
			StringBuilder extractBuilder = new StringBuilder();
			for (ExtractOption extractOption : extractOptions) {
				if (extractBuilder.length() > 0) {
					extractBuilder.append(",");
				}
				extractBuilder.append(extractOption.apiName);
			}
			loaderTask.setExtractOptions(extractBuilder.toString());
		}

		if ((fields != null) && (!fields.isEmpty())) {
			loaderTask.setFields(fields);
		}

		if (whereClause.length() > 0) {
			vqlCriteria.append(whereClause);
		}

		if (maxRows != null) {
			vqlCriteria.append(" MAXROWS " + maxRows);
		}

		if (skip != null) {
			vqlCriteria.append(" SKIP " + skip);
		}

		if (vqlCriteria.length() > 0) {
			loaderTask.setVqlCriteria(vqlCriteria.toString());
		}

		return loaderTask;
	}

	public void clear() {
		fields.clear();
		orderBy.clear();
		whereClause.setLength(0);
		extractOptions = new ArrayList<>();
		loaderTask = new LoaderTask();
	}

	/*
		LoaderTask setters / getters
	*/

	public enum Action {
		CREATE("create"),
		UPDATE("update"),
		UPSERT("upsert"),
		DELETE("delete");

		String apiName;

		Action(String apiName) {
			this.apiName = apiName;
		}

		public String getApiName() {
			return apiName;
		}
	}

	public LoaderTaskBuilder setAction(Action action) {
		loaderTask.setAction(action.getApiName());
		return this;
	}

	public enum ExtractOption {
		INCLUDE_SOURCE("include_source__v"),
		INCLUDE_RENDITIONS("include_renditions__v");

		String apiName;

		ExtractOption(String apiName) {
			this.apiName = apiName;
		}

		public String getApiName() {
			return apiName;
		}
	}

	public LoaderTaskBuilder setDocumentMigrationMode(Boolean documentMigrationMode) {
		loaderTask.setDocumentMigrationMode(documentMigrationMode);
		return this;
	}

	public LoaderTaskBuilder addExtractOption(ExtractOption extractOption) {
		extractOptions.add(extractOption);
		return this;
	}

	public LoaderTaskBuilder setExtractOptions(List<ExtractOption> extractOptions) {
		extractOptions.addAll(extractOptions);
		return this;
	}

	public LoaderTaskBuilder setFile(String file) {
		loaderTask.setFile(file);
		return this;
	}

	public LoaderTaskBuilder setObject(String object) {
		loaderTask.setObject(object);
		return this;
	}

	public LoaderTaskBuilder setRecordMigrationMode(Boolean recordMigrationMode) {
		loaderTask.setRecordMigrationMode(recordMigrationMode);
		return this;
	}

	public enum ObjectType {
		DOCUMENTS("documents__v"),
		DOCUMENT_ATTACHMENTS("document_attachments__v"),
		DOCUMENT_RELATIONSHIPS("document_relationships__v"),
		DOCUMENT_RENDITIONS("document_renditions__v"),
		DOCUMENT_ROLES("document_roles__v"),
		DOCUMENT_VERSIONS("document_versions__v"),
		DOCUMENT_VERSIONS_ROLES("document_versions_roles__v"),
		GROUPS("groups__v"),
		OBJECTS("vobjects__v");

		String apiName;

		ObjectType(String apiName) {
			this.apiName = apiName;
		}

		public String getApiName() {
			return apiName;
		}
	}

	public LoaderTaskBuilder setObjectType(ObjectType objectType) {
		if (objectType != null) {
			loaderTask.setObjectType(objectType.getApiName());
		} else {
			loaderTask.setObjectType(null);
		}

		return this;
	}

	public LoaderTaskBuilder setOrder(Integer order) {
		loaderTask.setOrder(order);
		return this;
	}

	public LoaderTaskBuilder addField(String fieldName) {
		fields.add(fieldName);
		return this;
	}

	public LoaderTaskBuilder setFields(List<String> fieldNames) {
		fields.addAll(fieldNames);
		return this;
	}

	public LoaderTaskBuilder setIdParam(String idParam) {
		loaderTask.setIdParam(idParam);
		return this;
	}

	public LoaderTaskBuilder addOrderBy(String fieldName) {
		orderBy.add(fieldName);
		return this;
	}

	public LoaderTaskBuilder appendWhere(Object... vql) {
		if ((vql != null) && (vql.length > 0)) {

			String where = null;
			if (vql.length > 1) {
				Object[] args = Arrays.copyOfRange(vql, 1, vql.length - 1);
				where = String.format(vql[0].toString(), args);
			} else {
				where = vql[0].toString();
			}
			whereClause.append(where);
		}
		return this;
	}

	public LoaderTaskBuilder setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
		return this;
	}

	public LoaderTaskBuilder setSkip(Integer skip) {
		this.skip = skip;
		return this;
	}

}
