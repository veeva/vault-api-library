/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.DocumentEvent;
import com.veeva.vault.vapil.api.model.response.DocumentEventResponse;
import com.veeva.vault.vapil.api.model.response.DocumentEventSubtypeResponse;
import com.veeva.vault.vapil.api.model.response.DocumentEventTypeResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Events
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-events">https://developer.veevavault.com/api/23.1/#document-events</a>
 */
public class DocumentEventRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_DOCUMENT_EVENT = "/objects/documents/{document_id}/events";
	private static final String URL_DOCUMENT_VERSION_EVENT = "/objects/documents/{document_id}/versions/{major_version}/{minor_version}/events";
	private static final String URL_DOCUMENT_EVENT_TYPE = "/metadata/objects/documents/events";
	private static final String URL_DOCUMENT_EVENT_SUBTYPE_TYPE = "/metadata/objects/documents/events/{event_type}/types/{event_subtype}";

	private DocumentEventRequest() {
	}

	/**
	 * Retrieve Document Event Types and Subtypes
	 *
	 * @return DocumentEventTypeResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/events</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-event-types-and-subtypes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-event-types-and-subtypes</a>
	 * @vapil.request <pre>
	 * DocumentEventTypeResponse response = vaultClient.newRequest(DocumentEventRequest.class)
	 * 					.retrieveDocumentEventTypesandSubtypes();</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponseStatus());
	 * if (response.getEvents() != null) {
	 *   for (DocumentEventTypeResponse.Event event : response.getEvents()) {
	 *     System.out.println("Event Type Name: " + event.getName());
	 *   }
	 * }</pre>
	 */

	public DocumentEventTypeResponse retrieveDocumentEventTypesandSubtypes() {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_EVENT_TYPE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentEventTypeResponse.class);
	}

	/**
	 * Retrieve Document Event SubType Metadata
	 *
	 * @param type    event type
	 * @param subtype event sub type
	 * @return DocumentEvenSubtypeResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/events/{event_type}/types/{event_subtype}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-event-subtype-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-event-subtype-metadata</a>
	 * @vapil.request <pre>
	 * DocumentEventSubtypeResponse response = vaultClient.newRequest(DocumentEventRequest.class)
	 * 					.retrieveDocumentEventSubTypeMetadata(type, subtype);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponseStatus());
	 * if (response.getEventProperties() != null) {
	 *   for (DocumentEventSubtypeResponse.DocumentEventSubtype event : response.getEventProperties()) {
	 *     System.out.println("Event Subtype Name: " + event.getName());
	 *     }
	 *   }
	 * }</pre>
	 */
	public DocumentEventSubtypeResponse retrieveDocumentEventSubTypeMetadata(String type, String subtype) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_EVENT_SUBTYPE_TYPE)
				.replace("{event_type}", type)
				.replace("{event_subtype}", subtype);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentEventSubtypeResponse.class);
	}

	/**
	 * Create Document Event
	 *
	 * @param docId           Document metadata to update
	 * @param majorVersion    document major version number
	 * @param minorVersion    document minor version number
	 * @param eventProperties properties for the event
	 * @return DocumentEvenSubtypeResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{document_id}/versions/{major_version}/{minor_version}/events</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-document-event' target='_blank'>https://developer.veevavault.com/api/23.1/#create-document-event</a>
	 * @vapil.response <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentEventRequest.class)
	 * 				.createDocumentEvent(docId, majorVersion, minorVersion, event.getEventProperties());</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponseStatus());</pre>
	 */
	public VaultResponse createDocumentEvent(int docId, int majorVersion, int minorVersion, DocumentEvent.Property eventProperties) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_EVENT)
				.replace("{document_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.setBodyParams(eventProperties.toMap());

		return send(HttpMethod.POST, request, DocumentEventSubtypeResponse.class);
	}

	/**
	 * Retrieve Document Events
	 *
	 * @param docId Document metadata to update
	 * @return DocumentEventResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{document_id}/events</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-events' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-events</a>
	 * @vapil.request <pre>
	 * DocumentEventResponse response = vaultClient.newRequest(DocumentEventRequest.class)
	 * 					.retrieveDocumentEvents(docId);</pre>
	 * @vapil.response <pre>System.out.println(response.getResponseStatus());
	 *
	 * if (response.getEvents() != null) {
	 *   for (DocumentEvent event : response.getEvents()) {
	 *     System.out.println("Event Name: " + event.getName());
	 *   }
	 * }</pre>
	 */
	public DocumentEventResponse retrieveDocumentEvents(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_EVENT)
				.replace("{document_id}", String.valueOf(docId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentEventResponse.class);
	}

}
