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
package com.wlsautomation.other_utilities.parallel;

import java.util.Properties;
import java.io.FileInputStream;


public class Test {

    public static void main(String[] args)
    {
       JMXThreadTest myJMXThreadTest;
       try {
           // create globalProperties from File
           Properties globalProperties = new Properties();
           globalProperties.load(new FileInputStream(args[0]));
           int counter=1;
           int reloadInterval = Integer.parseInt(globalProperties.getProperty("reload.interval","3"));
           System.out.println("Reload-Interval = " + reloadInterval);

           while (globalProperties.getProperty("domain."+counter+".name") != null)
           {
               try {
                   myJMXThreadTest =
                             new JMXThreadTest(counter,
                                               globalProperties.getProperty("domain."+counter+".url"),
                                               globalProperties.getProperty("domain."+counter+".user"),
                                               globalProperties.getProperty("domain."+counter+".password"),
                                               reloadInterval
                             );

                    myJMXThreadTest.start();
               }
               catch (Exception ex) {
                   ex.printStackTrace();
               }
                counter++;
           }

       }
       catch (Exception ex) {
         ex.printStackTrace();
       }
    }
}
