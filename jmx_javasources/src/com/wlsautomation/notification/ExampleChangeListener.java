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

import javax.management.Notification;
import javax.management.AttributeChangeNotification;  
import weblogic.management.RemoteNotificationListener;


@SuppressWarnings("deprecation")
public class ExampleChangeListener implements RemoteNotificationListener{

	public void handleNotification(Notification newNotify, Object myObject)  
	{  
	    System.out.println("\n Callback handleNotification called");  
   
   	    if(newNotify instanceof AttributeChangeNotification)  {  
            AttributeChangeNotification myChange = (AttributeChangeNotification) newNotify;  

	        System.out.println("Attribute " + myChange.getAttributeName() + " has been modified from " + myChange.getOldValue() + " to " + myChange.getNewValue());  
	    }  
   	    else
   	    	System.out.println("Notification of type " + newNotify.getClass().getName() + "  but this is not yet supported !");
	}  
}




