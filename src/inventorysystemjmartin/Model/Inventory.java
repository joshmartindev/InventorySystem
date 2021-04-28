/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystemjmartin.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joshp
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    //Setters
    public static void addPart(Part part) {
        allParts.add(part);
    }
    
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
        
    }
    
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }
    
    public static void deletePart(Part part) {
        allParts.remove(part);
    }
    
    public static void deleteProduct(Product product) { 
        allProducts.remove(product);
        
    }

    
    //Getters
    public static Part lookupPart(int partId) {
        Part partLookup = null;
        for (Part part : allParts) {
            if (partId == part.getId()) {
                partLookup = part;
            } 
        }
        return partLookup;
    }
    
    public static Part lookupPart(String partName) {
        Part partLookup = null;
        for (Part part : allParts) {
            if (partName.toUpperCase().equals(part.getName().toUpperCase())) {
                partLookup = part;
            }
        }
        return partLookup;
    }
    
    public static Product lookupProduct(int productId) {
        Product prodLookup = null;
        for (Product product : allProducts) {
            if (productId == product.getId()) {
                prodLookup = product;
            } 
        }
        return prodLookup;
    }
    
    public static Product lookupProduct(String productName) {
        Product prodLookup = null;
        for (Product product : allProducts) {
            if (productName.toUpperCase().equals(product.getName().toUpperCase())) {
                prodLookup = product;
            }
        }
        return prodLookup;
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
