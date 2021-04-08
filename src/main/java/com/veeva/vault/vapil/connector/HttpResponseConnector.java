package com.veeva.vault.vapil.connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * HTTP response class, instantiated via the HttpRequestConnector response.
 * This class provides methods for accessing the response status code, headers,
 * and the response body.
 */
public class HttpResponseConnector {

	private static Logger log = Logger.getLogger(HttpResponseConnector.class);

	// Standard properties	
	private Map<String, List<String>> headers;
	private String contentType;
	private int statusCode;
	private String statusMessage;

	private HttpRequestConnector.ResponseOption responseOption;

	private String response;
	private byte[] byteArray;

	// For returning responses written to file
	private String outputFilePath;

	/**
	 * Default constructor to retrieve response as a string
	 */
	public HttpResponseConnector() {
		this(HttpRequestConnector.ResponseOption.STRING);
	}

	/**
	 * Constructor to set specific response option
	 * (String, write to file, byte array)
	 *
	 * @param responseOption Option for processing the response
	 */
	public HttpResponseConnector(HttpRequestConnector.ResponseOption responseOption) {
		this.responseOption = responseOption;
	}

	/**
	 * Process the HTTP response
	 *
	 * @param httpResponse The HTTP response from the request
	 */
	public void processResponse(Response httpResponse) {
		processResponse(httpResponse, null);
	}

	/**
	 * Process the HTTP response and optionally write to a file
	 *
	 * @param httpResponse   The HTTP response from the request
	 * @param outputFilePath Destination for writing files
	 */
	public void processResponse(Response httpResponse, String outputFilePath) {

		this.outputFilePath = outputFilePath;
		statusCode = httpResponse.code();
		statusMessage = httpResponse.message();
		setHeaders(httpResponse);

		ResponseBody responseBody = httpResponse.body();
		if ((responseBody != null) && (responseBody.contentType() != null)) {
			contentType = responseBody.contentType().toString();
		}

		try {
			switch (responseOption) {
				case STRING:
					response = responseBody.string();
					break;
				case TO_FILE:
					writeToFile(responseBody, outputFilePath);
					break;
				case BYTE_ARRAY:
					byteArray = responseBody.bytes();
					break;
				default:
					response = responseBody.string();
			}
		} catch (IOException e) {
			log.error(e.toString());
		}

		responseBody.close();
	}

	/**
	 * Stream the response and write the results to a file
	 *
	 * @param responseBody   The HTTP response
	 * @param outputFilePath The destination file
	 */
	private void writeToFile(ResponseBody responseBody, String outputFilePath) {

		try {
			File file = new File(outputFilePath);
			if (file.exists()) file.delete();
			FileOutputStream output = new FileOutputStream(file);


			byte[] data = new byte[1024];
			int len = 0;

			while ((len = responseBody.byteStream().read(data)) > 0)
				output.write(data, 0, len);

			output.flush();
			output.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}


	/**
	 * @return The HTTP response headers. Single element headers,
	 * such as Content-Type, can be retrieved by getting the first
	 * element in the list.
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	/**
	 * Return first header string value of the given named header.
	 * It is up to calling code to check for multi-valued headers.
	 *
	 * @param name The header name (key) to retrieve
	 * @return The first value as a string, null if not found
	 */
	public String headerFirstValue(String name) {
		String value = null;

		if (headers.containsKey(name))
			value = headers.get(name).get(0);

		return value;
	}

	/**
	 * Form a library agnostic list of the header values returned.
	 * Allows calling class a consistent property for header information.
	 *
	 * @param httpResponse The HTTP response
	 */
	private void setHeaders(Response httpResponse) {

		headers = new HashMap<String, List<String>>();

		Headers httpHeaders = httpResponse.headers();
		for (String name : httpHeaders.names())
			headers.put(name, httpResponse.headers(name));
	}

	/**
	 * @return The HTTP response status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return The HTTP response status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @return The HTTP Content-Type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return The HTTP response as a string
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @return The HTTP response as a byte array
	 */
	public byte[] getByteArray() {
		return byteArray;
	}

	/**
	 * @return The file path of the output
	 */
	public String getOutputFilePath() {
		return outputFilePath;
	}

}
