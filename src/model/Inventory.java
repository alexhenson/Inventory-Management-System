package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is responsible for the functionality of the Inventory class which is the super class for all other
 *  classes in this package.  Its function is to instantiate the ObservableLists to hold all the parts and products
 *  created by its subclasses. It also has the implementation details for a variety of different methods used by
 *  its subclasses.
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    private static int partId = 1;
    private static int productId = 1000;

    /**
     * @return the partId
     */
    public static int getPartId() {
        return partId;
    }

    /**
     * @param partId the partId to set
     */
    public static void setPartId(int partId) {
        Inventory.partId = partId;
    }

    /**
     * @return the productId
     */
    public static int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public static void setProductId(int productId) {
        Inventory.productId = productId;
    }

    /** This method increments the partId field.
     *  @return int value of partID
     */
    public static int incrementPartId() {
        return partId++;
    }

    /** This method increments the productId field.
     *  @return int value of productID
     */
    public static int incrementProductId() {
        return productId++;
    }

    /** This method a Part object to the ObservableList allParts.
     * @param newPart Part object selected in the TableView object
     */
    public static void addPart(Part newPart) { allParts.add(newPart); }

    /** This method a Product object to the ObservableList allProducts.
     * @param newProduct Product object selected in the TableView object
     */
    public static void addProduct(Product newProduct) { allProducts.add(newProduct); }

    /** This method searches for and returns a part object from allParts.
     * @param partId int id of part
     *  @return Part object that was found
     */
    public static Part lookupPart(int partId) {
        for (Part part: allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        System.out.println("Did not find part (ID)");
        return null;
    }

    /** This method searches for and returns a product object from allProducts.
     * @param productId int id of product
     *  @return Product object that was found
     */
    public static Product lookupProduct(int productId) {
        for (Product product: allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        System.out.println("Did not find product (ID)");
        return null;
    }

    /** This method searches for parts that contain the substring partName and returns an ObservableList
     *  @param partName String name of part
     *  @return ObservableList of parts that contain the String partName
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempParts = FXCollections.observableArrayList();
        for (Part part: allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                tempParts.add(part);
            }
        }
        return tempParts;
    }

    /** This method searches for products that contain the substring productName and returns an ObservableList
     *  @param productName String name of product
     *  @return ObservableList of products that contain the String productName
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempProducts = FXCollections.observableArrayList();
        for (Product product: allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                tempProducts.add(product);
            }
        }
        return tempProducts;
    }

    /** This method updates a part in the allParts ObservableList
     *  @param index id of part
     *  @param selectedPart Part to update
     */
    public static void updatePart(int index, Part selectedPart) {
        for (Part part: allParts) {
            if (part.getId() == index) {
                allParts.set(index - 1, selectedPart);
            }
        }
    }

    /** This method updates a product in the allProducts ObservableList
     *  @param index id of product
     *  @param selectedProduct Product to update
     */
    public static void updateProduct(int index, Product selectedProduct) {
        for (Product product: allProducts) {
            if (product.getId() == index) {
                allProducts.set(index - 1000, selectedProduct);
            }
        }
    }

    public static boolean deletePart(Part selectedPart) { return allParts.remove(selectedPart); }

    /** This method deletes a product in the allProducts ObservableList
     *  @param selectedProduct Product object to delete
     *  @return selectedProduct Product to update
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /** This method returns an ObservableList called allParts
     *  @return allParts ObservableList of parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** This method returns an ObservableList called allProducts
     *  @return allProducts ObservableList of products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
