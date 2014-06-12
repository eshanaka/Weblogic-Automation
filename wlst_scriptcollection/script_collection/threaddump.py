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
# Print Threaddump
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
        if ((len(sys.argv) != 3) and (len(sys.argv) != 4)):
                print 'Usage:  threaddump.py <default.properties_file> <property_file>';
                print 'OR'
                print 'Usage:  threaddump.py <default.properties_file> <property_file> <ADMIN-URL overwrite>';
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

                if (len(sys.argv) == 4):
                    overwrittenServerURL = sys.argv[3];


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
             connUri = 't3://locahost:7001';
         else:
             connURI=overwrittenServerURL

         print 'Connecting to the Admin Server ('+connURI+')';
         connect(userConfigFile=userConfigFile,userKeyFile=userKeyFile,url=connURI);
         print 'Connected';



if __name__== "main":
   intialize()
   connnectToAdminServer()
   
   threadDump()
   
   disconnect()

