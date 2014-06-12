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
import javax.management.ObjectName;



public class XACMLConfiguration {
	
    private JMXWrapper myJMXWrapper = null;
    
    private String realmName = "myrealm";

    public XACMLConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

    public XACMLConfiguration(JMXWrapper _wrapper, String _realmName) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    	realmName = _realmName;
    }

    
    // export authentication data based on XACML
    public void addPolicySet(String securityProviderName, String myPolicySet) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthorizationMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthorizationMBean!=null)
			{
				// # export DefaultAtn type of data
				// cmo.exportData("DefaultAtn",fileName,Properties())
				myJMXWrapper.invoke(myAuthorizationMBean,
	                        "addPolicySet",
	                        new Object[]{myPolicySet},
	                        new String[]{String.class.getName()});
			}
			else
				throw new WLSAutomationException("AuthorizationProvider with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }

    // export authentication data based on XACML
    public void addPolicy(String securityProviderName, String myPolicy) throws WLSAutomationException
    {
		try
		{
			ObjectName securityRealmMBean =new ObjectName("Security:Name="+realmName);

			ObjectName myAuthorizationMBean = (ObjectName)myJMXWrapper.invoke(securityRealmMBean,
										"lookupAuthorizer",
										new Object[]{new String(securityProviderName)},
										new String[]{String.class.getName()});
			if (myAuthorizationMBean!=null)
			{
				try {
				    myJMXWrapper.invoke(myAuthorizationMBean,
	                        "addPolicy",
	                        new Object[]{myPolicy},
	                        new String[]{String.class.getName()});
				}
				//catch( weblogic.management.utils.AlreadyExistsException ex)
				//catch( javax.management.MBeanException ex)
				catch( Exception ex)
				{
					System.out.println("EX: "+ex.getClass().getName());
					
					
					System.out.println("Policy already exists - will modify it ! " );
				    myJMXWrapper.invoke(myAuthorizationMBean,
	                        "modifyPolicy",
	                        new Object[]{myPolicy},
	                        new String[]{String.class.getName()});
					
				}
			}
			else
				throw new WLSAutomationException("AuthorizationProvider with name "+securityProviderName+"  does not exist !");

		}	
		catch(WLSAutomationException ex) {
			throw ex;  // just re-throw
		}
		catch(Exception ex) {
			throw new WLSAutomationException(ex);
		}
    }

}

