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
import javafx.scene.control.Alert.AlertType;
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
public class MainScreenController implements Initializable {

    @FXML private TextField fxidPartSearch;
    @FXML private TableView<Part> fxidPartList;
    @FXML private TableColumn<Part, Integer> fxidPartListIDCol;
    @FXML private TableColumn<Part, String> fxidPartListNameCol;
    @FXML private TableColumn<Part, Integer> fxidPartListInvCol;
    @FXML private TableColumn<Part, String> fxidPartListPriceCol;
    
    
    
    @FXML private TextField fxidProdSearch;
    @FXML private TableView<Product> fxidProdList;
    @FXML private TableColumn<Part, Integer> fxidProdListIDCol;
    @FXML private TableColumn<Part, String> fxidProdListNameCol;
    @FXML private TableColumn<Part, Integer> fxidProdListInvCol;
    @FXML private TableColumn<Part, String> fxidProdListPriceCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        fxidPartListIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        fxidPartListNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        fxidPartListInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        fxidPartListPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        //populate the tableview with all the parts
        this.fxidPartList.setItems(Inventory.getAllParts());
        
        
        //set up the columns in the table
        fxidProdListIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        fxidProdListNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        fxidProdListInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        fxidProdListPriceCol.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        //populate the tableview with all the parts
        this.fxidProdList.setItems(Inventory.getAllProducts());
        
        //load dummy data
        //fxidPartList.setItems(getDummy());
        
        //Do I even need this block of code for searching??? 
        /*
        fxidPartSearch.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) {
                System.out.println("You clicked off the search box.");
            }
        });*/
    }    

    @FXML private void handlePartSearchButton(ActionEvent event) {
        String searchValue = fxidPartSearch.getText();
        try {
            // Let's try to search by Integer/Part Number.
            if (Inventory.lookupPart(Integer.parseInt(searchValue)) != null) {
                //Part Found - Select it in the list.
                fxidPartList.getSelectionModel().select(Integer.parseInt(searchValue));
            } else {
                Alert partSearchError = new Alert(AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        } catch(NumberFormatException e) {
            //must be a string search if we get to here...!
            // Let's try to search by string/name.
            if (Inventory.lookupPart(searchValue) != null) {
                //Part Found - Select it in the list.
                fxidPartList.getSelectionModel().select(Inventory.lookupPart(searchValue).getId());
            } else {
                Alert partSearchError = new Alert(AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        }
        
    }

    @FXML
    private void handlePartAdd(ActionEvent event) throws IOException {
        Parent addPartScreenParent = FXMLLoader.load(getClass().getResource("addPartScreen.fxml"));
        Scene addPartScreenScene = new Scene(addPartScreenParent);
        //this line gets the stage informatoin
        Stage addPartScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        addPartScreenStage.setScene(addPartScreenScene);
        addPartScreenStage.setTitle("Add Part - Inventory Management");
        addPartScreenStage.show();
    }

    @FXML
    private void handlePartModify(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyPartScreen.fxml"));
        Parent modifyPartScreenParent = loader.load();

        Scene modifyPartScreenScene = new Scene(modifyPartScreenParent);

        //this line gets the stage informatoin
        Stage modifyPartScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        modifyPartScreenStage.setScene(modifyPartScreenScene);
        modifyPartScreenStage.setTitle("Modify Part - Inventory Management");
        
        //Pass the selected data to the Modify screen
        ModifyPartScreenController controller = loader.getController();
        controller.initData(fxidPartList.getSelectionModel().getSelectedItem());
        //Now show the screen.
        modifyPartScreenStage.show();

    }

    @FXML private void handlePartDelete(ActionEvent event) {
        //Get the current selected part.
        Part partDeleting = fxidPartList.getSelectionModel().getSelectedItem();
        
        //Display Confirm Box
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setHeaderText("Are you sure?");
        confirmDelete.setContentText("Are you sure you want to delete the " + partDeleting.getName() + " part?");
        Optional<ButtonType> result = confirmDelete.showAndWait();
        //If they click OK
        if (result.get() == ButtonType.OK){
            //Delete the part.
            Inventory.deletePart(partDeleting);
            //Refresh the list view.
            fxidPartList.setItems(Inventory.getAllParts());

            Alert successDelete = new Alert(Alert.AlertType.INFORMATION);
            successDelete.setHeaderText("Confirmation");
            successDelete.setContentText(partDeleting.getName() + " has been deleted.");
            successDelete.showAndWait();
        }

    }
    
    @FXML
    private void handleExitButton(ActionEvent event) {
        //Display Confirm Box
        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);
        confirmExit.setHeaderText("Are you sure?");
        confirmExit.setContentText("Are you sure you want to exit? All work will be lost.");
        Optional<ButtonType> result = confirmExit.showAndWait();
        //If they click OK
        if (result.get() == ButtonType.OK){
            System.exit(0);
        }
    }

    @FXML private void handleProdSearchButton(ActionEvent event) {
        String searchValue = fxidProdSearch.getText();
        try {
            // Let's try to search by Integer/Part Number.
            if (Inventory.lookupProduct(Integer.parseInt(searchValue)) != null) {
                //Part Found - Select it in the list.
                fxidProdList.getSelectionModel().select(Integer.parseInt(searchValue));
            } else {
                Alert partSearchError = new Alert(AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        } catch(NumberFormatException e) {
            //must be a string search if we get to here...!
            // Let's try to search by string/name.
            if (Inventory.lookupProduct(searchValue) != null) {
                //Part Found - Select it in the list.
                fxidProdList.getSelectionModel().select(Inventory.lookupProduct(searchValue).getId());
            } else {
                Alert partSearchError = new Alert(AlertType.INFORMATION);
                partSearchError.setHeaderText("Search Error");
                partSearchError.setContentText("Your search " + searchValue + " returned 0 results.");
                partSearchError.showAndWait();
            }
        }
    }

    @FXML private void handleProdAdd(ActionEvent event) throws IOException {
        Parent addProductScreenParent = FXMLLoader.load(getClass().getResource("addProductScreen.fxml"));
        Scene addProductScreenScene = new Scene(addProductScreenParent);
        //this line gets the stage informatoin
        Stage addProductScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        addProductScreenStage.setScene(addProductScreenScene);
        addProductScreenStage.setTitle("Add Product - Inventory Management");
        addProductScreenStage.show();
    }

    @FXML private void handleProdModify(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyProductScreen.fxml"));
        Parent modifyProdScreenParent = loader.load();

        Scene modifyProdScreenScene = new Scene(modifyProdScreenParent);

        //this line gets the stage informatoin
        Stage modifyProdScreenStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        modifyProdScreenStage.setScene(modifyProdScreenScene);
        modifyProdScreenStage.setTitle("Modify Product - Inventory Management");
        
        //Pass the selected data to the Modify screen
        ModifyProductScreenController controller = loader.getController();
        controller.initData(fxidProdList.getSelectionModel().getSelectedItem());
        //Now show the screen.
        modifyProdScreenStage.show();
    }
    
    @FXML private void handleProdDelete(ActionEvent event) {
        //Get the current selected part.
        Product prodDeleting = fxidProdList.getSelectionModel().getSelectedItem();
        
        //Display Confirm Box
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setHeaderText("Are you sure?");
        confirmDelete.setContentText("Are you sure you want to delete " + prodDeleting.getName() + "?");
        Optional<ButtonType> result = confirmDelete.showAndWait();
        //If they click OK
        if (result.get() == ButtonType.OK){
            //Delete the part.
            Inventory.deleteProduct(prodDeleting);
            //Refresh the list view.
            fxidProdList.setItems(Inventory.getAllProducts());
        }
    }
    
    
    public ObservableList<Part> getDummy() {
        
        ObservableList<Part> part = FXCollections.observableArrayList();
        
        part.add(new OutsourcedPart(0, "Graphics Card", 445.25, 20, 50, 5,"nVidia"));
        part.add(new InHousePart(1, "Motherboard",155.00, 22, 50, 20, 110287));
        part.add(new InHousePart(2, "2GB RAM", 89.00, 24, 50, 20, 98347));
        part.add(new OutsourcedPart(3,"3.6GHz CPU", 367.14, 37, 50, 20, "Ryzen"));
        
        return part;
    }
    
}
