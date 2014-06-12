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
package com.wlsautomation.utils;


import javax.management.ObjectName;


public class EditSessionUtils {

    private JMXWrapper myJMXWrapper = null;

    public EditSessionUtils(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }
	
	
    public boolean isEditSessionActive() throws WLSAutomationException
	{
    	boolean sessionFound = false;
		try
		{  
			ObjectName myConfigurationManager = new ObjectName("com.bea:Name=ConfigurationManager,Type=weblogic.management.mbeanservers.edit.ConfigurationManagerMBean");
			
			
			System.out.println("Changes...........: "+myJMXWrapper.getAttribute(myConfigurationManager, "Changes").getClass().getName());
			System.out.println("UnactivatedChanges: "+myJMXWrapper.getAttribute(myConfigurationManager, "UnactivatedChanges").getClass().getName());
			
			
			
			// try to get an active session. If call succeeds, then a session is available, otherwise an exception is thrown
			javax.management.openmbean.CompositeData[] changes = 
					(javax.management.openmbean.CompositeData[])myJMXWrapper.getAttribute(myConfigurationManager, "UnactivatedChanges");
			

			if (changes != null && changes.length>0)
			{
				// changes found
				System.out.println("Active edit session found !");
				
				// who is doing the changes
				String userWhoOwnsCurrentEditSession = (String)myJMXWrapper.getAttribute(myConfigurationManager, "CurrentEditor");
				System.out.println("   - The actual session is changed by "+userWhoOwnsCurrentEditSession+" !");
				

				// number of changes waiting
				System.out.println("   - The actual session has "+changes.length+" not yet activated changes ");

				
				sessionFound = true;
			}
			else
			{
				System.out.println("No active changes found !");
				sessionFound = false;
			}

			return sessionFound;


/**
 * --> com.bea:Name=ConfigurationManager,Type=weblogic.management.mbeanservers.edit.ConfigurationManagerMBean
         Attribute: CurrentEditorExpired   of Type : java.lang.Boolean
         Attribute: CurrentEditorExclusive   of Type : java.lang.Boolean
         Attribute: Editor   of Type : java.lang.Boolean
         Attribute: ParentAttribute   of Type : java.lang.String
         Attribute: Changes   of Type : javax.management.openmbean.ArrayType
         Attribute: CurrentEditorExpirationTime   of Type : java.lang.Long
         Attribute: CurrentEditor   of Type : java.lang.String
         Attribute: Type   of Type : java.lang.String
         Attribute: ParentService   of Type : javax.management.ObjectName
         Attribute: UnactivatedChanges   of Type : javax.management.openmbean.ArrayType
         Attribute: CurrentEditorStartTime   of Type : java.lang.Long
         Attribute: Path   of Type : java.lang.String
         Attribute: Name   of Type : java.lang.String
         Attribute: ActivationTasks   of Type : [Ljavax.management.ObjectName;
         Attribute: CompletedActivationTasks   of Type : [Ljavax.management.ObjectName;
         Attribute: CompletedActivationTasksCount   of Type : java.lang.Long
         Attribute: ActiveActivationTasks   of Type : [Ljavax.management.ObjectName;
		 
         Operation: java.lang.Void  removeReferencesToBean(configurationMBean:javax.management.ObjectName  )
         Operation: javax.management.ObjectName  activate(timeout:java.lang.Long  )
         Operation: javax.management.openmbean.ArrayType  getChangesToDestroyBean(configurationMBean:javax.management.ObjectName  )
         Operation: java.lang.Void  cancelEdit()
         Operation: javax.management.ObjectName  startEdit(waitTimeInMillis:java.lang.Integer  timeOutInMillis:java.lang.Integer  exclusive:java.lang.Boolean  )
         Operation: javax.management.ObjectName  startEdit(waitTimeInMillis:java.lang.Integer  timeOutInMillis:java.lang.Integer  )
         
		 Operation: java.lang.Boolean  haveUnactivatedChanges()
		 
         Operation: java.lang.Void  save()
         Operation: java.lang.Void  purgeCompletedActivationTasks()
         Operation: java.lang.Void  undo()
         Operation: java.lang.Void  validate()
         Operation: java.lang.Void  undoUnactivatedChanges()
         Operation: java.lang.Void  stopEdit()
 */
		}
		catch(Exception ex)
		{
			System.out.println("Error while isEditSessionActive : "+ ex.getMessage());
			throw new WLSAutomationException(ex.getMessage());
		}
	}

    
}

