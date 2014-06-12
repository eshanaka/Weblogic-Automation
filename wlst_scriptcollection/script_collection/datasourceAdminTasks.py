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


def testDatasourceOnDestinationServer(destinationServerRuntime, datasourceName):
    # get all datasource runtime mbeans
    allDSRuntimesOnThisServer = destinationServerRuntime.getJDBCServiceRuntime().getJDBCDataSourceRuntimeMBeans()

    # loop over the runtime mbeans and if the right datasource is found test it
    already_found = false
    for datasources in allDSRuntimesOnThisServer:
        # if desired datasource is found OR no name (=None) was provided
        if ((datasourceName==None) or (datasourceName == datasources.getName())):
           # print name
           print 'Datasource name is: '+datasources.getName()
           # print state
           print 'State is ' +datasources.getState()
           # test datasource pool
           print datasources.testPool()
           # remember that DS was found
           already_found = true

    if ((datasourceName!=None) and (already_found==false)):
        print 'Datasource '+ datasourceName + ' not found on server '+destinationServerRuntime.getName()

def testDatasourceOnConnectedServer(datasourceName):
    # switch to server runtime
    serverRuntime()
    cd ('/')
    testDatasourceOnDestinationServer(cmo, datasourceName)


def testAllDatasourceOnConnectedServer():
    testDatasourceOnConnectedServer(None)


def testDatasourceOnAllServersInDomain(datasourceName):
    if (str(isAdminServer)=='true'):       
       domainConfig()
       allServers = cmo.getServers()

       domainRuntime()
       for server in allServers:
          print 'Test Datasources on server: '+server.getName()
          cd ('/ServerRuntimes/'+server.getName())
          # call testDatasourceOnDestinationServer
          testDatasourceOnDestinationServer(cmo, datasourceName)
    else:
        print 'testDatasourceOnAllServersInDomain is only available if connected to the adminserver !'
        return


def testAllDatasourceOnAllServersInDomain():
    if (str(isAdminServer)=='true'):
        testDatasourceOnAllServersInDomain(None)
    else:
        print 'testAllDatasourceOnAllServersInDomain is only available if connected to the adminserver !'


	
# lifecycleoperation can be 'start' or 'shutdown' or 'suspend' or 'resume'
def lifecycleDatasourceOnDestinationServer(destinationServerRuntime, datasourceName, lifecycleoperation):
    # get all datasource runtime mbeans
    allDSRuntimesOnThisServer = destinationServerRuntime.getJDBCServiceRuntime().getJDBCDataSourceRuntimeMBeans()

    # loop over the runtime mbeans and if the right datasource is found start it
    already_found = false
    for datasources in allDSRuntimesOnThisServer:
        # if desired datasource is found OR no name (=None) was provided
        if ((datasourceName==None) or (datasourceName == datasources.getName())):
           # print name
           print 'Datasource name is: '+datasources.getName()
           # print state
           actualState = datasources.getState()
           print 'State is: ' + actualState
		   
	   if ('start' == lifecycleoperation):
               if (actualState != 'Running'):
	           datasources.start()
               else:
                   print 'Datasource '+datasources.getName()+' is already running !'
	   elif ('shutdown' == lifecycleoperation):
               if (actualState != 'Shutdown'):
                   datasources.shutdown()
               else:
                   print 'Datasource '+datasources.getName()+' is already stopped !'
	   elif ('resume' == lifecycleoperation):
               if (actualState != 'Running'):
                   datasources.resume()
               else:
                   print 'Datasource '+datasources.getName()+' is already running !'
	   elif ('suspend' == lifecycleoperation):
               if (actualState != 'Suspended'):
                   datasources.suspend()
               else:
                   print 'Datasource '+datasources.getName()+' is already suspended !'
           else:
               print '!!!  Lifecycleoperation '+lifecycleoperation+' is not supported !'		   
		   
           # print state
           try:
               print 'State is now: ' +datasources.getState()
           except:
               pass        
           already_found = true

    if ((datasourceName!=None) and (already_found==false)):
        print 'Datasource '+ datasourceName + ' not found on server '+destinationServerRuntime.getName()


def lifecycleDatasourceOnConnectedServer(datasourceName, lifecycleoperation):
    # switch to server runtime
    serverRuntime()
    cd ('/')
    lifecycleDatasourceOnDestinationServer(cmo, datasourceName, lifecycleoperation)


def lifecycleDatasourceOnAllServersInDomain(datasourceName, lifecycleoperation):
    if (str(isAdminServer)=='true'):
       domainConfig()
       allServers = cmo.getServers()

       domainRuntime()
       for server in allServers:
          print lifecycleoperation+' all datasources on server: '+server.getName()
          try:
             cd ('/ServerRuntimes/'+server.getName())
             # call testDatasourceOnDestinationServer
             lifecycleDatasourceOnDestinationServer(cmo, datasourceName,lifecycleoperation)
          except:
             print 'Problem with server '+server.getName()
    else:
        print 'lifecycleoperationDatasourceOnAllServersInDomain('+lifecycleoperation+') is only available if connected to the adminserver !'
        return



def lifecycleAllDatasourceOnAllServersInDomain(lifecycleoperation):
    if (str(isAdminServer)=='true'):
        lifecycleDatasourceOnAllServersInDomain(None, lifecycleoperation)
    else:
        print 'lifecycleAllDatasourceOnAllServersInDomain('+lifecycleoperation+') is only available if connected to the adminserver !'




def startAllDatasourceOnAllServersInDomain():
    lifecycleAllDatasourceOnAllServersInDomain('start')

def stopAllDatasourceOnAllServersInDomain():
    lifecycleAllDatasourceOnAllServersInDomain('shutdown')

def suspendAllDatasourceOnAllServersInDomain():
    lifecycleAllDatasourceOnAllServersInDomain('suspend')

def resumeAllDatasourceOnAllServersInDomain():
    lifecycleAllDatasourceOnAllServersInDomain('resume')

	
def startDatasourceOnAllServersInDomain(datasourceName):
    lifecycleDatasourceOnAllServersInDomain(datasourceName, 'start')
	
def stopDatasourceOnAllServersInDomain(datasourceName):
    lifecycleDatasourceOnAllServersInDomain(datasourceName, 'shutdown')
	
def suspendDatasourceOnAllServersInDomain(datasourceName):
    lifecycleDatasourceOnAllServersInDomain(datasourceName, 'suspend')
	
def resumeDatasourceOnAllServersInDomain(datasourceName):
    lifecycleDatasourceOnAllServersInDomain(datasourceName, 'resume')
	
	
	
def startDatasourceOnConnectedServer(datasourceName):
    lifecycleDatasourceOnConnectedServer(datasourceName, 'start')

def stopDatasourceOnConnectedServer(datasourceName):
    lifecycleDatasourceOnConnectedServer(datasourceName, 'shutdown')

def suspendDatasourceOnConnectedServer(datasourceName):
    lifecycleDatasourceOnConnectedServer(datasourceName, 'suspend')

def resumeDatasourceOnConnectedServer(datasourceName):
    lifecycleDatasourceOnConnectedServer(datasourceName, 'resume')	
	
	
	
	
	
	
	
	
	
	
	

if __name__== "main":
        print '#####################################################################';
        print '#                   Test Datasource                                 #';
        print '#####################################################################';
        print '';
        connect('weblogic','< pw >','t3://localhost:7001')


        print isAdminServer

        #print '\n CALL: startAllDatasourceOnAllServersInDomain()\n'
        #startAllDatasourceOnAllServersInDomain()

        #print '\n CALL: stopAllDatasourceOnAllServersInDomain()\n'
        #stopAllDatasourceOnAllServersInDomain()

        #print '\n CALL: suspendAllDatasourceOnAllServersInDomain()\n'
        #suspendAllDatasourceOnAllServersInDomain()

        print '\n CALL: resumeAllDatasourceOnAllServersInDomain()\n'
        resumeAllDatasourceOnAllServersInDomain()

