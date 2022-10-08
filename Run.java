package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{

		Link link1 = new Link();
		Link link2 = new Link();

		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);

		host1.setPeer(link1);
		host2.setPeer(link2);

		Router routeNode = new Router(4);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);


		host1.StartSending(2, 2, 10, 3, 1);
		host1.changeInterface(3,10);

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
