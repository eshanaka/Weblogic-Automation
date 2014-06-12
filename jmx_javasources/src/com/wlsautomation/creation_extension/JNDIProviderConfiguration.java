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
package com.wlsautomation.creation_extension;

import javax.management.ObjectName;

import com.wlsautomation.utils.JMXWrapper;
import com.wlsautomation.utils.WLSAutomationException;
import javax.management.Attribute;

public class JNDIProviderConfiguration 
{

    private JMXWrapper myJMXWrapper = null;

    public JNDIProviderConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

	
	public void createForeignJNDIProvider(String providerName, ObjectName[] targets, java.util.Properties properties)  throws WLSAutomationException
	 {
	 	try
	 	{
	 	     // e.g.: com.bea:Name=TestDomain,Type=Domain
	 	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

	 	     ObjectName myProviderMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
	 	                                                               "lookupForeignJNDIProvider",
	 	                                                               new Object[]{new String(providerName)},
	 	                                                               new String[]{String.class.getName()});
	 	     if (myProviderMBean==null)
	 	     {
	 	    	 // create
	 	    	 // Operation: javax.management.ObjectName  createForeignJNDIProvider(name:java.lang.String  )
	 	    	myProviderMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
                         "createForeignJNDIProvider",
                         new Object[]{new String(providerName)},
                         new String[]{String.class.getName()});
	 	    	
	 	    	//target to cluster
	 	    	myJMXWrapper.setAttribute(myProviderMBean, new Attribute("Targets",targets));
	 	    	
	 	    	// configure
	 	    	if (properties.containsKey("INITIALCONTEXTFACTORY"))
		 	    	myJMXWrapper.setAttribute(myProviderMBean, new Attribute("InitialContextFactory",properties.get("INITIALCONTEXTFACTORY")));
	 	    	
	 	    	if (properties.containsKey("PROVIDERURL"))
		 	    	myJMXWrapper.setAttribute(myProviderMBean, new Attribute("ProviderURL",properties.get("PROVIDERURL")));

	 	    	if (properties.containsKey("USER"))
		 	    	myJMXWrapper.setAttribute(myProviderMBean, new Attribute("User",properties.get("USER")));

	 	    	if (properties.containsKey("PASSWORD"))
		 	    	myJMXWrapper.setAttribute(myProviderMBean, new Attribute("Password",properties.get("PASSWORD")));

	 	    	// create and configure JNDI link		
	 	    	// Operation: javax.management.ObjectName  createForeignJNDILink(name:java.lang.String  )
		 	     ObjectName myForeignLinkMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
                          "createForeignJNDILink",
                          new Object[]{new String(providerName+"_Link")},
                          new String[]{String.class.getName()});
	 	    	
		 	    if (properties.containsKey("LOCALJNDINAME"))
			 	    myJMXWrapper.setAttribute(myForeignLinkMBean, new Attribute("LocalJNDIName",properties.get("LOCALJNDINAME")));

		 	    if (properties.containsKey("REMOTEJNDINAME"))
			 	    myJMXWrapper.setAttribute(myForeignLinkMBean, new Attribute("RemoteJNDIName",properties.get("REMOTEJNDINAME")));
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("Foreign JNDI provider "+providerName+" already exist  -  cannot create !");
	 	     
	 	}
	 	catch(Exception ex)
	 	{
	 		throw new WLSAutomationException(ex);
	 	}
	 }	
	
	public void deleteForeignJNDIProvider(String providerName)  throws WLSAutomationException
	 {
	 	try
	 	{
	 	     // e.g.: com.bea:Name=TestDomain,Type=Domain
	 	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

	 	     ObjectName myProviderMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
	 	                                                               "lookupForeignJNDIProvider",
	 	                                                               new Object[]{new String(providerName)},
	 	                                                               new String[]{String.class.getName()});
	 	     if (myProviderMBean!=null)
	 	     {
	 	    	 // delete
	 	    	 // Operation: java.lang.Void  destroyForeignJNDIProvider(provider:javax.management.ObjectName  )
	 	    	 myJMXWrapper.invoke(myDomainMBean,"destroyForeignJNDIProvider",new Object[]{new String(providerName)},new String[]{String.class.getName()});
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("Foreign JNDI provider "+providerName+" does not exist  -  cannot delete !");
	 	     
	 	}
	 	catch(WLSAutomationException ex)
	 	{
	 		throw ex;  // just re-throw
	 	}
	 	catch(Exception ex)
	 	{
	 		throw new WLSAutomationException(ex);
	 	}
	 }	
	
}
