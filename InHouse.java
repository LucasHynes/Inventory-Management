/**
 * To create an extension of the part class to handle objects marked as inHouse
 *
 * @author Lucas Hynes
 * @since 9/27/20
 * @version 1.0
 */
public class InHouse extends Part {
	private int machineId;

	/**
	 * Calls the constructor for the part class and assigns values, while also defining the values associated with
	 * an object that is marked as inHouse
	 * @param id is the id of the part
	 * @param name is the name of the part
	 * @param price is the price of the part
	 * @param stock is the inventory of the part
	 * @param min is the min of the part
	 * @param max is the max of the part
	 * @param machineId is the machine ID of the InHouse part
	 */
	public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
		super(id, name, price, stock, min, max);
		this.machineId = machineId;
	}

	/**
	 * @return machine id
	 */
	public int getMachineId() {
		return machineId;
	}

	/**
	 * lets the user set the machine id
	 * @param machineId is the machine's id
	 */
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

}
