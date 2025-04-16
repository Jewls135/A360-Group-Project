import java.util.HashMap;

/**
 * Represents an airport with details such as identifier, name, location, frequencies, and fuel types.
 */
public class Airport {
    
    private String identifier;
    private String name;
    private double latitude;
    private double longitude;
    private HashMap<String, Double> frequencies;
    private String[] fuelTypes;

    /**
     * Constructs an Airport object with the given details.
     *
     * @param identifier The ICAO identifier of the airport.
     * @param name The name of the airport.
     * @param latitude The latitude of the airport.
     * @param longitude The longitude of the airport.
     * @param frequencies A map of frequencies available at the airport, with the frequency name as the key and frequency value as the value.
     * @param fuelTypes A list of fuel types available at the airport.
     */
    public Airport(String identifier, String name, double latitude, double longitude,
            HashMap<String, Double> frequencies, String[] fuelTypes) {
        setIdentifier(identifier);
        setName(name);
        setLatitude(Math.round(latitude * 10000.0) / 10000.0);
        setLongitude(Math.round(longitude * 10000.0) / 10000.0);
        setFrequencies(frequencies);
        setFuelTypes(fuelTypes);
    }

    /**
     * Returns the ICAO identifier of the airport.
     *
     * @return The ICAO identifier of the airport.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the ICAO identifier of the airport.
     *
     * @param newIdentifier The new ICAO identifier to set.
     */
    private void setIdentifier(String newIdentifier) {
        identifier = newIdentifier;
    }

    /**
     * Returns the name of the airport.
     *
     * @return The name of the airport.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the airport.
     *
     * @param newName The new name of the airport.
     */
    private void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the latitude of the airport.
     *
     * @return The latitude of the airport.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the airport.
     *
     * @param newLatitude The new latitude of the airport.
     */
    private void setLatitude(double newLatitude) {
        latitude = newLatitude;
    }

    /**
     * Returns the longitude of the airport.
     *
     * @return The longitude of the airport.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the airport.
     *
     * @param newLongitude The new longitude of the airport.
     */
    private void setLongitude(double newLongitude) {
        longitude = newLongitude;
    }

    /**
     * Returns a map of the frequencies available at the airport.
     *
     * @return A map of frequencies with the frequency name as the key and frequency value as the value.
     */
    public HashMap<String, Double> getFrequencies() {
        return frequencies;
    }

    /**
     * Sets the frequencies available at the airport.
     *
     * @param newFrequencies A map of frequencies with the frequency name as the key and frequency value as the value.
     */
    private void setFrequencies(HashMap<String, Double> newFrequencies) {
        frequencies = new HashMap<>();
        for (HashMap.Entry<String, Double> entry : newFrequencies.entrySet()) {
            double rounded = Math.round(entry.getValue() * 1000.0) / 1000.0;
            frequencies.put(entry.getKey(), rounded);
        }
    }

    /**
     * Returns the list of fuel types available at the airport.
     *
     * @return An array of fuel types available at the airport.
     */
    public String[] getFuelTypes() {
        return fuelTypes;
    }

    /**
     * Sets the fuel types available at the airport.
     *
     * @param newFuelTypes An array of fuel types available at the airport.
     */
    private void setFuelTypes(String[] newFuelTypes) {
        fuelTypes = newFuelTypes;
    }

    /**
     * Converts the airport details into a CSV string representation.
     *
     * @return A CSV string containing the airport's identifier, name, location, frequencies, and fuel types.
     */
    public String toCSV() {
        StringBuilder sb = new StringBuilder();

        sb.append(identifier).append(",") // ICAO
                .append(name).append(",") // Airport
                .append(latitude).append(",") // Latitude
                .append(longitude).append(","); // Longitude

        // Converting frequencies HashMap to a string where key-value pairs are
        // separated by a ":" and pairs are separated by ";"
        sb.append(frequencies.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .reduce((first, second) -> first + ";" + second).orElse("")).append(",");

        // Convert fuelTypes array to a string, where values are separated by semicolons
        sb.append(String.join(";", fuelTypes));

        return sb.toString();
    }

    /**
     * Returns a string representation of the airport with its details including identifier, name, location, frequencies, and fuel types.
     *
     * @return A string with formatted airport information.
     */
    public String displayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Identifier: ").append(identifier).append(", ");
        sb.append("Name: ").append(name).append(", ");
        sb.append("Latitude: ").append(latitude).append(", ");
        sb.append("Longitude: ").append(longitude).append(", ");

        sb.append("Frequencies: ");
        for (String key : frequencies.keySet()) {
            sb.append(key).append(": ").append(frequencies.get(key)).append(" MHz");
            sb.append(", ");
        }

        sb.append("Fuel Types: ");
        if (fuelTypes.length > 0) {
            for (int i = 0; i < fuelTypes.length; i++) {
                sb.append(fuelTypes[i]);
                if (i < fuelTypes.length - 1)
                    sb.append(", ");
            }
        } else {
            sb.append("None");
        }

        return sb.toString();
    }
}
