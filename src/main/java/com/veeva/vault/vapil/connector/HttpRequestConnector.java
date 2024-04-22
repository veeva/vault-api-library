/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.connector;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * HTTP request class, whether to the Vault API or other servers/APIs.
 * This is a generic set of methods with no Vault specific information.
 * It is used internally by VAPIL for all HTTP requests.
 * It can also be used
 * for integration specific HTTP calls (non-Vault).
 */
public class HttpRequestConnector {

	// Standard constants for all requests. 
	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HTTP_HEADER_ACCEPT = "Accept";
	public static final String HTTP_HEADER_CONTENT_MD5 = "Content-MD5";
	public static final String HTTP_HEADER_RANGE = "Range";
	public static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
	public static final String HTTP_CONTENT_TYPE_CSV = "text/csv";
	public static final String HTTP_CONTENT_TYPE_JSON = "application/json";
	public static final String HTTP_CONTENT_TYPE_SCIM_JSON = "application/scim+json";
	public static final String HTTP_CONTENT_TYPE_OCTET = "application/octet-stream";
	public static final String HTTP_CONTENT_TYPE_PLAINTEXT = "text/plain";
	public static final String HTTP_CONTENT_TYPE_XFORM = "application/x-www-form-urlencoded";
	public static final String HTTP_CONTENT_TYPE_MULTIPART_FORM = "multipart/form-data";
	public static final String HTTP_CONTENT_TYPE_MULTIPART_FORM_BOUNDARY = "multipart/form-data; boundary=";

	private static Logger log = LoggerFactory.getLogger(HttpRequestConnector.class);

	private static int globalTimeout = 60;

	// Declare static to ensure single instance
	private static OkHttpClient clientInstance = null;

	private static OkHttpClient buildClient() {
		return new OkHttpClient().newBuilder()
				.callTimeout(globalTimeout, TimeUnit.MINUTES)
				.connectTimeout(globalTimeout, TimeUnit.MINUTES)
				.writeTimeout(globalTimeout, TimeUnit.MINUTES)
				.readTimeout(globalTimeout, TimeUnit.MINUTES)
				.build();
	}

	private static OkHttpClient getClient() {
		if (clientInstance == null) {
			clientInstance = buildClient();
		}
		return clientInstance;
	}

	// Parameters of the HTTP request
	private String url = null;
	private Map<String, Object> queryParams = null;
	private Map<String, Object> headerParams = null;
	private Map<String, Object> bodyParams = null;

	/*
	 * Optional parameters for the request body
	 */
	RequestOption requestOption;
	private String requestMediaType = null; // Mediatype for the requested parameter
	private byte[] requestBinaryContent = null; // Request binary content
	private String requestFile = null; // Request with only a file
	private String requestRawString = null; // Request with only a string

	/**
	 * Constructor for an HTTP request to a given URL
	 *
	 * @param url Endpoint for the HTTP call
	 */
	public HttpRequestConnector(String url) {
		this.url = url;
		this.requestOption = RequestOption.EMPTY;

	}

	/**
	 * Perform an HTTP call based on the class properties
	 * (url, queryParams, headerParams, bodyParams)
	 * for the provided HTTP Method. Responses are returned
	 * as a string.
	 *
	 * @param method The HttpMethod for the request (GET, POST, PUT, DELETE)
	 * @return The HTTP response from the request, returned as a string
	 */
	public HttpResponseConnector send(HttpMethod method) {
		return send(method, HttpRequestConnector.ResponseOption.STRING);
	}

	/**
	 * Perform an HTTP call based on the class properties
	 * (url, queryParams, headerParams, bodyParams)
	 * for the provided HTTP Method. Responses are returned
	 * based on the provided response option input.
	 *
	 * @param method         The HttpMethod for the request (GET, POST, PUT, DELETE)
	 * @param responseOption The format to return the HTTP response
	 * @return The HTTP response from the request, returned in format as specified by responseOption
	 */
	public HttpResponseConnector send(HttpMethod method, HttpRequestConnector.ResponseOption responseOption) {
		return send(method, responseOption, null);
	}

	/**
	 * Perform an HTTP call based on the class properties
	 * (url, queryParams, headerParams, bodyParams)
	 * for the provided HTTP Method. Responses are returned
	 * based on the provided response option input.
	 * An output file is optionally written for streamed data.
	 *
	 * @param method         The HttpMethod for the request (GET, POST, PUT, DELETE)
	 * @param responseOption The format to return the HTTP response
	 * @param outputFilePath Optional output file to write, pass null to ignore
	 * @return The HTTP response from the request, returned in format as specific by responseOption
	 */
	public HttpResponseConnector send(HttpMethod method, HttpRequestConnector.ResponseOption responseOption, String outputFilePath) {
		HttpResponseConnector response = null;

		Request.Builder requestBuilder = getRequestBuilder(method);
		Request request = requestBuilder.build();

		log.info("Sending " + request.method() + " to " + request.url());

		Call call = getClient().newCall(request);

		try {
			Response clientResponse = call.execute();
			response = new HttpResponseConnector(responseOption);
			response.processResponse(clientResponse, outputFilePath);

			clientResponse.close();

			log.debug("HTTP status code = " + response.getStatusCode());

			if (response.getStatusCode() > 299) {
				log.error("Error status code = " + response.getStatusCode());

			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return response;
	}

	/**
	 * Perform an HTTP GET with default response processing (as a string)
	 *
	 * @return The HTTP response from the request, returned as a string
	 */
	public HttpResponseConnector sendGet() {
		return send(HttpMethod.GET);
	}

	/**
	 * Perform an HTTP GET while specifying the response processing option
	 *
	 * @param responseOption Option for processing the response as string, inputstream, byte array, etc.
	 * @return The HTTP response from the request, returned in format as specified by responseOption
	 */
	public HttpResponseConnector sendGet(ResponseOption responseOption) {
		return send(HttpMethod.GET, responseOption);
	}

	/**
	 * Perform an HTTP GET and writing the response to the specified file.
	 *
	 * @param outputFilePath The output file path to write to
	 * @return The HTTP response from the request
	 */
	public HttpResponseConnector sendGet(String outputFilePath) {
		return send(HttpMethod.GET, ResponseOption.TO_FILE, outputFilePath);
	}

	/**
	 * Perform an HTTP POST
	 *
	 * @return The HTTP response from the request
	 */
	public HttpResponseConnector sendPost() {
		return send(HttpMethod.POST);
	}

	/**
	 * Perform an HTTP PUT
	 *
	 * @return The HTTP response from the request
	 */
	public HttpResponseConnector sendPut() {
		return send(HttpMethod.PUT);
	}

	/**
	 * Perform an HTTP DELETE
	 *
	 * @return The HTTP response from the request
	 */
	public HttpResponseConnector sendDelete() {
		return send(HttpMethod.DELETE);
	}

	/**
	 * Helper method to provide consistent and repeatable method
	 * for creating the RequestBuilder based on class properties
	 * for the URL, header params, request params, and body.
	 *
	 * @param method Http Method (GET, POST, PUT, DELETE)
	 * @return The instantiated request builder
	 */
	private Request.Builder getRequestBuilder(HttpMethod method) {

		Request.Builder requestBuilder = new Request.Builder()
				.url(getUrl(url, queryParams));

		switch (method) {
			case GET:
				requestBuilder.get();
				break;
			case POST:
				requestBuilder.post(getRequestBody());
				break;
			case PUT:
				requestBuilder.put(getRequestBody());
				break;
			case DELETE:
				RequestBody requestBody = getRequestBody();
				if (requestBody == null)
					requestBuilder.delete();
				else
					requestBuilder.delete(requestBody);
				break;
			default:
				log.error("Unsupported HTTP method");
		}

		// Add header parameters
		if (headerParams != null && !headerParams.isEmpty()) {
			for (String key : headerParams.keySet())
				requestBuilder.addHeader(key, headerParams.get(key).toString());
		}

		return requestBuilder;
	}

	/**
	 * Form the request body based on the set request option
	 * and set properties of the class
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBody() {

		// First get the request body if request option is set				
		switch (requestOption) {
			case FILE:
				return getRequestBodyFile();
			case MULTIPART:
				return getRequestBodyMultiPart();
			case BYTE_ARRAY:
				return getRequestBodyBinary();
			case STRING:
				return getRequestBodyString();
			default:
				break;
		}

		if (bodyParams != null && !bodyParams.isEmpty()) {
			// Create body based on request name/value pairs (no multipart)
			return getRequestBodyForm();
		} else {
			log.debug("RequestBody: None");
			return RequestBody.create("", null);
		}
	}

	/**
	 * Form the request body with body name/value pairs
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBodyForm() {
		log.debug("RequestBody: Form");

		FormBody.Builder formBody = new FormBody.Builder();

		if (bodyParams != null && !bodyParams.isEmpty()) {
			for (String key : bodyParams.keySet()) {
				if (bodyParams.get(key) != null)
					formBody.add(key, bodyParams.get(key).toString());
				else
					formBody.add(key, "");
			}
		}

		return formBody.build();
	}

	/**
	 * Form the request body for multipart/form-data requests, including
	 * requests with a file
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBodyMultiPart() {
		log.debug("RequestBody: Multipart");

		MultipartBody.Builder formBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
				if (entry.getValue() instanceof Path) {
					// File
					Path path = (Path) entry.getValue();

					formBody.addFormDataPart(entry.getKey(), path.getFileName().toString(),
							RequestBody.create(path.toFile(), MediaType.parse(HTTP_CONTENT_TYPE_OCTET)));
				} else if (entry.getValue() instanceof BinaryFile) {
					BinaryFile bf = (BinaryFile) entry.getValue();

					formBody.addFormDataPart(entry.getKey(), bf.getFileName(),
							RequestBody.create(bf.getBinaryContent(), MediaType.parse(HTTP_CONTENT_TYPE_OCTET)));
				} else {
					// Standard name/value pair
					formBody.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
				}
			}
		}

		return formBody.build();
	}

	/**
	 * Form the request body with file request
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBodyFile() {
		log.debug("RequestBody: File");

		return RequestBody.create(new File(requestFile), MediaType.parse(requestMediaType));
	}

	/**
	 * Form the request body with a raw string
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBodyString() {
		log.debug("RequestBody: String");

		return RequestBody.create(requestRawString, MediaType.parse(requestMediaType));
	}

	/**
	 * Form the request body with binary data
	 *
	 * @return The created RequestBody
	 */
	private RequestBody getRequestBodyBinary() {
		log.debug("RequestBody: Binary");

		return RequestBody.create(requestBinaryContent, MediaType.parse(requestMediaType));
	}

	/**
	 * Form a URL with request params. The request param
	 * values are encoding for sending.
	 *
	 * @param url         Endpoint for the HTTP call
	 * @param queryParams (Optional) Request parameters to add to the URL string. Pass null if none.
	 * @return Formed URL with request params
	 */
	protected String getUrl(String url, Map<String, Object> queryParams) {

		if (queryParams != null && !queryParams.isEmpty()) {
			Map<String, Object> encodedMap = encodeMap(queryParams);

			String s = encodedMap.keySet().stream().map(key -> key + "=" + encodedMap.get(key))
					.collect(Collectors.joining("&"));

			url += "?" + s;
		}

		return url;
	}

	/**
	 * Encode a string
	 *
	 * @param value Input to be encoded
	 * @return Encoded value
	 * @throws UnsupportedEncodingException unsupported encoding
	 */
	private String encodeValue(Object value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.toString());
	}

	/**
	 * Encode the value in a map of name/value pairs
	 *
	 * @param map Name/Value pairs to encode
	 * @return Encoded map
	 */
	protected Map<String, Object> encodeMap(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			for (String key : map.keySet()) {
				try {
					map.put(key, encodeValue(map.get(key)));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public Map<String, Object> getHeaderParams() {
		return headerParams;
	}

	public Map<String, Object> getBodyParams() {
		return bodyParams;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	public void setHeaderParams(Map<String, Object> headerParams) {
		this.headerParams = headerParams;
	}

	public void setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
	}

	/**
	 * Add a query name/value pair to the request
	 *
	 * @param name  The parameter name
	 * @param value The parameter value
	 */
	public void addQueryParam(String name, Object value) {
		addParam(ParamType.QUERY, name, value);
	}

	/**
	 * Add a header name/value pair to the request
	 *
	 * @param name  The parameter name
	 * @param value The parameter value
	 */
	public void addHeaderParam(String name, Object value) {
		addParam(ParamType.HEADER, name, value);
	}

	/**
	 * Add a body name/value pair to the request
	 *
	 * @param name  The body parameter name
	 * @param value The body parameter value
	 */
	public void addBodyParam(String name, Object value) {
		addParam(ParamType.BODY, name, value);
	}

	/**
	 * Add a body name/value pair to the request
	 *
	 * @param name  The body parameter name
	 * @param value The body parameter value
	 */
	public void addBodyParamMultiPart(String name, Object value) {
		requestOption = RequestOption.MULTIPART;
		addParam(ParamType.BODY, name, value);
	}

	/**
	 * Add a file to the request, such as "--data-binary" POST request,
	 * in which the file is the only entity in the post request.
	 *
	 * @param mediaType The media type of the file
	 * @param filePath  Absolute file path of file to stream in the request
	 */
	public void addFile(String mediaType, String filePath) {
		requestOption = RequestOption.FILE;
		requestMediaType = mediaType;
		requestFile = filePath;
	}

	/**
	 * Add a string to the request, such as POST of raw data
	 *
	 * @param mediaType The media type of the raw string
	 * @param rawString The data as a string
	 */
	public void addRawString(String mediaType, String rawString) {
		requestOption = RequestOption.STRING;
		requestMediaType = mediaType;
		requestRawString = rawString;
	}

	/**
	 * Add a file in multipart/form-data request.
	 *
	 * @param mediaType     The media type of the raw string
	 * @param binaryContent Absolute file path of file to upload
	 */
	public void addBinary(String mediaType, byte[] binaryContent) {
		requestOption = RequestOption.BYTE_ARRAY;
		requestMediaType = mediaType;
		requestBinaryContent = binaryContent;
	}

	/**
	 * Add a file in multipart/form-data request.
	 *
	 * @param name     The name of the body param in the multipart/form-data request
	 * @param filePath Absolute file path of file to upload
	 */
	public void addFileMultiPart(String name, String filePath) {
		requestOption = RequestOption.MULTIPART;
		addBodyParam(name, Paths.get(filePath));
	}

	/**
	 * Add a binary content of a file in multipart/form-data request.
	 *
	 * @param name          The name of the body param in the multipart/form-data request
	 * @param binaryContent Binary content of the file
	 * @param fileName      The name of the file for receiving server to create
	 */
	public void addFileBinary(String name, byte[] binaryContent, String fileName) {
		requestOption = RequestOption.MULTIPART;
		addBodyParam(name, new BinaryFile(fileName, binaryContent));
	}

	private void addParam(ParamType paramType, String name, Object value) {
		switch (paramType) {
			case QUERY:
				if (queryParams == null) queryParams = new HashMap<String, Object>();
				queryParams.put(name, value);
				break;
			case HEADER:
				if (headerParams == null) headerParams = new HashMap<String, Object>();
				headerParams.put(name, value);
				break;
			case BODY:
				if (bodyParams == null) bodyParams = new HashMap<String, Object>();
				bodyParams.put(name, value);
				break;
		}
	}

	public enum ParamType {
		QUERY, HEADER, BODY
	}

	public enum HttpMethod {
		GET, POST, PUT, DELETE
	}

	/**
	 * Options for processing the HTTP response
	 */
	public enum ResponseOption {
		/**
		 * Handle response as a string,
		 * such as a JSON response or
		 * small text file
		 */
		STRING,
		/**
		 * Write response to a file
		 */
		TO_FILE,
		/**
		 * Receive the response as a byte[]
		 */
		BYTE_ARRAY
	}

	/**
	 * Options for the RequestBody, set by specific
	 * methods as the request is built. Used to ensure
	 * the proper request body is formed based on the
	 * variables set in the request.
	 */
	private enum RequestOption {
		EMPTY,
		/**
		 * Form the request with name/value pairs in a form
		 */
		FORM,
		/**
		 * Form the request body as a string
		 */
		STRING,
		/**
		 * Form the request body as a string
		 */
		BYTE_ARRAY,
		/**
		 * Form the request body with a file
		 */
		FILE,
		/**
		 * Form the request body with a multipart form
		 * request, such as body params and a file
		 */
		MULTIPART
	}

	/**
	 * Handler for streaming a binary file in a multi-part
	 * request. Wrapper class to house the name of the file
	 * and the file's binary content
	 */
	public static class BinaryFile {

		private String fileName;
		private byte[] binaryContent;

		public BinaryFile(String fileName, byte[] binaryContent) {
			this.fileName = fileName;
			this.binaryContent = binaryContent;
		}

		public String getFileName() {
			return fileName;
		}

		public byte[] getBinaryContent() {
			return binaryContent;
		}
	}

	/**
	 * Set the global timeout for the HTTP Client. Default = 60 minutes.
	 * <p>&nbsp;</p>
	 * Can only be set before any and all HTTP calls are first executed
	 *
	 * @param minutes Number of minutes before http timeout occurs
	 */
	public static void setGlobalTimeout(int minutes) {
		log.info("Setting http timeout = "  + minutes);
		globalTimeout = minutes;
	}
}
