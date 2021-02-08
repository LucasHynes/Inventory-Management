import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * To create a class that is to hold a product that has an id, a name, a price, a stock level,
 * a minimum amount, a maximum amount, and a flexible list of parts that can be associated with the product.
 * This class is able to be edited with every feature able to retrieve and display the information
 *
 * @author Lucas Hynes
 * @since 9/27/20
 * @version 1.0
 */
public class Product {
	//list of the associated parts for the product
	private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
	private int id;
	private String name;
	private double price;
	private int stock;
	private int min;
	private int max;

	/**
	 * this is the constructor for the class
	 * @param id is the id of the product
	 * @param name is the name of the product
	 * @param price is the price of the product
	 * @param stock is the stock level of the product
	 * @param min is the minimum amount of the product
	 * @param max is the maximum amount of the product
	 */
	public Product(int id, String name, double price, int stock, int min, int max) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.min = min;
		this.max = max;
	}

	/**
	 * lets the user set the id
	 * @param id is the id of the product
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * lets the user set the name
	 * @param name is the name of the product
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * lets the user set the name
	 * @param price is the price of the product
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * lets the user set the stock
	 * @param stock is the stock of the product
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * lets the user set the minimum amount of the product
	 * @param min is the minimum amount necessary of the product
	 */
	public void setMin(int min) {

		//validating input from the user
		if(min < this.max){
			this.min = min;
		}
		else{
			this.min = 0;
		}
	}

	/**
	 * lets the user set the maximum amount needed of the product
	 * @param max is the maximum amount of product
	 */
	public void setMax(int max) {
		//validating input from the user
		if(max > this.min) {
			this.max = max;
		}
		else{
			//default set amount for max
			this.max = 10;
		}
	}

	/**
	 * @return id is the product's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return name is the product's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return price is the product's price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return stock is the amount of product on hand
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * @return min is the minimum amount of product needed
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @return max is the maximum amount of product needed
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param part is an object of the class Part, and it is being added to the list of associated parts
	 */
	public void addAssociatedPart(Part part) {
		associatedParts.add(part);
	}

	/**
	 * lets the user insert a part that is in the array of parts for that product, and the part is then removed from
	 * the product
	 * @param part is the associated part to be deleted
	 * @return boolean value representing the success or failure of the algorithm
	 */
	public boolean deleteAssociatedPart(Part part) {
		//checks to make sure its a valid part
		if(part != null) {
			//loops through all associated parts and checks to find a match
			for (int i = 0; i < associatedParts.size(); i++) {
				if(associatedParts.get(i).getId() == part.getId()){
					//once the match is found return true, escaping the loop and function call
					associatedParts.remove(i);
					return true;
				}
			}
		}
		//if going through the list does not return a single match, false is returned as there was no
		//matching part in the list
		return false;
	}

	/**
	 * @return associatedParts is a list of all of the previously added associated parts
	 */
	public ObservableList<Part> getAllAssociatedParts() {
		return associatedParts;

	}
}
