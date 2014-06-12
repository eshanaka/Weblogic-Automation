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
# Start all managed servers of a domain
###########################################################################

# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File


domainProps = Properties();
userConfigFile = '';
userKeyFile = '';

###################################################################
# Load properties 
###################################################################
def intialize():
        global domainProps;
        global userConfigFile;
        global userKeyFile;

        # test arguments
        if len(sys.argv) != 3:
                print 'Usage:  startAllManagedServers.sh  <default.properties_file> <property_file>';
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

        except:
                print 'Cannot load properties  !';
                exit();

        print 'Initialization completed';




###################################################################
# Connect to adminserver - wait max. 10 minutes for connection
###################################################################
def connnectToAdminServer():
     
	 connUri = 't3://'+domainProps.getProperty('adminserver.listenAddress')+':'+ str( int( int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeListenPort'))));

	 currentcount = 0;
	 adminServerIsRunning = 'false';

	 while ((adminServerIsRunning=='false')  and (currentcount<30)):
	       try:
			print 'Connecting to the Admin Server ('+connUri+')';
                        connect(userConfigFile=userConfigFile,userKeyFile=userKeyFile,url=connUri);
			print 'Connected';
			adminServerIsRunning = 'true';
	       except:
			print 'AdminServer is (not yet) running. Will wait for 10sec.';
			java.lang.Thread.sleep(10000);
			currentcount = currentcount +1;

	 if (adminServerIsRunning=='false'):
	        print 'Could not connect to admin server - script will be exit !'
		exit();


		


###################################################################
# get Server status
###################################################################
def getMSserverStatus(server): 
        try:
             cd('/ServerLifeCycleRuntimes/' +server) 
        except:
             print 'oh ohohohoh';
             dumpStack();
	return cmo.getState() 

		
###################################################################
# Start all managed server
###################################################################
def startAllManagedServers():
    try:
		print 'Loop through the managed servers and start all servers ';
		domainConfig() 
		svrs = cmo.getServers()
		
		domainRuntime()
		for server in svrs:
			# Do not start the adminserver, it's already running
			if server.getName() != 'AdminServer':
				# Get state and machine
				serverState = getMSserverStatus(server.getName())
				print server.getName() + " is " + serverState 
				# startup if needed
                                if (serverState == "SHUTDOWN") or (serverState=="FAILED_NOT_RESTARTABLE"):
					   print "Starting " + server.getName();
				           start(server.getName(),'Server')
					   serverState = getMSserverStatus(server.getName())
				           print "Now " + server.getName() + " is " + serverState;
    except:
        print 'Exception while starting managed servers !';
        dumpStack();

		

###################################################################
# disconnect from  adminserver
###################################################################
def disconnectFromAdminserver():
        print 'Disconnect from the Admin Server...';
        disconnect();




# ================================================================
#           Main Code Execution
# ================================================================
if __name__== "main":
        print '###################################################################################';
        print '#                   Start all managed server of the domain                        #';
        print '###################################################################################';
        print '';
        intialize();
        connnectToAdminServer();
        startAllManagedServers();
        disconnectFromAdminserver();

# =========== End Of Script ===============

