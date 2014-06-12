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


public interface PushExampleMBean
{
	/**
     * Is plugin enabled - means does plugin record periodically the runtime values and report them
     * @return String
     */
    public String getEnabled();

    /**
     * VERY IMPORTANT:  Activates or deactivates the gathering and pushing
     * @param enabled String
     */
    public void setEnabled(String enabled);

 
    /**
     * Interval in seconds how often the information are collected and reported
     * @return interval
     */
    public int getUpdateIntervalInSeconds();


    /**
     * Set the  interval how often the runtime information should be gathered and reported
     * @param intervalInSeconds int
     */
    public void setUpdateIntervalInSeconds(int intervalInSeconds);

    /**
     * GEt the destination URL:   
     *     file://...    for writing to a file
     *     http://...    for sending via http
     *     iiop://...    sending via CORBA      
     *            
     * @return the destination URL
     */
    public String getDestinationURL();

    
    /**
     * Set the destination URL:   
     *     file://...    for writing to a file
     *     http://...    for sending via http
     *     iiop://...    sending via CORBA      
     */
    public void setDestinationURL(String destinationURL);
}    

