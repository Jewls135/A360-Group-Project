import java.io.*;
import java.util.*;

public class AirplaneManager {
    private ArrayList<Airplane> airplanes;

    public AirplaneManager() {
        airplanes = new ArrayList<>();
        // Incase we can't load from main file, load from tmp
        if (!loadAirplanesFromCSV("Airplanes.csv")){
            loadAirplanesFromCSV("Airplanes.csv.tmp");
        }
    }

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

    private boolean exists(Airplane airplane) {
        return airplanes.contains(airplane);
    }

    public void addAirplane(Airplane airplane) {
        if (!exists(airplane)) {
            airplanes.add(airplane);
            saveAirplanesToCSV("Airplanes.csv");  // Save to CSV after adding
        } else {
            System.out.println("Could not add Airplane, Airplane already exists");
        }
    }

    public ArrayList<Airplane> getAirplanes() {
        return airplanes;
    }

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

    public void editAirplane(int airplaneIndex, Airplane airplane) {
        if (exists(airplanes.get(airplaneIndex))) {
            airplanes.set(airplaneIndex, airplane);
            saveAirplanesToCSV("Airplanes.csv"); 
        } else {
            System.out.println("Could not edit Airplane, Airplane not found");
        }
    }

    public void displayAirplane(Airplane airplane) {
        if (exists(airplane)) {
            String airplaneInformation = airplane.displayInfo();
            System.out.println(airplaneInformation);
        } else {
            System.out.println("Could not display Airplane, Airplane not found");
        }
    }

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
