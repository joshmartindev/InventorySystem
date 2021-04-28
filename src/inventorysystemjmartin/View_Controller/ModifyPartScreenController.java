/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystemjmartin.View_Controller;

import inventorysystemjmartin.Model.InHousePart;
import inventorysystemjmartin.Model.Inventory;
import inventorysystemjmartin.Model.OutsourcedPart;
import inventorysystemjmartin.Model.Part;
import java.io.IOException;
import static java.lang.Boolean.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joshp
 */
public class ModifyPartScreenController implements Initializable {
    private Part selectedPart;
    private boolean isInHouse;
    private boolean isOutsourced;
    
    @FXML private RadioButton fxidInHouse;
    @FXML private ToggleGroup partType;
    @FXML private Label fxidAddPartCompanyMachineLabel;
    @FXML private RadioButton fxidOutsourced;
    @FXML private TextField fxidModifyPartID;
    @FXML private TextField fxidModifyPartName;
    @FXML private TextField fxidModifyPartInv;
    @FXML private TextField fxidModifyPartCost;
    @FXML private TextField fxidModifyPartMin;
    @FXML private TextField fxidModifyPartMax;
    @FXML private TextField fxidModifyPartCompany;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML private void handlePartType(ActionEvent event) {
        if (fxidInHouse.selectedProperty().getValue() == true) {
            fxidModifyPartCompany.accessibleTextProperty().setValue("Machine ID");
            fxidAddPartCompanyMachineLabel.setText("Machine ID");
        } else {
            fxidModifyPartCompany.accessibleTextProperty().setValue("Company Name");
            fxidAddPartCompanyMachineLabel.setText("Company Name");
        }
    }

    @FXML private void handleModifyPartSave(ActionEvent event) throws IOException {
        int id = Integer.parseInt(fxidModifyPartID.getText());
        String name = fxidModifyPartName.getText();
        int inv = Integer.parseInt(fxidModifyPartInv.getText());
        Double cost = Double.parseDouble(fxidModifyPartCost.getText());
        int max = Integer.parseInt(fxidModifyPartMax.getText());
        int min = Integer.parseInt(fxidModifyPartMin.getText());
        
        int checkMin = min;
        int checkMax = max;
        int checkInv = inv;
        
        if (checkInv <= checkMax && checkInv >= checkMin) {
        
            //If the part is currently outsourced, but the InHouse option is checked
            //we need to convert it into an InHouse Part.
            if (isOutsourced && fxidInHouse.selectedProperty().getValue() == true) {
                int machineID = Integer.parseInt(fxidModifyPartCompany.getText());
                InHousePart changedPart = new InHousePart(id, name, cost, inv, min, max, machineID);
                Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), changedPart);
            }
            //If ths part is current InHouse, but the Outsourced option is checked
            //we need to convert it into an Outsourced Part
            else if (isInHouse && fxidOutsourced.selectedProperty().getValue() == true) {
                String companyName = fxidModifyPartCompany.getText();
                OutsourcedPart changedPart = new OutsourcedPart(id, name, cost, inv, min, max, companyName);
                Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), changedPart);
            }
            //We aren't converting into a different type of part, but we need to
            //update the part respective to what it already is.  If it's Outsourced,
            //then update the part. 
            else {
                if (isOutsourced) {
                    String companyName = fxidModifyPartCompany.getText();
                    OutsourcedPart changedPart = new OutsourcedPart(id, name, cost, inv, min, max, companyName);
                    Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), changedPart);
                } else if (isInHouse) {
                    int machineID = Integer.parseInt(fxidModifyPartCompany.getText());
                    InHousePart changedPart = new InHousePart(id, name, cost, inv, min, max, machineID);
                    Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), changedPart);
                }
            }

            //Show the Main Screen
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            //this line gets the stage informatoin
            Stage mainScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.setTitle("Inventory Management");
            mainScreenStage.show();
        } else {
            //Inventory level falls outside of our Min/Max. Display error.
            Alert partModifyError = new Alert(Alert.AlertType.WARNING);
            partModifyError.setHeaderText("Modify Part Error");
            partModifyError.setContentText("Inventory level must be have a minimum of " + Integer.toString(checkMin) + " and no more than " + Integer.toString(checkMax));
            partModifyError.showAndWait();
        }
    }

    @FXML private void handleModifyPartCancel(ActionEvent event) throws IOException {
        //Display Confirm Box
        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);
        confirmExit.setHeaderText("Are you sure?");
        confirmExit.setContentText("Are you sure you want to cancel? All work will be lost.");
        Optional<ButtonType> result = confirmExit.showAndWait();
        //If they click OK
        if (result.get() == ButtonType.OK){
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            //this line gets the stage informatoin
            Stage mainScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.setTitle("Inventory Management");
            mainScreenStage.show();
        }
    }
    
    public void initData(Part part) {
        selectedPart = part;
        fxidModifyPartID.setText(Integer.toString(selectedPart.getId()));
        fxidModifyPartName.setText(selectedPart.getName());
        fxidModifyPartInv.setText(Integer.toString(selectedPart.getStock()));
        fxidModifyPartCost.setText(Double.toString(selectedPart.getPrice()));
        fxidModifyPartMin.setText(Integer.toString(selectedPart.getMin()));
        fxidModifyPartMax.setText(Integer.toString(selectedPart.getMax()));
        
        if (part instanceof OutsourcedPart) {
            // This is an Outsourced Part
            isOutsourced = true;
            fxidOutsourced.selectedProperty().setValue(TRUE);
            fxidModifyPartCompany.accessibleTextProperty().setValue("Company Name");
            fxidAddPartCompanyMachineLabel.setText("Company Name");
            OutsourcedPart selectedOutsourcedPart = (OutsourcedPart) part;
            fxidModifyPartCompany.setText(selectedOutsourcedPart.getCompanyName());
        } else if (part instanceof InHousePart) { 
            // This is an InHouse Part
            isInHouse = true;
            fxidInHouse.selectedProperty().setValue(TRUE);
            fxidModifyPartCompany.accessibleTextProperty().setValue("Machine ID");
            fxidAddPartCompanyMachineLabel.setText("Machine ID");
            InHousePart selectedInHousePart = (InHousePart) part;
            fxidModifyPartCompany.setText(Integer.toString(selectedInHousePart.getMachineId()));
        } 
    }
    
}
