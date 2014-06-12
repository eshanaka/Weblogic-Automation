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

public class DatasourceMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public DatasourceMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    
    public HashMap<String, ObjectName> getDatasourceNames(String serverName) throws Exception
    {
        HashMap<String, ObjectName> myResultList = new HashMap<String, ObjectName>();

        // ObjectName jdbcServerRuntime = new ObjectName("com.bea:Name="+serverName+",ServerRuntime="+serverName+",Location="+serverName+",Type=JDBCServiceRuntime");
        ObjectName jdbcServerRuntime = new ObjectName("com.bea:Name="+serverName+",ServerRuntime="+serverName+",Type=JDBCServiceRuntime");

        ObjectName[] allDatasourceRuntimeMBeans = (ObjectName[]) myJMXWrapper.getAttribute(jdbcServerRuntime,"JDBCDataSourceRuntimeMBeans");

        String nextDatasourceName = null;
        for (int i=0;i<allDatasourceRuntimeMBeans.length;i++)
        {
           nextDatasourceName = (String)myJMXWrapper.getAttribute(allDatasourceRuntimeMBeans[i],"Name");

           myResultList.put(nextDatasourceName,allDatasourceRuntimeMBeans[i]);
        }

        return myResultList;
    }


    public void printDatasourceInformation(String datasourceName, ObjectName jdbcRuntimeMBean) throws Exception
    {
        // print Name
    	System.out.println("DATASOURCE:   Information about the datasource: "+datasourceName);
    	
    	System.out.println("   Enabled="+ myJMXWrapper.getAttribute(jdbcRuntimeMBean, "Enabled") +
    			           "   State="+ myJMXWrapper.getAttribute(jdbcRuntimeMBean, "State") );
        // print ActiveConnectionsHighCount
    	System.out.println("   ActiveConnectionsHighCount="+ myJMXWrapper.getAttribute(jdbcRuntimeMBean, "ActiveConnectionsHighCount"));
        // print ActiveConnectionsCurrentCount
    	System.out.println("   ActiveConnectionsCurrentCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "ActiveConnectionsCurrentCount"));
        // print ActiveConnectionsAverageCount
    	System.out.println("   ActiveConnectionsAverageCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "ActiveConnectionsAverageCount"));
        // print ConnectionsTotalCount
    	System.out.println("   ConnectionsTotalCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "ConnectionsTotalCount"));
        // print CurrCapacity
    	System.out.println("   CurrCapacity=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "CurrCapacity"));
        // print CurrCapacityHighCount
    	System.out.println("   CurrCapacityHighCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "CurrCapacityHighCount"));
        // print HighestNumAvailable
    	System.out.println("   HighestNumAvailable=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "HighestNumAvailable"));
        // print LeakedConnectionCount
    	System.out.println("   LeakedConnectionCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "LeakedConnectionCount"));

        // print FailuresToReconnectCount
    	System.out.println("   FailuresToReconnectCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "FailuresToReconnectCount"));

        // print WaitSecondsHighCount
    	System.out.println("   WaitSecondsHighCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "WaitSecondsHighCount"));
        // print WaitingForConnectionCurrentCount
    	System.out.println("   WaitingForConnectionCurrentCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "WaitingForConnectionCurrentCount"));
        // print WaitingForConnectionFailureTotal
    	System.out.println("   WaitingForConnectionFailureTotal=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "WaitingForConnectionFailureTotal"));
        // print WaitingForConnectionTotal
    	System.out.println("   WaitingForConnectionTotal=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "WaitingForConnectionTotal"));
        // print WaitingForConnectionHighCount
    	System.out.println("   WaitingForConnectionHighCount=" + myJMXWrapper.getAttribute(jdbcRuntimeMBean, "WaitingForConnectionHighCount"));
    }
    
}
