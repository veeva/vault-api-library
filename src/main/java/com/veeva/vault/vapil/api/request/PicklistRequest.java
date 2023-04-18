/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.veeva.vault.vapil.api.model.response.PicklistResponse;
import com.veeva.vault.vapil.api.model.response.PicklistValueResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Picklist requests, including retrieval of Picklist meta-data
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#picklists">https://developer.veevavault.com/api/23.1/#picklists</a>
 */
public class PicklistRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_PICKLISTS = "/objects/picklists";
	private static final String URL_PICKLIST_NAME = "/objects/picklists/{picklist_name}";
	private static final String URL_PICKLIST_NAME_VALUE = "/objects/picklists/{picklist_name}/{picklist_value_name}";

	private PicklistRequest() {
	}

	/**
	 * Retrieve All Picklists
	 * <br>
	 * Get a list of all metadata components and their properties.
	 * Note that the Vault API returns only the active picklist values.
	 * Reference "metadata" endpoint for inactive picklists.
	 *
	 * @return PicklistResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/picklists</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-picklists' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-picklists</a>
	 * @vapil.request <pre>
	 * PicklistResponse resp = vaultClient.newRequest(PicklistRequest.class).retrieveAllPicklists();</pre>
	 * @vapil.response <pre>
	 * for (PicklistResponse.Picklist : resp.getPicklists()) {
	 *   System.out.println("\n" + plist.getName());
	 *   System.out.println(plist.getLabel());
	 *   System.out.println(plist.getKind());
	 *
	 *   if (plist.getUsedIn() != null) {
	 *     for (PicklistResponse.Picklist.UsedIn ui : plist.getUsedIn())
	 *       System.out.println("Used In " + ui.getObjectName() + " " + ui.getPropertyName());
	 *   }
	 * }</pre>
	 */
	public PicklistResponse retrieveAllPicklists() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PICKLISTS));

		return send(HttpMethod.GET, request, PicklistResponse.class);
	}

	/**
	 * Retrieve Picklist Values
	 * <br>
	 * Retrieve picklist values for a single picklist.
	 * Note that the Vault API returns only the active picklist values.
	 * Reference "metadata" endpoint for inactive picklists.
	 *
	 * @param picklistName The picklist name to retrieve
	 * @return PicklistValueResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/picklists/{picklist_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-picklist-values' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-picklist-values</a>
	 * @vapil.request <pre>
	 * PicklistValueResponse resp = vaultClient.newRequest(PicklistRequest.class).retrievePicklistValues(picklistName);</pre>
	 * @vapil.response <pre>
	 * for (PicklistValueResponse.PicklistValue value : resp.getPicklistValues())
	 *   System.out.println(value.getName() + " = " + value.getLabel());
	 * }</pre>
	 */
	public PicklistValueResponse retrievePicklistValues(String picklistName) {
		String url = vaultClient.getAPIEndpoint(URL_PICKLIST_NAME).replace("{picklist_name}", picklistName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, PicklistValueResponse.class);
	}

	/**
	 * Create Picklist Values
	 *
	 * @param picklistName The picklist name field value
	 * @param picklists    List of picklist values (list is used for ordering)
	 * @return PicklistValueResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/picklists/{picklist_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-picklist-values' target='_blank'>https://developer.veevavault.com/api/23.1/#create-picklist-values</a>
	 * @vapil.request <pre>
	 * PicklistValueResponse resp = vaultClient.newRequest(PicklistRequest.class).createPicklistValues(picklistName, newPicklistValues);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public PicklistValueResponse createPicklistValues(String picklistName, List<String> picklists) {
		Map<String, String> mapPicklists = new HashMap<String, String>();
		int i = 1;
		for (String s : picklists) {
			mapPicklists.put("value_" + i, s);
			i++;
		}
		return picklistValueLabelHandler(HttpMethod.POST, picklistName, mapPicklists);
	}


	/**
	 * Change a picklist value label (only)
	 * <br>
	 * Use caution when editing picklist labels or names. When these attributes
	 * are changed, they affect all existing document and object metadata that
	 * refer to the picklist. For users in the UI who are accustomed to seeing
	 * a particular selection, the changes may cause confusion. This may also
	 * break existing integrations that access picklist values via the API.
	 *
	 * @param picklistName The picklist name field value
	 * @param mapPicklists Map  of new picklist values
	 * @return PicklistValueResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/picklists/{picklist_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-picklist-value-label' target='_blank'>https://developer.veevavault.com/api/23.1/#update-picklist-value-label</a>
	 * @vapil.request <pre>
	 * PicklistValueResponse resp = vaultClient.newRequest(PicklistRequest.class).updatePicklistValueLabel(picklistName, newPicklistValues);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public PicklistValueResponse updatePicklistValueLabel(String picklistName, Map<String, String> mapPicklists) {
		return picklistValueLabelHandler(HttpMethod.PUT, picklistName, mapPicklists);
	}

	private PicklistValueResponse picklistValueLabelHandler(HttpMethod httpMethod, String picklistName, Map<String, String> picklists) {
		String url = vaultClient.getAPIEndpoint(URL_PICKLIST_NAME).replace("{picklist_name}", picklistName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		for (String name : picklists.keySet())
			request.addBodyParam(name, picklists.get(name));

		return send(httpMethod, request, PicklistValueResponse.class);
	}

	/**
	 * Update Picklist Value - Name
	 * <br>
	 * Change a picklist value name. Use caution when editing picklist
	 * value names. When you change a picklist value name, it may affect existing
	 * document and object metadata that refer to the picklist. This may also break
	 * existing integrations that access picklist values via the API.
	 *
	 * @param picklistName      The picklist name field value (license_type__v, product_family__c, region__c, etc.)
	 * @param picklistValueName The picklist value name field value (north_america__c, south_america__c, etc.)
	 * @param newName           The new name for a picklist value. This does not affect the label.
	 *                          Vault adds __c after processing. Special characters and double underscores __ are not allowed.
	 * @return VaultResponse for ResponseStatus SUCCESS/FAILURE
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/picklists/{picklist_name}/{picklist_value_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-picklist-value' target='_blank'>https://developer.veevavault.com/api/23.1/#update-picklist-value</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(PicklistRequest.class).updatePicklistValue(picklistName, picklistValueName, status);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public VaultResponse updatePicklistValue(String picklistName, String picklistValueName, String newName) {
		return updatePicklistValueHandler(picklistName, picklistValueName, "name", newName);
	}

	/**
	 * Update Picklist Value - Status
	 * <br>
	 * Change a picklist value status. Use caution when editing picklist
	 * value names. When you change a picklist value name, it may affect existing
	 * document and object metadata that refer to the picklist. This may also break
	 * existing integrations that access picklist values via the API.
	 *
	 * @param picklistName      The picklist name field value (license_type__v, product_family__c, region__c, etc.)
	 * @param picklistValueName The picklist value name field value (north_america__c, south_america__c, etc.)
	 * @param status            The new status for a picklist value. True for active, False for inactive.
	 * @return VaultResponse for ResponseStatus SUCCESS/FAILURE
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/picklists/{picklist_name}/{picklist_value_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-picklist-value' target='_blank'>https://developer.veevavault.com/api/23.1/#update-picklist-value</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(PicklistRequest.class).updatePicklistValue(picklistName, picklistValueName, newName);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public VaultResponse updatePicklistValue(String picklistName, String picklistValueName, boolean status) {
		String value = "inactive";
		if (status) value = "active";

		return updatePicklistValueHandler(picklistName, picklistValueName, "status", value);
	}

	private VaultResponse updatePicklistValueHandler(String picklistName, String picklistValueName, String key, String value) {
		String url = vaultClient.getAPIEndpoint(URL_PICKLIST_NAME_VALUE);
		url = url.replace("{picklist_name}", picklistName);
		url = url.replace("{picklist_value_name}", picklistValueName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam(key, value);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * Inactivate Picklist Value
	 * <br>
	 * If you need to inactivate a picklist value, it is best practice to use Update Picklist Value.
	 *
	 * @param picklistName      The picklist name field value (license_type__v, product_family__c, region__c, etc.)
	 * @param picklistValueName The picklist value name field value (north_america__c, south_america__c, etc.)
	 * @return VaultResponse for ResponseStatus SUCCESS/FAILURE
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/picklists/{picklist_name}/{picklist_value_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#inactivate-picklist-value' target='_blank'>https://developer.veevavault.com/api/23.1/#inactivate-picklist-value</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(PicklistRequest.class).inactivatePicklistValue(picklistName, picklistValueName);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public VaultResponse inactivatePicklistValue(String picklistName, String picklistValueName) {
		String url = vaultClient.getAPIEndpoint(URL_PICKLIST_NAME_VALUE);
		url = url.replace("{picklist_name}", picklistName);
		url = url.replace("{picklist_value_name}", picklistValueName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}


}
