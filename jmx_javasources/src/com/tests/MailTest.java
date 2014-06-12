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
import com.wlsautomation.creation_extension.MailConfiguration;
import javax.management.ObjectName;

public class MailTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		// createMailTest();
		deleteMailTest();
	}

	public static void createMailTest() throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		MailConfiguration myMailConfiguration = new MailConfiguration(myJMXWrapperRemote);
		
		java.util.Properties properties = new java.util.Properties();
		properties.put("mail.to","lector@wlsscriptbook.com");
		properties.put("mail.from","author@wlsscriptbook.com");
		properties.put("mail.transport.protocol","smtp");
		properties.put("mail.smtp.host","mail.wlsscriptbook.com");
		properties.put("mail.smtp.port","25");
		properties.put("mail.smtp.user","username");
		properties.put("mail.smtp.password","password");
		
		
		myMailConfiguration.createMailSession("MyNewTestMail_2", "mail/testMail_2", new ObjectName[]{}, properties);
		
		myJMXWrapperRemote.disconnectFromAdminServer(true);
	}

	public static void deleteMailTest() throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();
		myJMXWrapperRemote.connectToAdminServer(true,true,"weblogic", "<password>", "t3://testhost.wlsautomation.de:7001");
		
		MailConfiguration myMailConfiguration = new MailConfiguration(myJMXWrapperRemote);
		
		myMailConfiguration.deleteMailSession("MyNewTestMail");
		myMailConfiguration.deleteMailSession("MyNewTestMail_2");
		
		myJMXWrapperRemote.disconnectFromAdminServer(true);
	}

}
