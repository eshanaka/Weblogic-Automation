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

def createAnewJMSServer(jmsServerName,wlsServerName):
	# now create the server
	cd('/')
	cmo.createJMSServer(jmsServerName)
	set('Targets', jarray.array([ObjectName('com.bea:Name='+wlsServerName+',Type=Server')], ObjectName))

	# now we can define threshold values
	cmo.setBytesThresholdHigh(-1)
	cmo.setBytesThresholdLow(-1)
	cmo.setMessagesThresholdHigh(-1)
	cmo.setMessagesThresholdLow(-1)

	# now we can define quotas values
	cmo.setBytesMaximum(-1)
	cmo.setMessagesMaximum(-1)
	cmo.setBlockingSendPolicy('FIFO')
	cmo.setMaximumMessageSize(10000000)
	
	# configure the log file for this JMS server
	cd('/Deployments/'+jmsServerName+'/JMSMessageLogFile/'+jmsServerName)
	cmo.setRotationType('byTime')
	cmo.setRotateLogOnStartup(false)
	cmo.setRotationTime('00:00')
	cmo.setFileTimeSpan(24)
	cmo.setFileCount(30)
	cmo.setNumberOfFilesLimited(true)
	cmo.setFileName('/test/testapplication/'+jmsServerName+'.log')

# creating a JMS module
def createJMSModule(jmsModuleName, targetType, targetName):
	cd('/')
	cmo.createJMSSystemResource(jmsModuleName)
	cd('/SystemResources/'+jmsModuleName)
	set('Targets',jarray.array([ObjectName('com.bea:Name='+targetName+',Type='+targetType)], ObjectName))

# connectionURL = file:/var/mqm/jms ?
def createFMSForeignJMSServer(jmsModuleName,foreignName,foreignConnectionFactoryName, cfnLocalName, cfnRemoteName,connectionURL):
	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName)
	cmo.createForeignServer(foreignName)

	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName)
	cmo.setDefaultTargetingEnabled(true)

	cmo.createForeignConnectionFactory(foreignConnectionFactoryName)
	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName+'/ForeignConnectionFactories/'+foreignConnectionFactoryName)
	cmo.setLocalJNDIName(cfnLocalName)
	cmo.setRemoteJNDIName(cfnRemoteName)

	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName)
	cmo.setConnectionURL(connectionURL)
	cmo.setInitialContextFactory('de.wlsautomation.test.InitialContextFactoryWrapper')
	cmo.unSet('JNDIPropertiesCredentialEncrypted')

	cmo.createJNDIProperty('factory')
	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName+'/JNDIProperties/factory')
	cmo.setValue('com.sun.jndi.fscontext.RefFSContextFactory')

	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName)
	cmo.createJNDIProperty('SECURITY_AUTHENTICATION')
	cd('/JMSSystemResources/'+jmsModuleName+'/JMSResource/'+jmsModuleName+'/ForeignServers/'+foreignName+'/JNDIProperties/SECURITY_AUTHENTICATION')
	cmo.setValue('none')



connect('weblogic','<password>','t3://testserver.wlsautomation.de:7001')
edit()
startEdit()

# create JMS server
# createAnewJMSServer('FMS_MQ_Integration',wlsServerName)

createJMSModule('MyJMSModule, 'Cluster', 'TestCluster')
createFMSForeignJMSServer('MyJMSModule','FTestServer','FTestConfactory', 'jms/myforeignServer', 'XYZ','file:/opt/test/info')

save()
activate()

