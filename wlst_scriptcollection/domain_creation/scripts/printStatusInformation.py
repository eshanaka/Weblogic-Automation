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

###########################################################################
# Print monitoring information of a full domain or fron one managed-server
###########################################################################

# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File

# ... global variables
domainProps = Properties();
userConfigFile = '';
userKeyFile = '';
overwrittenServerURL =''
levelToPrint='ALL'

###################################################################
# Load properties
###################################################################
def intialize():
        global domainProps;
        global userConfigFile;
        global userKeyFile;
        global overwrittenServerURL;
	global levelToPrint;
		
        # test arguments
        if ((len(sys.argv) != 4) and (len(sys.argv) != 5)):
                print 'Usage:  printStatusInformation.sh  <default.properties_file> <property_file> <SUMMARY / ALL>';
		print 'OR'
                print 'Usage:  printStatusInformation.sh  <default.properties_file> <property_file> <SUMMARY / ALL> <ADMIN-URL overwrite>';
                exit();


        print 'Starting the initialization process';

        try:
                domainProps = Properties()

                # load DEFAULT properties
                input = FileInputStream(sys.argv[1])
                domainProps.load(input)
                input.close()


                # load properties and overwrite defaults
                input = FileInputStream(sys.argv[2])
                domainProps.load(input)
                input.close()

                userConfigFile = File(sys.argv[2]).getParent()+'/'+domainProps.getProperty('domainName')+'.userconfig'
                userKeyFile = File(sys.argv[2]).getParent()+'/'+domainProps.getProperty('domainName')+'.userkey'

		levelToPrint = sys.argv[3]
				
		if (len(sys.argv) == 5):
		    overwrittenServerURL = sys.argv[4];
				
				
        except:
                dumpStack()
                print 'Cannot load properties  !';
                exit();

        print 'Initialization completed';


###################################################################
# Connect to adminserver - wait max. 10 minutes for connection
###################################################################
def connnectToAdminServer():

         if (overwrittenServerURL==''):
             connUri = 't3://'+domainProps.getProperty('adminserver.listenAddress')+':'+ str( int( int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeListenPort'))));
         else:
	     connUri=overwrittenServerURL
			 
         print 'Connecting to the Admin Server ('+connUri+')';
         connect(userConfigFile=userConfigFile,userKeyFile=userKeyFile,url=connUri);
         print 'Connected';




def getHealthStateInformation(myState):  # is of type weblogic.health.HealthState
        if(myState.getState()==weblogic.health.HealthState.HEALTH_OK):
            return "HEALTH_OK";
        elif(myState.getState()==weblogic.health.HealthState.HEALTH_WARN):
            return "HEALTH_WARN";
        elif(myState.getState()==weblogic.health.HealthState.HEALTH_CRITICAL):
            return "HEALTH_CRITICAL";
        elif(myState.getState()==weblogic.health.HealthState.HEALTH_FAILED):
            return "HEALTH_FAILED";
        elif(myState.getState()==weblogic.health.HealthState. HEALTH_OVERLOADED):
            return "HEALTH_OVERLOADED";
        else:
            return "UNKNOWN STATE";



def printServerBasicInformation(rootDirInTree):
        cd(rootDirInTree)
        print "Basic Server Information:"

        # print Name
        print "     Name ................................... = " + get("Name")
        # add state
        print "     State .................................. = " + get("State");
        # SocketsOpenedTotalCount
        print "     SocketsOpenedTotalCount ................ = " + str(get("SocketsOpenedTotalCount"));
        # OpenSocketsCurrentCount
        print "     OpenSocketsCurrentCount ................ = " + str(get("OpenSocketsCurrentCount"));
        # AdminServer  - nothe this is BOOLEAN and indicates if this server is the admin server
        print "     isAdminServer .......................... = " + str(get("AdminServer"));
        # HealthState
        print "     HealthState ............................ = " + getHealthStateInformation(get("HealthState"));
        print("");


def printServerJTAInformation(rootDirInTree):
        # change to JTA
        cd (rootDirInTree+"JTARuntime/JTARuntime")
        print "Server Transaction Information:"

        # print Name
        print "     Name ................................... = " + get("Name")
        # print HealthState
        print "     HealthState ............................ = " + getHealthStateInformation(get("HealthState"));
        # print TransactionTotalCount
        print "     TransactionTotalCount .................. = " + str(get("TransactionTotalCount"))
        # print TransactionCommittedTotalCount
        print "     TransactionCommittedTotalCount ......... = " + str(get("TransactionCommittedTotalCount"))
        # print ActiveTransactionsTotalCount
        print "     ActiveTransactionsTotalCount ........... = " + str(get("ActiveTransactionsTotalCount"))
        # print TransactionRolledBackTotalCount
        print "     TransactionRolledBackTotalCount ........ = " + str(get("TransactionRolledBackTotalCount"))
        # print TransactionRolledBackTimeoutTotalCount
        print "     TransactionRolledBackTimeoutTotalCount . = " + str(get("TransactionRolledBackTimeoutTotalCount"))
        # print TransactionRolledBackResourceTotalCount
        print "     TransactionRolledBackResourceTotalCount  = " + str(get("TransactionRolledBackResourceTotalCount"))
        # print TransactionAbandonedTotalCount
        print "     TransactionAbandonedTotalCount ......... = " + str(get("TransactionAbandonedTotalCount"))
        # print TransactionHeuristicsTotalCount
        print "     TransactionHeuristicsTotalCount ........ = " + str(get("TransactionHeuristicsTotalCount"))
        print("");



def printServerThreadPoolInformation(rootDirInTree):
        # change to ThreadPoolRuntime
        cd (rootDirInTree+"ThreadPoolRuntime/ThreadPoolRuntime")
        print "Server ThreadPool Information:"

        # print Name
        print "     Name ................................... = " + get("Name")
        # print HealthState
        print "     HealthState ............................ = " + getHealthStateInformation(get("HealthState"));
        # print CompletedRequestCount
        print "     CompletedRequestCount .................. = " + str(get("CompletedRequestCount"))
        # print ExecuteThreadTotalCount
        print "     ExecuteThreadTotalCount ................ = " + str(get("ExecuteThreadTotalCount"))
        # print ExecuteThreadIdleCount
        print "     ExecuteThreadIdleCount ................. = " + str(get("ExecuteThreadIdleCount"))
        # print HoggingThreadCount
        print "     HoggingThreadCount ..................... = " + str(get("HoggingThreadCount"))
        # print PendingUserRequestCount
        print "     PendingUserRequestCount ................ = " + str(get("PendingUserRequestCount"))
        # print QueueLength
        print "     QueueLength ............................ = " + str(get("QueueLength"))
        # print SharedCapacityForWorkManagers
        print "     SharedCapacityForWorkManagers .......... = " + str(get("SharedCapacityForWorkManagers"))
        # print StandbyThreadCount
        print "     StandbyThreadCount ..................... = " + str(get("StandbyThreadCount"))
        # print Suspended
        print "     Suspended .............................. = " + str(get("Suspended"))
        # print Throughput
        print "     Throughput ............................. = " + str(get("Throughput"))
        print("")


def printServerJVMRuntimeInformation(servername,rootDirInTree):
        # change to JVMRuntime
        cd (rootDirInTree+"JVMRuntime/"+servername)
        print "Server JVM Information:"

        # print JavaVendor
        print "     JavaVendor ............................. = " + get("JavaVendor")
        # print JavaVersion
        print "     JavaVersion ............................ = " + get("JavaVersion")
        # print HeapFreeCurrent
        print "     HeapFreeCurrent ........................ = " + str(get("HeapFreeCurrent"))
        # print HeapFreePercent
        print "     HeapFreePercent ........................ = " + str(get("HeapFreePercent"))
        # print HeapSizeCurrent
        print "     HeapSizeCurrent ........................ = " + str(get("HeapSizeCurrent"))
        # print Uptime
        print "     Uptime ................................. = " + str(get("Uptime")/1000)+" seconds"
        print("")



def printAllDatasourceInformation(rootDirInTree):
        print "All Datasource Runtime information:"
        cd(rootDirInTree)
        dataSources = cmo.getJDBCServiceRuntime().getJDBCDataSourceRuntimeMBeans()
        if (len(dataSources) > 0):
             for dataSource in dataSources:
                 print "     Name ................................... = "+  dataSource.getName()
                 print "     State .................................. = "+  dataSource.getState()
                 print "     DeploymentState ........................ = "+  str(dataSource.getDeploymentState())
                 print "     ConnectionsTotalCount .................. = "+  str(dataSource.getConnectionsTotalCount())
                 print "     ActiveConnectionsAverageCount .......... = "+  str(dataSource.getActiveConnectionsAverageCount())
                 print "     ActiveConnectionsCurrentCount .......... = "+  str(dataSource.getActiveConnectionsCurrentCount())
                 print "     ActiveConnectionsHighCount ............. = "+  str(dataSource.getActiveConnectionsHighCount())
                 print "     ConnectionDelayTime .................... = "+  str(dataSource.getConnectionDelayTime())
                 print "     CurrCapacity ........................... = "+  str(dataSource.getCurrCapacity())
                 print "     CurrCapacityHighCount .................. = "+  str(dataSource.getCurrCapacityHighCount())
                 print "     FailedReserveRequestCount .............. = "+  str(dataSource.getFailedReserveRequestCount())
                 print "     FailuresToReconnectCount ............... = "+  str(dataSource.getFailuresToReconnectCount())
                 print "     HighestNumAvailable .................... = "+  str(dataSource.getHighestNumAvailable())
                 print "     HighestNumUnavailable .................. = "+  str(dataSource.getHighestNumUnavailable())
                 print "     LeakedConnectionCount .................. = "+  str(dataSource.getLeakedConnectionCount())
                 print "     WaitingForConnectionCurrentCount ....... = "+  str(dataSource.getWaitingForConnectionCurrentCount())
                 print "     WaitingForConnectionFailureTotal ....... = "+  str(dataSource.getWaitingForConnectionFailureTotal())
                 print "     WaitingForConnectionHighCount .......... = "+  str(dataSource.getWaitingForConnectionHighCount())
                 print "     WaitingForConnectionSuccessTotal ....... = "+  str(dataSource.getWaitingForConnectionSuccessTotal())
                 print "     WaitingForConnectionTotal .............. = "+  str(dataSource.getWaitingForConnectionTotal())
                 print "     WaitSecondsHighCount ................... = "+  str(dataSource.getWaitSecondsHighCount())
                 print("")

def printApplicationInformation(rootDirInTree):
     print('APPLICATION RUNTIME INFORMATION');
     cd(rootDirInTree)
     apps = cmo.getApplicationRuntimes();
     for app in apps:
         print '     Application: ' + app.getName()+ '    -   HealthState: ' + getHealthStateInformation(app.getHealthState());


def printServerDetails(myServerName,rootDirInTree):
     if (levelToPrint=='ALL'):
	  print "*************************************************************************"
          print "*     Details for server : " +  myServerName
          print "*************************************************************************"
          printServerBasicInformation(rootDirInTree)
          printServerJVMRuntimeInformation(myServerName,rootDirInTree)
          printServerThreadPoolInformation(rootDirInTree)
          printServerJTAInformation(rootDirInTree)
          printAllDatasourceInformation(rootDirInTree)
          printApplicationInformation(rootDirInTree)

	 
	 
def printServerSummary(outputprefix, servername, rootDirInTree):
        cd(rootDirInTree)
        print outputprefix+"Server SUMMARY Information:"

        # print Name
        print outputprefix+"     Name ................................... = " + get("Name")
        print outputprefix+"     State .................................. = " + get("State");
        print outputprefix+"     HealthState ............................ = " + getHealthStateInformation(get("HealthState"));

        cd (rootDirInTree+"JVMRuntime/"+servername)
        print outputprefix+"     HeapFreeCurrent ........................ = " + str(get("HeapFreeCurrent"))

        cd (rootDirInTree+"JTARuntime/JTARuntime")
        # print HealthState
        print outputprefix+"     JTA - HealthState ...................... = " + getHealthStateInformation(get("HealthState"));

        # change to ThreadPoolRuntime
        cd (rootDirInTree+"ThreadPoolRuntime/ThreadPoolRuntime")
        print outputprefix+"     ThreadPoolRuntime-HealthState .......... = " + getHealthStateInformation(get("HealthState"));
        # print CompletedRequestCount
        print outputprefix+"     ThreadPoolRuntime-CompletedRequestCount  = " + str(get("CompletedRequestCount"))

        cd(rootDirInTree)
        print outputprefix+"     Datasources:";
        dataSources = cmo.getJDBCServiceRuntime().getJDBCDataSourceRuntimeMBeans()
        if (len(dataSources) > 0):
             for dataSource in dataSources:
                 print outputprefix+"          Datasource-Name......................... = "+  dataSource.getName()
                 print outputprefix+"          Datasource-State........................ = "+  dataSource.getState()
        print("\n\n")


# ================================================================
#           Main Code Execution
# ================================================================
if __name__== "main":
   intialize()
   connnectToAdminServer()  
   serverRuntime();
   if (get('AdminServer')):
       # for all servers: ....
       print "Full Domain Summary !"
       print "#####################"
       domainConfig();
       managedServers=cmo.getServers()
       domainRuntime()
       for ms in managedServers:
                printServerSummary("     ",ms.getName(),"/ServerRuntimes/" + ms.getName()+"/")
       print("\n\n")

       for ms in managedServers:
                printServerDetails( ms.getName(),"/ServerRuntimes/" + ms.getName()+"/")

   else:
       # only for current server
       printServerSummary("", serverName,"/")
       print("\n\n")
       printServerDetails(serverName,"/")
	   
   disconnect()

