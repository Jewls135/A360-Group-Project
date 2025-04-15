import java.io.*;
import java.util.*;

public class AirportManager {
    private ArrayList<Airport> airports;

    public AirportManager() {
        airports = new ArrayList<>();
        if (!loadAirportsFromCSV("Airports.csv")){
            loadAirportsFromCSV("Airports.csv.tmp");
        }
    }

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

    public void editAirport(Airport oldAirport, Airport newAirport) {
        System.out.println("in editAirport");
        if(airports.contains(oldAirport)){
            System.out.println("in if");
            airports.remove(oldAirport);
            airports.add(newAirport);
            saveAirportsToCSV("Airports.csv");
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
