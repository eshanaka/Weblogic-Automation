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
package com.wlsautomation.utils;

import java.util.*;

/**
 *
 * <p>Title: Date Utils</p>
 *
 * <p>Description: </p>
 * A set of different common date utils.
 *
 * @author Martin Heinzl
 * @version 1.0
 */
public class DateUtils
{
    /**
     * Get a stringified, readable time representation from a millicond value
     * @param myDate long
     * @return String
     */
    public static String getLocaleTimeRepresentation(long myDate)
    {
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(myDate);

        StringBuffer buf = new StringBuffer();
        buf.append(get2DigitString(g.get(Calendar.DAY_OF_MONTH)));
        buf.append(".");
        buf.append(get2DigitString((g.get(Calendar.MONTH)) + 1));
        buf.append(".");
        buf.append(g.get(Calendar.YEAR));
        buf.append(" ");

        buf.append(get2DigitString(g.get(Calendar.HOUR_OF_DAY)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.MINUTE)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.SECOND)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.MILLISECOND)));

        return buf.toString();
    }

    /**
     * Get a stringified, readable time representation from a millicond value but start with the year in order to
     * make sorting posible
     *
     * @param myDate long
     * @return String
     */
    public static String getReverseLocaleTimeRepresentation(long myDate)
    {
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(myDate);

        StringBuffer buf = new StringBuffer();
        buf.append(g.get(Calendar.YEAR));
        buf.append(".");
        buf.append(get2DigitString((g.get(Calendar.MONTH)) + 1));
        buf.append(".");
        buf.append(get2DigitString(g.get(Calendar.DAY_OF_MONTH)));
        buf.append(" ");

        buf.append(get2DigitString(g.get(Calendar.HOUR_OF_DAY)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.MINUTE)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.SECOND)));
        buf.append(":");
        buf.append(get2DigitString(g.get(Calendar.MILLISECOND)));

        return buf.toString();
    }


    /**
     * internal helper method
     * @param x int
     * @return String
     */
    private static String get2DigitString(int x)
    {
      if (x > 9) return ""+x;
      else
          return "0"+x;
    }
}
