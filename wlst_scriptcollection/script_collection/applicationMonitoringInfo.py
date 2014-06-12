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

def getHealthStateInformation(myState):  
    # is of type weblogic.health.HealthState
    if(myState.getState()==weblogic.health.HealthState.HEALTH_OK):
             return "HEALTH_OK";
    elif(myState.getState()==weblogic.health.HealthState.HEALTH_WARN):
             return "HEALTH_WARN";
    elif(myState.getState()==weblogic.health.HealthState.HEALTH_CRITICAL):
             return "HEALTH_CRITICAL";
    elif(myState.getState()==weblogic.health.HealthState.HEALTH_FAILED):
             return "HEALTH_FAILED";
    elif(myState.getState()==weblogic.health.HealthState.HEALTH_OVERLOADED):
             return "HEALTH_OVERLOADED";
    else:
             return "UNKNOWN STATE";

# print state
def printApplicationComponentState(applicationname, componentRuntime):
     myDeploymentState = componentRuntime.getDeploymentState()
     myComponentName   = componentRuntime.getName()

     if myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.UNPREPARED:
        print 'Status of component '+myComponentName+' of application '+applicationname+'  = UNPREPARED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.PREPARED:
        print 'Status of component '+myComponentName+' of application '+applicationname+'  = PREPARED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.ACTIVATED:
        print 'Status of component '+myComponentName+' of application '+applicationname+'  = ACTIVATED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.NEW:
        print 'Status of component '+myComponentName+' of application '+applicationname+'  = NEW'


# return state as string
def getApplicationComponentState(componentRuntime):
     myDeploymentState = componentRuntime.getDeploymentState()

     if myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.UNPREPARED:
        return 'UNPREPARED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.PREPARED:
        return 'PREPARED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.ACTIVATED:
        return 'ACTIVATED'
     elif myDeploymentState == weblogic.management.runtime.ComponentRuntimeMBean.NEW:
        return 'NEW'


		
# print EJB information
def printEJBRuntimeInformation(applicationName,ejbComponentRuntime):
     myName = ejbComponentRuntime.getName()
     print '\nFound EJB modul '+myName+' with current deployment state = ' + getApplicationComponentState(ejbComponentRuntime)

     # now look at the submodules
     myEJBs = ejbComponentRuntime.getEJBRuntimes()
     for nextEJB in myEJBs:
         nextEjbType = nextEJB.getType()
         print '    Found EJB modul: ' + nextEJB.getName() + ' of type ' + nextEjbType
         if nextEjbType == 'EntityEJBRuntime':
             # transaction information from TransactionRuntime
	     myTxRuntime = nextEJB.getTransactionRuntime()
             print '       TransactionInfo:  total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'   rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+'   timedout:' + str(myTxRuntime.getTransactionsTimedOutTotalCount())
		 
         elif nextEjbType == 'StatelessEJBRuntime':
             # transaction information from TransactionRuntime
	     myTxRuntime = nextEJB.getTransactionRuntime()
             print '       TransactionInfo:  total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'   rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+'   timedout:' + str(myTxRuntime.getTransactionsTimedOutTotalCount())
             # pool information from PoolRuntime
	     myPoolRuntime = nextEJB.getPoolRuntime()
             print '       PoolInfo:  totalAccess:'+str(myPoolRuntime.getAccessTotalCount())+'    beansInUse:'+str(myPoolRuntime.getBeansInUseCount())+'    beansInUseCurrent:'+str(myPoolRuntime.getBeansInUseCurrentCount())+'    destroyed:'+str(myPoolRuntime.getDestroyedTotalCount())+'    idle:'+str(myPoolRuntime.getIdleBeansCount())+'   pooledCurrent:'+str(myPoolRuntime.getPooledBeansCurrentCount())+'    timedout:'+str(myPoolRuntime.getTimeoutTotalCount())

             # timer consists of a list of timers			 
	     myTimerRuntime = nextEJB.getTimerRuntime()
	     if myTimerRuntime != None:
	         print '       Timer information:  Name:'+myTimerRuntime.getName()+'    activeTimers:'+str(myTimerRuntime.getActiveTimerCount())+'   timeout:'+str(myTimerRuntime.getTimeoutCount())+'    cancelled:'+str(myTimerRuntime.getCancelledTimerCount())+'   disabled:'+str(myTimerRuntime.getDisabledTimerCount())
         elif nextEjbType == 'StatefulEJBRuntime':
             # transaction information from TransactionRuntime
	     myTxRuntime = nextEJB.getTransactionRuntime()
             print '       TransactionInfo:  total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'   rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+'   timedout:' + str(myTxRuntime.getTransactionsTimedOutTotalCount())
	     # cache information from CacheRuntime
	     myCacheRuntime = nextEJB.getCacheRuntime()
             print '       CacheInfo:  hits:'+str(myCacheRuntime.getCacheHitCount())+'   currentBeans:'+str(myCacheRuntime.getCachedBeansCurrentCount())+'   access:'+str(myCacheRuntime.getCacheAccessCount())
	     # locking information from LockingRuntime
	     myLockingRuntime = nextEJB.getLockingRuntime()
             print '       LockingInfo:  currentCount:'+str(myLockingRuntime.getLockEntriesCurrentCount())+'   accessCount:'+str(myLockingRuntime.getLockManagerAccessCount())+'   timeoutTotalCount:'+str(myLockingRuntime.getTimeoutTotalCount())
		 
         elif nextEjbType == 'MessageDrivenEJBRuntime':
             # transaction information from TransactionRuntime
	     myTxRuntime = nextEJB.getTransactionRuntime()
             print '       TransactionInfo:  total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'   rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+'   timedout:' + str(myTxRuntime.getTransactionsTimedOutTotalCount())



def printWebAppRuntimeInformation(applicationName,webAppComponentRuntime):
     myName = webAppComponentRuntime.getName()
     print '\nFound WEB modul '+myName+' with current deployment state = ' + getApplicationComponentState(webAppComponentRuntime)
     print '         Sessions:  currentOpen:'+str(webAppComponentRuntime.getOpenSessionsCurrentCount())+'     highCount:'+str(webAppComponentRuntime.getOpenSessionsHighCount())+'    openedTotal:'+str(webAppComponentRuntime.getSessionsOpenedTotalCount())
 
     # now look at the submodules
     print '   Servlet overview:'
     myServlets = webAppComponentRuntime.getServlets()
     for nextServlet in myServlets:
         print '      Servlet: ' + nextServlet.getServletName() + ', amount invocation: ' + str(nextServlet.getInvocationTotalCount())
         print '               invocation time:  average: ' + str(nextServlet.getExecutionTimeAverage()) + ' high: ' + str(nextServlet.getExecutionTimeHigh()) + '  low: ' + str(nextServlet.getExecutionTimeLow()) + '  total time: ' + str(nextServlet.getExecutionTimeTotal())



def printConnectorRuntimeInformation(applicationName,connectorComponentRuntime):
     myName = connectorComponentRuntime.getName()
     print '\nFound JCA modul '+myName+' with current state='+connectorComponentRuntime.getState()+'  and deployment state = ' + getApplicationComponentState(connectorComponentRuntime)
     
     # connection pools
     myConPools = connectorComponentRuntime.getConnectionPools()
     for myNextCon in myConPools:
         print 'ConncetionPool: Name='+myNextCon.getName()+'   State='+myNextCon.getState()+'   testable='+str(myNextCon.isTestable())+'     TransactionSupport='+myNextCon.getTransactionSupport()
         print '                ActiveCurrentCount='+str(myNextCon.getActiveConnectionsCurrentCount())+'    ActiveHighCount='+str(myNextCon.getActiveConnectionsHighCount())+'    FreeCurrentCount='+str(myNextCon.getFreeConnectionsCurrentCount())+'    FreeHighCount='+str(myNextCon.getFreeConnectionsHighCount())+'   AverageActiveUsage='+str(myNextCon.getAverageActiveUsage())

		 

	 
connect('weblogic','<password','t3://localhost:7001');
serverRuntime()
apps = cmo.getApplicationRuntimes();
for app in apps:
    print 'Application: ' + app.getName()
    crs = app.getComponentRuntimes();
    for cr in crs:
        if (cr.getType() == 'EJBComponentRuntime'):
            printEJBRuntimeInformation(app.getName(),cr)
        if (cr.getType() == 'WebAppComponentRuntime'):
            printWebAppRuntimeInformation(app.getName(),cr)
        if (cr.getType() == 'ConnectorComponentRuntime'):
            printConnectorRuntimeInformation(app.getName(),cr)


