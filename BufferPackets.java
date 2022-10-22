package Sim;

import java.util.ArrayList;

public class BufferPackets extends SimEnt implements Event{

    private  NetworkAddr router_ID1;
    protected SimEnt _peer;
    private int _stopSendingAfter = 0;      //messages
    private int _timeBetweenSending = 10;   //time between messages
    private int _toNetwork = 0;             // Sets the peer to communicate with. This node is single homed
    private int _toHost = 0;
    protected int _sentmsg = 0;
    ArrayList<Integer> listOfPacketsToSend;
    protected NetworkAddr _id;



    BufferPackets(Router router_ID, int network, int node)
    {
        super();
        _id = new NetworkAddr(network, node);
        router_ID1 =  new NetworkAddr( router_ID.getRouter_ID(), 9);

    }

    public void StartSending(int network, int node,  int timeInterval, int delay, ArrayList<Integer> packetsToSend) {
        listOfPacketsToSend = packetsToSend;
        _stopSendingAfter = 1;
        _timeBetweenSending = timeInterval;
        _toNetwork = network;
        _toHost = node;


        send(this, new TimerEvent(), delay);
    }
    public void setPeerB(SimEnt peer) {
        _peer = peer;

        if (_peer instanceof Link) {
            ((Link) _peer).setConnector(this);
        }
    }
    public NetworkAddr getAddr() {
        return _id;
    }
    public void entering(SimEnt locale)
    {
    }
    public String toString() {
        return "Node(" + getAddr().toString() + ")";
    }

    public void recv(SimEnt source, Event event) {

        if (event instanceof TimerEvent) {

            if (_stopSendingAfter > _sentmsg) {
                for (int i = 0; i < listOfPacketsToSend.size(); i++) {

                    int seq = listOfPacketsToSend.get(i);
                    send(_peer, new Message(router_ID1, new NetworkAddr(_toNetwork, _toHost), seq), 100);
                    send(this, new TimerEvent(), _timeBetweenSending);
                    System.out.println("Node " + router_ID1.networkId() + "." + router_ID1.nodeId() + " sent message with seq: " + seq + " at time " + SimEngine.getTime());

                }
                _sentmsg++;

            }
        }

        if (event instanceof Message) {
            System.out.println("Node " + router_ID1.networkId() + "." + router_ID1.nodeId() + " receives message with seq: " + ((Message) event).seq() + " at time " + SimEngine.getTime());
            System.out.println("RECEIVED " + ((Message) event).seq());
//            System.out.println();

        }
    }
}