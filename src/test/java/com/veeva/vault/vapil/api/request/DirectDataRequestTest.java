package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DirectDataResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DirectDataRequest.ExtractType;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DirectDataRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Direct Data Request should")
public class DirectDataRequestTest {
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully list available direct data files")
    class TestRetrieveAvailableDirectDataFiles {
        DirectDataResponse retrieveAvailableDirectDataFilesResponse = null;

        @Test
        @Order(1)
        void testRequest() {
            ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
            ZonedDateTime stopTime = currentDateTime.toLocalDate().atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime startTime = stopTime.minusDays(1);

            retrieveAvailableDirectDataFilesResponse = vaultClient.newRequest(DirectDataRequest.class)
                    .setExtractType(ExtractType.INCREMENTAL)
                    .setStartTime(startTime)
                    .setStopTime(stopTime)
                    .retrieveAvailableDirectDataFiles();

            assertNotNull(retrieveAvailableDirectDataFilesResponse);
        }

        @Test
        @Order(2)
        void testResponse() {
            assertTrue(retrieveAvailableDirectDataFilesResponse.isSuccessful());
            assertNotNull(retrieveAvailableDirectDataFilesResponse.getResponseDetails());
            assertNotNull(retrieveAvailableDirectDataFilesResponse.getResponseDetails().getTotal());

            assertNotNull(retrieveAvailableDirectDataFilesResponse.getData());
            List<DirectDataResponse.DirectDataItem> data = retrieveAvailableDirectDataFilesResponse.getData();
            for (DirectDataResponse.DirectDataItem item : data) {
                assertNotNull(item.getName());
                assertNotNull(item.getFilename());
                assertNotNull(item.getExtractType());
                assertNotNull(item.getStartTime());
                assertNotNull(item.getStopTime());
                assertNotNull(item.getRecordCount());
                assertNotNull(item.getSize());
                assertNotNull(item.getFileparts());

                assertNotNull(item.getFilepartDetails());
                List<DirectDataResponse.DirectDataItem.FilePart> filepartDetails = item.getFilepartDetails();
                for (DirectDataResponse.DirectDataItem.FilePart filepart : filepartDetails) {
                    assertNotNull(filepart.getName());
                    assertNotNull(filepart.getFilename());
                    assertNotNull(filepart.getFilepart());
                    assertNotNull(filepart.getSize());
                    assertNotNull(filepart.getUrl());
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download a direct data file")
    class TestDownloadItem {
        VaultResponse downloadItemResponse = null;
        String fileName = null;

        @BeforeAll
        void setup() {
            ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
            ZonedDateTime stopTime = currentDateTime.toLocalDate().atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime startTime = stopTime.minusDays(1);

            DirectDataResponse response = vaultClient.newRequest(DirectDataRequest.class)
                    .setExtractType(ExtractType.INCREMENTAL)
                    .setStartTime(startTime)
                    .setStopTime(stopTime)
                    .retrieveAvailableDirectDataFiles();

            assertTrue(response.isSuccessful());
            fileName = response.getData().get(0).getFilepartDetails().get(0).getName();
        }

        @Test
        @Order(1)
        void testRequest() {
            downloadItemResponse = vaultClient.newRequest(DirectDataRequest.class)
                    .downloadDirectDataFile(fileName);

            assertNotNull(downloadItemResponse);
        }

        @Test
        @Order(2)
        void testResponse() {
            assertNotNull(downloadItemResponse.getBinaryContent());
            assertEquals(downloadItemResponse.getHeaderContentType(), "application/octet-stream;charset=UTF-8");
        }
    }
}
