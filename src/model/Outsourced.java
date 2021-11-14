package model;

/** This class is responsible for the functionality of the Outsourced class which is a subclass of "Part". */
public class Outsourced extends Part{
    private String companyName;

    /** This constructor uses a super method to initialize the fields from its super class "Part".
     *   @param id to set id
     *   @param name to set name
     *   @param price to set price
     *   @param stock to set stock
     *   @param min to set min
     *   @param max to set max
     *   @param companyName to set machineId
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price,stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return the machineId
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
