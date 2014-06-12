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

public class ServerAndProcessMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public ServerAndProcessMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    public void getServerSUMMARYInformation(String serverName) throws Exception
    {
        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);

        // print Name
        System.out.println("Server - Name="+ serverName);
        // print state
        System.out.println("Server - State="+myJMXWrapper.getAttribute(serverRuntime,"State"));
        // HealthState
        System.out.println("Server - HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(serverRuntime, "HealthState")));

        ObjectName jvmRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JVMRuntime");
        // print HeapFreeCurrent
        System.out.println("JVM - HeapFreeCurrent="+myJMXWrapper.getAttribute(jvmRuntime,"HeapFreeCurrent"));

        ObjectName jtaRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JTARuntime");
        // HealthState
        System.out.println("JTA - HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(jtaRuntime, "HealthState")));
        // print TransactionTotalCount
        System.out.println("JTA - TransactionTotalCount="+myJMXWrapper.getAttribute(jtaRuntime,"TransactionTotalCount"));


        ObjectName threadPoolRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "ThreadPoolRuntime");
        // HealthState
        System.out.println("Threadpool - HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(threadPoolRuntime, "HealthState")));
        // print CompletedRequestCount
        System.out.println("Threadpool - CompletedRequestCount="+myJMXWrapper.getAttribute(threadPoolRuntime,"CompletedRequestCount"));
    }




    public HashMap<String, Object> getServerBasicInformation(String serverName) throws Exception
    {
        HashMap<String, Object> myResultList = new HashMap<String, Object>();

        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);

        // print Name
        System.out.println("Server - Name="+ serverName);
        // print state
        System.out.println("   State=" + myJMXWrapper.getAttribute(serverRuntime, "State"));
        // SocketsOpenedTotalCount
        System.out.println("   SocketsOpenedTotalCount=" + myJMXWrapper.getAttribute(serverRuntime, "SocketsOpenedTotalCount"));
        // OpenSocketsCurrentCount
        System.out.println("   OpenSocketsCurrentCount=" + myJMXWrapper.getAttribute(serverRuntime, "OpenSocketsCurrentCount"));
        // AdminServer  - note this is BOOLEAN and indicates if this server is the admin server
        System.out.println("   AdminServer=" + myJMXWrapper.getAttribute(serverRuntime, "AdminServer"));
        // HealthState
        System.out.println("   HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(serverRuntime,"HealthState")));

        return myResultList;
    }

    
    public HashMap<String, Object> getServerJVMRuntimeInformation(String serverName) throws Exception
    {
        HashMap<String, Object> myResultList = new HashMap<String, Object>();

        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);
        ObjectName jvmRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JVMRuntime");

        // print JavaVendor
        System.out.println("   JavaVendor=" + myJMXWrapper.getAttribute(jvmRuntime, "JavaVendor"));
        // print JavaVersion
        System.out.println("   JavaVersion=" + myJMXWrapper.getAttribute(jvmRuntime, "JavaVersion"));
        // print HeapFreeCurrent
        System.out.println("   HeapFreeCurrent=" + myJMXWrapper.getAttribute(jvmRuntime, "HeapFreeCurrent"));
        // print HeapFreePercent
        System.out.println("   HeapFreePercent=" + myJMXWrapper.getAttribute(jvmRuntime, "HeapFreePercent"));
        // print HeapSizeCurrent
        System.out.println("   HeapSizeCurrent=" + myJMXWrapper.getAttribute(jvmRuntime, "HeapSizeCurrent"));
        // print Uptime
        System.out.println("   Uptime=" + myJMXWrapper.getAttribute(jvmRuntime, "Uptime"));

        return myResultList;
    }
    
    
    
}
