package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class is responsible for the functionality of the "Modify Part" form. */
public class ModifyPartForm implements Initializable {
    @FXML
    private TextField idTxt;
    @FXML
    private RadioButton inHouseRBtn;
    @FXML
    private TextField invTxt;
    @FXML
    private Label machComLbl;
    @FXML
    private TextField machComTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private RadioButton outsourcedRBtn;
    @FXML
    private TextField priceTxt;

    /** This method activates when the scene starts.
     *  @param url for initialization
     *  @param resourceBundle for initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    /** This method allows user to send data from MainForm controller to the ModifyPart controller.
     *  @param part selected from TableView
     */
    public void sendPart(Part part) {
        idTxt.setText(String.valueOf(part.getId()));
        nameTxt.setText(part.getName());
        invTxt.setText(String.valueOf(part.getStock()));
        priceTxt.setText(String.valueOf(part.getPrice()));
        maxTxt.setText(String.valueOf(part.getMax()));
        minTxt.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            machComTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            inHouseRBtn.setSelected(true);
            machComLbl.setText("Machine ID");
        } else {
            machComTxt.setText(((Outsourced)part).getCompanyName());
            outsourcedRBtn.setSelected(true);
            machComLbl.setText("Company Name");
        }
    }

    /** This method allows user to switch between InHouse and Outsourced part types.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionInHouse(ActionEvent actionEvent) {
        machComLbl.setText("Machine ID");
    }

    /** This method allows user to switch between InHouse and Outsourced part types.
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionOutsourced(ActionEvent actionEvent) {
        machComLbl.setText("Company Name");
    }

    /** This method will save valid text field data to modify the data for an existing Part object.
     *  The method is able to check and confirm if the Part has a valid MachineID if it is an InHouse part.
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) throws IOException {
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

        if (inHouseRBtn.isSelected()) {
            try {
                int machComInt = Integer.parseInt(machComTxt.getText());
                InHouse newIHPart = new InHouse(id, name, price, stock, min, max, machComInt);
                Inventory.updatePart(id, newIHPart);
            } catch (NumberFormatException e) {
                MainForm.alertBox("Error Dialog", "Please enter a valid integer value for the Machine ID field.");
                return;
            }
        } else {
            String machComString = machComTxt.getText();
            Outsourced newOPart = new Outsourced(id, name, price, stock, min, max, machComString);
            Inventory.updatePart(id, newOPart);
        }
        MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
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
