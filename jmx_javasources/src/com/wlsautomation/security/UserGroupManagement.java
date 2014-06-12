/**
 * WebLogic Automation Book Source Code (JMX sources)
 * 
 * This file is part of the WLS-Automation book sourcecode software distribution. 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Martin Heinzl
 * Copyright (C) 2013 MH-EnterpriseConsulting, All rights reserved.
 *
 */
package com.wlsautomation.security;

import com.wlsautomation.utils.*;
import java.util.*;
import javax.management.ObjectName;
import javax.management.Attribute;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;


public class UserGroupManagement 
{

	
/**
 * Security:Name=myrealmUserLockoutManager
         Attribute: UnlockedUsersTotalCount   of Type : java.lang.Long
         Attribute: LoginAttemptsWhileLockedTotalCount   of Type : java.lang.Long
         Attribute: LockoutDuration   of Type : java.lang.Long
         Attribute: InvalidLoginAttemptsTotalCount   of Type : java.lang.Long
         Attribute: InvalidLoginUsersHighCount   of Type : java.lang.Long
         Attribute: UserLockoutTotalCount   of Type : java.lang.Long
         Attribute: Name   of Type : java.lang.String
         Attribute: LockoutCacheSize   of Type : java.lang.Long
         Attribute: LockoutResetDuration   of Type : java.lang.Long
         Attribute: LockoutEnabled   of Type : java.lang.Boolean
         Attribute: LockedUsersCurrentCount   of Type : java.lang.Long
         Attribute: Realm   of Type : javax.management.ObjectName
         Attribute: LockoutThreshold   of Type : java.lang.Long
         Attribute: LockoutGCThreshold   of Type : java.lang.Long
         Operation: java.lang.Long  getLastLoginFailure(userName:java.lang.String  )
         Operation: java.lang.Boolean  isSet(propertyName:java.lang.String  )
         Operation: java.lang.Boolean  isLockedOut(userName:java.lang.String  )
         Operation: java.lang.Void  clearLockout(userName:java.lang.String  )
         Operation: java.lang.Long  getLoginFailureCount(userName:java.lang.String  )
         Operation: java.lang.Void  unSet(propertyName:java.lang.String  )
         Operation: java.lang.String  wls_getDisplayName()



--> Security:Name=myrealmDefaultAuthenticator
         Attribute: MaxGroupHierarchiesInCache   of Type : java.lang.Integer
         Attribute: KeepAliveEnabled   of Type : java.lang.Boolean
         Attribute: SupportedImportFormats   of Type : [Ljava.lang.String;
         Attribute: SupportedImportConstraints   of Type : [Ljava.lang.String;
         Attribute: Description   of Type : java.lang.String
         Attribute: ControlFlag   of Type : java.lang.String
         Attribute: GroupHierarchyCacheTTL   of Type : java.lang.Integer
         Attribute: PropagateCauseForLoginException   of Type : java.lang.Boolean
         Attribute: PasswordDigestEnabled   of Type : java.lang.Boolean
         Attribute: MaxGroupMembershipSearchLevel   of Type : java.lang.Integer
         Attribute: EnableGroupMembershipLookupHierarchyCaching   of Type : java.lang.Boolean
         Attribute: SupportedUserAttributeNames   of Type : [Ljava.lang.String;
         Attribute: SupportedExportFormats   of Type : [Ljava.lang.String;
         Attribute: Name   of Type : java.lang.String
         Attribute: UseRetrievedUserNameAsPrincipal   of Type : java.lang.Boolean
         Attribute: SupportedExportConstraints   of Type : [Ljava.lang.String;
         Attribute: ProviderClassName   of Type : java.lang.String
         Attribute: Realm   of Type : javax.management.ObjectName
         Attribute: GroupMembershipSearching   of Type : java.lang.String
         Attribute: Version   of Type : java.lang.String
         Attribute: MinimumPasswordLength   of Type : java.lang.Integer
         Operation: java.lang.Void  setGroupDescription(groupName:java.lang.String  description:java.lang.String  )
         Operation: java.lang.Void  changeUserPassword(userName:java.lang.String  oldPassword:java.lang.String  newPassword:java.lang.String  )
         Operation: java.lang.Void  setUserDescription(userName:java.lang.String  description:java.lang.String  )
         Operation: java.lang.String  listMemberGroups(memberUserOrGroupName:java.lang.String  )
         Operation: java.lang.Void  removeMemberFromGroup(groupName:java.lang.String  memberUserOrGroupName:java.lang.String  )
         Operation: java.lang.Boolean  groupExists(groupName:java.lang.String  )
         Operation: java.lang.String  getGroupDescription(groupName:java.lang.String  )
         Operation: java.lang.Void  advance(cursor:java.lang.String  )
         Operation: java.lang.String  getUserDescription(userName:java.lang.String  )
         Operation: java.lang.Boolean  haveCurrent(cursor:java.lang.String  )
         Operation: java.lang.String  listGroupMembers(groupName:java.lang.String  memberUserOrGroupNameWildcard:java.lang.String  maximumToReturn:java.lang.Integer  )
         Operation: java.lang.Void  unSet(propertyName:java.lang.String  )
         Operation: javax.management.openmbean.OpenType  getSupportedUserAttributeType(User:java.lang.String  )
         Operation: java.lang.Object  getUserAttributeValue(userName:java.lang.String  userAttributeName:java.lang.String  )
         Operation: java.lang.String  wls_getDisplayName()
         Operation: java.lang.Boolean  userExists(userName:java.lang.String  )
         Operation: java.lang.Void  close(cursor:java.lang.String  )
         Operation: java.lang.Boolean  isSet(propertyName:java.lang.String  )
         Operation: java.lang.Void  createGroup(groupName:java.lang.String  description:java.lang.String  )
         Operation: java.lang.String  listGroups(groupNameWildcard:java.lang.String  maximumToReturn:java.lang.Integer  )
         Operation: java.lang.Void  resetUserPassword(userName:java.lang.String  newPassword:java.lang.String  )
         Operation: java.lang.Void  createUser(userName:java.lang.String  password:java.lang.String  description:java.lang.String  )
         Operation: java.lang.Void  removeUser(userName:java.lang.String  )
         Operation: java.lang.Void  addMemberToGroup(groupName:java.lang.String  memberUserOrGroupName:java.lang.String  )
         Operation: [Ljava.lang.String;  listAllUsersInGroup(groupName:java.lang.String  userNameWildcard:java.lang.String  maximumToReturn:java.lang.Integer  )
         Operation: java.lang.Void  setUserAttributeValue(userName:java.lang.String  userAttributeName:java.lang.String  newValue:java.lang.Object  )
         Operation: java.lang.Void  importData(format:java.lang.String  filename:java.lang.String  constraints:java.util.Properties  )
         Operation: java.lang.Boolean  isMember(parentGroupName:java.lang.String  memberUserOrGroupName:java.lang.String  recursive:java.lang.Boolean  )
         Operation: java.lang.Void  removeGroup(groupName:java.lang.String  )
         Operation: java.lang.String  listUsers(userNameWildcard:java.lang.String  maximumToReturn:java.lang.Integer  )
         Operation: java.lang.Void  exportData(format:java.lang.String  filename:java.lang.String  constraints:java.util.Properties  )
         Operation: java.lang.Boolean  isUserAttributeNameSupported(User:java.lang.String  )

         Operation: java.lang.Void  advance(cursor:java.lang.String  )
         Operation: java.lang.String  getCurrentName(cursor:java.lang.String  )	
         Operation: java.lang.Boolean  haveCurrent(cursor:java.lang.String  )

 */
	
	final private static String MYREALM                    = "Security:Name=myrealm";	
	final private static String DEFAULTAUTHENTICATOR       = "Security:Name=myrealmDefaultAuthenticator";

	private ObjectName myRealm                = null;
	private ObjectName myDefaultAuthenticator = null;
	

    private JMXWrapper myJMXWrapper = null;

    public UserGroupManagement(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
		try
		{
			myRealm                = new ObjectName(MYREALM);
			myDefaultAuthenticator = new ObjectName(DEFAULTAUTHENTICATOR);
		}
		catch(Exception ex)
		{
			System.out.println("Error during initialization: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
    	
    }
    
	
	// #######################################################
	// ## Testing and information
	// #######################################################

	// # test if a user exists
	public boolean testIfUserExists(String userName) throws WLSAutomationException
	{
		try
		{
			java.lang.Boolean myValue = (java.lang.Boolean)myJMXWrapper.invoke(myDefaultAuthenticator,
					                                        "userExists",
					                                        new Object[]{userName},
			                                                new String[]{String.class.getName()});
			return myValue;
		}
		catch(Exception ex)
		{
			System.out.println("Error while testing user: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	// # test if a user exists
	public boolean testIfGroupExists(String groupName) throws WLSAutomationException
	{
		try
		{
			java.lang.Boolean myValue = (java.lang.Boolean)myJMXWrapper.invoke(myDefaultAuthenticator,
					                                        "groupExists",
					                                        new Object[]{groupName},
			                                                new String[]{String.class.getName()});
			return myValue;
		}
		catch(Exception ex)
		{
			System.out.println("Error while testing group: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	

	// # test if user is member of a group
	public boolean testIfUserIsMemberOfGroup(String groupName, String userName) throws WLSAutomationException
	{
		try
		{
			java.lang.Boolean myValue = (java.lang.Boolean)myJMXWrapper.invoke(myDefaultAuthenticator,
					                                        "isMember",
					                                        new Object[]{groupName,userName,new Boolean(true)},
			                                                new String[]{String.class.getName(),String.class.getName(),Boolean.class.getName()});
			return myValue;
		}
		catch(Exception ex)
		{
			System.out.println("Error while testing group membership: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	// #######################################################
	// # Delete basic security artefacts
	// #######################################################
	public void deleteUser(String userName) throws WLSAutomationException
	{
		try
		{
			myJMXWrapper.invoke(myDefaultAuthenticator,"removeUser",new Object[]{userName},new String[]{String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while deleting user ("+userName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	public void deleteGroup(String groupName) throws WLSAutomationException
	{
		try
		{
			myJMXWrapper.invoke(myDefaultAuthenticator,"removeGroup",new Object[]{groupName},new String[]{String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while deleting group ("+groupName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	public void removeMemberFromGroup(String groupName, String userName) throws WLSAutomationException
	{
		try
		{
			myJMXWrapper.invoke(myDefaultAuthenticator,"removeMemberFromGroup",
					            new Object[]{groupName,userName},
					            new String[]{String.class.getName(),String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while removing user ("+userName+") from group ("+groupName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	
	// #######################################################
	// # Create basic security artefacts
	// #######################################################

	// # create a new user in the default authenticator
	public void createUser(String newUserName, String newUserPassword, String newUserDescription, boolean deleteUserFirstIfExists) throws WLSAutomationException
	{
		try
		{
			if (testIfUserExists(newUserName))
			{
				if (deleteUserFirstIfExists)
				{
					System.out.println("User "+newUserName+" already exists - removing old user first !");
					deleteUser(newUserName);
				}
				else
				{
					System.out.println("User "+newUserName+" already exists - CANNOT create !");
					return ;
				}
			}
			
			myJMXWrapper.invoke(myDefaultAuthenticator,"createUser",
					new Object[]{newUserName,newUserPassword,newUserDescription},
					new String[]{String.class.getName(),String.class.getName(),String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while creating user ("+newUserName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	
	public void createGroup(String newGroupName, String newGroupDescription, boolean deleteGroupFirstIfExists) throws WLSAutomationException
	{
		try
		{
			if (testIfGroupExists(newGroupName))
			{
				if (deleteGroupFirstIfExists)
				{
					System.out.println("Group "+newGroupName+" already exists - removing old group first !");
					deleteGroup(newGroupName);
				}
				else
				{
					System.out.println("Group "+newGroupName+" already exists - CANNOT create !");
					return ;
				}
			}
			
			myJMXWrapper.invoke(myDefaultAuthenticator,"createGroup",
					new Object[]{newGroupName,newGroupDescription},
					new String[]{String.class.getName(),String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while creating group ("+newGroupName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	
	// # add a user to a group. Group membership is very important for correct security rules
	public void addUserToGroup(String userName, String groupName) throws WLSAutomationException
	{
		try
		{
	          // # check if user exists
	          if (testIfUserExists(userName)) {
	        	  System.out.println("User "+userName+" does not exist - CANNOT add "+userName+" to group "+groupName+" !");
	              return;
	          }    

	          // # check if group exists
	          if (testIfGroupExists(groupName)) {
	        	  System.out.println("Group "+groupName+" does not exist - CANNOT add "+userName+" to group "+groupName+" !");
	              return;
	          }    

	          // # check if already member
	          if (testIfUserIsMemberOfGroup(groupName, userName)) {
	        	  System.out.println("User "+userName+" is already member of group "+groupName+" !");
	              return;
	          }    

	          // # finally :-) add user to group
  			  myJMXWrapper.invoke(myDefaultAuthenticator,"addMemberToGroup",
					            new Object[]{groupName,userName},
					            new String[]{String.class.getName(),String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while adding user ("+userName+") to group ("+groupName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	
	// # change the password of a user
	public void changeUserpassword(String userName, String oldPassword, String newPassword) throws WLSAutomationException
	{
		try
		{
			if (testIfUserExists(userName))
			{
					myJMXWrapper.invoke(myDefaultAuthenticator,"changeUserPassword",
							new Object[]{userName,oldPassword,newPassword},
							new String[]{String.class.getName(),String.class.getName(),String.class.getName()});
			}		
			else
			{
					System.out.println("User "+userName+" does not exists - CANNOT change password !");
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error while changing password of user ("+userName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	
	//#######################################################
	// List Information
	//#######################################################

	
    private boolean methodExistsOnMBean(String methodName, ObjectName myMBean) throws WLSAutomationException
    {
		try
		{
	    	MBeanInfo myInfo = myJMXWrapper.getConnection().getMBeanInfo(myMBean);
	    	
	    	// get operations
	    	MBeanOperationInfo[] myOps = myInfo.getOperations();
	    	for (int i=0;i<myOps.length;i++)
	    		if (myOps[i].getName().equals(methodName))
	    			return true;
	    	
	    	// oh no
	    	return false;
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException(ex.getMessage());
		}
    }
	
	
	
	// return all user names in all authentication providers
	public ArrayList<String> returnAllUserNames() throws WLSAutomationException
	{
		ArrayList<String> resultList = new ArrayList<String>();
		
		try
		{
			ObjectName[] allAuthenticationProviders = (ObjectName[])myJMXWrapper.getAttribute(myRealm, "AuthenticationProviders");  
			
			for (int i=0;i<allAuthenticationProviders.length;i++)
			{
				if ( methodExistsOnMBean("listUsers", allAuthenticationProviders[i]))  // yes has users (listusers is inherited from UserReaderMBean)
				{
					// get cursor for user listing
					String cursor = (String)myJMXWrapper.invoke(allAuthenticationProviders[i],"listUsers",
							new Object[]{"*",new Integer(0)},
							new String[]{String.class.getName(),Integer.class.getName()});
					
					while ((Boolean)myJMXWrapper.invoke(allAuthenticationProviders[i],"haveCurrent",new Object[]{cursor},new String[]{String.class.getName()}))
					{
						// add next user to list
						resultList.add((String)myJMXWrapper.invoke(allAuthenticationProviders[i],"getCurrentName",new Object[]{cursor},new String[]{String.class.getName()}));
						
						// advance cursor
						myJMXWrapper.invoke(allAuthenticationProviders[i],"advance",new Object[]{cursor},new String[]{String.class.getName()});
					}
					// close cursor
					myJMXWrapper.invoke(allAuthenticationProviders[i],"close",new Object[]{cursor},new String[]{String.class.getName()});
				}
			}
			
			// return list of users
			return resultList;
		}
		catch(Exception ex)
		{
			System.out.println("Error while returnAllUserNames: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	
	// List all users in all authentication providers
	public void listAllUsers() throws WLSAutomationException
	{
		ArrayList<String> resultList = returnAllUserNames();
		
		System.out.println("All users of the connected domain :");
		for (int i=0;i<resultList.size();i++)
			System.out.println("    " + resultList.get(i)); 
	}
	
	
	
	// return all group names in all authentication providers
	public ArrayList<String> returnAllGroupNames() throws WLSAutomationException
	{
		ArrayList<String> resultList = new ArrayList<String>();
		
		try
		{
			ObjectName[] allAuthenticationProviders = (ObjectName[])myJMXWrapper.getAttribute(myRealm, "AuthenticationProviders");  
			
			for (int i=0;i<allAuthenticationProviders.length;i++)
			{
				if ( methodExistsOnMBean("listGroups", allAuthenticationProviders[i])) // yes has groups  (listgroups is inherited from GroupReaderMBean)
				{
					// get cursor for user listing
					String cursor = (String)myJMXWrapper.invoke(allAuthenticationProviders[i],"listGroups",
							new Object[]{"*",new Integer(0)},
							new String[]{String.class.getName(),Integer.class.getName()});
					
					while ((Boolean)myJMXWrapper.invoke(allAuthenticationProviders[i],"haveCurrent",new Object[]{cursor},new String[]{String.class.getName()}))
					{
						// add next user to list
						resultList.add((String)myJMXWrapper.invoke(allAuthenticationProviders[i],"getCurrentName",new Object[]{cursor},new String[]{String.class.getName()}));
						
						// advance cursor
						myJMXWrapper.invoke(allAuthenticationProviders[i],"advance",new Object[]{cursor},new String[]{String.class.getName()});
					}
					// close cursor
					myJMXWrapper.invoke(allAuthenticationProviders[i],"close",new Object[]{cursor},new String[]{String.class.getName()});
				}
			}
			
			// return list of groups
			return resultList;
		}
		catch(Exception ex)
		{
			System.out.println("Error while returnAllGroupNames: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}
	
	
	// List all users in all authentication providers
	public void listAllGroups() throws WLSAutomationException
	{
		ArrayList<String> resultList = returnAllGroupNames();
		
		System.out.println("All groups of the connected domain :");
		for (int i=0;i<resultList.size();i++)
			System.out.println("    " + resultList.get(i)); 
	}
	
	
	

	// List all user in the different groups in all authentication providers
	public void listUsersInGroups() throws WLSAutomationException
	{
		try
		{
			ObjectName[] allAuthenticationProviders = (ObjectName[])myJMXWrapper.getAttribute(myRealm, "AuthenticationProviders");  

			System.out.println("All user/groups available in realm: myrealm");
			for (int i=0;i<allAuthenticationProviders.length;i++)
			{
				if ( methodExistsOnMBean("listGroups", allAuthenticationProviders[i])) // yes has groups  (listgroups is inherited from GroupReaderMBean)
				{
					// get cursor for user listing
					String cursor = (String)myJMXWrapper.invoke(allAuthenticationProviders[i],"listGroups",
							new Object[]{"*",new Integer(0)},
							new String[]{String.class.getName(),Integer.class.getName()});
					
					while ((Boolean)myJMXWrapper.invoke(allAuthenticationProviders[i],"haveCurrent",new Object[]{cursor},new String[]{String.class.getName()}))
					{
						// add next user to list
						String nextGroupName = (String)myJMXWrapper.invoke(allAuthenticationProviders[i],"getCurrentName",new Object[]{cursor},new String[]{String.class.getName()});
						System.out.println("    All user available in group: "+nextGroupName);
		
						// get all users of this group
						String[] usersInGroup = (String[])myJMXWrapper.invoke(allAuthenticationProviders[i],"listAllUsersInGroup",
								new Object[]{nextGroupName,"*",new Integer(0)},
								new String[]{String.class.getName(),String.class.getName(),Integer.class.getName()});
						
						// print users
						for (int u=0;u<usersInGroup.length;u++)
							System.out.println("           User: "+usersInGroup[u]);
						
						// advance cursor
						myJMXWrapper.invoke(allAuthenticationProviders[i],"advance",new Object[]{cursor},new String[]{String.class.getName()});
					}
					// close cursor
					myJMXWrapper.invoke(allAuthenticationProviders[i],"close",new Object[]{cursor},new String[]{String.class.getName()});
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error while listUsersInGroups: "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	//#######################################################
	//# Locking / Unlocking
	//#######################################################

	// # test if a user is lockedOut
	public boolean testIfUserAccountIsLocked(String userName) throws WLSAutomationException
	{
		try
		{
			ObjectName myLockoutManager = (ObjectName)myJMXWrapper.getAttribute(myRealm, "UserLockoutManager");
			
			return (Boolean)myJMXWrapper.invoke(myLockoutManager,"isLockedOut",new Object[]{userName},new String[]{String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while testIfUserAccountIsLocked user ("+userName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

	// test if a user is lockedOut
	public void clearUserAccountLock(String userName) throws WLSAutomationException
	{
		try
		{
			ObjectName myLockoutManager = (ObjectName)myJMXWrapper.getAttribute(myRealm, "UserLockoutManager");
			
			myJMXWrapper.invoke(myLockoutManager,"clearLockout",new Object[]{userName},new String[]{String.class.getName()});
		}
		catch(Exception ex)
		{
			System.out.println("Error while clearLockout user ("+userName+"): "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	
    // list all user lockout information
	public void listAllUserLockoutInformation() throws WLSAutomationException
	{
		try
		{
			ArrayList<String> userList = returnAllUserNames();
			ObjectName myLockoutManager = (ObjectName)myJMXWrapper.getAttribute(myRealm, "UserLockoutManager");
			
			System.out.println("Lockout-Information about all user acoounts:");
			for (int i=0;i<userList.size();i++)
				System.out.println("   User:"+userList.get(i)+
						"  isLocked:"+(Boolean)myJMXWrapper.invoke(myLockoutManager,"isLockedOut",new Object[]{userList.get(i)},new String[]{String.class.getName()})
						+"  LoginFailureCount:"+(Long)myJMXWrapper.invoke(myLockoutManager,"getLoginFailureCount",new Object[]{userList.get(i)},new String[]{String.class.getName()})
						+"  LastLoginFailure:"+(Long)myJMXWrapper.invoke(myLockoutManager,"getLastLoginFailure",new Object[]{userList.get(i)},new String[]{String.class.getName()})
								);
		}
		catch(Exception ex)
		{
			System.out.println("Error while listAllUserLockoutInformation "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}


	
	// configure UserLockout Manager  -  lockoutEnabled (boolean), lockoutThreshold (int), lockoutDuration (int)
	
	// NOTE:  MUST BE CONNECTED TO THE EDIT MBEAN Tree !!!!
	public void configureUserLockoutManager(boolean lockoutEnabled, long lockoutThreshold, long lockoutDuration)  throws WLSAutomationException
	{
		try
		{
			ObjectName myLockoutManager = (ObjectName)myJMXWrapper.getAttribute(myRealm, "UserLockoutManager");
			
	        // lockout activated or not ?
		    myJMXWrapper.setAttribute(myLockoutManager, new Attribute("LockoutEnabled",new Boolean(lockoutEnabled)));

	        // lockout threshold - when gets an account locked
		    myJMXWrapper.setAttribute(myLockoutManager, new Attribute("LockoutThreshold",new Long(lockoutThreshold)));

	        // amount (in minutes) how long an account is locked
		    myJMXWrapper.setAttribute(myLockoutManager, new Attribute("LockoutDuration",new Long(lockoutDuration)));
		}
		catch(Exception ex)
		{
			System.out.println("Error while configureUserLockoutManager : "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}	
}
