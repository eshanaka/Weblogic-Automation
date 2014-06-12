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

from weblogic.management.security.authentication import UserReaderMBean
from weblogic.management.security.authentication import GroupReaderMBean
from weblogic.management.security.authentication import UserLockoutManagerMBean
#from weblogic.management.security.authentication import UserLockoutManagerMBeanserverRuntime

#######################################################	
# PART-1:   List Information	
#######################################################	

# List all users in all authentication providers 
def listAllUsers():
    try:
      cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
      allAuthenticationProviders = cmo.getAuthenticationProviders()
      
      print 'List all Users in all Authentication Providers:'
      for provider in allAuthenticationProviders:
            if isinstance(provider,UserReaderMBean):
                  nextUserAuthenticationProvider = provider
		  print 'All users available in provider:'+provider.getName()+' in realm: myrealm'
                  cursor = provider.listUsers("*",0)
                  while nextUserAuthenticationProvider.haveCurrent(cursor):
                        print '   User: ' + nextUserAuthenticationProvider.getCurrentName(cursor)
                        nextUserAuthenticationProvider.advance(cursor)
                  nextUserAuthenticationProvider.close(cursor)
    except:
      dumpStack()
      print "Error in listAllUsers"

	  
# return all user names in all authentication providers 
def returnAllUserNames():
    try:
      userList = []
      cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
      allAuthenticationProviders = cmo.getAuthenticationProviders()
      for provider in allAuthenticationProviders:
            if isinstance(provider,UserReaderMBean):
                  nextUserAuthenticationProvider = provider
				  
                  cursor = provider.listUsers("*",0)
                  while nextUserAuthenticationProvider.haveCurrent(cursor):
		        userList.append(nextUserAuthenticationProvider.getCurrentName(cursor))
                        nextUserAuthenticationProvider.advance(cursor)
                  nextUserAuthenticationProvider.close(cursor)
      return userList
    except:
      dumpStack()
      print "Error in returnAllUserNames"
	  

# List all groups in all authentication providers 
def listAllGroups():
    try:
      cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
      allAuthenticationProviders = cmo.getAuthenticationProviders()

      print 'List all groups in all Authentication Providers:'
      for provider in allAuthenticationProviders:
            if isinstance(provider,GroupReaderMBean):
                  nextGroupAuthenticationProvider = provider
		  print 'All groups available in provider:'+provider.getName()+' in realm: myrealm'
				  
                  cursor = provider.listGroups("*",0)
                  while nextGroupAuthenticationProvider.haveCurrent(cursor):
                        print '   Group: ' + nextGroupAuthenticationProvider.getCurrentName(cursor)
                        nextGroupAuthenticationProvider.advance(cursor)
                  nextGroupAuthenticationProvider.close(cursor)
    except:
      dumpStack()
      print "Error in listAllGroups"

	  
# return all group names in all authentication providers 
def returnAllGroupNames():
    try:
      groupList = []
      cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
      allAuthenticationProviders = cmo.getAuthenticationProviders()
      for provider in allAuthenticationProviders:
            if isinstance(provider,GroupReaderMBean):
                  nextGroupAuthenticationProvider = provider
				  
                  cursor = provider.listGroups("*",0)
                  while nextGroupAuthenticationProvider.haveCurrent(cursor):
                        groupList.append(nextGroupAuthenticationProvider.getCurrentName(cursor))
                        nextGroupAuthenticationProvider.advance(cursor)
                  nextGroupAuthenticationProvider.close(cursor)
      return groupList
    except:
      dumpStack()
      print "Error in returnAllGroupNames"
	  
	  

# List all user in the different groups in all authentication providers 
def listUsersInGroups():
    try:
      cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
      allAuthenticationProviders = cmo.getAuthenticationProviders()

      print 'List users in all groups in all Authentication Providers:'
      for provider in allAuthenticationProviders:
            if isinstance(provider,GroupReaderMBean):
                  nextGroupAuthenticationProvider = provider
		  print 'All user/groups available in provider:'+provider.getName()+' in realm: myrealm'
				  
                  cursor = provider.listGroups("*",0)
                  while nextGroupAuthenticationProvider.haveCurrent(cursor):
                        nextGroup = nextGroupAuthenticationProvider.getCurrentName(cursor)    
                        usersInActualGroup = provider.listAllUsersInGroup(nextGroup,"*",0) 

		        print '   Group: ' + nextGroupAuthenticationProvider.getCurrentName(cursor)
			for nextUser in usersInActualGroup: 
				print '          User: '+nextUser
                        nextGroupAuthenticationProvider.advance(cursor)
                  nextGroupAuthenticationProvider.close(cursor)
    except:
      dumpStack()
      print "Error in listUsersInGroups"
	  
#######################################################	
# PART-2:   Create basic security artefacts
#######################################################	
 
# create a new user in the default authenticator 
def createUser(newUserName, newUserPassword, newUserDescription, deleteUserFirstIfExists): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  if (cmo.userExists(newUserName)):
	      if (deleteUserFirstIfExists):
		      print 'User '+newUserName+' already exists - removing old user first !'
		      removeUser(newUserName)
	      else:
		      # cannot create !!
		      print 'User '+newUserName+' already exists - CANNOT create !'
		      return
      
	  # create user 	  
	  cmo.createUser(newUserName, newUserPassword, newUserDescription)
    except:
	  dumpStack()
 
# create a new group in the default authenticator 
def createGroup(newGroupName, newGroupDescription, deleteGroupFirstIfExists): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  if (cmo.groupExists(newGroupName)):
	      if (deleteGroupFirstIfExists):
		      print 'Group '+newGroupName+' already exists - removing old group first !'
		      removeGroup(newGroupName)
	      else:
		      # cannot create !!
		      print 'Group '+newGroupName+' already exists - CANNOT create !'
	              return
      
	  # create group 	  
	  cmo.createGroup(newGroupName, newGroupDescription)

    except:
	  dumpStack()

 
# add a user to a group. Group membership is very important for correct security rules 
def addUserToGroup(userName, groupName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  # check if user exists
	  if (cmo.userExists(userName)==0):
	      print 'User '+userName+' does not exist - CANNOT add '+userName+' to group '+groupName+' !'
	      return

	  # check if group exists
	  if (cmo.groupExists(groupName)==0):
	      print 'Group '+groupName+' does not exist - CANNOT add '+userName+' to group '+groupName+' !'
	      return
      
	  # check if already member
          if (cmo.isMember(grouName,userName,true)):
	      print 'User '+userName+' is already member of group '+groupName+' !'
	      return
	  
	  # finally :-) add user to group
	  cmo.addMemberToGroup(groupName, userName)
	  
    except:
	  dumpStack()


# change the password of a user
def changeUserpassword(userName, oldPassword, newPassword): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  # check if user exists
	  if (userExists(userName)==0):
	      print 'User '+userName+' does not exist - CANNOT change password !'
	      return

	  # change the password
	  cmo.changeUserPassword(userName, oldPassword, newPassword)
          print "Changed password of user '+userName+' successfully"	  
    except:
	  dumpStack()
	  

#######################################################	
# PART-3:   Testing and information
#######################################################	
 
# test if a user exists
def testIfUserExists(userName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  return userExists(newUserName)
    except:
	  dumpStack()

# test if a group exists
def testIfGroupExists(groupName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  return groupExists(groupName)
    except:
	  dumpStack()
	  

# test if user is member of a group
def testIfUserIsMemberOfGroup(groupName, userName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/AuthenticationProviders/DefaultAuthenticator')

	  return cmo.isMember(grouName,userName,true)
    except:
	  dumpStack()
	  

#######################################################	
# PART-4:   Locking / Unlocking
#######################################################	
 
# test if a user is lockedOut
def testIfUserAccountIsLocked(userName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
	  myLockoutManager = cmo.getUserLockoutManager()

	  return myLockoutManager.isLockedOut(userName)
    except:
	  dumpStack()


# test if a user is lockedOut
def clearUserAccountLock(userName): 
    try:
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
	  myLockoutManager = cmo.getUserLockoutManager()

	  myLockoutManager.clearLockout(userName)
	  print 'User account '+userName+' was unlocked !'
    except:
	  dumpStack()
	  
 
 
# list all user information
def listAllUserLockoutInformation(): 
    try:  
	  alluserNames = returnAllUserNames()
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm')
	  myLockoutManager = cmo.getUserLockoutManager()
          
	  print 'Lockout-Information about all user acoounts:'
	  for nextUser in alluserNames: 
		print '   User:'+nextUser+'  isLocked:'+str(myLockoutManager.isLockedOut(nextUser))+'  LoginFailureCount:'+str(myLockoutManager.getLoginFailureCount(nextUser))+'  LastLoginFailure:'+str(myLockoutManager.getLastLoginFailure(nextUser))
    except:
	  dumpStack()
 
 
# configure UserLockout Manager  -  lockoutEnabled (boolean), lockoutThreshold (int), lockoutDuration (int)
def configureUserLockoutManager(lockoutEnabled, lockoutThreshold, lockoutDuration): 
    try:
	  edit()
	  startEdit()
	  cd ('/')
	  ulm=cmo.getSecurityConfiguration().getDefaultRealm().getUserLockoutManager()
	  
	  # lockout activated or not ?
	  ulm.setLockoutEnabled(lockoutEnabled)
	  
	  # lockout threshold - when gets an account locked
	  ulm.setLockoutThreshold(lockoutThreshold)
	  
	  # amount (in minutes) how long an account is locked
	  ulm.setLockoutDuration(lockoutDuration)
	  
	  save()
	  activate()	
    except:
	  dumpStack()


# list all user information
def listUserLockoutManagerConfiguration(): 
    try:  
	  cd('/SecurityConfiguration/'+domainName+'/Realms/myrealm/UserLockoutManager/UserLockoutManager')

	  print 'Actual lockout-information from the domain '+domainName+' :'
	  print '   LockoutEnabled                       : ', get('LockoutEnabled')
	  print '   InvalidLoginAttemptsTotalCount       : ',cmo.getInvalidLoginAttemptsTotalCount()
	  print '   InvalidLoginUsersHighCount           : ',cmo.getInvalidLoginUsersHighCount()
	  print '   LockedUsersCurrentCount              : ',cmo.getLockedUsersCurrentCount()
	  print '   LockoutCacheSize                     : ',cmo.getLockoutCacheSize()
	  print '   LockoutDuration                      : ',cmo.getLockoutDuration()
	  print '   LockoutGCThreshold                   : ',cmo.getLockoutGCThreshold()
	  print '   LockoutResetDuration                 : ',cmo.getLockoutResetDuration()
	  print '   LockoutThreshold                     : ',cmo.getLockoutThreshold()
	  print '   LoginAttemptsWhileLockedTotalCount   : ',cmo.getLoginAttemptsWhileLockedTotalCount()
	  print '   UnlockedUsersTotalCount              : ',cmo.getUnlockedUsersTotalCount()
	  print '   UserLockoutTotalCount                : ',cmo.getUserLockoutTotalCount()

    except:
	  dumpStack()


 
# ================================================================
#           Main Code Execution
# ================================================================
if __name__== "main":
        connect('weblogic','<password>','t3://localhost:7001')
        #serverRuntime()
	print '\n\n'
	listAllUsers()
	print '\n\n'
	listAllGroups()
	print '\n\n'
        listUsersInGroups()
	print '\n\n'
	listAllUserLockoutInformation()
	print '\n\n'
	listUserLockoutManagerConfiguration()
