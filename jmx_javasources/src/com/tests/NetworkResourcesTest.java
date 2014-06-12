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

import com.wlsautomation.creation_extension.NetworkResourceConfiguration;
import com.wlsautomation.utils.JMXWrapperRemote;


public class NetworkResourcesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		//createTest();
		
		deleteTest();
	}

	public static void createTest() throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		NetworkResourceConfiguration myNetworkResourceConfiguration = new NetworkResourceConfiguration(myJMXWrapperRemote);

		
		// http channel
		myNetworkResourceConfiguration.createHTTPNetworkChannel("Martin_VirtualHostTest_Domain_MS1", "MS1_VHChannel1", 7005);
		myNetworkResourceConfiguration.createHTTPNetworkChannel("Martin_VirtualHostTest_Domain_MS1", "MS1_VHChannel2", 7006);
		                                                         
		
		// create virtual host
		myNetworkResourceConfiguration.createVirtualHost("VirtualTestHost_1", "MS1_VHChannel1", new String[]{"Martin_VirtualHostTest_Domain_MS1"}, new String[]{"localhost"});
		myNetworkResourceConfiguration.createVirtualHost("VirtualTestHost_2", "MS1_VHChannel2", new String[]{"Martin_VirtualHostTest_Domain_MS1"}, new String[]{"localhost"});
		
		myJMXWrapperRemote.disconnectFromAdminServer(true);
	}


	public static void deleteTest() throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		NetworkResourceConfiguration myNetworkResourceConfiguration = new NetworkResourceConfiguration(myJMXWrapperRemote);

		// delete http channel
		myNetworkResourceConfiguration.deleteNetworkChannel("Martin_VirtualHostTest_Domain_MS1", "MS1_VHChannel2");
		
		// delete virtual host
		myNetworkResourceConfiguration.deleteVirtualHost("VirtualTestHost_2");
		
		
		myJMXWrapperRemote.disconnectFromAdminServer(true);
	}

}
