package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

/** This class is responsible for the functionality of the "Main" form. */
public class MainForm implements Initializable {
    @FXML
    private TextField partIdNameTxt;
    @FXML
    private TableView<Part> partTblView;
    @FXML
    private TableColumn<Part, Integer> partPartIdCol;
    @FXML
    private TableColumn<Part, String> partPartNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvLevCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TextField prodIdNameTxt;
    @FXML
    private TableView<Product> prodTblView;
    @FXML
    private TableColumn<Product, Integer> prodProdIdCol;
    @FXML
    private TableColumn<Product, String> prodProdNameCol;
    @FXML
    private TableColumn<Product, Integer> prodInvLevCol;
    @FXML
    private TableColumn<Product, Double> prodPriceCol;

    // In order to promote code reuse, this MainForm class will be home to many static variables and methods.
    public static Stage stage;
    public static Parent scene;

    public static int addPartId;
    public static int addProdId;

    /** This method activates when the scene starts.
     *  @param url for initialization
     *  @param resourceBundle for initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MainForm is initialized!");
        partTblView.setItems(Inventory.getAllParts());
        partPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTblView.setItems(Inventory.getAllProducts());
        prodProdIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodProdNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method will use user input to search for a part.
     *  If the part is found, it will appear in the left TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionPartsSearch(ActionEvent actionEvent) {
        String searchPartStr = partIdNameTxt.getText();
        if (!searchPartStr.isBlank()) {
            try {
                int searchPartId = Integer.parseInt(searchPartStr);
                Part partResult = lookupPart(searchPartId);
                if (partResult != null) {
                    partTblView.getSelectionModel().select(partResult);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> partResultList = lookupPart(searchPartStr);
                if (partResultList.size() == 0) {
                    alertBox("Error Dialog", "The part is not found.  Please try again with different input.");
                    return;
                } else {
                    partTblView.setItems(partResultList);
                }
            }
        } else {
            partTblView.setItems(Inventory.getAllParts());
        }
    }

    /** This method will use user input to search for a product.
     *  If the product is found, it will appear in the right TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionProductSearch(ActionEvent actionEvent) {
        String searchProductStr = prodIdNameTxt.getText();
        if (!searchProductStr.isBlank()) {
            try {
                int searchProdId = Integer.parseInt(searchProductStr);
                Product prodResult = lookupProduct(searchProdId);
                if (prodResult != null) {
                    prodTblView.getSelectionModel().select(prodResult);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                ObservableList<Product> prodResultList = lookupProduct(searchProductStr);
                if (prodResultList.size() == 0) {
                    alertBox("Error Dialog", "The product is not found.  Please try again with different input.");
                    return;
                } else {
                    prodTblView.setItems(prodResultList);
                }
            }
        } else {
            prodTblView.setItems(Inventory.getAllProducts());
        }
    }

    /** This method will take you to the Add Part Form.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionPartsAdd(ActionEvent actionEvent) throws IOException {
        System.out.println("Parts add button clicked");
        buttonAction("/view/AddPartForm.fxml", "Add Parts Form", actionEvent);
    }

    /** This method will take you to the Modify Part Form.
     *  You must select the part you wish to modify in the TableView.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionPartsModify(ActionEvent actionEvent) throws IOException {
        System.out.println("Parts modify button clicked");
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
        loader.load();

        ModifyPartForm MPFController = loader.getController();

        if (partTblView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("Selected part was null.");
            alertBox("Error Dialog", "Please select a part to modify.");
            return;
        }
        MPFController.sendPart(partTblView.getSelectionModel().getSelectedItem());
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method will delete the selected part.
     *  You must select the part you wish to delete in the TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionPartsDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected part, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Parts delete button clicked");
            Part selectedPart = partTblView.getSelectionModel().getSelectedItem();

            if (selectedPart == null) {
                alertBox("Error Dialog", "Please select the part that you want to delete.");
                return;
            }
            deletePart(selectedPart);
            partTblView.setItems(Inventory.getAllParts());
        }
    }

    /** This method will take you to the Add Product Form.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionProdAdd(ActionEvent actionEvent) throws IOException {
        System.out.println("Prod add button clicked");
        buttonAction("/view/AddProductForm.fxml", "Add Product Form", actionEvent);
    }

    /** This method will take you to the Modify Product Form.
     *  You must select the product you wish to modify in the TableView.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionProdModify(ActionEvent actionEvent) throws IOException {
        System.out.println("Prod mod button clicked");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
        loader.load();

        ModifyProductForm MProdFController = loader.getController();
        if (prodTblView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("Selected product was null.");
            alertBox("Error Dialog", "Please select a product to modify.");
            return;
        }

        MProdFController.sendProduct(prodTblView.getSelectionModel().getSelectedItem());
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method will delete the selected product.
     *  You must select the product you wish to delete in the TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionProdDelete(ActionEvent actionEvent) {
        System.out.println("Product delete clicked");
        Product selectedProd = prodTblView.getSelectionModel().getSelectedItem();
        if (selectedProd == null) {
            alertBox("Error Dialog", "Please select the product that you want to delete.");
            return;
        }
        if (selectedProd.getAllAssociatedParts().size() > 0) {
            alertBox("Error Dialog", "You may not delete a product with any associated parts.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected product, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteProduct(selectedProd);
            prodTblView.setItems(Inventory.getAllProducts());
        }
    }

    /** This method activates when the Exit button is clicked.
     *  This will exit the program.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will exit the program, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Exit button clicked");
            System.exit(0);
        }
    }

    /** Static method that will allow user to change scenes with the click of a button.
     *  @param fileName name of the file to change the scene
     *  @param formName name of the form to place as the title
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    public static void buttonAction(String fileName, String formName, ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(MainForm.class.getResource(fileName));
        stage.setTitle(formName);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Static method that will allow user to check if the values in min, max, and stock are logical.
     *  @param min int for minimum value
     *  @param max int for maximum value
     *  @param stock int for stock/ inv value
     *  @return boolean to determine if data is logical
     */
    public static boolean isLogical (int min, int max, int stock) {
        if (min > max) {
            alertBox("Error Dialog", "Min should be less than or equal to the Max");
            return false;
        }
        if (stock > max || stock < min) {
            alertBox("Error Dialog", "Inv should be between Min and Max values.");
            return false;
        }
        return true;
    }

    /** Static method that will allow user bring up a dialog box for erroneous input or catch logical errors.
     *  @param title title of the alert
     *  @param text text of the alert
     */
    public static void alertBox(String title, String text) {
        Alert alertAssPart = new Alert(Alert.AlertType.ERROR);
        alertAssPart.setTitle(title);
        alertAssPart.setContentText(text);
        alertAssPart.showAndWait();
    }
}

