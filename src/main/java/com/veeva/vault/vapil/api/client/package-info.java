/**
 * HTTPS Client for the Vault API
 *
 * <h3>Authenticate with a Session ID</h3>
 * <pre>
 *      VaultClient vaultClient = VaultClient
 *           .newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
 *           .withVaultClientId("verteobiotech-vault-demo") //required
 *           .withVaultDNS("verteobiotech.veevavault.com") //required
 *           .withVaultSessionId("123456ABCDEFG") //required
 *           .withValidation(true) //default = true
 *           .withApiErrorLogging(true) //default = true
 *           .withHttpTimeout(60) //default = 60
 *           .build();
 *
 *      System.out.println("Response Status: " + vaultClient.getAuthenticationResponse().getResponseStatus());
 *      System.out.println("SessionId: " + vaultClient.getSessionId());
 *      if (!vaultClient.hasSessionId()) {
 *          System.out.println("Login failed");
 *      }
 * </pre>
 *
 * <h3>Authenticate with username and password</h3>
 * <pre>
 *      VaultClient vaultClient = VaultClient
 *           .newClientBuilder(VaultClient.AuthenticationType.BASIC)
 *           .withVaultClientId("verteobiotech-vault-demo") //required
 *           .withVaultDNS("verteobiotech.veevavault.com") //required
 *           .withVaultUsername("vault.user@verteobiotech.com") //required
 *           .withVaultPassword("Use$tr0ngPasswords") //required
 *           .withValidation(true) //default = true
 *           .withApiErrorLogging(true) //default = true
 *           .withHttpTimeout(60) //default = 60
 *           .build();
 *
 *      System.out.println("Response Status: " + vaultClient.getAuthenticationResponse().getResponseStatus());
 *      System.out.println("SessionId: " + vaultClient.getSessionId());
 *      if (!vaultClient.hasSessionId()) {
 *          System.out.println("Login failed");
 *      }
 * </pre>
 *
 * <h3>Authenticate with a JSON settings file</h3>
 * Example settings files can be found <a href="https://github.com/veeva/vault-api-library/tree/main/src/test/resources/settings_files">here</a>
 * <pre>
 *      File settingsFile = new File("settings.json");
 *
 *      VaultClient vaultClient = VaultClient
 *         .newClientBuilderFromSettings(settingsFile)
 *         .build();
 *
 *      System.out.println("responseStatus = " + vaultClient.getAuthenticationResponse().getResponseStatus());
 *      System.out.println("SessionId = " + vaultClient.getSessionId());
 *      if (!vaultClient.hasSessionId()) {
 *          System.out.println("Login failed");
 *      }
 * </pre>
 */
package com.veeva.vault.vapil.api.client;

