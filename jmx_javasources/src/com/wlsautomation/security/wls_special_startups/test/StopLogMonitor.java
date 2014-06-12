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
package com.wlsautomation.security.wls_special_startups.test;


import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

public class StopLogMonitor
{
	
  // delete own admin user when server starts
  public static void main(String[] args)
  { 
	try {
			InitialContext ctx = new InitialContext();
			MBeanServer server = (MBeanServer) ctx.lookup("java:comp/jmx/runtime");
			ObjectName myDefaultAuthenticator = new ObjectName("Security:Name=myrealmDefaultAuthenticator");
			
			if ( (java.lang.Boolean)server.invoke(myDefaultAuthenticator,
                    "userExists",
                    new Object[]{"specialuser"},
                    new String[]{String.class.getName()}))
			{
				
				// remove user from group
				server.invoke(myDefaultAuthenticator,"removeMemberFromGroup",
						            new Object[]{"Administrators","specialuser"},
						            new String[]{String.class.getName(),String.class.getName()});

				// finally remove user
				server.invoke(myDefaultAuthenticator,"removeUser",
						new Object[]{"specialuser"},
						new String[]{String.class.getName()});
				
				System.out.println("LOG: Test log terminated !");
			}
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
  }
}
