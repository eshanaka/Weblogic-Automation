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
package com.wlsautomation.security.wls_special_startups.specialtest;

import java.io.FileInputStream;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

public class StartSpecialTest {
	
	  public static void main(String[] args)
	  {
		try {
				MBeanServer server = (MBeanServer) (new InitialContext()).lookup("java:comp/jmx/runtime");
				ObjectName service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
	            ObjectName domainMBean =(ObjectName) server.getAttribute(service,"DomainConfiguration");
	            String serverName = (String) server.getAttribute(service,"ServerName");
	            String domainRoot = (String) server.getAttribute(domainMBean,"RootDirectory");
				
				String bootIdentityFile = System.getProperty("weblogic.system.BootIdentityFile");
				
				if (bootIdentityFile==null)  // create filename from mbean information
		            bootIdentityFile = domainRoot + "/servers/"+serverName+"/security/boot.properties";
				
				// load boot properties file
				Properties bootProperties = new Properties();
				bootProperties.load(new FileInputStream(bootIdentityFile));

				ClearOrEncryptedService ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(domainRoot));

				// decrypt user
				System.out.println("SPECIAL: User="+ces.decrypt(bootProperties.getProperty("username")));
				
				// decrypt password
				System.out.println("SPECIAL: PW  ="+ces.decrypt(bootProperties.getProperty("password")));
		}    
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	  }

}


