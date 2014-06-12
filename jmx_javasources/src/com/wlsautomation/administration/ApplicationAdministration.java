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


public class ApplicationAdministration {

    private JMXWrapperRemote myJMXWrapper = null;
    
    public ApplicationAdministration(JMXWrapperRemote _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

    
    
    
    /**
     * The following function will create a list of names of all known applications. Again this list only lists the name of all applications
       known to the domain, regardless on which target they are deployed. This is useful e.g. for checking names, or create selection
       menus (for interactive scripts) 
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

    
}
