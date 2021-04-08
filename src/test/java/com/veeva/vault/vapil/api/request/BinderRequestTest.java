package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Binder.Node.BinderSection;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.BinderResponse;
import com.veeva.vault.vapil.api.model.response.BinderSectionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("BinderRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class BinderRequestTest {
    static final boolean DEPTH_ALL = true;
    static final int BINDER_ID = 1;
    static final String BINDER_NAME = "FKO Decks";
    static final int FKO_BINDER_MAJOR_VERSION = 1;
    static final int FKO_BINDER_MINOR_VERSION = 0;
    static final int NEW_BINDER_ID = 2;
    static final String NEW_BINDER_NAME = "VAPIL Binder";
    static final String NEW_BINDER_LIFECYCLE = "General Lifecycle";
    static final String NEW_BINDER_TYPE = "General";
    static final String NEW_BINDER_TITLE = "Test Upload VAPIL Binder";
    static final String UPDATED_BINDER_NAME = "Vapil Binder Updated";
    static final String UPDATED_BINDER_TITLE = "Test Upload VAPIL Binder Updated";
    static final String UPDATED_BINDER_TYPE = "Miscellaneous";
    static final String UPDATED_BINDER_LIFECYCLE = "Unclassified";
    static final int BINDER_ID_TO_DELETE = 11;
    static final String SECTION_ID = "1617043435446:-1965656107";
    static final String BINDER_TEMPLATE_NAME = "test_binder__c";
    static final String NEW_BINDER_SECTION = "Binder Section";
    static final String BINDER_SECTION_ID_TO_DELETE = "1617043378062:941861690";

    @Test
    public void testRetrieveBinder(VaultClient vaultClient) {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .setDepth(DEPTH_ALL)
                .retrieveBinder(BINDER_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertEquals(FKO_BINDER_MAJOR_VERSION, response.getDocument().getMajorVersionNumber());
        Assertions.assertEquals(FKO_BINDER_MINOR_VERSION, response.getDocument().getMinorVersionNumber());
        Assertions.assertEquals(BINDER_NAME, response.getDocument().getName());
    }

    @Test
    public void testRetrieveAllBinderVersions(VaultClient vaultClient) {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveAllBinderVersions(BINDER_ID);
        Assertions.assertTrue(response.isSuccessful());
    }


    @Test
    public void testCreateBinder(VaultClient vaultClient) {

        Document doc = new Document();

        doc.setName(NEW_BINDER_NAME);
        doc.setLifecycle(NEW_BINDER_LIFECYCLE);
        doc.setType(NEW_BINDER_TYPE);
        doc.setTitle(NEW_BINDER_TITLE);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .setAsync(true)
                .createBinder(doc);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument().getId());
    }

    @Test
    public void testCreateBinderVersion(VaultClient vaultClient) {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderVersion(NEW_BINDER_ID);
        Assertions.assertTrue(response.isSuccessful());
    }


    @Test
    public void testCreateBinderFromTemplate(VaultClient vaultClient) {
        Document doc = new Document();

        doc.setName(NEW_BINDER_NAME);
        doc.setLifecycle(NEW_BINDER_LIFECYCLE);
        doc.setType(NEW_BINDER_TYPE);
        doc.setTitle(NEW_BINDER_TITLE);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderFromTemplate(doc, BINDER_TEMPLATE_NAME);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testUpdateBinder(VaultClient vaultClient) {
        Document doc = new Document();

        doc.setName(UPDATED_BINDER_NAME);
        doc.setTitle(UPDATED_BINDER_TITLE);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinder(NEW_BINDER_ID, doc);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testReclassifyBinder(VaultClient vaultClient) {
        Document doc = new Document();

        doc.setName(UPDATED_BINDER_NAME);
        doc.setTitle(UPDATED_BINDER_TITLE);
        doc.setLifecycle(UPDATED_BINDER_LIFECYCLE);
        doc.setType(UPDATED_BINDER_TYPE);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .reclassifyBinder(NEW_BINDER_ID, doc);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testDeleteBinder(VaultClient vaultClient) {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .deleteBinder(BINDER_ID_TO_DELETE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testRetrieveBinderSectionRootNode(VaultClient vaultClient) {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderSections(BINDER_ID);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testRetrieveBinderSectionSpecificNode(VaultClient vaultClient) {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderSections(BINDER_ID, SECTION_ID);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testRetrieveBinderVersionSection(VaultClient vaultClient) {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderVersionSections(BINDER_ID, FKO_BINDER_MAJOR_VERSION, FKO_BINDER_MINOR_VERSION, SECTION_ID);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testCreateBinderSection(VaultClient vaultClient) {

        BinderSection binderSection = new BinderSection();
        binderSection.setName(NEW_BINDER_SECTION);

        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderSection(BINDER_ID, binderSection);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testDeleteBinderSection(VaultClient vaultClient) {

        BinderSection binderSection = new BinderSection();
        binderSection.setName(NEW_BINDER_SECTION);

        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .deleteBinderSection(BINDER_ID, BINDER_SECTION_ID_TO_DELETE);
        Assertions.assertTrue(response.isSuccessful());
    }

}
