package Sim;

import java.util.ArrayList;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
    static protected ArrayList<Integer> sentPackets = new ArrayList<>();
    protected NetworkAddr _id;
    protected SimEnt _peer;
    protected int _sentmsg = 0;
    protected int _seq = 0;
    private int _msgSent;
    private int _newInterfaceNumber;
    private int _stopSendingAfter = 0;      //messages
    private int _timeBetweenSending = 10;   //time between messages
    private int _toNetwork = 0;             // Sets the peer to communicate with. This node is single homed
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


    public String toString() {
        return "Node(" + getAddr().toString() + ")";
    }

//**********************************************************************************


//    public ArrayList<Integer> getReceivedPackets() {
//        return receivedPackets;
//    }

    // This method is called upon that an event destined for this node triggers.
    public void recv(SimEnt src, Event ev) {


        if (ev instanceof TimerEvent) {

            if(sentPackets.size() >0) {
                send(_peer, new BufferPackets(), 0);
            }

            if (_stopSendingAfter > _sentmsg ) {
                System.out.println();
                _sentmsg++;
                send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 10);
                send(this, new TimerEvent(), _timeBetweenSending);
                System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq + " at time " + SimEngine.getTime());
                sentPackets.add(_seq);
                System.out.println(sentPackets);
                _seq++;

            }
        }
        if (ev instanceof Message) {
            System.out.println();
            System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

            if (!sentPackets.isEmpty()){
                sentPackets.remove(_seq);
                System.out.println(sentPackets);}
            else {
                System.out.println("All packets have been sent");
            }


        }
    }

}

//
////Change interface after _msgSent amount massages
//                if (_sentmsg == _msgSent) {
//                        System.out.println("Change interface " + _id.networkId() + "." + _id.nodeId() + " changing to interface " + _newInterfaceNumber);
//                        send(_peer, new ChangeInterface(_id, _newInterfaceNumber), 0);
//
//                        }
