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


public class PushExampleThread extends Thread
{
    private PushExampleMBeanImpl myMBean = null;
    
    private PushExampleDestinations myPushExampleDestination= null;

    public PushExampleThread(PushExampleMBeanImpl _myMBean) {
    	myMBean = _myMBean;
    	myPushExampleDestination = new PushExampleDestinations(myMBean);
    }
 

	public void run()
    {
       try
       {
           // do pushing while plugin is enabled
           while (myMBean.getEnabled().equalsIgnoreCase("true"))
           {
               int waitSeconds = myMBean.getUpdateIntervalInSeconds();
               // wait
               try {
                   Thread.sleep(waitSeconds*1000);
               }
               catch (Exception ex) {
                   continue;
               }

               // still enabled ?
               if (myMBean.getEnabled().equalsIgnoreCase("true"))
               {
            	   // get monitoring values and push to destination
            	   
            	   // if admin => push domain data
            	   if (myMBean.isConnectedToAdmin())
            		   myPushExampleDestination.pushData("DOMAIN", myMBean.getDomainData());
            	   
            	   // push server data
            	   myPushExampleDestination.pushData("SERVER", myMBean.getServerData());
               }
           }
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
       }
    }
}
