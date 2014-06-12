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
# Activate / Deactivate Admin Console
###########################################################################


# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File

pathSeparator = '/';
domainProps = Properties();
userConfigFile = '';
userKeyFile = '';
consoleAction = '';

###################################################################
# Load properties
###################################################################
def intialize():
        global domainProps;
        global userConfigFile;
        global userKeyFile;
        global consoleAction;

        # test arguments
        if len(sys.argv) != 4:
                print 'Usage:  adminConsole.sh  <default.properties_file> <property_file>  ON/OFF';
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

                consoleAction = sys.argv[3]
        except:
                print 'Cannot load properties  !';
                exit();

        print 'Initialization completed';



###################################################################
# restart the admin server
###################################################################
def restartAdminServer():
    try:
        print 'Shutting down the Admin Server...';
        shutdown(force='true', block='true');

        print 'Start the admin server using start script';
        domainLocation = domainProps.getProperty('domainsDirectory') + pathSeparator + domainProps.getProperty('domainName');
        startScript = domainLocation+ pathSeparator + "startWebLogic.sh";

        myCommand = 'nohup '+startScript+' > '+domainProps.getProperty('domainName')+'.log 2>&1 &'
        print ('The following start command will be used: '+myCommand)
        os.system(myCommand);
        # Just in case wait for 5 seconds
        java.lang.Thread.sleep(5000);
    except OSError:
        print 'Exception while rstarting the adminserver !'
        dumpStack();

		

###################################################################
#           Main Code Execution
###################################################################
if __name__== "main":

         intialize()        
         connUri = 't3://localhost:7001'

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

         if ('ON' == consoleAction):
                 # mark it for edit
                 edit()
                 # start the edit
                 startEdit()
                 # set the flag to true
                 cmo.setConsoleEnabled(true)
                 # save the changes
                 save()
                 # activate the changes
                 activate()
                 #  restart admin
                 restartAdminServer()
         elif ('OFF' == consoleAction):
                # mark it for edit
                edit()
                # start the edit
                startEdit()
                # set the flag to true
                cmo.setConsoleEnabled(false)
                # save the changes
                save()
                # activate the changes
                activate()
                # restart admin
                restartAdminServer()
         else:
                print ('Operation '+ consoleAction+ ' is not supported !!')

         print 'Disconnect from the Admin Server...';
         disconnect();

