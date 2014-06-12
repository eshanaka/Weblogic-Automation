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
package com.wlsautomation.notification;

import javax.management.*;   
import java.util.*;  
import com.wlsautomation.utils.JMXWrapperRemote;

public class TestListener implements NotificationListener
{


   public void handleNotification(javax.management.Notification _new_servernotification, Object handback)  
   {  
      System.out.println("Notification received !");  

      if(_new_servernotification instanceof AttributeChangeNotification)  
      {  
         AttributeChangeNotification myNotification = (AttributeChangeNotification) _new_servernotification;  

         System.out.println("Change notification for MBean attribute " + myNotification.getAttributeName());
         System.out.println("    notification type: "+ myNotification.getAttributeType());
         System.out.println("    OLD value: "+ myNotification.getOldValue());
         System.out.println("    NEW value: "+ myNotification.getNewValue());
         System.out.println("    Notification message: " + myNotification.getMessage());  
         System.out.println("    Change time: " + myNotification.getTimeStamp());  
      }  

   }  




	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		JMXWrapperRemote myJMXWrapperRemote = new JMXWrapperRemote();

		myJMXWrapperRemote.connectToAdminServer(false,true,"weblogic", "...", "t3://localhost:7001");

		TestListener listener = new TestListener();  

		ArrayList<String> msServerNames = myJMXWrapperRemote.getManagedServerNames();
		
		 // iterator over server list and get server info
        for (int i=0; i<msServerNames.size();i++)
        {
        	try
        	{
        	  myJMXWrapperRemote.getConnection().addNotificationListener(myJMXWrapperRemote.getServerRuntime(msServerNames.get(i)), listener, null, null);
        	  System.out.println("Notification listener registered with managed-server: "+msServerNames.get(i));
        	}
        	catch(Exception ex)
        	{
        		System.out.println("Notification listener NOT registered with managed-server: "+msServerNames.get(i)+" due to problem: "+ex.getMessage());
        	}
        }

		System.out.println("Waiting for Notifications (Press Anykey to Exit). ........");  

		try {
		  System.in.read();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
}

