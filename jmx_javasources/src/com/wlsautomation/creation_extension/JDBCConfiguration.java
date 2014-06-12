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

import com.wlsautomation.utils.*;

import javax.management.ObjectName;
import javax.management.Attribute;


public class JDBCConfiguration {

    private JMXWrapper myJMXWrapper = null;

    public JDBCConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }
	
    /**
     * Create a datasource either using user/password or using wallets
     *
     * @param datasourcename String  Name of the new datasource
     * @param clustername String  Name of the target cluster
     * @param jndiName String  JNDI entry name
     * @param globalTransactionsProtocol String   transaction setting
     * @param drivername String   name of the driver
     * @param url String   database URL - NOTE: in case of wallets don't forget the "/"
     * @param username String  username - ONLY necessary if useWallet=false
     * @param password String  password - ONLY necessary if useWallet=false
     * @param useWallet boolean does this datasource use wallets true/false
     * @param walletlocation String  wallet directory - ONLY necessary if useWallet=true
     * @throws Exception
     */
    public void createDataSource(String datasourcename,
                                 String clustername,
                                 String jndiName,
                                 String globalTransactionsProtocol,
                                 String drivername,
                                 String url,
                                 String username,
                                 String password,
                                 boolean useWallet,
                                 String walletlocation) throws WLSAutomationException
    {
    	try
    	{
    	     // e.g.: com.bea:Name=TestDomain,Type=Domain
    	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

    	     // javax.management.ObjectName  createJDBCSystemResource(name:java.lang.String  )
    	     ObjectName mySystemResourceMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
    	                                                               "createJDBCSystemResource",
    	                                                               new Object[]{new String(datasourcename)},
    	                                                               new String[]{String.class.getName()});

    	     ObjectName myJDBCResourceMBean = (ObjectName)myJMXWrapper.getAttribute(mySystemResourceMBean,"JDBCResource");
    	     ObjectName myJDBCDataSourceParamsMBean = (ObjectName)myJMXWrapper.getAttribute(myJDBCResourceMBean,"JDBCDataSourceParams");
    	     ObjectName myJDBCDriverParamsMBean = (ObjectName)myJMXWrapper.getAttribute(myJDBCResourceMBean,"JDBCDriverParams");
    	     ObjectName myJDBCConnectionPoolParamsMBean = (ObjectName)myJMXWrapper.getAttribute(myJDBCResourceMBean,"JDBCConnectionPoolParams");

    	     // set attribute ListenAddress
    	     myJMXWrapper.setAttribute(myJDBCResourceMBean,new Attribute("Name",datasourcename));

    	     // set JNDI names
    	     myJMXWrapper.setAttribute(myJDBCDataSourceParamsMBean,new Attribute("JNDINames",new String[]{jndiName}));

    	     // set GlobalTransactionsProtocol
    	     myJMXWrapper.setAttribute(myJDBCDataSourceParamsMBean,new Attribute("GlobalTransactionsProtocol",new String(globalTransactionsProtocol)));

    	     // set TestTableName
    	     myJMXWrapper.setAttribute(myJDBCConnectionPoolParamsMBean,new Attribute("TestTableName",new String("SQL SELECT * FROM DUAL")));

    	     // set URL
    	     myJMXWrapper.setAttribute(myJDBCDriverParamsMBean,new Attribute("Url",new String(url)));

    	     // set DriverName
    	     myJMXWrapper.setAttribute(myJDBCDriverParamsMBean,new Attribute("DriverName",new String(drivername)));

    	     if (! useWallet)
    	     {
    	         // set Password
    	         myJMXWrapper.setAttribute(myJDBCDriverParamsMBean, new Attribute("Password", new String(password)));

    	         // user
    	         ObjectName myJDBCDriverParamsPropertiesMBean = (ObjectName) myJMXWrapper.getAttribute(myJDBCDriverParamsMBean, "Properties");
    	         ObjectName myUserPropertyMBean = null;
    	         try {
    	             myUserPropertyMBean = (ObjectName) myJMXWrapper.invoke(
    	                     myJDBCDriverParamsPropertiesMBean,
    	                     "lookupProperty",
    	                     new Object[] {new String("user")},
    	                     new String[] {String.class.getName()});
    	         } catch (Exception ex) {
    	             // ignore
    	         }
    	         if (myUserPropertyMBean == null)
    	             myUserPropertyMBean = (ObjectName) myJMXWrapper.invoke(
    	                     myJDBCDriverParamsPropertiesMBean,
    	                     "createProperty",
    	                     new Object[] {new String("user")},
    	                     new String[] {String.class.getName()});
    	         myJMXWrapper.setAttribute(myUserPropertyMBean, new Attribute("Value", new String(username)));
    	     }
    	     else // WALLET !!
    	     {
    	         // user
    	         ObjectName myJDBCDriverParamsPropertiesMBean = (ObjectName) myJMXWrapper.getAttribute(myJDBCDriverParamsMBean, "Properties");
    	         ObjectName myWalletLocationPropertyMBean = null;
    	         try {
    	             myWalletLocationPropertyMBean = (ObjectName) myJMXWrapper.invoke(
    	                     myJDBCDriverParamsPropertiesMBean,
    	                     "lookupProperty",
    	                     new Object[] {new String("oracle.net.wallet_location")},
    	                     new String[] {String.class.getName()});
    	         } catch (Exception ex) {
    	             // ignore
    	         }
    	         if (myWalletLocationPropertyMBean == null)
    	             myWalletLocationPropertyMBean = (ObjectName) myJMXWrapper.invoke(
    	                     myJDBCDriverParamsPropertiesMBean,
    	                     "createProperty",
    	                     new Object[] {new String("oracle.net.wallet_location")},
    	                     new String[] {String.class.getName()});
    	         myJMXWrapper.setAttribute(myWalletLocationPropertyMBean, new Attribute("Value", new String(walletlocation)));
    	     }

    	     // set target cluster.  Note that in thi example it is exactly one cluster, but this does not need to be.
    	     myJMXWrapper.setAttribute(mySystemResourceMBean,new Attribute("Targets",new ObjectName[]{new ObjectName("com.bea:Name="+clustername+",Type=Cluster")} ));
    	}
    	catch(Exception ex)
    	{
    		throw new WLSAutomationException(ex);
    	}
    }

	
    public void deleteDataSource(String datasourcename) throws Exception
    {
    	try
    	{
   	     // e.g.: com.bea:Name=TestDomain,Type=Domain
   	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

	     // Operation: javax.management.ObjectName  lookupJDBCSystemResource(name:java.lang.String  )	
	     ObjectName mySystemResourceMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
	                                                               "lookupJDBCSystemResource",
	                                                               new Object[]{new String(datasourcename)},
	                                                               new String[]{String.class.getName()});
	     if (mySystemResourceMBean!=null)
	     {
	   	     // ok, found => delete it now !
	   	     myJMXWrapper.invoke(myDomainMBean,"destroyJDBCSystemResource",
	   	                         new Object[]{mySystemResourceMBean},new String[]{ObjectName.class.getName()});
	     }
	     else
	    	 throw new WLSAutomationException("Datasource "+datasourcename+" does not exist  -  cannot delete !");
	   }
	   catch(Exception ex)
   	   {
		  throw new WLSAutomationException(ex);
	   }
    } 	
}
	
