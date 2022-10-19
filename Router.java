package Sim;
// This class implements a simple router

import java.util.ArrayList;
import java.util.HashMap;

public class Router extends SimEnt {

    private RouteTableEntry[] _routingTable;
    private HashMap<NetworkAddr, NetworkAddr> bindings;
    private int _RID;
    private int _interfaces;
    private int _now = 0;



    // When created, number of interfaces are defined
    Router(int RouterID, int interfaces) {
        this._RID = RouterID;
        _routingTable = new RouteTableEntry[interfaces];
        _interfaces = interfaces;
        bindings = new HashMap<>();
    }

    // This method connects links to the router and also informs the
    // router of the host connects to the other end of the link

    public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node) {
        if (interfaceNumber <= _interfaces) {
            _routingTable[interfaceNumber] = new RouteTableEntry(link, node);
        } else
            System.out.println("Trying to connect to port not in router");

        ((Link) link).setConnector(this);
    }

    // This method searches for an entry in the routing table that matches
    // the network number in the destination field of a messages. The link
    // represents that network number is returned

    private SimEnt getInterfaceA(int networkAddress) {
        SimEnt routerInterface = null;
        for (int i = 0; i < _interfaces; i++)
            if (_routingTable[i] != null) {
                if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress) {
                    routerInterface = _routingTable[i].link();
                }
            }
        return routerInterface;
    }

    private SimEnt getInterface(NetworkAddr addr) {
        SimEnt routerInterface;
        for (int i = 0; i < _interfaces; i++)
            if (_routingTable[i] != null) {
                SimEnt dev = _routingTable[i].node();

                if (dev instanceof Node node) {
                    if (node.getAddr().equals(addr)) {
                        routerInterface = _routingTable[i].link();
                        return routerInterface;
                    }
                } else if (dev instanceof Router router) {

                    if (router._RID == addr.networkId()) {
                        routerInterface = _routingTable[i].link();
                        return routerInterface;
                    }
                }
            }

        // not found
        System.out.println("NOT FOUND");
        return null;
    }

    private void changeInterface(NetworkAddr source, int newInterfaceNumber) {
        if (this._routingTable[newInterfaceNumber] != null) {
            System.out.println("The interface is not available");
        } else {
            for (int i = 0; i < this._interfaces; ++i) {
                if (((Node) this._routingTable[i].node()).getAddr() == source) {
                    RouteTableEntry temp = this._routingTable[i];
                    this._routingTable[i] = null;
                    this._routingTable[newInterfaceNumber] = temp;
                    break;
                }
            }
        }
    }

    /// Returns a node id that's not currently being used
    private int newNodeId() {
        int nid = 0;

        while (true) {
            boolean taken = false;
            for (RouteTableEntry entry : _routingTable) {
                if (entry == null) {
                    continue;
                }

                SimEnt dev = entry.node();

                if (dev instanceof Node node) {

                    if (node._id.nodeId() == nid) {
                        taken = true;
                        break; // try another id
                    }
                }
            }

            if (!taken) {
                return nid;
            }
        }
    }

    public RouteTableEntry[] get_routingTable() {
        return _routingTable;
    }

    public void printAllInterfaces(RouteTableEntry[] _routingTable) {
        System.out.println("============================================");
        System.out.println("Node table for R" + _RID);
        for (int i = 0; i < _routingTable.length; i++) {
            try {
                System.out.println("Interface " + i + " has node: " + ((Node) this._routingTable[i].node()).getAddr().networkId() + ". " + ((Node) this._routingTable[i].node()).getAddr().nodeId());
            } catch (Exception e) {
                if (_routingTable[i] == null) {
                    System.out.println("Interface " + i + " is null");
                } else {
                    System.out.println("Interface " + i + ": RouterID: " + ((Router) _routingTable[i].node())._RID);
                }
            }
        }
        System.out.println("============================================");
    }

    private int nextFreeSlot() {
        int i = 0;
        for (RouteTableEntry entry : _routingTable) {
            if (entry == null) {
                return i;
            }
            i++;
        }
        return -1;
    }

    // When messages are received at the router this method is called
    public void recv(SimEnt source, Event event) {
        if (event instanceof ChangeInterface) {

//            this.printAllInterfaces();
            this.changeInterface(((ChangeInterface) event).getSource(), ((ChangeInterface) event).getNewInterfaceNumber());
//            this.printAllInterfaces();

        }

//        if (event instanceof Message)
//        {
//            System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
//            SimEnt sendNext = getInterfaceA(((Message) event).destination().networkId());
//            System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId());
//            send (sendNext, event, _now);
//
//        }

        if (event instanceof Message m) {

            NetworkAddr msource = m.source();
            NetworkAddr mdestination = m.destination();
            NetworkAddr coa = bindings.get(mdestination);
            if (coa != null) {
                // tunnel message to the care-of address
                System.out.println("HA: Tunneling message from " + mdestination.toString() + " to " + coa);
                mdestination = coa;
                m.setDestination(coa);
            }

            System.out.println("Router " + _RID + " handles packet with seq: " + m.seq() + " from node: " + msource);

            SimEnt sendNext = getInterface(mdestination);

            if (sendNext == null) {
                System.err.println("Router " + _RID + ": host " + mdestination + " is unreachable");
            } else {
                System.out.println("Router sends to node: " + mdestination.toString());

                send(sendNext, event, _now);

            }
        }


        // Registration request by a mobile node
        if (event instanceof RegistrationRequest request) {

            Node mn = (Node) source;
            Router fa = this;

            // Network id
            int nid = fa._RID;

            // Start of the registration request
            NetworkAddr hoa = mn.getAddr();
            System.out.println(mn + " is migrating to network " + nid);

            // update IP address
            mn._id = new NetworkAddr(nid, newNodeId());
            NetworkAddr coa = mn.getAddr();
            System.out.println("Node with home address " + hoa.toString() + " has been assigned the care-of address " + coa.toString());

            // Update the node's link
            Link l = new Link();
            mn.setPeer(l);

            // Add the mobile node to the routing table of the foreign agent
            int freeSpot = nextFreeSlot();
            fa.connectInterface(freeSpot, l, mn);

            // Create a binding in the home agent routing table
            Router ha = request.homeAgent();
            ha.bindings.put(hoa, coa);
        }
    }
}
