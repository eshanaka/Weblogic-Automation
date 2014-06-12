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
package com.wlsautomation.monitoring.pushmonitoring;

import java.util.*;


public class PushExampleDestinations 
{

	// this is only a dummy implementation as it does nothing but print the values to system.out.
	// depending on the supported destinations (e.g. http, file, corba(iiop), webservice, geneos, or any other destination
	// appropriate endpoint communication must be implemented here
	
	@SuppressWarnings("unused")
	private PushExampleMBeanImpl myPushExampleMBeanImpl = null;
	
	public PushExampleDestinations(PushExampleMBeanImpl myBean)
	{
		myPushExampleMBeanImpl = myBean;
		
		// get connection URL an initialize appropriate backend connectivity
		// this should be outsourced into own classes according to the well known design patterns
		
		// e.g. String connURI = myPushExampleMBeanImpl.getDestinationURL();
		// if (connURI.startswith('http://') ....       http backend
		// else if (connURI.startswith("iiop://") ....  corba backend
		// ...
	}
	
	
	// only simple example to write out data
	public void pushData(String dataCategory, HashMap<String, String> data)
	{
		System.out.println("\nMonitoring Data - Push simulation:\n==================================================\n");
		System.out.println("    Category: " + dataCategory);
		
		Iterator<String> it = data.keySet().iterator();
		
		while (it.hasNext())
		{
			String name = it.next();
			String value = data.get(name);
			System.out.println("             "+name+" = "+value);
		}
	}
	
}
