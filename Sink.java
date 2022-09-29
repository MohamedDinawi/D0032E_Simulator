package Sim;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Sink extends Node{

    public Sink(int network, int node) {
        super(network, node);
    }
    public static void toFile(String fileName, double time)
        throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
        writer.write(time + "\n");
        writer.close();
        }
    public void recv(SimEnt src, Event ev)
    {

        if (ev instanceof Message)
        {
            double time = SimEngine.getTime();
            try {
                toFile("Sink_CBR",time );
//                toFile("Sink_G",time );
//                toFile("Sink_P",time );
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }

            System.out.println("SinkNode " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
        }
    }


    }
