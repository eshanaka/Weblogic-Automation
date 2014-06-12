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
package com.wlsautomation.administration;

import com.wlsautomation.utils.*;


import javax.management.ObjectName;



public class JMSAdministration {

    private JMXWrapper myJMXWrapper = null;

    public JMSAdministration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

 

    // ********************* GEneral ! **********************************************************
    public ObjectName getJMSServerRuntime(String jmsServerName, String wlsServerName) throws WLSAutomationException
	{
		try
		{    
			 // get the runtime of the server
			 ObjectName serverRuntime = myJMXWrapper.getServerRuntime(wlsServerName);
			 
			 // the the jms runtime
			 ObjectName jmsRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");
			 
			 if (jmsRuntime!=null)
			 {
				 ObjectName[] jmsServerRuntimes = (ObjectName[]) myJMXWrapper.getAttribute(jmsRuntime, "JMSServers");
                 
                 for (int i=0;i<jmsServerRuntimes.length;i++) 
                 	 if (jmsServerName.equals((String)myJMXWrapper.getAttribute(jmsServerRuntimes[i], "Name")))
                		 return jmsServerRuntimes[i];
             
                 throw new WLSAutomationException("JMSServer "+jmsServerName+" not found on server "+wlsServerName+" ! ");
			 }
			 else
				 throw new WLSAutomationException("No JMSRuntime found on server "+wlsServerName+" ! ");
			 
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in getJMSServerRuntime : "+ ex.getMessage());
		}
	}    
    
    
    public ObjectName getJMSDestinationRuntime(String destinationName, String jmsServerName, String wlsServerName) throws WLSAutomationException
	{
		try
		{    
			 // get the runtime of the server
			 ObjectName serverRuntime = myJMXWrapper.getServerRuntime(wlsServerName);
			 
			 // the the jms runtime
			 ObjectName jmsRuntime = (ObjectName) myJMXWrapper.getAttribute(serverRuntime, "JMSRuntime");
			 
			 if (jmsRuntime!=null)
			 {
				 ObjectName[] jmsServerRuntimes = (ObjectName[]) myJMXWrapper.getAttribute(jmsRuntime, "JMSServers");
                 
                 for (int i=0;i<jmsServerRuntimes.length;i++) 
                 	 if (jmsServerName.equals((String)myJMXWrapper.getAttribute(jmsServerRuntimes[i], "Name")))
                 	 {
                 		 // found jms server
                 		ObjectName[] destinationRuntimes = (ObjectName[]) myJMXWrapper.getAttribute(jmsServerRuntimes[i], "Destinations");
                        for (int d=0;d<destinationRuntimes.length;d++) 
                        	if (destinationName.equals((String)myJMXWrapper.getAttribute(destinationRuntimes[d], "Name")))
                        		return destinationRuntimes[d];
                 	 }
             
                 throw new WLSAutomationException("JMSServer "+jmsServerName+" or destination "+destinationName+" not found on server "+wlsServerName+" ! ");
			 }
			 else
				 throw new WLSAutomationException("No JMSRuntime found on server "+wlsServerName+" ! ");
			 
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in getJMSDestinationRuntime : "+ ex.getMessage());
		}
	}    
    
    
    // *********************  JMS Server ********************************************************
    
	public void jmsServerPauseProduction(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try {    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isProductionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "ProductionPaused");
				 
				 // if already in paused - no action required
				 if (! isProductionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"pauseProduction",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerPauseProduction : "+ ex.getMessage());
		}
	}    
    
	public void jmsServerResumeProduction(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isProductionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "ProductionPaused");
				 
				 // if not in paused - no action required
				 if (isProductionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"resumeProduction",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerResumeProduction : "+ ex.getMessage());
		}
	}    
	
           
	public void jmsServerPauseInsertion(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isInsertionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "InsertionPaused");
				 
				 // if already in paused - no action required
				 if (! isInsertionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"pauseInsertion",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerPauseInsertion : "+ ex.getMessage());
		}
	}    
    
	public void jmsServerResumeInsertion(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isInsertionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "InsertionPaused");
				 
				 // if not in paused - no action required
				 if (isInsertionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"resumeInsertion",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerResumeInsertion : "+ ex.getMessage());
		}
	}    
           
	
	public void jmsServerPauseConsumption(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isConsumptionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "ConsumptionPaused");
				 
				 // if alread in paused - no action required
				 if (! isConsumptionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"pauseConsumption",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerPauseConsumption : "+ ex.getMessage());
		}
	}    
    
	public void jmsServerResumeConsumption(ObjectName jmsServerRuntime) throws WLSAutomationException
	{
		try {    
			 if (jmsServerRuntime!=null)
			 {
				 Boolean isConsumptionPaused = (Boolean) myJMXWrapper.getAttribute(jmsServerRuntime, "ConsumptionPaused");
				 
				 // if not in paused - no action required
				 if (isConsumptionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsServerRuntime,"resumeConsumption",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsServerResumeConsumption : "+ ex.getMessage());
		}
	}    	
	
	
    //  ***********************   Destination !!  ****************************************************	

	public void jmsDestinationPauseProduction(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try {
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isProductionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "ProductionPaused");
				 
				 // if already in paused - no action required
				 if (! isProductionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"pauseProduction",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationPauseProduction : "+ ex.getMessage());
		}
	}    
    
	public void jmsDestinationResumeProduction(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isProductionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "ProductionPaused");
				 
				 // if not in paused - no action required
				 if (isProductionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"resumeProduction",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationResumeProduction : "+ ex.getMessage());
		}
	}    
	
           
	public void jmsDestinationPauseInsertion(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isInsertionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "InsertionPaused");
				 
				 // if already in paused - no action required
				 if (! isInsertionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"pauseInsertion",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationPauseInsertion : "+ ex.getMessage());
		}
	}    
    
	public void jmsDestinationResumeInsertion(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isInsertionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "InsertionPaused");
				 
				 // if not in paused - no action required
				 if (isInsertionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"resumeInsertion",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationResumeInsertion : "+ ex.getMessage());
		}
	}    
           
	
	public void jmsDestinationPauseConsumption(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try
		{    
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isConsumptionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "ConsumptionPaused");
				 
				 // if alread in paused - no action required
				 if (! isConsumptionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"pauseConsumption",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationPauseConsumption : "+ ex.getMessage());
		}
	}    
    
	public void jmsDestinationResumeConsumption(ObjectName jmsDestinationRuntime) throws WLSAutomationException
	{
		try {    
			 if (jmsDestinationRuntime!=null)
			 {
				 Boolean isConsumptionPaused = (Boolean) myJMXWrapper.getAttribute(jmsDestinationRuntime, "ConsumptionPaused");
				 
				 // if not in paused - no action required
				 if (isConsumptionPaused.booleanValue())
				     myJMXWrapper.invoke(jmsDestinationRuntime,"resumeConsumption",new Object[]{},new String[]{});
			 }
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationResumeConsumption : "+ ex.getMessage());
		}
	}    	
 
       
	// *********************************     Other actions ************************************************
	
	// selector = "" means all messages, otherwise this must be a valid message selector
	public String deleteMessagesFromJmsDestination(ObjectName jmsDestinationRuntime, String selector) throws WLSAutomationException
	{
		try
		{    
			 if (jmsDestinationRuntime!=null)
			 {

				 Object result = myJMXWrapper.invoke(jmsDestinationRuntime,
                         "deleteMessages", new Object[] {selector}, new String[] {"java.lang.String"});
				 
				 // return result
				 return (result!=null) ? result.toString() : null;
			 }
			 else
				 throw new WLSAutomationException("Undefined destination runtime (null) not accepted as parameter !");
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in jmsDestinationPauseConsumption : "+ ex.getMessage());
		}
	}    

	
	

	// This example defines a function which will browse through all messges of a queue:
	// messageState must be null to get all messages
	public void printMessagesFromJmsDestination(ObjectName jmsDestinationRuntime, String selector, Integer messageState) throws WLSAutomationException
	{
		try
		{    if (selector==null) selector="true";
		
			 if (jmsDestinationRuntime!=null)
			 {

				 // create a cursor
				 String myMessageCursor = null;
				 if (messageState==null)
					 myMessageCursor = (String)myJMXWrapper.invoke(jmsDestinationRuntime,
	                         "getMessages",
	                         new Object[] {selector, new Integer(9999999)}, new String[] {String.class.getName(), Integer.class.getName()});
				 else // state provided
					 myMessageCursor = (String)myJMXWrapper.invoke(jmsDestinationRuntime,
	                         "getMessages",
	                         new Object[] {selector, new Integer(9999999), messageState}, 
	                         new String[] {String.class.getName(), Integer.class.getName(), Integer.class.getName()});
				 
				 // how many messages are available in the destination (queue or topic)
				 Long totalAmountOfMessages=(Long)myJMXWrapper.invoke(jmsDestinationRuntime,
                         "getCursorSize",new Object[] {myMessageCursor},new String[] {String.class.getName()});
				 
				 // get all messages from the cursor starting from the beginning
		         // Operation: [Ljavax.management.openmbean.CompositeData;  getItems(cursorHandle:java.lang.String  start:java.lang.Long  count:java.lang.Integer  )
				 javax.management.openmbean.CompositeData[] allDestinationMessages=
						 (javax.management.openmbean.CompositeData[])myJMXWrapper.invoke(jmsDestinationRuntime,
                         "getItems",
                         new Object[] {myMessageCursor, new Integer(1), totalAmountOfMessages}, 
                         new String[] {String.class.getName(), Integer.class.getName(), Long.class.getName()});
								 
				 // print all the messages' contents
				 System.out.println(allDestinationMessages);
			 }
			 else
				 throw new WLSAutomationException("Undefined destination runtime (null) not accepted as parameter !");
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printMessagesFromJmsDestination : "+ ex.getMessage());
		}
	}    
	

	// this example will query exactly ONE message from the queue and print it
	public void printOneMessageFromJmsDestination(ObjectName jmsDestinationRuntime, String messageID) throws WLSAutomationException
	{
		try
		{    if (jmsDestinationRuntime!=null)
			 {
			    // get the message with the provided message ID
			    // ID should look similiar to 'ID:<1234.1234567890>'
			javax.management.openmbean.CompositeData myMessage = 
					(javax.management.openmbean.CompositeData) myJMXWrapper.invoke(jmsDestinationRuntime,
	                         "getMessage",
	                         new Object[] {messageID}, new String[] {String.class.getName()});
			if (myMessage != null)					 
				 // print the message contents
				 System.out.println(myMessage);
			 }
			 else
				 throw new WLSAutomationException("Undefined destination runtime (null) not accepted as parameter !");
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in printOneMessageFromJmsDestination : "+ ex.getMessage());
		}
	}	
    
}
	
	