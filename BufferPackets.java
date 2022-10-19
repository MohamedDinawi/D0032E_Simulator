package Sim;

import java.util.ArrayList;

public class BufferPackets implements Event{
    private NetworkAddr source;
    private NetworkAddr destination;
    private ArrayList<Integer> packetsToSend = new ArrayList<>();

    BufferPackets (NetworkAddr source,NetworkAddr destination, ArrayList<Integer> packetsToSend)
    {
        this.source = source;
        this.destination = destination;
        this.packetsToSend = packetsToSend;
    }

    public NetworkAddr getSource()
    {
        return source;
    }

    public NetworkAddr getDestination()
    {
        return destination;
    }

    public ArrayList<Integer> getPacketsToSend()
    {
        return packetsToSend;
    }

    public void entering(SimEnt locale)
    {
    }
}


//send seq nr
//dest
//soure