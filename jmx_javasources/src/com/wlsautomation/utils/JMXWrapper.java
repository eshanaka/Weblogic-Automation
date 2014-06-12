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



public interface JMXWrapper
{

    /**
     *
     * @param name ObjectName
     * @param attributeName Attribute
     * @return Object
     * @throws Exception
     */
    public Object getAttribute(ObjectName name, String attributeName)   throws Exception;

    /**
     * Set an attribute on the MBean
     * @param name
     * @param attributeName
     * @param attributeValue
     * @throws Exception
     */
    public void setAttribute(ObjectName name, Attribute myAttribute)  throws Exception;
    
    /**
     *
     * @param name ObjectName
     * @param operationName String
     * @param params Object[]
     * @param signature String[]
     * @return Object
     * @throws Exception
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)  throws Exception;



    /**
     * Disconnect from Admin server
     * @param edit boolean
     * @throws Exception
     */
    public void disconnectFromAdminServer() throws Exception;


    /**
     * Get the main Values for the connected domain
     * @return Hashtable<String,String>
     * @throws Exception
     */
    public Hashtable<String,String> getMainServerDomainValues() throws Exception;


    /**
     * Get the list of managed Server MBEANS (here the ObjectNames) without the adminserver
     * @return Hashtable<String,String>
     * @throws Exception
     */
    public ArrayList<ObjectName> getManagedServerObjectNames() throws Exception;

    /**
     * Get the list of managed Server MBEANS (here the ObjectNames) without the adminserver
     * @return ArrayList<String>
     * @throws Exception
     */
    public ArrayList<String> getManagedServerNames() throws Exception;

    /**
     * Get the runtime state of one managed server.
     *
     * @param serverName String
     * @return String
     * @throws Exception
     */
    public String getServerState(String serverName)  throws Exception;

    public ObjectName getServerRuntime(String serverName)  throws Exception;


    /**
     * Returns connection
     *
     * @return MBeanServerConnection
     */
    public MBeanServerConnection getConnection();

    /**
     * Get the actual connector
     * @return JMXConnector
     */
    public JMXConnector getConnector();

    /**
     * Get the service object name
     * @return ObjectName
     */
    public ObjectName getService();

    /**
     * Returns the JMX-ObjectName for the domain root MBean
     * @return ObjectName
     */
    public ObjectName getDomainConfigRoot();

    public ObjectName getDomainRuntimeRoot();


}
