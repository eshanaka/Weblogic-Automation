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

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.*;
import javax.management.MBeanServer;
import javax.naming.InitialContext;



public class JMXWrapperLocal implements JMXWrapper
{
    // active (if any) mbean server connection
    private MBeanServerConnection connection;

    private ObjectName service;

    // Root MBean for configurations
    private ObjectName domainConfigRoot = null;
    
    // Root MBean for configurations
    private ObjectName domainRuntimeRoot = null;

    /**
     * Init JMX Wrapper
     */
    public JMXWrapperLocal() {}


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
            // todo: logging  - JMXWrapperLocal:getAttribute called for "+name+" : "+ attributeName +" !");

            // do INVOKE
            return getConnection().getAttribute(name,attributeName);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("PROBLEM with JMXWrapperLocal:getAttribute: " + ex.getMessage());
       }
    }


    /**
     * Set an attribute on the MBean
     * @param name
     * @param attributeName
     * @param attributeValue
     * @throws Exception
     */
    public void setAttribute(ObjectName name, String attributeName, Object attributeValue)  throws Exception
    {
        try {
            // todo: logging  - JMXWrapperRemote:setAttribute called for "+name+" : "+ attributeName +" !");

            // do INVOKE
        	Attribute myAttribute = new Attribute(attributeName, attributeValue);
            getConnection().setAttribute(name,myAttribute);
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
            // todo: logging  - JMXWrapperLocal:invoke called for "+name+" : "+ operationName +" !");

            if (params==null)
                params = new Object[0];
            if (signature == null)
                signature = new String[0];

            // do INVOKE
            return getConnection().invoke(name, operationName, params, signature);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("PROBLEM with JMXWrapperLocal:invoke: " + ex.getMessage());
       }
    }



    /**
     * Conect to an admin server
     *
     * @param username String
     * @param password String
     * @param url String
     * @throws Exception
     */
    public void connectToAdminServer() throws Exception
    {
        try {
            InitialContext ctx = new InitialContext();
            connection = (MBeanServer) ctx.lookup("java:comp/jmx/runtime");


            // service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
            service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");

            domainConfigRoot = (ObjectName) getAttribute(service,"DomainConfiguration");
     	    domainRuntimeRoot = (ObjectName) getAttribute(service,"DomainRuntime");

        }
        catch (Exception ex)
        {
            throw new Exception("PROBLEM with JMXWrapperLocal:initConnection: " + ex.getMessage());
        }

    }



    /**
     * Disconnect from Admin server
     * @param edit boolean
     * @throws Exception
     */
    public void disconnectFromAdminServer() throws Exception
    {
        // nothing to do in local wrapper !!
        return;
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
            result.put("connectedToAdminServer",getAttribute(service,"AdminServer").toString());

            return result;
        }
        catch (Exception ex)
        {
            //LogUtils.getLogger(LogUtils.JMX_LAYER).error("PROBLEM with JMXWrapperLocal:getMainServerDomainValues: " + ex.getMessage(), ex);
            throw new Exception("PROBLEM with JMXWrapperLocal:getMainServerDomainValues: " + ex.getMessage());
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
            throw new Exception("PROBLEM with JMXWrapperLocal:getManagedServerObjectNames: " + ex.getMessage());
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
            throw new Exception("PROBLEM with JMXWrapperLocal:getManagedServerNames: " + ex.getMessage());
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
            // return new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");
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
        throw new RuntimeException("Not supported in local wrapper");
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


}
