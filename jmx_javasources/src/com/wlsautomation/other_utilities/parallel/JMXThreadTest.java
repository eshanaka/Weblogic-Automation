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
package com.wlsautomation.other_utilities.parallel;

import java.util.*;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.*;
import javax.naming.Context;
import java.io.FileOutputStream;

public class JMXThreadTest extends Thread
{
    // active (if any) mbean server connection
    private MBeanServerConnection connection;

    // connector
    private JMXConnector connector;

    private ObjectName domainRuntimeService = null;

    private ObjectName domainMBean = null;

    private int internalReloadInterval = 0;

    private int id;

    private boolean endOfLoop = false;

    public JMXThreadTest(int _id, String url, String username, String password, int reloadInterval)
    {
        try {
            id = _id;
            internalReloadInterval = reloadInterval;

            // get protocol from URL string
            String protocol = url.substring(0,url.indexOf("://"));

            // get hostname from URL string
            String hostname = url.substring(url.indexOf("//")+2, url.indexOf(":",protocol.length()+3));

            // get port from URL string
            String portString = url.substring(url.indexOf(":",protocol.length()+3)+1,url.length());

            Integer portInteger = Integer.valueOf(portString);
            int port = portInteger.intValue();
            JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,"/jndi/weblogic.management.mbeanservers.domainruntime");

            Hashtable<String, String> h = new Hashtable<String, String>();
            h.put(Context.SECURITY_PRINCIPAL, username);
            h.put(Context.SECURITY_CREDENTIALS, password);
            h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,"weblogic.management.remote");
            connector = JMXConnectorFactory.connect(serviceURL, h);
            connection = connector.getMBeanServerConnection();

            domainRuntimeService = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
            domainMBean =(ObjectName) connection.getAttribute(domainRuntimeService,"DomainConfiguration");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


   public void run()
   {
      while(!endOfLoop)
      {
         getManagedServerObjectNames();

         try {
             sleep(internalReloadInterval*1000);
         } catch (Exception ex) {

         }
      }

   }




    public void disconnectFromAdminServer()
    {
        try {
            endOfLoop = true;
            connector.close();
            connector = null;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void getManagedServerObjectNames()
    {
      try
      {
        FileOutputStream os = new FileOutputStream(domainMBean.getKeyProperty("Name")+".log",true);
        try {

            String domainName = (String) connection.getAttribute(domainMBean,"Name");
            System.out.println("Aufruf Admin-Server Nr.: "+id+" : "+domainName);

            os.write(("\n"+"Domain-Name : " + domainName).getBytes() );
            os.write(("\n"+"  AdminServerName : "+(String) connection.getAttribute(domainMBean,"AdminServerName")).getBytes());
            os.write(("\n"+"  RootDirectory : "+(String) connection.getAttribute(domainMBean,"RootDirectory")).getBytes() );
            os.write(("\n"+"  ProductionModeEnabled : "+((Boolean) connection.getAttribute(domainMBean,"ProductionModeEnabled")).booleanValue() ).getBytes());

            // Managed Server
            os.write(("\n"+"  Managed-Server:").getBytes());
            String adminServerName = (String) connection.getAttribute(domainMBean,"AdminServerName");
            ObjectName[] serverRuntimes = (ObjectName[])connection.getAttribute(domainMBean,"Servers");
            for (int i = 0; i < serverRuntimes.length; i++) {
                 String ms_name = (String) connection.getAttribute(serverRuntimes[i],"Name");
                 if (!ms_name.equals(adminServerName)) {
                     os.write(("\n"+"     -> "+ms_name + " : StartupMode = "+(String) connection.getAttribute(serverRuntimes[i],"StartupMode")).getBytes());
                     os.write(("\n"+"        "+ (String)connection.getAttribute(serverRuntimes[i],"ListenAddress")+":"+((Integer)connection.getAttribute(serverRuntimes[i],"ListenPort")).intValue() ).getBytes());
                 }
            }

            // Cluster
            os.write(("\n"+"  Cluster:").getBytes());
            ObjectName[] cluster = (ObjectName[])connection.getAttribute(domainMBean,"Clusters");
            for (int i = 0; i < cluster.length; i++)
                os.write(("\n"+"     -> "+(String) connection.getAttribute(cluster[i],"Name")).getBytes());


            // Machines
            os.write(("\n"+"  Machines:").getBytes());
            ObjectName[] machines = (ObjectName[])connection.getAttribute(domainMBean,"Machines");
            for (int i = 0; i < machines.length; i++)
                os.write(("\n"+"     -> "+(String) connection.getAttribute(machines[i],"Name")).getBytes());

            // AppDeployments
            os.write(("\n"+"  AppDeployments:").getBytes());
            ObjectName[] appDeployments = (ObjectName[])connection.getAttribute(domainMBean,"AppDeployments");
            for (int i = 0; i < appDeployments.length; i++)
                os.write(("\n"+"     -> "+(String) connection.getAttribute(appDeployments[i],"Name")).getBytes());


        }
        catch (Exception ex)
        {

            os.write(("Probleme mit Admin-Server Nr.: "+id).getBytes());
            os.write(ex.getMessage().getBytes());
            disconnectFromAdminServer();
        }
        os.close();
        }
        catch (Exception ex)
        {
            System.out.println("Probleme mit Admin-Server Nr.: "+id);
            ex.printStackTrace();
            disconnectFromAdminServer();
        }

    }


}

