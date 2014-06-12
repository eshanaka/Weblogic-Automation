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

import javax.management.ObjectName;
import javax.management.Attribute;
import com.wlsautomation.utils.*;

public class NetworkResourceConfiguration {

  private JMXWrapper myJMXWrapper;

  public NetworkResourceConfiguration(JMXWrapper _myJMXWrapper)
  {
    myJMXWrapper = _myJMXWrapper;
  }


  public void createHTTPNetworkChannel(String serverName, String channelName, int port) throws WLSAutomationException
  {
	 try {    
	   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
               "lookupServer",
               new Object[]{new String(serverName)},
               new String[]{String.class.getName()});
		      
	   // create new channel with the provided name
	   ObjectName myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
               "lookupNetworkAccessPoint",
               new Object[]{new String(channelName)},
               new String[]{String.class.getName()});
	   
	   if (myNewChannel != null)
		   throw new WLSAutomationException("NetworkChannel "+channelName+" already exists !");
	   
	   // now create
	   myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
		                         "createNetworkAccessPoint",
		                         new Object[]{new String(channelName)},
		                         new String[]{String.class.getName()});

	   // set protocol to http
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Protocol",new String("http")));

	   // set listener port
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ListenPort",new Integer(port)));

	   // enable
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Enabled",new Boolean(true)));
		      
	   // enable HttpEnabledForThisProtocol
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("HttpEnabledForThisProtocol",new Boolean(true)));
		      
	   // set OutboundEnabled
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("OutboundEnabled",new Boolean(false)));

	   // disable https
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("TwoWaySSLEnabled",new Boolean(false)));
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ClientCertificateEnforced",new Boolean(false)));
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while createHTTPNetworkChannel ("+channelName+"): "+ ex.getMessage());
	}
  }


  
  public void createT3NetworkChannel(String serverName, String channelName, int port) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // create new channel with the provided name
	   ObjectName myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
               "lookupNetworkAccessPoint",
               new Object[]{new String(channelName)},
               new String[]{String.class.getName()});
	   
	   if (myNewChannel != null)
		   throw new WLSAutomationException("NetworkChannel "+channelName+" already exists !");
	   
	   // now create
	   myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
		                         "createNetworkAccessPoint",
		                         new Object[]{new String(channelName)},
		                         new String[]{String.class.getName()});

	   // set protocol to http
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Protocol",new String("t3")));

	   // set listener port
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ListenPort",new Integer(port)));

	   // enable
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Enabled",new Boolean(true)));
		      
	   // enable HttpEnabledForThisProtocol
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("HttpEnabledForThisProtocol",new Boolean(false)));
		      
	   // set OutboundEnabled
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("OutboundEnabled",new Boolean(false)));

	   // disable https
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("TwoWaySSLEnabled",new Boolean(false)));
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ClientCertificateEnforced",new Boolean(false)));
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while createT3NetworkChannel ("+channelName+"): "+ ex.getMessage());
	}
  }
  
  public void createIIOPNetworkChannel(String serverName, String channelName, int port) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // create new channel with the provided name
	   ObjectName myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
               "lookupNetworkAccessPoint",
               new Object[]{new String(channelName)},
               new String[]{String.class.getName()});
	   
	   if (myNewChannel != null)
		   throw new WLSAutomationException("NetworkChannel "+channelName+" already exists !");
	   
	   // now create
	   myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
		                         "createNetworkAccessPoint",
		                         new Object[]{new String(channelName)},
		                         new String[]{String.class.getName()});

	   // set protocol to http
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Protocol",new String("iiop")));

	   // set listener port
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ListenPort",new Integer(port)));

	   // enable
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Enabled",new Boolean(true)));
		      
	   // enable HttpEnabledForThisProtocol
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("HttpEnabledForThisProtocol",new Boolean(false)));
		      
	   // set OutboundEnabled
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("OutboundEnabled",new Boolean(false)));

	   // disable https
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("TwoWaySSLEnabled",new Boolean(false)));
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ClientCertificateEnforced",new Boolean(false)));
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while createIIOPNetworkChannel ("+channelName+"): "+ ex.getMessage());
	}
  }
  
  
  public void createHTTPSNetworkChannel(String serverName, String channelName, int port, boolean enforceClientCert) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // create new channel with the provided name
	   ObjectName myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
               "lookupNetworkAccessPoint",
               new Object[]{new String(channelName)},
               new String[]{String.class.getName()});
	   
	   if (myNewChannel != null)
		   throw new WLSAutomationException("NetworkChannel "+channelName+" already exists !");
	   
	   // now create
	   myNewChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
		                         "createNetworkAccessPoint",
		                         new Object[]{new String(channelName)},
		                         new String[]{String.class.getName()});

	   // set protocol to http
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Protocol",new String("https")));

	   // set listener port
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ListenPort",new Integer(port)));

	   // enable
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("Enabled",new Boolean(true)));
		      
	   // enable HttpEnabledForThisProtocol
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("HttpEnabledForThisProtocol",new Boolean(true)));
	   
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("TunnelingEnabled",new Boolean(false)));
		      
	   // set OutboundEnabled
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("OutboundEnabled",new Boolean(false)));

	   // enable https
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("TwoWaySSLEnabled",new Boolean(true)));
	   myJMXWrapper.setAttribute(myNewChannel,new Attribute("ClientCertificateEnforced",new Boolean(enforceClientCert)));
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while createHTTPSNetworkChannel ("+channelName+"): "+ ex.getMessage());
	}
  }  
 
  
  public void deleteNetworkChannel(String serverName, String channelName) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // lookup the channel with the provided name
	   ObjectName myChannel = (ObjectName)myJMXWrapper.invoke(myServerRuntime,
               "lookupNetworkAccessPoint",
               new Object[]{new String(channelName)},
               new String[]{String.class.getName()});
	   
	   if (myChannel != null)
	   {
		   // now destroy
		   myJMXWrapper.invoke(myServerRuntime,
			                   "destroyNetworkAccessPoint",
			                   new Object[]{myChannel},
			                   new String[]{ObjectName.class.getName()});
	   }
	   else
		   throw new WLSAutomationException("NetworkChannel "+channelName+" already exists !");
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while deleteNetworkChannel ("+channelName+"): "+ ex.getMessage());
	}
  }  
 
  
  // add an existing channel to a managed-server    
  public void addNetworkChannel(String serverName, ObjectName myChannel) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // add the channel to this server
	   myJMXWrapper.invoke(myServerRuntime,
               "addNetworkAccessPoint",
               new Object[]{myChannel},
               new String[]{ObjectName.class.getName()});
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while addNetworkChannel : "+ ex.getMessage());
	}
  }  
  

  // remove an existing channel to a managed-server    
  public void removeNetworkChannel(String serverName, ObjectName myChannel) throws WLSAutomationException
  {
	 try {    
		   ObjectName myServerRuntime = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(serverName)},
	               new String[]{String.class.getName()});
		      
	   // remove the channel to this server
	   myJMXWrapper.invoke(myServerRuntime,
               "removeNetworkAccessPoint",
               new Object[]{myChannel},
               new String[]{ObjectName.class.getName()});
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while addNetworkChannel : "+ ex.getMessage());
	}
  }  
  
  
  
  // ****************************************  Virtual Host **************************************************
  
  public void createVirtualHost(String virtualhostName,  // name
		  						String channelName, // which networkchannel is attached
		  						String[] targetServerNames,  // targets
		  						String[] networkNames  // on which accept requests
		  					   ) throws WLSAutomationException
  {
	 try {    
		 
	   // e.g.: com.bea:Name=TestDomain,Type=Domain
	   ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
	 		      

	   
	   // lookup and try to find virtual host
	   ObjectName myNewVirtualHost = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
               "lookupVirtualHost",
               new Object[]{new String(virtualhostName)},
               new String[]{String.class.getName()});
	   
	   if (myNewVirtualHost != null)
		   throw new WLSAutomationException("Virtual host "+virtualhostName+" already exists !");
	   
	   // create new virtual host with the provided name
	   myNewVirtualHost = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
		                         "createVirtualHost",
		                         new Object[]{new String(virtualhostName)},
		                         new String[]{String.class.getName()});

	   // set referenced network channel
	   myJMXWrapper.setAttribute(myNewVirtualHost,new Attribute("NetworkAccessPoint",channelName));

	   
	   // get servers and set it to virtual host targets
	   ObjectName[] myServerRuntimes = new ObjectName[targetServerNames.length];
	   for (int i=0;i<targetServerNames.length;i++)
		   myServerRuntimes[i] = (ObjectName)myJMXWrapper.invoke(myJMXWrapper.getDomainConfigRoot(),
	               "lookupServer",
	               new Object[]{new String(targetServerNames[i])},
	               new String[]{String.class.getName()});

	   myJMXWrapper.setAttribute(myNewVirtualHost,new Attribute("Targets",myServerRuntimes));

	   // set  VirtualHostNames
	   myJMXWrapper.setAttribute(myNewVirtualHost,new Attribute("VirtualHostNames",networkNames));
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while createVirtualHost ("+virtualhostName+"): "+ ex.getMessage());
	}
  }
  
  
  public void deleteVirtualHost(String virtualhostName) throws WLSAutomationException
  {
	 try {    
		 
	   // e.g.: com.bea:Name=TestDomain,Type=Domain
	   ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
	   
	   // lookup and try to find virtual host
	   ObjectName myVirtualHost = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
               "lookupVirtualHost",
               new Object[]{new String(virtualhostName)},
               new String[]{String.class.getName()});
	   
	   if (myVirtualHost != null)
		   // destroy virtual host with the provided name
		   myJMXWrapper.invoke(myDomainMBean,
			                   "destroyVirtualHost",
			                   new Object[]{myVirtualHost},
			                   new String[]{ObjectName.class.getName()});
	   else		   
		   throw new WLSAutomationException("Virtual host "+virtualhostName+" does not exists !");
	}
	catch(Exception ex)
	{
		throw new WLSAutomationException("Error while destroyVirtualHost ("+virtualhostName+"): "+ ex.getMessage());
	}
  }
  
}
