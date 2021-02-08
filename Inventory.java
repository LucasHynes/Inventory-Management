import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to manage the whole of all the products and parts entered
 *
 * @author Lucas Hynes
 * @since 9/27/20
 * @version 1.0
 */
public class Inventory {
	private static ObservableList<Part> allParts;
	private static ObservableList<Product> allProducts;

	/**
	 * This is the constructor, initializes the two arrays of parts and products
	 */
	Inventory() {
		allParts = FXCollections.observableArrayList();
		allProducts = FXCollections.observableArrayList();
	}

	/**
	 * used to add part to the class
	 * @param newPart is the new part added to the inventory
	 */
	public static void addPart(Part newPart) {
		allParts.add(newPart);
	}

	/**
	 * used to add a product to the class
	 * @param newProduct is the new product being added to the inventory
	 */
	public static void addProduct(Product newProduct) {
		allProducts.add(newProduct);
	}

	/**
	 * used to look up a part by the part id
	 * @param partId is the part to be searched for
	 * @return any matching part found, or null if otherwise
	 */
	public static Part lookupPart(int partId) {
		Part tempInHouse = new InHouse(0, "", 0,0,0,0,0);
		Part tempOutsourced = new Outsourced(0, "", 0,0,0,0,"");
		for(int i = 0; i < allParts.size(); i++) {
			//looks through trying to find the part by checking it's id
			if(allParts.get(i).getId() == partId) { return allParts.get(i); }
		}
		return null;
	}

	/**
	 * used to look up a product by the product id
	 * @param productId is the id of the product being searched for
	 * @return any matching product found, or null if otherwise
	 */
	public static Product lookupProduct(int productId) {
		for(int i = 0; i < allProducts.size(); i++) {
			//looks through trying to find the part by checking it's id
			if(allProducts.get(i).getId() == productId) { return(allProducts.get(i)); }
		}
		return null;
	}

	/**
	 * used to look up a part by the name of the part that was input
	 * @param partName is the name that the part has
	 * @return the part that's name matches the input name
	 */
	public static Part lookupPart(String partName) {
		for(int i = 0; i < allParts.size(); i++) {
			//looks through trying to find the part by checking it's name
			if(allParts.get(i).getName().contains(partName)) { return allParts.get(i); }
		}
		//if part is not found
		return null;
	}

	/**
	 * used to look up a product by a name entered by the user
	 * @param productName is the name submitted by the user
	 * @return the product with the matching name
	 */
	public static Product lookupProduct(String productName) {
		for(int i = 0; i < allProducts.size(); i++) {
			//looks through trying to find the product by checking it's id
			if(allProducts.get(i).getName().contains(productName)) { return allProducts.get(i); }
		}
		//if product is not found
		return null;
	}

	/**
	 * used to update a part, changing the part stored at a certain index
	 * @param index is the index at which the part will be switched
	 * @param selectedPart is the part that is being added to the inventory
	 */
	public static void updatePart(int index, Part selectedPart) {
		//attempts to set the given part at the given index
		try { allParts.set(index, selectedPart); }
		//if there is a null pointer, print the message
		catch(NullPointerException e) {
			System.out.println("Error thrown, index " + index + " : NULL POINTER EXCEPTION " + e);
		}
	}

	/**
	 * used to update the product at a given index with a new product
	 * @param index is the index at which the new product will be
	 * @param newProduct is the product being updated
	 */
	public static void updateProduct(int index, Product newProduct) {
		//attempts to set the new product at the index given
		try { allProducts.set(index, newProduct); }
		//if there was an error, message is thrown
		catch(NullPointerException e) {
			System.out.println("Error thrown, index " + index + " : NULL POINTER EXCEPTION " + e);
		}
	}

	/**
	 * used to delete a selected part
	 * @param selectedPart is the part to be deleted
	 * @return the status of the deletion
	 */
	public static boolean deletePart(Part selectedPart) {
		//attempts to delete the given part
		try {
			allParts.remove(selectedPart);
			return true;
		}
		//if failed, exception and message thrown
		catch(NullPointerException e){
			System.out.println("Error thrown, part " + selectedPart + " NULL POINTER EXCEPTION " + e);
		}
		return false;
	}

	/**
	 * used to delete a selected product
	 * @param selectedProduct is the product to be delete
	 * @return the status of the deletion
	 */
	public static boolean deleteProduct(Product selectedProduct) {
		//attempts to delete the given product
		try {
			allProducts.remove(selectedProduct);
			return true;
		}
		//if failed throws exception
		catch (NullPointerException e) {
			System.out.println("Error thrown, product " + selectedProduct + " : NULL POINTER EXCEPTION " + e);
		}
		return false;
	}

	/**
	 * returns all of the parts
	 * @return allParts
	 */
	public static ObservableList<Part> getAllParts() {
		return allParts;
	}

	/**
	 * returns all of the products
	 * @return allProducts
	 */
	public static ObservableList<Product> getAllProducts() {
		return allProducts;
	}
	
}
