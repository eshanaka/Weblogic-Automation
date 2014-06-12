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
package com.wlsautomation.monitoring.pushmonitoring;

import java.util.*;
import javax.management.*;



public class PushExampleMBeanImpl extends StandardMBean implements PushExampleMBean
{

	private boolean pluginEnabled           = true;    // default: true
	
	private int updateIntervalInSeconds     = 60;        // default every minute

	private String connectionURL            = "localhost:12345";   // t.d.b
	
	private boolean connectedToAdmin         = false;
	

    // JMXWrapper
    private JMXWrapperLocal myJMXWrapper = null;	

    // MonitoringExecutionThread
    private PushExampleThread myPushExampleThread = null;

	
    
    // basic domain values queried from mbean tree and cached for optimizations
    private Hashtable<String,String> mainDomainValues = null;

    
    
    
    
    public PushExampleMBeanImpl(JMXWrapperLocal _JMXWrapper) throws Exception
    { 
    	 super(PushExampleMBean.class, false);
    	 
    	 try
    	 {
        	 myJMXWrapper = _JMXWrapper;
             myJMXWrapper.connectToAdminServer(false,false);
             mainDomainValues = myJMXWrapper.getMainServerDomainValues(); 

             ObjectName mymbean = new ObjectName("pushMonitoring:Name=ExamplePushInformation,Type=ExamplePushPlugin");
             ((MBeanServer)myJMXWrapper.getConnection()).registerMBean(this, mymbean);
        	 System.out.println("Push-Monitoring:  PushExampleMBean - MBean registered");
        	 
        	 // if running on adminserver -> change to domainruntime
        	 String isCurrentServerAnAdminServer = mainDomainValues.get("connectedToAdminServer");
         	
         	 if ("true".equalsIgnoreCase(isCurrentServerAnAdminServer))  // is Admin
         	 {
         		 myJMXWrapper.disconnectFromAdminServer();
         		 myJMXWrapper.connectToAdminServer(false,true);
         		 connectedToAdmin = true;
         		 System.out.println("Monitoring:  Connection changed to DomainRuntime as THIS is the admin server !");  
         	 }

         	 // example implementation in order to start data thread
         	 setEnabled(getEnabled());
         	 
        	 System.out.println("PushExample-Monitoring:  init completed");
    	 }
    	 catch(Exception ex)
    	 {
    		 ex.printStackTrace();
    		 throw ex;
    	 }
    }
    

    public boolean isConnectedToAdmin()
    {
    	return connectedToAdmin;
    }

	
// ***************************************  GENERAL attributes *********************************************************    
	
    /**
     * Interval in seconds how often the information are collected and reported
     * @return int
     */
    public int getUpdateIntervalInSeconds()
    {
    	return updateIntervalInSeconds;
    }


    /**
     * Set the  interval how often the runtime information should be gathered and reported
     * @param intervalInSeconds int
     */
    public void setUpdateIntervalInSeconds(int intervalInSeconds)
    {
    	updateIntervalInSeconds = intervalInSeconds;
    }
    
    
 
    
    /**
     * Get the connection URL 
     * @return String
     */
    public String getDestinationURL()
    {
    	return connectionURL;
    }

    /**
     * Set connection URL
     * @param connectionURL String
     */
    public void setDestinationURL(String newConnectionURL)
    {
    	connectionURL = newConnectionURL;
    }

  
  
    
 
	/**
     * Is plugin enabled - means does plugin record periodically the runtime values and report them
     * @return boolean
     */
    public String getEnabled()
    {
    	return pluginEnabled ? "true" : "false" ;
    }

    
    public void setEnabled(String newEnabledValue)
    {
        // set value
    	pluginEnabled = Boolean.parseBoolean(newEnabledValue);
    	
    	//myReloadSamplerParametersThread

        if (pluginEnabled==true)   // ENABLE
        {
            if (myPushExampleThread != null)
            {
                System.out.println("Monitoring is already active !!");
            }
            else
            {
                System.out.println("Monitoring will be started !!");
                myPushExampleThread = new PushExampleThread(this);
                myPushExampleThread.setDaemon(true);
                myPushExampleThread.start();
            }
        }
        else  // DISABLE
        {
            if (myPushExampleThread == null)
            {
                System.out.println("Monitoring is already disabled !!");
            }
            else
            {
                // setting enabled to false is enough - will be picked up by thread
                System.out.println("Monitoring will be disabled !!");
                myPushExampleThread.interrupt();
                myPushExampleThread = null;
            }

        }
    }    
    
    
    
    // monitoring methods   (examples)
    
    private String getHealthStateInformation(weblogic.health.HealthState myState)
    {
        if(myState.getState()==weblogic.health.HealthState.HEALTH_OK)
            return "HEALTH_OK";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_WARN)
            return "HEALTH_WARN";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_CRITICAL)
            return "HEALTH_CRITICAL";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_FAILED)
            return "HEALTH_FAILED";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_OVERLOADED)
            return "HEALTH_OVERLOADED";
        else
            return "UNKNOWN STATE";
    }

    
    

    public HashMap<String, String> getDomainData() throws Exception
    {
    	HashMap<String, String> myResultList = new HashMap<String, String>();
    	
        // add domain ID and name
    	myResultList.put("DomainName", (String)mainDomainValues.get("domainName"));

        ArrayList<String> msServerNames = myJMXWrapper.getManagedServerNames();
        
        int amountOfMSServer = msServerNames.size();
        int amountRunning = 0;
        int amountShutdown = 0;
        int amountFailedAdmin = 0;
        
        // iterator over server list and get server info
        for (int i=0; i<amountOfMSServer;i++)
        {
        	String nextState =  myJMXWrapper.getServerState(msServerNames.get(i));

        	if ("RUNNING".equalsIgnoreCase(nextState))
        		amountRunning++;
        	else if ("SHUTDOWN".equalsIgnoreCase(nextState) || "SHUTTING_DOWN".equalsIgnoreCase(nextState))
        		amountShutdown++;
        	if ("UNKNOWN".equalsIgnoreCase(nextState) || "FAILED_NOT_RESTARTABLE".equalsIgnoreCase(nextState) ||
        		"FAILED".equalsIgnoreCase(nextState) || "FAILED_RESTARTING".equalsIgnoreCase(nextState) ||	
        		"FORCE_SHUTTING_DOWN".equalsIgnoreCase(nextState)
        	   )
        		amountFailedAdmin++;
        }
        
        /*
        static String UNKNOWN 
        static String FAILED 
        static String FAILED_NOT_RESTARTABLE 
        static String FAILED_RESTARTING
        static String FORCE_SHUTTING_DOWN 
        
        static String SHUTDOWN 
        static String SHUTTING_DOWN 
        
        
        static String FORCE_SUSPENDING 
        static String RESUMING 
        static String ADMIN 
        static String STANDBY 
        
        static String RUNNING 
        static String STARTING 
*/
        
        
        
        // only one cluster allowed , therefore use first cluster ONLY !!
        // e.g.: com.bea:Name=TestDomain,Type=Domain
        ObjectName myDomainMBean = new ObjectName("com.bea:Name=" + (String)mainDomainValues.get("domainName") +",Type=Domain");
        
        ObjectName[] allClusters = (ObjectName[])myJMXWrapper.getAttribute(myDomainMBean,"Clusters");
        
        if (allClusters.length > 0)
        {
        	// use 1st cluster !!!
        	// get cluster member
        	ObjectName[] clusterServer = (ObjectName[])myJMXWrapper.getAttribute(allClusters[0],"Servers");
            int amountClusterServerRunning = 0;
            int amountClusterServerShutdown = 0;
            int amountClusterServerNotWorkingOrFailed = 0;
        	
        	// iterate over cluster member
            for (int i=0; i<clusterServer.length;i++)
            {
            	String nextState =  myJMXWrapper.getServerState((String)myJMXWrapper.getAttribute(clusterServer[i],"Name"));

            	if ("RUNNING".equalsIgnoreCase(nextState))
            		amountClusterServerRunning++;
            	else if ("SHUTDOWN".equalsIgnoreCase(nextState))
            		amountClusterServerShutdown++;
            	else // all other states
            		amountClusterServerNotWorkingOrFailed++;
            }
            
            // NOTE: Only looking at 1st cluster as DAP only supports one cluster
        	if (clusterServer.length == amountClusterServerRunning)  // all running
        		myResultList.put("ClusterState", "RUNNING");
        	else if (clusterServer.length == amountClusterServerShutdown)  // all shutdown or failed :-((
        		myResultList.put("ClusterState", "SHUTDOW");
        	else if (clusterServer.length == amountClusterServerNotWorkingOrFailed)  // all shutdown or failed :-((
        		myResultList.put("ClusterState", "NOT WORKING OR FAILED");
        	else  if (amountClusterServerRunning>0) // partly running
        		myResultList.put("ClusterState", "PARTLY RUNNING");
        	else
        		myResultList.put("ClusterState", "NOT WORKING OR FAILED");
        }
        else
        {
        	// no cluster ?!?!?!
        	myResultList.put("ClusterState", "NO Cluster configured");
        }
        
        
        // add values to result list
        myResultList.put("NrServers",      ""+amountOfMSServer);
        myResultList.put("SrvRunning",     ""+amountRunning);
        myResultList.put("SrvFailedAdmin", ""+amountFailedAdmin);
        myResultList.put("SrvDown",        ""+amountShutdown);

    	return myResultList;
    }
    

    
    /**
     * WebLogic Server View:
			S_ID                 # Unique server ID
			S_Name           # server name
			D_ID                 # (unique ID from domain table)
			D_Name           # domain name
			S_State
			S_HealthState
			S_JVM_HeapFree
			S_JVM_HeapSize
			S_JTA_Health
			S_JTA_TxTotal
			S_JTA-TxRolledBack
			S_JTA-TxAbandoned
			S_Thread-Health
			S_Thread-QueueLength			
			S_Thread-CompletedRequest
			S_Thread-Throughput
			
     * @return
     * @throws Exception
     */
    public HashMap<String, String> getServerData() throws Exception
    {
    	HashMap<String, String> myResultList = new HashMap<String, String>();
    	
        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(mainDomainValues.get("serverName"));

 
        // add Name
        myResultList.put("Name", (String)mainDomainValues.get("serverName"));

        // add State
        myResultList.put("State", myJMXWrapper.getServerState((String)mainDomainValues.get("serverName")));
        
        // HealthState
        myResultList.put("HealthState",getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(serverRuntime,"HealthState")));
        

        ObjectName jvmRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JVMRuntime");
        // add HeapFreeCurrent
        myResultList.put("HeapFreeCurrent",myJMXWrapper.getAttribute(jvmRuntime, "HeapFreeCurrent").toString());
        // add HeapSizeCurrent
        myResultList.put("HeapSizeCurrent",myJMXWrapper.getAttribute(jvmRuntime, "HeapSizeCurrent").toString());

        // add Uptime
        myResultList.put("Uptime",myJMXWrapper.getAttribute(jvmRuntime, "Uptime").toString());
        
       
        ObjectName jtaRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JTARuntime");

        // HealthState
        myResultList.put("JTA_HealthState",getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(jtaRuntime,"HealthState")));

        // add TransactionTotalCount
        myResultList.put("TransactionTotalCount",myJMXWrapper.getAttribute(jtaRuntime, "TransactionTotalCount").toString());

        // add TransactionCommittedTotalCount
        myResultList.put("TransactionCommittedTotalCount",myJMXWrapper.getAttribute(jtaRuntime, "TransactionCommittedTotalCount").toString());
        
        // add ActiveTransactionsTotalCount
        myResultList.put("ActiveTransactionsTotalCount",myJMXWrapper.getAttribute(jtaRuntime, "ActiveTransactionsTotalCount").toString());
        
        // add TransactionRolledBackTotalCount
        myResultList.put("TransactionRolledBackTotalCount",myJMXWrapper.getAttribute(jtaRuntime, "TransactionRolledBackTotalCount").toString());

        // add TransactionAbandonedTotalCount
        myResultList.put("TransactionAbandonedTotalCount",myJMXWrapper.getAttribute(jtaRuntime, "TransactionAbandonedTotalCount").toString());

        
        // Thread
        ObjectName threadPoolRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "ThreadPoolRuntime");
        // HealthState
        myResultList.put("Thread_HealthState",getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(threadPoolRuntime,"HealthState")));

        // add CompletedRequestCount
        myResultList.put("CompletedRequestCount",myJMXWrapper.getAttribute(threadPoolRuntime, "CompletedRequestCount").toString());

        // add ExecuteThreadTotalCount
        myResultList.put("ExecuteThreadTotalCount",myJMXWrapper.getAttribute(threadPoolRuntime, "ExecuteThreadTotalCount").toString());
        
        // add PendingUserRequestCount
        myResultList.put("PendingUserRequestCount",myJMXWrapper.getAttribute(threadPoolRuntime, "PendingUserRequestCount").toString());

        // add QueueLength
        myResultList.put("QueueLength",myJMXWrapper.getAttribute(threadPoolRuntime, "QueueLength").toString());

        // add Throughput
        myResultList.put("Throughput",myJMXWrapper.getAttribute(threadPoolRuntime, "Throughput").toString());

        // OpenSocketsCurrentCount
        myResultList.put("OpenSocketsCurrentCount",myJMXWrapper.getAttribute(serverRuntime, "OpenSocketsCurrentCount").toString());
        
		// add value of field SocketsOpenedTotalCount
        myResultList.put("SocketsOpenedTotalCount",myJMXWrapper.getAttribute(serverRuntime, "SocketsOpenedTotalCount").toString());

		// add value of field ActivationTime
        myResultList.put("ActivationTime",myJMXWrapper.getAttribute(serverRuntime, "ActivationTime").toString());

    	return myResultList;
    }

}
