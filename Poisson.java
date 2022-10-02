package Sim;

import java.io.IOException;
import java.util.Random;


    public class Poisson extends Node {

        protected int nrOfPackets;
        private int _toNetwork = 0;
        private int _seq;
        private int _toHost = 0;
        private double mean;



        public Poisson(int network, int node) {
            super(network, node);

        }


        //**********************************************************************************
        // Just implemented to generate some traffic for demo.
        // In one of the labs you will create some traffic generators


        public void StartSending(int network, int node, int nrOfPackets, double mean)  {
            this.nrOfPackets = nrOfPackets;
            this._toNetwork = network;
            this._toHost = node;
            this.mean = mean;

            _seq = 1;
            send(this, new TimerEvent(), 0);

        }

//**********************************************************************************

        private double poissonDouble(double mean) {
            Random rnd = new Random();

            int x = 0;
            double a = rnd.nextDouble();
            double p = Math.exp(-mean);

            while (a > p) {
                x++;
                a = a - p;
                p = p * mean / x;
            }
            return x;
        }

        public void recv(SimEnt src, Event ev) {
            if (ev instanceof TimerEvent) {

                if (nrOfPackets > _sentmsg) {

                    for (int i = 0; i < nrOfPackets; i++) {

                        double poissonD = poissonDouble(mean);


                        try {
                            Sink.toFile("poissonD.txt", poissonD/100);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        _sentmsg++;
                        send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), poissonD/100);
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

