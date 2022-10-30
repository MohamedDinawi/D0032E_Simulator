package Sim;

import java.util.ArrayList;

public class Mobility {
    public static void main (String [] args)
    {
        // Links
        LossyLink a = new LossyLink(0,0,0);
        LossyLink b = new LossyLink(0,0,0.55);
        Link r = new LossyLink(0,0,0);
        Link c = new LossyLink(0,0,0);

        // Hosts
        Node A = new Node(1, 1);
        Node B = new Node(1, 2);
        Node C = new Node(2, 3);

        //Connect hosts to links


        // Routers
        Router R1 = new Router(1, 4);
        Router R2 = new Router(2, 4);


        A.setPeer(a);
        B.setPeer(b);
        C.setPeer(c);

        // Wire up routers
        R1.connectInterface(0, a, A);
        R1.connectInterface(1, b, B);
        R1.connectInterface(2, r, R2);

        R2.connectInterface(0, c, C);
        R2.connectInterface(0, r, R1);


        // migrate B to network 2
        B.send(R2, new RegistrationRequest(R1), 0);


        A.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(), 3, 10, 0, 10);


//        homeagent.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(),10, 100, list);


//        C.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(), 2, 40, 2, 10);
//        B.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(), 2, 40, 4, 20);





        // Start the simulation engine and of we go!
        Thread t=new Thread(SimEngine.instance());

        t.start();
        try
        {
            t.join();
        }
        catch (Exception e)
        {
            System.out.println("The motor seems to have a problem, time for service?");
        }
//        R1.printAllInterfaces(R1.get_routingTable());
//        R2.printAllInterfaces(R2.get_routingTable());


    }
}

