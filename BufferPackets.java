package Sim;

public class BufferPackets implements Event{
    private NetworkAddr source;
    private NetworkAddr destination;

    BufferPackets ()
    {

    }

    public NetworkAddr getSource()
    {
        return source;
    }

    public NetworkAddr getDestination()
    {
        return destination;
    }

    public void entering(SimEnt locale)
    {
    }
}


//send seq nr
//dest
//soure