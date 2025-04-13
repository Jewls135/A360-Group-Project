import java.io.*;
import java.util.*;

public class AirportManager {
    private ArrayList<Airport> airports;

    public AirportManager() {
        airports = new ArrayList<>();
        loadAirportsFromCSV("Airports.csv");
    }

    private void loadAirportsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
    
                if (parts.length >= 6) {
                    String ICAO = parts[1].trim();
                    String name = parts[2].trim();
                    double latitude = Double.parseDouble(parts[3].trim());
                    double longitude = Double.parseDouble(parts[4].trim());
    
                    // Converting frequencies into HashMap<String, Double>
                    HashMap<String, Double> frequencies = new HashMap<>();
                    String[] freqPairs = parts[5].trim().split(";");
                    for (String pair : freqPairs) {
                        String[] kv = pair.split(":");
                        if (kv.length == 2) {
                            frequencies.put(kv[0].trim(), Double.parseDouble(kv[1].trim()));
                        }
                    }
    
                    // Converting fuel types into String[]
                    String[] fuelTypes = {};
                    if (parts.length >= 7) {
                        fuelTypes = parts[6].trim().split(";");
                    }
    
                    Airport airport = new Airport(ICAO, name, latitude, longitude, frequencies, fuelTypes);
                    airports.add(airport);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void saveAirportsToCSV(String fileName) {
        // New file for error handling in case user cuts program off short
        File tempFile = new File(fileName + ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            for (Airport airport : airports) {
                // Writing each airport's data to CSV format
                bw.write(airport.toCSV());
                bw.newLine();
            }
            
            // File writing finished, now we can update
            // First rename the temp file to the original file, then delete the temp file
            File originalFile = new File(fileName);
            if (originalFile.exists()) {
                originalFile.delete();
            }
            tempFile.renameTo(originalFile);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    

    private boolean exists(Airport airport) {
        return airports.contains(airport);
    }

    public void addAirport(Airport airport) {
        if (!exists(airport)) {
            airports.add(airport);
            saveAirportsToCSV("Airports.csv");
        } else {
            System.out.println("Could not add Airport, Airport already exists");
        }
    }

    public ArrayList<Airport> getAirports() {
        return airports;
    }

    public void deleteAirport(Airport airport) {
        if (exists(airport)) {
            airports.remove(airport);
            saveAirportsToCSV("Airports.csv");
        } else {
            System.out.println("Could not delete Airport, Airport not found");
        }
    }

    public void editAirport(int airportIndex, Airport newAirport) {
        if (airportIndex >= 0 && airportIndex < airports.size() && exists(airports.get(airportIndex))) {
            airports.set(airportIndex, newAirport);
        } else {
            System.out.println("Could not edit Airport, Airport not found");
        }
    }

    public void displayAirport(Airport airport) {
        if (exists(airport)) {
            String airportInformation = airport.displayInfo(); 
            System.out.println(airportInformation);
        } else {
            System.out.println("Could not display Airport, Airport not found");
        }
    }

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
