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
public class AddProductScreenController implements Initializable {

    @FXML private TextField fxidAddProdID;
    @FXML private TextField fxidAddProdName;
    @FXML private TextField fxidAddProdInv;
    @FXML private TextField fxidAddProdPrice;
    @FXML private TextField fxidAddProdMax;
    @FXML private TextField fxidAddProdMin;
    @FXML private TableView<Part> fxidAddProdAvailableParts;
    @FXML private TableView<Part> fxidAddProdSelectedParts;
    @FXML private TextField fxidAddProdSearch;
    
    @FXML private TableColumn<Part, Integer> fxidAvailablePartListIDCol;
    @FXML private TableColumn<Part, String> fxidAvailablePartListNameCol;
    @FXML private TableColumn<Part, Integer> fxidAvailablePartListInvCol;
    @FXML private TableColumn<Part, String> fxidAvailablePartListPriceCol;
    
    @FXML private TableColumn<Part, Integer> fxidSelectedPartListIDCol;
    @FXML private TableColumn<Part, String> fxidSelectedPartListNameCol;
    @FXML private TableColumn<Part, Integer> fxidSelectedPartListInvCol;
    @FXML private TableColumn<Part, String> fxidSelectedPartListPriceCol;

    private ObservableList<Part> partToSave = FXCollections.observableArrayList();
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
        //populate the tableview with all the parts
        this.fxidAddProdAvailableParts.setItems(Inventory.getAllParts());
        
        //set up the columns in the table view for Selected Parts
        fxidSelectedPartListIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fxidSelectedPartListNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxidSelectedPartListInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        fxidSelectedPartListPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //set the ID of the Product
        fxidAddProdID.setText(Integer.toString(Inventory.getAllProducts().size()));
    }    

    @FXML private void handleAddProdAdd(ActionEvent event) {
        //make sure something is selected or we'll add a null item to the list
        if (fxidAddProdAvailableParts.getSelectionModel().getSelectedItem() != null) {
            //get the selected part from the available list
            Part tempPart = fxidAddProdAvailableParts.getSelectionModel().getSelectedItem();
            //save it to our temporary observable list
            partToSave.add(tempPart);
            //update the selected parts list
            fxidAddProdSelectedParts.setItems(partToSave);            
        } else {
            Alert partSearchError = new Alert(Alert.AlertType.INFORMATION);
            partSearchError.setHeaderText("Error!");
            partSearchError.setContentText("Please select a part to add.");
            partSearchError.showAndWait();
        }
    }

    @FXML private void handleAddProdDelete(ActionEvent event) {
        //Get the current selected part.
        Part partDeleting = fxidAddProdSelectedParts.getSelectionModel().getSelectedItem();
        
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
                fxidAddProdSelectedParts.setItems(partToSave);

                Alert successDelete = new Alert(Alert.AlertType.CONFIRMATION);
                successDelete.setHeaderText("Confirmation");
                successDelete.setContentText(partDeleting.getName() + " has been removed from product.");
                successDelete.showAndWait();
            }
        }   
    }

    @FXML private void handleAddProdSave(ActionEvent event) throws IOException {
        int checkMin = Integer.parseInt(fxidAddProdMin.getText());
        int checkMax = Integer.parseInt(fxidAddProdMax.getText());
        int checkInv = Integer.parseInt(fxidAddProdInv.getText());
        
        if (checkInv <= checkMax && checkInv >= checkMin) {
            //Create an empty Product object
            Product newProduct = new Product();

            //add in required information for the product
            newProduct.setId(Integer.parseInt(fxidAddProdID.getText()));
            newProduct.setName(fxidAddProdName.getText());
            newProduct.setPrice(Double.parseDouble(fxidAddProdPrice.getText()));
            newProduct.setMin(Integer.parseInt(fxidAddProdMin.getText()));
            newProduct.setMax(Integer.parseInt(fxidAddProdMax.getText()));
            newProduct.setStock(Integer.parseInt(fxidAddProdInv.getText()));

            //associate the parts to the product
            for (Part part : partToSave ) {
                newProduct.addAssociatedPart(part);
            }

            //add the product to the inventory.
            Inventory.addProduct(newProduct);

            //Show the Main screen again
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            //this line gets the stage informatoin
            Stage mainScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.setTitle("Inventory Management");
            mainScreenStage.show();
        } else {
            //Inventory level falls outside of our Min/Max. Display error.
            Alert prodAddError = new Alert(Alert.AlertType.WARNING);
            prodAddError.setHeaderText("Add Product Error");
            prodAddError.setContentText("Inventory level must be have a minimum of " + Integer.toString(checkMin) + " and no more than " + Integer.toString(checkMax));
            prodAddError.showAndWait();
        }
                
        
    }

    @FXML private void handleAddProdCancel(ActionEvent event) throws IOException {
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

    @FXML private void handleAddProductSearch(ActionEvent event) {
        String searchValue = fxidAddProdSearch.getText();
        try {
            // Let's try to search by Integer/Part Number.
            if (Inventory.lookupPart(Integer.parseInt(searchValue)) != null) {
                //Part Found - Select it in the list.
                fxidAddProdAvailableParts.getSelectionModel().select(Integer.parseInt(searchValue));
            } else {
                Alert partSearchError = new Alert(Alert.AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        } catch(NumberFormatException e) {
            //must be a string search if we get to here...!
            // Let's try to search by string/name.
            if (Inventory.lookupPart(searchValue) != null) {
                //Part Found - Select it in the list.
                fxidAddProdAvailableParts.getSelectionModel().select(Inventory.lookupPart(searchValue).getId());
            } else {
                Alert partSearchError = new Alert(Alert.AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        }
    }
    
}
