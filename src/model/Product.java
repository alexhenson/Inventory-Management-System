package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is responsible for the functionality of the Product class which has an ObservableList of
 *  objects from the Parts class and its subclasses InHouse and Outsourced.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** This constructor initializes the fields from the six parameters.
     *   @param id to set id
     *   @param name to set name
     *   @param price to set price
     *   @param stock to set stock
     *   @param min to set min
     *   @param max to set max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() { return id; }

    /**
     * @param id the id to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * @return the name
     */
    public String getName() { return name; }

    /**
     * @param name the name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * @return the price
     */
    public double getPrice() { return price; }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * @return the stock
     */
    public int getStock() { return stock; }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * @return the min
     */
    public int getMin() { return min; }

    /**
     * @param min the min to set
     */
    public void setMin(int min) { this.min = min; }

    /**
     * @return the max
     */
    public int getMax() { return max; }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /** This method adds a Part object to the ObservableList associatedParts.
     *  @param part Part object to add to associatedParts Observable List
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /** This method deletes a Part object from the ObservableList associatedParts.
     * @param selectedAssociatedPart Part object selected in the TableView object
     *  @return boolean value for whether part was removed or not
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /** This method returns the ObservableList associatedParts.
     * @return associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }



}
