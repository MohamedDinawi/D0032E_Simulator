package Sim;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public class LossyLink extends Link {
    private double delay;
    private double probability;
    private Random rnd;
    private double jitter;
    private double prevDelay;

    private int droppedPacketCounter;
    private int sentPackets;
    List jitterList = new ArrayList();
    public LossyLink(double delay, double jitter, double probability) {

        this.delay = delay;
        this.jitter = jitter;
        this.probability = probability;
        this.rnd = new Random();
    }

    public void recv(SimEnt src, Event ev) {

        if (ev instanceof Message) {
            sentPackets++;
            double rnd1 = Math.random();
            if (rnd1 > probability) {
                double nextdelay = Math.abs(this.delay + rnd.nextDouble(jitter + 1));
                System.out.println("The next delay is  :" + nextdelay);

                System.out.println("Link recv msg with seq: " +
                        ((Message) ev).seq() + " , passes it through");

                if (src == _connectorA) {

                    jitter = Math.abs(nextdelay - prevDelay); //add comments
                    System.out.println("jitter from A :" + jitter);
                    jitterList.add(jitter);
                    prevDelay = nextdelay;

                    send(_connectorB, ev, nextdelay);

                } else {
                    jitter = Math.abs(nextdelay - prevDelay);
                    System.out.println("jitter from B :" + jitter);
                    prevDelay = nextdelay;

                    send(_connectorA, ev, nextdelay);
                }
            } else {
                droppedPacketCounter++;

            }

        }
        System.out.println(droppedPacketCounter + "out of" + sentPackets);
        System.out.println(jitterList);
    }
}
//System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
//        " sent message with seq: "+_seq + " at time "+SimEngine.getTime());