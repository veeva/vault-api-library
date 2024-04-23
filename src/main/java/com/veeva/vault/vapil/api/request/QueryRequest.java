/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import org.apache.log4j.Logger;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * THE VQL CLASS TO END ALL VQL CLASSES
 * <p>
 * Available query methods:
 * <ul>
 * <li>{@link #query(String)} - query with pagination, use {@link #queryByPage(String)} for pagination of the results
 * </ul>
 * <p>
 * See {@link #query(String)} for example request and response methods, including reading of the resulting
 * data and handling of the X-VaultAPI-DescribeQuery parameter.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.2/#vault-query-language-vql">https://developer.veevavault.com/api/23.2/#vault-query-language-vql</a>
 */
public class QueryRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(QueryRequest.class);

	/**
	 * <b>X-VaultAPI-DescribeQuery</b> Set to true to include static field metadata in the response
	 */
	public static final String HTTP_HEADER_VAULT_DESCRIBE_QUERY = "X-VaultAPI-DescribeQuery";

	/**
	 * <b>X-VaultAPI-RecordProperties</b> If present, the response includes the record properties object.
	 */
	public static final String HTTP_HEADER_VAULT_RECORD_PROPERTIES = "X-VaultAPI-RecordProperties";

	// API Endpoints
	private static final String URL_QUERY = "/query";

	// API Request parameters
	private Boolean queryDescribe = false;
	private RecordPropertyType recordPropertyType = null;

	private QueryRequest() {
	}

	/**
	 * <b>VQL Query</b>
	 * <p>
	 * Perform a Vault query request. Subsequent queries and pagination
	 * are needed to retrieve the full result set if the total records returned exceed the "pagesize"
	 * parameter in the response. See {@link #queryByPage(String)}.
	 * <p>
	 * Returned records can be retrieved via the "getData" method in the response.
	 *
	 * @param vql The fully formed query string
	 * @return QueryResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/query</pre>
	 * @vapil.request <pre>
	 * QueryResponse response = vaultClient.newRequest(QueryRequest.class)
	 * 				.setDescribeQuery(true)
	 * 				.query(query);</pre>
	 * @vapil.response <pre>
	 * <i>Example display of query results, including sub-query results</i>
	 * List&lt;QueryResponse.QueryResult&gt; results = response.getData();
	 * System.out.println(spacer + "Total records = " + results.size());
	 *
	 * String spacer = StringUtils.leftPad("",level,"\t");	 *
	 * int i = 0;
	 *
	 * for (QueryResponse.QueryResult queryResult : results) {
	 *   i++;
	 *   System.out.println(spacer + "------- " + i + " -------");
	 *   for (String fieldName : queryResult.getFieldNames()) {
	 *     Object data = queryResult.get(fieldName);
	 *     if (data instanceof LinkedHashMap) {
	 *       QueryResponse subQuery = queryResult.getSubQuery(fieldName);
	 *       if (subQuery != null) {
	 *         System.out.println(spacer + fieldName + " = ");
	 *         // Get the subquery results using subQuery.getData()
	 *       }
	 *       else {
	 *         System.out.println(spacer + fieldName + " = null");
	 *       }
	 *     }
	 *     else {
	 *       System.out.println(spacer + fieldName + " = " + queryResult.get(fieldName));
	 *     }
	 *   }
	 * }</pre>
	 * @vapil.response <pre>
	 * <i>Example reading of pagination details</i>
	 * System.out.println("PageSize " + response.getResponseDetails().getPageSize());
	 * System.out.println("PageOffset " + response.getResponseDetails().getPageOffset());
	 * System.out.println("Size " + response.getResponseDetails().getSize());
	 * System.out.println("Total " + response.getResponseDetails().getTotal());
	 *
	 * if (resp.getResponseDetails().getNextPage() != null) {
	 *   System.out.println("Next page " + response.getResponseDetails().getNextPage());
	 *   System.out.println("Previous page " + response.getResponseDetails().getPreviousPage());
	 * }</pre>
	 * @vapil.response <pre>
	 * <i>Example reading of X-VaultAPI-DescribeQuery results</i>
	 * The X-VaultAPI-DescribeQuery can be set via the {@link #setDescribeQuery(Boolean)} method, with an example response:
	 *
	 * if (resp.getQueryDescribe() != null) {
	 *   System.out.println("Object Name = " + response.getQueryDescribe().getQueryObject().getName());
	 *   System.out.println("Object Label = " + response.getQueryDescribe().getQueryObject().getLabel());
	 *
	 *   for (VaultObjectField field : resp.getQueryDescribe().getFields()) {
	 *     System.out.println("\tField Name = " + field.getName());
	 *     System.out.println("\tField Label = " + field.getLabel());
	 *   }
	 * }</pre>
	 */
	public QueryResponse query(String vql) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_QUERY));

		request.addBodyParam("q", vql);
		log.info("Query = " + vql);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		if (queryDescribe != null && queryDescribe)
			request.addHeaderParam(HTTP_HEADER_VAULT_DESCRIBE_QUERY, Boolean.toString(queryDescribe));

		if (recordPropertyType != null)
			request.addHeaderParam(HTTP_HEADER_VAULT_RECORD_PROPERTIES, recordPropertyType.getValue());

		return send(HttpMethod.POST, request, QueryResponse.class);
	}

	/**
	 * <b>Get Query Page</b>
	 * <p>
	 * Perform a paginated query based on the URL from a previous query (previous_page or next_page in the response details).
	 * <p>
	 * Note that this does not support described query, which should be read
	 * upon the first query call if needed.
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter in the form
	 *                  <br>
	 *                  POST /query/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx?pagesize=10{@literal &}pageoffset=10
	 * @return QueryResponse
	 * @vapil.api <pre>
	 * <i>Example query URL format</i>
	 * POST /query/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx?pagesize=10{@literal &}pageoffset=10</pre>
	 * @vapil.request <pre>
	 * QueryResponse response = vaultClient.newRequest(QueryRequest.class).query(query);
	 * // Get the first pageoffset (assuming correct response and an pageoffset exists)
	 * QueryResponse paginatedResponse = = vaultClient.newRequest(QueryRequest.class)
	 * 		.queryByPage(response.getResponseDetails().getNextPage());
	 * 	</pre>
	 * @vapil.response <pre>System.out.println(paginatedResponse.getResponseStatus())</pre>;
	 */
	public QueryResponse queryByPage(String pageUrl) {
		// Manipulate the URL for passing in the exact URL from next_page or previous_page
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, QueryResponse.class);
	}

	/*
	 *
	 * Enums
	 *
	 */

	/**
	 * Download option for Document Token
	 */
	public enum RecordPropertyType {
		ALL("all"),
		HIDDEN("hidden"),
		REDACTED("redacted"),
		WEBLINK("weblink");

		private String value;

		RecordPropertyType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * <b>X-VaultAPI-DescribeQuery</b>
	 * <p>
	 * Set to true to include static field metadata in the response for the data record.
	 * If not specified, the response does not include any static field metadata.
	 * This option eliminates the need to make additional API calls to understand
	 * the shape of query response data.
	 *
	 * @param queryDescribe True for a describe
	 * @return The Request
	 * @vapil.response <i>Example results of a query describe</i>
	 * <pre>
	 * if (response.getQueryDescribe() != null) {
	 *   System.out.println("Object Name = " + response.getQueryDescribe().getQueryObject().getName());
	 *   System.out.println("Object Label = " + response.getQueryDescribe().getQueryObject().getLabel());
	 *
	 *   for (VaultObjectField field : response.getQueryDescribe().getFields()) {
	 *     System.out.println("\tField Name = " + field.getName());
	 *     System.out.println("\tField Label = " + field.getLabel());
	 *   }
	 * }</pre>
	 */
	public QueryRequest setDescribeQuery(Boolean queryDescribe) {
		this.queryDescribe = queryDescribe;
		return this;
	}

	/**
	 * <b>X-VaultAPI-RecordProperties</b>
	 * <p>
	 * If present, the response includes the record properties object.
	 * Possible values are all, hidden, redacted, and weblink.
	 * If omitted, the record properties object is not included in the response.
	 *
	 * @param recordPropertyType record property type
	 * @return The Request
	 * @vapil.response <i>Example results of record properties</i>
	 * <pre>
	 * if (response.getRecordProperties() != null) {
	 *   for (RecordProperty recordProperty : response.getRecordProperties()) {
	 *     System.out.println("\tId = " + recordProperty.getId());
	 *     System.out.println("\tHidden = " + String.join(",", recordProperty.getFieldProperties().get("hidden")));
	 *   }
	 * }</pre>
	 */
	public QueryRequest setRecordProperties(RecordPropertyType recordPropertyType) {
		this.recordPropertyType = recordPropertyType;
		return this;
	}
}
