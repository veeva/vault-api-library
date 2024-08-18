package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.common.ObjectRecordType;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Tag("ObjectRecordTypeTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Object Record Type Request should")
public class ObjectRecordTypeTest {

    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    @Disabled
    public void testCreateObject(VaultClient vaultClient) {

        List<ObjectRecord> objectRecords = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            ObjectRecord objectRecord = new ObjectRecord();
//            objectRecord.set("test_run_id__c", TestRunHelper.getFilesafeTestRunID());
            objectRecord.set("test_run_id__c", ZonedDateTime.now());
            objectRecord.set("test_object_no__c", i);
            if (i % 2 == 0) {
                objectRecord.set("create_test_date__c","2050-01-01");
            }
            objectRecords.add(objectRecord);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeJson()
                    .setBinaryFile("file",mapper.writeValueAsBytes(objectRecords))
                    .createObjectRecords("vapil_test_create__c");
            Assertions.assertTrue(resp.isSuccessful());
            Assertions.assertNull(resp.getErrors());
            Assertions.assertTrue(resp.getData().stream().allMatch(VaultResponse::isSuccessful));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    public void testChangeObjectType(VaultClient vaultClient) {

        List<ObjectRecord> objectRecords = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            ObjectRecord objectRecord = new ObjectRecord();
//            objectRecord.set("test_run_id__c", TestRunHelper.getFilesafeTestRunID());
            objectRecord.set("test_run_id__c", ZonedDateTime.now());
            objectRecord.set("test_object_no__c", i);
            switch (i % 5) {
                case 1:
                    objectRecord.set("initial_type_designation__c","A_B");
                    objectRecord.set("object_type__v.api_name__v","alpha__c");
                    break;
                case 2:
                    objectRecord.set("initial_type_designation__c","B_C");
                    objectRecord.set("object_type__v.api_name__v","bravo__c");
                    break;
                case 3:
                    objectRecord.set("initial_type_designation__c","C_D");
                    objectRecord.set("object_type__v.api_name__v","charlie__c");
                    break;
                case 4:
                    objectRecord.set("initial_type_designation__c","A__");
                    objectRecord.set("object_type__v.api_name__v","alpha__c");
                    break;
                default: // (0)
                    objectRecord.set("initial_type_designation__c","A_D");
                    objectRecord.set("object_type__v.api_name__v","alpha__c");
                    break;
            }
            objectRecords.add(objectRecord);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeJson()
                    .setBinaryFile("file",mapper.writeValueAsBytes(objectRecords))
                    .createObjectRecords("vapil_test_change_obj_type__c");
            Assertions.assertTrue(resp.isSuccessful());
            Assertions.assertNull(resp.getErrors());
            Assertions.assertTrue(resp.getData().stream().allMatch(VaultResponse::isSuccessful));
            //"vapil_test_change_obj_type__c"
            QueryResponse newRecs = vaultClient.newRequest(QueryRequest.class).query("select id, name__v, object_type__v, test_object_no__c  from vapil_test_change_obj_type__c where test_run_id__c = '"+ZonedDateTime.now()+"'");
            Assertions.assertTrue(newRecs.isSuccessful());
            Assertions.assertEquals(10, newRecs.getData().size());
            objectRecords.clear();
            newRecs.getData().forEach( rec -> {
                boolean skip = false;
                ObjectRecord objectRecord = new ObjectRecord();
                objectRecord.set("id",rec.get("id"));
                switch (rec.getInteger("test_object_no__c") % 5){
                    case 1:
                        objectRecord.set("second_type_designation__c","A_B");
                        objectRecord.set("object_type__v.api_name__v","bravo__c");
                        break;
                    case 2:
                        objectRecord.set("second_type_designation__c","B_C");
                        objectRecord.set("object_type__v.api_name__v","charlie__c");
                        break;
                    case 3:
                        objectRecord.set("second_type_designation__c","C_D");
                        objectRecord.set("object_type__v.api_name__v","delta__c");
                        break;
                    case 4:
                        skip = true;
                        break;
                    default: // (0)
                        objectRecord.set("second_type_designation__c","A_D");
                        objectRecord.set("object_type__v.api_name__v","delta__c");
                        break;
                }
                if (!skip) {
                    objectRecords.add(objectRecord);
                }

            });
            ObjectRecordBulkResponse changeResp = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeJson()
                    .setBinaryFile("file", mapper.writeValueAsBytes(objectRecords))
                    .changeObjectType("vapil_test_change_obj_type__c");
            Assertions.assertTrue(changeResp.isSuccessful());
            Assertions.assertFalse(changeResp.hasErrors());
            Assertions.assertTrue(changeResp.getData().stream().allMatch(VaultResponse::isSuccessful));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve details from all object types")
    class TestRetrieveDetailsFromAllObjectTypes {
        ObjectRecordTypeBulkResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordRequest.class)
                    .retrieveDetailsFromAllObjectTypes();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getResponseDetails());
            assertNotNull(response.getResponseDetails().getSize());
            assertNotNull(response.getResponseDetails().getTotal());
            assertNotNull(response.getData());
            for (ObjectRecordType objectRecordType : response.getData()) {
                assertNotNull(objectRecordType.getName());
                assertNotNull(objectRecordType.getObject());
                assertNotNull(objectRecordType.getActive());
                assertNotNull(objectRecordType.getLabel());
                assertNotNull(objectRecordType.getLabelPlural());
                assertNotNull(objectRecordType.getTypeFields());
                for (ObjectRecordType.ObjectRecordTypeField field : objectRecordType.getTypeFields()) {
                    assertNotNull(field.getName());
                    assertNotNull(field.getSource());
                    assertNotNull(field.getRequired());
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve details from a specific object")
    class TestRetrieveDetailsFromASpecificObject {
        ObjectRecordTypeResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            String objectName = "dataset_item__sys";
            String objectType = "base__v";
            response = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setLocalized(true)
                    .retrieveDetailsFromASpecificObject(objectName, objectType);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getLocalizedData());
            assertNotNull(response.getData().getLocalizedData().getLabel());
            assertNotNull(response.getData().getLocalizedData().getLabelPlural());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getObject());
            assertNotNull(response.getData().getActive());
            assertNotNull(response.getData().getLabel());
            assertNotNull(response.getData().getLabelPlural());
            assertNotNull(response.getData().getTypeFields());
            for (ObjectRecordType.ObjectRecordTypeField field : response.getData().getTypeFields()) {
                assertNotNull(field.getName());
                assertNotNull(field.getSource());
                assertNotNull(field.getRequired());
            }
        }
    }

    /*private static void displayResults(ObjectRecordTypeResponse resp, TestReporter reporter) {

        for (ObjectRecordTypeResponse.ObjectRecordType rt : resp.getData()) {
            reporter.publishEntry("------------------------------------------------------\n");
            //System.out.println("------------------------------------------------------");
            reporter.publishEntry("Object " + rt.getObject() + " - " + rt.getName() + " - "+ rt.getLabel() + "\n");
            //System.out.println("Object " + rt.getObject() + " - " + rt.getName() + " - "+ rt.getLabel());

            for (String s : rt.getAdditionalTypeValidations())
                reporter.publishEntry("Validations " + s + "\n");

            List<ObjectRecordTypeResponse.ObjectRecordType.ObjectRecordTypeAction> actions = rt.getActions();
            if (actions != null) {
                for (ObjectRecordTypeResponse.ObjectRecordType.ObjectRecordTypeAction action : rt.getActions())
                    reporter.publishEntry("Action " + action.getAction() + " - " + action.getName() + "\n");
            }

            for (ObjectRecordTypeResponse.ObjectRecordType.ObjectRecordTypeField fld : rt.getFields())
                reporter.publishEntry("Field " + fld.getName() + " - " + fld.getSource() + " - " + fld.getRequired() + "\n");
        }
    }*/
}
