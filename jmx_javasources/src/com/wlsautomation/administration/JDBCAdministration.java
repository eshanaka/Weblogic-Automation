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
package com.wlsautomation.administration;

import com.wlsautomation.utils.*;

import javax.management.Attribute;
import javax.management.ObjectName;



public class JDBCAdministration {

    private JMXWrapper myJMXWrapper = null;

    public JDBCAdministration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }


    public void startDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "start");
    }
    
    public void shutdownDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "shutdown");
    }
    
    public void forceShutdownDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "forceShutdown");
    }
    
    public void shrinkDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "shrink");
    }
    
    public void suspendDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "suspend");
    }
    
    public void forceSuspendDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "forceSuspend");
    }
    
    public void dumpPoolDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "dumpPool");
    }
    
    public void resetDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "reset");
    }
    
    public void resumeDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "resume");
    }
    
    public void clearStatementCacheDataSource(String datasourcename)  throws WLSAutomationException
    {
    	doDataSourceOperation(datasourcename, "clearStatementCache");
    }
    
    
    
    
    private void doDataSourceOperation(String datasourcename, String operationName)  throws WLSAutomationException
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
    	    	 // get datasource
    	    	 ObjectName myDataResourceMBean = (ObjectName)myJMXWrapper.getAttribute(mySystemResourceMBean, "JDBCResource");
    	    	 myJMXWrapper.invoke(myDataResourceMBean,operationName,new Object[]{},new String[]{});
    	     }
    	     else
    	    	 throw new WLSAutomationException("Datasource "+datasourcename+" does not exist  -  cannot "+operationName+" !");
    	     
    	}
    	catch(Exception ex)
    	{
    		throw new WLSAutomationException(ex);
    	}
    }


    public String testDataSource(String datasourcename)  throws WLSAutomationException
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
    	    	 // get datasource
    	    	 ObjectName myDataResourceMBean = (ObjectName)myJMXWrapper.getAttribute(mySystemResourceMBean, "JDBCResource");
    	    	 return (String)myJMXWrapper.invoke(myDataResourceMBean,"testPool",new Object[]{},new String[]{});
    	     }
    	     else
    	    	 throw new WLSAutomationException("Datasource "+datasourcename+" does not exist  -  cannot test !");
    	     
    	}
    	catch(Exception ex)
    	{
    		throw new WLSAutomationException(ex);
    	}
    }

    
    public void changeJDBCUrl(String datasourcename, String newURL)  throws WLSAutomationException
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
    	    	 // get datasource
    	    	 ObjectName myJDBCResourceMBean = (ObjectName)myJMXWrapper.getAttribute(mySystemResourceMBean,"JDBCResource");
        	     ObjectName myJDBCDriverParamsMBean = (ObjectName)myJMXWrapper.getAttribute(myJDBCResourceMBean,"JDBCDriverParams");

        	     // set URL
        	     myJMXWrapper.setAttribute(myJDBCDriverParamsMBean,new Attribute("Url",new String(newURL)));

    	     }
    	     else
    	    	 throw new WLSAutomationException("Datasource "+datasourcename+" does not exist  -  cannot change URL !");
    	     
    	}
    	catch(Exception ex)
    	{
    		throw new WLSAutomationException(ex);
    	}
    }
    
    
    public void changeDatasourceUserAndPasswordJ(String datasourcename, String newUser, String newPassword)  throws WLSAutomationException
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
    	    	 // get datasource
    	    	 ObjectName myJDBCResourceMBean = (ObjectName)myJMXWrapper.getAttribute(mySystemResourceMBean,"JDBCResource");
        	     ObjectName myJDBCDriverParamsMBean = (ObjectName)myJMXWrapper.getAttribute(myJDBCResourceMBean,"JDBCDriverParams");

    	         // set Password
    	         myJMXWrapper.setAttribute(myJDBCDriverParamsMBean, new Attribute("Password", new String(newPassword)));

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
    	         myJMXWrapper.setAttribute(myUserPropertyMBean, new Attribute("Value", new String(newUser)));
    	     }
    	     else
    	    	 throw new WLSAutomationException("Datasource "+datasourcename+" does not exist  -  cannot change user and password !");
    	     
    	}
    	catch(Exception ex)
    	{
    		throw new WLSAutomationException(ex);
    	}
    }
    
    
}
