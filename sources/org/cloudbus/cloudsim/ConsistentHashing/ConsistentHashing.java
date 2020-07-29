package org.cloudbus.cloudsim.ConsistentHashing;
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
import org.cloudbus.cloudsim.ConsistentHashing.ConsistentHashRouter;
import org.cloudbus.cloudsim.ConsistentHashing.Node;
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

public class ConsistentHashing implements Node{
    private final String idc;
    private final String ip;
    private final int port;

    public ConsistentHashing(String idc,String ip, int port) {
        this.idc = idc;
        this.ip = ip;
        this.port = port;
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
        ConsistentHashing node1 = new ConsistentHashing("Bin","127.0.0.1",1);
        ConsistentHashing node2 = new ConsistentHashing("Bin","127.0.0.1",6);
        ConsistentHashing node3 = new ConsistentHashing("Bin","127.0.0.1",4);
        ConsistentHashing node4 = new ConsistentHashing("Bin","127.0.0.1",2);

        //hash them to hash ring
        ConsistentHashRouter<ConsistentHashing> consistentHashRouter = new ConsistentHashRouter<>(Arrays.asList(node1,node2,node3,node4),1);//10 virtual node

        //we have 5 requester ip, we are trying them to route to one service node
        String requestIP1 = "1";
        String requestIP2 = "2";
        String requestIP3 = "3";
        String requestIP4 = "4";
        String requestIP5 = "5";

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        ConsistentHashing node5 = new ConsistentHashing("Bin","127.0.0.1",90);//put new service online
        System.out.println("-------------putting new node online " +node5.getKey()+"------------");
        consistentHashRouter.addNode(node5,10);

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        consistentHashRouter.removeNode(node3);
        System.out.println("-------------remove node online " + node3.getKey() + "------------");
        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);
        System.out.printf("---------------\n");
        String binNum = goRoute(consistentHashRouter,"adsdas");
        System.out.printf(binNum);
        //LognormalDistr lognormalDistr = new LognormalDistr(new Random(),2,3);
        //System.out.printf("sample:%f\n",lognormalDistr.sample());
        //System.out.printf("sample:%f\n",lognormalDistr.sample());
    }
    private static void GetBall(ConsistentHashRouter<ConsistentHashing> consistentHashRouter ,String ... requestIps){
        for (String requestIp: requestIps) {
            System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        }
    }

    private static void goRoute(ConsistentHashRouter<ConsistentHashing> consistentHashRouter ,String ... requestIps){
        for (String requestIp: requestIps) {
            System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        }
    }
    public static String goRoute(ConsistentHashRouter<ConsistentHashing> consistentHashRouter ,String requestIp){
        System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        return ""+consistentHashRouter.routeNode(requestIp);
    }
    public static int GetBin(ConsistentHashRouter<ConsistentHashing> consistentHashRouter ,String requestIp){
        System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        return consistentHashRouter.routeNodeCH(requestIp).getPort();
    }
    public int getPort() {
    	return this.port;
    }
}
