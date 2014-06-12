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



import java.util.ArrayList;

import com.wlsautomation.utils.*;
import javax.management.ObjectName;



public class ServerAdministration {

    private JMXWrapper myJMXWrapper = null;

    public ServerAdministration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

	
	   /**
     * Shutdown a managed server or the admin server
     * @param serverName String
     * @throws Exception
     */
    public void shutdownServer(String serverName) throws WLSAutomationException
    {
        try {
            System.out.println("shutdownServer called !");
            String state = myJMXWrapper.getServerState(serverName);

            if (!state.equalsIgnoreCase("RUNNING") &&
                !state.equalsIgnoreCase("ADMIN") &&
                !state.equalsIgnoreCase("_UNKNOWN_ERROR_"))
                return ;  // Nothing to do or ERROR


            ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");

            myJMXWrapper.invoke(serverRuntimeObjectName, "forceShutdown", null, null);
        }
        catch (Exception ex)
        {
            System.out.println("PROBLEM with shutdownServer: " + ex.getMessage());
            throw new WLSAutomationException("PROBLEM with shutdownServer: " + ex.getMessage());
        }
    }

    
    public void startAllManagedServer() throws WLSAutomationException
    {
        try {
            System.out.println("startAllManagedServer called !");
            
            ArrayList<String> serverNames = myJMXWrapper.getManagedServerNames();
            
            for (int i=0;i<serverNames.size();i++)
            	startManagedServer(serverNames.get(i));
        }
        catch (Exception ex)
        {
            System.out.println("PROBLEM with startAllManagedServer: " + ex.getMessage());
            throw new WLSAutomationException("PROBLEM with startAllManagedServer: " + ex.getMessage());
        }
    }
    

    /**
     * Start a managed server ( NOT the admin server)
     * @param serverName String
     * @throws Exception
     */
    public void startManagedServer(String serverName) throws WLSAutomationException
    {
        try {
            System.out.println("startManagedServer called !");

            try {
                String state = myJMXWrapper.getServerState(serverName);
                if (state.equalsIgnoreCase("RUNNING"))
                {
                    System.out.println("   Server "+serverName+" is already running !");
                    return; // Nothing to do or ERROR
                }
            }
            catch (Exception ex) {
               System.out.println("PROBLEM with startManagedServer - asume managed server is down !");
            }

            System.out.println("   Try to start server "+serverName+" !");
            ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Name=" + serverName + ",Type=ServerLifeCycleRuntime");

            myJMXWrapper.invoke(serverRuntimeObjectName, "start", null, null);

            System.out.println("   Server "+serverName+" started  !");
        }
        catch (Exception ex)
        {
            System.out.println("PROBLEM with startManagedServer: " + ex.getMessage());
            throw new WLSAutomationException("PROBLEM with startManagedServer: " + ex.getMessage());
        }
    }

 /**
  * suspend a managed server
  * @param serverName String
  * @throws Exception
  */
 public void suspendServer(String serverName) throws WLSAutomationException
 {
     try {
         System.out.println("suspendServer called !");
         String state = myJMXWrapper.getServerState(serverName);

         if (!state.equalsIgnoreCase("SHUTDOWN"))
         {
             ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");

             myJMXWrapper.invoke(serverRuntimeObjectName, "suspend", null, null);
         }
     }
     catch (Exception ex)
     {
         System.out.println("PROBLEM with suspendServer: " + ex.getMessage());
         throw new WLSAutomationException("PROBLEM with suspendServer: " + ex.getMessage());
     }
 }


 /**
  * suspend a managed server
  * @param serverName String
  * @throws Exception
  */
 public void resumeServer(String serverName) throws WLSAutomationException
 {
     try {
         System.out.println("resumeServer called !");
         String state = myJMXWrapper.getServerState(serverName);

         if (!state.equalsIgnoreCase("SHUTDOWN") && !state.equalsIgnoreCase("RUNNING"))
         {
             ObjectName serverRuntimeObjectName = new ObjectName("com.bea:Location=" +serverName + ",Name=" + serverName + ",Type=ServerRuntime");

             myJMXWrapper.invoke(serverRuntimeObjectName, "resume", null, null);
         }
     }
     catch (Exception ex)
     {
         System.out.println("PROBLEM with resumeServer: " + ex.getMessage());
         throw new WLSAutomationException("PROBLEM with resumeServer: " + ex.getMessage());
     }
 }


 
// #########################  CLUSTER ###########################################################
 
 public void startCluster(String clustername)  throws WLSAutomationException
 {
 	try
 	{
 	     // e.g.: com.bea:Name=TestDomain,Type=Domain
 	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

 	     // Operation: javax.management.ObjectName  lookupJDBCSystemResource(name:java.lang.String  )	
 	     ObjectName myClusterMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
 	                                                               "lookupCluster",
 	                                                               new Object[]{new String(clustername)},
 	                                                               new String[]{String.class.getName()});
 	     if (myClusterMBean!=null)
 	     {
 	    	 // start
 	    	 myJMXWrapper.invoke(myClusterMBean,"start",new Object[]{},new String[]{});
 	     }
 	     else
 	    	 throw new WLSAutomationException("Cluster "+clustername+" does not exist  -  cannot start !");
 	     
 	}
 	catch(Exception ex)
 	{
 		throw new WLSAutomationException(ex);
 	}
 }

 
 public void stopCluster(String clustername)  throws WLSAutomationException
 {
 	try
 	{
 	     // e.g.: com.bea:Name=TestDomain,Type=Domain
 	     ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();

 	     // Operation: javax.management.ObjectName  lookupJDBCSystemResource(name:java.lang.String  )	
 	     ObjectName myClusterMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
 	                                                               "lookupCluster",
 	                                                               new Object[]{new String(clustername)},
 	                                                               new String[]{String.class.getName()});
 	     if (myClusterMBean!=null)
 	     {
 	    	 // start
 	    	 myJMXWrapper.invoke(myClusterMBean,"kill",new Object[]{},new String[]{});
 	     }
 	     else
 	    	 throw new WLSAutomationException("Cluster "+clustername+" does not exist  -  cannot stop !");
 	     
 	}
 	catch(Exception ex)
 	{
 		throw new WLSAutomationException(ex);
 	}
 }
 

    
}
