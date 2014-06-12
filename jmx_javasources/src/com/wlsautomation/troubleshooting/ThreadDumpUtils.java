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
package com.wlsautomation.troubleshooting;

import javax.management.ObjectName;
import com.wlsautomation.utils.*;

public class ThreadDumpUtils {
	
    private JMXWrapper myJMXWrapper = null;


    public ThreadDumpUtils(JMXWrapper _wrapper) throws WLSAutomationException
    {
    	myJMXWrapper = _wrapper;
    }

    public String getThreadDump(String serverName) throws WLSAutomationException
    {
		try {    
			 // get the server runtime(!)
			 ObjectName serverRuntime = myJMXWrapper.getServerRuntime(serverName);
			 
			 // get JVMRuntime of that server
			 ObjectName jvmRuntime = (ObjectName)myJMXWrapper.getAttribute(serverRuntime, "JVMRuntime");
			 
			 // finally return the threaddump
			 return (String)myJMXWrapper.getAttribute(jvmRuntime, "ThreadStackDump");
		}
		catch(Exception ex)
		{
			throw new WLSAutomationException("Error while getThreadDump of server "+serverName+" : "+ ex.getMessage());
		}
    }
    
}    