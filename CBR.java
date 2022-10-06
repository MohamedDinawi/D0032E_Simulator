package Sim;

import java.io.IOException;

public class CBR extends Node {

    protected int nrOfPackets;
    private int _toNetwork = 0;
    private int _seq;
    private int _toHost = 0;
    private int timeInterval;


    public CBR(int network, int node) {
        super(network, node);
    }


    //**********************************************************************************
    // Just implemented to generate some traffic for demo.
    // In one of the labs you will create some traffic generators


    public void StartSending(int network, int node, int nrOfPackets, int timeInterval) {
        this.nrOfPackets = nrOfPackets;
        this._toNetwork = network;
        this._toHost = node;
        this.timeInterval = timeInterval;
        _seq = 1;
        send(this, new TimerEvent(), 0);
        System.out.println("starts sending...");

    }

//**********************************************************************************

    public void recv(SimEnt src, Event ev) {
        if (ev instanceof TimerEvent) {

            if (SimEngine.getTime() < timeInterval) {

                double time = SimEngine.getTime();
                for (int i = 0; i < nrOfPackets; i++) {
                    time += 1.0 / nrOfPackets;
                    try {
                        Sink.toFile("CBR.txt", time);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    _sentmsg++;
                    send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
                    System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq + " at time " + SimEngine.getTime());
                    _seq++;

                }
                send(this, new TimerEvent(), 1);
            }

        }
        if (ev instanceof Message) {
            System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

        }
    }
}
