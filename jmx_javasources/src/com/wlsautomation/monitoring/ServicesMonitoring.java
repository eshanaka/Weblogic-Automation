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

public class ServicesMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public ServicesMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    public void printServerJTAInformation(String serverName) throws Exception
    {
        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);
        ObjectName jtaRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JTARuntime");

        // HealthState
        System.out.println("   HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(jtaRuntime,"HealthState")));
        // print TransactionTotalCount
        System.out.println("   TransactionTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionTotalCount"));
        // print TransactionCommittedTotalCount
        System.out.println("   TransactionCommittedTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionCommittedTotalCount"));
        // print ActiveTransactionsTotalCount
        System.out.println("   ActiveTransactionsTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "ActiveTransactionsTotalCount"));
        // print TransactionRolledBackTotalCount
        System.out.println("   TransactionRolledBackTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionRolledBackTotalCount"));
        // print TransactionRolledBackTimeoutTotalCount
        System.out.println("   TransactionRolledBackTimeoutTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionRolledBackTimeoutTotalCount"));
        // print TransactionRolledBackResourceTotalCount
        System.out.println("   TransactionRolledBackResourceTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionRolledBackResourceTotalCount"));
        // print TransactionAbandonedTotalCount
        System.out.println("   TransactionAbandonedTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionAbandonedTotalCount"));
        // print TransactionHeuristicsTotalCount
        System.out.println("   TransactionHeuristicsTotalCount=" + myJMXWrapper.getAttribute(jtaRuntime, "TransactionHeuristicsTotalCount"));
    }


    public void printServerThreadPoolInformation(String serverName) throws Exception
    {
        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);
        ObjectName threadPoolRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "ThreadPoolRuntime");

        // print CompletedRequestCount
        System.out.println("   CompletedRequestCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "CompletedRequestCount"));
        // print ExecuteThreadTotalCount
        System.out.println("   ExecuteThreadTotalCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "ExecuteThreadTotalCount"));
        // print ExecuteThreadIdleCount
        System.out.println("   ExecuteThreadIdleCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "ExecuteThreadIdleCount"));
        // HealthState
        System.out.println("   HealthState="+myWLSMonitoringUtils.getHealthStateInformation((weblogic.health.HealthState)myJMXWrapper.getAttribute(threadPoolRuntime,"HealthState")));
        // print HoggingThreadCount
        System.out.println("   HoggingThreadCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "HoggingThreadCount"));
        // print PendingUserRequestCount
        System.out.println("   PendingUserRequestCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "PendingUserRequestCount"));
        // print QueueLength
        System.out.println("   QueueLength=" + myJMXWrapper.getAttribute(threadPoolRuntime, "QueueLength"));
        // print SharedCapacityForWorkManagers
        System.out.println("   SharedCapacityForWorkManagers=" + myJMXWrapper.getAttribute(threadPoolRuntime, "SharedCapacityForWorkManagers"));
        // print StandbyThreadCount
        System.out.println("   StandbyThreadCount=" + myJMXWrapper.getAttribute(threadPoolRuntime, "StandbyThreadCount"));
        // print Suspended
        System.out.println("   Suspended=" + myJMXWrapper.getAttribute(threadPoolRuntime, "Suspended"));
        // print Throughput
        System.out.println("   Throughput=" + myJMXWrapper.getAttribute(threadPoolRuntime, "Throughput"));
    }
}