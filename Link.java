//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Sim;

public class Link extends SimEnt {
	private SimEnt _connectorA = null;
	private SimEnt _connectorB = null;
	private int _now = 0;

	public Link() {
	}

	public void setConnector(SimEnt connectTo) {
		if (this._connectorA == null) {
			this._connectorA = connectTo;
		} else {
			this._connectorB = connectTo;
		}

	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof Message || ev instanceof ChangeInterface) {
			System.out.println("Link recv msg, passes it through");
			if (src == this._connectorA) {
				this.send(this._connectorB, ev, (double)this._now);
			} else {
				this.send(this._connectorA, ev, (double)this._now);
			}
		}

	}
}
