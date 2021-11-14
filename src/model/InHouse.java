package model;

/** This class is responsible for the functionality of the InHouse class which is a subclass of "Part". */
public class InHouse extends Part{
    private int machineId;

    /** This constructor uses a super method to initialize the fields from its super class "Part".
     *  @param id to set id
     *  @param name to set name
     *  @param price to set price
     *  @param stock to set stock
     *  @param min to set min
     *  @param max to set max
     *  @param machineId to set machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price,stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
