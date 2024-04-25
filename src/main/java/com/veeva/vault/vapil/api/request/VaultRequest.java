/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse.APIResponseError;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpResponseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Class for performing HTTP requests to the Vault API. This class
 * is extended by the specific request class that maps to a Vault
 * API endpoint. It cannot be instantiated directly.
 */
public abstract class VaultRequest<T extends VaultRequest<T>> {

	/**
	 * HTTP header value for the Vault Client Id
	 * Value = {@value #HTTP_HEADER_VAULT_CLIENT_ID}
	 */

	/**
	 * HTTP header value for the Vault session
	 * Value = {@value #HTTP_HEADER_AUTHORIZATION}
	 */
	public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

	public static final String HTTP_HEADER_VAULT_CLIENT_ID = "X-VaultAPI-ClientID";
	public static final String HTTP_HEADER_REFERENCE_ID = "X-VaultAPI-ReferenceId";
	protected String referenceId;

	private static Logger log = LoggerFactory.getLogger(VaultRequest.class);

	/**
	 * The Vault client, containing domain, authentication, and session information.
	 * All requests are initiated via the VaultClient.
	 */
	protected VaultClient vaultClient;

	/**
	 * Default constructor for the abstract VaultRequest class.
	 * Extended/Instantiated via the sub-class.
	 */
	protected VaultRequest() {
	}


	/**
	 * Perform an HTTP request after setting standard Vault information
	 * such as the session id. This method is a wrapper to the HttpRequestConnector
	 * class to provide central area for setting information and
	 * processing the response.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T send(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, Class<T> responseObjectClass) {
		return send(method, request, getBaseObjectMapper(), responseObjectClass);
	}

	/**
	 * Perform an HTTP request after setting standard Vault information
	 * such as the session id. This method is a wrapper to the HttpRequestConnector
	 * class to provide central area for setting information and processing
	 * the response.
	 * <p>
	 * A passed object mapper allows flexibility for the calling
	 * method to set deserialization rules when processing the response.
	 * User this method to have greater control of the JSON deserialization.
	 * For example, set a deserialization rule to treat a single JSON object
	 * as a JSON array.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param objectMapper        Deserialization object mapper to POJO
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T send(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, ObjectMapper objectMapper, Class<T> responseObjectClass) {
		request = setVaultHeaderParams(request);

		HttpResponseConnector response = request.send(method);
		log.debug("HTTP response before deserialization = " + response.getResponse());

		return processResponse(response, HttpRequestConnector.ResponseOption.STRING, objectMapper, responseObjectClass);
	}

	/**
	 * Perform an HTTP request that creates a file. The request will contain
	 * standard Vault information such as the session id. This method is a
	 * wrapper to the HttpRequestConnector class to provide central area
	 * for setting information and processing the response.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param outputFilePath      file path for the output file
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T sendToFile(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, String outputFilePath, Class<T> responseObjectClass) {
		request = setVaultHeaderParams(request);
		HttpResponseConnector response = request.send(method, HttpRequestConnector.ResponseOption.TO_FILE, outputFilePath);

		return processResponse(response, HttpRequestConnector.ResponseOption.TO_FILE, getBaseObjectMapper(), responseObjectClass);
	}

	/**
	 * Perform an HTTP request that creates a file. The request will contain
	 * standard Vault information such as the session id. This method is a
	 * wrapper to the HttpRequestConnector class to provide central area
	 * for setting information and processing the response.
	 * <p>
	 * A passed object mapper allows flexibility for the calling
	 * method to set deserialization rules when processing the response.
	 * User this method to have greater control of the JSON deserialization.
	 * For example, set a deserialization rule to treat a single JSON object
	 * as a JSON array.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param objectMapper        Deserialization object mapper to POJO
	 * @param outputFilePath      file path for the output file
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T sendToFile(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, ObjectMapper objectMapper, String outputFilePath, Class<T> responseObjectClass) {
		request = setVaultHeaderParams(request);
		HttpResponseConnector response = request.send(method, HttpRequestConnector.ResponseOption.TO_FILE, outputFilePath);

		return processResponse(response, HttpRequestConnector.ResponseOption.TO_FILE, objectMapper, responseObjectClass);
	}

	/**
	 * Perform an HTTP request that creates a file. The request will contain
	 * standard Vault information such as the session id. This method is a
	 * wrapper to the HttpRequestConnector class to provide central area
	 * for setting information and processing the response.
	 * <p>
	 * A passed object mapper allows flexibility for the calling
	 * method to set deserialization rules when processing the response.
	 * User this method to have greater control of the JSON deserialization.
	 * For example, set a deserialization rule to treat a single JSON object
	 * as a JSON array.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param objectMapper        Deserialization object mapper to POJO
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T sendReturnBinary(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, ObjectMapper objectMapper, Class<T> responseObjectClass) {
		request = setVaultHeaderParams(request);
		HttpResponseConnector response = request.send(method, HttpRequestConnector.ResponseOption.BYTE_ARRAY);

		return processResponse(response, HttpRequestConnector.ResponseOption.BYTE_ARRAY, objectMapper, responseObjectClass);
	}

	/**
	 * Perform an HTTP request that creates a file. The request will contain
	 * standard Vault information such as the session id. This method is a
	 * wrapper to the HttpRequestConnector class to provide central area
	 * for setting information and processing the response.
	 *
	 * @param <T>                 The object
	 * @param method              HTTP Method (GET, POST, PUT, DELETE)
	 * @param request             Fully formed HTTP request
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response based on a response string
	 */
	protected <T> T sendReturnBinary(HttpRequestConnector.HttpMethod method, HttpRequestConnector request, Class<T> responseObjectClass) {
		request = setVaultHeaderParams(request);
		HttpResponseConnector response = request.send(method, HttpRequestConnector.ResponseOption.BYTE_ARRAY);

		return processResponse(response, HttpRequestConnector.ResponseOption.BYTE_ARRAY, getBaseObjectMapper(), responseObjectClass);
	}

	/**
	 * Deserialize the JSON response from the HTTP request to Java object.
	 * An object mapper defines the deserialization rules.
	 *
	 * @param <T>                 The object
	 * @param response            The response
	 * @param responseOption      The option
	 * @param objectMapper        Deserialization object mapper to POJO
	 * @param responseObjectClass POJO class representing the deserialized JSON response
	 * @return POJO modeled response
	 */
	private <T> T processResponse(HttpResponseConnector response, HttpRequestConnector.ResponseOption responseOption, ObjectMapper objectMapper, Class<T> responseObjectClass) {
		T obj = null;

		log.debug("responseOption = " + responseOption);

		// Instantiate the object based on the type of response 
		switch (responseOption) {
			case BYTE_ARRAY:
			case TO_FILE:
				// This is going to be VaultResponse but use the variable just in case			
				try {
					if (response.getStatusCode() == 200) {
						//if the contentType is JSON, it means there is an inner JSON error returned by Vault API
						if ((response.getContentType() != null)
								&& (response.getContentType().contains(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON))) {

							obj = (T) VaultResponse.class.newInstance();

							((VaultResponse) obj).setResponseStatus(VaultResponse.HTTP_RESPONSE_FAILURE);

							byte[] errorContent = null;

							if (responseOption == HttpRequestConnector.ResponseOption.BYTE_ARRAY) {
								errorContent = response.getByteArray();
							} else {
								File file = new File(response.getOutputFilePath());
								if (file.exists()) {
									errorContent = Files.readAllBytes(file.toPath());
								}
							}

							obj = objectMapper.readValue(new String(errorContent, StandardCharsets.UTF_8), responseObjectClass);
							((VaultResponse) obj).setResponse(new String(errorContent, StandardCharsets.UTF_8));

						} else {
							obj = responseObjectClass.newInstance();
							((VaultResponse) obj).setResponseStatus(VaultResponse.HTTP_RESPONSE_SUCCESS);
						}
					} else {
						obj = responseObjectClass.newInstance();
						((VaultResponse) obj).setResponseStatus(VaultResponse.HTTP_RESPONSE_FAILURE);
					}

				} catch (InstantiationException | IllegalAccessException | IOException e1) {
					log.error(e1.getMessage());
				}


				break;
			case STRING:
				// JSON response, initialize the object by deserializing the response
				try {
					obj = objectMapper.readValue(response.getResponse(), responseObjectClass);
				} catch (IOException e) {
					log.error(e.getMessage());
				}

				break;
		}

		// Set response specific properties	
		switch (responseOption) {
			case BYTE_ARRAY:
				((VaultResponse) obj).setBinaryContent(response.getByteArray());
				break;
			case TO_FILE:
				((VaultResponse) obj).setOutputFilePath(response.getOutputFilePath());
				break;
			case STRING:
				// Set the full response
				((VaultResponse) obj).setResponse(response.getResponse());
			default:
				break;
		}

		// Set additional properties for the VaultResponse class
		((VaultResponse) obj).setHeaders(response.getHeaders());

		if (((VaultResponse) obj).hasErrors() && vaultClient.isLogApiErrorsEnabled()) {
			//this only logs errors at the top level. any record level errors are not logged
			List<APIResponseError> errors = ((VaultResponse) obj).getErrors();
			if (errors != null) {
				for (APIResponseError error : ((VaultResponse) obj).getErrors()) {
					log.error("Vault Exception " + error.getType() + " " + error.getMessage());
				}
			}
		}

		return obj;
	}

	/**
	 * Base object mapper for common mapping rules of Vault requests.
	 * Creates an object mapper with common properties. Allows additional
	 * properties to be enabled/disabled before sending the HTTP request.
	 *
	 * @return Base object mapper
	 */
	public ObjectMapper getBaseObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		return objectMapper;
	}

	/**
	 * Set the HTTP header with standard Vault header parameters
	 *
	 * @param request The HTTP request
	 * @return The HTTP request with added Vault header parameters
	 */
	private HttpRequestConnector setVaultHeaderParams(HttpRequestConnector request) {
		// Add the Vault session id if it exists
		if (vaultClient.hasSessionId())
			request.addHeaderParam(HTTP_HEADER_AUTHORIZATION, vaultClient.getSessionId());

		// Add the client id 
		String vaultClientId = vaultClient.getVaultClientId();

		if (vaultClientId != null)
			request.addHeaderParam(HTTP_HEADER_VAULT_CLIENT_ID, vaultClientId);

		if (referenceId != null && !referenceId.isEmpty())
			request.addHeaderParam(HTTP_HEADER_REFERENCE_ID, referenceId);

		return request;
	}

	/**
	 * Set the VaultClient for Vault domain and connection information
	 *
	 * @param vaultClient The VaultClient
	 */
	public void setVaultClient(VaultClient vaultClient) {
		this.vaultClient = vaultClient;
	}

	/**
	 * Abstract method to set the reference id header for the Vault API request.
	 * This method is implemented for all Request classes. When set in the request, the
	 * Reference ID is returned in the response headers of the returned Response class.
	 *
	 * @param referenceId The reference id
	 */
	public T setHeaderReferenceId(String referenceId) {
		this.referenceId = referenceId;
		return (T) this;
	}
}
