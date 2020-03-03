import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.transport.TransportMappings;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.snmp4j.util.WorkerPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Agent extends BaseAgent {
    //Agent instance variables
    private boolean multithreaded=false;
    private boolean walk=false;
    private boolean bulk=false;
    private double period;
    private static int retry = 5;
    private int port;
    private int nonreps=0;
    private int maxreps=0;
    private int threads=1;
    private String ip;
    private String communityString;
    private static List<OID> oids = new ArrayList<>();
    private static List<String> resultList = new ArrayList<>();
    private static List<MusicInfo> musicList = new ArrayList<>();
    // SNMP4J Classes
    private TransportMapping transport;
    private Snmp snmp;
    private CommunityTarget target;

    public Agent () throws Exception {
        super(new File("../ageConfs/conf.agent"), new File("../ageConfs/bootCounter.agent"),
                new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        this.port=161;
        this.ip = "127.0.0.1";
        this.communityString = "public";
        this.transport= new DefaultUdpTransportMapping();
        this.snmp = new Snmp(transport);
        this.transport.listen();
       // this.target = init_connection();
    }

    public Agent (String ip,int port,String com,boolean multithreaded,int threads) throws Exception {
        super(new File("../ageConfs/conf.agent"), new File("../ageConfs/bootCounter.agent"),
                new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        this.port = port;
        this.ip=ip;
        this.communityString=com;
        this.multithreaded=multithreaded;
        this.threads=threads;
        if (!multithreaded) {
            this.transport= new DefaultUdpTransportMapping();
            this.snmp = new Snmp(transport);
            this.transport.listen();
        } else {
            WorkerPool threadPool = ThreadPool.create("TestSNMPWorkPool", this.threads);
            MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
            this.transport = new DefaultUdpTransportMapping();
            this.snmp = new Snmp(dispatcher,transport);
            this.snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
            this.snmp.listen();
        }
       // this.target = init_connection();
    }

    @Override
    protected void registerManagedObjects() {

    }

    @Override
    protected void unregisterManagedObjects() {

    }

    public void unregisterManagedObjects(MOGroup objGroup) {
        objGroup.unregisterMOs(getServer(),new OctetString("public"));
    }

    @Override
    protected void addUsmUser(USM usm) {

    }

    @Override
    protected void addNotificationTargets(SnmpTargetMIB snmpTargetMIB, SnmpNotificationMIB snmpNotificationMIB) {

    }

    @Override
    protected void addViews(VacmMIB vacmMIB) {
        vacmMIB.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
                "cpublic"), new OctetString("v1v2group"), StorageType.nonVolatile);

        vacmMIB.addAccess(new OctetString("v1v2group"), new OctetString("public"),
                SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
                MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
                new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);

        vacmMIB.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"), new OctetString(),
                VacmMIB.vacmViewIncluded, StorageType.nonVolatile);

        vacmMIB.addViewTreeFamily(new OctetString("fullWriteView"),new OID("1.3.6.1.3.2020"),new OctetString(),VacmMIB.vacmViewIncluded,StorageType.nonVolatile);
    }


    @Override
    protected void addCommunities(SnmpCommunityMIB snmpCommunityMIB) {
        Variable[] com2sec = new Variable[] {
                new OctetString("public"),
                new OctetString("cpublic"), // security name
                getAgent().getContextEngineID(), // local engine ID
                new OctetString("public"), // default context name
                new OctetString(), // transport tag
                new Integer32(StorageType.nonVolatile), // storage type
                new Integer32(RowStatus.active) // row status
        };

        MOTableRow row;
        row = snmpCommunityMIB.getSnmpCommunityEntry().createRow(new OctetString("public2public").toSubIndex(true), com2sec);
        snmpCommunityMIB.getSnmpCommunityEntry().addRow((SnmpCommunityMIB.SnmpCommunityEntryRow) row);

    }

    public void registerManagedObject(ManagedObject mo) {
        try {
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unregisterObject(ManagedObject mo) {
        getServer().unregister(mo,null);
    }

    public void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }



    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        String address = "udp" + ":" + this.ip + "/" + this.port;
        Address addr = GenericAddress.parse(address);
        TransportMapping tm = TransportMappings.getInstance().createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    public void start() throws IOException {
        init();
        this.target = init_connection();
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public void setDEFAULT_PORT(int port) {
        this.port = port;
    }

    public void setWalk(boolean walk) {
        this.walk = walk;
    }

    public void setBulk(boolean bulk) {
        this.bulk = bulk;
    }

    public void setMultithreaded(boolean multithreaded) {
        this.multithreaded=multithreaded;
    }

    public void setThreads(int threads) {
        this.threads=threads;
    }

    public void setNonreps(int Nonrepeaters) {
        this.nonreps = (Nonrepeaters==0) ? (this.nonreps) : (Nonrepeaters);
    }

    public void setMaxreps (int maxreps) {
        this.maxreps=maxreps;
    }

    public List<MusicInfo> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfo> mi) {
        if (!musicList.isEmpty()) musicList.clear();
        musicList.addAll(mi);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isMultithreaded() {
        return this.multithreaded;
    }

    public boolean isWalk() {
        return walk;
    }

    public boolean isBulk() {
        return bulk;
    }


    public double getPeriod() {
        return period;
    }

    public List<String> getResultList() {
        return this.resultList;
    }

    public void clearResults() {
        this.resultList.clear();
    }

    public List<OID> getOids() {
        return this.oids;
    }

    public CommunityTarget init_connection() {
        Address address = GenericAddress.parse("udp" + ":" + this.ip + "/" + this.port);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.communityString));
        target.setAddress(address);
        target.setVersion(1);
        return target;
    }

    /**
     * Implementa a primitiva snmpwalk sincronamente. Verifica, através do PDU de resposta , se a travessia já terminou , se sim , termina a execução , caso contrário envia um GETNEXT ao CommunityTarget
     * @param targetOid - OID que será percorrido
     */
    public void snmpWalk(OID targetOid) {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(targetOid));
            pdu.setType(PDU.GETNEXT);
            boolean finished = false;
            while (!finished) {
                ResponseEvent respEvent = this.snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();
                VariableBinding vb = response.get(0);
                finished = isWalkFinished(targetOid, pdu, vb); // check finish
                if (!finished) {
                    this.resultList.add(vb.getVariable().toString());
                    pdu.setRequestID(new Integer32(0)); // Set up the variable binding for the next entry.
                    pdu.set(0, vb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asyncWalk(OID oid) {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(oid));
            pdu.setType(PDU.GETNEXT);
            CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public <A extends Address> void onResponse(ResponseEvent<A> responseEvent) {
                    ((Snmp) responseEvent.getSource()).cancel(responseEvent.getRequest(), this);

                    try {
                        PDU response = responseEvent.getResponse();
                        if (response == null) return;
                        VariableBinding vb = response.get(0);
                        resultList.add(vb.getVariable().toString());
                        boolean finished = isWalkFinished(oid,response, vb);
                        if (!finished) {
                                pdu.setRequestID(new Integer32(0));
                                pdu.set(0, vb);
                                ((Snmp) responseEvent.getSource()).getNext(pdu, target, null, this);
                        } else latch.countDown();

                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }
                }
            };
            snmp.getNext(pdu, target, null, listener);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asyncWalkMulti(OID id) {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(id));
            pdu.setType(PDU.GETNEXT);
            CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public <A extends Address> void onResponse(ResponseEvent<A> responseEvent) {
                    try {
                        PDU response = responseEvent.getResponse();
                        if (response==null) return;
                        VariableBinding vb = response.get(0);
                        resultList.add(vb.toString());
                        boolean finished = isWalkFinished(id,response, vb);
                        if (!finished) {
                            pdu.setRequestID(new Integer32(0));
                            pdu.set(0, vb);
                            ((Snmp) responseEvent.getSource()).getNext(pdu, target, null, this); // Asynchronously sends a GETNEXT request to target
                        } else latch.countDown();

                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }
                }
            };
            snmp.getNext(pdu, target, null, listener);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param targetOID
     * @param pdu
     * @param vb
     * @return
     */
    private boolean isWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean cond1 = vb.getOid() == null || vb.getOid().size() < targetOID.size() || vb.getOid().compareTo(targetOID)<=0;
        boolean cond2 = targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0 || Null.isExceptionSyntax(vb.getVariable().getSyntax());
        boolean cond3 = pdu.getErrorStatus() !=0;
        return (cond1 || cond2 || cond3);

    }

    private boolean isBulkFinished(OID targetOID,PDU pdu, List lvb) {
        VariableBinding last = (VariableBinding) lvb.get(lvb.size()-1);
        boolean cond1 = last.getOid() == null || last.getOid().size() < targetOID.size() || last.getOid().compareTo(targetOID)<=0;
        boolean cond2 = targetOID.leftMostCompare(targetOID.size(), last.getOid()) != 0 || Null.isExceptionSyntax(last.getVariable().getSyntax());
        boolean cond3 = pdu.getErrorStatus() !=0;
        return (cond1 || cond2 || cond3);
    }

    public void snmpBulkGet (OID oid) {
       try {
                PDU pdu = new PDU();
                pdu.add(new VariableBinding(oid));
                pdu.setType(PDU.GETBULK);
                pdu.setMaxRepetitions(this.maxreps);
                pdu.setNonRepeaters(this.nonreps);
                boolean finished = false;
                while (!finished) {
                    ResponseEvent respEvent = this.snmp.send(pdu, this.target);
                    PDU response = respEvent.getResponse();
                    if (response == null) System.out.println("RESPOSTA NULA");
                    for (VariableBinding v : response.getVariableBindings()) {
                        if (!v.getOid().startsWith(oid)) break;
                        else this.resultList.add(v.getVariable().toString());
                        }
                        finished = isBulkFinished(oid, response, response.getVariableBindings());
                        if (!finished) {
                            pdu.setRequestID(new Integer32(0)); // Set up the variable binding for the next entry with unique identifier;
                            int N = response.size() -1;
                            pdu.set(0, response.getVariableBindings().get(N)); // Last element index = Max_Reps -1;
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void closeSession() throws IOException {
        this.snmp.close();
    }

    public void asyncBulkGet (OID oid) {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(oid));
            pdu.setType(PDU.GETBULK);
            pdu.setMaxRepetitions(this.maxreps);
            pdu.setNonRepeaters(this.nonreps);
            CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public <A extends Address> void onResponse(ResponseEvent<A> responseEvent) {
                    ((Snmp) responseEvent.getSource()).cancel(responseEvent.getRequest(), this);
                    try {
                        PDU response = responseEvent.getResponse();
                        if (response == null) return;
                        for (VariableBinding v : response.getVariableBindings()) {
                            if (!v.getOid().startsWith(oid)) break;
                            else resultList.add(v.getVariable().toString());
                        }
                        boolean finished = isBulkFinished(oid, response, response.getVariableBindings());
                        if (!finished) {
                            pdu.setRequestID(new Integer32(0)); // Set up the variable binding for the next entry with unique identifier;
                            int N = response.size() -1;
                            pdu.set(0, response.getVariableBindings().get(N)); // Last element index = Max_Reps -1;
                            ((Snmp) responseEvent.getSource()).getBulk(pdu, target, null, this); // Asynchronously sends a GETBULK request to target
                        }
                        else latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }

                }
            };
            snmp.getBulk(pdu,target,null,listener);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asyncBulkGetMulti (OID oid) {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(oid));
            pdu.setType(PDU.GETBULK);
            pdu.setMaxRepetitions(this.maxreps);
            pdu.setNonRepeaters(this.nonreps);
            CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public <A extends Address> void onResponse(ResponseEvent<A> responseEvent) {
                    ((Snmp) responseEvent.getSource()).cancel(responseEvent.getRequest(), this);
                    try {
                        PDU response = responseEvent.getResponse();
                        if (response == null) return;
                        for (VariableBinding v : response.getVariableBindings()) {
                            if (!v.getOid().startsWith(oid)) break;
                            else resultList.add(v.getVariable().toString());
                        }
                        boolean finished = isBulkFinished(oid, response, response.getVariableBindings());
                        if (!finished) {
                            pdu.setRequestID(new Integer32(0)); // Set up the variable binding for the next entry with unique identifier;
                            int N = response.size() -1;
                            pdu.set(0, response.getVariableBindings().get(N)); // Last element index = Max_Reps -1;
                            ((Snmp) responseEvent.getSource()).getBulk(pdu, target, null, this); // Asynchronously sends a GETBULK request to target
                        }
                        else latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }

                }
            };
            snmp.getBulk(pdu,target,null,listener);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDU(VariableBinding var) {
        try {
            PDU pdu = new PDU();
            pdu.add(var);
            pdu.setType(PDU.SET);
            CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public <A extends Address> void onResponse(ResponseEvent<A> responseEvent) {
                    ((Snmp) responseEvent.getSource()).cancel(responseEvent.getRequest(), this);
                    try {
                        PDU response = responseEvent.getResponse();
                        if (response == null) return;
                        else {
                            VariableBinding vb = response.get(0);
                            System.out.println(vb.toString());
                            pdu.setRequestID(new Integer32(0)); // Set up the variable binding for the next entry with unique identifier;
                            pdu.set(0, vb);
                            //((Snmp) responseEvent.getSource()).set(pdu, target, null, this); // Asynchronously sends a SET request to target
                        }
                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }

                }
            };
            snmp.set(pdu,target,null,listener);
            latch.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
