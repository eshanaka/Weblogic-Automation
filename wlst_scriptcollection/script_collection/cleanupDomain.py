# 
# WebLogic Automation Book Source Code (WLST script collection)
# 
# This file is part of the WLS-Automation book sourcecode software distribution. 
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
# POSSIBILITY OF SUCH DAMAGE.
#
# @author Martin Heinzl
# Copyright (C) 2013 MH-EnterpriseConsulting, All rights reserved.
#
#
#

import sys

# --------------  Check functions ------------------------------	

# utility function to check weather a managed-server still hosts
# one or more applications
def managedserverHostsApplications(managedServerName):
    # to do
    return false
	

# utility function to check weather a managed-server still hosts
# one or more datasources
def managedserverHostsDatasources(managedServerName):
    # to do
    return false
	
# utility function to check weather a managed-server still hosts
# one or more JMS provider
def managedserverHostsJMSProviders(managedServerName):
    # to do
    return false

# utility function to check weather a machine still has 
# managed-server assigned to it
def machineHostsManagedServer(machineName):
   cd ('/')
   # get list of servers
   listOfManagedServer = cmo.getServers()
	   
   # loop over the server mbeans and if the right one is found (or all if 'None' was provided) delete it
   already_found = false
   for nextManagedServerInstance in listOfManagedServer:
       # test if machine is defined and if yes if it is the machine in question
       if (managedServerNnextManagedServerInstance.getMachine()!=None):
	   if (managedServerNnextManagedServerInstance.getMachine().getName() == machineName):
               already_found = true; 
    
   return already_found			 


# utility function to check weather a cluster still has members
def clusterHasManagedServers(clusterName):
    cd ('/')
    # get the cluster mbean
    clusterMBean = getMBean('/Clusters/'+clusterName)
	
    # check if the number of servers in the cluster is > 0
    if (len(clusterMBean.getServers() > 0)):
        return true
    else:
	return false

	
# --------------  Delete functions ------------------------------	
	
# delete a specific datasource or if 'None' is passed as argument delete all DS
def deleteDataSource(dataSourceName):
   serverConfig()
   cd ('/')
   listOfDataSources = cmo.getJDBCSystemResources()
	   
   # loop over the datasources mbeans and if the right one is found (or all if 'None' was provided) delete it
   edit()
   startEdit()
   already_found = 'false'
   for datasourceInstance in listOfDataSources:
       # if desired datasource is found OR no name (=None) was provided
       if ((dataSourceName==None) or (dataSourceName == datasourceInstance.getName())):
            # print name
            print 'Datasource '+datasourceInstance.getName()+' will be destroyed !'
	    # first delete targets
	    datasourceInstance.setTargets(None)
            # delete
	    cmo.destroyForeignJNDIProvider(datasourceInstance)
            # remember that DataSource was found
            already_found = 'true'

       if ((dataSourceName!=None) and (already_found=='false')):
          print 'DataSource '+ dataSourceName + ' not found'
   save()
   activate()
   serverConfig()


# delete a specific mail session or if 'None' is passed as argument delete all sessions
def deleteMailSessions(mailSessionName):
    # to do
    return
	

# delete a specific JMS provider session or if 'None' is passed as argument delete all provider
def deleteJMSProvider(jmsProviderName):
    # to do
    return

	
# delete a specific JNDI provider session or if 'None' is passed as argument delete all provider
def deleteForeignJNDIProvider(providerName):
   serverConfig()
   cd ('/')
   listOfForeignProviders = cmo.getForeignJNDIProviders()
	   
   # loop over the foreign provider mbeans and if the right one is found (or all if 'None' was provided) delete it
   edit()
   startEdit()
   already_found = 'false'
   for foreignProviderInstance in listOfForeignProviders:
       # if desired provider is found OR no name (=None) was provided
       if ((providerName==None) or (providerName == foreignProviderInstance.getName())):
             # print name
             print 'Foreign provider '+foreignProviderInstance.getName()+' will be destroyed !'
             # delete
	     cmo.destroyForeignJNDIProvider(foreignProviderInstance)
             # remember that Provider was found
             already_found = 'true'

       if ((providerName!=None) and (already_found=='false')):
          print 'Foreign provider '+ providerName + ' not found'
   save()
   activate()
   serverConfig()
	

# delete a specific managed-server or if 'None' is passed as argument delete all managed-servers
# note that optionally the system can check if datasources, JMS providers or applications are still
# hosted on this server - then it will not delete it unless you pass true for the second option
def deleteManagedServer(managedServerName, deleteAlsoIfDependenciesExist=false):
   print 'deleteManagedServer: '
   print deleteAlsoIfDependenciesExist
   serverConfig()
   cd ('/')
   listOfManagedServer = cmo.getServers()
	   
   # loop over the server mbeans and if the right one is found (or all if 'None' was provided) delete it
   edit()
   startEdit()
   already_found = 'false'
   for nextManagedServerInstance in listOfManagedServer:
       # if desired MS is found OR no name (=None) was provided
       if ((managedServerName==None) or (managedServerName == nextManagedServerInstance.getName())):
           can_be_deleted = 'true'; 
           if (str(deleteAlsoIfDependenciesExist)=='false'):
	         # check for dependencies
	         if (managedserverHostsApplications(managedServerName)):
		    can_be_deleted = 'false'
                    print 'Applications still deployed on managed-server '+nextManagedServerInstance.getName()+' - cannot delete.'
	         if (managedserverHostsDatasources(managedServerName)):
		    can_be_deleted = 'false'
                    print 'Managed-Server '+nextManagedServerInstance.getName()+' still hosts datasources - cannot delete.'
	         if (managedserverHostsJMSProviders(managedServerName)):
		    can_be_deleted = 'false'
                    print 'Managed-Server '+nextManagedServerInstance.getName()+' still hosts JMS provider - cannot delete.'
			 
	   if (can_be_deleted=='true'):
		# print name
                print 'Managed Server '+nextManagedServerInstance.getName()+' will be destroyed !'
                # detach from cluster if any
                nextManagedServerInstance.setCluster(None)
                # detach from machine if any
                nextManagedServerInstance.setMachine(None)

	        # finally shutdown the server
		print "Stopping " + nextManagedServerInstance.getName();
		# note that we are using the nodemanager functions instead of the runtime mbeans
		# so that we do not need to switch back and forward between runtime and edit tree
                try:               
                   shutdown(nextManagedServerInstance.getName(),'Server','true',1000,force='true', block='true')
                except:
                   pass
                
                delete(nextManagedServerInstance.getName(),'Server')

	   # remember that Provider was found
           already_found = 'true'

   if ((managedServerName!=None) and (already_found=='false')):
      print 'Managed-Server '+ managedServerName + ' not found'
   save()
   activate()
   serverConfig()


# delete a specific cluster or if 'None' is passed as argument delete all clusters
# note that optionally the system can check if managed-servers are still members of this cluster.
# In this case it will not delete it unless you pass true for the second option. In the later case this
# function has to detach the server(s) from the cluster first, otherwise it cannot be deleted
def deleteCluster(clusterName, deleteAlsoIfDependenciesExist=false):
   serverConfig()
   cd ('/')
   listOfCluster = cmo.getClusters()
	   
   # loop over the cluster mbeans and if the right one is found (or all if 'None' was provided) delete it
   edit()
   startEdit()
   already_found = 'false'
   for nextClusterInstance in listOfCluster:
      # if desired cluster is found OR no name (=None) was provided
      if ((clusterName==None) or (clusterName == nextClusterInstance.getName())):
         can_be_deleted = 'true'; 
         if (str(deleteAlsoIfDependenciesExist)=='false'):
	    # check for dependencies
	    if (clusterHasManagedServers(nextClusterInstance.getName())):
	        can_be_deleted = 'false'
                print 'Cluster '+nextClusterInstance.getName()+' still has server members - cannot delete.'
			 
	 if (can_be_deleted=='true'):
            # print name
            print 'Cluster '+nextClusterInstance.getName()+' will be destroyed !'

	    # if the cluster still has managed-servers, then detach them from the cluster first !
	    listOfManagedServer = nextClusterInstance.getServers()
	    for nextManagedServerInstance in listOfManagedServer:
	        # detach from cluster if any
                nextManagedServerInstance.setCluster(None)

	        # shutdown the cluster
	        print "Stopping the cluster " + nextClusterInstance.getName();
                try:
                   shutdown(nextClusterInstance.getName(),"Cluster");
                except:
                   pass 

		# finally delete the cluster
		delete(nextClusterInstance.getName(),'Cluster')

	        # remember that cluster was found
         already_found = 'true'

   if ((clusterName!=None) and (already_found=='false')):
       print 'Cluster '+ clusterName + ' not found'
   save()
   activate()
   serverConfig()

	
# delete a specific machine or if 'None' is passed as argument delete all machines
# note that optionally the system can check if managed-servers are still hosted on this machine.
# In this case it will not delete it unless you pass true for the second option
def deleteMachine(machineName, deleteAlsoIfDependenciesExist=false):
    serverConfig()
    cd ('/')
    listOfMachines = cmo.getMachines()
	   
    # loop over the machine mbeans and if the right one is found (or all if 'None' was provided) delete it
    edit()
    startEdit()
    already_found = 'false'
    for nextMachineInstance in listOfMachines:
       # if desired machine is found OR no name (=None) was provided
       if ((machineName==None) or (machineName == nextMachineInstance.getName())):
          can_be_deleted = 'true'; 
          if (str(deleteAlsoIfDependenciesExist)=='false'):
	      # check for dependencies
	      if (machineHostsManagedServer(nextMachineInstance.getName())):
		    can_be_deleted = 'false'
                    print 'Machine '+nextMachineInstance.getName()+' still has server members - cannot delete.'
			 
	  if (can_be_deleted=='true'):
	      # print name
              print 'Machine '+nextMachineInstance.getName()+' will be destroyed !'

	      # finally delete the machine
	      delete(nextMachineInstance.getName(),'Machine')

	      # remember that machine was found
              already_found = true

    if ((machineName!=None) and (already_found=='false')):
        print 'Machine '+ machineName + ' not found'
    save()
    activate()
    serverConfig()
	
# ================================================================
#           Main Code Execution
# ================================================================
if __name__== "main":
        print '#######################################################################';
        print '#                   Weblogic resource deletion                        #';
        print '#######################################################################';
        print 'Usage:  <user> <password> <URL> <resource-type> <None or resource name> <true/false for deleteIfDep>\n';
        wls_user = sys.argv[1]
        wls_password = sys.argv[2]
        wls_url = sys.argv[3]
	wls_typeForDeletion = sys.argv[4]
	wls_whatShouldBeDeleted = sys.argv[5]   # can be 'None'
	deleteAlsoIfDependenciesExist = sys.argv[6] # can be 'false' (should be) or 'true'
	
	# connect
	connect(wls_user, wls_password, wls_url)
		
	if (wls_whatShouldBeDeleted != 'None'):
	    whatShouldBeDeleted = wls_whatShouldBeDeleted
	else:
	    whatShouldBeDeleted = None
		
	if (wls_typeForDeletion == 'Machine'):
	    deleteMachine(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'Cluster'):
	    deleteCluster(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'ManagedServer'):
	    deleteManagedServer(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'DataSource'):
	    deleteDataSource(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'Mail'):
	    deleteMailSessions(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'JMS'):
	    deleteJMSProvider(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
	elif (wls_typeForDeletion == 'JNDI'):
	    xxx(whatShouldBeDeleted,deleteAlsoIfDependenciesExist)
        else:
            print '\nUNKOWN resource type: '+wls_typeForDeletion
