package Sim;

public class ChangeInterface implements Event{
    private NetworkAddr source;
    private int newInterfaceNumber;

    ChangeInterface (NetworkAddr source, int newInterfaceNumber)
    {
        this.source = source;
        this.newInterfaceNumber = newInterfaceNumber;
    }

    public NetworkAddr getSource()
    {

        return source;
    }

    public int getNewInterfaceNumber()
    {
        return newInterfaceNumber;
    }

    public void entering(SimEnt locale)
    {
    }
}