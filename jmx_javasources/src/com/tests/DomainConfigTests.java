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
package com.tests;

import com.wlsautomation.creation_extension.DomainConfiguration;
import com.wlsautomation.creation_extension.JDBCConfiguration;
import com.wlsautomation.creation_extension.ServerAndMachineConfiguration;
import com.wlsautomation.utils.JMXWrapperRemote;

public class DomainConfigTests {

	   public static void configureDomain(JMXWrapperRemote myJMXWrapper) throws Exception
	    {
		   
		    DomainConfiguration myDomainConfiguration = new DomainConfiguration(myJMXWrapper);
		   
		    myDomainConfiguration.configureDomainJTASetting();
		    myDomainConfiguration.configureDomainLog();
		    myDomainConfiguration.configureServerLog("AdminServer");
		    myDomainConfiguration.createCluster("TestCluster_1");
		    myDomainConfiguration.createCluster("TestCluster_99");
	        
	        
	        ServerAndMachineConfiguration myServerAndMachineConfiguration = new ServerAndMachineConfiguration(myJMXWrapper);
	        
	        myServerAndMachineConfiguration.createUnixMachine("MyTestMachine");
	        myServerAndMachineConfiguration.createUnixMachine("MyTestMachine_2");
	        myServerAndMachineConfiguration.createManagedServer("MyTestServer","TestCluster_99","MyTestMachine");
	        myServerAndMachineConfiguration.createManagedServer("MyTestServer_2","TestCluster_99","MyTestMachine_2");
	        
	        
	        
	        JDBCConfiguration myJDBCConfiguration = new JDBCConfiguration(myJMXWrapper);
	        
	        myJDBCConfiguration.createDataSource("MyNormal_DS","TestCluster_99","jdbc/testDSJMX","OnePhaseCommit",
	                         "oracle.jdbc.OracleDriver","jdbc:oracle:thin:@monitoringbook.com:1521:TestDB",
	                         "scott","tiger",false,null);
	        myJDBCConfiguration.createDataSource("MyWallet_DS","TestCluster_99","jdbc/testDSWalletJMX","OnePhaseCommit",
	                         "oracle.jdbc.OracleDriver","jdbc:oracle:thin:/@monitoringbook.com:1521:TestWalletDB",
	                         null,null,true,"/applications/wallets/TestWalletDB");
	    }


	    public static void main(String[] args) throws Exception
	    {
	    	JMXWrapperRemote myJMXWrapper = new JMXWrapperRemote();

	        myJMXWrapper.connectToAdminServer(true, // edit
	        		                 true,  // domain
	                                 "weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
	        configureDomain(myJMXWrapper);



	        myJMXWrapper.disconnectFromAdminServer(true);
	    }
}
