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
package com.wlsautomation.monitoring;

import com.wlsautomation.utils.*;

public class WLSMonitoringUtils
{
    JMXWrapper myJMXWrapper = null;

    public WLSMonitoringUtils(JMXWrapper _myJMXWrapper)
    {
        myJMXWrapper = _myJMXWrapper;
    }


    public String getHealthStateInformation(weblogic.health.HealthState myState)
    {
        if(myState.getState()==weblogic.health.HealthState.HEALTH_OK)
            return "HEALTH_OK";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_WARN)
            return "HEALTH_WARN";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_CRITICAL)
            return "HEALTH_CRITICAL";
        else if(myState.getState()==weblogic.health.HealthState.HEALTH_FAILED)
            return "HEALTH_FAILED";
        else if(myState.getState()==weblogic.health.HealthState. HEALTH_OVERLOADED)
            return "HEALTH_OVERLOADED";
        else
            return "UNKNOWN STATE";
    }



}




