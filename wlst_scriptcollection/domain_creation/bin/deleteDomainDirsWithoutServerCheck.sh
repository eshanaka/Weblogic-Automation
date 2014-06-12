#!/bin/sh

# 
# WebLogic Automation Book Source Code (WLST script collection)
# 
# This file is part of the WLS-Automation book sourcecode software distribution. 
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
# POSSIBILITY OF SUCH DAMAGE.
#
# @author Martin Heinzl
# Copyright (C) 2013 MH-EnterpriseConsulting, All rights reserved.
#
#
#

# Syntax:   deleteDomainDirs.sh <property file> 

MYDIR=`dirname $0`
MYFULLDIR=$(cd $MYDIR && pwd -P)

MYWLSTSCRIPT=${MYFULLDIR}/../scripts/stopDomain.py
MYDEFAULTPROPSFILE=${MYFULLDIR}/../scripts/default.properties

. ${MYFULLDIR}/platformSpecificEnv.sh


DOMAINSDIR=`sed '/^\#/d' $1 | grep 'domainsDirectory'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`

if [[ -z "$DOMAINSDIR" ]] ;then
       DOMAINSDIR=`sed '/^\#/d' $MYDEFAULTPROPSFILE | grep 'domainsDirectory'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
fi

LOGSDIR=`sed '/^\#/d' $1 | grep 'logsDirectory'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`

if [[ -z "$LOGSDIR" ]] ;then
       LOGSDIR=`sed '/^\#/d' $MYDEFAULTPROPSFILE | grep 'logsDirectory'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
fi


DOMAINNAME=`sed '/^\#/d' $1 | grep 'domainName'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`

if [[ -z "$DOMAINNAME" ]] ;then
       DOMAINNAME=`sed '/^\#/d' $MYDEFAULTPROPSFILE | grep 'domainName'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
fi


DOMAIN_INSTALL_DIR=${DOMAINSDIR}/${DOMAINNAME}
DOMAIN_LOG_DIR=${LOGSDIR}/${DOMAINNAME}



CLASSPATH="${CLASSPATH}${CLASSPATHSEP}${FMWLAUNCH_CLASSPATH}${CLASSPATHSEP}${DERBY_CLASSPATH}${CLASSPATHSEP}${DERBY_TOOLS}${CLASSPATHSEP}${POINTBASE_CLASSPATH}${CLASSPATHSEP}${POINTBASE_TOOLS}"

if [ "${WLST_HOME}" != "" ] ; then
        WLST_PROPERTIES="-Dweblogic.wlstHome='${WLST_HOME}' ${WLST_PROPERTIES}"
        export WLST_PROPERTIES
fi




#  2nd:   delete both directories
echo Will delete directory  ${DOMAIN_INSTALL_DIR}
rm -rf  ${DOMAIN_INSTALL_DIR}

echo Will delete directory ${DOMAIN_LOG_DIR}
rm -rf ${DOMAIN_LOG_DIR}



