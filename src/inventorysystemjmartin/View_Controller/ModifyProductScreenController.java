/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystemjmartin.View_Controller;

import inventorysystemjmartin.Model.Inventory;
import inventorysystemjmartin.Model.Part;
import inventorysystemjmartin.Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joshp
 */
public class ModifyProductScreenController implements Initializable {
    private Product selectedProduct;
    private ObservableList<Part> partToSave = FXCollections.observableArrayList();
    
    @FXML private TextField fxidModifyProdID;
    @FXML private TextField fxidModifyProdName;
    @FXML private TextField fxidModifyProdInv;
    @FXML private TextField fxidModifyProdPrice;
    @FXML private TextField fxidModifyProdMax;
    @FXML private TextField fxidModifyProdMin;
    @FXML private TableView<Part> fxidModifyProdAvailableParts;
    @FXML private TableView<Part> fxidModifyProdSelectedParts;
    @FXML private TextField fxidModifyProdSearch;
    
    @FXML private TableColumn<Part, Integer> fxidAvailablePartListIDCol;
    @FXML private TableColumn<Part, String> fxidAvailablePartListNameCol;
    @FXML private TableColumn<Part, Integer> fxidAvailablePartListInvCol;
    @FXML private TableColumn<Part, String> fxidAvailablePartListPriceCol;
    
    @FXML private TableColumn<Part, Integer> fxidSelectedPartListIDCol;
    @FXML private TableColumn<Part, String> fxidSelectedPartListNameCol;
    @FXML private TableColumn<Part, Integer> fxidSelectedPartListInvCol;
    @FXML private TableColumn<Part, String> fxidSelectedPartListPriceCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table view for Available Parts
        fxidAvailablePartListIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        fxidAvailablePartListNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        fxidAvailablePartListInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        fxidAvailablePartListPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        
        //set up the columns in the table view for Selected Parts
        fxidSelectedPartListIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fxidSelectedPartListNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxidSelectedPartListInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        fxidSelectedPartListPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    

    @FXML
    private void handleModifyProdAdd(ActionEvent event) {
        //make sure something is selected or we'll add a null item to the list
        if (fxidModifyProdAvailableParts.getSelectionModel().getSelectedItem() != null) {
            //get the selected part from the available list
            Part tempPart = fxidModifyProdAvailableParts.getSelectionModel().getSelectedItem();
            //save it to our temporary observable list
            partToSave.add(tempPart);
            //update the selected parts list
            fxidModifyProdSelectedParts.setItems(partToSave);            
        } else {
            Alert prodModifyAddPartError = new Alert(Alert.AlertType.INFORMATION);
            prodModifyAddPartError.setHeaderText("Error!");
            prodModifyAddPartError.setContentText("Please select a part to add.");
            prodModifyAddPartError.showAndWait();
        }
    }

    @FXML
    private void handleModifyProdDelete(ActionEvent event) {
        //Get the current selected part.
        Part partDeleting = fxidModifyProdSelectedParts.getSelectionModel().getSelectedItem();
        
        if (partDeleting != null) {
            //Display Confirm Box
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setHeaderText("Are you sure?");
            confirmDelete.setContentText("Are you sure you want to remove the " + partDeleting.getName() + " part?");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            //If they click OK
            if (result.get() == ButtonType.OK) {
                //Delete the part.
                partToSave.remove(partDeleting);
                //Refresh the list view.
                fxidModifyProdSelectedParts.setItems(partToSave);

                Alert successDelete = new Alert(Alert.AlertType.CONFIRMATION);
                successDelete.setHeaderText("Confirmation");
                successDelete.setContentText(partDeleting.getName() + " has been removed from product.");
                successDelete.showAndWait();
            }
        }  
    }

    @FXML
    private void handleModifyProdSave(ActionEvent event) throws IOException {
        int id = Integer.parseInt(fxidModifyProdID.getText());
        String name = fxidModifyProdName.getText();
        int inv = Integer.parseInt(fxidModifyProdInv.getText());
        Double cost = Double.parseDouble(fxidModifyProdPrice.getText());
        int max = Integer.parseInt(fxidModifyProdMax.getText());
        int min = Integer.parseInt(fxidModifyProdMin.getText());
            
        int checkMin = min;
        int checkMax = max;
        int checkInv = inv;
        
        if (checkInv <= checkMax && checkInv >= checkMin) {
            //Create a Product Object
            Product changedProduct = new Product(id, name, cost, inv, min, max);

            //associate the parts to the product
            for (Part part : partToSave) {
                changedProduct.addAssociatedPart(part);
            }

            Inventory.updateProduct(Inventory.getAllProducts().indexOf(selectedProduct), changedProduct);

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
            Alert modifyProdSave = new Alert(Alert.AlertType.WARNING);
            modifyProdSave.setHeaderText("Add Product Error");
            modifyProdSave.setContentText("Inventory level must be have a minimum of " + Integer.toString(checkMin) + " and no more than " + Integer.toString(checkMax));
            modifyProdSave.showAndWait();
        }
    }

    @FXML
    private void handleModifyProdCancel(ActionEvent event) throws IOException {
        //Display Confirm Box
        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);
        confirmExit.setHeaderText("Are you sure?");
        confirmExit.setContentText("Are you sure you want to cancel? All work will be lost.");
        Optional<ButtonType> result = confirmExit.showAndWait();
        //If they click OK
        if (result.get() == ButtonType.OK) {
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
    private void handleModifyProdSearch(ActionEvent event) {
        String searchValue = fxidModifyProdSearch.getText();
        try {
            // Let's try to search by Integer/Part Number.
            if (Inventory.lookupPart(Integer.parseInt(searchValue)) != null) {
                //Part Found - Select it in the list.
                fxidModifyProdAvailableParts.getSelectionModel().select(Integer.parseInt(searchValue));
            } else {
                Alert prodSearchError = new Alert(Alert.AlertType.INFORMATION);
                prodSearchError.setHeaderText("Search Error");
                prodSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                prodSearchError.showAndWait();
            }
        } catch(NumberFormatException e) {
            //must be a string search if we get to here...!
            // Let's try to search by string/name.
            if (Inventory.lookupPart(searchValue) != null) {
                //Part Found - Select it in the list.
                fxidModifyProdAvailableParts.getSelectionModel().select(Inventory.lookupPart(searchValue).getId());
            } else {
                Alert prodSearchError = new Alert(Alert.AlertType.INFORMATION);
                prodSearchError.setHeaderText("Search Error");
                prodSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                prodSearchError.showAndWait();
            }
        }       
    }

    public void initData(Product product) {
        selectedProduct = product;
        fxidModifyProdID.setText(Integer.toString(selectedProduct.getId()));
        fxidModifyProdName.setText(selectedProduct.getName());
        fxidModifyProdInv.setText(Integer.toString(selectedProduct.getStock()));
        fxidModifyProdPrice.setText(Double.toString(selectedProduct.getPrice()));
        fxidModifyProdMax.setText(Integer.toString(selectedProduct.getMax()));
        fxidModifyProdMin.setText(Integer.toString(selectedProduct.getMin()));
        
        fxidModifyProdAvailableParts.setItems(Inventory.getAllParts());
        fxidModifyProdSelectedParts.setItems(selectedProduct.getAllAssociatedParts());
        
        partToSave.addAll(selectedProduct.getAllAssociatedParts());
    }
    
}
