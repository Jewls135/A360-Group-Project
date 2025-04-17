import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * FlightManagementSystem provides a console-based interface for managing
 * airports,
 * airplanes, and planning flight routes between multiple airports.
 */
class FlightManagementSystem {
    Flight flight;
    AirportManager airportManager;
    AirplaneManager airplaneManager;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Entry point for the Flight Management System application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        boolean quit = false;
        while (!quit) {
            FlightManagementSystem fms = new FlightManagementSystem();
            try {
                quit = fms.displayOptions();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Reads non-empty string input from the user via the console.
     * 
     * @return A valid, non-empty user input string.
     */
    private String getUserInputString() {
        String input = "";
        while (true) {
            try {
                System.out.print("\nInput: ");
                input = scanner.nextLine();
                System.out.print("\n");
                if (input.trim().length() <= 0) {
                    System.out.println("Zero-length character inputs are not allowed, please try again.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        }
        return input;
    }

    /**
     * Reads a valid integer input from the user.
     * 
     * @return A valid integer.
     */
    private int getUserInputInt() {
        int input = -1;

        while (true) {
            try {
                System.out.print("\nInput: ");
                String line = scanner.nextLine();
                System.out.print("\n");
                if (line.trim().length() <= 0) {
                    System.out.println("Zero-length character inputs are not allowed, please try again.");
                    continue;
                }
                input = Integer.parseInt(line);
                break;
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Invalid input, please enter a valid Integer. (Whole number)");
                } else {
                    System.out.println("Error, please try again: " + e.getMessage());
                }
            }
        }

        return input;
    }

    /**
     * Reads a valid double input from the user.
     * 
     * @return A valid double.
     */
    private double getUserInputDouble() {
        double input;

        while (true) {
            try {
                System.out.print("\nInput: ");
                String line = scanner.nextLine();
                System.out.print("\n");
                if (line.trim().length() <= 0) {
                    System.out.println("Zero-length character inputs are not allowed, please try again.");
                    continue;
                }
                input = Double.parseDouble(line);
                break;
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Invalid input, please enter a valid number.");
                } else {
                    System.out.println("Error, please try again: " + e.getMessage());
                }
            }
        }

        return input;
    }

    /**
     * Displays the main menu and handles user navigation through options.
     * 
     * @return A boolean value to quit using the software.
     */
    private boolean displayOptions() {
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
                    System.out.println("Thank you for using this software.");
                    return true;
                default:
                    System.out.println("Invalid choice, please enter a valid option. \n");
            }
        }
    }

    /**
     * Initializes or updates the airplane and airport databases.
     */
    private void updateDatabases() {
        airportManager = new AirportManager();
        airplaneManager = new AirplaneManager();
    }

    /**
     * Handles user selection of an airplane from the available list.
     * 
     * @return The selected Airplane object, or null if invalid.
     */
    private Airplane handleAirplaneChoice() {
        ArrayList<Airplane> airplanes = airplaneManager.getAirplanes();
        if (airplanes.isEmpty()) {
            System.out.println("No airplanes available, please add them");
            return null;
        }

        System.out.println("Available Airplanes:");
        airplaneManager.displayAllAirplanes();

        System.out.println("\nEnter airplane key (0 to " + (airplanes.size() - 1) + "):");
        int airplaneIndex = getUserInputInt();

        if (airplaneIndex < 0 || airplaneIndex >= airplanes.size()) {
            System.out.println("Invalid key, please try again.");
            return null;
        }

        return airplanes.get(airplaneIndex);
    }

    /**
     * Handles the user selection of a start airport and one or more destination
     * airports.
     * 
     * @return A list of airports representing the flight path.
     */
    private ArrayList<Airport> handleAirportChoice() {
        System.out.println("Available Airports:");
        airportManager.displayAllAirports();
        Airport startAirport;
        while (true) {
            System.out.println("\nEnter starting airport (ICAO or part of name):");
            String startInput = getUserInputString();
            ArrayList<Airport> matchingStartAirports = findMatchingAirports(startInput);

            if (matchingStartAirports.isEmpty()) {
                System.out.println("No matching airports found.");
                continue;
            }

            if (matchingStartAirports.size() > 1) {
                System.out.println("Multiple airports found. Please choose one:");
                for (int i = 0; i < matchingStartAirports.size(); i++) {
                    System.out.println((i + 1) + ". " + matchingStartAirports.get(i).displayInfo());
                }

                int startInputIndex = getUserInputInt();
                if (startInputIndex < 1 || startInputIndex > matchingStartAirports.size()) {
                    System.out.println("Invalid selection. Aborting.");
                    continue;
                }

                startAirport = matchingStartAirports.get(startInputIndex - 1);
            } else {
                startAirport = matchingStartAirports.get(0);
            }
            break;
        }

        ArrayList<Airport> destinations = new ArrayList<>();
        destinations.add(startAirport);

        while (destinations.size() < 2) {
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

    /**
     * Finds airports that match the user's input either exactly or partially.
     * Exact matches are returned first, followed by partial matches.
     *
     * @param input The user's search query for airport name or ICAO code.
     * @return A list of matching Airport objects.
     */
    private ArrayList<Airport> findMatchingAirports(String input) {
        ArrayList<Airport> exactMatches = new ArrayList<>();
        ArrayList<Airport> partialMatches = new ArrayList<>();
        String search = input.toLowerCase();

        for (Airport airport : airportManager.getAirports()) {
            String identifier = airport.getIdentifier().toLowerCase();
            String name = airport.getName().toLowerCase();

            if (identifier.equals(search) || name.equals(search)) {
                exactMatches.add(airport);
            } else if (identifier.contains(search) || name.contains(search)) {
                partialMatches.add(airport);
            }
        }

        // Return exact matches first, then partial matches (they wont overlap b/c
        // arraylist)
        exactMatches.addAll(partialMatches);
        return exactMatches;
    }

    /**
     * Displays the airport management menu and handles user input
     * for listing, adding, editing, and deleting airports.
     * Loops until the user chooses to return to the main menu.
     */
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
                    System.out.println("\n");
                    break;
                case 2:
                    // Handles input for creating a new airport and adds it to the system
                    System.out.println("Enter new airport name:");
                    String name = getUserInputString();
                    String icao;
                    while (true) {
                        System.out.println("Enter ICAO code (4 Characters maximum):");
                        icao = getUserInputString();
                        if (icao.length() > 4) {
                            System.out.println("Invalid input, only 4 characters or less are allowed\n");
                            continue;
                        }

                        if (icao.matches(".*\\d.*")) {
                            System.out.println("Invalid input, no digits allowed\n");
                            continue;
                        }
                        break;
                    }
                    double latitude;
                    while (true) {
                        System.out.println("Enter latitude (-90 to 90):");
                        latitude = getUserInputDouble();
                        if (latitude >= -90 && latitude <= 90) {
                            break;
                        }
                        System.out.println("Invalid input. Latitude must be between -90 and 90.");
                    }
                    latitude = Math.max(-90, Math.min(latitude, 90)); // Still clamp just in case

                    double longitude;
                    while (true) {
                        System.out.println("Enter longitude (-180 to 180):");
                        longitude = getUserInputDouble();
                        if (longitude >= -180 && longitude <= 180) {
                            break;
                        }
                        System.out.println("Invalid input. Longitude must be between -180 and 180.");
                    }
                    longitude = Math.max(-180, Math.min(longitude, 180)); // Still clamp just in case

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

                    Set<String> validFuelTypes = new HashSet<>(Arrays.asList("avgas", "ja-a"));
                    String[] fuelTypes;

                    while (true) {
                        System.out.println("Enter fuel types, Options: AVGAS, JA-a (Comma-separated if multiple)");
                        String[] inputFuels = getUserInputString().split(",");
                        Set<String> fuelTypesSet = new HashSet<>();

                        boolean allValid = true;
                        for (String fuel : inputFuels) {
                            String trimmed = fuel.trim().toLowerCase();
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
                            System.out.println(
                                    "Please enter one or two valid fuel types, Options: AVGAS, JA-a (Comma-separated if multiple).");
                        } else {
                            fuelTypes = fuelTypesSet.toArray(new String[fuelTypesSet.size()]);
                            break;
                        }
                    }

                    airportManager.addAirport(new Airport(icao, name, latitude, longitude, frequencies, fuelTypes));
                    updateDatabases();
                    break;
                case 3:
                    // Handles input and editing of an existing airport
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
                        String newIdentifier;
                        while (true) {
                            System.out.println("Enter ICAO code (4 Characters maximum):");
                            newIdentifier = getUserInputString();
                            if (newIdentifier.length() > 4) {
                                System.out.println("Invalid input, only 4 characters or less are allowed\n");
                                continue;
                            }

                            if (newIdentifier.matches(".*\\d.*")) {
                                System.out.println("Invalid input, no digits allowed\n");
                                continue;
                            }
                            break;
                        }
                        double newLatitude;
                        while (true) {
                            System.out.println("Enter latitude (-90 to 90):");
                            newLatitude = getUserInputDouble();
                            if (newLatitude >= -90 && newLatitude <= 90) {
                                break;
                            }
                            System.out.println("Invalid input. Latitude must be between -90 and 90.");
                        }
                        newLatitude = Math.max(-90, Math.min(newLatitude, 90));

                        double newLongitude;
                        while (true) {
                            System.out.println("Enter longitude (-180 to 180):");
                            newLongitude = getUserInputDouble();
                            if (newLongitude >= -180 && newLongitude <= 180) {
                                break;
                            }
                            System.out.println("Invalid input. Longitude must be between -180 and 180.");
                        }
                        newLongitude = Math.max(-180, Math.min(newLongitude, 180));

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

                        Set<String> validFuelType = new HashSet<>(Arrays.asList("avgas", "ja-a"));
                        String[] newFuelTypes;

                        while (true) {
                            System.out.println("Enter fuel types, Options: AVGAS, JA-a (Comma-separated if multiple)");
                            String[] inputFuels = getUserInputString().split(",");
                            Set<String> fuelTypesSet = new HashSet<>();

                            boolean allValid = true;
                            for (String fuel : inputFuels) {
                                String trimmed = fuel.trim().toLowerCase();
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
                                System.out.println(
                                        "Please enter one or two valid fuel types, Options: AVGAS, JA-a (Comma-separated if multiple).");
                            } else {
                                newFuelTypes = fuelTypesSet.toArray(new String[fuelTypesSet.size()]);
                                break;
                            }
                        }

                        Airport updatedAirport = new Airport(newIdentifier, newName, newLatitude, newLongitude,
                                newFrequencies, newFuelTypes);

                        airportManager.editAirport(airport, updatedAirport);
                        updateDatabases();
                    } else {
                        System.out.println("Airport not found.");
                    }

                    break;
                case 4:
                    // Handles deletion of airports based on user input
                    while (true) {
                        airportManager.displayAllAirports();

                        System.out
                                .println("\nEnter airport to delete (ICAO or part of name), or type 'done' to cancel:");
                        String deleteInput = getUserInputString();

                        if (deleteInput.equalsIgnoreCase("done")) {
                            System.out.println("Deletion cancelled.");
                            break;
                        }

                        ArrayList<Airport> matchingAirports = findMatchingAirports(deleteInput);

                        if (matchingAirports.isEmpty()) {
                            System.out.println("No matching airports found. Try again.");
                            continue;
                        }

                        Airport toDelete;
                        if (matchingAirports.size() > 1) {
                            System.out.println("Multiple airports found. Please choose one:");
                            for (int i = 0; i < matchingAirports.size(); i++) {
                                System.out.println((i + 1) + ". " + matchingAirports.get(i).displayInfo());
                            }

                            int deleteInputIndex = getUserInputInt();
                            if (deleteInputIndex < 1 || deleteInputIndex > matchingAirports.size()) {
                                System.out.println("Invalid selection. Try again.");
                                continue;
                            }

                            toDelete = matchingAirports.get(deleteInputIndex - 1);
                        } else {
                            toDelete = matchingAirports.get(0);
                        }

                        airportManager.deleteAirport(toDelete);
                        updateDatabases();
                        System.out.println("Airport deleted successfully.");
                        break;
                    }
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please enter a valid option.\n");
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
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println("Enter airplane make/model:");
                    String model = getUserInputString();

                    int type;
                    System.out.println("Enter airplane type:\n\n1. Jet\n2. Turboprop\n3. Prop plane");
                    while (true) {
                        type = getUserInputInt();
                        if (type > 3 || type <= 0) {
                            System.out.println("Invalid input, please enter a type 1-3 as listed previously.\n");
                            continue;
                        }
                        break;
                    }
                    double tankSize;

                    System.out.println("Enter tank size (Gallons):");
                    while (true) {
                        tankSize = getUserInputDouble();
                        if (tankSize > 0) {
                            break;
                        } else {
                            System.out.println("Invalid input. Tank size must be greater than 0.");
                        }
                    }

                    System.out.println("Enter fuel burn rate (Gallons per hour):");
                    double fuelBurnRate = getUserInputDouble();

                    double airspeed;

                    System.out.println("Enter airspeed: (In Knots)");
                    while (true) {
                        airspeed = getUserInputDouble();
                        if (airspeed > 0) {
                            break;
                        } else {
                            System.out.println("Invalid input, Airspeed must be greater than 0.");
                        }
                    }

                    airplaneManager.addAirplane(new Airplane(airplaneManager.getAirplanes().size(), model, type,
                            tankSize, fuelBurnRate, airspeed));
                    updateDatabases();
                    break;
                case 3:
                    airplaneManager.displayAllAirplanes();
                    String yesOrNoInput;
                    int modelToEditIndex;
                    Airplane airplane;
                    System.out.println("\nEnter the airplane's key to edit:");
                    while (true) {
                        modelToEditIndex = getUserInputInt();
                        if (modelToEditIndex < 0 || modelToEditIndex >= airplaneManager.getAirplanes().size()) {
                            System.out.println("Key does not exist, please try again.");
                            continue;
                        }

                        airplane = airplaneManager.getAirplanes().get(modelToEditIndex);
                        if (airplane != null) {
                            break;
                        } else {
                            System.out.println("Key does not exist, please try again.");
                        }
                    }

                    if (airplane != null) {
                        String newModel = airplane.getMakeAndModel();
                        int newType = airplane.getType();
                        double newTankSize = airplane.getTankSize();
                        double newFuelBurnRate = airplane.getFuelBurnRate();
                        double newAirspeed = airplane.getAirspeed();

                        // Model
                        while (true) {
                            System.out.println("Would you like to edit the make/model? [Y/N]");
                            yesOrNoInput = getUserInputString().trim().toUpperCase();
                            if (yesOrNoInput.equals("Y")) {
                                System.out.println("Enter new make/model:");
                                newModel = getUserInputString();
                                break;
                            } else if (yesOrNoInput.equals("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter Y or N.");
                            }
                        }

                        // Type
                        while (true) {
                            System.out.println("Would you like to edit the airplane type? [Y/N]");
                            yesOrNoInput = getUserInputString().trim().toUpperCase();
                            if (yesOrNoInput.equals("Y")) {
                                System.out.println("Enter new airplane type:\n1. Jet\n2. Turboprop\n3. Prop plane");
                                while (true) {
                                    newType = getUserInputInt();
                                    if (newType >= 1 && newType <= 3) {
                                        break;
                                    }
                                    System.out
                                            .println("Invalid input, please enter a type 1-3 as listed previously.\n");
                                }
                                break;
                            } else if (yesOrNoInput.equals("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter Y or N.");
                            }
                        }

                        // Tank size
                        while (true) {
                            System.out.println("Would you like to edit the tank size? [Y/N]");
                            yesOrNoInput = getUserInputString().trim().toUpperCase();
                            if (yesOrNoInput.equals("Y")) {
                                System.out.println("Enter tank size (Gallons):");
                                while (true) {
                                    newTankSize = getUserInputDouble();
                                    if (newTankSize > 0) {
                                        break;
                                    } else {
                                        System.out.println("Invalid input. Tank size must be greater than 0");
                                    }
                                }
                                break;
                            } else if (yesOrNoInput.equals("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter Y or N.");
                            }
                        }

                        // Fuel burn rate
                        while (true) {
                            System.out.println("Would you like to edit the fuel burn rate? [Y/N]");
                            yesOrNoInput = getUserInputString().trim().toUpperCase();
                            if (yesOrNoInput.equals("Y")) {
                                System.out.println("Enter new fuel burn rate (Gallons per hour):");
                                newFuelBurnRate = getUserInputDouble();
                                break;
                            } else if (yesOrNoInput.equals("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter Y or N.");
                            }
                        }

                        // Airspeed
                        while (true) {
                            System.out.println("Would you like to edit the airspeed? [Y/N]");
                            yesOrNoInput = getUserInputString().trim().toUpperCase();
                            if (yesOrNoInput.equals("Y")) {
                                System.out.println("Enter airspeed: (In Knots)");
                                while (true) {
                                    newAirspeed = getUserInputDouble();
                                    if (newAirspeed > 0) {
                                        break;
                                    } else {
                                        System.out.println("Invalid input, Airspeed must be greater than 0.");
                                    }
                                }
                                break;
                            } else if (yesOrNoInput.equals("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter Y or N.");
                            }
                        }

                        // Update airplane
                        airplaneManager.editAirplane(modelToEditIndex, new Airplane(modelToEditIndex, newModel,
                                newType, newTankSize, newFuelBurnRate, newAirspeed));
                        updateDatabases();
                    } else {
                        System.out.println("Airplane not found.");
                    }
                    break;
                case 4:
                    airplaneManager.displayAllAirplanes();
                    System.out.println("\nEnter the airplane's key to delete:");
                    while (true) {
                        int modelToDeleteIndex = getUserInputInt();

                        if (modelToDeleteIndex < 0 || modelToDeleteIndex >= airplaneManager.getAirplanes().size()) {
                            System.out.println("Key does not exist, please try again.");
                            continue;
                        }

                        Airplane toDelete = airplaneManager.getAirplanes().get(modelToDeleteIndex);
                        if (toDelete != null) {
                            airplaneManager.deleteAirplane(toDelete);
                            updateDatabases();
                            break;
                        } else {
                            System.out.println("Key does not exist, please try again.");
                        }
                    }

                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please enter a valid option.\n");
            }
        }
    }
}
