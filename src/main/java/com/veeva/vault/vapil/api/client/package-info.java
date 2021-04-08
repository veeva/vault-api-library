/**
 * HTTPS Client for the Vault API
 *
 * 	<pre>
 * 	private static void testSessionId(VaultClientId vaultClientId) throws Exception {
 * 		//build a client with a session and validate the session
 * 		VaultClient vaultClient = VaultClientBuilder
 * 				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
 * 				.withVaultClientId(vaultClientId) //required
 * 				.withVaultDNS("verteobiotech.veevavault.com") //required
 * 				.withVaultSessionId("7B813C91B526529B574F7CAE26D63395EFDD78601005C6C47B5264DFFFE3062F41E66E1A2AA4B2072198BAFCA53D992BD02F138C4FCED83F942ED89A0AB285B9") //required
 * 				.withValidation(true) //default = true
 * 				.withApiErrorLogging(true) //default = true
 * 				.withHttpTimeout(60) //default = 60
 * 				.build();
 *
 * 		System.out.println("responseStatus = " + vaultClient.getAuthenticationResponse().getResponseStatus());
 * 		System.out.println("SessionId = " + vaultClient.getSessionId());
 * 		if (!vaultClient.hasSessionId()) {
 * 			System.out.println("Login failed");
 * 		}
 * 	}
 *	</pre>
 * 	<pre>
 * 	private static void testBasic(VaultClientId vaultClientId) throws Exception {
 * 		VaultClient vaultClient = VaultClientBuilder
 * 				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
 * 				.withVaultClientId(vaultClientId) //required
 * 				.withVaultDNS("verteobiotech.veevavault.com") //required
 * 				.withVaultUsername("vault.user@verteobiotech.com") //required
 * 				.withVaultPassword("Use$tr0ngPasswords") //required
 * 				.withValidation(true) //default = true
 * 				.withApiErrorLogging(true) //default = true
 * 				.withHttpTimeout(60) //default = 60
 * 				.build();
 *
 * 		System.out.println("responseStatus = " + vaultClient.getAuthenticationResponse().getResponseStatus());
 * 		System.out.println("SessionId = " + vaultClient.getSessionId());
 * 		if (!vaultClient.hasSessionId()) {
 * 			System.out.println("Login failed");
 * 		}
 * 	}
 * 	</pre>
 */
package com.veeva.vault.vapil.api.client;

