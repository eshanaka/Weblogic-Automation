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











connect('weblogic','< password >','t3://locahost:7001')

stopAndUndeployAllApplicationsFromDomain()

