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
package com.wlsautomation.utils;

import java.util.*;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.Attribute;
import javax.management.remote.*;
import javax.naming.Context;




public class JMXWrapperRemote  implements JMXWrapper
{
    // active (if any) mbean server connection
    private MBeanServerConnection connection;

    // connector
    private JMXConnector connector;

    private ObjectName service;

    // is wrapper initialized ?
    private boolean initialized = false ;

    // Root MBean for configurations
    private ObjectName domainConfigRoot = null;

    // Root MBean for configurations
    private ObjectName domainRuntimeRoot = null;
    
    
    // protocol information: will be set during initialization
    private String protocol = null;
    private String hostname = null;
    private String portString = null;
    private String connectionuser = null;
    private String connectionpassword = null;


    private boolean isEdit = false;
    
    private boolean isDomainRuntime = false;
    
    /**
     * Init JMX Wrapper
     */
    public JMXWrapperRemote() {}


    /**
     *
     * @param name ObjectName
     * @param attributeName Attribute
     * @return Object
     * @throws Exception
     */
    public Object getAttribute(ObjectName name, String attributeName)   throws Exception
    {
        try {
            // todo: logging  - JMXWrapperRemote:getAttribute called for "+name+" : "+ attributeName +" !");

            // do INVOKE
            return getConnection().getAttribute(name,attributeName);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("PROBLEM with JMXWrapperRemote:getAttribute: " + ex.getMessage());
       }
    }

    /**
     * Set an attribute on the MBean
     * @param name
     * @param attributeName
     * @param attributeValue
     * @throws Exception
     */
    public void setAttribute(ObjectName name, Attribute myAttribute)  throws Exception
    {
        try {
            // todo: logging  - JMXWrapperRemote:setAttribute called for "+name+" : "+ attributeName +" !");

            // do INVOKE
            getConnection().setAttribute(name,myAttribute);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("PROBLEM with JMXWrapperRemote:getAttribute: " + ex.getMessage());
       }
    }


    /**
     *
     * @param name ObjectName
     * @param operationName String
     * @param params Object[]
     * @param signature String[]
     * @return Object
     * @throws Exception
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)  throws Exception
    {
        try {
            // todo: logging  - JMXWrapperRemote:invoke called for "+name+" : "+ operationName +" !");

            if (params==null)
                params = new Object[0];
            if (signature == null)
                signature = new String[0];

            // do INVOKE
            return getConnection().invoke(name, operationName, params, signature);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("PROBLEM with JMXWrapperRemote:invoke: " + ex.getMessage());
       }
    }





    /**
     * Init and open connection to an MBean server
     *
     * @param editmode boolean  Edit mode or not (this determines the MBEanserver instance !)
     * @param url String connection URL
     * @param username String User
     * @param password String Password
     * @param logErrors boolean
     * @throws Exception
     */
    private void initConnection(boolean editmode, 
								boolean domainRuntime,
                                String url,
                                String username,
                                String password) throws Exception
    {
        try {
            isEdit = editmode;
            isDomainRuntime = domainRuntime;
            connectionuser = username;
            connectionpassword = password;

            // get protocol from URL string
            protocol = url.substring(0,url.indexOf("://"));

            // get hostname from URL string
            hostname = url.substring(url.indexOf("//")+2, url.indexOf(":",protocol.length()+3));

            // get port from URL string
            portString = url.substring(url.indexOf(":",protocol.length()+3)+1,url.length());

            Integer portInteger = Integer.valueOf(portString);
            int port = portInteger.intValue();
            String jndiroot = "/jndi/";
			
            String mserver = null;
			
			if(isEdit) 
			    mserver = "weblogic.management.mbeanservers.edit";
			else
				if (isDomainRuntime)
					 mserver = "weblogic.management.mbeanservers.domainruntime";
				else	 
					 mserver = "weblogic.management.mbeanservers.runtime";
			
            JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname,
               port, jndiroot + mserver);

            Hashtable<String, String> h = new Hashtable<String, String>();
            h.put(Context.SECURITY_PRINCIPAL, connectionuser);
            h.put(Context.SECURITY_CREDENTIALS, connectionpassword);
            h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,"weblogic.management.remote");
            connector = JMXConnectorFactory.connect(serviceURL, h);
            connection = connector.getMBeanServerConnection();

            // success
            initialized = true;
        }
        catch (Exception ex)
        {
            initialized = false;
            throw new Exception("PROBLEM with JMXWrapper:initConnection: " + ex.getMessage());
        }
    }




    /**
     * Is wrapper initialized
     *
     * @return boolean
     */
    public boolean isInitialized()
    {
      return initialized;
    }

    /**
     * Start an edit session.
     * @return ObjectName
     * @throws Exception
     */
    private ObjectName startEditSession() throws Exception
    {
       try {
    	   // do not set domain runtime
    	   domainRuntimeRoot = null;
    	   
           // Get the object name for ConfigurationManagerMBean.
           ObjectName cfgMgr = (ObjectName) getAttribute(service,"ConfigurationManager");
           // The startEdit operation returns a handle to DomainMBean, which is the root of the edit hierarchy.
           ObjectName domainConfigRoot = (ObjectName)invoke(cfgMgr,
                   "startEdit", new Object[] {new Integer(60000),
                   new Integer(120000)}, new String[] {"java.lang.Integer",
                   "java.lang.Integer"});
           if (domainConfigRoot == null)
           {
               // Couldn't get the lock
               throw new Exception("Somebody else is editing already");
           }
           return domainConfigRoot;
       }
       catch (Exception ex) {
           throw new Exception("PROBLEM with JMXWrapper:startEditSession: " + ex.getMessage());
       }
    }

    /**
     * Start a NON-edit session
     * @return ObjectName
     * @throws Exception
     */
    private ObjectName startNONEditSession() throws Exception
    {
        try {
            ObjectName domainMBean =(ObjectName) getAttribute(service,"DomainConfiguration");
            
            if (isDomainRuntime)
            	domainRuntimeRoot = (ObjectName) getAttribute(service,"DomainRuntime");
            
            return domainMBean;
        }
        catch (Exception ex) {
            throw new Exception("PROBLEM with JMXWrapper:startNONEditSession: " + ex.getMessage());
        }
    }


    /**
     * Conect to an admin server
     *
     * @param edit boolean
     * @param username String
     * @param password String
     * @param url String
     * @param logConnectionProblems boolean
     * @throws Exception
     */
    public void connectToAdminServer(boolean edit,
									 boolean domainRuntime,
                                     String username,
                                     String password,
                                     String url
                                    ) throws Exception
    {
       try {

           initConnection(edit, domainRuntime, url, username,password);

			if(edit) 
			    service = new ObjectName("com.bea:Name=EditService,Type=weblogic.management.mbeanservers.edit.EditServiceMBean");
			else
				if (domainRuntime)
					 service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
				else	 
					 service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");

           if (edit)
               domainConfigRoot = startEditSession();
           else
               domainConfigRoot = startNONEditSession();
       }
       catch (Exception ex)
       {
           throw new Exception("PROBLEM with JMXWrapper:connectToAdminServer: " + ex.getMessage());
       }
    }
    


    /**
     * Disconnect from Admin server
     * @param edit boolean
     * @throws Exception
     */
    public void disconnectFromAdminServer() throws Exception
    {
        try {
            // Close the connection with the MBean server.
            domainConfigRoot = null;
            try {
               connector.close();
            }
            catch (Exception ex) {
               // ignore
            }

            connector = null;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapperRemote:disconnectFromAdminServer: " + ex.getMessage());
        }
    }

    
    /**
     * The activate operation returns an ActivationTaskMBean.
     * You can use the ActivationTaskMBean to track the progress
     * of activating changes in the domain
     * @return ObjectName
     * @throws Exception
     */
    private ObjectName activate() throws Exception
    {
        try {
            ObjectName cfgMgr = (ObjectName) getAttribute(service,"ConfigurationManager");
            invoke(cfgMgr, "save", null, null);
            ObjectName result = (ObjectName) invoke(cfgMgr,"activate",new Object[] {new Long(120000)}, new String[] {"java.lang.Long"});
            return result;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapper:activate: " + ex.getMessage());
        }
    }

    private ObjectName cancelChanges() throws Exception
    {
        try {

            ObjectName cfgMgr = (ObjectName) getAttribute(service,"ConfigurationManager");
            invoke(cfgMgr, "save", null, null);
            ObjectName result = (ObjectName) invoke(cfgMgr,"cancelEdit",new Object[] {}, new String[] {});
            return result;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapper:cancelChanges: " + ex.getMessage());
        }
    }

    /**
     * Disconnect from Admin server
     * @param edit boolean
     * @throws Exception
     */
    public void disconnectFromAdminServer(boolean activate) throws Exception
    {
        try {
			if (isEdit)
				if (activate)
					activate();
				else
					cancelChanges();			

            // Close the connection with the MBean server.
            domainConfigRoot = null;
            try {
               connector.close();
            }
            catch (Exception ex) {
               // ignore
            }

            connector = null;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapper:disconnectFromAdminServer: " + ex.getMessage());
        }
    }    
    
    
    

    /**
     * Get the main Values for the connected domain
     * @return Hashtable<String,String>
     * @throws Exception
     */
    public Hashtable<String,String> getMainServerDomainValues() throws Exception
    {
        try {
            Hashtable<String,String> result = new Hashtable<String,String>();
            ObjectName domainMBean =(ObjectName) getAttribute(service,"DomainConfiguration");

            String serverName = (String) getAttribute(service,"ServerName");

            String adminServerName = (String) getAttribute(domainMBean,"AdminServerName");
            String domainName = domainMBean.getKeyProperty("Name");
            String domainRoot = (String) getAttribute(domainMBean,"RootDirectory");

            result.put("serverName",serverName);
            result.put("adminServerName",adminServerName);
            result.put("domainName",domainName);
            result.put("domainRoot",domainRoot);
            result.put("domainBase",domainRoot.substring(0,domainRoot.length()-(domainName.length()+1)));
            
            // check if server is adminserver
            try {
               ObjectName serverRuntime =(ObjectName) getAttribute(service,"ServerRuntime");
               result.put("connectedToAdminServer",getAttribute(serverRuntime,"AdminServer").toString());
            }
            catch(Exception ex)
            {
        	System.out.println("Uppps: Attribute AdminServer not found on ("+service.toString()+")!\n");
            	result.put("connectedToAdminServer","false");
            }

            return result;
        }
        catch (Exception ex)
        {
            //LogUtils.getLogger(LogUtils.JMX_LAYER).error("PROBLEM with JMXWrapperRemote:getMainServerDomainValues: " + ex.getMessage(), ex);
            throw new Exception("PROBLEM with JMXWrapperRemote:getMainServerDomainValues: " + ex.getMessage());
        }
    }


    /**
     * Get the list of managed Server MBEANS (here the ObjectNames) without the adminserver
     * @return Hashtable<String,String>
     * @throws Exception
     */
    public ArrayList<ObjectName> getManagedServerObjectNames() throws Exception
    {
        try {
            ArrayList<ObjectName> result = new ArrayList<ObjectName>();


            ObjectName domainMBean =(ObjectName) getAttribute(service,"DomainConfiguration");
            String adminServerName = (String) getAttribute(domainMBean,"AdminServerName");

            ObjectName[] serverRuntimes = (ObjectName[])getAttribute(domainMBean,"Servers");
            for (int i = 0; i < serverRuntimes.length; i++)
            {
                 String tmp = serverRuntimes[i].getKeyProperty("Name");
                 if (!tmp.equals(adminServerName))
                     result.add(serverRuntimes[i]);
            }

            return result;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapperRemote:getManagedServerObjectNames: " + ex.getMessage());
        }
    }

    /**
     * Get the list of managed Server MBEANS (here the ObjectNames) without the adminserver
     * @return ArrayList<String>
     * @throws Exception
     */
    public ArrayList<String> getManagedServerNames() throws Exception
    {
        try {
            ArrayList<String> result = new ArrayList<String>();


            ObjectName domainMBean =(ObjectName) getAttribute(service,"DomainConfiguration");
            String adminServerName = (String) getAttribute(domainMBean,"AdminServerName");

            ObjectName[] serverRuntimes = (ObjectName[])getAttribute(domainMBean,"Servers");
            for (int i = 0; i < serverRuntimes.length; i++)
            {
                 String tmp = serverRuntimes[i].getKeyProperty("Name");
                 if (!tmp.equals(adminServerName))
                     result.add(tmp);
            }

            return result;
        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapperRemote:getManagedServerNames: " + ex.getMessage());
        }
    }
    
    /**
     * Get the runtime state of one managed server.
     *
     * @param serverName String
     * @return String
     * @throws Exception
     */
    public String getServerState(String serverName)  throws Exception
    {
        try {

             //ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");
             ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Name=" + serverName + ",Type=ServerLifeCycleRuntime");

             return (String)getAttribute(serverRuntimeObjectName,"State");
         }
         catch (Exception ex)
         {
             return "_UNKNOWN_ERROR_";
         }
    }


    public ObjectName getServerRuntime(String serverName)  throws Exception
    {
        try {
        	if (isDomainRuntime)
                return new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");
        	else
                return new ObjectName("com.bea:Name=" + serverName + ",Type=ServerRuntime");
         }
         catch (Exception ex)
         {
             return null;
         }
    }


    /**
     * Returns connection
     *
     * @return MBeanServerConnection
     */
    public MBeanServerConnection getConnection()
    {
        return connection;
    }

    /**
     * Get the actual connector
     * @return JMXConnector
     */
    public JMXConnector getConnector() {
        return connector;
    }

    /**
     * Get the service object name
     * @return ObjectName
     */
    public ObjectName getService() {
        return service;
    }

    /**
     * Returns the JMX-ObjectName for the domain root MBean
     * @return ObjectName
     */
    public ObjectName getDomainConfigRoot() {
        return domainConfigRoot;
    }

    public ObjectName getDomainRuntimeRoot() {
        return domainRuntimeRoot;
    }


    public String getRemoteProtocol()
    {
    	return protocol;
    }
    
    public String getRemoteHostName()
    {
    	return hostname;
    }
    
    public String getRemotePort()
    {
    	return portString;
    }
    
    public String getRemoteUser()
    {
    	return connectionuser;
    }
    
    public String getRemotePassword()
    {
    	return connectionpassword;
    }
    
}
