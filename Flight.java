import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * The Flight class is responsible for planning and managing flight routes for an airplane.
 * It calculates routes between airports while considering factors such as fuel, airspeed, and the availability of refueling.
 */
public class Flight {
    Graph airportsGraph;
    private final String LINE_SEPARATOR = "--------------------------------";
    
    /**
     * Plans a flight route between a list of destination airports.
     *
     * @param destinationAirports A list of airports representing the flight's destination airports (Usually only 2).
     * @param selectedPlane The airplane selected for the flight.
     * @param airportList The list of all available airports for routing.
     */
    public void planFlight(ArrayList<Airport> destinationAirports, Airplane selectedPlane,
            ArrayList<Airport> airportList) {
        airportsGraph = new Graph(airportList);

        ArrayList<Edge> allFlightLegs = new ArrayList<>();

        // Process each leg of the flight route
        for (int i = 0; i < destinationAirports.size() - 1; i++) {
            ArrayList<Edge> currentLegs = findRoute(destinationAirports.get(i), destinationAirports.get(i + 1),
                    selectedPlane);

            // Return early if no route is found or destination airports are the same
            if (currentLegs == null) {
                return;
            }

            if (currentLegs.isEmpty()) {
                System.out.println("Flight plan not possible between " +
                        destinationAirports.get(i).getName() + " and " +
                        destinationAirports.get(i + 1).getName());
                return;
            }

            allFlightLegs.addAll(currentLegs);
        }

        // Output the flight plan
        System.out.println(displayFlightPlan(allFlightLegs, selectedPlane));
    }

    /**
     * Finds the optimal route between two airports derived from dijkstra's shortest path algorithm.
     * It was modified to fit the requirement of refueling takingthe airplanes fuel and airspeed into consideration. 
     * 
     * @param fromAirport The starting airport.
     * @param toAirport The destination airport.
     * @param selectedAirplane The airplane used for the flight.
     * @return A list of edges representing the optimal route between the two airports.
     */
    private ArrayList<Edge> findRoute(Airport fromAirport, Airport toAirport, Airplane selectedAirplane) {

        // If origin and destination are the same, no route is required
        if (fromAirport.equals(toAirport)) {
            System.out.println("Origin and destination are the same, routing is not needed.\nReturning to main menu:\n");
            return null;
        } else if(selectedAirplane.getAirspeed() <= 0){
            System.out.println("Airspeed is not greater than 0, route is not possible.\nReturning to main menu:\n");
            return null;
        } else if(selectedAirplane.getTankSize() <= 0){
            System.out.println("Tank Size is not greater than 0, route is not possible.\nReturning to main main menu:\n");
            return null;
        }

        ArrayList<Edge> route = new ArrayList<>();
        HashMap<Airport, Double> distanceMap = new HashMap<>();
        PriorityQueue<Airport> queue = new PriorityQueue<>(Comparator.comparingDouble(a -> distanceMap.getOrDefault(a, Double.MAX_VALUE)));
        HashMap<Airport, Edge> previousEdges = new HashMap<>();
        HashMap<Airport, Double> fuelRemaining = new HashMap<>();

        distanceMap.put(fromAirport, 0.0);
        fuelRemaining.put(fromAirport, selectedAirplane.getTankSize());
        queue.add(fromAirport);

        System.out.println("Starting route search from " + fromAirport.getName() + " to " + toAirport.getName());
        System.out.println("Selected airplane: " + selectedAirplane.displayInfo() + "\n");

        // Dijkstra's algorithm 
        while (!queue.isEmpty()) {
            Airport current = queue.poll();

            // Stop once the destination airport is reached
            if (current.equals(toAirport)) {
                break;
            }

            // Process all neighboring airports connected by edges
            for (Edge edge : airportsGraph.getListings().getOrDefault(current, new ArrayList<>())) {
                Airport nextAirport = edge.getDestinationNode();
                double legDistance = edge.getDistance();

                double airspeed = selectedAirplane.getAirspeed(); // in knots
                double fuelBurnRate = selectedAirplane.getFuelBurnRate(); // in gallons/hour

                double flightTimeHours = legDistance / airspeed;
                double fuelRequired = flightTimeHours * fuelBurnRate;

                double currentFuel = fuelRemaining.getOrDefault(current, 0.0);

                String requiredFuel = (selectedAirplane.getType() == 3) ? "AVGAS" : "JA-a";
                boolean canRefuel = Arrays.asList(nextAirport.getFuelTypes()).contains(requiredFuel);

                double usableFuel = currentFuel;

                // If fuel is insufficient, check if refueling is possible
                if (usableFuel < fuelRequired) {
                    if (!canRefuel) {
                        continue;
                    }
                    usableFuel = selectedAirplane.getTankSize();
                    if (usableFuel < fuelRequired) {
                        continue;
                    }
                }

                double remainingFuelAfterLeg = usableFuel - fuelRequired;
                double newFuelLevel = canRefuel ? selectedAirplane.getTankSize() : remainingFuelAfterLeg;
                double newDistance = distanceMap.get(current) + legDistance;

                // Update distances and fuel levels for the next airport
                if (newDistance < distanceMap.getOrDefault(nextAirport, Double.MAX_VALUE)) {
                    distanceMap.put(nextAirport, newDistance);
                    fuelRemaining.put(nextAirport, newFuelLevel);
                    previousEdges.put(nextAirport, edge);
                    queue.add(nextAirport);
                }
            }
        }

        // Reconstruct the route from the destination back to the origin
        Airport step = toAirport;
        while (previousEdges.containsKey(step)) {
            Edge edge = previousEdges.get(step);
            route.add(0, edge);
            step = edge.getOriginNode();
        }

        if (route.isEmpty()) {
            System.out.println("No route found.");
        } else {
            System.out.println("Route successfully found.");
        }

        return route;
    }

    /**
     * Calculates the flight time for a given leg of the route.
     *
     * @param leg The edge representing a flight leg.
     * @param selectedAirplane The airplane used for the flight.
     * @return The flight time in hours.
     */
    private double calculateLegFlightTime(Edge leg, Airplane selectedAirplane) {
        double distanceInKnots = leg.getDistance();
        double airspeedInKnots = selectedAirplane.getAirspeed();
        double timeInHours = distanceInKnots / airspeedInKnots;
        return timeInHours;
    }

    /**
     * Displays the flight plan, showing all the flight legs with distances, headings, and times.
     *
     * @param flightLegs The list of flight legs.
     * @param selectedAirplane The airplane selected for the flight.
     * @return A formatted string representing the flight plan.
     */
    private String displayFlightPlan(ArrayList<Edge> flightLegs, Airplane selectedAirplane) {
        StringBuilder flightPlan = new StringBuilder();
        flightPlan.append("Flight Plan:\n\n");

        // Display details for each flight leg
        for (int i = 0; i < flightLegs.size(); i++) {
            Edge leg = flightLegs.get(i);
            String from = leg.getOriginNode().getName();
            String to = leg.getDestinationNode().getName();
            double distance = leg.getDistance();
            double heading = leg.getHeading();
            double time = calculateLegFlightTime(leg, selectedAirplane);

            flightPlan.append((i + 1) + ". " + from + " to " + to + "\n" +
                    "   Distance: " + String.format("%.2f Nautical Miles", distance) + "\n" +
                    "   Heading: " + String.format("%.1f°", heading) + "\n" +
                    "   Time Taken: " + String.format("%.2f Hours", time) + "\n" + LINE_SEPARATOR + "\n");
        }
        return flightPlan.toString();
    } // End of method displayFlightPlan
}