import java.util.HashMap;

public class Airport {
    private String identifier;
    private String name;
    private double latitude;
    private double longitude;
    private HashMap<String, Double> frequencies;
    private String[] fuelTypes;

    public Airport(String identifier, String name, double latitude, double longitude,
            HashMap<String, Double> frequencies, String[] fuelTypes) {
        setIdentifier(identifier);
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
        setFrequencies(frequencies);
        setFuelTypes(fuelTypes);
    }

    public String getIdentifier() {
        return identifier;
    }

    private void setIdentifier(String newIdentifier) {
        identifier = newIdentifier;
    }

    public String getName() {
        return name;
    }

    private void setName(String newName) {
        name = newName;
    }

    public double getLatitude() {
        return latitude;
    }

    private void setLatitude(double newLatitude) {
        latitude = newLatitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setLongitude(double newLongitude) {
        longitude = newLongitude;
    }

    public HashMap<String, Double> getFrequencies() {
        return frequencies;
    }

    private void setFrequencies(HashMap<String, Double> newFrequencies) {
        frequencies = newFrequencies;
    }

    public String[] getFuelTypes() {
        return fuelTypes;
    }

    private void setFuelTypes(String[] newFuelTypes) {
        fuelTypes = newFuelTypes;
    }

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
