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
package com.tests;

import com.wlsautomation.security.*;
import com.wlsautomation.utils.JMXWrapperRemote;

import java.util.*;

import javax.management.ObjectName;
import javax.management.Attribute;





public class SecurityProviderTest {
	
	
	public static void main(String[] args) throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		//myJMXWrapperRemote.connectToAdminServer(false,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		SecurityProviderConfiguration mySecurityProviderConfiguration = new SecurityProviderConfiguration(myJMXWrapperRemote);
		

		
		// TEST
		mySecurityProviderConfiguration.setControlFlagForAuthenticationProvider("DefaultAuthenticator", "OPTIONAL");

		mySecurityProviderConfiguration.createDefaultSAMLAuthenticationProvider("MyTestSAMLAUthenticator");
		
		
		mySecurityProviderConfiguration.configureDefaultIdentityAsserterForX509(); 
		
		mySecurityProviderConfiguration.createDefaultXACMLAuthorizationProvider("MyDefXCAMLAuthProvider"); 
		
		//mySecurityProviderConfiguration.createDefaultAdjudicator("myDefAdjudicator") ;
		mySecurityProviderConfiguration.createDefaultRoleMapper("MyDefRoleMapper") ;
		mySecurityProviderConfiguration.createDefaultCredentialMapper("DefaultCredMapper") ;
	
		testCreatePasswordValidator(mySecurityProviderConfiguration, myJMXWrapperRemote);
		
		testCreatePKICredentialMapper(mySecurityProviderConfiguration, myJMXWrapperRemote);
		
		testCreateSAMLCredentialMapper(mySecurityProviderConfiguration);
		
		mySecurityProviderConfiguration.createDefaultSAMLIdentityAsserter("MyDefSAMLIDentityAsserter") ;
		
		
		
		// now switch to runtime and create asserting and relying party
	    testCreateSAMLRelyingParty(mySecurityProviderConfiguration);
		
	}
	

	
	private static void testCreateSAMLCredentialMapper(SecurityProviderConfiguration mySecurityProviderConfiguration) throws Exception
	{
		Properties myProps = new Properties();
		myProps.setProperty("IssuerURI","http://myservices.book.com/serviceURL");
		myProps.setProperty("NameQualifier","myservices.book.com");
		myProps.setProperty("DefaultTimeToLive","120");
		myProps.setProperty("DefaultTimeToLiveDelta","-10");
		myProps.setProperty("SigningKeyAlias","webservice_alias");
		myProps.setProperty("SigningKeyPassPhrase",",mypassword");

		mySecurityProviderConfiguration.createDefaultSAMLCredentialMapper("TestSAMLCredMapperWLSTB", myProps);
	}

	// RUNTIME !!
	public static void testCreateSAMLRelyingParty(SecurityProviderConfiguration mySecurityProviderConfiguration) throws Exception
	{
		Properties myProps = new Properties();
		myProps.setProperty("RELYING_PARTY_PROFILE","WSS/Sender-Vouches");
		myProps.setProperty("RELYING_PARTY_KEYINFO_INCLUDED","true");
		myProps.setProperty("RELYING_PARTY_DESCRIPTION","rp_wlsbook");
		myProps.setProperty("RELYING_PARTY_ENABLED","true");
		myProps.setProperty("RELYING_PARTY_GROUPSATTRIBUTE_ENABLED","false");
		myProps.setProperty("RELYING_PARTY_SIGNED_ASSERTIONS","true");
		myProps.setProperty("RELYING_PARTY_TARGET_URL","default");
		myProps.setProperty("RELYING_PARTY_TIME_TO_LIVE","100");
		myProps.setProperty("RELYING_PARTY_TIME_TO_LIVE_OFFSET","-15");		
		
		mySecurityProviderConfiguration.createRelyingParty("TestSAMLCredMapperWLSTB", myProps);
	}
	
	
	public static void testCreatePKICredentialMapper(SecurityProviderConfiguration mySecurityProviderConfiguration, JMXWrapperRemote myJMXWrapperRemote) throws Exception
	{
		ObjectName myCredMapper = mySecurityProviderConfiguration.createPKICredentialMapper("TESTPKICredMapper") ;
		
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("KeyStoreProvider","SUN"));
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("KeyStoreType","JKS"));
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("KeyStoreFileName","/opt/book/teststores/mykeystore"));
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("KeyStorePassPhrase","secretBook"));
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("UseResourceHierarchy",true));
		myJMXWrapperRemote.setAttribute(myCredMapper, new Attribute("UseInitiatorGroupNames",true));
	}
	

	public static void testCreatePasswordValidator(SecurityProviderConfiguration mySecurityProviderConfiguration, JMXWrapperRemote myJMXWrapperRemote) throws Exception
	{
		ObjectName myPasswordvalidator= mySecurityProviderConfiguration.createDefaultPasswordValidator("MyDefPassWordValid") ;
		
		myJMXWrapperRemote.setAttribute(myPasswordvalidator, new Attribute("MaxConsecutiveCharacters",new Integer(0)));
		// set the max instance of each character to 3
		myJMXWrapperRemote.setAttribute(myPasswordvalidator, new Attribute("MaxInstancesOfAnyCharacter",new Integer(3)));
		// set the minimal number of alphabetic chars to 5
		myJMXWrapperRemote.setAttribute(myPasswordvalidator, new Attribute("MinAlphabeticCharacters",new Integer(5)));
		// set the min. of upper case characters to 2
		myJMXWrapperRemote.setAttribute(myPasswordvalidator, new Attribute("MinUppercaseCharacters",new Integer(2)));
		// set the min. of lower case characters to 2
		myJMXWrapperRemote.setAttribute(myPasswordvalidator, new Attribute("MinLowercaseCharacters",new Integer(2)));
	}

}
