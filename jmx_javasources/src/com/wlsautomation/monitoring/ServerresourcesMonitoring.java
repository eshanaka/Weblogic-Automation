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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.management.ObjectName;

import com.wlsautomation.utils.*;

public class ServerresourcesMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public ServerresourcesMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }




	public void printAllWorkManagerInfos()	  throws Exception
	{
	    Hashtable<String,String> mainDomainValues = myJMXWrapper.getMainServerDomainValues(); 

        Set<ObjectName> mySet = (Set<ObjectName>)myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,ServerRuntime="+mainDomainValues.get("serverName")+",Type=WorkManagerRuntime"), null);
        Iterator<ObjectName> it = mySet.iterator();
        System.out.println("All WorkManagerRuntime MBeans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n");
		
		
		
		
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,ServerRuntime="+mainDomainValues.get("serverName")+",Type=MaxThreadsConstraintRuntime"), null);
        it = mySet.iterator();
        System.out.println("All MaxThreadsConstraintRuntime MBeans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n");
		
        
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,Type=MaxThreadsConstraint"), null);
        it = mySet.iterator();
        System.out.println("All MaxThreadsConstraint MBeans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n");

        
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,ServerRuntime="+mainDomainValues.get("serverName")+",Type=MinThreadsConstraintRuntime"), null);
        it = mySet.iterator();
        System.out.println("All MinThreadsConstraintRuntime MBEans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n");

        
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,Type=MinThreadsConstraint"), null);
        it = mySet.iterator();
        System.out.println("All MinThreadsConstraint MBeans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n");

        
        
        
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,Type=RequestClassRuntime"), null);
        it = mySet.iterator();
        System.out.println("All RequestClassRuntime MBEans !");
        while (it.hasNext()) {
            ObjectName myName = (ObjectName) it.next();
            System.out.println("    " + myName.toString());
        }
        System.out.println("\n\n\n");

        
        System.out.println("\n\n\n\n######################################################################################################################");
        
        mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,ServerRuntime="+mainDomainValues.get("serverName")+",Type=WorkManagerRuntime"), null);
        it = mySet.iterator();
        System.out.println("All WorkManagerRuntime MBeans !");
        while (it.hasNext()) {
        	printOneWorkManagerInfo((ObjectName) it.next());
        }
        System.out.println("\n");
	}


	public void printOneWorkManagerInfo(ObjectName wmRuntime) throws Exception 
	{
		System.out.println("\nWorkmanager : "+wmRuntime.toString());
		
		String name   =  (String) myJMXWrapper.getAttribute(wmRuntime, "Name");
		String health = getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(wmRuntime,"HealthState"));        	
		long completedRequests = (Long)myJMXWrapper.getAttribute(wmRuntime, "CompletedRequests");
		int pendingRequests = (Integer)myJMXWrapper.getAttribute(wmRuntime, "PendingRequests");
		int stuckThreadCount = (Integer)myJMXWrapper.getAttribute(wmRuntime, "StuckThreadCount");
		
		System.out.println("    Name: "+ name);
		System.out.println("    Healthstate: "+ health);
		System.out.println("    CompletedRequests: "+ completedRequests);
		System.out.println("    PendingRequests: "+ pendingRequests);
		System.out.println("    StuckThreadCount: "+ stuckThreadCount);
		
		
		ObjectName myMaxThreadsConstraintRuntime = (ObjectName)myJMXWrapper.getAttribute(wmRuntime, "MaxThreadsConstraintRuntime");
		ObjectName myMinThreadsConstraintRuntime = (ObjectName)myJMXWrapper.getAttribute(wmRuntime, "MinThreadsConstraintRuntime");
		ObjectName myRequestClassRuntime = (ObjectName)myJMXWrapper.getAttribute(wmRuntime, "RequestClassRuntime");

		if (myMaxThreadsConstraintRuntime != null)
		{
			String maxContraintName = (String)myJMXWrapper.getAttribute(myMaxThreadsConstraintRuntime, "Name");
			int maxContraintDeferredRequests = (Integer)myJMXWrapper.getAttribute(myMaxThreadsConstraintRuntime, "DeferredRequests");
			int maxContraintExecutingRequests = (Integer)myJMXWrapper.getAttribute(myMaxThreadsConstraintRuntime, "ExecutingRequests");
			
			System.out.println("    MaxThreadsConstraint:  Name: "+maxContraintName+"  DeferredRequests: "+maxContraintDeferredRequests+"    ExecutingRequests: "+maxContraintExecutingRequests);

			String maxONname=myMaxThreadsConstraintRuntime.getKeyProperty("Name");
			if (maxONname != null)
			{
				// try to get config mbean
		        Set<ObjectName> mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,Name="+maxONname+",Type=MaxThreadsConstraint"), null);
		        if (! mySet.isEmpty())
		        {
		        	// get first
		        	ObjectName myFirstConfigName = (ObjectName) mySet.iterator().next();
		        	// get count
		        	int maxCount = (Integer)myJMXWrapper.getAttribute(myFirstConfigName, "Count");
		        	System.out.println("         -> max limit = "+maxCount);
		        	
		        }
		        else
		        {
		        	System.out.println("         -> no config mbean found !");
		        }
			}
		}
		else 
		{
			System.out.println("    MaxThreadsConstraint not defined !");
		}
		
		if (myMinThreadsConstraintRuntime != null)
		{
			String minContraintName = (String)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "Name");
			long minContraintOutOfOrderExecutionCount = (Long)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "OutOfOrderExecutionCount");
			int minContraintPendingRequests = (Integer)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "PendingRequests");
			long minContraintCompletedRequests = (Long)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "CompletedRequests");
			long minContraintMaxWaitTime = (Long)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "MaxWaitTime");
			int minContraintMustRunCount = (Integer)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "MustRunCount");

			long minContraintCurrentWaitTime = (Long)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "CurrentWaitTime");
			int minContraintExecutingRequests = (Integer)myJMXWrapper.getAttribute(myMinThreadsConstraintRuntime, "ExecutingRequests");

			System.out.println("    MinThreadsConstraint:  Name = "+ minContraintName);
			System.out.println("                           OutOfOrderExecutionCount = " + minContraintOutOfOrderExecutionCount);
			System.out.println("                           PendingRequests = " + minContraintPendingRequests);
			System.out.println("                           CompletedRequests = " + minContraintCompletedRequests);
			System.out.println("                           MaxWaitTime = " + minContraintMaxWaitTime);
			System.out.println("                           MustRunCount = " + minContraintMustRunCount);
			System.out.println("                           CurrentWaitTime = " + minContraintCurrentWaitTime);
			System.out.println("                           ExecutingRequests = " + minContraintExecutingRequests);

			String minONname=myMinThreadsConstraintRuntime.getKeyProperty("Name");
			if (minONname != null)
			{
				// try to get config mbean
		        Set<ObjectName> mySet = myJMXWrapper.getConnection().queryNames(new ObjectName("*:*,Name="+minONname+",Type=MinThreadsConstraint"), null);
		        if (! mySet.isEmpty())
		        {
		        	// get first
		        	ObjectName myFirstConfigName = (ObjectName) mySet.iterator().next();
		        	// get count
		        	int minCount = (Integer)myJMXWrapper.getAttribute(myFirstConfigName, "Count");
		        	System.out.println("         -> min limit = "+minCount);
		        	
		        }
		        else
		        {
		        	System.out.println("         -> no config mbean found !");
		        }
			}
		
		
		}
		else {
			System.out.println("    MinThreadsConstraint not defined !");
		}
		
		if (myRequestClassRuntime != null)
		{
			long reqClassTotalThreadUse = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "TotalThreadUse");
			double reqClassInterval = (Double)myJMXWrapper.getAttribute(myRequestClassRuntime, "Interval");
			long reqClassCompletedCount = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "CompletedCount");
			long reqClassVirtualTimeIncrement = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "VirtualTimeIncrement");
			long reqClassThreadUseSquares = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "ThreadUseSquares");
			long reqClassDeltaFirst = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "DeltaFirst");
			int reqClassPendingRequestCount = (Integer)myJMXWrapper.getAttribute(myRequestClassRuntime, "PendingRequestCount");
			long reqClassDeltaRepeat = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "DeltaRepeat");
			long reqClassMyLast = (Long)myJMXWrapper.getAttribute(myRequestClassRuntime, "MyLast");
			String reqClassRequestClassType = (String)myJMXWrapper.getAttribute(myRequestClassRuntime, "RequestClassType");
			String reqClassName = (String)myJMXWrapper.getAttribute(myRequestClassRuntime, "Name");

			System.out.println("    RequestClass:  Name = "+reqClassName+"  is of type "+reqClassRequestClassType);
			System.out.println("                   TotalThreadUse = "+reqClassTotalThreadUse);
			System.out.println("                   Interval = "+reqClassInterval);
			System.out.println("                   CompletedCount = "+reqClassCompletedCount);
			System.out.println("                   VirtualTimeIncrement = "+reqClassVirtualTimeIncrement);
			System.out.println("                   ThreadUseSquares = "+reqClassThreadUseSquares);
			System.out.println("                   DeltaFirst = "+reqClassDeltaFirst);
			System.out.println("                   PendingRequestCount = "+reqClassPendingRequestCount);
			System.out.println("                   DeltaRepeat = "+reqClassDeltaRepeat);
			System.out.println("                   MyLast = "+reqClassMyLast);
		
		
		
		
		
		}
		else {
			System.out.println("    RequestClass not defined !");
		}
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


    
    
    
    public void printNetworkChannelInformation() throws Exception
    {
    	Hashtable<String,String> mainDomainValues = myJMXWrapper.getMainServerDomainValues(); 

        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(mainDomainValues.get("serverName"));

        ObjectName[] serverChannelRuntimes = (ObjectName[])myJMXWrapper.getAttribute(serverRuntime, "ServerChannelRuntimes");
        
        for (int i=0;i<serverChannelRuntimes.length;i++)
        {
        	
        	String channelName = (String)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "Name");
        	String channelURL = (String)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "PublicURL");
        	
        	long channelAcceptCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "AcceptCount");
        	long channelActiveConnectionsCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "ConnectionsCount");
        	long channelBytesReceivedCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "BytesReceivedCount");
        	long channelBytesSentCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "BytesSentCount");
        	long channelMessagesReceivedCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "MessagesReceivedCount");
        	long channelMessagesSentCount = (Long)myJMXWrapper.getAttribute(serverChannelRuntimes[i], "MessagesSentCount");
        	        	
        	System.out.println("Channelname: "+channelName+"  (URL="+channelURL+")");
        	System.out.println("             Total connections count = "+channelAcceptCount);
        	System.out.println("             Actual connected connections = "+channelActiveConnectionsCount);
        	System.out.println("             BytesReceived = "+channelBytesReceivedCount);
        	System.out.println("             BytesSent = "+channelBytesSentCount);
        	System.out.println("             MessagesReceived = "+channelMessagesReceivedCount);
        	System.out.println("             MessagesSent = "+channelMessagesSentCount);
        	System.out.println("\n");
        }
        
        
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
