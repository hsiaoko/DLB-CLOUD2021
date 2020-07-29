package org.cloudbus.cloudsim.ConsistentHashingWithBoundedLoad;
import java.text.DecimalFormat;
import java.util.SortedMap;
import java.util.TreeMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.ConsistentHashingWithBoundedLoad.ConsistentHashRouter;
import org.cloudbus.cloudsim.ConsistentHashingWithBoundedLoad.Node;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.distributions.LognormalDistr;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ConsistentHashingWithBoundedLoad implements Node{
    private String idc;
    private String ip;
    private int port;
    public int loadNumber;
    public int boundedLoad;
    
    public ConsistentHashingWithBoundedLoad() {
        this.loadNumber = 0;
    }
    public ConsistentHashingWithBoundedLoad(String idc,String ip, int port, int boundedLoad) {
        this.idc = idc;
        this.ip = ip;
        this.port = port;
        this.loadNumber = 0;
        this.boundedLoad = boundedLoad;
    }

    @Override
    public String getKey() {
        //return idc + "-"+ip+":"+port;
        return ""+port;
    }

    @Override
    public String toString(){
        return getKey();
    }

    public static void main(String[] args) {
        //initialize 4 service node
        ConsistentHashingWithBoundedLoad node1 = new ConsistentHashingWithBoundedLoad("Bin","127.0.0.1",1, 5);
        ConsistentHashingWithBoundedLoad node2 = new ConsistentHashingWithBoundedLoad("Bin","127.0.0.1",6, 3);
        ConsistentHashingWithBoundedLoad node3 = new ConsistentHashingWithBoundedLoad("Bin","127.0.0.1",4, 5);
        ConsistentHashingWithBoundedLoad node4 = new ConsistentHashingWithBoundedLoad("Bin","127.0.0.1",2, 5);

        //hash them to hash ring
        ConsistentHashRouter<ConsistentHashingWithBoundedLoad> consistentHashRouter = new ConsistentHashRouter<>(Arrays.asList(node1,node2,node3,node4),1);//10 virtual node

        //we have 5 requester ip, we are trying them to route to one service node
        String requestIP1 = "1";
        String requestIP2 = "2";
        String requestIP3 = "3";
        String requestIP4 = "4";
        String requestIP5 = "5";

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        //ConsistentHashing node5 = new ConsistentHashing("Bin","127.0.0.1",90, 5);//put new service online
        //System.out.println("-------------putting new node online " +node5.getKey()+"------------");
        //consistentHashRouter.addNode(node5,10);

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        String requestIP6 = "9";
        goRoute(consistentHashRouter, requestIP6);
        String requestIP7 = "819";
        goRoute(consistentHashRouter, requestIP7);
        int bin = GetBin(consistentHashRouter, requestIP7);
        System.out.print(bin);
        
    }
    public static int GetBin(ConsistentHashRouter<ConsistentHashingWithBoundedLoad> consistentHashRouter ,String requestIp){
        System.out.println(requestIp + " is route to " + consistentHashRouter.routeNodeCHBL(requestIp));
        return consistentHashRouter.routeNodeCHBL(requestIp).getPort();
    }

    private static void goRoute(ConsistentHashRouter<ConsistentHashingWithBoundedLoad> consistentHashRouter ,String ... requestIps){
    	
        for (String requestIp: requestIps) {
            System.out.println(requestIp + " is route to " + consistentHashRouter.routeNodeCHBL(requestIp));
        }
    }
    /*
    public static String goRoute(ConsistentHashRouter<ConsistentHashing> consistentHashRouter ,String requestIp){
        //System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
            System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        //System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        return ""+consistentHashRouter.routeNode(requestIp);
    }
    */
    public int getPort() {
    	return this.port;
    }
    public int getLoadNumber() {
    	return this.loadNumber;
    }
    public int getBoundedLoad() {
    	return this.boundedLoad;
    }
    public void addLoadNumber() {
    	this.loadNumber += 1;
    }
    public void clear() {
    	this.loadNumber = 0;
    }
}
