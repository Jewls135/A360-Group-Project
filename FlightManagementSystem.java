import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class FlightManagementSystem {
    Flight flight;
    AirportManager airportManager;
    AirplaneManager airplaneManager;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        FlightManagementSystem fms = new FlightManagementSystem();
        fms.displayOptions();
    }

    private String getUserInputString() {
        String input = "";
        while (true) {
            try {
                input = scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        }
        return input;
    }

    private int getUserInputInt() {
        int input = -1;

        while (true) {
            String line = scanner.nextLine();

            try {
                input = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid integer.");
            }
        }

        return input;
    }

    private double getUserInputDouble() {
        double input;

        while (true) {
            String line = scanner.nextLine();

            try {
                input = Double.parseDouble(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number.");
            }
        }

        return input;
    }

    private void displayOptions() {
        // Initially populating databases
        updateDatabases();
        flight = new Flight();
        System.out.println("-- THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE --\n\n");
        while (true) {
            System.out.println("1. Manage Airports");
            System.out.println("2. Manage Airplanes");
            System.out.println("3. Plan a Flight Route");
            System.out.println("4. Close Flight Planning System");

            int choice = getUserInputInt();

            switch (choice) {
                case 1:
                    manageAirports();
                    break;
                case 2:
                    manageAirplanes();
                    break;
                case 3:
                    Airplane airplaneToUse = handleAirplaneChoice();
                    if (airplaneToUse == null) {
                        break;
                    }
                    ArrayList<Airport> flightDestinations = handleAirportChoice();
                    if (flightDestinations == null || flightDestinations.size() <= 1) {
                        break;
                    }
                    flight.planFlight(flightDestinations, airplaneToUse, airportManager.getAirports());
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice, please enter a valid option.");
            }
        }
    }

    private void updateDatabases() {
        // Initializing / populating both manager objects
        airportManager = new AirportManager();
        airplaneManager = new AirplaneManager();
    }

    private Airplane handleAirplaneChoice() {
        // Getting airplane choice
        ArrayList<Airplane> airplanes = airplaneManager.getAirplanes();
        if (airplanes.isEmpty()) {
            System.out.println("No airplanes available please add some.");
            return null;
        }

        System.out.println("Available Airplanes:");
        airplaneManager.displayAllAirplanes();

        System.out.println("Enter airplane key (0 to " + (airplanes.size() - 1) + "):");
        int airplaneIndex = getUserInputInt();

        if (airplaneIndex < 0 || airplaneIndex >= airplanes.size()) {
            System.out.println("Invalid key, please try again.");
            return null;
        }

        Airplane selectedAirplane = airplanes.get(airplaneIndex);
        return selectedAirplane;
    }

    private ArrayList<Airport> handleAirportChoice() {
        System.out.println("Available Airports:");
        airportManager.displayAllAirports();

        // Getting starting airport
        System.out.println("Enter starting airport (ICAO or part of name):");
        String startInput = getUserInputString(); // Get user input as string
        ArrayList<Airport> matchingStartAirports = findMatchingAirports(startInput);

        if (matchingStartAirports.isEmpty()) {
            System.out.println("No matching airports found.");
            return null;
        }

        Airport startAirport;
        if (matchingStartAirports.size() > 1) {
            System.out.println("Multiple airports found. Please choose one:");
            for (int i = 0; i < matchingStartAirports.size(); i++) {
                System.out.println((i + 1) + ". " + matchingStartAirports.get(i).displayInfo());
            }

            int startInputIndex = getUserInputInt();
            if (startInputIndex < 1 || startInputIndex > matchingStartAirports.size()) {
                System.out.println("Invalid selection. Aborting.");
                return null;
            }

            startAirport = matchingStartAirports.get(startInputIndex - 1);
        } else {
            startAirport = matchingStartAirports.get(0);
        }

        ArrayList<Airport> destinations = new ArrayList<>();
        destinations.add(startAirport);

        // Getting more airports
        while (true) {
            System.out.println("Enter destination airport (ICAO or part of name), or type 'done' to finish:");
            String destinationInput = getUserInputString();

            if (destinationInput.equalsIgnoreCase("done"))
                break;

            ArrayList<Airport> matchingDestinations = findMatchingAirports(destinationInput);
            if (matchingDestinations.isEmpty()) {
                System.out.println("No matching airports found.");
                continue;
            }

            if (matchingDestinations.size() > 1) {
                System.out.println("Multiple airports found. Please choose one:");
                for (int i = 0; i < matchingDestinations.size(); i++) {
                    System.out.println((i + 1) + ". " + matchingDestinations.get(i).displayInfo());
                }

                int destinationInputIndex = getUserInputInt();
                if (destinationInputIndex < 1 || destinationInputIndex > matchingDestinations.size()) {
                    System.out.println("Invalid selection, try again.");
                    continue;
                }

                destinations.add(matchingDestinations.get(destinationInputIndex - 1));
            } else {
                destinations.add(matchingDestinations.get(0));
            }
        }

        return destinations;
    }

    private ArrayList<Airport> findMatchingAirports(String input) {
        ArrayList<Airport> matchingAirports = new ArrayList<>();
        for (Airport airport : airportManager.getAirports()) {
            if (airport.getIdentifier().equalsIgnoreCase(input)
                    || airport.getName().toLowerCase().contains(input.toLowerCase())) {
                matchingAirports.add(airport);
            }
        }
        return matchingAirports;
    }

    private void manageAirports() {
        while (true) {
            System.out.println("1. List Airports");
            System.out.println("2. Add Airport");
            System.out.println("3. Edit Airport");
            System.out.println("4. Delete Airport");
            System.out.println("5. Back to Main Menu");

            int choice = getUserInputInt();

            switch (choice) {
                case 1:
                    airportManager.displayAllAirports();
                    break;
                case 2:
                    System.out.println("Enter new airport name:");
                    String name = getUserInputString();

                    System.out.println("Enter ICAO code:");
                    String icao = getUserInputString();

                    System.out.println("Enter latitude:");
                    double latitude = getUserInputDouble();

                    System.out.println("Enter longitude:");
                    double longitude = getUserInputDouble();

                    HashMap<String, Double> frequencies = new HashMap<>();
                    while (true) {
                        System.out.println("Enter frequency name (or type 'done' to finish):");
                        String freqName = getUserInputString();
                        if (freqName.equalsIgnoreCase("done"))
                            break;

                        System.out.println("Enter frequency value:");
                        double freqValue = getUserInputDouble();
                        frequencies.put(freqName, freqValue);
                    }

                    Set<String> validFuelTypes = new HashSet<>(Arrays.asList("AVGAS", "JA-a"));
                    String[] fuelTypes;

                    while (true) {
                        System.out.println("Enter fuel types (comma-separated, options: AVGAS, JA-a):");
                        String[] inputFuels = getUserInputString().split(",");
                        Set<String> fuelTypesSet = new HashSet<>();

                        boolean allValid = true;
                        for (String fuel : inputFuels) {
                            String trimmed = fuel.trim().toUpperCase();
                            if (validFuelTypes.contains(trimmed)) {
                                fuelTypesSet.add(trimmed);
                            } else {
                                System.out.println("Invalid fuel type entered: " + fuel.trim());
                                allValid = false;
                                break;
                            }
                        }

                        if (!allValid)
                            continue;

                        if (fuelTypesSet.isEmpty() || fuelTypesSet.size() > 2) {
                            System.out.println("Please enter one or two valid fuel types (AVGAS, JA-a).");
                        } else {
                            fuelTypes = fuelTypesSet.toArray(new String[fuelTypesSet.size()]);
                            break;
                        }
                    }

                    airportManager.addAirport(new Airport(icao, name, latitude, longitude, frequencies, fuelTypes));
                    updateDatabases();
                    break;
                case 3:
                    int selectedIndex = 0;
                    System.out.println("Enter airport name or ICAO code to edit:");
                    String input = getUserInputString();
                    ArrayList<Airport> matches = findMatchingAirports(input);

                    if (matches.isEmpty()) {
                        System.out.println("No matching airports found.");
                        break;
                    }

                    Airport airport;
                    if (matches.size() > 1) {
                        System.out.println("Multiple airports found. Please choose one:");
                        for (int i = 0; i < matches.size(); i++) {
                            System.out.println((i + 1) + ". " + matches.get(i).displayInfo());
                        }

                        selectedIndex = getUserInputInt();
                        if (selectedIndex < 1 || selectedIndex > matches.size()) {
                            System.out.println("Invalid selection. Aborting.");
                            break;
                        }

                        airport = matches.get(selectedIndex - 1);
                    } else {
                        airport = matches.get(0);
                    }

                    if (airport != null) {
                        System.out.println("Enter new name:");
                        String newName = getUserInputString();

                        System.out.println("Enter new ICAO code:");
                        String newIdentifier = getUserInputString();

                        System.out.println("Enter new latitude:");
                        double newLatitude = getUserInputDouble();

                        System.out.println("Enter new longitude:");
                        double newLongitude = getUserInputDouble();

                        HashMap<String, Double> newFrequencies = new HashMap<>();

                        while (true) {
                            System.out.println("Enter frequency name (or type 'done' to finish):");
                            String freqName = getUserInputString();
                            if (freqName.equalsIgnoreCase("done")) {
                                break;
                            }

                            System.out.println("Enter frequency value:");
                            double freqValue = getUserInputDouble();
                            newFrequencies.put(freqName, freqValue);
                        }

                        Set<String> validFuelType = new HashSet<>(Arrays.asList("AVGAS", "JA-a"));
                        String[] newFuelTypes;

                        while (true) {
                            System.out.println("Enter fuel types (comma-separated, options: AVGAS, JA-a):");
                            String[] inputFuels = getUserInputString().split(",");
                            Set<String> fuelTypesSet = new HashSet<>();

                            boolean allValid = true;
                            for (String fuel : inputFuels) {
                                String trimmed = fuel.trim().toUpperCase();
                                if (validFuelType.contains(trimmed)) {
                                    fuelTypesSet.add(trimmed);
                                } else {
                                    System.out.println("Invalid fuel type entered: " + fuel.trim());
                                    allValid = false;
                                    break;
                                }
                            }

                            if (!allValid)
                                continue;

                            if (fuelTypesSet.isEmpty() || fuelTypesSet.size() > 2) {
                                System.out.println("Please enter one or two valid fuel types (AVGAS, JA-a).");
                            } else {
                                newFuelTypes = fuelTypesSet.toArray(new String[fuelTypesSet.size()]);
                                break;
                            }
                        }

                        // Create a new airport with the updated information
                        Airport updatedAirport = new Airport(newIdentifier, newName, newLatitude, newLongitude,
                                newFrequencies, newFuelTypes);

                        // Update the airport in the airport manager
                        airportManager.editAirport(selectedIndex, updatedAirport);
                        updateDatabases();
                    } else {
                        System.out.println("Airport not found.");
                    }

                    break;
                case 4:
                    System.out.println("Enter airport name to delete:");
                    int deleteInputindex = getUserInputInt(); // Will be converted to index from input
                    Airport toDelete = airportManager.getAirports().get(deleteInputindex);
                    if (toDelete != null) {
                        airportManager.deleteAirport(toDelete);
                        updateDatabases();
                    } else {
                        System.out.println("Airport not found.");
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void manageAirplanes() {
        while (true) {
            System.out.println("1. List Airplanes");
            System.out.println("2. Add Airplane");
            System.out.println("3. Edit Airplane");
            System.out.println("4. Delete Airplane");
            System.out.println("5. Back to Main Menu");

            int choice = getUserInputInt();

            switch (choice) {
                case 1:
                    airplaneManager.displayAllAirplanes();
                    break;
                case 2:
                    System.out.println("Enter airplane model:");
                    String model = getUserInputString();
                    System.out.println("Enter airplane type:");
                    int type = getUserInputInt();
                    System.out.println("Enter tank size:");
                    double tankSize = getUserInputDouble();
                    System.out.println("Enter fuel burn rate:");
                    double fuelBurnRate = getUserInputDouble();
                    System.out.println("Enter airspeed:");
                    double airspeed = getUserInputDouble();
                    airplaneManager.addAirplane(new Airplane(airplaneManager.getAirplanes().size(), model, type,
                            tankSize, fuelBurnRate, airspeed));
                    updateDatabases();
                    break;
                case 3:
                    System.out.println("Enter the airplane's key to edit:");
                    int modelToEditIndex = getUserInputInt();
                    Airplane airplane = airplaneManager.getAirplanes().get(modelToEditIndex);
                    if (airplane != null) {
                        System.out.println("Enter new model name:");
                        String newModel = getUserInputString();
                        System.out.println("Enter new airplane type:");
                        int newType = getUserInputInt();
                        System.out.println("Enter new tank size:");
                        double newTankSize = getUserInputDouble();
                        System.out.println("Enter new fuel burn rate:");
                        double newFuelBurnRate = getUserInputDouble();
                        System.out.println("Enter new airspeed:");
                        double newAirspeed = getUserInputDouble();
                        airplaneManager.addAirplane(new Airplane(airplaneManager.getAirplanes().size(), newModel,
                                newType, newTankSize, newFuelBurnRate, newAirspeed));
                        updateDatabases();
                    } else {
                        System.out.println("Airplane not found.");
                    }
                    break;
                case 4:
                    System.out.println("Enter the airplane's key to delete:");
                    int modelToDeleteIndex = getUserInputInt();
                    Airplane toDelete = airplaneManager.getAirplanes().get(modelToDeleteIndex);
                    if (toDelete != null) {
                        airplaneManager.deleteAirplane(toDelete);
                        updateDatabases();
                    } else {
                        System.out.println("Airplane not found.");
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
