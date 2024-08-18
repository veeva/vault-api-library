package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentEvent;
import com.veeva.vault.vapil.api.model.response.DocumentSignatureMetadataResponse;
import com.veeva.vault.vapil.api.model.response.DomainResponse;
import com.veeva.vault.vapil.api.model.response.DomainsResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentSignatureRequest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Document Signature request should")
public class DocumentSignatureRequestTest {

    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully Retrieve Document Signature Metadata")
    class TestRetrieveDocumentSignatureMetadata {

        private DocumentSignatureMetadataResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentSignatureRequest.class)
                    .retrieveDocumentSignatureMetadata();

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getProperties());
            assertNotNull(response.getProperties().getName());
            List<DocumentSignatureMetadataResponse.Properties.Field> fields = response.getProperties().getFields();
            for (DocumentSignatureMetadataResponse.Properties.Field field : fields) {
                assertNotNull(field.getName());
                assertNotNull(field.getType());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully Retrieve Archived Document Signature Metadata")
    class TestRetrieveArchivedDocumentSignatureMetadata {

        private DocumentSignatureMetadataResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentSignatureRequest.class)
                    .retrieveArchivedDocumentSignatureMetadata();

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getProperties());
            assertNotNull(response.getProperties().getName());
            List<DocumentSignatureMetadataResponse.Properties.Field> fields = response.getProperties().getFields();
            for (DocumentSignatureMetadataResponse.Properties.Field field : fields) {
                assertNotNull(field.getName());
                assertNotNull(field.getType());
            }
        }
    }
}
