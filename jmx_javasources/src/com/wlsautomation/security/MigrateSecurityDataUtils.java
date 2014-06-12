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



public class MigrateSecurityDataUtils {
	
    private JMXWrapper myJMXWrapper = null;
    
    private String realmName = "myrealm";

    public MigrateSecurityDataUtils(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

    public MigrateSecurityDataUtils(JMXWrapper _wrapper, String _realmName) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    	realmName = _realmName;
    }

    
    // export authentication data based on XACML
    public void exportAuthenticatorData(String securityProviderName, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean!=null)
			{
				// # export DefaultAtn type of data
				// cmo.exportData("DefaultAtn",fileName,Properties())
				myJMXWrapper.invoke(myAuthenticationProviderMBean,
	                        "exportData",
	                        new Object[]{"DefaultAtn",fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("AuthenticationProvider with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }

    
    // import authentication data based on XACML
    public void importAuthenticatorData(String securityProviderName, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthenticationProviderMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthenticationProvider",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthenticationProviderMBean!=null)
			{
				// # import DefaultAtn type of data
				// cmo.importData("DefaultAtn",fileName,Properties())
				myJMXWrapper.invoke(myAuthenticationProviderMBean,
	                        "importData",
	                        new Object[]{"DefaultAtn",fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("AuthenticationProvider with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }
    

    
    // export authorizer data based on XACML
    public void exportAuthorizerData(String securityProviderName, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthorizerMBean!=null)
			{
				// # export DefaultAtn type of data
				// cmo.exportData("DefaultAtn",fileName,Properties())
				myJMXWrapper.invoke(myAuthorizerMBean,
	                        "exportData",
	                        new Object[]{"XACML",fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("Authorizer with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }
    
    
    // import authorizer data based on XACML
    public void importAuthorizerData(String securityProviderName, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthorizerMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthorizerMBean!=null)
			{
				// # export DefaultAtn type of data
				// cmo.exportData("DefaultAtn",fileName,Properties())
				myJMXWrapper.invoke(myAuthorizerMBean,
	                        "importData",
	                        new Object[]{"XACML",fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("Authorizer with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }    
    
    
    
    // export rolemapper data: type can either bei XACML or DefaultRoles
    public void exportRoleMapperData(String roleMapperName, String exportFormat, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
								"lookupRoleMapper",
								new Object[]{new String(roleMapperName)},
								new String[]{String.class.getName()});
			if (myRoleMapperMBean!=null)
			{
				// export <exportFormat> type of data
				// cmo.exportData(exportFormat,fileName,Properties())
				myJMXWrapper.invoke(myRoleMapperMBean,
	                        "exportData",
	                        new Object[]{exportFormat,fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("RoleMapper with name "+roleMapperName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }
    

    // import rolemapper data: type can either bei XACML or DefaultRoles
    public void importRoleMapperData(String roleMapperName, String exportFormat, String fileName) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myRoleMapperMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
								"lookupRoleMapper",
								new Object[]{new String(roleMapperName)},
								new String[]{String.class.getName()});
			if (myRoleMapperMBean!=null)
			{
				// import <exportFormat> type of data
				// cmo.importData(exportFormat,fileName,Properties())
				myJMXWrapper.invoke(myRoleMapperMBean,
	                        "importData",
	                        new Object[]{exportFormat,fileName, new Properties()},
	                        new String[]{String.class.getName(),String.class.getName(),Properties.class.getName()});
			}
			else
				throw new WLSAutomationException("RoleMapper with name "+roleMapperName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }
}
