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
package com.wlsautomation.monitoring;

import java.util.*;

import javax.management.*;

import com.wlsautomation.utils.*;

public class ApplicationMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public ApplicationMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    public HashMap<String, ObjectName> getApplicationNames(String serverName, boolean onlyIncludeEARs) throws Exception
    {
        HashMap<String, ObjectName> myResultList = new HashMap<String, ObjectName>();

        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);

        ObjectName[] allApplicationRuntimeMBeans = (ObjectName[])myJMXWrapper.getAttribute(serverRuntime,"ApplicationRuntimes");

        String nextApplicationName = null;
        for (int i=0;i<allApplicationRuntimeMBeans.length;i++)
        {
           nextApplicationName = (String) myJMXWrapper.getAttribute(allApplicationRuntimeMBeans[i],"Name");

           // Ignore ADMIN console
           if ("consoleapp".equals(nextApplicationName))
               continue;

           if (onlyIncludeEARs)
           {
              Boolean isEAR = (Boolean) myJMXWrapper.getAttribute(allApplicationRuntimeMBeans[i],"EAR");
              if (isEAR)
                  myResultList.put(nextApplicationName,allApplicationRuntimeMBeans[i]);
           }
           else
               myResultList.put(nextApplicationName,allApplicationRuntimeMBeans[i]);
        }

        return myResultList;
    }

    // print monitoring information about all application components
    public void printApplicationMonitoringInformation(ObjectName applicationRuntimeMBean) throws Exception
    {
        // get component runtimes
        ObjectName[] componentRuntimes =  (ObjectName[]) myJMXWrapper.getAttribute(applicationRuntimeMBean, "ComponentRuntimes");

        for (int compNumber=0; compNumber < componentRuntimes.length; compNumber++)
        {
           String componentType = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Type");
           String name = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Name");
           if (componentType.toString().equals("WebAppComponentRuntime"))
           {
        	   printWebAppComponentInformation(name,componentRuntimes[compNumber]);
        	   printServletInvocationCountInformation(componentRuntimes[compNumber]);
           }
           else if (componentType.toString().equals("EJBComponentRuntime"))
           {
        	   printEJBInformation(componentRuntimes[compNumber], true);
           }
           if (componentType.toString().equals("ConnectorComponentRuntime"))
           {
        	   printConnectorMonitoringValues(componentRuntimes[compNumber]);
           }
        }
    }    
    

    public HashMap<String, ObjectName> getWebComponentNames(ObjectName applicationRuntimeMBean) throws Exception
    {
        HashMap<String, ObjectName> myResultList = new HashMap<String, ObjectName>();

        // get component runtimes
        ObjectName[] componentRuntimes =  (ObjectName[]) myJMXWrapper.getAttribute(applicationRuntimeMBean, "ComponentRuntimes");

        for (int compNumber=0; compNumber < componentRuntimes.length; compNumber++)
        {
           String componentType = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Type");
           if (componentType.toString().equals("WebAppComponentRuntime"))
           {
              String name = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Name");
              myResultList.put(name,componentRuntimes[compNumber]);
           }

        }
        return myResultList;
    }

    public HashMap<String, ObjectName> getEJBComponentNames(ObjectName applicationRuntimeMBean) throws Exception
    {
        HashMap<String, ObjectName> myResultList = new HashMap<String, ObjectName>();

        // get component runtimes
        ObjectName[] componentRuntimes =  (ObjectName[]) myJMXWrapper.getAttribute(applicationRuntimeMBean, "ComponentRuntimes");

        for (int compNumber=0; compNumber < componentRuntimes.length; compNumber++)
        {
           String componentType = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Type");
           if (componentType.toString().equals("EJBComponentRuntime"))
           {
              String name = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Name");
              myResultList.put(name,componentRuntimes[compNumber]);
           }

        }
        return myResultList;
    }
    

    public void printApplicationInformation(String applicationName,ObjectName applicationRuntimeMBean) throws Exception
    {
        // print Name
    	System.out.println(" Application information for component: "+applicationName);
        // print ApplicationName
        System.out.println("   ApplicationName=" + myJMXWrapper.getAttribute(applicationRuntimeMBean, "ApplicationName"));
        // print ApplicationVersion
        System.out.println("   ApplicationVersion=" + myJMXWrapper.getAttribute(applicationRuntimeMBean, "ApplicationVersion"));
        // print EAR
        System.out.println("   EAR=" + myJMXWrapper.getAttribute(applicationRuntimeMBean, "EAR"));
        // HealthState
        System.out.println("   HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(applicationRuntimeMBean,"HealthState")));
    }


    public void printWebAppComponentInformation(String webappName,ObjectName webAppComponentRuntime) throws Exception
    {
        // print Name
    	System.out.println(" Webapplication information for component: "+webappName);
        // print ContextRoot
        System.out.println("   ContextRoot=" + myJMXWrapper.getAttribute(webAppComponentRuntime, "ContextRoot"));
        // print Status
        System.out.println("   Status=" + myJMXWrapper.getAttribute(webAppComponentRuntime, "Status"));
        // print OpenSessionsCurrentCount
        System.out.println("   OpenSessionsCurrentCount=" + myJMXWrapper.getAttribute(webAppComponentRuntime, "OpenSessionsCurrentCount"));
        // print OpenSessionsHighCount
        System.out.println("   OpenSessionsHighCount=" + myJMXWrapper.getAttribute(webAppComponentRuntime, "OpenSessionsHighCount"));
        // print SessionsOpenedTotalCount
        System.out.println("   SessionsOpenedTotalCount=" + myJMXWrapper.getAttribute(webAppComponentRuntime, "SessionsOpenedTotalCount"));
    }


   public void printServletInvocationCountInformation(ObjectName webAppComponentRuntime) throws Exception
   {
       // calculcate the total summary of all calls
       long invocationTotal = 0;

       // get all servlet MBeans
       ObjectName[] servletRuntimes = (ObjectName[])myJMXWrapper.getAttribute(webAppComponentRuntime, "Servlets");

       for (int servletNumber=0; servletNumber<servletRuntimes.length; servletNumber++)
       {
            String nextName = (String)myJMXWrapper.getAttribute(servletRuntimes[servletNumber], "Name");
            int contextInvocationTotalCount = (Integer)myJMXWrapper.getAttribute(servletRuntimes[servletNumber], "InvocationTotalCount");

            // sum up
            invocationTotal += contextInvocationTotalCount;

            // print to list
            System.out.println("        Servlet:"+nextName+"  Invocations:"+new Integer(contextInvocationTotalCount));
       }

       // print SUMMARY to list
       System.out.println("   Overall-InvocationTotalCount" + new Long(invocationTotal));
   }
   
   
   
   public String getApplicationComponentState(ObjectName componentRuntime)  throws Exception
   {
	  int myDeploymentState = (Integer)myJMXWrapper.getAttribute(componentRuntime, "DeploymentState");
	  
	  if (myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.UNPREPARED)
	   return "UNPREPARED";
	  else if (myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.PREPARED)
	   return "PREPARED";
	  else if (myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.ACTIVATED)
	   return "ACTIVATED";
	  else if (myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.NEW)
	   return "NEW";
	  
	  // oh oh
	  return "UNKNOWN";
   }
   
   
   public void printEJBInformation(ObjectName ejbComponentRuntime, boolean printDetails) throws Exception
   {
	   String myName = (String)myJMXWrapper.getAttribute(ejbComponentRuntime, "Name");
	   System.out.println("Found EJB modul "+myName+" with current deployment state = " + getApplicationComponentState(ejbComponentRuntime));
   
	   // now look at the submodules
	   ObjectName[] myEJBs = (ObjectName[])myJMXWrapper.getAttribute(ejbComponentRuntime, "EJBRuntimes");
	   
	   for (int i=0;i<myEJBs.length;i++)   
	   {
		   String ejbName = (String)myJMXWrapper.getAttribute(myEJBs[i], "Name");
		   String ejbType = (String)myJMXWrapper.getAttribute(myEJBs[i], "Type");
		   System.out.println("   EJB: "+ejbName+" is of type "+ejbType);
		   
		   if (printDetails)
		   {
			   // all have transactions
  			   //transaction information from TransactionRuntime
  			   ObjectName myTxRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "TransactionRuntime");
  			   System.out.println("   TransactionInfo: total:"+myJMXWrapper.getAttribute(myTxRuntime, "TransactionsCommittedTotalCount")+
  					                                  "rolledback:"+myJMXWrapper.getAttribute(myTxRuntime, "TransactionsRolledBackTotalCount")+
  					                                  " timedout:" +myJMXWrapper.getAttribute(myTxRuntime, "TransactionsTimedOutTotalCount"));

  			   if ("StatelessEJBRuntime".equals(ejbType))
			   {
	   				// pool information from PoolRuntime
				    ObjectName myPoolRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "PoolRuntime");
				    System.out.println("   PoolInfo: totalAccess:"+myJMXWrapper.getAttribute(myPoolRuntime, "AccessTotalCount")+
				    		           "   beansInUse:"+myJMXWrapper.getAttribute(myPoolRuntime, "BeansInUseCount")+
				    		           "   beansInUseCurrent:"+myJMXWrapper.getAttribute(myPoolRuntime, "BeansInUseCurrentCount")+
				    		           "   destroyed:"+myJMXWrapper.getAttribute(myPoolRuntime, "DestroyedTotalCount")+
				    		           "   idle:"+myJMXWrapper.getAttribute(myPoolRuntime, "IdleBeansCount")+
				    		           "   pooledCurrent:"+myJMXWrapper.getAttribute(myPoolRuntime, "PooledBeansCurrentCount")+
				    		           "   timedout:"+myJMXWrapper.getAttribute(myPoolRuntime, "TimeoutTotalCount"));
	   				// timer consists of a list of timers
				    ObjectName myTimerRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "TimerRuntime");
	   				if (myTimerRuntime != null)
	   					System.out.println("   Timer information: Name:"+myJMXWrapper.getAttribute(myTimerRuntime, "Name")+
	   							           "   activeTimers:"+myJMXWrapper.getAttribute(myTimerRuntime, "ActiveTimerCount")+
	   							           "   timeout:"+myJMXWrapper.getAttribute(myTimerRuntime, "TimeoutCount")+
	   							           "   cancelled:"+myJMXWrapper.getAttribute(myTimerRuntime, "CancelledTimerCount")+
	   							           "   disabled:"+myJMXWrapper.getAttribute(myTimerRuntime, "DisabledTimerCount"));
			   }
			   else if ("StatefulEJBRuntime".equals(ejbType))
			   {
	   				// cache information from CacheRuntime
				    ObjectName myCacheRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "CacheRuntime");
				    System.out.println("   CacheInfo: hits:"+myJMXWrapper.getAttribute(myCacheRuntime, "CacheHitCount")+
				    		           "   currentBeans:"+myJMXWrapper.getAttribute(myCacheRuntime, "CachedBeansCurrentCount")+
				    		           "   access:"+myJMXWrapper.getAttribute(myCacheRuntime, "CacheAccessCount"));
	   				// locking information from LockingRuntime
				    ObjectName myLockingRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "LockingRuntime");
				    System.out.println("   LockingInfo: currentCount:"+myJMXWrapper.getAttribute(myLockingRuntime, "LockEntriesCurrentCount")+
				    		           "   accessCount:"+myJMXWrapper.getAttribute(myLockingRuntime, "LockManagerAccessCount")+
				    		           "   timeoutTotalCount:"+myJMXWrapper.getAttribute(myLockingRuntime, "TimeoutTotalCount"));
				   
			   }
			   else if ("EntityEJBRuntime".equals(ejbType))
			   {
				   ObjectName entityPool =  (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "PoolRuntime");
				   
				   System.out.println("   PooledBeansCurrent ="+ myJMXWrapper.getAttribute(entityPool,"PooledBeansCurrentCount")+
				                      "   AccessTotal ="+ myJMXWrapper.getAttribute(entityPool,"AccessTotalCount")+
				                      "   DestroyedTotal ="+ myJMXWrapper.getAttribute(entityPool,"DestroyedTotalCount")+
				                      "   IdleBeans ="+ myJMXWrapper.getAttribute(entityPool,"IdleBeansCount")+
				                      "   BeansInUse ="+ myJMXWrapper.getAttribute(entityPool,"BeansInUseCount")+
				                      "   BeansInUseCurrent ="+ myJMXWrapper.getAttribute(entityPool,"BeansInUseCurrentCount")+
				                      "   WaiterTotal ="+ myJMXWrapper.getAttribute(entityPool,"WaiterTotalCount")+
				                      "   WaiterCurrent ="+ myJMXWrapper.getAttribute(entityPool,"WaiterCurrentCount")+
				                      "   TimeoutTotal ="+ myJMXWrapper.getAttribute(entityPool,"TimeoutTotalCount"));
			   }
			   else if ("MessageDrivenEJBRuntime".equals(ejbType))
			   {
		            // print the mdb status
		            System.out.println("   ConnectionStatus = "+myJMXWrapper.getAttribute(myEJBs[i], "ConnectionStatus"));
		            // print status of the MDB
		            System.out.println("   MDBStatus = "+myJMXWrapper.getAttribute(myEJBs[i], "MDBStatus"));
		            // client id
		            System.out.println("   JmsClientID = "+myJMXWrapper.getAttribute(myEJBs[i], "JmsClientID"));
		            // count of processed messages
		            System.out.println("   ProcessedMessageCount = "+myJMXWrapper.getAttribute(myEJBs[i], "ProcessedMessageCount"));
		            // amout of suspended messages
		            System.out.println("   SuspendCount = "+myJMXWrapper.getAttribute(myEJBs[i], "SuspendCount"));
		            // healthstate of MDB
		            System.out.println("   HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(myEJBs[i],"HealthState")));

	   				// pool information from PoolRuntime
				    ObjectName myPoolRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "PoolRuntime");
				    System.out.println("   PoolInfo: totalAccess:"+myJMXWrapper.getAttribute(myPoolRuntime, "AccessTotalCount")+
				    		           "   beansInUse:"+myJMXWrapper.getAttribute(myPoolRuntime, "BeansInUseCount")+
				    		           "   beansInUseCurrent:"+myJMXWrapper.getAttribute(myPoolRuntime, "BeansInUseCurrentCount")+
				    		           "   destroyed:"+myJMXWrapper.getAttribute(myPoolRuntime, "DestroyedTotalCount")+
				    		           "   idle:"+myJMXWrapper.getAttribute(myPoolRuntime, "IdleBeansCount")+
				    		           "   pooledCurrent:"+myJMXWrapper.getAttribute(myPoolRuntime, "PooledBeansCurrentCount")+
				    		           "   timedout:"+myJMXWrapper.getAttribute(myPoolRuntime, "TimeoutTotalCount"));
	   				// timer consists of a list of timers
				    ObjectName myTimerRuntime = (ObjectName)myJMXWrapper.getAttribute(myEJBs[i], "TimerRuntime");
	   				if (myTimerRuntime != null)
	   					System.out.println("   Timer information: Name:"+myJMXWrapper.getAttribute(myTimerRuntime, "Name")+
	   							           "   activeTimers:"+myJMXWrapper.getAttribute(myTimerRuntime, "ActiveTimerCount")+
	   							           "   timeout:"+myJMXWrapper.getAttribute(myTimerRuntime, "TimeoutCount")+
	   							           "   cancelled:"+myJMXWrapper.getAttribute(myTimerRuntime, "CancelledTimerCount")+
	   							           "   disabled:"+myJMXWrapper.getAttribute(myTimerRuntime, "DisabledTimerCount"));
			   }
		   }
	   }
   
   }   
   
   
 public HashMap<String, ObjectName> getJCAConnectorRuntimes(ObjectName applicationRuntimeMBean) throws Exception
 {
     HashMap<String, ObjectName> myResultList = new HashMap<String, ObjectName>();

     // get component runtimes
     ObjectName[] componentRuntimes =  (ObjectName[]) myJMXWrapper.getAttribute(applicationRuntimeMBean, "ComponentRuntimes");

     for (int compNumber=0; compNumber < componentRuntimes.length; compNumber++)
     {
        String componentType = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Type");
        if (componentType.toString().equals("ConnectorComponentRuntime"))
        {
           String name = (String) myJMXWrapper.getAttribute(componentRuntimes[compNumber], "Name");
           myResultList.put(name,componentRuntimes[compNumber]);
        }

     }
     return myResultList;
 }
 
   public void printConnectorMonitoringValues(ObjectName connectorRuntime) throws Exception
   {
	 String myName = (String)myJMXWrapper.getAttribute(connectorRuntime, "Name");
	 System.out.println("Found Connector modul "+myName+" with current deployment state = " + getApplicationComponentState(connectorRuntime));
     
     int myDeploymentState = (Integer)  myJMXWrapper.getAttribute(connectorRuntime,"DeploymentState");
     String d_state = "UNKNOWN";
     
     if (myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.UNPREPARED)
     	d_state = "UNPREPARED";
     else if(myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.PREPARED)
     	d_state = "PREPARED";
     else if(myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.ACTIVATED)
     	d_state = "ACTIVATED";
     else if(myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.NEW)
     	 d_state = "NEW";
     
     // add T_DeploymentState
     System.out.println("   DeploymentState: "+ d_state);

     
     ObjectName[] myConPools = (ObjectName[])myJMXWrapper.getAttribute(connectorRuntime,"ConnectionPools");
     if (myConPools.length > 0)
     {
     	// monitor only first pool !
     	
         System.out.println("   ActiveConCurrent: "+ myJMXWrapper.getAttribute(connectorRuntime, "ActiveConnectionsCurrentCount"));	
         System.out.println("   ActiveConHigh: "+ myJMXWrapper.getAttribute(connectorRuntime, "ActiveConnectionsHighCount"));	
         System.out.println("   ConCreatedTotal: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsCreatedTotalCount"));	
         System.out.println("   ConRejectedTotal: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsRejectedTotalCount"));	
         System.out.println("   ConDestroyedTotal: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsDestroyedTotalCount"));	
         System.out.println("   FreeConCurrent: "+ myJMXWrapper.getAttribute(connectorRuntime, "FreeConnectionsCurrentCount"));	
         System.out.println("   FreeConHigh: "+ myJMXWrapper.getAttribute(connectorRuntime, "FreeConnectionsHighCount"));	
         System.out.println("   AverageActiveUsage: "+ myJMXWrapper.getAttribute(connectorRuntime, "AverageActiveUsage"));	
         System.out.println("   CloseCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "CloseCount"));	
         System.out.println("   ConnectionsDestroyedByErrorTotalCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsDestroyedByErrorTotalCount"));	
         System.out.println("   ConnectionsDestroyedByShrinkingTotalCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsDestroyedByShrinkingTotalCount"));	
         System.out.println("   ConnectionsMatchedTotalCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "ConnectionsMatchedTotalCount"));	
         System.out.println("   CurrentCapacity: "+ myJMXWrapper.getAttribute(connectorRuntime, "CurrentCapacity"));	
         System.out.println("   MaxCapacity: "+ myJMXWrapper.getAttribute(connectorRuntime, "MaxCapacity"));	
         System.out.println("   MaxIdleTime: "+ myJMXWrapper.getAttribute(connectorRuntime, "MaxIdleTime"));	
         System.out.println("   NumUnavailableCurrentCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "NumUnavailableCurrentCount"));	
         System.out.println("   NumUnavailableHighCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "NumUnavailableHighCount"));	
         System.out.println("   NumWaiters: "+ myJMXWrapper.getAttribute(connectorRuntime, "NumWaiters"));	
         System.out.println("   NumWaitersCurrentCount: "+ myJMXWrapper.getAttribute(connectorRuntime, "NumWaitersCurrentCount"));	
         System.out.println("   RecycledTotal: "+ myJMXWrapper.getAttribute(connectorRuntime, "RecycledTotal"));	
         System.out.println("   ShrinkCountDownTime: "+ myJMXWrapper.getAttribute(connectorRuntime, "ShrinkCountDownTime"));	
         System.out.println("   ShrinkPeriodMinutes: "+ myJMXWrapper.getAttribute(connectorRuntime, "ShrinkPeriodMinutes"));	
     }
  }	
 
 
 
   
}
