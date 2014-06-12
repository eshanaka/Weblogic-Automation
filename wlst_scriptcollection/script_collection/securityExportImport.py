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

# export authentication data based on XACML
def exportAuthenticatorData(securityProviderName, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/AuthenticationProviders/'+securityProviderName)
	
    # export DefaultAtn type of data
    cmo.exportData('DefaultAtn',fileName,Properties())

# import authentication data based on XACML
def importAuthenticatorData(securityProviderName, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/AuthenticationProviders/'+securityProviderName)
	
    # import DefaultAtn type of data
    cmo.importData('DefaultAtn',fileName,Properties())
	
	
# export authorizer data based on XACML	
def exportAuthorizerData(securityProviderName, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/Authorizers/'+securityProviderName)
	
    # export XACML type of data
    cmo.exportData('XACML',fileName,Properties())

# import authorizer data based on XACML	
def importAuthorizerData(securityProviderName, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/Authorizers/'+securityProviderName)
	
    # import XACML type of data
    cmo.importData('XACML',fileName,Properties())

	
# export rolemapper data:  type can either bei XACML or DefaultRoles
def exportRoleMapperData(securityProviderName, exportFormat, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/RoleMappers/'+securityProviderName)
	
    # export <exportFormat> type of data
    cmo.exportData(exportFormat,fileName,Properties())

	
# import rolemapper data:  type can either bei XACML or DefaultRoles
def importRoleMapperData(securityProviderName, exportFormat, fileName):
    # cd into provider
    cd ('/DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/DefaultRealm/myrealm/RoleMappers/'+securityProviderName)
	
    # import <exportFormat> type of data
    cmo.importData(exportFormat,fileName,Properties())



connect('weblogic','< pw >','t3://test.wlsautomation.de:7001')
domainRuntime()
# export authentication data
exportAuthenticatorData("DefaultAuthenticator", "/data/data_1_DefaultAtn")

# export authorizerdata
exportAuthorizerData("XACMLAuthorizer", "/data/data_2_xacml")

# export the rolemapper data in both formats 
exportRoleMapperData("XACMLRoleMapper", "XACML", "/data/data_3_xacml")
exportRoleMapperData("XACMLRoleMapper", "DefaultRoles", "/data/data_4_DefaultRoles")

