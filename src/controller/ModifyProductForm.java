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

import static model.Inventory.lookupPart;
import static model.Inventory.updateProduct;

/** This class is responsible for the functionality of the "Modify Product" form. */
public class ModifyProductForm implements Initializable {
    @FXML
    private TableColumn<Part, Integer> btmInvLevCol;
    @FXML
    private TableColumn<Part, Integer> btmPartIdCol;
    @FXML
    private TableColumn<Part, String> btmPartNameCol;
    @FXML
    private TableView<Part> btmPartTblView;
    @FXML
    private TableColumn<Part, Double> btmPriceCol;
    @FXML
    private TextField idTxt;
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
    private TableColumn<Part, Integer> topInvLevCol;
    @FXML
    private TableColumn<Part, Integer> topPartIdCol;
    @FXML
    private TableColumn<Part, String> topPartNameCol;
    @FXML
    private TableColumn<Part, Double> topPriceCol;

    Product productToModify;

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

    /** This method allows user to send data from MainForm controller to the ModifyProduct controller.
     *  @param product selected by TableView
     */
    public void sendProduct(Product product) {
        idTxt.setText(String.valueOf(product.getId()));
        nameTxt.setText(product.getName());
        invTxt.setText(String.valueOf(product.getStock()));
        priceTxt.setText(String.valueOf(product.getPrice()));
        maxTxt.setText(String.valueOf(product.getMax()));
        minTxt.setText(String.valueOf(product.getMin()));

        productToModify = product;
        btmPartTblView.setItems(productToModify.getAllAssociatedParts());
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

    /** This method will add the associated part to the existing product.
     *  The added part will also be added to the bottom TableView.
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
        productToModify.addAssociatedPart(selectedPart);
        btmPartTblView.setItems(productToModify.getAllAssociatedParts());
    }

    /** This method will remove the associated part from the existing product.
     *  The removed part will also be removed from the bottom TableView.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionRemovePart(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You will remove associated part, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Remove Associated Part button clicked");
            Part selectedPart = btmPartTblView.getSelectionModel().getSelectedItem();

            if (selectedPart == null) {
                System.out.println("Selected part was null.");
                MainForm.alertBox("Error Dialog", "Please select part to disassociate.");
                return;
            }
            productToModify.deleteAssociatedPart(selectedPart);
            btmPartTblView.setItems(productToModify.getAllAssociatedParts());
        }
    }

    /** This method activates when the Save button is clicked.
     *  The input in the text boxes will be validated and then the existing product will be updated.
     *  The product changes will appear in the Main Form's TableView
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(idTxt.getText());
            String name = AddPartForm.validateString(nameTxt, "Name");
            int stock = AddPartForm.validateInteger(invTxt, "Inv");
            double price = AddPartForm.validateDouble(priceTxt, "Price");
            int min = AddPartForm.validateInteger(minTxt, "Min");
            int max = AddPartForm.validateInteger(maxTxt, "Max");

            // Checks return values for each field to ensure they are valid
            if (name == null || stock == -1 || price == -1 || min == -1 || max == -1) {
                return;
            }
            if (!MainForm.isLogical(min, max, stock)) {
                return;
            }

            productToModify.setName(name);
            productToModify.setStock(stock);
            productToModify.setPrice(price);
            productToModify.setMin(min);
            productToModify.setMax(max);

            updateProduct(id, productToModify);
            MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
        } catch (NumberFormatException e) {
            MainForm.alertBox("Error Dialog", "Please enter a valid value for each text field.");
        }
    }

    /** This method activates when the Cancel button is clicked.
     *  This will clear all text fields and go back to the Main Form.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "None of your changes will be saved, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Cancel button clicked");
            MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
        }
    }
}
