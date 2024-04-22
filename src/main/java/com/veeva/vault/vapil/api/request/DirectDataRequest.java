package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.DirectDataResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Direct Data Requests
 * <ul>
 * <li>Retrieve Available Direct Data Files</li>
 * <li>Download Direct Data File</li>
 * </ul>
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.1/#direct-data">https://developer.veevavault.com/api/24.1/#direct-data</a>
 */

public class DirectDataRequest extends VaultRequest<DirectDataRequest> {
    // API Endpoints
    private static final String URL_LIST_ITEMS = "/services/directdata/files";
    private static final String URL_DOWNLOAD_ITEM = "/services/directdata/files/{name}";

    // API Request Parameters
    private ExtractType extractType;
    private ZonedDateTime startTime;
    private ZonedDateTime stopTime;
    private Integer filepart;
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    // API Request Parameter Constants
    public static final String EXTRACT_TYPE = "extract_type";
    public static final String START_TIME_PARAMETER = "start_time";
    public static final String STOP_TIME_PARAMETER = "stop_time";


    /**
     * <b>Retrieve Available Direct Data Files</b>
     * <p>
     * Retrieve a list of all Direct Data files available for download.
     *
     * @return DirectDataResponse
     * @vapil.api <pre> GET /api/{version}/services/directdata/files </pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.1/#retrieve-available-direct-data-files' target='_blank'>https://developer.veevavault.com/api/24.1/#retrieve-available-direct-data-files</a>
     * @vapil.request <pre>
     * ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
     * ZonedDateTime stopTime = currentDateTime.toLocalDate().atStartOfDay(ZoneId.of("UTC"));
     * ZonedDateTime startTime = stopTime.minusDays(1);
     *
     * DirectDataResponse response = vaultClient.newRequest(DirectDataRequest.class)
     *      .setExtractType(DirectDataRequest.ExtractType.INCREMENTAL)
     *      .setStartTime(startTime)
     *      .setStopTime(stopTime)
     *      .retrieveAvailableDirectDataFiles();
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Total Direct Data Files: " + response.getResponseDetails().getTotal());
     * System.out.println("-----------------------");
     * List&lt;DirectDataResponse.DirectDataItem&gt; data = response.getData();
     * for (DirectDataResponse.DirectDataItem item : data) {
     *      System.out.println("Name: " + item.getName());
     *      System.out.println("Filename: " + item.getFilename());
     *      System.out.println("Extract Type: " + item.getExtractType());
     *      System.out.println("Record Count: " + item.getRecordCount());
     *      System.out.println("Size: " + item.getSize());
     *      System.out.println("File parts: " + item.getFileparts());
     *      System.out.println("-----------------------");
     * }
     * </pre>
     */
    public DirectDataResponse retrieveAvailableDirectDataFiles() {
        String url = vaultClient.getAPIEndpoint(URL_LIST_ITEMS);
        HttpRequestConnector request = new HttpRequestConnector(url);

        if (this.extractType != null) {
            request.addQueryParam(EXTRACT_TYPE, extractType.getValue());
        }

        if (this.startTime != null) {
            request.addQueryParam(START_TIME_PARAMETER, getFormattedDate(startTime, DATE_TIME_PATTERN));
        }

        if (this.stopTime != null) {
            request.addQueryParam(STOP_TIME_PARAMETER, getFormattedDate(stopTime, DATE_TIME_PATTERN));
        }

        return send(HttpRequestConnector.HttpMethod.GET, request, DirectDataResponse.class);
    }

    /**
     * <b>Download Direct Data File</b>
     * <p>
     * Download a Direct Data file.
     *
     * @return VaultResponse
     * @vapil.api <pre> GET /api/{version}/services/directdata/files/{name} </pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.1/#download-direct-data-file' target='_blank'>https://developer.veevavault.com/api/24.1/#download-direct-data-file</a>
     * @vapil.request <pre>
     * VaultResponse response = vaultClient.newRequest(DirectDataRequest.class)
     *      .downloadDirectDataFile(fileName);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Content-Type: " + response.getHeaderContentType());
     * System.out.println("Content-Disposition: " + response.getHttpHeaderContentDisposition());
     * </pre>
     */
    public VaultResponse downloadDirectDataFile(String name) {
        String url = vaultClient.getAPIEndpoint(URL_DOWNLOAD_ITEM)
                .replace("{name}", name);
        HttpRequestConnector request = new HttpRequestConnector(url);

        if (this.filepart != null) {
            request.addQueryParam("filepart", filepart);
        }

        return sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, VaultResponse.class);
    }

    /**
     * Set the Direct Data file type to retrieve. Options are FULL, INCREMENTAL, and LOG.
     *
     * @param extractType ExtractType enum value
     * @return DirectDataRequest
     */
    public DirectDataRequest setExtractType(ExtractType extractType) {
        this.extractType = extractType;
        return this;
    }

    /**
     * Specify a start date and time to retrieve direct data files.
     *
     * @param startTime Start date and time for retrieving direct data file requests.
     * @return DirectDataRequest
     */
    public DirectDataRequest setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Specify a stop date and time to retrieve direct data files.
     *
     * @param stopTime Stop date and time for retrieving direct data file requests.
     * @return DirectDataRequest
     */
    public DirectDataRequest setStopTime(ZonedDateTime stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    /**
     * Set the number for the file part.
     *
     * @param filepart This value is required if the file to download has more than one part.
     * @return DirectDataRequest
     */
    public DirectDataRequest setFilepart(Integer filepart) {
        this.filepart = filepart;
        return this;
    }

    /**
     * Converts the date to the proper string format expected by the API
     *
     * @param date        The date to convert
     * @param datePattern DateTimeFormatter pattern
     * @return Formatted date as string
     */
    private String getFormattedDate(ZonedDateTime date, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return date.format(formatter);
    }

    /**
     * Enum ExtractType represents the types of extracts.
     * Option(s): Full, Incremental
     */
    public enum ExtractType {
        FULL("full_directdata"),
        INCREMENTAL("incremental_directdata"),
        LOG("log_directdata");

        private final String extractType;

        ExtractType(String extractType) {
            this.extractType = extractType;
        }

        public String getValue() {
            return extractType;
        }
    }
}
