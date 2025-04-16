import java.io.*;
import java.util.*;

/**
 * Manages a collection of airports and performs operations such as loading, saving, adding, editing, deleting, and displaying airports.
 */
public class AirportManager {
    private ArrayList<Airport> airports;

    /**
     * Constructs an AirportManager and loads airports from a CSV file.
     */
    public AirportManager() {
        airports = new ArrayList<>();
        if (!loadAirportsFromCSV("Airports.csv")){
            loadAirportsFromCSV("Airports.csv.tmp");
        }
    }

    /**
     * Loads airport data from a CSV file and populates the list of airports.
     *
     * @param fileName The name of the CSV file to load data from.
     * @return true if airports were successfully loaded, false otherwise.
     */
    private boolean loadAirportsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String ICAO = parts[0].trim();
                String name = parts[1].trim();
                double latitude = Double.parseDouble(parts[2].trim());
                double longitude = Double.parseDouble(parts[3].trim());

                HashMap<String, Double> frequencies = new HashMap<>();
                String[] freqPairs = parts[4].trim().replaceAll("[{}]", "").split(";");

                for (String pair : freqPairs) {
                    String[] kv = pair.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].trim();
                        String value = kv[1].trim();
                        if (!value.isEmpty()) {
                            try {
                                frequencies.put(key, Double.parseDouble(value));
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid number format for key: " + key + " value: " + value);
                            }
                        }
                    }
                }

                // Converting fuel types into String[]
                String[] fuelTypes = {};
                fuelTypes = parts[5].trim().split(";");

                Airport airport = new Airport(ICAO, name, latitude, longitude, frequencies, fuelTypes);
                airports.add(airport);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Saves the list of airports to a CSV file.
     *
     * @param fileName The name of the CSV file to save data to.
     */
    private void saveAirportsToCSV(String fileName) {
        File tempFile = new File(fileName + ".tmp");
        for(int i = 0; i < 3; i++) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
                for (Airport airport : airports) {
                    bw.write(airport.toCSV());
                    bw.newLine();
                }
                break;
            } catch (IOException e) {
                System.out.println("Error writing to .tmp file, " + "Attempt: " + i);
                e.printStackTrace();
            }
        }

        File fileToWrite = new File(fileName);
        for(int i = 0; i < 3; i++) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToWrite))) {
                for (Airport airport : airports) {
                    bw.write(airport.toCSV());
                    bw.newLine();
                }
                break;
            } catch (IOException e) {
                System.out.println("Error writing to .csv file, " + "Attempt: " + i);
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the specified airport already exists in the list.
     *
     * @param airport The airport to check for existence.
     * @return true if the airport exists, false otherwise.
     */
    private boolean exists(Airport airport) {
        return airports.contains(airport);
    }

    /**
     * Adds an airport to the list if it does not already exist, then saves the list to a CSV file.
     *
     * @param airport The airport to add.
     */
    public void addAirport(Airport airport) {
        if (!exists(airport)) {
            airports.add(airport);
            saveAirportsToCSV("Airports.csv");
        } else {
            System.out.println("Could not add Airport, Airport already exists");
        }
    }

    /**
     * Returns the list of all airports managed by this AirportManager.
     *
     * @return An ArrayList containing all airports.
     */
    public ArrayList<Airport> getAirports() {
        return airports;
    }

    /**
     * Deletes the specified airport from the list and saves the updated list to a CSV file.
     *
     * @param airport The airport to delete.
     */
    public void deleteAirport(Airport airport) {
        if (exists(airport)) {
            airports.remove(airport);
            saveAirportsToCSV("Airports.csv");
        } else {
            System.out.println("Could not delete Airport, Airport not found");
        }
    }

    /**
     * Edits an existing airport by removing the old airport and adding the new one, then saves the updated list to a CSV file.
     *
     * @param oldAirport The airport to be replaced.
     * @param newAirport The new airport to replace the old one.
     */
    public void editAirport(Airport oldAirport, Airport newAirport) {
        System.out.println("in editAirport");
        if(airports.contains(oldAirport)){
            System.out.println("in if");
            airports.remove(oldAirport);
            airports.add(newAirport);
            saveAirportsToCSV("Airports.csv");
        }
    }

    /**
     * Displays detailed information about the specified airport.
     *
     * @param airport The airport whose information is to be displayed.
     */
    public void displayAirport(Airport airport) {
        if (exists(airport)) {
            String airportInformation = airport.displayInfo();
            System.out.println(airportInformation);
        } else {
            System.out.println("Could not display Airport, Airport not found");
        }
    }

    /**
     * Displays detailed information about all airports managed by this AirportManager.
     */
    public void displayAllAirports() {
        if (airports.isEmpty()) {
            System.out.println("No airports available to display.");
        } else {
            for (Airport airport : airports) {
                String airportInformation = airport.displayInfo();
                System.out.println(airportInformation);
            }
        }
    }
}
