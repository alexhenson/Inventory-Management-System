package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainForm.addPartId;

/** This class is responsible for the functionality of the "Add Part" form. */
public class AddPartForm implements Initializable {
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
    private TextField priceTxt;

    // This boolean is used to determine if the addPartId had already been incremented but not yet saved.
    boolean didIncrementId = false;

    /** This method activates when the scene starts.
     *  @param url for initialization
     *  @param resourceBundle for initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        machComLbl.setText("Machine ID");
        inHouseRBtn.setSelected(true);
    }

    /** This method activates when the In-House radio button is clicked. Will set the label to "Machine ID"
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionInHouse(ActionEvent actionEvent) {
        machComLbl.setText("Machine ID");
    }

    /** This method activates when the Outsourced radio button is clicked. Will set the label to "Company Name"
     *  @param actionEvent object to trigger actions
     */
    @FXML
    private void onActionOutsourced(ActionEvent actionEvent) {
        machComLbl.setText("Company Name");
    }

    /** This method activates when the Save button is clicked.
     *  The input in the text boxes will be validated and then saved to a new part.
     *  The part will be saved in the Main Form's TableView
     *  @param actionEvent object to trigger actions
     *  @throws IOException If an input or output exception occurred
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) throws IOException {
        if (!didIncrementId) {
            addPartId = Inventory.incrementPartId();
            didIncrementId = true;
        }

        String name = validateString(nameTxt, "Name");
        int stock = validateInteger(invTxt, "Inv");
        double price = validateDouble(priceTxt, "Price");
        int min = validateInteger(minTxt, "Min");
        int max = validateInteger(maxTxt, "Max");

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
                Inventory.addPart(new InHouse(addPartId, name, price, stock, min, max, machComInt));
            } catch (NumberFormatException e) {
                MainForm.alertBox("Error Dialog", "Please enter a valid integer value for the Machine ID field.");
                return;
            }
        } else {
            String machComString = machComTxt.getText();
            if (machComString.isBlank()) {
                Inventory.addPart(new Outsourced(addPartId, name, price, stock, min, max, machComString));
            } else {
                MainForm.alertBox("Error Dialog", "Please enter a valid name for the Company Name.");
                return;
            }
        }
        MainForm.buttonAction("/view/MainForm.fxml", "Main Form", actionEvent);
    }

    /** This method activates when the Cancel button is clicked.
     *  This will clear all text fields and go back to the Main Form
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

    /** This method validates a String from the text box.
     *  @param textField object used with getText() method
     *  @param labelName name of the label for alert
     *  @return String or null
     */
    public static String validateString(TextField textField, String labelName) {
        if (textField.getText().isBlank()) {
            MainForm.alertBox("Error Dialog", "Please enter a valid string value for the " + labelName + " field.");
            return null;
        }
        return textField.getText();
    }

    /** This method validates an int from the text box.
     *  @param textField object used with getText() method
     *  @param labelName name of the label for alert
     *  @return int or -1
     */
    public static int validateInteger(TextField textField, String labelName) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            MainForm.alertBox("Error Dialog", "Please enter a valid integer value for the " + labelName + " field.");
            return -1;
        }
    }

    /** This method validates a double from the text box.
     *  @param textField object used with getText() method
     *  @param labelName name of the label for alert
     *  @return double or -1
     */
    public static double validateDouble(TextField textField, String labelName) {
        try {
            return Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
            MainForm.alertBox("Error Dialog", "Please enter a valid decimal value for the " + labelName + " field.");
            return -1;
        }
    }
}

