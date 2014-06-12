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

###########################################################################
# Create a domain
###########################################################################

# ................  import ........................
import sys
from java.util import Properties
from java.io import FileInputStream
from java.io import File

# ................ global Parameter ................
pathSeparator = '/';
domainLocation = '';
jvmLocation = '';
domainTemplate = '';

domainProps = Properties();
adminUserName = 'xxx';
adminPassword = 'xxx';

userConfigFile = '';
userKeyFile = '';

###################################################################
# Load properties and do initial tests
###################################################################
def intialize():
        global domainLocation;
        global jvmLocation;
        global domainTemplate;
        global domainProps;
        global adminUserName;
        global adminPassword;
        global userConfigFile;
        global userKeyFile;

        # test arguments
        if len(sys.argv) != 6:
                print 'Usage:  createDomain.sh <template-file> <default.properties_file> <property_file> <wls_username> <wls_password>';
                exit();

        print 'Starting the initialization process';

        domainTemplate = sys.argv[1];
        print 'Using Domain Template: ' + domainTemplate;
        try:
                domainProps = Properties()

                # load DEFAULT properties
                # print 'Reading default properties from '+sys.argv[2];
                input = FileInputStream(sys.argv[2])
                domainProps.load(input)
                input.close()


                # load properties and overwrite defaults
                input = FileInputStream(sys.argv[3])
                domainProps.load(input)
                input.close()

                adminUserName = sys.argv[4];
                adminPassword = sys.argv[5];

		# Files for generating secret key

		userConfigFile = File(sys.argv[3]).getParent()+'/'+domainProps.getProperty('domainName')+'.userconfig'
                print userConfigFile
		userKeyFile = File(sys.argv[3]).getParent()+'/'+domainProps.getProperty('domainName')+'.userkey'
	        print userKeyFile			

                domainLocation = domainProps.getProperty('domainsDirectory') + pathSeparator + domainProps.getProperty('domainName');
                print 'Domain Location: ' + domainLocation;
                if len(domainProps.getProperty('jvmLocation')) == 0:
                        print 'JVM location property not defined - cancel creation !';
                        exit();
                print 'JVM Location: ' + domainProps.getProperty('jvmLocation');

        except:
                dumpStack()
                print 'Cannot initialize creation process !';
                exit();

        print 'Initialization completed';



###################################################################
# Set domain options
###################################################################
def configureDomainOptions():
    try:
        domainLogPath = domainProps.getProperty('logsDirectory')+"/"+domainProps.getProperty('domainName');
        try:
           os.makedirs(domainLogPath);
        except:
           print 'Unable to create domain root log path - please check !';
        print('Setting Domain Options...');
        create(domainProps.getProperty('domainName'), 'Log')
        cd('/Log/'+domainProps.getProperty('domainName'))
        cmo.setRotationType('byTime')
        cmo.setRotationTime('02:00')
        cmo.setFileTimeSpan(24)
        cmo.setNumberOfFilesLimited(true)
        cmo.setFileCount(14)
        cmo.setFileName(domainLogPath+'/'+domainProps.getProperty('domainName')+'.log')

    except:
        print 'Exception while setting startup options !';
        dumpStack();
        exit();



###################################################################
# Set some startup options
###################################################################
def setStartupOptions():
    try:
        print('Setting StartUp Options...');
        setOption('CreateStartMenu', 'false');
        setOption('ServerStartMode', 'prod');
        setOption('JavaHome', domainProps.getProperty('jvmLocation'));
        setOption('OverwriteDomain', 'false');
    except:
        print 'Exception while setting startup options !';
        dumpStack();
        exit();


###################################################################
# Setting JTA Transaction timeout
###################################################################
def setJTATimeout():
    try:
        print 'Setting JTA Transaction timeout...';
        edit();
        startEdit();

        cd('/JTA/'+domainProps.getProperty('domainName'));
        cmo.setTimeoutSeconds(int(domainProps.getProperty('JTATimeout')));

        save();
        activate();

    except:
        print 'Exception while setting JTA Transaction timeout !';
        dumpStack();
        exit();


###################################################################
# Set server log
###################################################################
def configureServerLog(servername):
    try:
        serverLogPath = domainProps.getProperty('logsDirectory')+"/"+domainProps.getProperty('domainName')+"/"+servername;
        try:
           os.makedirs(serverLogPath);
        except:
           dumpStack()
           print 'Unable to create server ('+servername+') log path - please check !';
        cd('/Server/'+servername)
        create(servername, 'Log')
        cd('/Server/'+servername+'/Log/'+servername)
        cmo.setRotationType('byTime')
        cmo.setRotationTime('02:00')
        cmo.setFileTimeSpan(24)
        cmo.setNumberOfFilesLimited(true)
        cmo.setFileCount(14)
        cmo.setFileName(serverLogPath+'/'+servername+'.log')

        cd('/Server/'+servername)
        create(servername, 'WebServer')
        cd('/Server/'+servername+'/WebServer/'+servername)
        create(servername, 'WebServerLog')
        cd('/Server/'+servername+'/WebServer/'+servername+'/WebServerLog/'+servername)
        cmo.setRotationTime('02:00')
        cmo.setRotationType('byTime')
        cmo.setFileTimeSpan(24)
        cmo.setFileCount(14)
        cmo.setFileName(serverLogPath+'/access.log')


    except:
        print 'Exception while setting server log options !';
        dumpStack();
        exit();



###################################################################
# Configure the admin server
###################################################################
def configureAdminServer():
    try:
        print 'Starting Admin Server Configuration...';

        # Setting listen address/port
        cd('/Server/AdminServer')

        set('ListenAddress',domainProps.getProperty('adminserver.listenAddress'))                                                                                                                                                            
        set('ListenPort',int(int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeListenPort'))));

        # SSL Settings
        create('AdminServer','SSL')
        cd('SSL/AdminServer')
        set('Enabled', domainProps.getProperty('adminserver.enableSSL'));
        set('ListenPort', (int(int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeSslListenPort')))));

        # Setting the username/password
        cd('/Security/base_domain/User/weblogic');
        cmo.setName(adminUserName);
        cmo.setPassword(adminPassword);

        # Setting Log settings
        configureServerLog('AdminServer')

        # disable hostname verification
        cd('/Servers/AdminServer/SSL/AdminServer')
        cmo.setHostnameVerificationIgnored(true)
        cmo.setHostnameVerifier(None)


        print 'Admin Server Configuration Completed.';
    except:
        print 'Exception while configuring admin server !';
        dumpStack();
        exit();


		
###################################################################
# Get instance properties
###################################################################
def get_instance_property(instancetype, instanceNumber, propName):
        # read PARAMETER;  if PARAMETER="" try to overwrite with default. Note that the default parameter has "x" instead of number
        concreateValue = domainProps.getProperty(instancetype+'.'+instanceNumber+'.'+propName,"");
        if len(concreateValue) == 0:
                concreateValue = domainProps.getProperty(instancetype+'.x.'+propName,"");  # read default !!

        return concreateValue;
		
		

###################################################################
# Create and configure all managed servers
###################################################################
def createManagedServers():
    try:
        print 'Starting creating managed servers ...';

        try:
                amountOfServers = int(  domainProps.getProperty('amountManagedServer') );
        except:
                dumpStack();
                print 'CANNOT CONVERT TO INT: ' + domainProps.getProperty('amountManagedServer');
                amountOfServers = 0;


        actualServer = 1;

        print "Start create";
        while (actualServer <= int(amountOfServers)) :
                # read PARAMETER;  if PARAMETER="" try to overwrite with default. Note that the default parameter has "x" instead of number

                actual_managedserver_name                  = get_instance_property('managedserver',str(actualServer),'name');
                actual_managedserver_machine               = get_instance_property('managedserver',str(actualServer),'machine');
                actual_managedserver_maxHeap               = get_instance_property('managedserver',str(actualServer),'maxHeap');
                actual_managedserver_listenerAddress       = get_instance_property('managedserver',str(actualServer),'listenerAddress');
                actual_managedserver_relativeListenPort    = get_instance_property('managedserver',str(actualServer),'relativeListenPort');
                actual_managedserver_addToCluster          = get_instance_property('managedserver',str(actualServer),'addToCluster');
                actual_managedserver_sslEnabled            = get_instance_property('managedserver',str(actualServer),'sslEnabled');
                actual_managedserver_relativeSslListenPort = get_instance_property('managedserver',str(actualServer),'relativeSslListenPort');
                actual_managedserver_Classpath             = get_instance_property('managedserver',str(actualServer),'ClassPath');
                actual_managedserver_BeaHome               = get_instance_property('managedserver',str(actualServer),'BeaHome');
                actual_managedserver_usenodemanager        = get_instance_property('managedserver',str(actualServer),'useNodeManager');
                actual_managedserver_additionalVMargs      = get_instance_property('managedserver',str(actualServer),'additionalVMarguments');

                if len(actual_managedserver_name) == 0:
                        actual_managedserver_name = domainProps.getProperty('domainName')+'_MS'+str(actualServer);
                print 'Creating managedserver: '+actual_managedserver_name;

                # read relative port, if null => <number>0
                if len(actual_managedserver_relativeListenPort) == 0:
                   actual_managedserver_relativeListenPort = str(actualServer)+'0';
                # read relative sslport, if null => <number>0
                if len(actual_managedserver_relativeSslListenPort) == 0:
                   actual_managedserver_relativeSslListenPort = str(actualServer)+'1';


                cd('/');
                create(actual_managedserver_name, 'Server')

                # create server
                cd('/Servers/'+actual_managedserver_name);
                print 'Configure managedserver';
                set('ListenAddress',actual_managedserver_listenerAddress)
                set('ListenPort',int(int(domainProps.getProperty('basePortNumber'))+int(actual_managedserver_relativeListenPort)));
                set('ListenPortEnabled',true);
                set('JavaCompiler','javac');
                set('ClientCertProxyEnabled',false);

                # Add to machine
                print 'Adding server to machine';
                assign('Server', actual_managedserver_name,'Machine', actual_managedserver_machine);

                # Cluster y/n ?
                if ('None' != actual_managedserver_addToCluster):
                        print 'Adding server to cluster ('+actual_managedserver_addToCluster+')';
                        assign('Server',actual_managedserver_name,'Cluster',actual_managedserver_addToCluster);


                # SSL
                if ('true' == actual_managedserver_sslEnabled):
                        print 'Configure SSL';
                        create(actual_managedserver_name,'SSL');
                        cd('/Servers/'+actual_managedserver_name+'/SSL/'+actual_managedserver_name);
                        set('Enabled', 'True');
                        set('ListenPort', int(int(domainProps.getProperty('basePortNumber'))+int(actual_managedserver_relativeSslListenPort)));


                # Startup Parameter  -- IF SET -- !!
                cd('/Servers/'+actual_managedserver_name);
                create(actual_managedserver_name,'ServerStart');

                ms_out = domainProps.getProperty('logsDirectory')+'/'+domainProps.getProperty('domainName')+'/'+actual_managedserver_name+'/'+actual_managedserver_name+'.out'
                ms_err = domainProps.getProperty('logsDirectory')+'/'+domainProps.getProperty('domainName')+'/'+actual_managedserver_name+'/'+actual_managedserver_name+'.err'
                
                managedserver_args = '-Djava.awt.headless=true  -Xmx'+actual_managedserver_maxHeap+'  -XX:MaxPermSize=512m  -Dweblogic.Stdout='+ms_out+' -Dweblogic.Stderr='+ ms_err + ' ' + actual_managedserver_additionalVMargs 

                cd ('ServerStart/'+actual_managedserver_name);
                set ('Arguments',managedserver_args);

                set ('BeaHome',actual_managedserver_BeaHome);

                set ('ClassPath',actual_managedserver_Classpath);

                set ('JavaHome', domainProps.getProperty('jvmLocation'));
                #  log settings
                configureServerLog(actual_managedserver_name)
                actualServer = actualServer+1;

    except:
        dumpStack();
        print 'Exception while configuring managed servers !';
        exit();


###################################################################
# Create the machine (usually local machine)
###################################################################
def createMachines():
    try:
        print 'Starting creating machines ...';

        try:
                amountOfMachines = int(  domainProps.getProperty('amountMachine') );
        except:
                dumpStack();
                print 'CANNOT CONVERT TO INT: ' + domainProps.getProperty('amountMachine');
                amountOfMachines = 0;


        actualMachine = 1;

        print "Start create";
        while (actualMachine <= int(amountOfMachines)) :
                # read PARAMETER;  if PARAMETER="" try to overwrite with default. Note that the default parameter has "x" instead of number

                actual_machine_name                     = get_instance_property('machine',str(actualMachine),'name');
                actual_managedserver_listenerAddress    = get_instance_property('machine',str(actualMachine),'listenerAddress');
                actual_managedserver_nodemanagerPort    = get_instance_property('machine',str(actualMachine),'nodemanagerPort');

		print 'Creating the machine '+actual_machine_name+'...';
		cd('/')
		create(actual_machine_name,'Machine');

		cd('/Machines/'+actual_machine_name);
		create(actual_machine_name,'NodeManager');

		cd('/Machines/'+actual_machine_name+'/NodeManager/'+actual_machine_name);
		set('NMType','SSL');
		set('ListenAddress',actual_managedserver_listenerAddress);
                set('ListenPort',int(actual_managedserver_nodemanagerPort));
		set('DebugEnabled',false);
 
                actualMachine = actualMachine +1
    except:
        print 'Exception while creating machine '+actual_machine_name+' !';
        dumpStack();
        exit();


###################################################################
# Create cluster
###################################################################
def createCluster():
    try:
        print 'Starting creating cluster ...';

        try:
                amountOfCluster = int(  domainProps.getProperty('amountCluster') );
        except:
                dumpStack();
                print 'CANNOT CONVERT TO INT: ' + domainProps.getProperty('amountCluster');
                amountOfCluster = 0;


        actualCluster = 1;

        print "Start create";
        while (actualCluster <= int(amountOfCluster)) :
                # read PARAMETER;  if PARAMETER="" try to overwrite with default. Note that the default parameter has "x" instead of number

                actual_cluster_name                     = get_instance_property('cluster',str(actualCluster),'name');
                actual_cluster_clusterMessagingMode     = get_instance_property('cluster',str(actualCluster),'clusterMessagingMode');
				

		print 'Creating the cluster '+actual_cluster_name+'...';
                cd('/')
                create(actual_cluster_name, 'Cluster')
                cd('Cluster/'+actual_cluster_name)
                set('ClusterMessagingMode', actual_cluster_clusterMessagingMode)
                set('FrontendHTTPSPort', 0)
                set('FrontendHTTPPort', 0)
                set('WeblogicPluginEnabled', 'false')

                actualCluster = actualCluster +1

    except:
        print 'Exception while creating cluster '+actual_cluster_name+' !';
        dumpStack();
        exit();				
									

###################################################################
# Create the domain using the above defined functions
###################################################################
def createCustomDomain():
    try:
        print 'Creating the domain...';
        readTemplate(domainTemplate);
        configureDomainOptions();
        configureAdminServer();
        setStartupOptions();
        createCluster();
        createMachines();
        createManagedServers();
        print 'Writing Domain: '+ domainLocation;
        writeDomain(domainLocation);
        closeTemplate();
        print 'Domain Created';
    except:
        print 'Exception while creating domain !';
        dumpStack();
        exit();



###################################################################
# Start adminserver and test connection
##################################################################
def startAndConnnectToAdminServer():
        connUri = 't3://'+domainProps.getProperty('adminserver.listenAddress')+':'+ str( int( int(domainProps.getProperty('basePortNumber'))+int(domainProps.getProperty('adminserver.relativeListenPort'))));
        print 'Connection URI : ' + connUri;
        print 'Starting the Admin Server...';
        startServer('AdminServer', domainProps.getProperty('domainName') , connUri, adminUserName, adminPassword, domainLocation,'true',60000,'false',jvmArgs='-XX:MaxPermSize=196m, -Xmx1024m');
        print 'Started the Admin Server'
        print 'Connecting to the Admin Server ('+connUri+')';
        connect(adminUserName, adminPassword, connUri);
        print 'Connected';
		 
	print 'Create secret access files !';
        storeUserConfig(userConfigFile , userKeyFile);



###################################################################
# Shutdown adminserver
###################################################################
def shutdownAndExit():
        print 'Shutting down the Admin Server...';
        shutdown();
        print 'Exiting...';
        exit();

###################################################################
# Create the boot.properties file
###################################################################
def createBootProperties(serverName):
    try:
        os.makedirs(domainLocation + "/servers/" + serverName + "/security")
        filename=domainLocation + "/servers/" + serverName + "/security/boot.properties"

        f=open(filename, 'w')
        # better encrypt !!  line='username=' + encrypt(adminUserName, domainLocation + "/" + domainName) + '\n'
        line='username=' + adminUserName + '\n';
        f.write(line)
        # better encrypt !!  line='password=' + encrypt(adminPassword, domainLocation + "/" + domainName) + '\n'
        line='password=' + adminPassword + '\n';
        f.write(line)
        f.flush()
        f.close()
    except OSError:
        print 'Exception while creating boot.properties for server '+serverName+'. You need to control/create this file manually !';
        dumpStack();


		
###################################################################
# create all datasources
###################################################################
def createAllDatasources():
  try:
        totalDataSource_to_Create=domainProps.get("amountDatasources")

        edit()
        startEdit()
        print 'Creating All DataSources ....'
        i=1
        while (i <= int(totalDataSource_to_Create)) :

            try:
                cd('/')
                datasource_name                       = get_instance_property('datasource',str(i), 'name');
                datasource_usesXA                     = get_instance_property('datasource',str(i), 'usesXA');
                datasource_targettype                 = get_instance_property('datasource',str(i), 'targettype');
                datasource_target                     = get_instance_property('datasource',str(i), 'target');
                datasource_jndiname                   = get_instance_property('datasource',str(i), 'jndiname');
                datasource_securitytype               = get_instance_property('datasource',str(i), 'securityType'); # MUST BE WALLET or USERPASSWORD
                datasource_relativeWalletDir          = get_instance_property('datasource',str(i), 'relativeWalletDir');
                datasource_driver_class               = get_instance_property('datasource',str(i), 'driver_class');
                datasource_url                        = get_instance_property('datasource',str(i), 'url');
                datasource_username                   = get_instance_property('datasource',str(i), 'username');
                datasource_password                   = get_instance_property('datasource',str(i), 'password');
                datasource_maxcapacity                = get_instance_property('datasource',str(i), 'maxcapacity');
                datasource_testquery                  = get_instance_property('datasource',str(i), 'testquery');
                datasource_onsnodes                   = get_instance_property('datasource',str(i), 'onsnodes');
                # GlobalTransactionsProtocol ONLY for NON-XA datasources possible !!!!   values possible: 'None' and 'OnePhaseCommit'
                datasource_globalTransactionsProtocol = get_instance_property('datasource',str(i), 'globalTransactionsProtocol');

                print ''
                print 'Creating DataSource: ',datasource_name,' ....'
                cmo.createJDBCSystemResource(datasource_name)
                cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name)
                cmo.setName(datasource_name)

                cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDataSourceParams/' + datasource_name )
                set('JNDINames',jarray.array([String(datasource_jndiname)], String))

                cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDriverParams/' + datasource_name )
                cmo.setUrl(datasource_url)
                cmo.setDriverName( datasource_driver_class );

                if ('USERPASSWORD'==datasource_securitytype):
                      cmo.setPassword(datasource_password);

                cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCConnectionPoolParams/' + datasource_name )
                cmo.setTestTableName(datasource_testquery);

                if ('USERPASSWORD'==datasource_securitytype):
                      cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDriverParams/' + datasource_name + '/Properties/' + datasource_name )
                      cmo.createProperty('user')

                      cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDriverParams/' + datasource_name + '/Properties/' + datasource_name + '/Properties/user')
                      cmo.setValue(datasource_username);
                else:
                      # WALLET
                      cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDriverParams/' + datasource_name + '/Properties/' + datasource_name )
                      cmo.createProperty('oracle.net.wallet_location')

                      cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDriverParams/' + datasource_name + '/Properties/' + datasource_name + '/Properties/oracle.net.wallet_location')
                      cmo.setValue(domainProps.getProperty('walletsDirectory')+'/'+datasource_relativeWalletDir);


                if ('false' == datasource_usesXA):
                        cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCDataSourceParams/' + datasource_name )
                        cmo.setGlobalTransactionsProtocol(datasource_globalTransactionsProtocol);

                cd('/SystemResources/' + datasource_name );
                set('Targets',jarray.array([ObjectName('com.bea:Name=' + datasource_target + ',Type='+datasource_targettype)], ObjectName))

                if (datasource_onsnodes !=None) and (datasource_onsnodes !=''):
                   cd('/JDBCSystemResources/' + datasource_name + '/JDBCResource/' + datasource_name + '/JDBCOracleParams/' + datasource_name)
                   cmo.setFanEnabled(true)
                   cmo.setOnsNodeList(datasource_onsnodes)

                   

                print 'DataSource: ',datasource_name,', has been created Successfully !!!'

            except:
                dumpStack();
                print '***** CANNOT CREATE DATASOURCE !!! Check If the DataSource With the Name : ' , datasource_name ,' Alreday exists or NOT...'
                print ''

            i = i + 1
        save()
        activate()
  except:
        print 'Exception while creating datasources - please check databases !';
        dumpStack();

###################################################################
# create all WTC server
###################################################################
def createAllWTCServer():
  try:
        totalWTCServer=domainProps.get("wtc.amountserver")

        edit()
        startEdit()
        print 'Creating All WTC server ....'
        i=1
        while (i <= int(totalWTCServer)) :

            try:
                cd('/')
                wtc_name                   = get_instance_property('wtc.server',str(i), 'name');
                wtc_targetmanagedserver    = get_instance_property('wtc.server',str(i), 'targetmanagedserver');
                wtc_localdomainname        = get_instance_property('wtc.server',str(i), 'localtuxdomain.name');
                wtc_access_point           = get_instance_property('wtc.server',str(i), 'localtuxdomain.access_point');
                wtc_access_point_id        = get_instance_property('wtc.server',str(i), 'localtuxdomain.access_point_id');
                wtc_connection_policy      = get_instance_property('wtc.server',str(i), 'localtuxdomain.connection_policy');
                wtc_nwaddr                 = get_instance_property('wtc.server',str(i), 'localtuxdomain.nw_addr');
				
		# create WTC server
                cmo.createWTCServer(wtc_name)

		# change to WTC server
                cd ('/WTCServers/'+wtc_name)
                cmo.addTarget(getMBean('/Servers/'+wtc_targetmanagedserver))
				
		# create local domain configuration
                cmo.createWTCLocalTuxDom(wtc_localdomainname)
                cd ('WTCLocalTuxDoms/'+wtc_localdomainname)
                cmo.setAccessPoint(wtc_access_point)
                cmo.setAccessPointId(wtc_access_point_id)
                cmo.setNWAddr(wtc_nwaddr)
                cmo.setConnectionPolicy(wtc_connection_policy)				
				
		# create remote tux domains
		totalWTCRemoteDomains=get_instance_property('wtc.server',str(i), 'amountremotedomains')
				
		r=1
		while (r <= int(totalWTCRemoteDomains)) :
                        remotetuxdomain_name                   = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.name');
                        remotetuxdomain_access_point           = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.access_point');
                        remotetuxdomain_access_point_id        = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.access_point_id');
                        remotetuxdomain_local_access_point     = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.local_access_point');
                        remotetuxdomain_nw_addr                = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.nw_addr');
                        remotetuxdomain_federation_url         = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.federation_url');
                        remotetuxdomain_federation_name        = get_instance_property('wtc.server',str(i), 'remotetuxdomain.'+str(r)+'.federation_name');

                        # create remote tux domain
                        cd ('/WTCServers/'+wtc_name)
                        cmo.createWTCRemoteTuxDom(remotetuxdomain_name)
                        cd ('WTCRemoteTuxDoms/'+remotetuxdomain_name)
                        cmo.setAccessPoint(remotetuxdomain_access_point)
                        cmo.setAccessPointId(remotetuxdomain_access_point_id)
                        cmo.setLocalAccessPoint(remotetuxdomain_local_access_point)
                        cmo.setNWAddr(remotetuxdomain_nw_addr)
                        cmo.setFederationName(remotetuxdomain_federation_name)
                        cmo.setFederationURL(remotetuxdomain_federation_url)

                        r = r+1

                # create WTC imports
                totalWTCRImports=get_instance_property('wtc.server',str(i), 'amountimports')

                r=1
                while (r <= int(totalWTCRImports)) :
                        import_name                 = get_instance_property('wtc.server',str(i), 'import.'+str(r)+'.name');
                        import_resource_name        = get_instance_property('wtc.server',str(i), 'import.'+str(r)+'.resource_name');
                        import_remote_name          = get_instance_property('wtc.server',str(i), 'import.'+str(r)+'.remote_name');
                        import_local_access_point   = get_instance_property('wtc.server',str(i), 'import.'+str(r)+'.local_access_point');
                        import_remote_access_point  = get_instance_property('wtc.server',str(i), 'import.'+str(r)+'.remote_access_point');

			# create WTC import
			cd ('/WTCServers/'+wtc_name)
			cmo.createWTCImport(import_name)
			cd ('WTCImports/'+import_name)
			cmo.setRemoteName(import_remote_name)
			cmo.setLocalAccessPoint(import_local_access_point)
			cmo.setResourceName(import_resource_name)
			cmo.setRemoteAccessPointList(import_remote_access_point)

			r = r+1

					
                print 'WTC Server: ',wtc_name,', has been created Successfully !!!'

            except:
                dumpStack();
                print '***** CANNOT CREATE WTC-Server !!! Check If the WTC-Server with the Name : ' , wtc_name ,' Alreday exists or NOT...'
                print ''

            i = i + 1
        save()
        activate()
  except:
        print 'Exception while creating WTC server !';
        dumpStack();


###################################################################
# Create ALL boot.properties files
###################################################################
def createAllBootProperties():
    try:
                print 'Starting creating all boot.properties ...';

                # 1st: Adminserver
                createBootProperties('AdminServer');
                amountOfServers = int(  domainProps.getProperty('amountManagedServer') );
                actualServer = 1;

                while (actualServer <= int(amountOfServers)) :
                        name = get_instance_property('managedserver',str(actualServer),'name');
                        usenodemanager        = get_instance_property('managedserver',str(actualServer),'useNodeManager');

                        if len(name) == 0:
                                name = domainProps.getProperty('domainName')+'_MS'+str(actualServer);
                        if ('false' == usenodemanager):
                             createBootProperties(name);
                        actualServer = actualServer+1;

    except:
                dumpStack();
                print 'Exception while creating all boot.properties !';

  

###################################################################
# deploy all applications
###################################################################
def deployAllApplications():
    try:
        amountOfDeployments=domainProps.get("amountDeployments")

        print 'Deploy all applications ....'
        i=1
        while (i <= int(amountOfDeployments)) :
            try:
                                applicationName        = get_instance_property('deployment',str(i),'applicationname');
                                sourcepath             = get_instance_property('deployment',str(i),'sourcepath');
                                deploymentOrderSetting = get_instance_property('deployment',str(i),'deploymentOrder');
                                targetsSetting         = get_instance_property('deployment',str(i),'targets');

                                print 'Start deployment of '+applicationName+' ('+sourcepath+') ';
                                deploy(appName=applicationName, path=sourcepath, targets=targetsSetting, upload='true');

            except:
                print '***** deployment of '+applicationname+' ('+sourcepath+') FAILED !';
                print ''
            i = i + 1

    except:
        print 'Exception while deploying all applications !';
        dumpStack();


###################################################################
# Overwrite Domain Admin Start Script
###################################################################
def overwriteDomainAdminStartScript():
    try:
        print 'Overwrite Domain Admin Start Script';
        filename=domainLocation + '/startWebLogic.sh';
        adminOut = domainProps.getProperty('logsDirectory')+"/"+domainProps.getProperty('domainName')+"/AdminServer/AdminServer.out";
        adminErr = domainProps.getProperty('logsDirectory')+"/"+domainProps.getProperty('domainName')+"/AdminServer/AdminServer.err";

        f=open(filename, 'w')
        f.write('#!/bin/sh\n');
        f.write('\n');
        f.write('DOMAIN_HOME="'+domainProps.getProperty('domainsDirectory')+'/'+domainProps.getProperty('domainName')+'"\n');
        f.write('\n');
        f.write('${DOMAIN_HOME}/bin/startWebLogic.sh $*  >> '+adminOut+' 2>> '+adminErr+'\n');
        f.write('\n');
        f.flush()
        f.close()
    except OSError:
        print 'Exception while overwrite Domain Admin Start Script.';
        dumpStack();



# ================================================================
#           Main Code Execution
# ================================================================
if __name__== "main":
        print '###################################################################';
        print '#                   Domain Creation                               #';
        print '###################################################################';
        print '';
        intialize();
        createCustomDomain();
        createAllBootProperties();
        overwriteDomainAdminStartScript();

        startAndConnnectToAdminServer();

        # do enroll on local machine
        print ' Do enroll '+ domainLocation +'  -  '+ domainProps.getProperty('nmDir')+' !\n';
        nmEnroll(domainLocation, domainProps.getProperty('nmDir'));

        setJTATimeout();
        createAllDatasources();

        createAllWTCServer()           

        deployAllApplications();

        shutdownAndExit();
# =========== End Of Script ===============


