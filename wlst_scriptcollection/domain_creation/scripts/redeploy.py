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
# Redeploy all artefacts of a domain
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
                print 'Usage:  redeploy.sh  <default.properties_file> <property_file>';
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
# Get instance properties
###################################################################
def get_instance_property(instancetype, instanceNumber, propName):
        # read PARAMETER;  if PARAMETER="" try to overwrite with default. Note that the default parameter has "x" instead of number
        concreateValue = domainProps.getProperty(instancetype+'.'+instanceNumber+'.'+propName,"");
        if len(concreateValue) == 0:
                concreateValue = domainProps.getProperty(instancetype+'.x.'+propName,"");  # read default !!

        return concreateValue;



###################################################################
# Connect to adminserver - wait max. 10 minutes for connection
###################################################################
def connnectToAdminServer():
     
	 connUri = 't3://'+domainProps.getProperty('adminserver.listenAddress')+':'+ str( int( int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeListenPort'))));

	 print 'Connecting to the Admin Server ('+connUri+')';
         connect(userConfigFile=userConfigFile,userKeyFile=userKeyFile,url=connUri);
         print 'Connected';

		

def getNamesOfAllDeployedApplication():
    listOfDeployedApplicationNames = []

    # get all installed applications
    allInstalledApplicatons=cmo.getAppDeployments()

    # iterator over those applications and compare the name with the provided name
    for nextApplication in allInstalledApplicatons:
            listOfDeployedApplicationNames.append(nextApplication.getName())

    # return list
    return listOfDeployedApplicationNames




def stopAndUndeployAllApplicationsFromDomain():
    # go to the domain config tree
    domainConfig()

    # get list of applications
    myApplicationNames = getNamesOfAllDeployedApplication()

    # go back to domainRuntime
    domainRuntime()

    # iterator over list and undeploy
    for nextApplication in myApplicationNames:
        print 'Stop application '+nextApplication
        # stopApplication(appName, [block], [timeout])
        progress=stopApplication(nextApplication, block='true')
        print 'Now undeploy application '+nextApplication
        # undeploy(appName, [targets], [block], [subModuleTargets], [timeout])
        progress=undeploy(nextApplication, block='true')
        print 'Finished\n'


###################################################################
# deploy all applications
###################################################################
def deployAllApplications():
    try:
        amountOfDeployments=domainProps.get("amountDeployments")
        print 'Am:',amountOfDeployments

        print 'Deploy all applications ....'
        i=1
        while (i <= int(amountOfDeployments)) :
            try:
                                applicationName        = get_instance_property('deployment',str(i),'applicationname');
                                sourcepath             = get_instance_property('deployment',str(i),'sourcepath');
                                deploymentOrderSetting = get_instance_property('deployment',str(i),'deploymentOrder');
                                targetsSetting         = get_instance_property('deployment',str(i),'targets');

                                print 'Start deployment of '+applicationName+' ('+sourcepath+') ';
                                deploy(appName=applicationName, path=sourcepath, targets=targetsSetting, upload='true');

            except:
                print '***** deployment of '+applicationname+' ('+sourcepath+') FAILED !';
                print ''
            i = i + 1

    except:
        print 'Exception while deploying all applications !';
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
        print '##################################################################';
        print '#                  (Re)-Deploy all deployments                   #';
        print '##################################################################';
        print '';
        intialize();
        connnectToAdminServer();
        stopAndUndeployAllApplicationsFromDomain()
        deployAllApplications();
        disconnectFromAdminserver();

# =========== End Of Script ===============

