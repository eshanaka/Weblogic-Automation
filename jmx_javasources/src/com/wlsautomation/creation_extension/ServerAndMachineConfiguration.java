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

import java.util.*;
import javax.management.ObjectName;
import javax.management.Attribute;
import com.wlsautomation.utils.*;

public class ServerAndMachineConfiguration {

  private JMXWrapper myJMXWrapper;

  public ServerAndMachineConfiguration(JMXWrapper _myJMXWrapper)
  {
    myJMXWrapper = _myJMXWrapper;
  }


  /**
   * Configure a new unix machine
   * @throws Exception
   */
  public void createUnixMachine(String machinename) throws WLSAutomationException
  {
		try
		{    
			  // e.g.: com.bea:Name=TestDomain,Type=Domain
		      ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");

		      // createUnixMachine
		      myJMXWrapper.invoke(myDomainMBean,
		                          "createUnixMachine",
		                          new Object[]{new String(machinename)},
		                          new String[]{String.class.getName()});

		      // the new mbean will have a name similiar to com.bea:Name=MyTestMachine,Type=UnixMachine
		      // also the nodemanager mbean was created: com.bea:Name=MyTestMachine,Type=NodeManager,UnixMachine=MyTestMachine

		      ObjectName myNodemanagerMBean = new ObjectName("com.bea:Name="+machinename+",Type=NodeManager,UnixMachine="+machinename);

		      // set attribute NMType
		      myJMXWrapper.setAttribute(myNodemanagerMBean,new Attribute("NMType",new String("SSL")));

		      // set attribute ListenAddress
		      myJMXWrapper.setAttribute(myNodemanagerMBean,new Attribute("ListenAddress",new String("myUnixHostName")));

		      // set attribute ListenPort
		      myJMXWrapper.setAttribute(myNodemanagerMBean,new Attribute("ListenPort",new Integer(4711)));

		      // set attribute DebugEnabled
		      myJMXWrapper.setAttribute(myNodemanagerMBean,new Attribute("DebugEnabled",new Boolean("false")));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createUnixMachine ("+machinename+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
  }


  /**
   * Configure a new managed server
   * @param servername String
   * @param clustername String
   * @param machinename String
   * @throws Exception
   */
  public void createManagedServer(String servername, String clustername, String machinename) throws Exception
  {
		try
		{   
			String domainName = (String)myJMXWrapper.getMainServerDomainValues().get("domainName");
			
			  // e.g.: com.bea:Name=TestDomain,Type=Domain
		      ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");

		      // javax.management.ObjectName  createCluster(name:java.lang.String  )
		      // the new mbean will have a name similiar to com.bea:Name=TestManagdServer,Type=Server
		      ObjectName myManagedServerMBean = (ObjectName)myJMXWrapper.invoke(myDomainMBean,
		                                                                "createServer",
		                                                                new Object[]{new String(servername)},
		                                                                new String[]{String.class.getName()});

		      // and SSL: com.bea:Name=TestManagdServer,Server=TestManagdServer,Type=SSL
		      ObjectName myManagedServerSSLMBean = new ObjectName("com.bea:Name="+servername+",Server="+servername+",Type=SSL");
		      // and ServerStart: com.bea:Name=MyTestServer,Server=MyTestServer,Type=ServerStart
		      ObjectName myManagedServerServerStartMBean = new ObjectName("com.bea:Name="+servername+",Server="+servername+",Type=ServerStart");


		      // set attribute ListenAddress
		      myJMXWrapper.setAttribute(myManagedServerMBean,new Attribute("ListenAddress",new String("myManagedServerListenerAddress")));

		      // set attribute ListenPort
		      myJMXWrapper.setAttribute(myManagedServerMBean,new Attribute("ListenPort",new Integer(10432)));

		      // set attribute ListenPortEnabled
		      myJMXWrapper.setAttribute(myManagedServerMBean,new Attribute("ListenPortEnabled",new Boolean(true)));


		      // add to machine
		      if (machinename != null)
		          myJMXWrapper.setAttribute(myManagedServerMBean,new Attribute("Machine",new ObjectName("com.bea:Name="+machinename+",Type=UnixMachine")));

		      // add to cluster
		      // set attribute Cluster
		      if (clustername != null)
		          myJMXWrapper.setAttribute(myManagedServerMBean,new Attribute("Cluster",new ObjectName("com.bea:Name="+clustername+",Type=Cluster")));


		      // SSL
		      // set attribute ListenPort
		      myJMXWrapper.setAttribute(myManagedServerSSLMBean,new Attribute("ListenPort",new Integer(11111)));

		      // set attribute Enabled
		      myJMXWrapper.setAttribute(myManagedServerSSLMBean,new Attribute("Enabled",new Boolean(true)));


		      // SERVERSTART
		      // note the following paths and file names are only examples !!
		      String ms_out = "/logs/domains/"+domainName+"/"+servername+"/"+servername+".out";
		      String ms_err = "/logs/domains/"+domainName+"/"+servername+"/"+servername+".err";
		      String managedserver_args = "-Djava.awt.headless=true  -Xmx1024 -XX:MaxPermSize=512m -Dweblogic.Stdout="+ms_out+" -Dweblogic.Stderr="+ ms_err;

		      // set attribute Arguments
		      myJMXWrapper.setAttribute(myManagedServerServerStartMBean,new Attribute("Arguments",managedserver_args));

		      // set attribute JavaHome
		      myJMXWrapper.setAttribute(myManagedServerServerStartMBean,new Attribute("JavaHome","/opt/jdks/jdk1.6"));
		}
		catch(Exception ex)
		{
			System.out.println("Error while createManagedServer ("+servername+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
  }
  
  
  
  public void renameMachineName()  throws WLSAutomationException
   {
	   // change machine names
     try
     {
       // get domain configuration mbean	 
       ObjectName domainMBean =(ObjectName)myJMXWrapper.getAttribute(myJMXWrapper.getService(),"DomainConfiguration");

       // get all machines
       ObjectName[] old_machines = (ObjectName[])myJMXWrapper.getAttribute(domainMBean,"Machines");
       
       // a list of all machines in a hasmap format for better mapping 
       HashMap<String,ObjectName> allMachines = new HashMap<String,ObjectName>();
       
       // a list of all machine names for better mapping and increased performance
       ArrayList<String> allMachineNames = new ArrayList<String>();

       // iterate over all old machine and create the two lists defined
       for (int om=0;om<old_machines.length;om++)
       {
           String nextname = (String)myJMXWrapper.getAttribute(old_machines[om],"Name");
           System.out.println(" Found machine "+nextname+" of type "+old_machines[om].toString());
           allMachines.put(nextname,old_machines[om]);
           allMachineNames.add(nextname);
       }

       // a list to collect all machines which can be deleted
       ArrayList<ObjectName> toBeDeletedMachinesMap = new ArrayList<ObjectName>();
       
       // a list with all new machines
       HashMap<String,ObjectName> myNewMachines = new  HashMap<String,ObjectName>();

       // iterate over all machines
       Iterator<String> omIt = allMachines.keySet().iterator();
       while (omIt.hasNext())
       {
         String nextOldName = (String)omIt.next();
         ObjectName nextOldMachine = (ObjectName)allMachines.get(nextOldName);
         System.out.println("Testing machine: "+nextOldName+" with objectname="+nextOldMachine.toString());

         // This is only an example in order to demo this functionality.
         // normally there should be a mapping method which returns the newName if a new name is required
         //  String newName = getNewNameIfMachineMustBeRenamed(nextOldName);
         
         // for demo purpose change all machines and add_TEST to the machine name
         String newName = nextOldName+"_MyTEST";
         //if (newName == null)
         //{   // for the demo this code will never be reached !!
         //	 System.out.println("Machine "+nextOldName+" MUST NOT be converted !");
         //}
         //else // ok something to do
         {
        	System.out.println("New Name for machine "+nextOldName+" is "+newName);

            // create Machine
            Object  values[] = { newName};
            String  signatur[] = { String.class.getName()};

            if (allMachines.containsKey(newName))
            {
            	System.out.println("??? Machine "+newName+" ALREADY EXISTS - using this one !!");
                toBeDeletedMachinesMap.add(nextOldMachine);  // this means this machine will later be deleted !!
                myNewMachines.put(nextOldName,(ObjectName)allMachines.get(newName));
            }
            else // create a new machine with the new name and then copy all values (including nodemanager !!)
            {
            	System.out.println("Try to create machine "+newName);
                ObjectName newMachine = (ObjectName)myJMXWrapper.invoke(domainMBean,"createUnixMachine",values,signatur);
                System.out.println("Created machine"+newName);

               toBeDeletedMachinesMap.add(nextOldMachine);  // this means this machine will later be deleted !!
               myNewMachines.put(nextOldName,newMachine);

               System.out.println("Copy nodemanager from machine "+nextOldName+" to "+newName);

               // old nodemanager
               ObjectName myOldNodeManager = (ObjectName)myJMXWrapper.getAttribute(nextOldMachine,"NodeManager");
               // new nodemanager
               ObjectName myNewNodeManager = (ObjectName)myJMXWrapper.getAttribute(newMachine,"NodeManager");

               // copy all values from old machine to new machine
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("NMType",myJMXWrapper.getAttribute(myOldNodeManager,"NMType")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("NodeManagerHome",myJMXWrapper.getAttribute(myOldNodeManager,"NodeManagerHome")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("ListenPort",myJMXWrapper.getAttribute(myOldNodeManager,"ListenPort")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("Notes",myJMXWrapper.getAttribute(myOldNodeManager,"Notes")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("Name",myJMXWrapper.getAttribute(myOldNodeManager,"Name")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("DebugEnabled",myJMXWrapper.getAttribute(myOldNodeManager,"DebugEnabled")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("ShellCommand",myJMXWrapper.getAttribute(myOldNodeManager,"ShellCommand")));
               myJMXWrapper.setAttribute(myNewNodeManager,new Attribute("ListenAddress",myJMXWrapper.getAttribute(myOldNodeManager,"ListenAddress")));

            }

         }
       }

       // now as all new machines are created, reconfigure - means retarget the appropriate server
       System.out.println("RECONFIGURE all servers if necessary !");
       ObjectName[] serverRuntimes = (ObjectName[])myJMXWrapper.getAttribute(domainMBean,"Servers");
       for (int i = 0; i < serverRuntimes.length; i++)
       {
           String serverName = serverRuntimes[i].getKeyProperty("Name");
           System.out.println("Try to reconfigure "+serverName);
            // Machine
            try {
            	// get the machine of this managed server , if any
                ObjectName server_machine_object = (ObjectName)myJMXWrapper.getAttribute(serverRuntimes[i],"Machine");
                // if this server is attached to a machine (note adminservers often are not)
                if (server_machine_object!=null)
                {
                  String server_machine_name = (String)myJMXWrapper.getAttribute(server_machine_object,"Name");

                  // map name and set new machine
                  ObjectName myNewMachineTmpName = (ObjectName)myNewMachines.get(server_machine_name);

                  // if no entry is found in the new machine list - this means no new machine has been created
                  if (myNewMachineTmpName == null)
                  {
                	  System.out.println("Server" +serverName+" does NOT need any modifications !");
                  }
                  else
                  {
                    // set Machine - re-target the server to the new machine
                    Attribute newMachineAttribute = new Attribute("Machine",myNewMachineTmpName);
                    myJMXWrapper.setAttribute(serverRuntimes[i],newMachineAttribute);
                  }
                }
                else
                	System.out.println("NO machine found for Server = "+serverName);
            }
            catch (Exception ex) {
            	System.out.println("Problem in SwitchAllMachinesStep:executeStep "+ex);
                throw new WLSAutomationException(ex.getMessage());
            }
       }

       // destroy all old Machine
       System.out.println("DESTROY all old machines");
       //Iterator it =
       for (int om=0;om<toBeDeletedMachinesMap.size();om++)
       {
    	  System.out.println("DESTROY "+((ObjectName)toBeDeletedMachinesMap.get(om)).toString());
          Object  values[] = { (ObjectName)toBeDeletedMachinesMap.get(om)};
          String  signatur[] = { javax.management.ObjectName.class.getName()};
          myJMXWrapper.invoke(domainMBean,"destroyMachine",values,signatur);
       }

   }
   catch (Exception ex) {
         throw new WLSAutomationException(ex.getMessage());
     }
   }



   // utility function to check weather a machine still has
   // managed-server assigned to it
   public boolean machineHostsManagedServer(String machineName) throws WLSAutomationException
   {
	    boolean machineHasServers = false;
		try
		{    
			   // e.g.: com.bea:Name=TestDomain,Type=Domain
		       ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");

		       ObjectName[] serverRuntimes = (ObjectName[])myJMXWrapper.getAttribute(myDomainMBean,"Servers");
		       for (int i = 0; i < serverRuntimes.length; i++)
		       {
		           //String serverName = serverRuntimes[i].getKeyProperty("Name");

		           ObjectName server_machine_object = (ObjectName)myJMXWrapper.getAttribute(serverRuntimes[i],"Machine");
		           if (server_machine_object!=null)  // machine found for Server
		           {
		              String server_machine_name = (String)myJMXWrapper.getAttribute(server_machine_object,"Name");
		              
		              if (machineName.equals(server_machine_name))
		            	  machineHasServers = true;   // ok found a server for this machine
		           }
		       }
		       
		       return machineHasServers;
		}
		catch(Exception ex)
		{
			System.out.println("Error while machineHostsManagedServer ("+machineName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
   }


   
   // utility function to get the servers hosted on a special machine
   public ArrayList<ObjectName> getServersOfMachine(String machineName) throws WLSAutomationException
   {
	   ArrayList<ObjectName> resultList = new ArrayList<ObjectName>();
		try
		{    
			   // e.g.: com.bea:Name=TestDomain,Type=Domain
		       ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");

		       ObjectName[] serverRuntimes = (ObjectName[])myJMXWrapper.getAttribute(myDomainMBean,"Servers");
		       for (int i = 0; i < serverRuntimes.length; i++)
		       {
		           ObjectName server_machine_object = (ObjectName)myJMXWrapper.getAttribute(serverRuntimes[i],"Machine");
		           if (server_machine_object!=null)  // machine found for Server
		           {
		              String server_machine_name = (String)myJMXWrapper.getAttribute(server_machine_object,"Name");
		              
		              if (machineName.equals(server_machine_name))
		            	  resultList.add(serverRuntimes[i]);
		           }
		       }
		       
		       return resultList;
		}
		catch(Exception ex)
		{
			System.out.println("Error while machineHostsManagedServer ("+machineName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
   }
   


   // utility function to check weather a cluster still has members
   public boolean clusterHasManagedServers(String clusterName) throws WLSAutomationException
   {
		try
		{    
		   // e.g.: com.bea:Name=TestDomain,Type=Domain
		   ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
		   
		   ObjectName myCluster = (ObjectName)myJMXWrapper.invoke(myDomainMBean,"lookupCluster",new Object[]{clusterName},new String[]{String.class.getName()});

		   if (myCluster == null)
			   throw new WLSAutomationException("Cluster "+clusterName+" does not exist !");
		   else
		   {
			   //  Attribute: Servers   of Type : [Ljavax.management.ObjectName;
			   ObjectName[] clusterMembers = (ObjectName[])myJMXWrapper.getAttribute(myCluster,"Servers");
			   
			   if (clusterMembers==null ||clusterMembers.length==0)
				   return false;   // no members
			   else
				   return true;  // at least one member found;
		   }
		}
		catch(WLSAutomationException ex)
		{
			// just re-throw
			throw ex;
		}
		catch(Exception ex)
		{
			System.out.println("Error while clusterHasManagedServers ("+clusterName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
   
   }

   
// ######################################## Delete functions ##############################################################

   public boolean managedserverHostsApplications(String managedServerName)
   {
	   // TO DO
	   
	   return true;
   }


   public boolean managedserverHostsDatasources(String managedServerName)
   {
	   // TO DO
	   
	   return true;
   }

   // delete a specific managed-server or if 'None' is passed as argument delete all managed-servers
   // note that optionally the system can check if datasources, JMS providers or applications are still
   // hosted on this server - then it will not delete it unless you pass true for the second option
   public void deleteManagedServer(String managedServerName, boolean deleteAlsoIfDependenciesExist) throws WLSAutomationException
   {
		try
		{    
			// e.g.: com.bea:Name=TestDomain,Type=Domain
		    ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
		    
		    //          Operation: javax.management.ObjectName  lookupServer(name:java.lang.String  )
			ObjectName myServer = (ObjectName)myJMXWrapper.invoke(myDomainMBean,"lookupServer",new Object[]{managedServerName},new String[]{String.class.getName()});

			if (myServer == null)
				   throw new WLSAutomationException("Server "+managedServerName+" does not exist !");
			else
			{
				// server exists
		        if (! deleteAlsoIfDependenciesExist)
		        {
		           // check for dependencies
		           if (managedserverHostsApplications(managedServerName))
		        	   throw new WLSAutomationException("Applications still deployed on server "+managedServerName+" - cannot delete !");
		           if (managedserverHostsDatasources(managedServerName))
		        	   throw new WLSAutomationException("Datasources still deployed on server "+managedServerName+" - cannot delete !");
		        }
		        
		        // ok, can delete
		        System.out.println("Managed Server "+managedServerName+" will be destroyed !");
		        
		        // detach from cluster if any
		        myJMXWrapper.setAttribute(myServer, new Attribute("Cluster", null));
		        // detach from machine if any
		        myJMXWrapper.setAttribute(myServer, new Attribute("Machine", null));

		        // destroy server
		        myJMXWrapper.invoke(myDomainMBean,"destroyServer",new Object[]{myServer},new String[]{ObjectName.class.getName()});		        
			}
		    
		}
		catch(WLSAutomationException ex)
		{
			// just re-throw
			throw ex;
		}
		catch(Exception ex)
		{
			System.out.println("Error while deleteManagedServer ("+managedServerName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	   
   }
   

   // delete a specific cluster or if 'None' is passed as argument delete all clusters
   // note that optionally the system can check if managed-servers are still members of this cluster.
   // In this case it will not delete it unless you pass true for the second option. In the later case this
   // function has to detach the server(s) from the cluster first, otherwise it cannot be deleted
   public void deleteCluster(String clusterName, boolean deleteAlsoIfDependenciesExist) throws WLSAutomationException
   {
		try
		{    
			   // e.g.: com.bea:Name=TestDomain,Type=Domain
			   ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
			   
			   ObjectName myCluster = (ObjectName)myJMXWrapper.invoke(myDomainMBean,"lookupCluster",new Object[]{clusterName},new String[]{String.class.getName()});

			   if (myCluster == null)
				   throw new WLSAutomationException("Cluster "+clusterName+" does not exist !");
			   else
			   {
					// cluster exists
			        if (! deleteAlsoIfDependenciesExist)
			        {
			        	// check for dependencies
			        	if (clusterHasManagedServers(clusterName))
			        		throw new WLSAutomationException("Cluster "+clusterName+" still has server members - cannot delete !");
			        }
			        
			        // ok delete cluster
					//  Attribute: Servers   of Type : [Ljavax.management.ObjectName;
					ObjectName[] clusterMembers = (ObjectName[])myJMXWrapper.getAttribute(myCluster,"Servers");
					   
					if (clusterMembers!=null && clusterMembers.length>0)
					{
						for (int i=0;i<clusterMembers.length;i++)
							myJMXWrapper.setAttribute(clusterMembers[i], new Attribute("Cluster", null));
					}

					// destroy
					System.out.println("Cluster "+clusterName+" will be destroyed !");
					myJMXWrapper.invoke(myDomainMBean,"destroyCluster",new Object[]{myCluster},new String[]{ObjectName.class.getName()});		        
			   }
		}
		catch(WLSAutomationException ex)
		{
			// just re-throw
			throw ex;
		}
		catch(Exception ex)
		{
			System.out.println("Error while deleteCluster ("+clusterName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	   
   }


   // delete a specific machine or if 'None' is passed as argument delete all machines
   // note that optionally the system can check if managed-servers are still hosted on this machine.
   // In this case it will not delete it unless you pass true for the second option
   public void deleteMachine(String machineName, boolean deleteAlsoIfDependenciesExist) throws WLSAutomationException
   {
		try
		{    
			   // e.g.: com.bea:Name=TestDomain,Type=Domain
			   ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot(); // new ObjectName("com.bea:Name=" + domainName +",Type=Domain");
			   
			   ObjectName myMachine = (ObjectName)myJMXWrapper.invoke(myDomainMBean,"lookupMachine",new Object[]{machineName},new String[]{String.class.getName()});

			   if (myMachine == null)
				   throw new WLSAutomationException("Machine "+machineName+" does not exist !");
			   else
			   {
					// cluster exists
			        if (! deleteAlsoIfDependenciesExist)
			        {
			        	// check for dependencies
			        	if (machineHostsManagedServer(machineName))
			        		throw new WLSAutomationException("Machine "+machineName+" still has server members - cannot delete !");
			        }
			        
			        // ok delete machine
			        ArrayList<ObjectName> machineServers = getServersOfMachine(machineName);
			        for (int i=0;i<machineServers.size();i++)
							myJMXWrapper.setAttribute(machineServers.get(i), new Attribute("Machine", null));

					// destroy
					System.out.println("Machine "+machineName+" will be destroyed !");
					myJMXWrapper.invoke(myDomainMBean,"destroyMachine",new Object[]{myMachine},new String[]{ObjectName.class.getName()});		        
			   }
		}
		catch(WLSAutomationException ex)
		{
			// just re-throw
			throw ex;
		}
		catch(Exception ex)
		{
			System.out.println("Error while deleteMachine ("+machineName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	   
   }
}
