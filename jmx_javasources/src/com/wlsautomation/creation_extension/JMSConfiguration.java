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
package com.wlsautomation.creation_extension;

import com.wlsautomation.utils.*;

import javax.management.ObjectName;
import javax.management.Attribute;


public class JMSConfiguration {
	
    private JMXWrapper myJMXWrapper = null;

    public JMSConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }
	
	
	
	// ####################  CREATE ####################################################


	//  create s filestore with a provided name
    // must be called on EDIT mbeanserver
	public ObjectName createFileStore(String fileStoreName, String wlsServerName) throws WLSAutomationException
	{
		try
		{    
			 // get the domain config root
			 ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();
			 
			 // create filesstore and configure it
			 ObjectName newFileStore = (ObjectName)myJMXWrapper.invoke(domainRoot,"createFileStore",new Object[]{fileStoreName},new String[]{String.class.getName()});
			 myJMXWrapper.setAttribute(newFileStore, new Attribute("Directory", "/domains/MartinTest_Domain"));

			 // define targets
			 myJMXWrapper.setAttribute(newFileStore, new Attribute("Targets", new ObjectName[]{new ObjectName("com.bea:Name="+wlsServerName+",Type=Server")}));
			 
			 return newFileStore;
		}
		catch(Exception ex)
		{
			System.out.println("Error while createFileStore ("+fileStoreName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	

	// create a JMS server
	// defines a JMS server and target it
    // must be called on EDIT mbeanserver
	public void createAnewJMSServer(String jmsServerName, String fileStoreName, String wlsServerName) throws WLSAutomationException
	{
		try
		{
			// 1st create the filestore:
			ObjectName newFileStore = createFileStore(fileStoreName, wlsServerName);
		    
		    // get the domain config root
		    ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();
		    
		    // now create the server
			ObjectName newJMSServer = (ObjectName)myJMXWrapper.invoke(domainRoot,"createJMSServer",new Object[]{jmsServerName},new String[]{String.class.getName()});

			// set the persistent store
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("PersistentStore", newFileStore));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("Targets", new ObjectName[]{new ObjectName("com.bea:Name="+wlsServerName+",Type=Server")}));
		       
		    // now we can define threshold values
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("BytesThresholdHigh", new Long(-1)));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("BytesThresholdLow", new Long(-1)));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("MessagesThresholdHigh", new Long(-1)));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("MessagesThresholdLow", new Long(-1)));

		    // now we can define quotas values
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("BytesMaximum", new Long(-1)));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("MessagesMaximum", new Long(-1)));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("BlockingSendPolicy", "FIFO"));
			myJMXWrapper.setAttribute(newJMSServer, new Attribute("MaximumMessageSize", new Integer(10000000)));

			// now configure the log file
			// similiar to com.bea:Name=MyTestServer_1,Type=JMSMessageLogFile,JMSServer=MyTestServer_1
			ObjectName newJMSServerLogFile = (ObjectName)myJMXWrapper.getAttribute(newJMSServer,"JMSMessageLogFile");

			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("RotationType", new String("byTime")));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("RotateLogOnStartup", new Boolean(false)));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("RotationTime", new String("00:00")));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("FileTimeSpan", new Integer(24)));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("FileCount", new Integer(25)));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("NumberOfFilesLimited", new Boolean(true)));
			myJMXWrapper.setAttribute(newJMSServerLogFile, new Attribute("FileName", new String(jmsServerName+".log")));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createAnewJMSServer ("+jmsServerName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	    	       
	// creating a JMS module
	public void createJMSModule(String jmsModuleName, String targetType, String targetName)  throws WLSAutomationException
	{
		try
		{    
			 // get the domain config root
			 ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();

			 // now create the server
			 ObjectName newJMSModule = (ObjectName)myJMXWrapper.invoke(domainRoot,"createJMSSystemResource",new Object[]{jmsModuleName},new String[]{String.class.getName()});

			 // set the target
			 myJMXWrapper.setAttribute(newJMSModule, new Attribute("Targets", new ObjectName[]{new ObjectName("com.bea:Name="+targetName+",Type="+targetType)}));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createJMSModule ("+jmsModuleName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	// create a connection factory
	public void createJmsConnectionFactory(String jmsModuleName, String connectionFactoryName, String jmsJNDIname)  throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");
			 
			 // create the connection factory
			 ObjectName myJMSModuleConnfactory = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"createConnectionFactory",new Object[]{connectionFactoryName},new String[]{String.class.getName()});
			 
			 // configure connection factory
			 myJMXWrapper.setAttribute(myJMSModuleConnfactory, new Attribute("JNDIName", new String(jmsJNDIname)));
			 myJMXWrapper.setAttribute(myJMSModuleConnfactory, new Attribute("DefaultTargetingEnabled", new Boolean(true)));
			    
			 // get the security params
			 ObjectName myJMSModuleConnfactorySecurityParams = (ObjectName)myJMXWrapper.getAttribute(myJMSModuleConnfactory,"SecurityParams");
			 // set AttachJMSXUserId
			 myJMXWrapper.setAttribute(myJMSModuleConnfactorySecurityParams, new Attribute("AttachJMSXUserId", new Boolean(false)));
			 
			 
			 // get the default delivery params
			 ObjectName myJMSModuleConnDefaultDeliveryParams = (ObjectName)myJMXWrapper.getAttribute(myJMSModuleConnfactory,"DefaultDeliveryParams");
			 // set AttachJMSXUserId
			 myJMXWrapper.setAttribute(myJMSModuleConnDefaultDeliveryParams, new Attribute("DefaultDeliveryMode", new String("Persistent")));
			 myJMXWrapper.setAttribute(myJMSModuleConnDefaultDeliveryParams, new Attribute("DefaultTimeToLive", new Integer(0)));
			 myJMXWrapper.setAttribute(myJMSModuleConnDefaultDeliveryParams, new Attribute("DefaultPriority", new Integer(2)));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createJmsConnectionFactory ("+jmsModuleName+":"+connectionFactoryName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	    


	public void createJms_XA_ConnectionFactory(String jmsModuleName, String connectionFactoryName, String jmsJNDIname) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");
			 
			 // create the connection factory
			 ObjectName myJMSModuleConnfactory = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"createConnectionFactory",new Object[]{connectionFactoryName},new String[]{String.class.getName()});
			 
			 // configure connection factory
			 myJMXWrapper.setAttribute(myJMSModuleConnfactory, new Attribute("JNDIName", new String(jmsJNDIname)));
			 myJMXWrapper.setAttribute(myJMSModuleConnfactory, new Attribute("DefaultTargetingEnabled", new Boolean(true)));
			    
			 // get the security params
			 ObjectName myJMSModuleConnfactorySecurityParams = (ObjectName)myJMXWrapper.getAttribute(myJMSModuleConnfactory,"SecurityParams");
			 // set AttachJMSXUserId
			 myJMXWrapper.setAttribute(myJMSModuleConnfactorySecurityParams, new Attribute("AttachJMSXUserId", new Boolean(false)));

			 // get the transaction params
			 ObjectName myJMSModuleConnTransactionParams = (ObjectName)myJMXWrapper.getAttribute(myJMSModuleConnfactory,"TransactionParams");
			 // configure it
			 myJMXWrapper.setAttribute(myJMSModuleConnTransactionParams, new Attribute("TransactionTimeout", new Integer(3600)));
			 myJMXWrapper.setAttribute(myJMSModuleConnTransactionParams, new Attribute("XAConnectionFactoryEnabled", new Boolean(true)));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createJms_XA_ConnectionFactory ("+jmsModuleName+":"+connectionFactoryName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	

	// create a subdeployment
	public void createJMSSubDeployment(String jmsModuleName, String subDeploymentName, String targetName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");

			 // get the resource
			 //ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");
			 
			// create subdeployment
			 ObjectName myJMSModuleSubDeploy = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModule,"createSubDeployment",new Object[]{subDeploymentName},new String[]{String.class.getName()});
			 
			 // target the new subdeployment
			 // set the target
			 myJMXWrapper.setAttribute(myJMSModuleSubDeploy, new Attribute("Targets", new ObjectName[]{new ObjectName("com.bea:Name="+targetName+",Type=JMSServer")}));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createJMSSubDeployment ("+jmsModuleName+":"+subDeploymentName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}	


	// Creating queue
	public void createQueue(String jmsModuleName, String queueName, String jndiQueueName, String subDeploymentName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

				// create queue
			 ObjectName myJMSQueue = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"createQueue",new Object[]{queueName},new String[]{String.class.getName()});
			 
			 // configue queue
			 myJMXWrapper.setAttribute(myJMSQueue, new Attribute("JNDIName", new String(jndiQueueName)));
			 myJMXWrapper.setAttribute(myJMSQueue, new Attribute("SubDeploymentName", new String(subDeploymentName)));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createQueue ("+jmsModuleName+":"+queueName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	// create queue with error handling    
	public void createQueueWithErrorHandling(String jmsModuleName, String queueName, String jndiQueueName, String subDeploymentName, String errorQueueName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

				// create queue
			 ObjectName myJMSQueue = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"createQueue",new Object[]{queueName},new String[]{String.class.getName()});
			 
			 // configure queue
			 myJMXWrapper.setAttribute(myJMSQueue, new Attribute("JNDIName", new String(jndiQueueName)));
			 myJMXWrapper.setAttribute(myJMSQueue, new Attribute("SubDeploymentName", new String(subDeploymentName)));

			 
			 // get the default delivery params
			 ObjectName myQueueDeliveryFailureParams = (ObjectName)myJMXWrapper.getAttribute(myJMSQueue,"DeliveryFailureParams");
			 // set AttachJMSXUserId
			 myJMXWrapper.setAttribute(myQueueDeliveryFailureParams, new Attribute("RedeliveryLimit", new Integer(3)));
			 myJMXWrapper.setAttribute(myQueueDeliveryFailureParams, new Attribute("ExpirationPolicy", new String("Redirect")));
			 
			 // lookup error queue on SAME module (can be done more generic if error queue is located on a different module)
			 ObjectName errorQueue = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"lookupQueue",new Object[]{errorQueueName},new String[]{String.class.getName()});
			 // set error queue
			 myJMXWrapper.setAttribute(myQueueDeliveryFailureParams, new Attribute("ErrorDestination",errorQueue));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createQueueWithErrorHandling ("+jmsModuleName+":"+queueName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	// Creating Topic
	public void createTopic(String jmsModuleName, String topicName, String jndiTopicName, String subDeploymentName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

				// create topic
			 ObjectName myJMSTopic = (ObjectName)(ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"createTopic",new Object[]{topicName},new String[]{String.class.getName()});
			 
			 // configure topic
			 myJMXWrapper.setAttribute(myJMSTopic, new Attribute("JNDIName", new String(jndiTopicName)));
			 myJMXWrapper.setAttribute(myJMSTopic, new Attribute("SubDeploymentName", new String(subDeploymentName)));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createTopic ("+jmsModuleName+":"+topicName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	
	// ###########################################  DESTROY #################################################

	public void destroyFileStore(String fileStoreName) throws WLSAutomationException
	{
		try {    
			 // get the domain config root
			 ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();
			 
			 // lookup filestore as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myFileStore = (ObjectName)myJMXWrapper.invoke(domainRoot,"lookupFileStore",new Object[]{fileStoreName},new String[]{String.class.getName()});
			 
			 // destroy filesstore 
			 myJMXWrapper.invoke(domainRoot,"destroyFileStore",new Object[]{myFileStore},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyFileStore ("+fileStoreName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	// destroy a JMS server
	public void destroyJMSServer(String jmsServerName) throws WLSAutomationException
	{
		try {    
			 // get the domain config root
			 ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();
			 
			 // lookup jmsserver as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myJMSServer = (ObjectName)myJMXWrapper.invoke(domainRoot,"lookupJMSServer",new Object[]{jmsServerName},new String[]{String.class.getName()});
			 
			 // destroy filesstore 
			 myJMXWrapper.invoke(domainRoot,"destroyJMSServer",new Object[]{myJMSServer},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyJMSServer ("+jmsServerName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	    	       
	// delete a JMS module
	public void destroyJMSModule(String jmsModuleName)  throws WLSAutomationException
	{
		try {    
			 // get the domain config root
			 ObjectName domainRoot = myJMXWrapper.getDomainConfigRoot();
			 
			 // lookup jmsmodule as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myJMSModule = (ObjectName)myJMXWrapper.invoke(domainRoot,"lookupJMSSystemResource",new Object[]{jmsModuleName},new String[]{String.class.getName()});
			 
			 // destroy filesstore 
			 myJMXWrapper.invoke(domainRoot,"destroyJMSSystemResource",new Object[]{myJMSModule},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyJMSModule ("+jmsModuleName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	// destroy a connection factory
	public void destroyJmsConnectionFactory(String jmsModuleName, String connectionFactoryName)  throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

			 // lookup connection factory from module as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myJMSConnFactory = (ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"lookupConnectionFactory",new Object[]{connectionFactoryName},new String[]{String.class.getName()});
			 
			 // destroy the connection factory
			 myJMXWrapper.invoke(myJMSModuleResource,"destroyConnectionFactory",new Object[]{myJMSConnFactory},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyJmsConnectionFactory ("+jmsModuleName+":"+connectionFactoryName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	

	// destroy a subdeployment
	public void destroyJMSSubDeployment(String jmsModuleName, String subDeploymentName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");


			 // lookup sub deplyoment from module as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myJMSSubDeployment = (ObjectName)myJMXWrapper.invoke(myJMSModule,"lookupSubDeployment",new Object[]{subDeploymentName},new String[]{String.class.getName()});
			 
			 // destroy the connection factory
			 myJMXWrapper.invoke(myJMSModule,"destroySubDeployment",new Object[]{myJMSSubDeployment},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyJMSSubDeployment ("+jmsModuleName+":"+subDeploymentName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}	


	// delete queue
	public void destroyQueue(String jmsModuleName, String queueName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

			 // lookup queue from moduleResource as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myQueue = (ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"lookupQueue",new Object[]{queueName},new String[]{String.class.getName()});
			 
			 // destroy the queue
			 myJMXWrapper.invoke(myJMSModuleResource,"destroyQueue",new Object[]{myQueue},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyQueue ("+jmsModuleName+":"+queueName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	// delete Topic
	public void destroyTopic(String jmsModuleName, String topicName) throws WLSAutomationException
	{
		try
		{    
			 // get the jmsModuleName config root
			 ObjectName myJMSModule = new ObjectName("com.bea:Name="+jmsModuleName+",Type=JMSSystemResource");
			 
			 // get the resource
			 ObjectName myJMSModuleResource = (ObjectName)myJMXWrapper.getAttribute(myJMSModule,"JMSResource");

			 // lookup topic from moduleResource as ObjectName is needed (alternatively you can construct the ObjectName)
			 ObjectName myTopic = (ObjectName)myJMXWrapper.invoke(myJMSModuleResource,"lookupTopic",new Object[]{topicName},new String[]{String.class.getName()});
			 
			 // destroy the topic
			 myJMXWrapper.invoke(myJMSModuleResource,"destroyTopic",new Object[]{myTopic},new String[]{ObjectName.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while destroyTopic ("+jmsModuleName+":"+topicName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

}
