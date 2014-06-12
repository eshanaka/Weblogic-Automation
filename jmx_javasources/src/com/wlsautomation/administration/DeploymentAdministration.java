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

import com.wlsautomation.utils.*;
import java.util.*;
import javax.management.ObjectName;
import java.io.*;
import weblogic.deploy.api.tools.*;
import weblogic.deploy.api.spi .*;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;


//@SuppressWarnings("deprecation")
public class DeploymentAdministration {

    private JMXWrapperRemote myJMXWrapper = null;
    
    private WebLogicDeploymentManager appDeploymentManager = null;

    public DeploymentAdministration(JMXWrapperRemote _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    	
    	try
    	{
            // get the deployment manager
    		// note that we cannot use our usual JMXWrapper directly, but we can use its connectivity information
    		appDeploymentManager =  SessionHelper.getRemoteDeploymentManager( 
        			myJMXWrapper.getRemoteProtocol(),
        			myJMXWrapper.getRemoteHostName(),
        			myJMXWrapper.getRemotePort(),
        			myJMXWrapper.getRemoteUser(),
        			myJMXWrapper.getRemotePassword());
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in getDeploymentManager : "+ ex.getMessage());
		}   
    	
    }

    public Target[] getListOfTargets() throws WLSAutomationException
    {
       try
       {
    	   if (appDeploymentManager != null) {
    		   // return target list
    		   return appDeploymentManager.getTargets(); 
    	   }
    	   else
    		   return null;
    		
       }
	   catch(Exception ex) {
			throw new WLSAutomationException("Error in getListOfTargets : "+ ex.getMessage());
		}   
    }
   
    
    public void releaseAndDisconnect() throws WLSAutomationException
    {
       try
       {
    	   if (appDeploymentManager != null) {
    		   // release
    		   appDeploymentManager.release();
    		   
    		   // set to null
    		   appDeploymentManager = null;
    	   }	   
       }
	   catch(Exception ex) {
			throw new WLSAutomationException("Error in releaseAndDisconnect : "+ ex.getMessage());
		}   
    }
    
    
    public String getConnectedDomainName() throws WLSAutomationException
    {
       return (appDeploymentManager != null) ? appDeploymentManager.getDomain() : "NOT_CONNECTED";
    }    

    public boolean isDeploymentManagerConnected() throws WLSAutomationException
    {
       return (appDeploymentManager != null) ? appDeploymentManager.isConnected()  : false;
    }    
    
    
    public void deployApplication(String applicationName, String appFileName) throws WLSAutomationException
    {
    	try
    	{
        	System.out.println("Using remote webLogic deployment manager: "+appDeploymentManager);
        	
        	// create deployment options and set application name
        	DeploymentOptions deploymentOptions = new DeploymentOptions();
        	deploymentOptions.setName(applicationName);
        	
        	// get and print the targets
        	Target deploymentTargets[]=appDeploymentManager.getTargets();
        	for (int i=0;i<deploymentTargets.length;i++)
        	{
        		System.out.println("   Target: "+deploymentTargets[i]);
        	}

        	// deploy and get the progress object for a status message
        	ProgressObject myProcStatus=appDeploymentManager.distribute(deploymentTargets, new File(appFileName), null,deploymentOptions);
        	// get and print the deplyoment status
        	DeploymentStatus myStatus=myProcStatus.getDeploymentStatus() ;
        	System.out.println("Actual state of this deployment: "+myStatus.getMessage() );
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in deployApplication : "+ ex.getMessage());
		}
    }


    public void undeployAllApplication() throws WLSAutomationException
    {
    	try
    	{
        	System.out.println("Using remote webLogic deployment manager: "+appDeploymentManager);

        	TargetModuleID[] deployedApplicationIDs=appDeploymentManager.getAvailableModules(ModuleType.EAR, appDeploymentManager.getTargets());
        	if(deployedApplicationIDs != null)
        	{
        		for(int i=0;i<deployedApplicationIDs.length;i++)
        		{
        			System.out.println("Will now undeploy application: "+deployedApplicationIDs[i]);
        			
        			// get the progress object for this action
        			ProgressObject myProcStatus=appDeploymentManager.undeploy(new TargetModuleID[]{deployedApplicationIDs[i]});
        			
        			// get the status
        			DeploymentStatus undeployStatus=myProcStatus.getDeploymentStatus() ;
        			System.out.println("Actual state of this undeployment is:  "+undeployStatus.getMessage() );
        		}
        	}
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in undeployAllApplication : "+ ex.getMessage());
		}
    }

    public ArrayList<TargetModuleID> getListOfDeployments()  throws WLSAutomationException { 
    	try
    	{
    		ArrayList<TargetModuleID> result = new ArrayList<TargetModuleID>();

    		// get all targets
       	    Target allTargets[] = getListOfTargets();
       	    
       	    // EAR
       	    TargetModuleID targetModuleID[] = appDeploymentManager.getAvailableModules(ModuleType.EAR, allTargets);
       	    if (targetModuleID!=null)
       	        for (int i=0;i<targetModuleID.length;i++) result.add(targetModuleID[i]);
       	    // WAR
       	    targetModuleID = appDeploymentManager.getAvailableModules(ModuleType.WAR, allTargets);
       	    if (targetModuleID!=null)
       	        for (int i=0;i<targetModuleID.length;i++) result.add(targetModuleID[i]);
       	    // EJB
       	    targetModuleID = appDeploymentManager.getAvailableModules(ModuleType.EJB, allTargets);
       	    if (targetModuleID!=null)
       	        for (int i=0;i<targetModuleID.length;i++) result.add(targetModuleID[i]);
       	    // RAR
   	        targetModuleID = appDeploymentManager.getAvailableModules(ModuleType.RAR, allTargets);
       	    if (targetModuleID!=null)
       	        for (int i=0;i<targetModuleID.length;i++) result.add(targetModuleID[i]);
       	    
		    return result;
		}
		catch(Exception ex)
		{   ex.printStackTrace();
			throw new WLSAutomationException("Error in getListOfDeployments : "+ ex.getMessage());
		}
    }
    
    
    
    /**
     * The following function will create a list of names of all known applications. Again this list only lists the name of all applications
       known to the domain, regardless on which target they are deployed. This is useful e.g. for checking names, or create selection
       menus (for interactive scripts) or …
     * @return
     * @throws WLSAutomationException
     */
    public ArrayList<String> getApplicationNames() throws WLSAutomationException
    {
    	ArrayList<String> result = new ArrayList<String>();
    	try
    	{
    		// get the domain config root
    		ObjectName domainConfigRoot = myJMXWrapper.getDomainConfigRoot();
    		
    		// get all appdeployments
    		ObjectName[] appdeployments = (ObjectName[]) myJMXWrapper.getAttribute(domainConfigRoot, "AppDeployments");
    		
    		for (int i=0;i<appdeployments.length;i++)
    		{
    			String name = (String) myJMXWrapper.getAttribute(appdeployments[i], "Name");
    			result.add(name);
    		}

    		return result;
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in getApplicationNames : "+ ex.getMessage());
		}
    	
    }
    
    
    /**
     * This method checks is an application with a specific name is deployed to a domain
     * @param applicationName
     * @return
     */
    public boolean isApplicationDeployedToDomain(String applicationName) throws WLSAutomationException
    {
    	try
    	{
    		ArrayList<String> appNames = getApplicationNames();
    		
    		return appNames.contains(applicationName);
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in isApplicationDeployedToDomain : "+ ex.getMessage());
		}
    	
    }


    public void startApplications(TargetModuleID[] modules, DeploymentOptions options) throws WLSAutomationException
    {
    	try
    	{
        	// start
        	ProgressObject myProcStatus=null;
        	if (options==null)
        		myProcStatus=appDeploymentManager.start(modules);
        	else
        		myProcStatus=appDeploymentManager.start(modules,options);
        	// get and print the status
        	DeploymentStatus myStatus=myProcStatus.getDeploymentStatus() ;
        	System.out.println("Actual state of this action: "+myStatus.getMessage() );
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in startApplications : "+ ex.getMessage());
		}
    }    
    
    
    public void stopApplications(TargetModuleID[] modules, DeploymentOptions options) throws WLSAutomationException
    {
    	try
    	{
        	// start
        	ProgressObject myProcStatus=null;
        	if (options==null)
        		myProcStatus=appDeploymentManager.stop(modules);
        	else
        		myProcStatus=appDeploymentManager.stop(modules,options);
        	// get and print the status
        	DeploymentStatus myStatus=myProcStatus.getDeploymentStatus() ;
        	System.out.println("Actual state of this action: "+myStatus.getMessage() );
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error in stopApplications : "+ ex.getMessage());
		}
    }    
 
    
    
 
}
