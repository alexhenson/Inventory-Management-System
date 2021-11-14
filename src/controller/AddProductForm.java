package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainForm.addPartId;
import static controller.MainForm.addProdId;
import static model.Inventory.lookupPart;

/** This class is responsible for the functionality of the "Add Product" form. */
public class AddProductForm implements Initializable {
    @FXML
    private TextField invTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField searchTxt;
    @FXML
    private TableView<Part> topPartTblView;
    @FXML
    private TableColumn<Part, Integer> topPartIdCol;
    @FXML
    private TableColumn<Part, Integer> topPartNameCol;
    @FXML
    private TableColumn<Part, Integer> topInvLevCol;
    @FXML
    private TableColumn<Part, Integer> topPriceCol;
    @FXML
    private TableView<Part> btmPartTblView;
    @FXML
    private TableColumn<Part, Integer> btmPartIdCol;
    @FXML
    private TableColumn<Part, Integer> btmPartNameCol;
    @FXML
    private TableColumn<Part, Integer> btmInvLevCol;
    @FXML
    private TableColumn<Part, Integer> btmPriceCol;

    // These are temporary variables to assign the product to while associating parts to it.
    // After everything is done the product will be saved to the allProducts array.
    Product newProduct;
    boolean wasNewProductMade = false;
    boolean didIncrementId = false;

    // The variables for the text fields need to be declared here to be used in the getFields method to reuse code.
    String name;
    int stock;
    double price;
    int min;
    int max;

    /** This method activates when the scene starts.
     *  @param url for initialization
     *  @param resourceBundle for initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topPartTblView.setItems(Inventory.getAllParts());
        topPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        topPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        topInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        topPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        btmPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        btmPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        btmInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        btmPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method will use user input to search for a part.
     *  If a part is found, it will appear in the top TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionSearch(ActionEvent actionEvent) {
        String searchPartStr = searchTxt.getText();
        if (!searchPartStr.isBlank()) {
            try {
                int searchPartId = Integer.parseInt(searchPartStr);
                Part partResult = lookupPart(searchPartId);
                if (partResult != null) {
                    topPartTblView.getSelectionModel().select(partResult);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> partResultList = lookupPart(searchPartStr);
                if (partResultList.size() == 0) {
                    MainForm.alertBox("Error Dialog", "The part is not found.  Please try again with different input.");
                    return;
                } else {
                    topPartTblView.setItems(partResultList);
                }
            }
        } else {
            topPartTblView.setItems(Inventory.getAllParts());
        }
    }

    /** This method will add the associated part to the new product.
     *  The added part will also be added to the bottom TableView.
     *  <p>RUNTIME ERROR - Originally I designed the onActionAddPart and onActionRemovePart methods to
     *  add parts to a temporary ObservableList, which I would then copy into the associatedParts
     *  ObservableList after all actions had taken place.  I removed the temporary ObservableList
     *  because I realized that I was not using the Product class's addAssociatedPart and
     *  deleteAssociatedPart methods.  In order to use these methods you needed to instantiate a
     *  product first.  I had a runtime error because a user might try to add a part prior to filling
     *  in all the text fields with valid data.  This method ensures that a product is instantiated
     *  before adding a part.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionAddPart(ActionEvent actionEvent) {
        System.out.println("Add button clicked");
        Part selectedPart = topPartTblView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            System.out.println("Selected part was null.");
            MainForm.alertBox("Error Dialog", "Please select part to associate.");
            return;
        }

        if (!didIncrementId) {
            addProdId = Inventory.incrementProductId();
            didIncrementId = true;
        }
        getFields();
        // Checks return values for each field to ensure they are valid
        if (name == null || stock == -1 || price == -1 || min == -1 || max == -1) {
            return;
        }
        if (!MainForm.isLogical(min, max, stock)) {
            return;
        }

        newProduct = new Product(addProdId, name, price, stock, min, max);
        wasNewProductMade = true;

        newProduct.addAssociatedPart(selectedPart);
        btmPartTblView.setItems(newProduct.getAllAssociatedParts());
    }

    /** This method will remove the associated part from the new product.
     *  The removed part will also be removed from the bottom TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionRemovePart(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You will remove associated part, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("RemoveAssociatedPart button clicked");
            Part selectedPart = btmPartTblView.getSelectionModel().getSelectedItem();

            if (selectedPart == null) {
                System.out.println("Selected part was null.");
                MainForm.alertBox("Error Dialog", "Please select part to disassociate.");
                return;
            }
            newProduct.deleteAssociatedPart(selectedPart);
            btmPartTblView.setItems(newProduct.getAllAssociatedParts());
        }
    }

    /** This method activates when the Save button is clicked.
     *  The input in the text boxes will be validated and then saved to a new product.
     *  The product will be saved in the Main Form's TableView
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) throws IOException {
        if (wasNewProductMade) {
            getFields();
            // Checks return values for each field to ensure they are valid
            if (name == null || stock == -1 || price == -1 || min == -1 || max == -1) {
                return;
            }
            if (!MainForm.isLogical(min, max, stock)) {
                return;
            }

            newProduct.setId(addProdId);
            newProduct.setName(name);
            newProduct.setStock(stock);
            newProduct.setPrice(price);
            newProduct.setMax(max);
            newProduct.setMin(min);

        } else {
            if (!didIncrementId) {
                addProdId = Inventory.incrementProductId();
                didIncrementId = true;
            }
            getFields();
            // Checks return values for each field to ensure they are valid
            if (name == null || stock == -1 || price == -1 || min == -1 || max == -1) {
                return;
            }
            if (!MainForm.isLogical(min, max, stock)) {
                return;
            }
            newProduct = new Product(addProdId, name, price, stock, min, max);
        }
        Inventory.addProduct(newProduct);
        MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
    }

    /** This method activates when the Cancel button is clicked.
     *  This will clear all text fields and go back to the Main Form.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Cancel button clicked");
            MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
        }
    }

    /** This method will assign all the valid text box data into the variables.
     *  The if statement ensures that the addProdId value doesn't increase each time the
     *  action button is clicked and the fields don't save because there is an invalid value.
     */
    private void getFields() {
        name = AddPartForm.validateString(nameTxt, "Name");
        stock = AddPartForm.validateInteger(invTxt, "Inv");
        price = AddPartForm.validateDouble(priceTxt, "Price");
        min = AddPartForm.validateInteger(minTxt, "Min");
        max = AddPartForm.validateInteger(maxTxt, "Max");
    }
}
