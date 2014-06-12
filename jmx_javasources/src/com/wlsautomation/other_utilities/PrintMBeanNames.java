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

package com.wlsautomation.other_utilities;

import java.util.*;
import javax.management.*;
import javax.management.ObjectName;
import com.wlsautomation.utils.*;


public class PrintMBeanNames {


    public static void main(String[] args) {
        System.out.println("-------------------------------------------------");
        printMBeans(); 
    }


    /**
     * Print all MBean information from an MBean server
     * @param edit boolean
     */
    private static void printMBeans() {

        try {

            // init wrapper
            JMXWrapperRemote myJMXWrapper = new JMXWrapperRemote();

            // connect to server - either runtime or edit mbean server
            
            // EDIT
            // myJMXWrapper.connectToAdminServer(true, true,  "weblogic","<password>","t3://testserver.wlsautomation.de:7001");
            
            // Domain Runtime
            myJMXWrapper.connectToAdminServer(false, true, "weblogic","<password>","t3://testserver.wlsautomation.de:7001");
            
            // Server Runtime
            // myJMXWrapper.connectToAdminServer(false, false, "weblogic","<password>","t3://testserver.wlsautomation.de:7001");

            
            MBeanServerConnection connection = myJMXWrapper.getConnection();
            Set<ObjectName> mySet = connection.queryNames(new ObjectName("*:*"), null);
            Iterator<ObjectName> it = mySet.iterator();

            while (it.hasNext()) {
                ObjectName myName = (ObjectName) it.next();

                try {
                    System.out.println("--> " + myName.getCanonicalName());

                    // get all attributes
                    MBeanAttributeInfo[] atribs = connection.getMBeanInfo(myName).getAttributes();

                    for (int i = 0; i < atribs.length; i++)  {
                        System.out.println("         Attribute: " + atribs[i].getName() +
                                           "   of Type : " + atribs[i].getType());
                    }

                    // get all operations
                    MBeanOperationInfo[] operations = connection.getMBeanInfo(myName).getOperations();

                    for (int i = 0; i < operations.length; i++)  {
                        System.out.print("         Operation: " +
                                         operations[i].getReturnType() + "  " +
                                         operations[i].getName() + "(");

                        for (int j = 0; j < operations[i].getSignature().length;j++)
                            System.out.print(operations[i].getSignature()[j].
                                             getName() + ":" +
                                             operations[i].getSignature()[j].
                                             getType() + "  ");

                        System.out.println(")");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // disconnect
            myJMXWrapper.disconnectFromAdminServer(edit);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
