import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * To design a main form as a GUI for the user to be able to interact with the data structure
 * made in the program, asking for inputs and being able to reach and modify all values and objects within the
 * scope of the project
 *
 * Problem faced while developing was the search bar and making sure to not add the match to the end of the search
 * results and making sure that the chart was empty except for the saved value, while not deleting all dat from the
 * table once the search is complete.
 *
 * Version update ideas include sever communication with outsourced parts to get data imported into the management
 * system without having to create the part manually each time to be able to have a more dynamic and easier application
 * for the user
 *
 * @author Lucas Hynes
 * @version 1.0
 * @since 9/28/20
 */
public class mainForm extends Application {

    //creation and initialization of the two main tables
    private final TableView<Part> parts_table = new TableView<>();
    private final TableView<Product> product_table = new TableView<>();

    private Inventory totalData = new Inventory();

    //Lists that will hold the data that is not held in the abstract Part method and therefore
    //this uses a congruent data system to match the id of the part that needed to be kept
    private final String[] InHouseList = new String[100];
    private final String[] OutsourcedList = new String[100];

    /**
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This is the main form and the function of the various buttons
     * and tables seen during the application
     *
     * @param stage the main stage
     */
    public void start(Stage stage) {
        //setting example data, pre entered into the program
        totalData.addPart(new InHouse(1, "asdf", 22, 23, 1, 43, 234224));
        totalData.addPart(new Outsourced(2, "qwerty", 23.56, 324, 12, 435, "Testing Co."));
        totalData.addProduct(new Product(1, "combo", 43.21, 45, 2, 56));
        totalData.lookupProduct(1).addAssociatedPart(totalData.lookupPart(1));

        //these two lists are used during the search function to hold the results
        //of the matching searched parts
        ObservableList<Part> tempStorage = FXCollections.observableArrayList();
        ObservableList<Product> tempStorage2 = FXCollections.observableArrayList();

        //assignment of the information to match the two examples given as the
        //two String[] arrays above
        InHouseList[1] = "34";
        OutsourcedList[2] = "company Name";

        //Creating the Add Part Button
        Button addPartButton = new Button("Add");
        //Creating the mouse event handler
        EventHandler<MouseEvent> addPartEventHandler = e -> {
            //setting up the attributes to launch the AddPartForm
            Scene addPartScene = addPartForm();
            Stage addPartStage = new Stage();

            //assigning to the proper data
            addPartStage.setScene(addPartScene);

            //setting title and size
            addPartStage.setTitle("Add Part Form");
            addPartStage.setHeight(800);
            addPartStage.setWidth(700);
            addPartStage.show();
        };
        //Registering the event filter
        addPartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, addPartEventHandler);

        //Creating the Modify Part Button
        Button modifyPartButton = new Button("Modify");
        //Creating the mouse event handler
        EventHandler<MouseEvent> modifyPartEventHandler = e -> {
            try {
                //defining the part that is going to be modified
                Part selected = parts_table.getSelectionModel().getSelectedItem();

                //this value is true if the Part is InHouse, and false if the Part is Outsourced
                boolean InHouseCheck = true;
                //holds the value of the inHouse or Outsourced info
                String value = "";

                if (InHouseList[selected.getId()] != null) {
                    //checking if the values have been stored correctly
                    value = InHouseList[selected.getId()];
                    InHouseCheck = true;
                } else if (OutsourcedList[selected.getId()] != null) {
                    //checking if the data has been stored correctly
                    value = OutsourcedList[selected.getId()];
                    InHouseCheck = false;
                }
                //if the value is not in either the in or out arrays, this message would appear in the value
                //for the user to be able to input a correct assignment
                else {
                    value = "Could not find";
                }

                //setting up the values to be passed into the form's function
                Scene modifyPartScene = modifyPartForm(selected, InHouseCheck, value);

                //setting up the basic environment details
                Stage modifyPartStage = new Stage();
                modifyPartStage.setScene(modifyPartScene);
                modifyPartStage.setTitle("Modify Part Form");
                modifyPartStage.setHeight(800);
                modifyPartStage.setWidth(700);
                modifyPartStage.show();
            }
            catch (NullPointerException e1) {
                System.out.println("Error thrown, modified part not selected : NULL POINTER EXCEPTION " + e1);
            }
        };
        //Registering the event filter
        modifyPartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, modifyPartEventHandler);

        //Creating the Delete Part Button
        Button deletePartButton = new Button("Delete");
        //Creating the mouse event handler
        EventHandler<MouseEvent> deletePartEventHandler = e -> {
            try {
                //returns the selected part and removes that part from the data bank
                Part partToDelete = parts_table.getSelectionModel().getSelectedItem();

                //displays popUp information to the user
                Stage popUp = new Stage();
                Label searchFail = new Label("Are you sure you want to delete the part?");
                //Creates yes button
                Button yesButton = new Button("Yes");
                //Creates event handler
                EventHandler<MouseEvent> yesResponse = e1 -> {
                    totalData.deletePart(partToDelete);

                    //displays popUp information to the user
                    Stage popUp2 = new Stage();
                    Label deleteDeniedText = new Label("Part has been deleted");
                    //Creates ok button
                    Button ok = new Button("Ok");
                    //Creates event handler
                    EventHandler<MouseEvent> popUpEvent = e2 -> {
                        //saves the stage and closes
                        popUp2.close();
                    };
                    //registers evenet handler to the button
                    ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                    //sets all children to the sence to be displayed correctly
                    VBox deleteDenined =  new VBox();
                    deleteDenined.getChildren().addAll(deleteDeniedText, ok);
                    deleteDenined.setAlignment(Pos.CENTER);
                    deleteDenined.setPadding(new Insets(10, 10, 10, 10));
                    deleteDenined.setSpacing(20);
                    Scene popUpScene = new Scene(deleteDenined);
                    popUp2.setScene(popUpScene);
                    popUp2.show();
                    
                    popUp.close();
                };
                //registers event handler to the button
                yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, yesResponse);

                //Creates no button
                Button noButton = new Button("No");
                //Creates event handler
                EventHandler<MouseEvent> noResponse = e1 -> {
                    //saves the stage and closes
                    popUp.close();
                };
                //registers evenet handler to the button
                noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, noResponse);

                //sets buttons to be displayed next to each other
                HBox yesNo = new HBox();
                yesNo.getChildren().addAll(yesButton, noButton);
                yesNo.setPadding(new Insets(10,10,10,10));
                yesNo.setSpacing(10);

                //adds the buttons to the bottom of the label
                VBox box = new VBox();
                box.getChildren().addAll(searchFail, yesNo);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(10,10,10,10));
                box.setSpacing(20);

                //adds all parts into a final scene, displayed for the user
                Scene popUpScene = new Scene(box);
                popUp.setScene(popUpScene);
                popUp.show();
            }
            catch(NullPointerException e1) {
                System.out.println("Error thrown, deleted part not selected : NULL POINTER EXCEPTION " + e1);
            }
        };
        //Registering the event filter
        deletePartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, deletePartEventHandler);

        //Creating the Add Product Button
        Button addProductButton = new Button("Add");
        //Creating the mouse event handler
        EventHandler<MouseEvent> addProductEventHandler = e -> {
            //sets the stage for the add part form for the user to be able to add Products
            Scene addProductScene = addProductForm();
            Stage addProductStage = new Stage();
            addProductStage.setScene(addProductScene);
            addProductStage.setTitle("Add Product Form");
            addProductStage.setHeight(600);
            addProductStage.setWidth(1000);
            addProductStage.show();
        };
        //Registering the event filter
        addProductButton.addEventFilter(MouseEvent.MOUSE_CLICKED, addProductEventHandler);

        //Creating the Modify Product Button
        Button modifyProductButton = new Button("Modify");
        //Creating the mouse event handler
        EventHandler<MouseEvent> modifyProductEventHandler = e -> {
            try {
                //returns the users selected product
                Product productToModify = product_table.getSelectionModel().getSelectedItem();

                //sets up the scene returned by the function and displayed correctly
                Scene modifyProductScene = modifyProductForm(productToModify);
                Stage modifyProductStage = new Stage();
                modifyProductStage.setScene(modifyProductScene);
                modifyProductStage.setTitle("Modify Product Form");
                modifyProductStage.setHeight(600);
                modifyProductStage.setWidth(1000);
                modifyProductStage.show();
            }
            catch (NullPointerException e1) {
                System.out.println("Error thrown, modified part not selected : NULL POINTER EXCEPTION " + e1);
            }
        };
        //Registering the event filter
        modifyProductButton.addEventFilter(MouseEvent.MOUSE_CLICKED, modifyProductEventHandler);

        //Creating the Delete Product Button
        Button deleteProductButton = new Button("Delete");
        //Creating the mouse event handler
        EventHandler<MouseEvent> deleteProductEventHandler = e -> {
            try {
                //returns the selected product and removes that product from the data bank
                Product productToDelete = product_table.getSelectionModel().getSelectedItem();

                //displays popUp information to the user
                Stage popUp = new Stage();
                Label searchFail = new Label("Are you sure you want to delete the product?");
                //Creates yes button
                Button yesButton = new Button("Yes");
                //Creates event handler
                EventHandler<MouseEvent> yesResponse = e1 -> {
                    if (productToDelete.getAllAssociatedParts().isEmpty()) {
                        totalData.deleteProduct(productToDelete);

                        //displays popUp information to the user
                        Stage popUp2 = new Stage();
                        Label deleteDeniedText = new Label("Product has been deleted");
                        //Creates ok button
                        Button ok = new Button("Ok");
                        //Creates event handler
                        EventHandler<MouseEvent> popUpEvent = e2 -> {
                            //saves the stage and closes
                            popUp2.close();
                        };
                        //registers evenet handler to the button
                        ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                        //sets all children to the sence to be displayed correctly
                        VBox deleteDenined =  new VBox();
                        deleteDenined.getChildren().addAll(deleteDeniedText, ok);
                        deleteDenined.setAlignment(Pos.CENTER);
                        deleteDenined.setPadding(new Insets(10, 10, 10, 10));
                        deleteDenined.setSpacing(20);
                        Scene popUpScene = new Scene(deleteDenined);
                        popUp2.setScene(popUpScene);
                        popUp2.show();
                    }
                    else {
                        //displays popUp information to the user
                        Stage popUp3 = new Stage();
                        Label deleteAcceptText = new Label("Product cannot be deleted with associated parts!");
                        //Creates ok button
                        Button ok = new Button("Ok");
                        //Creates event handler
                        EventHandler<MouseEvent> popUpEvent = e2 -> {
                            //saves the stage and closes
                            popUp3.close();
                        };
                        //registers evenet handler to the button
                        ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                        //sets all children to the sence to be displayed correctly
                        VBox deleteConfirm = new VBox();
                        deleteConfirm.getChildren().addAll(deleteAcceptText, ok);
                        deleteConfirm.setAlignment(Pos.CENTER);
                        deleteConfirm.setPadding(new Insets(10, 10, 10, 10));
                        deleteConfirm.setSpacing(20);
                        Scene popUpScene = new Scene(deleteConfirm);
                        popUp3.setScene(popUpScene);
                        popUp3.show();
                    }
                    popUp.close();
                };
                //registers event handler to the button
                yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, yesResponse);
                //Creates no button
                Button noButton = new Button("No");
                //Creates event handler
                EventHandler<MouseEvent> noResponse = e1 -> {
                    //saves the stage and closes
                    popUp.close();
                };
                //registers evenet handler to the button
                noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, noResponse);

                //sets buttons to be displayed next to each other
                HBox yesNo = new HBox();
                yesNo.getChildren().addAll(yesButton, noButton);
                yesNo.setPadding(new Insets(10,10,10,10));
                yesNo.setSpacing(10);

                //adds the buttons to the bottom of the label
                VBox box = new VBox();
                box.getChildren().addAll(searchFail, yesNo);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(10,10,10,10));
                box.setSpacing(20);

                //adds all parts into a final scene, displayed for the user
                Scene popUpScene = new Scene(box);
                popUp.setScene(popUpScene);
                popUp.show();
            }
            catch(NullPointerException e1) {
                System.out.println("Error thrown, deleted product not selected : NULL POINTER EXCEPTION " + e1);
            }
        };
        //Registering the event filter
        deleteProductButton.addEventFilter(MouseEvent.MOUSE_CLICKED, deleteProductEventHandler);

        //Creating the Exit Button
        Button exit = new Button("Exit");
        //creating mouse event handler
        EventHandler<MouseEvent> exitMainForm = e -> {
            stage.close();
        };
        //Registering the event filter
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, exitMainForm);

        //sets the tables so that they can not be directly edited by the user
        parts_table.setEditable(false);
        product_table.setEditable(false);

        //defines the Part Id Column and sets the property value factory to be formatted for the Part class
        TableColumn partIdCol = new TableColumn("Part ID");
        partIdCol.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));

        //defines the Part Name Column and sets the property value factory to be formatted for the Part class
        TableColumn partNameCol = new TableColumn("Part Name");
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        //defines the Part Stock Column and sets the property value factory to be formatted for the Part class
        TableColumn partStockCol = new TableColumn("Inventory Level");
        partStockCol.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        //defines the Part Price Column and sets the property value factory to be formatted for the Part class
        TableColumn partPriceCol = new TableColumn("Price/Cost Per Unit");
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        //defines the Product Id Column and sets the property value factory to be formatted for the Product class
        TableColumn productIdCol = new TableColumn("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));

        //defines the Product Name Column and sets the property value factory to be formatted for the Product class
        TableColumn productNameCol = new TableColumn("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        //defines the Product Stock Column and sets the property value factory to be formatted for the Product class
        TableColumn productInventoryCol = new TableColumn("Inventory Level");
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));

        //defines the Product Price Column and sets the property value factory to be formatted for the Product class
        TableColumn productPriceCol = new TableColumn("Price/Cost Per Unit");
        productPriceCol.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));

        //adds all Part Columns into one table, sets the data to be the Part's data bank, and sets the size
        parts_table.getColumns().addAll(partIdCol, partNameCol, partStockCol, partPriceCol);
        parts_table.setItems(totalData.getAllParts());
        parts_table.setPrefSize(400, 150);

        //adds all Product Columns into one table, sets the data to be the Product's data bank, and sets the size
        product_table.getColumns().addAll(productIdCol, productNameCol, productInventoryCol, productPriceCol);
        product_table.setItems(totalData.getAllProducts());
        product_table.setPrefSize(400, 150);

        //label for the header of the Part table
        final Label part_label = new Label("Parts");
        part_label.setFont(new Font("Arial", 20));

        //label for the header of the Product table
        final Label product_label = new Label("Products");
        product_label.setFont(new Font("Arial", 20));

        //label for the header title of the main form
        final Label header = new Label("Inventory Management System");
        header.setStyle("-fx-font-weight: bold;");

        //made to keep the spacing correct for the table layout
        Label blank_one = new Label("\t\t\t\t");
        Label blank_two = new Label("\t\t\t\t");

        //creation of the label for the search bar
        TextField partSearch = new TextField();
        partSearch.setPromptText("Search by Part ID or Name");
        //Creates the key event handler
        partSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //checks the search bar when the user presses the enter button
                if ((keyEvent.getCode() == KeyCode.ENTER && !partSearch.getText().isEmpty())) {

                    //clears all previously held data and reset the table to ensure no duplicates are made
                    parts_table.getItems().removeAll();
                    parts_table.refresh();
                    parts_table.setItems(totalData.getAllParts());
                    tempStorage.removeAll();
                    tempStorage.clear();

                    //looks up given information, checking format
                    try {
                        if (totalData.lookupPart(partSearch.getText()) != null) {
                            tempStorage.add(totalData.lookupPart(partSearch.getText()));
                        }
                    }
                    catch (NullPointerException e) {
                        System.out.println("Error thrown, partSearchBar " + partSearch
                                + " : NULL POINTER EXCEPTION " + e);
                    }

                    try { tempStorage.add(totalData.lookupPart(Integer.parseInt(partSearch.getText()))); }
                    catch (NumberFormatException e) {
                        System.out.println("Error caught, partSearchBar " + partSearch
                                + " : NUMBER FORMAT EXCEPTION " + e);
                    }

                    //removes all values from the table as the comparisons have finished
                    //and results need to be published
                    parts_table.getItems().removeAll();
                    parts_table.setItems(tempStorage);
                    parts_table.refresh();
                    tempStorage.removeAll();

                }
                //if the user has not pressed enter, or if the search bar is empty, makes the table refresh
                else {
                    //refreshing the stored values of the original parts to be shown until another search enter is made 
                    parts_table.getItems().removeAll();
                    parts_table.setItems(totalData.getAllParts());
                    parts_table.refresh();
                    tempStorage.removeAll();
                }
            }
        });

        //creation of the label for the search bar
        TextField productSearch = new TextField();
        productSearch.setPromptText("Search by Product ID or Name");
        //Creates the key event handler
        productSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //checks the search bar when the user presses the enter button
                if ((keyEvent.getCode() == KeyCode.ENTER && !productSearch.getText().isEmpty())) {

                    //clears all previously held data and reset the table to ensure no duplicates are made
                    product_table.getItems().removeAll();
                    product_table.refresh();
                    product_table.setItems(totalData.getAllProducts());
                    tempStorage2.removeAll();
                    tempStorage2.clear();

                    //looks up given information, checking format
                    try {
                        if (totalData.lookupProduct(productSearch.getText()) != null) {
                            tempStorage2.add(totalData.lookupProduct(productSearch.getText()));
                        }
                    }
                    catch (NullPointerException e) {
                        System.out.println("Error thrown, productSearchBar " + productSearch
                                + " : NULL POINTER EXCEPTION " + e);
                    }

                    try { tempStorage2.add(totalData.lookupProduct(Integer.parseInt(productSearch.getText()))); }
                    catch (NumberFormatException e) {
                        System.out.println("Error caught, productSearchBar " + productSearch
                                + " : NUMBER FORMAT EXCEPTION " + e);
                    }

                    //removes all values from the table as the comparisons have finished
                    //and results need to be published
                    product_table.getItems().removeAll();
                    product_table.setItems(tempStorage2);
                    product_table.refresh();
                    tempStorage2.removeAll();

                }
                //if the user has not pressed enter, or if the search bar is empty, makes the table refresh
                else {
                    //refreshing the stored values of the original product to be shown until another
                    // search enter is made
                    product_table.getItems().removeAll();
                    product_table.setItems(totalData.getAllProducts());
                    product_table.refresh();
                    tempStorage2.removeAll();
                }
            }
        });

        //Sets the add, modify, and delete buttons for being together in a line, with proper spacing and padding
        HBox hbox_parts_buttons = new HBox();
        hbox_parts_buttons.setSpacing(5);
        hbox_parts_buttons.setPadding(new Insets(10, 0, 0, 10));
        hbox_parts_buttons.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);

        //Sets the add, modify, and delete buttons for being together in a line, with proper spacing and padding
        HBox hbox_products_buttons = new HBox();
        hbox_products_buttons.setSpacing(5);
        hbox_products_buttons.setPadding(new Insets(10, 0, 0, 10));
        hbox_products_buttons.getChildren().addAll(addProductButton, modifyProductButton, deleteProductButton);

        //sets a GridPane for the parts panel
        GridPane parts_pane = new GridPane();
        //adds the label at the top left
        parts_pane.add(part_label, 0, 0, 1, 1);
        //adds the search bar top right
        parts_pane.add(partSearch, 2, 0, 1, 1);
        //adds the parts table to the middle and spans the grid
        parts_pane.add(parts_table, 0, 1, 3, 1);
        //adds the parts buttons to the bottom right of the grid
        parts_pane.add(hbox_parts_buttons, 2, 2, 1, 1);
        //sets the blank spaces in the middle between the search bar and the header
        parts_pane.add(blank_one, 1, 0, 1, 1);
        //sets the spacing between the elements
        parts_pane.setVgap(10);
        parts_pane.setHgap(10);
        //sets the style settings for the border around the grid
        parts_pane.setStyle("-fx-border-color: black;" +
                "-fx-padding: 10;" +
                "-fx-border-radius: 5;");

        //sets a GridPane for the products panel
        GridPane product_pane = new GridPane();
        //adds the label at the top left
        product_pane.add(product_label, 0, 0, 1, 1);
        //adds the search bar top right
        product_pane.add(productSearch, 2, 0, 1, 1);
        //adds the products table to the middle and spans the grid
        product_pane.add(product_table, 0, 1, 3, 1);
        //adds the products buttons to the bottom right of the grid
        product_pane.add(hbox_products_buttons, 2, 2, 1, 1);
        //sets the blank spaces in the middle between the search part and the header
        product_pane.add(blank_two, 1, 0, 1, 1);
        //sets the spacing between the elements
        product_pane.setVgap(10);
        product_pane.setHgap(10);
        //sets the style settings for the border around the grid
        product_pane.setStyle("-fx-border-color: black;" +
                "-fx-padding: 10;" +
                "-fx-border-radius: 5;");

        //adds together the two charts with all the buttons to make the final pane
        HBox hboxFinalTableBox = new HBox();
        hboxFinalTableBox.setSpacing(50);
        hboxFinalTableBox.setPadding(new Insets(10, 0, 10, 0));
        hboxFinalTableBox.getChildren().addAll(parts_pane, product_pane);

        //makes the finishing additions to the final layout
        GridPane finalLayout = new GridPane();
        finalLayout.add(header, 0, 0, 1, 1);
        finalLayout.add(hboxFinalTableBox, 0, 1, 3, 1);
        finalLayout.add(exit, 3, 3, 1, 1);
        finalLayout.setPadding(new Insets(10, 50, 10, 50));

        //sets the general settings of the main form
        stage.setTitle("Inventory Management System");
        stage.setWidth(1050);
        stage.setHeight(400);

        //setting the final scene to the stage and shown
        Scene scene = new Scene(finalLayout);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This is the form used by the user to add a part to the entire system, while
     * specifying all specific information about the part. returns the entire window
     * with functionality to the main form to be opened with the click of a button
     *
     * @return scene the add part scene
     */
    private Scene addPartForm() {
        //initializes the main stage
        Stage stage = new Stage();

        //sets the labels for the different information needed across the stage
        Label header = new Label("Add Part");
        Label partId = new Label("ID");
        Label partName = new Label("Name");
        Label partCount = new Label("Inv");
        Label partPrice = new Label("Price/Cost");
        Label partMax = new Label("Max");
        Label partMin = new Label("Min");
        Label partMachineID = new Label("Machine ID");

        //sets the companyName visibility to false by default (user is able to change)
        Label partCompanyName = new Label("Company Name");
        partCompanyName.setVisible(false);

        //sets the text field where the user might have put the id for the part, but instead does not show id
        //so that the user is not able to edit a default id that is applied
        TextField partIdAns = new TextField();
        partIdAns.setPromptText("Auto Gen - Disabled");
        partIdAns.setEditable(false);

        //sets the text field answers for the user to be able to fill out
        TextField partNameAns = new TextField();
        TextField partCountAns = new TextField();
        TextField partPriceAns = new TextField();
        TextField partMaxAns = new TextField();
        TextField partMinAns = new TextField();
        TextField partMachineIDAns = new TextField();

        //sets the default to companyName not being visible
        TextField partCompanyNameAns = new TextField();
        partCompanyNameAns.setVisible(false);

        //sets the radio options for the user to pick between, and the default selection to inHouse
        RadioButton inHouse = new RadioButton("In House");
        inHouse.setSelected(true);
        RadioButton Outsourced = new RadioButton("Outsourced");

        //checks to see if the user will decide to switch selections of the radio button
        inHouse.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    //sets the changes in visibility to the input to clear
                    partCompanyName.visibleProperty().set(false);
                    partCompanyNameAns.clear();
                    partCompanyNameAns.visibleProperty().set(false);
                    partMachineID.visibleProperty().set(true);
                    partMachineIDAns.visibleProperty().set(true);
                }
        );

        //checks to see if the user will decide to switch selections of the radio button
        Outsourced.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    //sets the changes in visibility to the input to clear
                    partCompanyName.visibleProperty().set(true);
                    partCompanyNameAns.visibleProperty().set(true);
                    partMachineID.visibleProperty().set(false);
                    partMachineIDAns.clear();
                    partMachineIDAns.visibleProperty().set(false);
                }
        );

        //Creating save button
        Button save = new Button("Save");
        //Creating mouse event handler
        EventHandler<MouseEvent> savePartEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //initializes the variables to temp hold answers
                String partName = "";
                int partStock = 0;
                double partPrice = 0.00;
                int partMax = 0;
                int partMin = 0;
                int machineId = 0;
                String companyName = "";
                int id = 0;

                try { partName = partNameAns.getText(); }
                catch (NullPointerException e1) {
                    System.out.println("Error thrown, partNameAns" + partNameAns
                            + " : NULL POINTER EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partStock = Integer.parseInt(partCountAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCountAns " + partCountAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partPrice = Double.parseDouble(partPriceAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partPriceAns " + partPriceAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partMax = Integer.parseInt(partMaxAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partMaxAns " + partMaxAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partMin = Integer.parseInt(partMinAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCountAns " + partMinAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { machineId = Integer.parseInt(partMachineIDAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partMachineIDAns " + partMachineIDAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { companyName = partCompanyNameAns.getText(); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCompanyNameAns " + partCompanyNameAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //goes through the data on the table and attempts to find the next empty id
                for (int i = 0; !parts_table.getItems().isEmpty() && parts_table.getItems().size() > i; i += 1) {
                    id += 1;
                }

                if ((partStock >= 0 && partPrice >= 0) && (partMin > 0)
                        && (partMin < partStock && partStock < partMax)) {

                    //checks to see if the user has selected to make the part as inHouse
                    if (inHouse.isSelected()) {
                        //makes a temporary inHouse Object to be added to the data bank
                        InHouse temp = new InHouse(id, partName, partPrice, partStock, partMin, partMax, machineId);
                        totalData.addPart(temp);
                        InHouseList[id] = String.valueOf(machineId);
                    }
                    //if the user has not selected the inHouse option then they have selected the Outsourced part option
                    //which is the only other choice because of the toggle group applied to the radio option
                    else {
                        Outsourced temp = new Outsourced(id, partName, partPrice, partStock, partMin, partMax, companyName);
                        totalData.addPart(temp);
                        OutsourcedList[id] = companyName;
                    }
                    //calls to save the stage and then close it, with all the information managed
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                }
                else {
                    //displays popUp information to the user
                    Stage popUp = new Stage();
                    Label Text = new Label("Cannot create product with imporoper input");

                    //Creates ok button
                    Button ok = new Button("Ok");
                    //Creates event handler
                    EventHandler<MouseEvent> popUpEvent = e1 -> {
                        //saves the stage and closes
                        popUp.close();
                    };
                    //registers evenet handler to the button
                    ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                    //sets all children to the sence to be displayed correctly
                    VBox imporperSave = new VBox();
                    imporperSave.getChildren().addAll(Text, ok);
                    imporperSave.setAlignment(Pos.CENTER);
                    imporperSave.setPadding(new Insets(10, 10, 10, 10));
                    imporperSave.setSpacing(20);
                    Scene popUpScene = new Scene(imporperSave);
                    popUp.setScene(popUpScene);
                    popUp.show();
                }
            }
        };
        //Registering the event filter
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, savePartEventHandler);

        //Creating cancel button
        Button cancel = new Button("Cancel");
        EventHandler<MouseEvent> cancelPartEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Stage stage = (Stage) save.getScene().getWindow();
                stage.close();
            }
        };
        //Registering the event filter
        cancel.addEventFilter(MouseEvent.MOUSE_CLICKED, savePartEventHandler);

        //Sets the toggleGroup for the radio buttons
        final ToggleGroup inOrOut = new ToggleGroup();
        inHouse.setToggleGroup(inOrOut);
        Outsourced.setToggleGroup(inOrOut);

        //initializes the main section of the form
        GridPane mainSection = new GridPane();

        //adds all the labels to the left side descending
        mainSection.add(partId, 0, 0, 1, 1);
        mainSection.add(partName, 0, 1, 1, 1);
        mainSection.add(partCount, 0, 2, 1, 1);
        mainSection.add(partPrice, 0, 3, 1, 1);
        mainSection.add(partMax, 0, 4, 1, 1);
        mainSection.add(partMin, 2, 4, 1, 1);
        mainSection.add(partMachineID, 0, 5, 1, 1);
        mainSection.add(partCompanyName, 0, 5, 1, 1);

        //adds all the spaces for the user input to be entered
        mainSection.add(partIdAns, 1, 0, 1, 1);
        mainSection.add(partNameAns, 1, 1, 1, 1);
        mainSection.add(partCountAns, 1, 2, 1, 1);
        mainSection.add(partPriceAns, 1, 3, 1, 1);
        mainSection.add(partMaxAns, 1, 4, 1, 1);
        mainSection.add(partMinAns, 3, 4, 1, 1);
        mainSection.add(partMachineIDAns, 1, 5, 1, 1);
        mainSection.add(partCompanyNameAns, 1, 5, 1, 1);

        //sets the main section spacing and padding
        mainSection.setHgap(10);
        mainSection.setVgap(10);
        mainSection.setPadding(new Insets(10, 10, 10, 10));

        //sets the spacing for the radio buttons
        HBox radioOption = new HBox();
        radioOption.getChildren().addAll(inHouse, Outsourced);
        radioOption.setPadding(new Insets(10, 50, 10, 50));
        radioOption.setSpacing(50);

        //sets the spacing for the buttons featured
        HBox buttonOption = new HBox();
        buttonOption.getChildren().addAll(save, cancel);
        buttonOption.setSpacing(25);
        radioOption.setPadding(new Insets(0, 10, 0, 10));

        //adds the final pieces of the form together into the final pane, while adding spacing and padding
        GridPane finalPane = new GridPane();
        finalPane.add(header, 0, 0, 1, 1);
        finalPane.add(radioOption, 1, 0, 1, 1);
        finalPane.add(mainSection, 0, 1, 3, 1);
        finalPane.add(buttonOption, 2, 2, 1, 1);
        finalPane.setPadding(new Insets(50, 50, 50, 50));
        finalPane.setVgap(10);
        finalPane.setHgap(50);

        //sets the settings for the final form
        stage.setTitle("Add Part Form");
        stage.setHeight(800);
        stage.setWidth(700);
        Scene scene = new Scene(finalPane);
        return scene;
    }

    /**
     * This is the form used by the user to modify a already present value within the system
     * and be able to save and apply any changes made within the form
     *
     * @param modifiedPart   the part that is going to be modified
     * @param inHouseCheck   the boolean value of whether the part is inHouse(T) or outsourced(F)
     * @param valueOfInOrOut the value as a String of either the companyName or machineId
     * @return scene the modify part form
     */
    private Scene modifyPartForm(Part modifiedPart, boolean inHouseCheck, String valueOfInOrOut) {
        //initializes the labels for the form
        Label header = new Label("Modify Part");
        Label partId = new Label("ID");
        Label partName = new Label("Name");
        Label partCount = new Label("Inv");
        Label partPrice = new Label("Price/Cost");
        Label partMax = new Label("Max");
        Label partMin = new Label("Min");
        Label partMachineID = new Label("Machine ID");
        Label partCompanyName = new Label("Company Name");

        //sets the ans field for the id to be disabled and unable to be edited
        TextField partIdAns = new TextField();
        partIdAns.setPromptText("Auto Gen - Disabled");
        partIdAns.setEditable(false);

        //sets the answer text fields for the answers that the user inputs
        TextField partNameAns = new TextField(modifiedPart.getName());
        TextField partCountAns = new TextField(String.valueOf(modifiedPart.getStock()));
        TextField partPriceAns = new TextField(String.valueOf(modifiedPart.getPrice()));
        TextField partMaxAns = new TextField(String.valueOf(modifiedPart.getMax()));
        TextField partMinAns = new TextField(String.valueOf(modifiedPart.getMin()));
        TextField partCompanyNameAns = new TextField(valueOfInOrOut);
        TextField partMachineIDAns = new TextField(valueOfInOrOut);

        //checks to see what state the text fields should be in based on the parameter passed
        if (inHouseCheck) {
            partCompanyName.visibleProperty().set(false);
            partCompanyNameAns.visibleProperty().set(false);
            partMachineID.visibleProperty().set(true);
            partMachineIDAns.visibleProperty().set(true);
        }
        else {
            partCompanyName.visibleProperty().set(true);
            partCompanyNameAns.visibleProperty().set(true);
            partMachineID.visibleProperty().set(false);
            partMachineIDAns.visibleProperty().set(false);
        }

        //initializes the radio buttons
        RadioButton Outsourced = new RadioButton("Outsourced");
        RadioButton inHouse = new RadioButton("In House");

        //sets the selection of the buttons based on parameters
        if (inHouseCheck) { inHouse.setSelected(true); }
        else { Outsourced.setSelected(true); }

        //toggles the radio buttons together
        final ToggleGroup inOrOut = new ToggleGroup();
        inHouse.setToggleGroup(inOrOut);
        Outsourced.setToggleGroup(inOrOut);

        //checks to see if the user will decide to switch selections of the radio button
        inHouse.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    //sets the changes in visibility to the input to clear
                    partCompanyName.visibleProperty().set(false);
                    partCompanyNameAns.clear();
                    partCompanyNameAns.visibleProperty().set(false);
                    partMachineID.visibleProperty().set(true);
                    partMachineIDAns.visibleProperty().set(true);

                    //checks to see if there is already a set answer
                    if (InHouseList[totalData.getAllParts().indexOf(modifiedPart) + 1] != "") {
                        partMachineIDAns.setText(InHouseList[totalData.getAllParts().indexOf(modifiedPart) + 1]);
                    }
                }
        );

        //checks to see if the user will decide to switch selections of the radio button
        Outsourced.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    //sets the changes in visibility to the input to clear
                    partCompanyName.visibleProperty().set(true);
                    partCompanyNameAns.visibleProperty().set(true);
                    partMachineID.visibleProperty().set(false);
                    partMachineIDAns.clear();
                    partMachineIDAns.visibleProperty().set(false);

                    //checks to see if there is already a set answer
                    if (InHouseList[totalData.getAllParts().indexOf(modifiedPart) + 1] != "") {
                        partCompanyNameAns.setText(OutsourcedList[totalData.getAllParts().indexOf(modifiedPart) + 1]);
                    }
                }
        );

        //Creates the save button for the form
        Button save = new Button("Save");
        EventHandler<MouseEvent> savePartEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //initializing the temporary values to be stored
                String partName = "";
                int partStock = 0;
                double partPrice = 0.00;
                int partMax = 0;
                int partMin = 0;
                int machineId = 0;
                String companyName = "";

                //gets the id of the part being modified
                int id = parts_table.getSelectionModel().getSelectedItem().getId();

                //attempts to assigns the temporary value to what the answer was in that moment
                try { partName = partNameAns.getText(); }
                catch (NullPointerException e1) {
                    System.out.println("Error thrown, partNameAns" + partNameAns
                            + " : NULL POINTER EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partStock = Integer.parseInt(partCountAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCountAns " + partCountAns.getText() 
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partPrice = Double.parseDouble(partPriceAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partPriceAns " + partPriceAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partMax = Integer.parseInt(partMaxAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partMaxAns " + partMaxAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { partMin = Integer.parseInt(partMinAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCountAns " + partMinAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { machineId = Integer.parseInt(partMachineIDAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partMachineIDAns " + partMachineIDAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { companyName = partCompanyNameAns.getText(); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, partCompanyNameAns " + partCompanyNameAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                if ((partStock >= 0 && partPrice >= 0) && (partMin > 0)
                        && (partMin < partStock && partStock < partMax)) {

                    //validating input
                    if ((modifiedPart.getMin() > partMax) && (modifiedPart.getMin() >= partMin)) {
                        partMax = partMin + 1;
                    }
                    if ((modifiedPart.getMax() < partMin) && (modifiedPart.getMax() <= partMax)) {
                        partMin = partMax - 1;
                    }

                    //checks to see whether to store the data as inHouse or Outsourced
                    if (inHouse.isSelected()) {
                        //assigns user input values to the new temp class and adds the class to the data bank
                        InHouse temp = new InHouse(id, partName, partPrice, partStock, partMin, partMax, machineId);
                        totalData.updatePart(parts_table.getSelectionModel().getSelectedItem().getId(), temp);
                        InHouseList[id] = String.valueOf(machineId);
                        OutsourcedList[id] = null;
                    } else {
                        //assigns user input values to the new temp class and adds the class to the data bank
                        Outsourced temp = new Outsourced(id, partName, partPrice, partStock, partMin, partMax, companyName);
                        totalData.updatePart(parts_table.getSelectionModel().getSelectedIndex(), temp);

                        OutsourcedList[id] = companyName;
                        InHouseList[id] = null;
                    }

                    //saves and closes the form
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                }
                else {
                    //displays popUp information to the user
                    Stage popUp = new Stage();
                    Label Text = new Label("Cannot create product with imporoper input");

                    //Creates ok button
                    Button ok = new Button("Ok");
                    //Creates event handler
                    EventHandler<MouseEvent> popUpEvent = e1 -> {
                        //saves the stage and closes
                        popUp.close();
                    };
                    //registers evenet handler to the button
                    ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                    //sets all children to the sence to be displayed correctly
                    VBox imporperSave = new VBox();
                    imporperSave.getChildren().addAll(Text, ok);
                    imporperSave.setAlignment(Pos.CENTER);
                    imporperSave.setPadding(new Insets(10, 10, 10, 10));
                    imporperSave.setSpacing(20);
                    Scene popUpScene = new Scene(imporperSave);
                    popUp.setScene(popUpScene);
                    popUp.show();
                }
            }
        };
        //Registering the event filter
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, savePartEventHandler);

        //Creating the cancel button
        Button cancel = new Button("Cancel");
        EventHandler<MouseEvent> cancelPartEventHandler = e -> {
            //saves and closes the form
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
        };
        //Registering the event filter
        cancel.addEventFilter(MouseEvent.MOUSE_CLICKED, savePartEventHandler);

        //initializing the grid pane to layout values
        GridPane mainSection = new GridPane();

        //adds the labels to the left side of the form
        mainSection.add(partId, 0, 0, 1, 1);
        mainSection.add(partName, 0, 1, 1, 1);
        mainSection.add(partCount, 0, 2, 1, 1);
        mainSection.add(partPrice, 0, 3, 1, 1);
        mainSection.add(partMax, 0, 4, 1, 1);
        mainSection.add(partMin, 2, 4, 1, 1);
        mainSection.add(partMachineID, 0, 5, 1, 1);
        mainSection.add(partCompanyName, 0, 5, 1, 1);

        //adds the text to the left of the label
        mainSection.add(partIdAns, 1, 0, 1, 1);
        mainSection.add(partNameAns, 1, 1, 1, 1);
        mainSection.add(partCountAns, 1, 2, 1, 1);
        mainSection.add(partPriceAns, 1, 3, 1, 1);
        mainSection.add(partMaxAns, 1, 4, 1, 1);
        mainSection.add(partMinAns, 3, 4, 1, 1);
        mainSection.add(partMachineIDAns, 1, 5, 1, 1);
        mainSection.add(partCompanyNameAns, 1, 5, 1, 1);

        //sets the padding and the gaps with the values
        mainSection.setHgap(10);
        mainSection.setVgap(10);
        mainSection.setPadding(new Insets(10, 10, 10, 10));

        //combines the radio buttons and their spacing
        HBox radioOption = new HBox();
        radioOption.getChildren().addAll(inHouse, Outsourced);
        radioOption.setPadding(new Insets(10, 50, 10, 50));
        radioOption.setSpacing(50);

        //combines the buttons present and their spacing
        HBox buttonOption = new HBox();
        buttonOption.getChildren().addAll(save, cancel);
        buttonOption.setSpacing(25);
        radioOption.setPadding(new Insets(0, 10, 0, 10));

        //creates the final pane to combine all the values present
        GridPane finalPane = new GridPane();
        finalPane.add(header, 0, 0, 1, 1);
        finalPane.add(radioOption, 1, 0, 1, 1);
        finalPane.add(mainSection, 0, 1, 3, 1);
        finalPane.add(buttonOption, 2, 2, 1, 1);
        finalPane.setPadding(new Insets(50, 50, 50, 50));
        finalPane.setVgap(10);
        finalPane.setHgap(50);

        //applies the pane to the scene and returns the scene
        Scene scene = new Scene(finalPane);
        return scene;
    }

    /**
     * This is the form used by the user to add a product to the system and to be able to add and remove associated
     * parts from the product, being able to choose between all made and added parts objects and will save all
     * decisions made by the user
     *
     * @return scene the add product form
     */
    private Scene addProductForm() {
        //initializes list tempStorage for search bar holding values
        ObservableList<Part> tempStorage = FXCollections.observableArrayList();

        //creates a new temp product to have the new values inserted into and to add associated parts
        Product temp = new Product(0, " ", 0, 0, -1, 0);

        //creates the two tables to be featured in the table
        TableView<Part> totalParts = new TableView();
        TableView<Part> productParts = new TableView();

        //creates the part id column for the total parts chart
        TableColumn totalPartIdCol = new TableColumn("Part ID");
        totalPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));

        //creates the part name column for the total parts chart
        TableColumn totalPartNameCol = new TableColumn("Part Name");
        totalPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        //creates the part stock column for the total parts chart
        TableColumn totalPartStockCol = new TableColumn("Inventory Level");
        totalPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        //creates the part price column for the total parts chart
        TableColumn totalPartPriceCol = new TableColumn("Price/Cost Per Unit");
        totalPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        //creates the part id column for the associated parts of the given product
        TableColumn productPartIdCol = new TableColumn("Part ID");
        productPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));

        //creates the part name column for the associated parts of the given product
        TableColumn productPartNameCol = new TableColumn("Part Name");
        productPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        //creates the part stock column for the associated parts of the given product
        TableColumn productPartStockCol = new TableColumn("Inventory Level");
        productPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        //creates the part price column for the associated parts of the given product
        TableColumn productPartPriceCol = new TableColumn("Price/Cost Per Unit");
        productPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        //adds all of the total columns to the total table with proper spacing
        totalParts.getColumns().addAll(totalPartIdCol, totalPartNameCol, totalPartStockCol, totalPartPriceCol);
        totalParts.setPrefSize(400, 150);
        totalParts.setItems(totalData.getAllParts());

        //adds all of thr product associated columns to the total table with proper spacing
        productParts.getColumns().addAll(productPartIdCol, productPartNameCol, productPartStockCol, productPartPriceCol);
        productParts.setPrefSize(400, 150);
        productParts.setItems(temp.getAllAssociatedParts());

        //initializes the labels for the user to see
        Label header = new Label("Add Product");
        Label productId = new Label("ID");
        Label productName = new Label("Name");
        Label productCount = new Label("Inv");
        Label productPrice = new Label("Price/Cost");
        Label productMax = new Label("Max");
        Label productMin = new Label("Min");

        //disabled the product id text field and displayed message
        TextField productIdAns = new TextField();
        productIdAns.setPromptText("Auto Gen - Disabled");
        productIdAns.setEditable(false);

        //creates the text field for the users input
        TextField productNameAns = new TextField();
        TextField productCountAns = new TextField();
        TextField productPriceAns = new TextField();
        TextField productMaxAns = new TextField();
        TextField productMinAns = new TextField();

        //creation of the label for the search bar
        TextField partSearch = new TextField();
        partSearch.setPromptText("Search by Part ID or Name");
        //Creates the key event handler
        partSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //checks the search bar when the user presses the enter button
                if ((keyEvent.getCode() == KeyCode.ENTER && !partSearch.getText().isEmpty())) {

                    //clears all previously held data and reset the table to ensure no duplicates are made
                    totalParts.getItems().removeAll();
                    totalParts.refresh();
                    totalParts.setItems(totalData.getAllParts());
                    tempStorage.removeAll();
                    tempStorage.clear();

                    //looks up given information, checking format
                    try {
                        if (totalData.lookupPart(partSearch.getText()) != null) {
                            tempStorage.add(totalData.lookupPart(partSearch.getText()));
                        }
                    }
                    catch (NullPointerException e) {
                        System.out.println("Error thrown, partSearchBar " + partSearch
                                + " : NULL POINTER EXCEPTION " + e);
                    }

                    try { tempStorage.add(totalData.lookupPart(Integer.parseInt(partSearch.getText()))); }
                    catch (NumberFormatException e) {
                        System.out.println("Error caught, partSearchBar " + partSearch
                                + " : NUMBER FORMAT EXCEPTION " + e);
                    }

                    //removes all values from the table as the comparisons have finished
                    //and results need to be published
                    totalParts.getItems().removeAll();
                    totalParts.setItems(tempStorage);
                    totalParts.refresh();
                    tempStorage.removeAll();

                }
                //if the user has not pressed enter, or if the search bar is empty, makes the table refresh
                else {
                    //refreshing the stored values of the original product to be shown until another
                    // search enter is made
                    totalParts.getItems().removeAll();
                    totalParts.setItems(totalData.getAllParts());
                    totalParts.refresh();
                    tempStorage.removeAll();
                }
            }
        });

        //Creating the add button to be able to add an associated part
        Button add = new Button("Add");
        //Creating the event handler for the button
        EventHandler<MouseEvent> addPartEventHandler = e -> {
            //gets the part that has been selected and adds it to the temp as an associated part
            Part partToAdd = totalParts.getSelectionModel().getSelectedItem();
            temp.addAssociatedPart(partToAdd);
        };
        //Registering the event filter
        add.addEventFilter(MouseEvent.MOUSE_CLICKED, addPartEventHandler);

        //Creating the save button
        Button save = new Button("Save");
        //Creating the event handler for the button
        EventHandler<MouseEvent> saveProductEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //provides temp values to hold the user's input
                String productName = "";
                int productStock = 0;
                double productPrice = 0.00;
                int productMax = 0;
                int productMin = 0;
                int id = totalData.getAllProducts().size();

                //attempts to assign product name to the temp value
                try { productName = productNameAns.getText(); }
                catch (NullPointerException e1) {
                    System.out.println("Error thrown, productNameAns" + productNameAns
                            + " : NULL POINTER EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try {
                    productStock = Integer.parseInt(productCountAns.getText());
                } catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productCountAns " + productCountAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try {
                    productPrice = Double.parseDouble(productPriceAns.getText());
                } catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productPriceAns " + productPriceAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try {
                    productMax = Integer.parseInt(productMaxAns.getText());
                } catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productMaxAns " + productMaxAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try {
                    productMin = Integer.parseInt(productMinAns.getText());
                } catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productCountAns " + productMinAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                if ((productStock >= 0 && productPrice >= 0) && (productMin > 0)
                        && (productMin < productStock && productStock < productMax)) {

                    //creates a temporary list of the associated parts
                    ObservableList<Part> tempPartAssociated = FXCollections.observableArrayList();
                    tempPartAssociated.addAll(temp.getAllAssociatedParts());

                    //sets the given values to the class
                    temp.setId(id);
                    temp.setName(productName);
                    temp.setStock(productStock);
                    temp.setPrice(productPrice);
                    temp.setMax(productMax);
                    temp.setMin(productMin);

                    //goes through all of the associated parts for the product
                    for (int i = 1; i < temp.getAllAssociatedParts().size() - 1; i++) {
                        temp.addAssociatedPart(tempPartAssociated.get(i - 1));
                    }

                    //adds the data to the data bank
                    totalData.addProduct(temp);

                    //saves the data
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                }
                else {
                    //displays popUp information to the user
                    Stage popUp = new Stage();
                    Label Text = new Label("Cannot create product with imporoper input");
                    //Creates ok button
                    Button ok = new Button("Ok");
                    //Creates event handler
                    EventHandler<MouseEvent> popUpEvent = e1 -> {
                        //saves the stage and closes
                        popUp.close();
                    };
                    //registers evenet handler to the button
                    ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                    //sets all children to the sence to be displayed correctly
                    VBox imporperSave = new VBox();
                    imporperSave.getChildren().addAll(Text, ok);
                    imporperSave.setAlignment(Pos.CENTER);
                    imporperSave.setPadding(new Insets(10,10,10,10));
                    imporperSave.setSpacing(20);
                    Scene popUpScene = new Scene(imporperSave);
                    popUp.setScene(popUpScene);
                    popUp.show();
                }
            }
        };
        //Registering the event filter
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, saveProductEventHandler);

        //Creating the cancel button
        Button cancel = new Button("Cancel");
        //Creating the event handler for the button
        EventHandler<MouseEvent> cancelProductEventHandler = e -> {
            //saves the stage and closes
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
        };
        //Registering the event filter
        cancel.addEventFilter(MouseEvent.MOUSE_CLICKED, cancelProductEventHandler);

        //Creating the remove associated part button
        Button removeProductPart = new Button("Remove Associated Part");
        //Creating the event handler for the button
        EventHandler<MouseEvent> removePartEventHandler = e -> {
            //gets the selected element that the user would like to remove
            Part partToRemove = productParts.getSelectionModel().getSelectedItem();

            //checks if the list of items is empty or if the user did not select an item
            if (!productParts.getItems().isEmpty() || partToRemove == null) {
                //loops through all the items
                for (int i = 0; i < productParts.getItems().size(); i++) {
                    try {
                        assert partToRemove != null;
                        //looks for the proper matching id and removes the object
                        if (productParts.getItems().get(i).getId() == partToRemove.getId()) {
                            int indexToRemove = i;
                            //displays prompt to double check user's intention
                            Stage userConfirm = new Stage();

                            //label with discription for the user
                            Label removeCheck = new Label("Arer you sure you would like to remove this part?");

                            //Creates yes button
                            Button yesButton = new Button("Yes");
                            //Creates event handler
                            EventHandler<MouseEvent> yesResponse = e1 -> {
                                //removes the desired object from the system
                                productParts.getItems().remove(indexToRemove);

                                //displays popUp information to the user
                                Stage popUp = new Stage();
                                Label deleteAcceptText = new Label("Object has been removed from the product.");
                                //Creates ok button
                                Button ok = new Button("Ok");
                                //Creates event handler
                                EventHandler<MouseEvent> popUpEvent = e2 -> {
                                    //saves the stage and closes
                                    popUp.close();
                                };
                                //registers evenet handler to the button
                                ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                                //sets all children to the sence to be displayed correctly
                                VBox deleteConfirm = new VBox();
                                deleteConfirm.getChildren().addAll(deleteAcceptText, ok);
                                deleteConfirm.setAlignment(Pos.CENTER);
                                deleteConfirm.setPadding(new Insets(10, 10, 10, 10));
                                deleteConfirm.setSpacing(20);
                                Scene popUpScene = new Scene(deleteConfirm);
                                popUp.setScene(popUpScene);
                                popUp.show();

                                //closes the window after user input
                                userConfirm.close();

                                //end of function
                                return;
                            };
                            //registers event handlers to the button
                            yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, yesResponse);

                            //Creates no button
                            Button noButton = new Button("No");
                            //Creates event handler
                            EventHandler<MouseEvent> noResponse = e1 -> {
                                //saves the stage and closes
                                userConfirm.close();
                            };
                            //registers evenet handler to the button
                            noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, noResponse);

                            //sets buttons to be displayed next to each other
                            HBox yesNo = new HBox();
                            yesNo.getChildren().addAll(yesButton, noButton);
                            yesNo.setPadding(new Insets(10,10,10,10));
                            yesNo.setSpacing(10);

                            //adds the buttons to the bottom of the label
                            VBox box = new VBox();
                            box.getChildren().addAll(removeCheck, yesNo);
                            box.setAlignment(Pos.CENTER);
                            box.setPadding(new Insets(10,10,10,10));
                            box.setSpacing(20);

                            //adds all parts into a final scene, displayed for the user
                            Scene popUpScene = new Scene(box);
                            userConfirm.setScene(popUpScene);
                            userConfirm.show();
                        }
                    }
                    //makes sure that the null exception does not cause a runtime error
                    catch (NullPointerException e1) {
                        System.out.println("Error thrown, partToRemove " + partToRemove 
                                + " : NULL POINTER EXCEPTION " + e1);
                    }
                }
            }
        };
        //Registering the event filter
        removeProductPart.addEventFilter(MouseEvent.MOUSE_CLICKED, removePartEventHandler);

        //initializing the main user section
        GridPane mainSection = new GridPane();

        //Adds the labels to the section on the right side
        mainSection.add(productId, 0, 0, 2, 1);
        mainSection.add(productName, 0, 1, 2, 1);
        mainSection.add(productCount, 0, 2, 1, 1);
        mainSection.add(productPrice, 0, 3, 1, 1);
        mainSection.add(productMax, 0, 4, 1, 1);
        mainSection.add(productMin, 2, 4, 1, 1);

        //adds the text fields to the section on the left side
        mainSection.add(productIdAns, 1, 0, 1, 1);
        mainSection.add(productNameAns, 1, 1, 1, 1);
        mainSection.add(productCountAns, 1, 2, 1, 1);
        mainSection.add(productPriceAns, 1, 3, 1, 1);
        mainSection.add(productMaxAns, 1, 4, 1, 1);
        mainSection.add(productMinAns, 3, 4, 1, 1);

        //sets the proper spacing
        mainSection.setHgap(10);
        mainSection.setVgap(10);
        mainSection.setPadding(new Insets(10, 10, 10, 10));

        //groups the save and cancel button to be under the remove associated part button
        HBox saveCancelGroup = new HBox();
        saveCancelGroup.getChildren().addAll(save, cancel);
        saveCancelGroup.setSpacing(50);

        //groups the three buttons together with the remove associated parts on top
        VBox bottomButtonCluster = new VBox();
        bottomButtonCluster.getChildren().addAll(removeProductPart, saveCancelGroup);
        bottomButtonCluster.setSpacing(10);

        //creates the right side of the frame adding the elements needed to be present
        VBox rightSide = new VBox();
        rightSide.getChildren().addAll(partSearch, totalParts, add, productParts, bottomButtonCluster);
        rightSide.setSpacing(10);

        //creates the left side with the input section for the user
        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(header, mainSection);
        leftSide.setSpacing(100);

        //adds both the left and right side of the frame together
        HBox total = new HBox();
        total.getChildren().addAll(leftSide, rightSide);
        //sets the style, space, and padding
        total.setSpacing(75);
        total.setStyle("-fx-border-color: black;" +
                "-fx-padding:20;" +
                "-fx-border-radius: 5;");
        total.setPadding(new Insets(50, 50, 50, 50));

        //returns the finished scene
        Scene scene = new Scene(total);
        return scene;
    }

    /**
     * This is the form used by the user to modify the product that the user had selected before clicking the button
     * in the main form. This form makes universal changes to the selected product, able to change any of the values
     * or associated parts for the product, with the single exception being the products ID
     *
     * @param modifiedProduct is the selected product the user selected before pressing the button
     * @return scene the modify product form
     */
    private Scene modifyProductForm(Product modifiedProduct) {
        //initializes list tempStorage for search bar holding values
        ObservableList<Part> tempStorage = FXCollections.observableArrayList();

        //creates the two tables to be featured in the table
        TableView<Part> totalParts = new TableView();
        TableView<Part> productParts = new TableView();

        //creates the part id column for the total parts chart
        TableColumn totalPartIdCol = new TableColumn("Part ID");
        totalPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));

        //creates the part name column for the total parts chart
        TableColumn totalPartNameCol = new TableColumn("Part Name");
        totalPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        //creates the part stock column for the total parts chart
        TableColumn totalPartStockCol = new TableColumn("Inventory Level");
        totalPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        //creates the part price column for the total parts chart
        TableColumn totalPartPriceCol = new TableColumn("Price/Cost Per Unit");
        totalPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        //creates the part id column for the associated parts of the given product
        TableColumn productPartIdCol = new TableColumn("Part ID");
        productPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));

        //creates the part name column for the associated parts of the given product
        TableColumn productPartNameCol = new TableColumn("Part Name");
        productPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));

        //creates the part stock column for the associated parts of the given product
        TableColumn productPartStockCol = new TableColumn("Inventory Level");
        productPartStockCol.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        //creates the part price column for the associated parts of the given product
        TableColumn productPartPriceCol = new TableColumn("Price/Cost Per Unit");
        productPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        //adds all of the total columns to the total table with proper spacing
        totalParts.getColumns().addAll(totalPartIdCol, totalPartNameCol, totalPartStockCol, totalPartPriceCol);
        totalParts.setPrefSize(400, 150);
        totalParts.setItems(totalData.getAllParts());

        //adds all of thr product associated columns to the total table with proper spacing
        productParts.getColumns().addAll(productPartIdCol, productPartNameCol, productPartStockCol, productPartPriceCol);
        productParts.setPrefSize(400, 150);
        productParts.setItems(modifiedProduct.getAllAssociatedParts());

        //initializes the labels for the user to see
        Label header = new Label("Modify Product");
        Label productId = new Label("ID");
        Label productName = new Label("Name");
        Label productCount = new Label("Inv");
        Label productPrice = new Label("Price/Cost");
        Label productMax = new Label("Max");
        Label productMin = new Label("Min");

        //disabled the product id text field and displayed message
        TextField productIdAns = new TextField();
        productIdAns.setPromptText("Auto Gen - Disabled");
        productIdAns.setEditable(false);

        //creates the text field for the users input
        TextField productNameAns = new TextField(modifiedProduct.getName());
        TextField productCountAns = new TextField(String.valueOf(modifiedProduct.getStock()));
        TextField productPriceAns = new TextField(String.valueOf(modifiedProduct.getPrice()));
        TextField productMaxAns = new TextField(String.valueOf(modifiedProduct.getMax()));
        TextField productMinAns = new TextField(String.valueOf(modifiedProduct.getMin()));

        //creation of the label for the search bar
        TextField partSearch = new TextField();
        partSearch.setPromptText("Search by Part ID or Name");
        //Creates the key event handler
        partSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //checks the search bar when the user presses the enter button
                if ((keyEvent.getCode() == KeyCode.ENTER && !partSearch.getText().isEmpty())) {

                    //clears all previously held data and reset the table to ensure no duplicates are made
                    totalParts.getItems().removeAll();
                    totalParts.refresh();
                    totalParts.setItems(totalData.getAllParts());
                    tempStorage.removeAll();
                    tempStorage.clear();

                    //looks up given information, checking format
                    try {
                        if (totalData.lookupPart(partSearch.getText()) != null) {
                            tempStorage.add(totalData.lookupPart(partSearch.getText()));
                        }
                    }
                    catch (NullPointerException e) {
                        System.out.println("Error thrown, partSearchBar " + partSearch
                                + " : NULL POINTER EXCEPTION " + e);
                    }

                    try { tempStorage.add(totalData.lookupPart(Integer.parseInt(partSearch.getText()))); }
                    catch (NumberFormatException e) {
                        System.out.println("Error caught, partSearchBar " + partSearch
                                + " : NUMBER FORMAT EXCEPTION " + e);
                    }

                    //removes all values from the table as the comparisons have finished
                    //and results need to be published
                    if(!tempStorage.isEmpty()) {
                        totalParts.getItems().removeAll();
                        totalParts.setItems(tempStorage);
                        totalParts.refresh();
                        tempStorage.removeAll();
                    }
                    else {
                        //displays popUp information to the user
                        Stage popUp = new Stage();
                        Label searchFail = new Label("Search has yeilded no results. Please try again.");
                        //Creates ok button
                        Button ok = new Button("Ok");
                        //Creates event handler
                        EventHandler<MouseEvent> popUpEvent = e1 -> {
                            //saves the stage and closes
                            popUp.close();
                        };
                        //registers evenet handler to the button
                        ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                        //sets all children to the sence to be displayed correctly
                        VBox box = new VBox();
                        box.getChildren().addAll(searchFail, ok);
                        box.setAlignment(Pos.CENTER);
                        box.setPadding(new Insets(10,10,10,10));
                        box.setSpacing(20);
                        Scene popUpScene = new Scene(box);
                        popUp.setScene(popUpScene); 
                        popUp.show();
                    }

                }
                //if the user has not pressed enter, or if the search bar is empty, makes the table refresh
                else {
                    //refreshing the stored values of the original parts to be shown until another search enter is made
                    totalParts.getItems().removeAll();
                    totalParts.setItems(totalData.getAllParts());
                    totalParts.refresh();
                    tempStorage.removeAll();
                }
            }
        });

        //Creating the add button
        Button add = new Button("Add");
        //Creating the event handler for the button
        EventHandler<MouseEvent> addPartEventHandler = e -> {
            Part partToAdd = totalParts.getSelectionModel().getSelectedItem();
            modifiedProduct.addAssociatedPart(partToAdd);
        };
        //Registering the event filter
        add.addEventFilter(MouseEvent.MOUSE_CLICKED, addPartEventHandler);

        Button save = new Button("Save");
        EventHandler<MouseEvent> saveProductEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //initializes the temp values to hold user input
                String productName = "";
                int productStock = 0;
                double productPrice = 0.00;
                int productMax = 0;
                int productMin = 0;

                //retrieves the id from the selected product
                int id = product_table.getSelectionModel().getSelectedItem().getId();

                //attempts to assign product name to the temp value
                try { productName = productNameAns.getText(); }
                catch (NullPointerException e1) {
                    System.out.println("Error thrown, productNameAns" + productNameAns
                            + " : NULL POINTER EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { productStock = Integer.parseInt(productCountAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productCountAns " + productCountAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { productPrice = Double.parseDouble(productPriceAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productPriceAns " + productPriceAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { productMax = Integer.parseInt(productMaxAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productMaxAns " + productMaxAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                //attempts to retrieve value input from user
                try { productMin = Integer.parseInt(productMinAns.getText()); }
                catch (NumberFormatException e1) {
                    System.out.println("Error thrown, productCountAns " + productMinAns.getText()
                            + " : NUMBER FORMAT EXCEPTION " + e1);
                }

                if ((productStock >= 0 && productPrice >= 0) && (productMin > 0)
                        && (productMin < productStock && productStock < productMax)) {

                    //creates a list of parts and adds all of the associated parts to it so that the
                    //list can be worked through with a loop
                    ObservableList<Part> tempPartAssociated = FXCollections.observableArrayList();
                    tempPartAssociated.addAll(modifiedProduct.getAllAssociatedParts());

                    //creates a temp product from the user's input
                    Product temp = new Product(id, productName, productPrice, productStock, productMin, productMax);
                    //loops through adding all associated parts
                    for (int i = 0; i < modifiedProduct.getAllAssociatedParts().size(); i++) {
                        temp.addAssociatedPart(tempPartAssociated.get(i));
                    }
                    //removes the original product, to be replace by the new temp product
                    totalData.updateProduct(product_table.getSelectionModel().getSelectedIndex(), temp);

                    //saves and closes the window
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                }
                else {
                    //displays popUp information to the user
                    Stage popUp = new Stage();
                    Label Text = new Label("Cannot create product with imporoper input");

                    //Creates ok button
                    Button ok = new Button("Ok");
                    //Creates event handler
                    EventHandler<MouseEvent> popUpEvent = e1 -> {
                        //saves the stage and closes
                        popUp.close();
                    };
                    //registers evenet handler to the button
                    ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                    //sets all children to the sence to be displayed correctly
                    VBox imporperSave = new VBox();
                    imporperSave.getChildren().addAll(Text, ok);
                    imporperSave.setAlignment(Pos.CENTER);
                    imporperSave.setPadding(new Insets(10, 10, 10, 10));
                    imporperSave.setSpacing(20);
                    Scene popUpScene = new Scene(imporperSave);
                    popUp.setScene(popUpScene);
                    popUp.show();
                }
            }
        };
        //Registering the event filter
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, saveProductEventHandler);

        //Creating the cancel button
        Button cancel = new Button("Cancel");
        //Creating the event handler for the button
        EventHandler<MouseEvent> cancelProductEventHandler = e -> {
            //saves the stage and closes
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
        };
        //Registering the event filter
        cancel.addEventFilter(MouseEvent.MOUSE_CLICKED, cancelProductEventHandler);

        //Creating the remove associated part button
        Button removeProductPart = new Button("Remove Associated Part");
        //Creating the event handler for the button
        EventHandler<MouseEvent> removePartEventHandler = e -> {
            //gets the selected element that the user would like to remove
            Part partToRemove = productParts.getSelectionModel().getSelectedItem();

            //checks if the list of items is empty or if the user did not select an item
            if (!productParts.getItems().isEmpty() || partToRemove == null) {
                //loops through all the items
                for (int i = 0; i < productParts.getItems().size(); i++) {
                    try {
                        assert partToRemove != null;
                        //looks for the proper matching id and removes the object
                        if (productParts.getItems().get(i).getId() == partToRemove.getId()) {
                            int indexToRemove = i;
                            //displays prompt to double check user's intention
                            Stage userConfirm = new Stage();

                            //label with discription for the user
                            Label removeCheck = new Label("Arer you sure you would like to remove this part?");

                            //Creates yes button
                            Button yesButton = new Button("Yes");
                            //Creates event handler
                            EventHandler<MouseEvent> yesResponse = e1 -> {
                                //removes the desired object from the system
                                productParts.getItems().remove(indexToRemove);

                                //displays popUp information to the user
                                Stage popUp = new Stage();
                                Label deleteAcceptText = new Label("Object has been removed from the product.");
                                //Creates ok button
                                Button ok = new Button("Ok");
                                //Creates event handler
                                EventHandler<MouseEvent> popUpEvent = e2 -> {
                                    //saves the stage and closes
                                    popUp.close();
                                };
                                //registers evenet handler to the button
                                ok.addEventFilter(MouseEvent.MOUSE_CLICKED, popUpEvent);

                                //sets all children to the sence to be displayed correctly
                                VBox deleteConfirm = new VBox();
                                deleteConfirm.getChildren().addAll(deleteAcceptText, ok);
                                deleteConfirm.setAlignment(Pos.CENTER);
                                deleteConfirm.setPadding(new Insets(10, 10, 10, 10));
                                deleteConfirm.setSpacing(20);
                                Scene popUpScene = new Scene(deleteConfirm);
                                popUp.setScene(popUpScene);
                                popUp.show();

                                //closes the window after user input
                                userConfirm.close();

                                //end of function
                                return;
                            };
                            //registers event handlers to the button
                            yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, yesResponse);

                            //Creates no button
                            Button noButton = new Button("No");
                            //Creates event handler
                            EventHandler<MouseEvent> noResponse = e1 -> {
                                //saves the stage and closes
                                userConfirm.close();
                            };
                            //registers evenet handler to the button
                            noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, noResponse);

                            //sets buttons to be displayed next to each other
                            HBox yesNo = new HBox();
                            yesNo.getChildren().addAll(yesButton, noButton);
                            yesNo.setPadding(new Insets(10,10,10,10));
                            yesNo.setSpacing(10);

                            //adds the buttons to the bottom of the label
                            VBox box = new VBox();
                            box.getChildren().addAll(removeCheck, yesNo);
                            box.setAlignment(Pos.CENTER);
                            box.setPadding(new Insets(10,10,10,10));
                            box.setSpacing(20);

                            //adds all parts into a final scene, displayed for the user
                            Scene popUpScene = new Scene(box);
                            userConfirm.setScene(popUpScene);
                            userConfirm.show();
                        }
                    }
                    //makes sure that the null exception does not cause a runtime error
                    catch (NullPointerException e1) {
                        System.out.println("Error thrown, partToRemove " + partToRemove
                                + " : NULL POINTER EXCEPTION " + e1);
                    }
                }
            }
        };
        //Registering the event filter
        removeProductPart.addEventFilter(MouseEvent.MOUSE_CLICKED, removePartEventHandler);

        //initializing the main user section
        GridPane mainSection = new GridPane();

        //Adds the labels to the section on the right side
        mainSection.add(productId, 0, 0, 2, 1);
        mainSection.add(productName, 0, 1, 2, 1);
        mainSection.add(productCount, 0, 2, 1, 1);
        mainSection.add(productPrice, 0, 3, 1, 1);
        mainSection.add(productMax, 0, 4, 1, 1);
        mainSection.add(productMin, 2, 4, 1, 1);

        //adds the text fields to the section on the left side
        mainSection.add(productIdAns, 1, 0, 1, 1);
        mainSection.add(productNameAns, 1, 1, 1, 1);
        mainSection.add(productCountAns, 1, 2, 1, 1);
        mainSection.add(productPriceAns, 1, 3, 1, 1);
        mainSection.add(productMaxAns, 1, 4, 1, 1);
        mainSection.add(productMinAns, 3, 4, 1, 1);

        //sets the proper spacing
        mainSection.setHgap(10);
        mainSection.setVgap(10);
        mainSection.setPadding(new Insets(10, 10, 10, 10));

        //groups the save and cancel button to be under the remove associated part button
        HBox saveCancelGroup = new HBox();
        saveCancelGroup.getChildren().addAll(save, cancel);
        saveCancelGroup.setSpacing(50);

        //groups the three buttons together with the remove associated parts on top
        VBox bottomButtonCluster = new VBox();
        bottomButtonCluster.getChildren().addAll(removeProductPart, saveCancelGroup);
        bottomButtonCluster.setSpacing(10);

        //creates the right side of the frame adding the elements needed to be present
        VBox rightSide = new VBox();
        rightSide.getChildren().addAll(partSearch, totalParts, add, productParts, bottomButtonCluster);
        rightSide.setSpacing(10);

        //creates the left side with the input section for the user
        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(header, mainSection);
        leftSide.setSpacing(100);

        //adds both the left and right side of the frame together
        HBox total = new HBox();
        total.getChildren().addAll(leftSide, rightSide);
        //sets the style, space, and padding
        total.setSpacing(75);
        total.setStyle("-fx-border-color: black;" +
                "-fx-padding:20;" +
                "-fx-border-radius: 5;");
        total.setPadding(new Insets(50, 50, 50, 50));

        //returns the finished scene
        return new Scene(total);
    }
}