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
package com.wlsautomation.administration;


import java.util.*;
import javax.management.Attribute;
import javax.management.ObjectName;
import com.wlsautomation.utils.*;


public class CheckAndRetargetIfRequired 
{
    private static final String APPSTATERUNTIME = "com.bea:Name=AppRuntimeStateRuntime,Type=AppRuntimeStateRuntime";
	
    
	
	public static void main(String[] args) throws Exception
	{
    	String[] newTargetNames = new String[]{"VH_MS1","VH_MS2"};
    	String[] newTargetTypes = new String[]{"VirtualHost","VirtualHost"};
    	
    	String aName = "vhtest";
    	String modName = "VH1";
    	
		  myJMXWrapper = new JMXWrapperRemote();
		  myJMXWrapper.connectToAdminServer(false,true,"weblogic", "test1234", "t3://localhost:7100");
		  
		  if (retargetNeeded(myJMXWrapper, aName, modName, newTargetNames, newTargetTypes))
		  {	  
			  myJMXWrapper.disconnectFromAdminServer();
			  myJMXWrapper.connectToAdminServer(true,true,"weblogic", "test1234", "t3://localhost:7100");  
			  dotargetOperation(myJMXWrapper, aName, modName, newTargetNames, newTargetTypes);
	    	    // save changes and activate
	   	        myJMXWrapper.disconnectFromAdminServer(true);			  
		  }	  
	}


	private static JMXWrapperRemote myJMXWrapper = null;



    public static boolean retargetNeeded(JMXWrapperRemote domainRuntime, String appName, String moduleName, String[] newTargetNames, String[] newTargetTypes)
    {
    	try
    	{
    		// Application state runtime
    		ObjectName myAppStateRuntime = new ObjectName(APPSTATERUNTIME);
    		
    		// get application Names
    		ArrayList<String> allApplicationNames = getApplicationNames(domainRuntime);
    		if (! allApplicationNames.contains(appName))
    		{
    			System.out.println("[ERR: Retarget]: Application "+appName+" does not exist ?!");
    			return false;
    		}
    		
    		// ok get module list
    		ArrayList<String> allApplicationModuleNames = getModuleNames(domainRuntime,appName);
    		if (! allApplicationModuleNames.contains(moduleName))
    		{
    			System.out.println("[ERR: Retarget]: Module "+moduleName+" of application "+appName+" does not exist ?!");
    			return false;
    		}

    		
    	    String[] myCurrentModuleTargets = (String[])myJMXWrapper.invoke(myAppStateRuntime,"getModuleTargets",
					new Object[]{appName, moduleName},	new String[]{String.class.getName(),String.class.getName()});
    	    if (myCurrentModuleTargets ==  null)
    	    	myCurrentModuleTargets = new String[0];
    	        
    	    ArrayList<String> myCurrentModuleTargetsList = new ArrayList<String>(Arrays.asList(myCurrentModuleTargets));
    	    
    	    ArrayList<String> newTargetNamesList = new ArrayList<String>(Arrays.asList(newTargetNames));
    	    
    	    // test if exactly all targets are already configured
    	    if (myCurrentModuleTargetsList.containsAll(newTargetNamesList) && (myCurrentModuleTargetsList.size()==newTargetNamesList.size()))
    	    {
    			System.out.println("Module "+moduleName+" of application "+appName+" already targeted correctly and does not need to be retargeted !");
                return false;
    	    }	
    	    else
    	    {
    			System.out.println("Module "+moduleName+" of application "+appName+" needs to be retarget :-( !");
                return true;
    	    }
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		
    		// do not do retarget
    		return false;
    	}
    }
    
    
    
    
    
    /**
     */
    
    public static void dotargetOperation(JMXWrapperRemote editMBeanReference, String appName, String moduleName, String[] newTargetNames, String[] newTargetTypes)
    {   
    	// MUST BE CALLED WITH EDIT MbeanServer
    	try
    	{
    		// Application deployment MBean
    		ObjectName appDeploymentName = new ObjectName("com.bea:Name="+appName+",Type=AppDeployment");
    		
    		
    		// create subdeployment for module and ignore exception if already exist
    		ObjectName subDeployment = (ObjectName)editMBeanReference.invoke(appDeploymentName,"lookupSubDeployment",
    							new Object[]{moduleName},new String[]{String.class.getName()});
    		
    		if (subDeployment == null)
    		{
    			// create
    			subDeployment = (ObjectName)editMBeanReference.invoke(appDeploymentName,"createSubDeployment",
      							new Object[]{moduleName},new String[]{String.class.getName()});
    		}

    		
    	    // create new array with target ObjectNames    
    	    ObjectName[] targetsForDeployment = new ObjectName[newTargetNames.length];

    	    // convert the argument lists into a ObjectName array
    	    for (int i=0;i<newTargetNames.length;i++)
    	    	targetsForDeployment[i] = new ObjectName("com.bea:Name="+newTargetNames[i]+",Type="+newTargetTypes[i]);

    	    // finally set the targets
   	        myJMXWrapper.setAttribute(subDeployment,new Attribute("Targets",targetsForDeployment));
    	    
   	        System.out.println("Module "+moduleName+" of application "+appName+" has been retargeted  !");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
   	        System.out.println("Retarget module "+moduleName+" of application "+appName+" has failed  !");
    	}
    }
    
    
    
 
    private static ArrayList<String> getApplicationNames(JMXWrapperRemote domainRuntime)
    {
    	try
    	{
    		ArrayList<String> result = new ArrayList<String>();
    		
    		// Application state runtime
    		ObjectName myAppStateRuntime = new ObjectName(APPSTATERUNTIME);
    		
    		// get application IDs
    		String[] appIDs = (String[])myJMXWrapper.getAttribute(myAppStateRuntime,"ApplicationIds");
    	    if (appIDs ==  null)
    	    	appIDs = new String[0];
    		
    		for (int i=0;i<appIDs.length;i++)
    			if (! "dap".equalsIgnoreCase(appIDs[i]))
    				result.add(appIDs[i]);
    		
    		return result;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		
    		// return emtpy list
    		return new ArrayList<String>();
    	}
    }

    private static ArrayList<String> getModuleNames(JMXWrapperRemote domainRuntime, String application)
    {
    	try
    	{
    		ArrayList<String> result = new ArrayList<String>();
    		
    		// Application state runtime
    		ObjectName myAppStateRuntime = new ObjectName(APPSTATERUNTIME);
    		
    		
			String[] moduleIDs = (String[])myJMXWrapper.invoke(myAppStateRuntime,"getModuleIds",
								new Object[]{application},	new String[]{String.class.getName()});
    	    if (moduleIDs ==  null)
    	    	moduleIDs = new String[0];
    		
    		for (int i=0;i<moduleIDs.length;i++)
    				result.add(moduleIDs[i]);
    		
    		return result;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		
    		// return emtpy list
    		return new ArrayList<String>();
    	}
    }



}
