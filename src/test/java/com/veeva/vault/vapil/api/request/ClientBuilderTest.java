package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.veeva.vault.vapil.api.client.*;
import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.model.response.DiscoveryResponse;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("ClientBuilderTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientBuilderTest {

	private static final String SETTINGS_FILES_FOLDER = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "settings_files" + File.separator;

	@Test
	@Order(1)
	@DisplayName("Should successfully build a client when a valid session ID is provided")
	public void testSessionId(VaultClient vaultClient) {
		VaultClient testClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
				.withVaultClientId(vaultClient.getVaultClientId())
				.withVaultDNS(vaultClient.getVaultDNS())
				.withVaultSessionId(vaultClient.getSessionId())
				.withValidation(true)
				.withApiErrorLogging(true)
				.withHttpTimeout(60)
				.build();

		Assertions.assertTrue(testClient.validateSession());
		File sessionSettingsFile = getSessionIdSettingsFile();
		JsonNode sessionNode = getSessionIdSettingsNode();
		FileHelper.writeSessionId(testClient.getSessionId(), sessionNode, sessionSettingsFile);
	}

	@Test
	@Tag("SmokeTest")
	@DisplayName("Should successfully build a client with a valid username and password")
	public void testBasic() {
		Map<String, String> basicMap = getBasicSettingsMap();
		VaultClient testClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultClientId(basicMap.get("vaultClientId"))
				.withVaultDNS(basicMap.get("vaultDNS"))
				.withVaultUsername(basicMap.get("vaultUsername"))
				.withVaultPassword(basicMap.get("vaultPassword"))
				.withValidation(true)
				.withApiErrorLogging(true)
				.withHttpTimeout(60)
				.build();

		Assertions.assertTrue(testClient.validateSession());

	}

	@Test
	@Tag("SmokeTest")
	@DisplayName("Should unsuccessfully build a client from a bad password")
	public void testBadAuthentication() {
		Map<String, String> basicMap = getBasicSettingsMap();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultClientId(basicMap.get("vaultClientId"))
				.withVaultDNS(basicMap.get("vaultDNS"))
				.withVaultUsername(basicMap.get("vaultUsername"))
				.withVaultPassword("bad password")
				.build();

		AuthenticationResponse response = vaultClient.getAuthenticationResponse();
		assertFalse(vaultClient.hasSessionId());
		assertFalse(response.isSuccessful());
	}

	// Test Manually by getting an Oauth access token and putting it in the Oauth settings file
	@Disabled
	@Test
	@DisplayName("Should successfully build a client with a valid Oauth access token")
	public void testOauthToken() {
		Map<String, String> oauthMap = getOauthTokenMap();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
				.withVaultClientId(oauthMap.get("vaultClientId")) //required
				.withVaultDNS(oauthMap.get("vaultDNS"))
				.withVaultOauthProfileId(oauthMap.get("vaultOauthProfileId")) //required
				.withVaultOauthClientId(oauthMap.get("vaultOauthClientId")) //required
				.withIdpOauthAccessToken(oauthMap.get("idpOauthAccessToken")) //required
				.withIdpOauthScope(oauthMap.get("idpOauthScope")) //default = openid
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should successfully build a client with valid Oauth parameters")
	public void testOauthDiscovery() {
		Map<String, String> oauthMap = getOauthTokenMap();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
				.withVaultClientId(oauthMap.get("vaultClientId")) //required
				.withVaultDNS(oauthMap.get("vaultDNS"))
				.withVaultUsername(oauthMap.get("vaultUsername"))
				.withVaultOauthProfileId(oauthMap.get("vaultOauthProfileId")) //required
				.withVaultOauthClientId(oauthMap.get("vaultOauthClientId")) //required
				.withIdpPassword(oauthMap.get("idpPassword")) //required
				.withIdpOauthScope(oauthMap.get("idpOauthScope")) //default = openid
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should successfully build a client with valid Oauth parameters")
	public void testOauthDiscoveryWithIdpUserName() {
		Map<String, String> oauthMap = getOauthTokenMap();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
				.withVaultClientId(oauthMap.get("vaultClientId")) //required
				.withVaultDNS(oauthMap.get("vaultDNS")) //required
				.withVaultUsername(oauthMap.get("vaultUsername")) //required
				.withVaultOauthProfileId(oauthMap.get("vaultOauthProfileId")) //required
				.withVaultOauthClientId(oauthMap.get("vaultOauthClientId")) //required
				.withIdpUsername(oauthMap.get("idpUsername")) //required
				.withIdpPassword(oauthMap.get("idpPassword")) //required
				.withIdpOauthScope(oauthMap.get("idpOauthScope")) //default = openid
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
				.authenticationTypeDiscovery("vapil.test@sb-developersupport.com");

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@Tag("SmokeTest")
	@DisplayName("Should successfully build a client with a valid username and password from a settings file")
	public void testNewClientBuilderFromSettingsFileBasic() {
		File basicSettingsFile = getBasicSettingsFile();
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(basicSettingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should successfully build a client with a session ID from a settings file")
	public void testNewClientBuilderFromSettingsFileSessionId() {
		File sessionSettingsFile = getSessionIdSettingsFile();
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(sessionSettingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	// Test Manually by getting an Oauth access token and putting it in the Oauth settings file
	@Disabled
	@Test
	@DisplayName("Should return a valid client when a valid OAuth access token is provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthToken() {
		File oauthTokenSettingsFile = getOauthTokenSettingsFile();
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(oauthTokenSettingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthDiscovery() {
		File oauthDiscoverySettingsFile = getOauthDiscoverySettingsFile();
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(oauthDiscoverySettingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@DisplayName("Should return a valid client when valid OAuth parameters are provided from a settings file")
	public void testNewClientBuilderFromSettingsFileOauthDiscoveryIdpUsername() {
		File oauthDiscoverySettingsFile = getOauthDiscoverySettingsFile();
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(oauthDiscoverySettingsFile).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	@Test
	@Tag("SmokeTest")
	@DisplayName("Should return a valid client when valid auth parameters are passed as a json string")
	public void testNewClientBuilderFromSettingsString() {
		File basicSettingsFile = getBasicSettingsFile();
		String jsonString = FileHelper.convertJsonFileToString(basicSettingsFile);
		VaultClient vaultClient = VaultClient.newClientBuilderFromSettings(jsonString).build();
		Assertions.assertTrue(vaultClient.validateSession());
	}

	private File getBasicSettingsFile() {
		String basicSettingsFilePath = SETTINGS_FILES_FOLDER + "settings_vapil_basic.json";
		File basicSettingsFile = FileHelper.getSettingsFile(basicSettingsFilePath);
		return basicSettingsFile;
	}

	private JsonNode getBasicSettingsNode() {
		File basicSettingsFile = getBasicSettingsFile();
		JsonNode basicNode = FileHelper.readSettingsFile(basicSettingsFile);
		return basicNode;
	}

	private Map<String, String> getBasicSettingsMap() {
		JsonNode basicNode = getBasicSettingsNode();
		Map<String, String> basicMap = FileHelper.convertJsonNodeToMap(basicNode);
		return basicMap;
	}

	private File getSessionIdSettingsFile() {
		String sessionIdSettingsFilePath = SETTINGS_FILES_FOLDER + "settings_vapil_session_id.json";
		File sessionIdSettingsFile = FileHelper.getSettingsFile(sessionIdSettingsFilePath);
		return sessionIdSettingsFile;
	}

	private JsonNode getSessionIdSettingsNode() {
		File sessionIdSettingsFile = getSessionIdSettingsFile();
		JsonNode sessionIdNode = FileHelper.readSettingsFile(sessionIdSettingsFile);
		return sessionIdNode;
	}

	private File getOauthDiscoverySettingsFile() {
		String oauthDiscoverySettingsFilePath = SETTINGS_FILES_FOLDER + "settings_vapil_oauth_discovery.json";
		File oauthDiscoverySettingsFile = FileHelper.getSettingsFile(oauthDiscoverySettingsFilePath);
		return oauthDiscoverySettingsFile;
	}

	private File getOauthTokenSettingsFile() {
		String oauthTokenSettingsFilePath = SETTINGS_FILES_FOLDER + "settings_vapil_oauth_token.json";
		File oauthTokenSettingsFile = FileHelper.getSettingsFile(oauthTokenSettingsFilePath);
		return oauthTokenSettingsFile;
	}

	private JsonNode getOauthTokenNode() {
		File oauthTokenSettingsFile = getOauthTokenSettingsFile();
		JsonNode oauthNode = FileHelper.readSettingsFile(oauthTokenSettingsFile);
		return oauthNode;
	}

	private Map<String, String> getOauthTokenMap() {
		JsonNode oauthNode = getOauthTokenNode();
		Map<String, String> oauthMap = FileHelper.convertJsonNodeToMap(oauthNode);
		return oauthMap;
	}
}
