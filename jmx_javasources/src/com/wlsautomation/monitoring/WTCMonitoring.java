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

import javax.management.*;
import weblogic.wtc.gwt.DSessConnInfo;
import weblogic.wtc.gwt.DServiceInfo;
import weblogic.wtc.gwt.WTCServiceStatus;


import com.wlsautomation.utils.*;

public class WTCMonitoring {
    JMXWrapper myJMXWrapper = null;
    WLSMonitoringUtils myWLSMonitoringUtils = null;

    public WTCMonitoring(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
        myWLSMonitoringUtils = new WLSMonitoringUtils(myJMXWrapper);
    }

    
/**
 * --> com.bea:Name=WTCService,ServerRuntime=MS1,Type=WTCRuntime
         Attribute: ServiceStatus   of Type : [Lweblogic.wtc.gwt.DServiceInfo;
         Attribute: Name   of Type : java.lang.String
         Operation: java.lang.Integer  getServiceStatus(localAccessPoint:java.lang.String  svcName:java.lang.String  isImport:java.lang.Boolean  )
         Operation: [Lweblogic.wtc.gwt.DSessConnInfo;  listConnectionsConfigured()
         Operation: java.lang.Integer  getServiceStatus(java.lang.String:java.lang.String  java.lang.String:java.lang.String  java.lang.String:java.lang.String  )
         Operation: java.lang.Integer  getServiceStatus(svcName:java.lang.String  isImport:java.lang.Boolean  )
         Operation: java.lang.Integer  getServiceStatus(localAccessPoint:java.lang.String  svcName:java.lang.String  )
         Operation: java.lang.Integer  getServiceStatus(svcName:java.lang.String  )
         Operation: java.lang.Void  startConnection(LDomAccessPointId:java.lang.String  RDomAccessPointId:java.lang.String  )    
 */
    
    public void monitorWTC() throws Exception
    {
    	// get main values
    	Hashtable<String,String> mainValues = myJMXWrapper.getMainServerDomainValues();
    	
    	// go to MBean
        ObjectName serverRuntime = myJMXWrapper.getServerRuntime(mainValues.get("serverName"));
        
        ObjectName wtcRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "WTCRuntime");
        
        // list connections configured
        DSessConnInfo[] myWTCConnectionInfos = (DSessConnInfo[])myJMXWrapper.invoke(wtcRuntime,"listConnectionsConfigured",
				new Object[]{},	new String[]{});
        
        System.out.println("DSessConnInfo:");
        for (int i=0;i<myWTCConnectionInfos.length;i++)
        {
        	DSessConnInfo nextDSessConnInfo = myWTCConnectionInfos[i];
        	System.out.println("DSessConnInfo-"+i);
        	System.out.println("    "+nextDSessConnInfo.getLocalAccessPointId());
        	System.out.println("    "+nextDSessConnInfo.getRemoteAccessPointId());
        	System.out.println("    "+nextDSessConnInfo.getConnected());
        	//System.out.println("    "+nextDSessConnInfo.isConnected());
        }

        
        DServiceInfo[] allDServiceInfos = (DServiceInfo[]) myJMXWrapper.getAttribute(wtcRuntime, "ServiceStatus");
        
        System.out.println("\n\nDServiceInfos:");
        for (int i=0;i<allDServiceInfos.length;i++)
        {
        	System.out.println("DSessConnInfo-"+i);
        	System.out.println("    "+allDServiceInfos[i].getLocalAccessPoint());
        	System.out.println("    "+allDServiceInfos[i].getServiceName());
        	System.out.println("    "+WTCServiceStatus.svcTypeToString(allDServiceInfos[i].getServiceType()));
        	System.out.println("    "+WTCServiceStatus.statusToString(allDServiceInfos[i].getStatus()));
        }        
    }
}