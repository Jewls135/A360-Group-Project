/**
 * Represents an Airplane with various properties like make, model, type, tank size,
 * fuel burn rate, and airspeed.
 */
public class Airplane {
    
    private String makeAndModel;
    private int type;
    private double tankSize;
    private double fuelBurnRate;
    private double airspeed;
    private int key;

    /**
     * Constructs an Airplane with the specified attributes.
     *
     * @param newKey The unique key identifier for the airplane.
     * @param makeAndModel The make and model of the airplane.
     * @param type The type of the airplane (1: Jet, 2: Turboprop, 3: Prop plane).
     * @param tankSize The fuel tank size in gallons.
     * @param fuelBurnRate The fuel burn rate in gallons per hour.
     * @param airspeed The airspeed of the airplane in kilometers per hour.
     */
    public Airplane(int newKey, String makeAndModel, int type, double tankSize, double fuelBurnRate,
            double airspeed) {
        setKey(newKey);
        setMakeAndModel(makeAndModel);
        setType(type);
        setTankSize(Math.round(tankSize * 10000.0) / 10000.0);
        setFuelBurnRate(Math.round(fuelBurnRate * 10000.0) / 10000.0);
        setAirspeed(Math.round(airspeed * 10000.0) / 10000.0);
    }

    /**
     * Returns the unique key identifier for the airplane.
     *
     * @return The key of the airplane.
     */
    public int getKey() {
        return key;
    }

    /**
     * Sets the unique key identifier for the airplane.
     *
     * @param newKey The new key for the airplane.
     */
    public void setKey(int newKey) {
        key = newKey;
    }

    /**
     * Returns the make and model of the airplane.
     *
     * @return The make and model of the airplane.
     */
    public String getMakeAndModel() {
        return makeAndModel;
    }

    /**
     * Sets the make and model of the airplane.
     *
     * @param newMakeAndModel The new make and model of the airplane.
     */
    private void setMakeAndModel(String newMakeAndModel) {
        makeAndModel = newMakeAndModel;
    }

    /**
     * Returns the type of the airplane.
     *
     * @return The type of the airplane (1: Jet, 2: Turboprop, 3: Prop plane).
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of the airplane.
     *
     * @param newType The new type of the airplane (1: Jet, 2: Turboprop, 3: Prop plane).
     */
    private void setType(int newType) {
        type = newType;
    }

    /**
     * Returns the fuel tank size of the airplane in gallons.
     *
     * @return The fuel tank size in gallons.
     */
    public double getTankSize() {
        return tankSize;
    }

    /**
     * Sets the fuel tank size of the airplane.
     *
     * @param newTankSize The new fuel tank size in gallons.
     */
    private void setTankSize(double newTankSize) {
        tankSize = newTankSize;
    }

    /**
     * Returns the fuel burn rate of the airplane in gallons per hour.
     *
     * @return The fuel burn rate in gallons per hour.
     */
    public double getFuelBurnRate() {
        return fuelBurnRate;
    }

    /**
     * Sets the fuel burn rate of the airplane.
     *
     * @param newFuelBurnRate The new fuel burn rate in gallons per hour.
     */
    private void setFuelBurnRate(double newFuelBurnRate) {
        fuelBurnRate = newFuelBurnRate;
    }

    /**
     * Returns the airspeed of the airplane in kilometers per hour.
     *
     * @return The airspeed in kilometers per hour.
     */
    public double getAirspeed() {
        return airspeed;
    }

    /**
     * Sets the airspeed of the airplane.
     *
     * @param newAirspeed The new airspeed in kilometers per hour.
     */
    private void setAirspeed(double newAirspeed) {
        airspeed = newAirspeed;
    }

    /**
     * Returns a CSV formatted string representing the airplane's attributes.
     *
     * @return The airplane's attributes as a CSV string.
     */
    public String toCSV() {
        return key + "," + makeAndModel + "," + type + "," + tankSize + "," + fuelBurnRate + "," + airspeed;
    }

    /**
     * Returns a string representation of the airplane's attributes in a readable format.
     *
     * @return The airplane's details in a human-readable format.
     */
    public String displayInfo() {
        return "Key: " + key +
                ", Make and Model: " + makeAndModel +
                ", Type: "
                + ((type == 1) ? "Jet"
                        : (type == 2) ? "Turboprop" : (type == 3) ? "Prop plane" : "Unknown Type of Plane")
                +
                ", Tank Size: " + tankSize + " Gallons" +
                ", Fuel Burn Rate: " + fuelBurnRate + " G/h" +
                ", Airspeed: " + airspeed + " Knots";
    }
}
