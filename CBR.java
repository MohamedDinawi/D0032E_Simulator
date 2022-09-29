package Sim;
public class CBR extends Node {
    private int _stopSendingAfter = 0; //messages
    private int _timeBetweenSending = 10; //time between messages
    private int _toNetwork = 0;
    private int _seq;
    private int _toHost = 0;
    protected int nrOfPackets;

    private int timeInterval;


    public CBR(int network, int node) {
        super(network, node);
    }


    //**********************************************************************************
    // Just implemented to generate some traffic for demo.
    // In one of the labs you will create some traffic generators


    public void StartSending(int network, int node, int number, int timeInterval) {
        _stopSendingAfter = number;
        _timeBetweenSending = timeInterval;
        _toNetwork = network;
        _toHost = node;
        _seq = 1;
        send(this, new TimerEvent(), 0);

    }

//**********************************************************************************

    public void recv(SimEnt src, Event ev) {
        if (ev instanceof TimerEvent) {
            if (SimEngine.getTime() > timeInterval) {
                for (int i = 0; i < nrOfPackets; i++) {
                    double time = SimEngine.getTime();
                    Sink.toFile("CBR.txt", time);
                    send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
                    send(this, new TimerEvent(), _timeBetweenSending);
                    System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq + " at time " + SimEngine.getTime());
                    _seq++;
                }
            }
            if (ev instanceof Message) {
                System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

            }
        }

    }
}
