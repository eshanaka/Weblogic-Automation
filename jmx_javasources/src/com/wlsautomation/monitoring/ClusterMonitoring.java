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

import javax.management.*;

import com.wlsautomation.utils.*;

public class ClusterMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public ClusterMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

	public void printClusterStates() throws Exception
	{
        ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();
        
        System.out.println("Cluster state information:");
        ObjectName[] allClusters = (ObjectName[])myJMXWrapper.getAttribute(myDomainMBean,"Clusters");
        
        for (int c=0;c<allClusters.length;c++)
        {
        	// use 1st cluster !!!
        	// get cluster member
        	ObjectName[] clusterServer = (ObjectName[])myJMXWrapper.getAttribute(allClusters[c],"Servers");
        	String nextName = (String)myJMXWrapper.getAttribute(allClusters[c],"Name");
        	        	
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
            
        	if (clusterServer.length == amountClusterServerRunning)  // all running
        		System.out.println("  Cluster "+nextName+" has the state: RUNNING" );
        	else if (clusterServer.length == amountClusterServerShutdown)  // all shutdown or failed :-((
        		System.out.println("  Cluster "+nextName+" has the state: SHUTDOW" );
        	else if (clusterServer.length == amountClusterServerNotWorkingOrFailed)  // all shutdown or failed :-((
        		System.out.println("  Cluster "+nextName+" has the state: NOT WORKING OR FAILED");
        	else  if (amountClusterServerRunning>0) // partly running
        		System.out.println("  Cluster "+nextName+" has the state: PARTLY RUNNING" );
        	else
        		System.out.println("  Cluster "+nextName+" has the state: NOT WORKING OR FAILED" );
        }
        
        // if cluster list is empty
        if (allClusters.length==0)
        {
        	// no cluster ?!?!?!
        	System.out.println("NO Cluster configured" );
        }		
	}
}
