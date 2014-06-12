/**
 * WebLogic Automation Book Source Code (JMX sources)
 * 
 * This file is part of the WLS-Automation book sourcecode software distribution. 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Martin Heinzl
 * Copyright (C) 2013 MH-EnterpriseConsulting, All rights reserved.
 *
 */
package com.wlsautomation.security;

import com.wlsautomation.utils.*;

import java.util.*;
import javax.management.ObjectName;
import javax.management.Attribute;


// weblogic specific
import weblogic.security.providers.saml.registry.SAMLAssertingParty;
import weblogic.security.providers.saml.registry.SAMLRelyingParty;

public class SecurityProviderConfiguration {
	
    private JMXWrapper myJMXWrapper = null;
    
    private String realmName = "myrealm";

    public SecurityProviderConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

    public SecurityProviderConfiguration(JMXWrapper _wrapper, String _realmName) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    	realmName = _realmName;
    }


    // Operation: javax.management.ObjectName  lookupAuthenticationProvider(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuthenticationProvider(name:java.lang.String  type:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuthenticationProvider(type:java.lang.String  )
	public ObjectName createAuthenticationProvider(String providerName, String providerType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean==null)
			{
				// create
				if (providerType==null)
					myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuthenticationProvider",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
				else
					myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuthenticationProvider",
	                        new Object[]{providerName, providerType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myAuthenticationProviderMBean;
			}
			else
				throw new WLSAutomationException("AuthenticationProvider with name "+providerName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	


	// Operation: javax.management.ObjectName  lookupAuthorizer(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuthorizer(name:java.lang.String  type:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuthorizer(type:java.lang.String  )
	public ObjectName createAuthorizer(String providerName, String providerType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myAuthorizerMBean==null)
			{
				// create
				if (providerType==null)
					myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuthorizer",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
				else
					myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuthorizer",
	                        new Object[]{providerName, providerType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myAuthorizerMBean;
			}
			else
				throw new WLSAutomationException("Authorizer with name "+providerName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
    
	// Operation: javax.management.ObjectName  createAdjudicator(type:java.lang.String  )
	// Operation: javax.management.ObjectName  createAdjudicator(name:java.lang.String  type:java.lang.String  )
	public ObjectName createAdjudicator(String providerName, String providerType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);
			ObjectName myAdjudicatorMBean = null;

			// create
			if (providerType==null)
					myAdjudicatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAdjudicator",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
			else
					myAdjudicatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAdjudicator",
	                        new Object[]{providerName, providerType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myAdjudicatorMBean;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	// Operation: javax.management.ObjectName  lookupAuditor(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuditor(name:java.lang.String  type:java.lang.String  )
	// Operation: javax.management.ObjectName  createAuditor(type:java.lang.String  )
	public ObjectName createAuditor(String providerName, String providerType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuditorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuditor",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myAuditorMBean==null)
			{
				// create
				if (providerType==null)
					myAuditorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuditor",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
				else
					myAuditorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createAuditor",
	                        new Object[]{providerName, providerType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myAuditorMBean;
			}
			else
				throw new WLSAutomationException("Auditor with name "+providerName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	// Operation: javax.management.ObjectName  lookupCertPathProvider(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createCertPathProvider(type:java.lang.String  )
	// Operation: javax.management.ObjectName  createCertPathProvider(name:java.lang.String  type:java.lang.String  )
	public ObjectName createCertPathProvider(String providerName, String providerType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myCertPathProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupCertPathProvider",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myCertPathProviderMBean==null)
			{
				// create
				if (providerType==null)
					myCertPathProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCertPathProvider",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
				else
					myCertPathProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCertPathProvider",
	                        new Object[]{providerName, providerType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myCertPathProviderMBean;
			}
			else
				throw new WLSAutomationException("CertPathProvider with name "+providerName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	// Operation: javax.management.ObjectName  lookupCredentialMapper(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createCredentialMapper(name:java.lang.String  type:java.lang.String  )
	// Operation: javax.management.ObjectName  createCredentialMapper(type:java.lang.String  )
	public ObjectName createCredentialMapper(String mapperName, String mapperType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupCredentialMapper",
										new Object[]{new String(mapperName)},
										new String[]{String.class.getName()});
			if (myCredentialMapperMBean==null)
			{
				// create
				if (mapperType==null)
					myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCredentialMapper",
	                        new Object[]{mapperName},
	                        new String[]{String.class.getName()});
				else
					myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCredentialMapper",
	                        new Object[]{mapperName, mapperType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myCredentialMapperMBean;
			}
			else
				throw new WLSAutomationException("CredentialMapper with name "+mapperName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
   

	// Creating a SAML 2 credential mapper
	public ObjectName createSAML2CredentialMapper(String mapperName, String mapperType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myCredentialMapperMBean =createCredentialMapper(mapperName,"weblogic.security.providers.saml.SAMLCredentialMapperV2");
			
			if (myCredentialMapperMBean==null)
			{
				// create
				if (mapperType==null)
					myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCredentialMapper",
	                        new Object[]{mapperName},
	                        new String[]{String.class.getName()});
				else
					myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createCredentialMapper",
	                        new Object[]{mapperName, mapperType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myCredentialMapperMBean;
			}
			else
				throw new WLSAutomationException("CredentialMapper with name "+mapperName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	
	// Operation: javax.management.ObjectName  lookupKeyStore(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createKeyStore(type:java.lang.String  )
	// Operation: javax.management.ObjectName  createKeyStore(name:java.lang.String  type:java.lang.String  )
	public ObjectName createKeyStore(String keystoreName, String keystoreType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myKeyStoreMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupKeyStore",
										new Object[]{new String(keystoreName)},
										new String[]{String.class.getName()});
			if (myKeyStoreMBean==null)
			{
				// create
				if (keystoreType==null)
					myKeyStoreMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createKeyStore",
	                        new Object[]{keystoreName},
	                        new String[]{String.class.getName()});
				else
					myKeyStoreMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createKeyStore",
	                        new Object[]{keystoreName, keystoreType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				return myKeyStoreMBean;
			}
			else
				throw new WLSAutomationException("KeyStore with name "+keystoreName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	// Operation: javax.management.ObjectName  lookupPasswordValidator(name:java.lang.String  )
	// Operation: javax.management.ObjectName  createPasswordValidator(name:java.lang.String  type:java.lang.String  )
	// Operation: javax.management.ObjectName  createPasswordValidator(subClass:java.lang.Class  name:java.lang.String  )
	// Operation: javax.management.ObjectName  createPasswordValidator(type:java.lang.String  )
	public ObjectName createPasswordValidator(String passwordValidatorName, String passwordValidatorType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myPasswordValidatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupPasswordValidator",
										new Object[]{new String(passwordValidatorName)},
										new String[]{String.class.getName()});
			if (myPasswordValidatorMBean==null)
			{
				// create
				if (passwordValidatorType==null)
					myPasswordValidatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createPasswordValidator",
	                        new Object[]{passwordValidatorName},
	                        new String[]{String.class.getName()});
				else
					myPasswordValidatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createPasswordValidator",
	                        new Object[]{passwordValidatorName, passwordValidatorType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				
				return myPasswordValidatorMBean;
			}
			else
				throw new WLSAutomationException("PasswordValidator with name "+passwordValidatorName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	// Operation: javax.management.ObjectName  lookupRoleMapper(java.lang.String:java.lang.String  )
	// Operation: javax.management.ObjectName  createRoleMapper(type:java.lang.String  )
	// Operation: javax.management.ObjectName  createRoleMapper(name:java.lang.String  type:java.lang.String  )	
	public ObjectName createRoleMapper(String roleMapperName, String roleMapperType)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupRoleMapper",
										new Object[]{new String(roleMapperName)},
										new String[]{String.class.getName()});
			if (myRoleMapperMBean==null)
			{
				// create
				if (roleMapperType==null)
					myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createRoleMapper",
	                        new Object[]{roleMapperName},
	                        new String[]{String.class.getName()});
				else
					myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "createRoleMapper",
	                        new Object[]{roleMapperName, roleMapperType},
	                        new String[]{String.class.getName(),String.class.getName()});
				
				// now do configuration
				// TO DO
				
				return myRoleMapperMBean;
			}
			else
				throw new WLSAutomationException("RoleMapper with name "+roleMapperName+" already exist  -  cannot create !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	

	public void deleteAuthenticationProvider(String providerName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean!=null)
			{
				// delete MBean
				myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyAuthenticationProvider",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("AuthenticationProvider with name "+providerName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	


	public void deleteAuthorizer(String authorizerName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(authorizerName)},
										new String[]{String.class.getName()});
			if (myAuthorizerMBean!=null)
			{
				// delete MBean
				myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyAuthorizer",
	                        new Object[]{authorizerName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("Authorizer with name "+authorizerName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
    

	public void deleteAdjudicator(String adjudicatorName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);
			
			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAdjudicatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAdjudicator",
										new Object[]{new String(adjudicatorName)},
										new String[]{String.class.getName()});
			if (myAdjudicatorMBean!=null)
			{
				// delete MBean
				myAdjudicatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyAdjudicator",
	                        new Object[]{adjudicatorName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("Adjudicator with name "+adjudicatorName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	


	public void deleteAuditor(String auditorName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuditorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuditor",
										new Object[]{new String(auditorName)},
										new String[]{String.class.getName()});
			if (myAuditorMBean!=null)
			{
				// delete MBean
				myAuditorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyAuditor",
	                        new Object[]{auditorName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("Auditor with name "+auditorName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	public void deleteCertPathProvider(String providerName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myCertPathProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupCertPathProvider",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myCertPathProviderMBean!=null)
			{
				// delete MBean
				myCertPathProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyCertPathProvider",
	                        new Object[]{providerName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("CertPathProvider with name "+providerName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	public void deleteCredentialMapper(String mapperName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupCredentialMapper",
										new Object[]{new String(mapperName)},
										new String[]{String.class.getName()});
			if (myCredentialMapperMBean!=null)
			{
				// delete MBean
				myCredentialMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyCredentialMapper",
	                        new Object[]{mapperName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("CredentialMapper with name "+mapperName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
   
	public void deleteKeyStore(String keystoreName)  throws WLSAutomationException
	{
		try
		{
			// e.g.: com.bea:Name=TestDomain,Type=Domain
			ObjectName securityRealmMBean = myJMXWrapper.getDomainConfigRoot();

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myKeyStoreMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupKeyStore",
										new Object[]{new String(keystoreName)},
										new String[]{String.class.getName()});
			if (myKeyStoreMBean!=null)
			{
				// delete MBean
				myKeyStoreMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyKeyStore",
	                        new Object[]{keystoreName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("KeyStore with name "+keystoreName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	public void deletePasswordValidator(String passwordValidatorName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myPasswordValidatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupPasswordValidator",
										new Object[]{new String(passwordValidatorName)},
										new String[]{String.class.getName()});
			if (myPasswordValidatorMBean!=null)
			{
				// delete MBean
				myPasswordValidatorMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyPasswordValidator",
	                        new Object[]{passwordValidatorName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("PasswordValidator with name "+passwordValidatorName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	public void deleteRoleMapper(String roleMapperName)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupRoleMapper",
										new Object[]{new String(roleMapperName)},
										new String[]{String.class.getName()});
			if (myRoleMapperMBean!=null)
			{
				// delete MBean
				myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
	                        "destroyRoleMapper",
	                        new Object[]{roleMapperName},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("RoleMapper with name "+roleMapperName+" does not exist  -  cannot delete !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	


	
	// ########################################################################################################
	// # concreate Providers
	// ########################################################################################################


	public void setControlFlagForAuthenticationProvider(String providerName, String controlFlag)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String(providerName)},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean!=null)
				myJMXWrapper.setAttribute(myAuthenticationProviderMBean, new Attribute("ControlFlag",controlFlag));
			else
				throw new WLSAutomationException("AuthenticationProvider with name "+providerName+" does not exist !");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
	
		
	public ObjectName createDefaultSAMLAuthenticationProvider(String providerName)  throws WLSAutomationException
	{
		try
		{
			// create identity asserter
			return createAuthenticationProvider(providerName, "weblogic.security.providers.saml.SAMLAuthenticator");
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

	public ObjectName createDefaultSAMLIdentityAsserter(String providerName)  throws WLSAutomationException
	{
		try
		{
			// create identity asserter
			return createAuthenticationProvider(providerName, "weblogic.security.providers.saml.SAMLIdentityAsserterV2");
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

	// NOTE: THis must be called on the RUNTIME (not Edit !!!) mbean tree
	public ObjectName createAssertingParty(String providerName, Properties myProps)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myIdentityAsserter = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
					"lookupAuthenticationProvider",
					new Object[]{new String(providerName)},
					new String[]{String.class.getName()});

			// CreateAssertingParty
	        SAMLAssertingParty mySAMLAssertingParty =(SAMLAssertingParty)myJMXWrapper.invoke(myIdentityAsserter,
	        		  		"newAssertingParty",new Object[0],new String[0]);

	        // setAssertingPartyValues
	        setAssertingPartyValues(mySAMLAssertingParty, myProps);

	        // send object back to JMX Server
	        // java.lang.Void addAssertingParty(assertingParty:weblogic.security.providers.saml.registry.SAMLAssertingParty 
	        String[] signature = new String[]{"weblogic.security.providers.saml.registry.SAMLAssertingParty"};
	        Object[] myValues  = new Object[]{mySAMLAssertingParty};

	        myJMXWrapper.invoke(myIdentityAsserter,"addAssertingParty",myValues,signature);
	        
	        return myIdentityAsserter;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	
	// NOTE: THis must be called on the RUNTIME (not Edit !!!) mbean tree
	public ObjectName updateAssertingparty(String providerName, String partner_id, Properties myProps)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myIdentityAsserter = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
					"lookupAuthenticationProvider",
					new Object[]{new String(providerName)},
					new String[]{String.class.getName()});
			
	        String[] signature = new String[]{"java.lang.String"};
	        Object[] values = new Object[]{partner_id};

	        // first check
	        // Operation: java.lang.Boolean assertingPartyExists(partnerId:java.lang.String  )
	        Boolean ap_exists = (Boolean)myJMXWrapper.invoke(myIdentityAsserter,"assertingPartyExists",
	        		  new Object[]{partner_id},
	        		  new String[]{"java.lang.String"});
	          
	        if (! ap_exists)
	        {
	            throw new WLSAutomationException("Asserting party with partner_id = "+partner_id+" does not exist !!");
	        }


	        // get AP
	        // Operation: weblogic.security.providers.saml.registry.SAMLAssertingParty getAssertingParty(partnerId:java.lang.String  )
	        SAMLAssertingParty mySAMLAssertingParty =	(SAMLAssertingParty)myJMXWrapper.invoke(myIdentityAsserter,
	        		  				"getAssertingParty",
	        		  				new Object[]{partner_id},
	                                new String[]{"java.lang.String"});

	        // change desired values
	        setAssertingPartyValues(mySAMLAssertingParty, myProps);
	        
	        // send object back to JMX Server
	        // Operation: java.lang.Void updateAssertingParty(assertingParty:weblogic.security.providers.saml.registry.SAMLAssertingParty)

	        signature = new String[]{"weblogic.security.providers.saml.registry.SAMLAssertingParty"};
	        values    = new Object[]{mySAMLAssertingParty};

	        myJMXWrapper.invoke(myIdentityAsserter,"updateAssertingParty",values,signature);
	        
	        return myIdentityAsserter;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

    private void setAssertingPartyValues(SAMLAssertingParty mySAMLAssertingParty, Properties myProps)  throws WLSAutomationException
    {
    	String nextValue = null;
    	try
    	{
            nextValue = (String)myProps.get("ASSERTERV2_ASSERTIONRETRIEVAL_URL");
            if (nextValue != null && !nextValue.equals("")) 
            	mySAMLAssertingParty.setAssertionRetrievalURL(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_AUDIENCE_URI");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setAudienceURI(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_ASSERTION_SIGNING_CERTALIAS");
            if (nextValue != null && !nextValue.equals(""))
            	mySAMLAssertingParty.setAssertionSigningCertAlias(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_DESCRIPTION");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setDescription(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_ENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setEnabled(new Boolean(nextValue));
            nextValue = (String)myProps.get("ASSERTERV2_GROUPSATTRIBUTEENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setGroupsAttributeEnabled(new Boolean(nextValue));
            nextValue = (String)myProps.get("ASSERTERV2_INTERSITETRANSFER_URL");
            if (nextValue != null && !nextValue.equals(""))
            	mySAMLAssertingParty.setIntersiteTransferURL(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_INTERSITETRANSFER_PARAMS");
            if (nextValue != null && !nextValue.equals(""))
            {
                String[] p = new String[1];
                p[0] = nextValue;
                mySAMLAssertingParty.setIntersiteTransferParams(p); //	Array of values expected
            }
            nextValue = (String)myProps.get("ASSERTERV2_ISSUER_URI");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setIssuerURI(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_NAMEMAPPER_CLASS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setNameMapperClass(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_PROFILE");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setProfile(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_PROTOCOL_SIGNING_CERTALIAS");
            if (nextValue != null && !nextValue.equals(""))
            	mySAMLAssertingParty.setProtocolSigningCertAlias(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_REDIRECT_URIS");
            if (nextValue != null && !nextValue.equals(""))
            {
                String[] p = new String[1];
                p[0] = nextValue;
                mySAMLAssertingParty.setRedirectURIs(p);  //	Array of values expected
            }
            nextValue = (String)myProps.get("ASSERTERV2_SIGNED_ASSERTIONS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setSignedAssertions(new Boolean(nextValue));
            nextValue = (String)myProps.get("ASSERTERV2_SOURCE_ID");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setSourceId(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_TARGET_URL");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setTargetURL(nextValue);
            nextValue = (String)myProps.get("ASSERTERV2_VIRTUAL_USER_ENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLAssertingParty.setVirtualUserEnabled(new Boolean(nextValue));
    		
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }




	public ObjectName createDefaultSAMLCredentialMapper(String mapperName, Properties myProps)  throws WLSAutomationException
	{
		try
		{
			// create credential mapper
			ObjectName myCredentialMapper = createCredentialMapper(mapperName, "weblogic.security.providers.saml.SAMLCredentialMapperV2");

			// first of all configure credential mapper
			if (myProps.containsKey("IssuerURI"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("IssuerURI",(String)myProps.get("IssuerURI")));
			if (myProps.containsKey("NameQualifier"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("NameQualifier",(String)myProps.get("NameQualifier")));
			if (myProps.containsKey("SigningKeyAlias"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("SigningKeyAlias",(String)myProps.get("SigningKeyAlias")));
			if (myProps.containsKey("SigningKeyPassPhrase"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("SigningKeyPassPhrase",(String)myProps.get("SigningKeyPassPhrase")));
			if (myProps.containsKey("DefaultTimeToLive"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("DefaultTimeToLive",Integer.parseInt((String)myProps.get("DefaultTimeToLive"))));
			if (myProps.containsKey("DefaultTimeToLiveDelta"))
				myJMXWrapper.setAttribute(myCredentialMapper, new Attribute("DefaultTimeToLiveDelta",Integer.parseInt((String)myProps.get("DefaultTimeToLiveDelta"))));

			return myCredentialMapper;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

	// NOTE: THis must be called on the RUNTIME (not Edit !!!) mbean tree
    public ObjectName createRelyingParty(String mapperName, Properties myProps)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myCredentialMapper = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
					"lookupCredentialMapper",
					new Object[]{new String(mapperName)},
					new String[]{String.class.getName()});
			
			// create relying party
	        SAMLRelyingParty mySAMLRelyingParty =	(SAMLRelyingParty)myJMXWrapper.invoke(myCredentialMapper,
	                                                  "newRelyingParty",
	                                                  new Object[0],new String[0]);

	        // set the values
	        setRelyingPartyValues(mySAMLRelyingParty,myProps);

	        // send object back to JMX Server
	        String[] signature = new String[]{"weblogic.security.providers.saml.registry.SAMLRelyingParty"};
	        Object[] myValues  = new Object[]{mySAMLRelyingParty};

	        myJMXWrapper.invoke(myCredentialMapper,"addRelyingParty",myValues,signature);
	        
	        return myCredentialMapper;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

	
	// NOTE: THis must be called on the RUNTIME (not Edit !!!) mbean tree
    public ObjectName updateRelyingParty(String mapperName, String partner_id, Properties myProps)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myCredentialMapper = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
					"lookupCredentialMapper",
					new Object[]{new String(mapperName)},
					new String[]{String.class.getName()});

			
	        // first check
	        Boolean ap_exists = (Boolean)myJMXWrapper.invoke(myCredentialMapper,"relyingPartyExists",
	        		  new Object[]{partner_id},
	        		  new String[]{"java.lang.String"});
	          
	        if (! ap_exists)
	        {
	            throw new WLSAutomationException("Relying party with partner_id = "+partner_id+" does not exist !!");
	        }


	        
	        // get RP
	        SAMLRelyingParty mySAMLRelyingParty = (SAMLRelyingParty)myJMXWrapper.invoke(myCredentialMapper,
	        		                                                  "getRelyingParty",
	        		                                                  new Object[]{partner_id},
	        		              	                                  new String[]{"java.lang.String"});

	        // change desired values
	        setRelyingPartyValues(mySAMLRelyingParty,myProps);
	        
	        // send object back to JMX Server
	        String[] signature = new String[]{"weblogic.security.providers.saml.registry.SAMLRelyingParty"};
	        Object[] myValues  = new Object[]{mySAMLRelyingParty};

	        myJMXWrapper.invoke(myCredentialMapper,"updateRelyingParty",myValues,signature);
	        
	        return myCredentialMapper;
		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		

	
	
    private void setRelyingPartyValues(SAMLRelyingParty mySAMLRelyingParty, Properties myProps)  throws WLSAutomationException
    {
    	String nextValue = null;
    	try
    	{
            nextValue = (String)myProps.get("RELYING_PARTY_KEYINFO_INCLUDED");
            if (nextValue != null && !nextValue.equals("")) 
            	mySAMLRelyingParty.setKeyinfoIncluded(new Boolean(nextValue));

            nextValue = (String)myProps.get("RELYING_PARTY_ASSERTION_CONSUMER_PARAMS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setAssertionConsumerParams(new String[]{nextValue}); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_ASSERTIONS_CONSUMER_URL");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setAssertionConsumerURL(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_AUDIENCE_URI");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setAudienceURI(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_CREDENTIAL_CACHE_ENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setCredentialCacheEnabled(new Boolean(nextValue)); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_DESCRIPTION");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setDescription(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_DONOT_CACHE_CONDITION");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setDoNotCacheCondition(new Boolean(nextValue)); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_ENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setEnabled(new Boolean(nextValue)); 
            
            nextValue = (String)myProps.get("RELYING_PARTY_GROUPSATTRIBUTE_ENABLED");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setGroupsAttributeEnabled(new Boolean(nextValue)); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_NAMEMAPPER_CLASS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setNameMapperClass(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_POSTFORM");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setPostForm(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_PROFILE");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setProfile(nextValue); 
    		
            nextValue = (String)myProps.get("RELYING_PARTY_SIGNED_ASSERTIONS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setSignedAssertions(new Boolean(nextValue)); 
            
            nextValue = (String)myProps.get("RELYING_PARTY_SSLCLIENT_CERTALIAS");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setSSLClientCertAlias(nextValue); 
            
            nextValue = (String)myProps.get("RELYING_PARTY_TARGET_URL");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setTargetURL(nextValue); 
            
            nextValue = (String)myProps.get("RELYING_PARTY_TIME_TO_LIVE");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setTimeToLive(Integer.parseInt(nextValue)); 
            
            nextValue = (String)myProps.get("RELYING_PARTY_TIME_TO_LIVE_OFFSET");
            if (nextValue != null && !nextValue.equals(""))
                mySAMLRelyingParty.setTimeToLiveOffset(Integer.parseInt(nextValue)); 
            
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }


    
	public void configureDefaultIdentityAsserterForX509()  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// Operation: javax.management.ObjectName  lookupMailSession(name:java.lang.String  )
			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String("DefaultIdentityAsserter")},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean!=null)
			{	
			    // set the active types to x.509 only
			    myJMXWrapper.setAttribute(myAuthenticationProviderMBean, new Attribute("ActiveTypes",new String[]{"AuthenticatedUser","X.509"}));
			    // define the X.509 attribute which should be used for the name
				myJMXWrapper.setAttribute(myAuthenticationProviderMBean, new Attribute("UseDefaultUserNameMapper",new Boolean(true)));
			    myJMXWrapper.setAttribute(myAuthenticationProviderMBean, new Attribute("DefaultUserNameMapperAttributeType","CN"));
			}    
			else
				throw new WLSAutomationException("DefaultIdentityAsserter does not exist !");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
    

    // Create an instance of the XACML authorization provider
	public ObjectName createDefaultXACMLAuthorizationProvider(String providerName)  throws WLSAutomationException
	{
		try
		{
			// create identity asserter
			return createAuthorizer(providerName, "weblogic.security.providers.xacml.authorization.XACMLAuthorizer");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}		
	

	
	// Create a password validator
	public ObjectName createDefaultPasswordValidator(String providerName)  throws WLSAutomationException
	{
		try {
			// create identity asserter
			return createPasswordValidator(providerName, "com.bea.security.providers.authentication.passwordvalidator.SystemPasswordValidator");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
	


	// Creating an adjudication provider
	public ObjectName createDefaultAdjudicator(String providerName)  throws WLSAutomationException
	{
		try {
			// create identity asserter
			return createAdjudicator(providerName, "weblogic.security.providers.authorization.DefaultAdjudicator");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
	

	// Creating a Role Mapping Provider
	public ObjectName createDefaultRoleMapper(String mapperName)  throws WLSAutomationException
	{
		try {
			// create identity asserter
			return createRoleMapper(mapperName, "weblogic.security.providers.authorization.DefaultRoleMapper");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	



	// creating the default credential mapper
	public ObjectName createDefaultCredentialMapper(String mapperName)  throws WLSAutomationException
	{
		try {
			// create identity asserter
			return createCredentialMapper(mapperName, "weblogic.security.providers.credentials.DefaultCredentialMapper");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	


	// creating the PKI credential mapper
	public ObjectName createPKICredentialMapper(String mapperName)  throws WLSAutomationException
	{
		try {
			// create identity asserter
			return createCredentialMapper(mapperName, "weblogic.security.providers.credentials.PKICredentialMapper");
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	

	
	
	public void reorderProviders(ObjectName[] providerRefs)  throws WLSAutomationException
	{
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			// reorder only means to set the list of providers as an array
			myJMXWrapper.setAttribute(securityRealmMBean, new Attribute("AuthenticationProviders",providerRefs));
		}	
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
	}	
	
	
}



/*


In order to clean up the events of the WebLogic standard-out we will redirect to the security audit provider. This of course
requires an audit provider to be configured. As discussed above, WebLogic has a default security audit provider which can be
used for this purpose.
The attribute setConfigurationAuditType has 4 different values:
None = Configuration events will neither be written to the server log or directed to the Security Audit Framework.
Change Log = Configuration events will be written to the server log.
Change Audit = Configuration events will be directed to the Security Audit Framework.
Change Log and Audit = Configuration events will be written to the server log and directed to the Security Audit
Framework.
cd('/')
cmo.setConfigurationAuditType('audit')
# set domain logging configurations
cd('/Servers/AdminServer/Log/AdminServer')
cmo.setLoggerSeverity('Warning')
cmo.setDomainLogBroadcastSeverity('Warning')
cmo.setLogFileSeverity('Info')
cmo.setStdoutSeverity('Info')
cmo.setMemoryBufferSeverity('Debug')
# change to the realm
cd('/SecurityConfiguration/MartinTest_Domain/Realms/myrealm')
# create an auditor instance
cmo.createAuditor('NewTestDefAuditor', 'weblogic.security.providers.audit.DefaultAuditor')
# change to the new auditor
cd('/SecurityConfiguration/MartinTest_Domain/Realms/myrealm/Auditors/NewTestDefAuditor')
# enable information level audit event logging
cmo.setInformationAuditSeverityEnabled(true)
# enable warning level audit event logging
cmo.setWarningAuditSeverityEnabled(true)
# enable failure level audit event logging
cmo.setFailureAuditSeverityEnabled(true)
# enable error level audit event logging
cmo.setErrorAuditSeverityEnabled(true)
cmo.setSeverity('WARNING')
# set active handler entries. This example sets all handlers available just to demonstrate what is available
set('ActiveContextHandlerEntries',jarray.array([String('com.bea.contextelement.channel.Address'),
String('com.bea.contextelement.channel.ChannelName'), String('com.bea.contextelement.channel.Port'),
String('com.bea.contextelement.channel.Protocol'), String('com.bea.contextelement.channel.PublicAddress'),
String('com.bea.contextelement.channel.PublicPort'), String('com.bea.contextelement.channel.RemoteAddress'),
String('com.bea.contextelement.channel.RemotePort'), String('com.bea.contextelement.channel.Secure'),
String('com.bea.contextelement.ejb20.Parameter'), String('com.bea.contextelement.entitlement.EAuxiliaryID'),
String('com.bea.contextelement.jmx.AuditProtectedArgInfo'), String('com.bea.contextelement.jmx.ObjectName'),
String('com.bea.contextelement.jmx.OldAttributeValue'), String('com.bea.contextelement.jmx.Parameters'),
String('com.bea.contextelement.jmx.ShortName'), String('com.bea.contextelement.jmx.Signature'),
String('com.bea.contextelement.saml.MessageSignerCertificate'),
String('com.bea.contextelement.saml.SSLClientCertificateChain'),
String('com.bea.contextelement.saml.subject.ConfirmationMethod'), String('com.bea.contextelement.saml.subject.dom.KeyInfo'),
String('com.bea.contextelement.security.ChainPrevailidatedBySSL'),
String('com.bea.contextelement.servlet.HttpServletRequest'), String('com.bea.contextelement.servlet.HttpServletResponse'),
String('com.bea.contextelement.webservice.Integrity'), String('com.bea.contextelement.wli.Message'),
String('com.bea.contextelement.wsee.SOAPMessage'), String('com.bea.contextelement.

*/