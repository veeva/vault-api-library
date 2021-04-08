package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.VaultClientBuilder;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.client.VaultClientId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("VaultClientBuilder")
public class ClientBuilderTest {

	@Test
	public void testSessionId() {
		TestProperties testProperties = new TestProperties();
		VaultClientId vaultClientId =  new VaultClientId(
				testProperties.getClientIdCompany(),
				testProperties.getClientIdOrganization(),
				testProperties.getClientIdTeam(),
				true,
				testProperties.getClientIdProgram());

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
					.withVaultClientId(vaultClientId)
					.withVaultDNS(testProperties.getVaultDNS())
					.withVaultSessionId(testProperties.getSessionId())
					.withValidation(true)
					.withApiErrorLogging(true)
					.withHttpTimeout(60)
					.build();
		Assertions.assertTrue(true, vaultClient.getAuthenticationResponse().getResponseStatus());
	}

	@Test
	public void testBasic() {
		TestProperties testProperties = new TestProperties();
		VaultClientId vaultClientId =  new VaultClientId(
				testProperties.getClientIdCompany(),
				testProperties.getClientIdOrganization(),
				testProperties.getClientIdTeam(),
				true,
				testProperties.getClientIdProgram());

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
				.withVaultClientId(vaultClientId)
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultUsername(testProperties.getVaultUsername())
				.withVaultPassword(testProperties.getVaultPassword())
				.withValidation(true)
				.withApiErrorLogging(true)
				.withHttpTimeout(60)
				.build();

		Assertions.assertTrue(true, vaultClient.getAuthenticationResponse().getResponseStatus());
	}

	@Test
	public void testOauthToken() {
		TestProperties testProperties = new TestProperties();
		VaultClientId vaultClientId =  new VaultClientId(
				testProperties.getClientIdCompany(),
				testProperties.getClientIdOrganization(),
				testProperties.getClientIdTeam(),
				true,
				testProperties.getClientIdProgram());

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
				.withVaultClientId(vaultClientId) //required
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
				.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
				.withIdpOauthAccessToken(testProperties.getIdpOauthAccessToken()) //required
				.withValidation(true) //default = true
				.withApiErrorLogging(true) //default = true
				.withHttpTimeout(60) //default = 60
				.build();

		Assertions.assertTrue(true, vaultClient.getAuthenticationResponse().getResponseStatus());
	}

	@Test
	public void testOauthDiscovery() {
		TestProperties testProperties = new TestProperties();
		VaultClientId vaultClientId =  new VaultClientId(
				testProperties.getClientIdCompany(),
				testProperties.getClientIdOrganization(),
				testProperties.getClientIdTeam(),
				true,
				testProperties.getClientIdProgram());

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
					.withVaultClientId(vaultClientId) //required
					.withVaultDNS(testProperties.getVaultDNS())
					.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
					.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
					.withIdpPassword(testProperties.getIdpUserPwd()) //required
					.withValidation(true) //default = true
					.withApiErrorLogging(true) //default = true
					.withHttpTimeout(60) //default = 60
					.build();

		Assertions.assertTrue(true, vaultClient.getAuthenticationResponse().getResponseStatus());
	}

	@Test
	public void testOauthDiscoveryWithIdpUserName() {
		TestProperties testProperties = new TestProperties();
		VaultClientId vaultClientId =  new VaultClientId(
				testProperties.getClientIdCompany(),
				testProperties.getClientIdOrganization(),
				testProperties.getClientIdTeam(),
				true,
				testProperties.getClientIdProgram());

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.OAUTH_DISCOVERY)
					.withVaultClientId(vaultClientId) //required
					.withVaultDNS(testProperties.getVaultDNS())
					.withVaultOauthProfileId(testProperties.getVaultOAuthProfileId()) //required
					.withVaultOauthClientId(testProperties.getVaultOAuthClientId()) //required
					.withIdpUsername(testProperties.getIdpUserName()) //required
					.withIdpPassword(testProperties.getIdpUserPwd()) //required
					.withValidation(true) //default = true
					.withApiErrorLogging(true) //default = true
					.withHttpTimeout(60) //default = 60
					.build();

		Assertions.assertTrue(true, vaultClient.getAuthenticationResponse().getResponseStatus());
	}
}
