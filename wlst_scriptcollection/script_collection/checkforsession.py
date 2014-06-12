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
# Check for session
###########################################################################

# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File




###################################################################
# check if session is active
###################################################################
def existActiveEditSession():
    # check for existing sessions using the configuration manager
    myConfigurationManager = getConfigManager()
    # try to get an active session.  If call succeeds, then a session is available, otherwise an exception is thrown
    try:
	   # Test if an active session is available and if yes return it
           myConfigurationManager.getChanges()
           print 'Active edit session found !'
       
	   # test which user is making changes
	   userWhoOwnsCurrentEditSession = myConfigurationManager.getCurrentEditor()
	   print '   - The active session belongs to ' + userWhoOwnsCurrentEditSession
	   
	   # out of interst, return the number of active changes in the current session
           changeList = myConfigurationManager.getUnactivatedChanges()
           numberOfUnactivatedChanges = len(changeList)
           print '   - The actual session has '+str(numberOfUnactivatedChanges)+' not yet activated changes '

           print '\n Waiting list of changes:'
           if (numberOfUnactivatedChanges > 0):
                 for nextChange in changeList:
                      print nextChange
           print '\n'


	   
	   return 'true'
    except: 
           dumpStack()
	   # good for us - no active changes found
	   print 'No active edit session found !'
	   return 'false'


###################################################################
#           Main Code Execution
###################################################################
if __name__== "main":

         intialize()
         connUri = 't3://test.wlsautomation.de:7001');
         print 'Connecting to the Admin Server ('+connUri+')';
         connect('weblogic','< password >',url=connUri);

	 if ('true' == existActiveEditSession()):
	     print '\n OH OH session already exists\n'
         else:
	     print '\n Good news - session does not yet exist !\n'

         print 'Disconnect from the Admin Server...';
         disconnect();
