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
# Print ServerChannelStatus
###########################################################################

def printServerChannelStatus():
   domainConfig()
   allservers cmo.getServers()

   domainRuntime()
      
   for server in allservers:
       print 'Server channels for server: '+server
       cd ('/ServerRuntimes/'+server)
	   
	   for (channel in cmo.getServerChannelRuntimes():
	      cd ('/ServerRuntimes/'+server+'/ServerChannelRuntimes/'+channel)
	   
	      print '    Channel : ' + cmo.getName()
	      print '              AcceptCount : '+ (str(cmo.getAcceptCount())) 
	      print '              ConnectionsCount : '+ (str(cmo.getConnectionsCount())) 
	      print '              MessagesReceivedCount : '+ (str(cmo.getMessagesReceivedCount())) 
	      print '              MessagesSentCount : '+ (str(cmo.getMessagesSentCount())) 
	      print '              BytesSentCount : '+ (str(cmo.getBytesSentCount())) 
	      print '              BytesReceivedCount : '+ (str(cmo.getBytesReceivedCount())) 

	   
	   
###########################################################################
# Main
###########################################################################
if __name__== "main":	   
    connect('weblogic','test1234','localhost:7001')
    printServerChannelStatus()
	   