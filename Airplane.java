public class Airplane {
    private String makeAndModel;
    private int type;
    private double tankSize;
    private double fuelBurnRate;
    private double airspeed;
    private int key;

    public Airplane(int newKey, String makeAndModel, int type, double tankSize, double fuelBurnRate,
            double airspeed) {
        setKey(newKey);
        setMakeAndModel(makeAndModel);
        setType(type);
        setTankSize(Math.round(tankSize * 10000.0) / 10000.0);
        setFuelBurnRate(Math.round(fuelBurnRate * 10000.0) / 10000.0);
        setAirspeed(Math.round(airspeed * 10000.0) / 10000.0);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int newKey) {
        key = newKey;
    }

    public String getMakeAndModel() {
        return makeAndModel;
    }

    private void setMakeAndModel(String newMakeAndModel) {
        makeAndModel = newMakeAndModel;
    }

    public int getType() {
        return type;
    }

    private void setType(int newType) {
        type = newType;
    }

    public double getTankSize() {
        return tankSize;
    }

    private void setTankSize(double newTankSize) {
        tankSize = newTankSize;
    }

    public double getFuelBurnRate() {
        return fuelBurnRate;
    }

    private void setFuelBurnRate(double newFuelBurnRate) {
        fuelBurnRate = newFuelBurnRate;
    }

    public double getAirspeed() {
        return airspeed;
    }

    private void setAirspeed(double newAirspeed) {
        airspeed = newAirspeed;
    }

    public String toCSV() {
        return key + "," + makeAndModel + "," + type + "," + tankSize + "," + fuelBurnRate + "," + airspeed;
    }

    public String displayInfo() {
        return "Key: " + key +
                ", Make and Model: " + makeAndModel +
                ", Type: "
                + ((type == 1) ? "Jet"
                        : (type == 2) ? "Turboprop" : (type == 3) ? "Prop plane" : "Unknown Type of Plane")
                +
                ", Tank Size: " + tankSize + " Gallons" +
                ", Fuel Burn Rate: " + fuelBurnRate + " G/h" +
                ", Airspeed: " + airspeed + " Km/h";
    }
}
