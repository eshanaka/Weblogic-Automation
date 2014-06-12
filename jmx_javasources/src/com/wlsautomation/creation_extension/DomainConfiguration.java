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
package com.wlsautomation.creation_extension;

import com.wlsautomation.utils.*;
import java.util.*;
import javax.management.ObjectName;
import javax.management.Attribute;


public class DomainConfiguration
{
    private JMXWrapper myJMXWrapper;

    private Hashtable<String , String> domainInfo;
    private String domainName = "";

    public DomainConfiguration(JMXWrapper _myJMXWrapper) throws Exception
    {
        myJMXWrapper = _myJMXWrapper;

        // get domainInfo
        domainInfo = myJMXWrapper.getMainServerDomainValues();
        domainName = domainInfo.get("domainName");
    }

    /**
     * Configure the attribute and rollover definition of the domain log file
     * @throws Exception
     */
    public void configureDomainLog() throws Exception
    {
        // e.g.: com.bea:Name=TestDomain,Type=Log
        ObjectName myLogMBean = new ObjectName("com.bea:Name="+domainName+",Type=Log");

        // set attribute RotationType
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("RotationType",new String("byTime")));

        // set attribute RotationTime
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("RotationTime",new String("02:00")));

        // set attribute FileTimeSpan
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileTimeSpan",new Integer(24)));

        // set attribute NumberOfFilesLimited
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("NumberOfFilesLimited",new Boolean(true)));

        // set attribute FileCount
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileCount",new Integer(14)));  // 2 weeks

        // set attribute FileName
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileName","/applications/log/"+domainName+"domainLog.log"));
    }


    /**
     * Configure the domain wide JTA attributes
     * @throws Exception
     */
    public void configureDomainJTASetting() throws Exception
    {
        // e.g.: com.bea:Name=TestDomain,Type=JTA
        ObjectName myJTAMBean = new ObjectName("com.bea:Name="+domainName+",Type=JTA");

        // set attribute TimeoutSeconds
        myJMXWrapper.setAttribute(myJTAMBean,new Attribute("TimeoutSeconds",new Integer(300)));
    }


    /**
     * Configure the attribute and rollover definition of a server log file
     * @throws Exception
     */
    public void configureServerLog(String servername) throws Exception
    {
        // e.g.: com.bea:Name=AdminServer,Server=AdminServer,Type=Log
        ObjectName myLogMBean = new ObjectName("com.bea:Name="+servername+",Server="+servername+",Type=Log");

        // e.g.: com.bea:Name=AdminServer,Server=AdminServer,Type=WebServerLog,WebServer=AdminServer
        ObjectName myWebserverLogMBean = new ObjectName("com.bea:Name="+servername+",Server="+servername+",Type=WebServerLog,WebServer="+servername+"");

        // set attribute RotationType
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("RotationType",new String("byTime")));

        // set attribute RotationTime
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("RotationTime",new String("02:00")));

        // set attribute FileTimeSpan
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileTimeSpan",new Integer(24)));

        // set attribute NumberOfFilesLimited
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("NumberOfFilesLimited",new Boolean(true)));

        // set attribute FileCount
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileCount",new Integer(14)));  // 2 weeks

        // set attribute FileName
        myJMXWrapper.setAttribute(myLogMBean,new Attribute("FileName","/applications/log/"+servername+"/"+servername+".log"));

        // now do the same for the access log of the webserver

        // set attribute RotationType
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("RotationType",new String("byTime")));

        // set attribute RotationTime
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("RotationTime",new String("02:00")));

        // set attribute FileTimeSpan
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("FileTimeSpan",new Integer(24)));

        // set attribute NumberOfFilesLimited
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("NumberOfFilesLimited",new Boolean(true)));

        // set attribute FileCount
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("FileCount",new Integer(14)));  // 2 weeks

        // set attribute FileName
        myJMXWrapper.setAttribute(myWebserverLogMBean,new Attribute("FileName","/applications/log/"+servername+"/access.log"));
    }


    /**
     * Configure a new cluster
     * @throws Exception
     */
    public void createCluster(String clustername) throws Exception
    {
        // e.g.: com.bea:Name=TestDomain,Type=Domain
        ObjectName myDomainMBean = new ObjectName("com.bea:Name=" + domainName +",Type=Domain");

        // javax.management.ObjectName  createCluster(name:java.lang.String  )
        ObjectName myNewCluster = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
                                                                  "createCluster",
                                                                  new Object[]{new String(clustername)},
                                                                  new String[]{String.class.getName()});

        // set attribute ClusterMessagingMode
        myJMXWrapper.setAttribute(myNewCluster,new Attribute("ClusterMessagingMode",new String("unicast")));

        // set attribute FrontendHTTPSPort
        myJMXWrapper.setAttribute(myNewCluster,new Attribute("FrontendHTTPSPort",new Integer(0)));

        // set attribute FrontendHTTPPort
        myJMXWrapper.setAttribute(myNewCluster,new Attribute("FrontendHTTPPort",new Integer(0)));

        // set attribute WeblogicPluginEnabled
        myJMXWrapper.setAttribute(myNewCluster,new Attribute("WeblogicPluginEnabled",new Boolean("false")));
    }

 
}
