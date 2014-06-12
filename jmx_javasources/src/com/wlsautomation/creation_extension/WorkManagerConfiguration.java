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
package com.wlsautomation.creation_extension;

import javax.management.ObjectName;

import com.wlsautomation.utils.JMXWrapper;
import com.wlsautomation.utils.WLSAutomationException;
import java.util.*;
import javax.management.Attribute;

public class WorkManagerConfiguration 
{

    private JMXWrapper myJMXWrapper = null;

    public WorkManagerConfiguration(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }


    // Operation: javax.management.ObjectName  createMinThreadsConstraint(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
    public ObjectName createMinThreadsConstraint(String newName, int count, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myMinThreadsConstraint = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupMinThreadsConstraint",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myMinThreadsConstraint==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myMinThreadsConstraint = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createMinThreadsConstraint",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myMinThreadsConstraint, new Attribute("Count",new Integer(count)));
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myMinThreadsConstraint, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("MinThreadsConstraint with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myMinThreadsConstraint;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }    
    
    
    // Operation: javax.management.ObjectName  createMaxThreadsConstraint(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupMaxThreadsConstraint(name:java.lang.String  )
    public ObjectName createMaxThreadsConstraint(String newName, int count, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myMaxThreadsConstraint = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupMaxThreadsConstraint",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myMaxThreadsConstraint==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myMaxThreadsConstraint = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createMaxThreadsConstraint",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myMaxThreadsConstraint, new Attribute("Count",new Integer(count)));
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myMaxThreadsConstraint, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("MaxThreadsConstraint with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myMaxThreadsConstraint;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }        
    
    
    // Operation: javax.management.ObjectName  createResponseTimeRequestClass(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupResponseTimeRequestClass(name:java.lang.String  )
    public ObjectName createResponseTimeRequestClass(String newName, int goalMs, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myResponseTimeRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupResponseTimeRequestClass",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myResponseTimeRequestClass==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myResponseTimeRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createResponseTimeRequestClass",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myResponseTimeRequestClass, new Attribute("GoalMs",new Integer(goalMs)));
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myResponseTimeRequestClass, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("ResponseTimeRequestClass with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myResponseTimeRequestClass;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }            

    
    
    // Operation: javax.management.ObjectName  createCapacity(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupCapacity(name:java.lang.String  )
    public ObjectName createCapacity(String newName, int count, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myCapacity = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupCapacity",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myCapacity==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myCapacity = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createCapacity",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myCapacity, new Attribute("Count",new Integer(count)));
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myCapacity, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("Capacity with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myCapacity;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }  
    
    
    
    // Operation: javax.management.ObjectName  createFairShareRequestClass(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupFairShareRequestClass(name:java.lang.String  )
    public ObjectName createFairShareRequestClass(String newName, int fairShare, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myFairShareRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupFairShareRequestClass",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myFairShareRequestClass==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myFairShareRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createFairShareRequestClass",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myFairShareRequestClass, new Attribute("FairShare",new Integer(fairShare)));
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myFairShareRequestClass, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("FairShareRequestClass with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myFairShareRequestClass;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }  
    
    
    // Operation: javax.management.ObjectName  createContextRequestClass(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupContextRequestClass(name:java.lang.String  )
    public ObjectName createContextRequestClass(String newName, ArrayList<ContextCaseData> myContextList, ObjectName[] targets)  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myContextRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupContextRequestClass",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myContextRequestClass==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myContextRequestClass = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createContextRequestClass",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	for (int i=0;i<myContextList.size();i++)
	 	    	{
	 	    		ContextCaseData myContextCaseData = myContextList.get(i);
	 	    		
	 	    		ObjectName newContext = (ObjectName)myJMXWrapper.invoke(myContextRequestClass,
	 	                      "createContextCase",
	 	                      new Object[]{new String("Context_"+i)},
	 	                      new String[]{String.class.getName()});
	 	    		
	 	    		myJMXWrapper.setAttribute(newContext, new Attribute("UserName",myContextCaseData.getUserName()));
	 	    		myJMXWrapper.setAttribute(newContext, new Attribute("GroupName",myContextCaseData.getGroupName()));
	 	    		myJMXWrapper.setAttribute(newContext, new Attribute("RequestClassName",myContextCaseData.getRequestClassName()));
	 	    		if (targets != null)
	 	    			myJMXWrapper.setAttribute(newContext, new Attribute("Targets",targets));
	 	    	}
	 	    	// to do 
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myContextRequestClass, new Attribute("Targets",targets));	 	    		
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("ContextRequestClass with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myContextRequestClass;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }  
    
    

    // Operation: javax.management.ObjectName  createWorkManager(name:java.lang.String  )
    // Operation: javax.management.ObjectName  lookupWorkManager(name:java.lang.String  )
    public ObjectName createWorkManager(String newName, 
    		                            boolean isIgnoreStuckThreads, 
    		                            ObjectName[] targets, 
    		                            ObjectName myMinThreadsConstraint, 
    		                            ObjectName myMaxThreadsConstraint,
    		                            ObjectName myFairShareRequestClass,  
    		                            ObjectName myResponseTimeRequestClass, 
    		                            ObjectName myContextRequestClass, 
    		                            ObjectName myCapacity
    		                            // !!!!!!!  WorkManagerShutdownTrigger missing
    		                            )  throws WLSAutomationException
    {
      try
      {   // lookup selftuning
    	  ObjectName mySelfTuning = lookupSelfTuning();
    	  
	 	  // Operation: javax.management.ObjectName  lookupMinThreadsConstraint(name:java.lang.String  )
	 	  ObjectName myWorkManager = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                   										"lookupWorkManager",
                   										new Object[]{new String(newName)},
                   										new String[]{String.class.getName()});
	 	     if (myWorkManager==null)
	 	     {
	 	    	 // create
	 	    	// Operation: javax.management.ObjectName  createMailSession(name:java.lang.String  )
	 	    	myWorkManager = (ObjectName)myJMXWrapper.invoke(mySelfTuning,
                      "createWorkManager",
                      new Object[]{new String(newName)},
                      new String[]{String.class.getName()});

	 	    	// configure
	 	    	myJMXWrapper.setAttribute(myWorkManager, new Attribute("IgnoreStuckThreads",new Boolean(isIgnoreStuckThreads)));
	 	    	
	 	    	if (myMinThreadsConstraint != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("IgnoreStuckThreads",new Boolean(isIgnoreStuckThreads)));
	 	    	
	 	    	// configure
	 	    	if (targets != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("Targets",targets));
	 	    	if (myMinThreadsConstraint != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("MinThreadsConstraint",myMinThreadsConstraint));
	 	    	if (myMaxThreadsConstraint != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("MaxThreadsConstraint",myMaxThreadsConstraint));
	 	    	if (myFairShareRequestClass != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("FairShareRequestClass",myFairShareRequestClass));
	 	    	if (myResponseTimeRequestClass != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("ResponseTimeRequestClass",myResponseTimeRequestClass));
	 	    	if (myContextRequestClass != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("ContextRequestClass",myContextRequestClass));
	 	    	if (myCapacity != null)
	 	    		myJMXWrapper.setAttribute(myWorkManager, new Attribute("Capacity",myCapacity));
	 	    	
	 	    	// WorkManagerShutdownTrigger MISSING !!!
	 	     }
	 	     else
	 	    	 throw new WLSAutomationException("WorkManager with name "+newName+" already exist  -  cannot create !");
    	  
    	  return myWorkManager;
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }
    
      
    private ObjectName lookupSelfTuning()  throws WLSAutomationException
    {
      try
      {
    	  // e.g.: com.bea:Name=TestDomain,Type=Domain
    	  ObjectName myDomainMBean = myJMXWrapper.getDomainConfigRoot();
    	  
    	  return (ObjectName)myJMXWrapper.getAttribute(myDomainMBean,"SelfTuning");
      }
      catch(Exception ex)
      {
    	  throw new WLSAutomationException(ex);
      }
    }	


}