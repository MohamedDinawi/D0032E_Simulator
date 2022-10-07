//package Sim;
//
//import java.io.IOException;
//import java.util.Random;
//
//public class Gaussian extends Node {
//
//    protected int nrOfPackets;
//    private int _toNetwork =_toNetwork 0;
//    private int _seq;
//    private int _toHost = 0;
//    private final Random rnd;
//    private final double mean;
//    private final double deviation;
//
//
//    public Gaussian(int network, int node, double mean, double deviation) {
//        super(network, node);
//        rnd = new Random();
//        this.mean = mean;
//        this.deviation = deviation;
//    }
//
//
//    //**********************************************************************************
//    // Just implemented to generate some traffic for demo.
//    // In one of the labs you will create some traffic generators
//
//
//    public void StartSending(int network, int node, int nrOfPackets) {
//        this.nrOfPackets = nrOfPackets;
//        this._toNetwork = network;
//        this._toHost = node;
//
//        _seq = 1;
//        send(this, new TimerEvent(), 0);
//
//    }
//
////**********************************************************************************
//
//    public void recv(SimEnt src, Event ev) {
//        if (ev instanceof TimerEvent) {
//
//            if (nrOfPackets > _sentmsg) {
//
//
//                for (int i = 0; i < nrOfPackets; i++) {
//
//                    double gaussianDelay = rnd.nextGaussian() * deviation + mean;
//                    System.out.println("starts sending...1");
//                    try {
//                        Sink.toFile("Gaussian.txt", gaussianDelay);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    _sentmsg++;
//                    send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), gaussianDelay);
//                    System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq + " at time " + SimEngine.getTime());
//                    _seq++;
//
//                }
//                send(this, new TimerEvent(), 1);
//            }
//
//        }
//        if (ev instanceof Message) {
//            System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
//
//        }
//    }
//}
