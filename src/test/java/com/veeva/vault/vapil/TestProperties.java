/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class TestProperties {
	// The Vault API success status, for JUNIT testing
	public final static String VAULT_API_SUCCESS_STATUS = "SUCCESS";
	
	private final String PROPERTY_FILE = "test.properties";
	private final Properties properties;
	
	private static Logger log = Logger.getLogger(TestProperties.class);
	
	public TestProperties() {
		
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE));
        } catch (IOException e) {
        	log.error(PROPERTY_FILE + " error - " + e.getMessage());
        }
    }
	
	/*
	 * 
	 * Vault Client Id Properties
	 * 
	 */

	/*
	 * 
	 * Domain and User Name Properties
	 * 
	 */
	public String getVaultClientId() {
		return properties.getProperty("vault.clientid");
	}
    public String getVaultDNS() {
        return properties.getProperty("vault.dns");
    }
    public String getVaultDNS(String propertyName) {
        return properties.getProperty(propertyName+".dns");
    }
    public String getVaultUsername() {
        return properties.getProperty("vault.username");
    }
    public String getVaultPassword() {
        return properties.getProperty("vault.password");
    }
	public String getVaultOAuthProfileId() {
		return properties.getProperty("vault.oauthProfileId");
	}
    public String getVaultOAuthClientId() {
        return properties.getProperty("vault.oauthClientId");
    }
    public String getIdpUserName() {
        return properties.getProperty("idp.username");
    }
    public String getIdpUserPwd() {
        return properties.getProperty("idp.password");
    }
	public String getIdpOauthAccessToken() {
		return properties.getProperty("vault.oauthAccessToken");
	}
	public String getIdpOauthScope() {
		return properties.getProperty("idp.scope");
	}
	public String getSessionId() {
		return properties.getProperty("vault.sessionid");
	}

    /* 
     * 
     * Transactional Properties
     * 
     */
    public String getTestFile() {
        return properties.getProperty("testfile");
    }
}
