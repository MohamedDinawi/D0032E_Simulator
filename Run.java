package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
// 		Link link1 = new Link();
//		Link link2 = new Link();
		Link link1 = new LossyLink(0, 0.5, 0.33);
		Link link2 = new LossyLink(0, 0.5, 0.33);


		// Create two end hosts that will be
		// communicating via the router
//		Node host1 = new Node(1,1);
//		Node host2 = new Node(2,1);
		CBR host1 = new CBR(1,1);
		Sink host2 = new Sink(2,1);
// 		Poisson host1 = new Poisson(1,1);
// 		Poisson host2 = new Poisson(2,1);
// 		Gaussian host1 = new Gaussian(1,1, 10, 5);
// 		Gaussian host2 = new Gaussian(2,1, 10, 5);



//		Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);

//		 Generate some traffic
//		host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
//		host1.StartSending(2, 2, 3, 5, 1);
//		host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
//		host2.StartSending(1, 1, 2, 10, 10);

		//CBR
		host1.StartSending(2, 2, 100, 5);

		//Poisson  has runtime of 10 packets and mean value of 5
		//host1.StartSending(2, 2, 1000, 5.0);

 		//Gaussian
 		//host1.StartSending(2, 2, 1000);
 		//host2.StartSending(2, 2, 10, 10, 5);

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



	}
}
