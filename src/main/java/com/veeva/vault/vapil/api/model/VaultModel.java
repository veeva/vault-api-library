/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VaultModel {
	private static Logger log = Logger.getLogger(VaultModel.class);

	@JsonIgnore
	private List<String> fieldNames = new ArrayList<>();

	@JsonIgnore
	public List<String> getFieldNames() {
		return this.fieldNames;
	}

	/**
	 * Holds all model data
	 */
	@JsonIgnore
	private Map<String, Object> vaultModelData = new HashMap<>();

	/**
	 * Get all model data
	 *
	 * @return Map&lt;String, Object%gt;
	 */
	@JsonAnyGetter
	public Map<String, Object> getVaultModelData() {
		return this.vaultModelData;
	}

	/**
	 * Set all model data
	 *
	 * @param keyValueData Map of model data
	 */
	@JsonIgnore
	public void setVaultModelData(Map<String, Object> keyValueData) {
		this.fieldNames.clear();
		if (keyValueData != null)
			this.fieldNames.addAll(keyValueData.keySet());

		this.vaultModelData = keyValueData;
	}

	/**
	 * Get any value from the map by key
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return Object
	 */
	@JsonIgnore
	public Object get(String key) {
		return this.vaultModelData.get(key);
	}

	/**
	 * Add any value to the map by key/value
	 *
	 * @param key   field name (Vault name format: example_field__c)
	 * @param value field value
	 */
	@JsonAnySetter
	public void set(String key, Object value) {
		if (!this.fieldNames.contains(key))
			this.fieldNames.add(key);

		this.vaultModelData.put(key, value);
	}

	/**
	 * Get any value from the map by key as Boolean
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return Boolean
	 */
	@JsonIgnore
	public Boolean getBoolean(String key) {
		if (this.get(key) != null) {
			return (Boolean) this.get(key);
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as Integer
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return Integer
	 */
	@JsonIgnore
	public Integer getInteger(String key) {
		if (this.get(key) != null) {
			return (Integer) this.get(key);
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as List&lt;Integer%gt;
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return List&lt;Integer%gt;
	 */
	@JsonIgnore
	public List<Integer> getListInteger(String key) {
		Object value = this.get(key);
		if (value != null) {
			if (value instanceof String) {
				return Stream.of(((String) value).split(","))
						.map(String::trim)
						.map(Integer::parseInt)
						.collect(Collectors.toList());
			} else {
				return (List<Integer>) value;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as List&lt;String%gt;
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return List&lt;String%gt;
	 */
	@JsonIgnore
	public List<String> getListString(String key) {
		Object value = this.get(key);
		if (value != null) {
			if (value instanceof String) {
				return Arrays.asList(((String) value).split(","));
			} else {
				return (List<String>) value;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as List&lt;Long%gt;
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return List&lt;Long%gt;
	 */
	@JsonIgnore
	public List<Long> getListLong(String key) {
		Object value = this.get(key);
		if (value != null) {
			if (value instanceof String) {
				return Stream.of(((String) value).split(","))
						.map(String::trim)
						.map(Long::parseLong)
						.collect(Collectors.toList());
			} else {
				return (List<Long>) value;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as Long
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return Long
	 */
	@JsonIgnore
	public Long getLong(String key) {
		if (this.get(key) != null) {
			return (Long) get(key);
		} else {
			return null;
		}
	}

	/**
	 * Get any value from the map by key as String
	 *
	 * @param key field name (Vault name format: example_field__c)
	 * @return String
	 */
	@JsonIgnore
	public String getString(String key) {
		return (String) this.get(key);
	}


	/**
	 * Converts the existing object into a Map of String/Object
	 *
	 * @return Map representing all model data
	 */
	@JsonIgnore
	public Map<String, Object> toMap() {
		return toMap(true);
	}

	/**
	 * Converts the existing object into a Map of String/Object
	 *
	 * @param convertArrayToString when true, ArrayList of String will be converted to CSV values
	 * @return Map representing all model data
	 */
	@JsonIgnore
	public Map<String, Object> toMap(boolean convertArrayToString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(this, new TypeReference<Map<String, Object>>() {
		});

		//this is necessary b/c the API wants a CSV string not array list
		if (convertArrayToString && result != null && !result.isEmpty()) {
			for (Map.Entry entry : result.entrySet()) {
				if (entry.getValue() != null && entry.getValue() instanceof ArrayList) {
					List<Object> values = (List) entry.getValue();
					if (!values.isEmpty() && values.get(0) instanceof String) {
						List<String> stringValues = (List) entry.getValue();
						result.put(entry.getKey().toString(), String.join(",", stringValues));
					} else if (!values.isEmpty() && values.get(0) instanceof Integer) {
						List<Integer> integerValues = (List) entry.getValue();
						List<String> stringValues = integerValues.stream()
								.map(s -> String.valueOf(s))
								.collect(Collectors.toList());
						result.put(entry.getKey().toString(), String.join(",", stringValues));
					} else if (!values.isEmpty() && values.get(0) instanceof Long) {
						List<Long> longValues = (List) entry.getValue();
						List<String> stringValues = longValues.stream()
								.map(s -> String.valueOf(s))
								.collect(Collectors.toList());
						result.put(entry.getKey().toString(), String.join(",", stringValues));
					}
				}
			}
		}

		return result;
	}

	/**
	 * Converts the existing object into a Json String
	 *
	 * @return String representing all model data
	 */
	@JsonIgnore
	public String toJsonString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
			return mapper.writeValueAsString(this.toMap(false));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts the existing object into a JSONObject
	 *
	 * @return JSONObject representing all model data
	 */
	@JsonIgnore
	public JSONObject toJSONObject() {
		try {
			return new JSONObject(toJsonString());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
