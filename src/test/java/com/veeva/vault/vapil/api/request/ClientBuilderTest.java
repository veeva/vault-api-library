package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.*;
import com.veeva.vault.vapil.api.model.response.DiscoveryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Tag("VaultClientBuilder")
public class ClientBuilderTest {

	@Test
	@DisplayName("Should return a valid client when a valid session ID is provided")
	public void testSessionId() {
		TestProperties testProperties = new TestProperties();

		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
				.withVaultClientId(testProperties.getVaultClientId())
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultSessionId(testProperties.getSessionId())
				.withValidation(true)
				.withApiErrorLogging(true)
				.withHttpTimeout(60)
				.build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when a valid username and password is provided")
	public void testBasic() {
		TestProperties testProperties = new TestProperties();

		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultClientId(testProperties.getVaultClientId())
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultUsername(testProperties.getVaultUsername())
				.withVaultPassword(testProperties.getVaultPassword())
				.withValidation(true)
				.withApiErrorLogging(true)
				.withHttpTimeout(60)
				.build();

		Assertions.assertTrue(vaultClient.validateSession());

	}

	@Test
	@DisplayName("Should return a valid client when a valid OAuth access token is provided")
	public void testOauthToken() {
		TestProperties testProperties = new TestProperties();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
				.withVaultClientId(testProperties.getVaultClientId()) //required
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
				.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
				.withIdpOauthAccessToken(testProperties.getIdpOauthAccessToken()) //required
				.withIdpOauthScope(testProperties.getIdpOauthScope()) //default = openid
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided")
	public void testOauthDiscovery() {
		TestProperties testProperties = new TestProperties();

		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
				.withVaultClientId(testProperties.getVaultClientId()) //required
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultUsername(testProperties.getVaultUsername())
				.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
				.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
				.withIdpPassword(testProperties.getIdpUserPwd()) //required
				.withIdpOauthScope(testProperties.getIdpOauthScope()) //default = openid
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided")
	public void testOauthDiscoveryWithIdpUserName() {
		TestProperties testProperties = new TestProperties();

		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
				.withVaultClientId(testProperties.getVaultClientId()) //required
				.withVaultDNS(testProperties.getVaultDNS()) //required
				.withVaultUsername(testProperties.getVaultUsername()) //required
				.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
				.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
				.withIdpUsername(testProperties.getIdpUserName()) //required
				.withIdpPassword(testProperties.getIdpUserPwd()) //required
				.withIdpOauthScope(testProperties.getIdpOauthScope()) //default = openid
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return valid Discovery Response")
	public void testAuthenticationTypeDiscovery() {
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.NO_AUTH)
				.withVaultClientId("clientId")
				.build();

		DiscoveryResponse response = vaultClient.newRequest(AuthenticationRequest.class)
//				.setVaultOAuthClientId("VeevaProfessionalServices")
				.authenticationTypeDiscovery("username@cholecap.com");

		System.out.println("Auth Type: " + response.getData().getAuthType());

		for (DiscoveryResponse.DiscoveryData.AuthProfile authProfile : response.getData().getAuthProfiles()) {
			System.out.println("ID: " + authProfile.getId());
			System.out.println("Label: " + authProfile.getLabel());
			System.out.println("AS Client ID: " + authProfile.getAsClientId());
			System.out.println("*** AS Metadata ***");
			System.out.println("    Token Endpoint: " + authProfile.getAsMetadata().getTokenEndpoint());
		}

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should return a valid client when a valid username and password is provided from a settings file")
	public void testNewClientBuilderFromSettingsFileBasic() {
		File settingsFile = new File("\\");
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when a valid session ID is provided from a settings file")
	public void testNewClientBuilderFromSettingsFileSessionId() {
		File settingsFile = new File("");
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when a valid OAuth access token is provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthToken() {
		File settingsFile = new File("");
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthDiscovery() {
		File settingsFile = new File("");
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthDiscoveryIdpUsername() {
		File settingsFile = new File("");
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid auth parameters are passed as a json string")
	public void testNewClientBuilderFromSettingsString() {
		String jsonString = "{\n" +
				"  \"authenticationType\": \"BASIC\",\n" +
				"  \"idpOauthAccessToken\": \"\",\n" +
				"  \"idpOauthScope\": \"\",\n" +
				"  \"idpUsername\": \"\",\n" +
				"  \"idpPassword\": \"\",\n" +
				"  \"vaultUsername\": \"USERNAME\",\n" +
				"  \"vaultPassword\": \"PASSWORD\",\n" +
				"  \"vaultDNS\": \"DNS\",\n" +
				"  \"vaultSessionId\": \"\",\n" +
				"  \"vaultClientId\": \"CLIENTID\",\n" +
				"  \"vaultOauthClientId\": \"\",\n" +
				"  \"vaultOauthProfileId\": \"\",\n" +
				"  \"logApiErrors\": true,\n" +
				"  \"httpTimeout\": null,\n" +
				"  \"validateSession\": true\n" +
				"}";

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid auth parameters are passed as a file")
	public void testNewClientBuilderFromSettingsFile() {
		File settingsFile = new File("settings.json");

		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(settingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}
}
