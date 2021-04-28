/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystemjmartin.View_Controller;

import inventorysystemjmartin.Model.*;
import java.io.IOException;
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
public class AddPartScreenController implements Initializable {

    @FXML private ToggleGroup partType;
    @FXML private TextField fxidAddPartID;
    @FXML private TextField fxidAddPartName;
    @FXML private TextField fxidAddPartInv;
    @FXML private TextField fxidAddPartPrice;
    @FXML private TextField fxidAddPartMin;
    @FXML private TextField fxidAddPartMax;
    @FXML private TextField fxidAddPartCompanyMachine;
    @FXML private Label fxidAddPartCompanyMachineLabel;
    @FXML private RadioButton fxidInHouse;
    @FXML private RadioButton fxidOutsourced;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fxidAddPartID.setText(Integer.toString(Inventory.getAllParts().size()));
    }    

    @FXML
    private void handleAddPartSave(ActionEvent event) throws IOException {
        int checkMin = Integer.parseInt(fxidAddPartMin.getText());
        int checkMax = Integer.parseInt(fxidAddPartMax.getText());
        int checkInv = Integer.parseInt(fxidAddPartInv.getText());
        
        if (checkInv <= checkMax && checkInv >= checkMin) {
            //Inventory level is good.  Continue to save...
            //create the object instance based on the part type. 
            if (fxidInHouse.isSelected()) {
            InHousePart inPart = new InHousePart(Integer.parseInt(fxidAddPartID.getText()),
                                                  fxidAddPartName.getText(),
                                                  Double.parseDouble(fxidAddPartPrice.getText()),
                                                  Integer.parseInt(fxidAddPartInv.getText()),
                                                  Integer.parseInt(fxidAddPartMin.getText()),
                                                  Integer.parseInt(fxidAddPartMax.getText()),
                                                  Integer.parseInt(fxidAddPartCompanyMachine.getText()));
            Inventory.addPart(inPart); 
            } else if (fxidOutsourced.isSelected()) {
            OutsourcedPart outPart = new OutsourcedPart(Integer.parseInt(fxidAddPartID.getText()),
                                                  fxidAddPartName.getText(),
                                                  Double.parseDouble(fxidAddPartPrice.getText()),
                                                  Integer.parseInt(fxidAddPartInv.getText()),
                                                  Integer.parseInt(fxidAddPartMin.getText()),
                                                  Integer.parseInt(fxidAddPartMax.getText()),
                                                  fxidAddPartCompanyMachine.getText());
            Inventory.addPart(outPart);
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
            Alert partAddError = new Alert(Alert.AlertType.WARNING);
            partAddError.setHeaderText("Add Part Error");
            partAddError.setContentText("Inventory level must be have a minimum of " + Integer.toString(checkMin) + " and no more than " + Integer.toString(checkMax));
            partAddError.showAndWait();
        }
    }

    @FXML
    private void handleAddPartCancel(ActionEvent event) throws IOException {
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

    @FXML
    private void handleInHouse(ActionEvent event) {
        fxidAddPartCompanyMachineLabel.setText("Machine ID");
        fxidAddPartCompanyMachine.setPromptText("Machine ID");
    }

    @FXML
    private void handleOutsourced(ActionEvent event) {
        fxidAddPartCompanyMachineLabel.setText("Company Name");
        fxidAddPartCompanyMachine.setPromptText("Company Name");
    }
    
}
