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

import java.util.ArrayList;

import com.wlsautomation.utils.*;
import com.wlsautomation.creation_extension.ContextCaseData;
import com.wlsautomation.creation_extension.WorkManagerConfiguration;
import javax.management.ObjectName;

public class TestWorkManagerCreation {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		ObjectName[] targets = new ObjectName[]{new ObjectName("com.bea:Name=AdminServer,Type=Server")};
		
		WorkManagerConfiguration myWorkManagerConfiguration = new WorkManagerConfiguration(myJMXWrapperRemote);
		
        ObjectName minTest1 = myWorkManagerConfiguration.createMinThreadsConstraint("min_test1", 100, targets);
        ObjectName minTest2 = myWorkManagerConfiguration.createMinThreadsConstraint("min_test2", 200, targets);
        
        ObjectName max_testAll = myWorkManagerConfiguration.createMaxThreadsConstraint("max_all", 500, targets) ;

        ObjectName test_cap = myWorkManagerConfiguration.createCapacity("test_cap_1", 50, targets);

         
        /* ObjectName resTimeReq =*/  myWorkManagerConfiguration.createResponseTimeRequestClass("response_test1", 100, targets);
                
        ObjectName testFaiShare = myWorkManagerConfiguration.createFairShareRequestClass("fairShare", 50,targets);
        
        
        ArrayList<ContextCaseData> myContextList = new ArrayList<ContextCaseData>();
        myContextList.add(new ContextCaseData("Class_1","Group_1","User_1"));
        myContextList.add(new ContextCaseData("Class_2","Group_2","User_2"));
        myContextList.add(new ContextCaseData("Class_3","Group_3","User_3"));
        
        ObjectName testContext = myWorkManagerConfiguration.createContextRequestClass("test_reqClass", myContextList, targets);
        
        
        myWorkManagerConfiguration.createWorkManager("workManager_test_1",true,targets, minTest1, max_testAll, testFaiShare, null, null, test_cap);
        myWorkManagerConfiguration.createWorkManager("workManager_test_2",true,targets, minTest2, max_testAll, null, null, testContext, test_cap);

        
		myJMXWrapperRemote.disconnectFromAdminServer(true);
		
		System.out.println("Test finished !");
	}

}
