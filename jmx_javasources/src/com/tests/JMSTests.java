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

import com.wlsautomation.utils.*;
import com.wlsautomation.creation_extension.JMSConfiguration;

public class JMSTests {

	public static void main(String[] args) throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		JMSConfiguration myJMSManagement = new JMSConfiguration(myJMXWrapperRemote);
		
		myJMSManagement.createAnewJMSServer("MyTestServer_1","mytest_filestore_1.store","AdminServer");
		
		myJMSManagement.createAnewJMSServer("MyTestServer_2","mytest_filestore_2.store","AdminServer");
		myJMSManagement.createAnewJMSServer("MyTestServer_3","mytest_filestore_3.store","AdminServer");

		myJMSManagement.createJMSModule("testModule_1", "Server", "AdminServer");
		myJMSManagement.createJMSModule("testModule_2", "Server", "AdminServer");
		myJMSManagement.createJMSModule("testModule_3", "Server", "AdminServer");
		
	
		
		
		myJMSManagement.createJmsConnectionFactory("testModule_1", "connection_1_1__TasdfsddgaT2", "jms/connection_1_1__TasdsdfgaT2");
    	myJMSManagement.createJms_XA_ConnectionFactory("testModule_1", "connection_1_1__XxsddgsffdgxA", "jms/connection_1_1__XsfsdfxxsgsA");
		
		myJMSManagement.createJmsConnectionFactory("testModule_1", "connection_1_2", "jms/connection_1_2");
		myJMSManagement.createJmsConnectionFactory("testModule_1", "connection_1_3", "jms/connection_1_3");
		
		myJMSManagement.createJmsConnectionFactory("testModule_2", "connection_2_1", "jms/connection_2_1");
		myJMSManagement.createJmsConnectionFactory("testModule_2", "connection_2_2", "jms/connection_2_2");
		
		myJMSManagement.createJmsConnectionFactory("testModule_3", "connection_3_1", "jms/connection_3_1");
		myJMSManagement.createJmsConnectionFactory("testModule_3", "connection_3_2", "jms/connection_3_2");

		myJMSManagement.createJms_XA_ConnectionFactory("testModule_1", "connection_xa_1_1", "jms/XA_connection_1_1");

		myJMSManagement.createJMSSubDeployment("testModule_1","subDeployment_1", "MyTestServer_1");
	
		myJMSManagement.createJMSSubDeployment("testModule_2","subDeployment_2", "MyTestServer_2");
		myJMSManagement.createJMSSubDeployment("testModule_3","subDeployment_3", "MyTestServer_3");

		myJMSManagement.createQueue("testModule_1", "Queue_1a", "jms/Queue_1a", "subDeployment_1");
		myJMSManagement.createQueue("testModule_1", "Queue_1b", "jms/Queue_1b", "subDeployment_1");
		myJMSManagement.createQueue("testModule_1", "Queue_1c", "jms/Queue_1c", "subDeployment_1");

		myJMSManagement.createQueue("testModule_2", "Queue_2", "jms/Queue_2", "subDeployment_2");
		myJMSManagement.createQueue("testModule_3", "Queue_3", "jms/Queue_3", "subDeployment_3");

		myJMSManagement.createQueueWithErrorHandling("testModule_1", "Queue_ERR_1a", "jms/Queue_ERR_1a", "subDeployment_1", "Queue_1c");

		myJMSManagement.createTopic("testModule_1", "Topic_1a", "jms/Topic_1a", "subDeployment_1");


		myJMXWrapperRemote.disconnectFromAdminServer(true);
	}
}
