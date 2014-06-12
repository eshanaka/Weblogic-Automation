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

public class JMSMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public JMSMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    
    private static String getHealthStateInformation(weblogic.health.HealthState myState)
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
    
    
    // prints the overall health state of the JMS runtime system
    public void printJMSRuntimeState(String myServerName)  throws WLSAutomationException
    {
    	try
    	{
            ObjectName serverRuntime = myJMXWrapper.getServerRuntime(myServerName);
            
            // get JMS runtime object reference
            ObjectName myJMSRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");

            // print state
            String health = getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(myJMSRuntime, "HealthState"));
            String name = (String)myJMXWrapper.getAttribute(myJMSRuntime, "Name");
        	System.out.println("JMSRuntime "+name+" current health state = "+health);
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }

    // prints overall connection information of the JMS runtime system. 
    // The metrics available are current, high and total connection count
    public void printJMSRuntimeInformation(String myServerName)  throws WLSAutomationException
    {
    	try
    	{
        	ObjectName serverRuntime = myJMXWrapper.getServerRuntime(myServerName);
            
            // get JMS runtime object reference
            ObjectName myJMSRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");
            String name = (String)myJMXWrapper.getAttribute(myJMSRuntime, "Name");
            
        	// System.out.println(connection summary information
        	System.out.println("JMSRuntime "+name+" ConnectionsCurrentCount = "+myJMXWrapper.getAttribute(myJMSRuntime, "ConnectionsCurrentCount"));
        	System.out.println("JMSRuntime "+name+" ConnectionsHighCount = "+myJMXWrapper.getAttribute(myJMSRuntime, "ConnectionsHighCount"));
        	System.out.println("JMSRuntime "+name+" ConnectionsTotalCount = "+myJMXWrapper.getAttribute(myJMSRuntime, "ConnectionsTotalCount"));
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }
    

    public Hashtable<String, ObjectName> getListOfJMSServerFromRuntime(ObjectName myJMSRuntime)  throws WLSAutomationException
    {
    	try
    	{
        	Hashtable<String, ObjectName> result = new Hashtable<String, ObjectName>();
        	
            ObjectName[] jmsServers = (ObjectName[])myJMXWrapper.getAttribute(myJMSRuntime, "JMSServers");
            for (int i=0;i<jmsServers.length;i++)
            {
               String nextName = (String)myJMXWrapper.getAttribute(jmsServers[i], "Name");
               result.put(nextName, jmsServers[i]);
            }
            		
            // return list
            return result;
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }
    
    public void printSessionPoolInformation(String myServerName, String myJMSServer)  throws WLSAutomationException
    {
    	try
    	{
            ObjectName serverRuntime = myJMXWrapper.getServerRuntime(myServerName);
            
            // get JMS runtime object reference
            ObjectName myJMSRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");

            ObjectName myJMSServerReference = getListOfJMSServerFromRuntime(myJMSRuntime).get(myJMSServer);
            if (myJMSServerReference==null)
            {
            	System.out.println("JMS Server "+myJMSServer+" does not exit !");
            }
            else
            {
            	System.out.println("The SessionPoolsCurrentCount of JMS server "+myJMSServer+" is "+ myJMXWrapper.getAttribute(myJMSRuntime, "SessionPoolsCurrentCount"));
            	System.out.println("The SessionPoolsHighCount of JMS server "+myJMSServer+" is "+ myJMXWrapper.getAttribute(myJMSRuntime, "SessionPoolsHighCount"));
            	System.out.println("The SessionPoolsTotalCount of JMS server "+myJMSServer+" is "+ myJMXWrapper.getAttribute(myJMSRuntime, "SessionPoolsTotalCount"));
            }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    } 	



    public ObjectName getJMSDestinationRuntime(String destinationName, String jmsServerName, String wlsServerName) throws WLSAutomationException
	{
		try
		{    
			 // get the runtime of the server
			 ObjectName serverRuntime = myJMXWrapper.getServerRuntime(wlsServerName);
			 
			 // the the jms runtime
			 ObjectName jmsRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");
			 
			 if (jmsRuntime!=null)
			 {
				 ObjectName[] jmsServerRuntimes = (ObjectName[]) myJMXWrapper.getAttribute(jmsRuntime, "JMSServers");
                 
                 for (int i=0;i<jmsServerRuntimes.length;i++) 
                 	 if (jmsServerName.equals((String)myJMXWrapper.getAttribute(jmsServerRuntimes[i], "Name")))
                 	 {
                 		 // found jms server
                 		ObjectName[] destinationRuntimes = (ObjectName[]) myJMXWrapper.getAttribute(jmsServerRuntimes[i], "Destinations");
                        for (int d=0;d<destinationRuntimes.length;d++) 
                        	if (destinationName.equals((String)myJMXWrapper.getAttribute(destinationRuntimes[d], "Name")))
                        		return destinationRuntimes[d];
                 	 }
             
                 throw new WLSAutomationException("JMSServer "+jmsServerName+" or destination "+destinationName+" not found on server "+wlsServerName+" ! ");
			 }
			 else
				 throw new WLSAutomationException("No JMSRuntime found on server "+wlsServerName+" ! ");
			 
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in getJMSDestinationRuntime : "+ ex.getMessage());
		}
	}        
    
    
    
    public void printQueueState(String myServerName, String myJMSServer, String myQueueName) throws WLSAutomationException
    {
    	try
    	{
            ObjectName myQueue = getJMSDestinationRuntime(myQueueName, myJMSServer, myServerName);
            
            if (myQueue != null)
            {
            	System.out.println("Information about the queue: "+myQueueName);
            	// Detail states
            	System.out.println("The InsertionPausedState of queue "+myQueueName+" is "+ myJMXWrapper.getAttribute(myQueue, "InsertionPausedState"));
            	System.out.println("The ConsumptionPausedState of queue "+myQueueName+" is "+ myJMXWrapper.getAttribute(myQueue, "ConsumptionPausedState"));
            	System.out.println("The ProductionPausedState of queue "+myQueueName+" is "+ myJMXWrapper.getAttribute(myQueue, "ProductionPausedState"));
            }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }	
    	
    	
    	
    //Message count for certain queues
    //The following example shows a little script which prints out the amount of message which are actually available in a queue. This
    //might give you an indicator about a backlog of work to do or especially in case or error or exception queues will provide you with
    //the information about the number of problems occurred.
    public void printAmountOfMessagesInDestination(String myServerName, String myJMSServer, String myDestinationName) throws WLSAutomationException
    {
    	try
    	{
            ObjectName myDestination = getJMSDestinationRuntime(myDestinationName, myJMSServer, myServerName);

        	// System.out.println(amount of messages
        	System.out.println("The number of messages in the queue/topic "+myDestinationName +" is "+ myJMXWrapper.getAttribute(myDestination, "MessagesCurrentCount"));
        	// System.out.println(amount of pending messages
        	System.out.println("The number of messages in the queue/topic "+myDestinationName +" is "+ myJMXWrapper.getAttribute(myDestination, "MessagesPendingCount"));	
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }

    // System.out.println(information about connected JMS clients
    public void printConnectedJMSClients(String myServerName) throws WLSAutomationException
    {
    	try
    	{
            ObjectName serverRuntime = myJMXWrapper.getServerRuntime(myServerName);
            
            // get JMS runtime object reference
            ObjectName myJMSRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");

        	// get all JMS connections for all JMS servers for this runtime
        	// note that all connection instances are of type JMSConnectionRuntimeMBean
            ObjectName[] jmsConnections = (ObjectName[])myJMXWrapper.getAttribute(myJMSRuntime, "Connections");
            
        	// amount
        	System.out.println("Actually the "+myServerName+".jms runtime has " + jmsConnections.length + " connections !");
        	
        	// now examine all jms connections and get some basic informations
        	for (int i=0;i<jmsConnections.length;i++)
        	{
        		System.out.println("Connection: "+myJMXWrapper.getAttribute(jmsConnections[i], "HostAddress")+
        				           " with client id = "+myJMXWrapper.getAttribute(jmsConnections[i], "ClientID"));
        	    // print all sessions associated with this connection
        	    System.out.println(" has actually "+myJMXWrapper.getAttribute(jmsConnections[i], "SessionsCurrentCount") + 
        	    		           " active sessions");
        	        	
        	    // get all sessions
        	    ObjectName[] allConnectionJMSSessions = (ObjectName[])myJMXWrapper.getAttribute(jmsConnections[i], "Sessions");

            	// iterate of sessions
            	for (int session=0;session<allConnectionJMSSessions.length;session++)
            	{
                	// print some session information
                	System.out.println(" Session: Active consumers: "+
                			           myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "ConsumersCurrentCount"));
                	System.out.println(" Active producer: "+
                			           myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "ProducersCurrentCount"));
                	System.out.println(" Messages: send:"+myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "MessagesSentCount")+
                			           " received:"+myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "MessagesReceivedCount")+
                			           " pending:"+myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "MessagesPendingCount"));

            	
                	// iterate over producer
                	ObjectName[] activeJMSProducer = (ObjectName[])myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "Producers");
                	for(int producer=0;producer<activeJMSProducer.length;producer++)
                	{
                	   // ... System.out.println(information you want -> see API);
                	}	
                	// iterate over consumer
                    ObjectName[] activeJMSConsumers = (ObjectName[])myJMXWrapper.getAttribute(allConnectionJMSSessions[session], "Consumers");
                	for(int consumer=0;consumer<activeJMSConsumers.length;consumer++)
                	{
                	   // ... System.out.println(information you want -> see API);
                	}	
            	}
        	    
        	}
        	
        	
        	
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printJMSRuntimeState : "+ ex.getMessage());
		}
    }

}


