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

import jarray

def retargetApplicationComponent(appName, componentName, newTargetListNames, newTargetListTypes):
    # switch to domain runtime
    domainRuntime()
    
    # go to app runtime state
    cd ('/AppRuntimeStateRuntime/AppRuntimeStateRuntime')

    # check if appName is in the list of known applications 
    appIDs = cmo.getApplicationIds() # ApplicationIds - java.lang.String[vhtest]    
    if not (appName in appIDs):
       print 'PROBLEM:  Application ' + appName + ' is unknown !'
       return
       
    # get the modules of application     
    myModules = cmo.getModuleIds(appName)

    # check if module exists in application
    if not (componentName in myModules):
       print 'PROBLEM:  Module '+componentName+' does not exist in application ' + appName + ' !'
       return

    # ok application and component exist - therefore go ahead

    # test if exactly all targets are already configured
    allDone = true
    myCurrentTargets = cmo.getModuleTargets('vhtest','VH1')
    print 'Actual Targets: ' , myCurrentTargets

    for index, value in enumerate(newTargetListNames):
       print ' ... checking ' , value
       if not (value in myCurrentTargets):
          allDone = false

    # check if current target list is longer than new target list
    if len(myCurrentTargets) > len(newTargetListNames):
        allDone = false

    if allDone:
       print 'The module '+componentName+' is already targeted to the desired list of targets - no retarget necessary !'
       return     

    # ok - this means we need to retarget :-(
    
    # start edit session
    edit()
    startEdit()
    
    # go to app deployments configuration
    cd ('/AppDeployments/vhtest')
    
    # create subdeployment for module and ignore exception if already exist    
    try:
          cmo.createSubDeployment('VH1')
    except:
          print 'Ignore submodule creation exception:  please check if submodule already exists'
          
    # go to the subdeployment
    cd('SubDeployments/VH1')


    # create new array with target ObjectNames    
    targetsForDeployment = []
    
    # convert the argument lists into a Obe´jectName array
    for index, value in enumerate(newTargetListNames):
        # nextName =str('com.bea:Name='+value+',Location='+domainName+',Type='+newTargetListTypes[index])
        nextName =str('com.bea:Name='+value+',Type='+newTargetListTypes[index])
        targetsForDeployment.append(ObjectName(nextName)) 
   
    # finally set the targets
    set('Targets',jarray.array(targetsForDeployment, ObjectName))

    # save changes and activate
    save()
    activate()   
    
    # back to domain runtime
    domainRuntime()      
      
    print 'Retarget of '+componentName+' is done !'


if __name__== "main":
    # connect to the server
    connect('weblogic','< pw >','t3://test-wlsautomation.de:7001')

    # retarget
    retargetApplicationComponent('vhtest', 'VH1', ['MyPrivateVirtualHost1','MyPrivateVirtualHost2'], ['VirtualHost','VirtualHost'])

    print '\nScript has finished successfully '

