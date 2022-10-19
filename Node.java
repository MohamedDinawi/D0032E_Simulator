package Sim;
import java.util.ArrayList;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
    protected NetworkAddr _id;
    protected SimEnt _peer;
    protected int _sentmsg = 0;
    protected int _seq = 0;
    private int _msgSent;
    private int _newInterfaceNumber;
    private int _stopSendingAfter = 0; //messages
    private int _timeBetweenSending = 10; //time between messages
    private int _toNetwork = 0;

    protected ArrayList<Integer> packetsRecv = new ArrayList<>();
    protected ArrayList<Integer> sentPackets = new ArrayList<>();


    public ArrayList<Integer> getPacketsToSend() {
        return packetsToSend;
    }

    protected ArrayList<Integer> packetsToSend = new ArrayList<>();


    private boolean isRecv = true;

    // Sets the peer to communicate with. This node is single homed
    private int _toHost = 0;


    public Node(int network, int node) {
        super();
        _id = new NetworkAddr(network, node);
    }

//**********************************************************************************
    // Just implemented to generate some traffic for demo.
    // In one of the labs you will create some traffic generators


    public void setPeer(SimEnt peer) {
        _peer = peer;

        if (_peer instanceof Link) {
            ((Link) _peer).setConnector(this);
        }
    }

    public NetworkAddr getAddr() {
        return _id;
    }

    public void StartSending(int network, int node, int number, int timeInterval, int startSeq, int delay) {
        _stopSendingAfter = number;
        _timeBetweenSending = timeInterval;
        _toNetwork = network;
        _toHost = node;
        _seq = startSeq;
        send(this, new TimerEvent(), delay);
    }

    public void changeInterface(int interfaceNumber, int packetsSent) {
        _msgSent = packetsSent;
        _newInterfaceNumber = interfaceNumber;
    }
    public void bufferPackets( ArrayList<Integer> packetsToSend, int packetsSent) {
        _msgSent = packetsSent;
        sentPackets = packetsToSend;
    }

    public String toString() {
        return "Node(" + getAddr().toString() + ")";
    }

//**********************************************************************************


    // This method is called upon that an event destined for this node triggers.

    public ArrayList<Integer> getPacketsRecv() {
        return packetsRecv;
    }

    public void recv(SimEnt src, Event ev) {
        if (ev instanceof TimerEvent) {

            if (_stopSendingAfter > _sentmsg) {
                _sentmsg++;
                System.out.println();
                send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
                send(this, new TimerEvent(), _timeBetweenSending);
                System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq + " at time " + SimEngine.getTime());
                isRecv = false;
                sentPackets.add(_seq);


                if (_sentmsg == _msgSent) {
                    System.out.println("Resending Lost Packets from " + _id.networkId() + "." + _id.nodeId() + " To " + _toNetwork);
                    send(_peer, new BufferPackets(_id, new NetworkAddr(_toNetwork, _toHost), packetsToSend), 0);

                }


                //Change interface after _msgSent amount massages
//                if (_sentmsg == _msgSent) {
//                    System.out.println("Change interface " + _id.networkId() + "." + _id.nodeId() + " changing to interface " + _newInterfaceNumber);
//                    send(_peer, new ChangeInterface(_id, _newInterfaceNumber), 0);
//
//                }



                _seq++;
            }
        }
        if (ev instanceof Message) {
            System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
            System.out.println("RECEIVED" + ((Message) ev).seq());
            System.out.println();
            packetsRecv.add(((Message) ev).seq());
            isRecv = true;

    }
}

}
