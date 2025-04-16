import java.io.*;
import java.util.*;

/**
 * Manages a collection of Airplane objects, providing methods for loading, saving, 
 * adding, deleting, and editing airplanes in a CSV file.
 */
public class AirplaneManager {
    
    private ArrayList<Airplane> airplanes;

    /**
     * Constructs an AirplaneManager object and loads airplanes from a CSV file.
     * If loading fails, it attempts to load from a backup temporary CSV file.
     */
    public AirplaneManager() {
        airplanes = new ArrayList<>();
        // In case we can't load from the main file, load from the temporary backup
        if (!loadAirplanesFromCSV("Airplanes.csv")){
            loadAirplanesFromCSV("Airplanes.csv.tmp");
        }
    }

    /**
     * Loads a list of airplanes from the specified CSV file.
     *
     * @param fileName The name of the file to load the airplanes from.
     * @return true if loading was successful, false otherwise.
     */
    private boolean loadAirplanesFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // 6 columns (key,makeAndModel,type,tankSize,fuelBurnRate,airspeed)
                    int key = Integer.parseInt(parts[0].trim());
                    String makeAndModel = parts[1].trim(); 
                    int type = Integer.parseInt(parts[2].trim());
                    double tankSize = Double.parseDouble(parts[3].trim());
                    double fuelBurnRate = Double.parseDouble(parts[4].trim());
                    double airspeed = Double.parseDouble(parts[5].trim());
        
                    Airplane airplane = new Airplane(key, makeAndModel, type, tankSize, fuelBurnRate, airspeed);
                    airplanes.add(airplane);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Saves the current list of airplanes to the specified CSV file.
     * A temporary file is created first, then the main file is overwritten with the saved data.
     *
     * @param fileName The name of the file to save the airplanes to.
     */
    private void saveAirplanesToCSV(String fileName) {
        File tempFile = new File(fileName + ".tmp");
        for(int i = 0; i < 3; i++) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
                for (Airplane airplane : airplanes) {
                    bw.write(airplane.toCSV());
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
                for (Airplane airplane : airplanes) {
                    bw.write(airplane.toCSV());
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
     * Checks if the given airplane already exists in the collection.
     *
     * @param airplane The airplane to check for existence.
     * @return true if the airplane exists, false otherwise.
     */
    private boolean exists(Airplane airplane) {
        return airplanes.contains(airplane);
    }

    /**
     * Adds an airplane to the collection if it does not already exist.
     *
     * @param airplane The airplane to add.
     */
    public void addAirplane(Airplane airplane) {
        if (!exists(airplane)) {
            airplanes.add(airplane);
            saveAirplanesToCSV("Airplanes.csv");  // Save to CSV after adding
        } else {
            System.out.println("Could not add Airplane, Airplane already exists");
        }
    }

    /**
     * Returns the list of all airplanes managed by this class.
     *
     * @return The list of airplanes.
     */
    public ArrayList<Airplane> getAirplanes() {
        return airplanes;
    }

    /**
     * Deletes the specified airplane from the collection and adjusts the keys of the remaining airplanes.
     *
     * @param airplane The airplane to delete.
     */
    public void deleteAirplane(Airplane airplane) {
        if (exists(airplane)) {
            int index = airplanes.indexOf(airplane); 
            airplanes.remove(index); 
            
            // Shifting / adjusting keys to fit with their new index in the ArrayList
            for (int i = index; i < airplanes.size(); i++) {
                airplanes.get(i).setKey(i);  
            }
    
            saveAirplanesToCSV("Airplanes.csv"); 
        } else {
            System.out.println("Could not delete Airplane, Airplane not found");
        }
    }

    /**
     * Edits the airplane at the specified index with a new airplane object.
     *
     * @param airplaneIndex The index of the airplane to edit.
     * @param airplane The new airplane object to set at the specified index.
     */
    public void editAirplane(int airplaneIndex, Airplane airplane) {
        if (exists(airplanes.get(airplaneIndex))) {
            airplanes.set(airplaneIndex, airplane);
            saveAirplanesToCSV("Airplanes.csv"); 
        } else {
            System.out.println("Could not edit Airplane, Airplane not found");
        }
    }

    /**
     * Displays information about a specific airplane.
     *
     * @param airplane The airplane whose information will be displayed.
     */
    public void displayAirplane(Airplane airplane) {
        if (exists(airplane)) {
            String airplaneInformation = airplane.displayInfo();
            System.out.println(airplaneInformation);
        } else {
            System.out.println("Could not display Airplane, Airplane not found");
        }
    }

    /**
     * Displays information about all airplanes in the collection.
     */
    public void displayAllAirplanes() {
        if (airplanes.isEmpty()) {
            System.out.println("No Airplanes available to display.");
        } else {
            for (Airplane airplane : airplanes) {
                String airplaneInformation = airplane.displayInfo(); 
                System.out.println(airplaneInformation);
            }
        }
    }
}
