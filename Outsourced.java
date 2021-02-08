/**
 * To create an extension of the part class to handle objects marked as outsourced
 *
 * @author Lucas Hynes
 * @since 9/27/20
 * @version 1.0
 */
public class Outsourced extends Part {
	private String companyName;

	/**
	 * Calls the constructor for the part class and assigns values, while also defining the values associated with
	 * an object that is marked as outsourced
	 * @param id is the id of the part
	 * @param name is the name of the part
	 * @param price is the price of the part
	 * @param stock is the inventory of the part
	 * @param min is the minimum amount of the part needed to have
	 * @param max is the maximum amount of the part needed to have
	 * @param companyName is the name of the company supplying the part
	 */
	public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
		super(id, name, price, stock, min, max);
		this.setCompanyName(companyName);
	}

	/**
	 * @return company name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * lets the user set the company move
	 * @param companyName is the companies name
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
