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
def printEJBRuntimeInformation(applicationName,ejbComponentRuntime,printDetails):
	myName = ejbComponentRuntime.getName()
	print '\nFound EJB modul '+myName+' with current deployment state = ' + getApplicationComponentState(ejbComponentRuntime)

	# now look at the submodules
	myEJBs = ejbComponentRuntime.getEJBRuntimes()
	for nextEJB in myEJBs:
		nextEjbType = nextEJB.getType()
		print ' Found EJB modul: ' + nextEJB.getName() + ' of type ' + nextEjbType
		
		if ('true' == printDetails):
			if nextEjbType == 'EntityEJBRuntime':
				# transaction information from TransactionRuntime
				myTxRuntime = nextEJB.getTransactionRuntime()
				print '    TransactionInfo: total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+' timedout:' +str(myTxRuntime.getTransactionsTimedOutTotalCount())
			elif nextEjbType == 'StatelessEJBRuntime':
				# transaction information from TransactionRuntime
				myTxRuntime = nextEJB.getTransactionRuntime()
				print '    TransactionInfo: total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+' timedout:' +str(myTxRuntime.getTransactionsTimedOutTotalCount())
				# pool information from PoolRuntime
				myPoolRuntime = nextEJB.getPoolRuntime()
				print '    PoolInfo: totalAccess:'+str(myPoolRuntime.getAccessTotalCount())+'beansInUse:'+str(myPoolRuntime.getBeansInUseCount())+'beansInUseCurrent:'+str(myPoolRuntime.getBeansInUseCurrentCount())+'destroyed:'+str(myPoolRuntime.getDestroyedTotalCount())+' idle:'+str(myPoolRuntime.getIdleBeansCount())+'pooledCurrent:'+str(myPoolRuntime.getPooledBeansCurrentCount())+' timedout:'+str(myPoolRuntime.getTimeoutTotalCount())
				# timer consists of a list of timers
				myTimerRuntime = nextEJB.getTimerRuntime()
				if myTimerRuntime != None:
					print '    Timer information: Name:'+myTimerRuntime.getName()+'activeTimers:'+str(myTimerRuntime.getActiveTimerCount())+' timeout:'+str(myTimerRuntime.getTimeoutCount())+'cancelled:'+str(myTimerRuntime.getCancelledTimerCount())+' disabled:'+str(myTimerRuntime.getDisabledTimerCount())
			elif nextEjbType == 'StatefulEJBRuntime':
				# transaction information from TransactionRuntime
				myTxRuntime = nextEJB.getTransactionRuntime()
				print '    TransactionInfo: total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+' timedout:' + str(myTxRuntime.getTransactionsTimedOutTotalCount())
				# cache information from CacheRuntime
				myCacheRuntime = nextEJB.getCacheRuntime()
				print '    CacheInfo: hits:'+str(myCacheRuntime.getCacheHitCount())+'currentBeans:'+str(myCacheRuntime.getCachedBeansCurrentCount())+' access:'+str(myCacheRuntime.getCacheAccessCount())
				# locking information from LockingRuntime
				myLockingRuntime = nextEJB.getLockingRuntime()
				print '    LockingInfo: currentCount:'+str(myLockingRuntime.getLockEntriesCurrentCount())+'accessCount:'+str(myLockingRuntime.getLockManagerAccessCount())+'timeoutTotalCount:'+str(myLockingRuntime.getTimeoutTotalCount())
			elif nextEjbType == 'MessageDrivenEJBRuntime':
				# transaction information from TransactionRuntime
				myTxRuntime = nextEJB.getTransactionRuntime()
				print '    TransactionInfo: total:'+str(myTxRuntime.getTransactionsCommittedTotalCount())+'rolledback:'+str(myTxRuntime.getTransactionsRolledBackTotalCount())+'timedout:' +str(myTxRuntime.getTransactionsTimedOutTotalCount())


connect('weblogic','< password>','t3://testserver.wlsautomation.de:7001')
domainRuntime()
cd ('/ServerRuntimes/TestDomain/ApplicationRuntimes/MyApplication/ComponentRuntimes/myejbComponent.jar/EJBRuntimes')
printEJBRuntimeInformation('MyApplication',cmo,'true')

