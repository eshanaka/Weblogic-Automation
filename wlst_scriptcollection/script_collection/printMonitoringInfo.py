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
# Print monitoring information of a full domain or of one managed-server
###########################################################################

# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File

def connnectToAdminServer():

         connUri = 't3://test.wlsautomation.de:7001'
			 
         print 'Connecting to the Admin Server ('+connUri+')';
         connect('weblogic','< password >',connUri);
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


if __name__== "main":
   connnectToAdminServer()
   serverRuntime();
   if (get('AdminServer')):
       # for all servers: ....
       print "#####################"
       domainConfig();
       managedServers=cmo.getServers()
       domainRuntime()

       for ms in managedServers:
          print '\nManaged-Server: '+ms.getName()+'\n---------------------------------------------------------------\n'
          printServerJVMRuntimeInformation(ms.getName(),"/ServerRuntimes/" + ms.getName()+"/")
          printServerThreadPoolInformation("/ServerRuntimes/" + ms.getName()+"/")
          print("\n")

   else:
       # only for current server
       printServerSummary("", serverName,"/")
       print("\n\n")
       printServerDetails(serverName,"/")
	   
   disconnect()

