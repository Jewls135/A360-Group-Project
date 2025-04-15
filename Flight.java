import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Flight {
    Graph airportsGraph;
    private final String LINE_SEPARATOR = "--------------------------------";
    private final double KNOT_CONVERSION = 1.852;

    public void planFlight(ArrayList<Airport> destinationAirports, Airplane selectedPlane,
            ArrayList<Airport> airportList) {
        airportsGraph = new Graph(airportList);

        ArrayList<Edge> allFlightLegs = new ArrayList<>();

        for (int i = 0; i < destinationAirports.size() - 1; i++) {
            ArrayList<Edge> currentLegs = findRoute(destinationAirports.get(i), destinationAirports.get(i + 1),
                    selectedPlane);

            if (currentLegs.isEmpty()) {
                System.out.println("Flight plan not possible between " +
                        destinationAirports.get(i).getName() + " and " +
                        destinationAirports.get(i + 1).getName());
                return;
            }

            allFlightLegs.addAll(currentLegs);
        }

        System.out.println(displayFlightPlan(allFlightLegs, selectedPlane));
    }

    private ArrayList<Edge> findRoute(Airport fromAirport, Airport toAirport, Airplane selectedAirplane) {
        ArrayList<Edge> route = new ArrayList<>();
        HashMap<Airport, Double> distanceMap = new HashMap<>();
        PriorityQueue<Airport> queue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> distanceMap.getOrDefault(a, Double.MAX_VALUE)));
        HashMap<Airport, Edge> previousEdges = new HashMap<>();
        HashMap<Airport, Double> fuelRemaining = new HashMap<>();

        distanceMap.put(fromAirport, 0.0);
        fuelRemaining.put(fromAirport, selectedAirplane.getTankSize()); // Start with full tank
        queue.add(fromAirport);

        System.out.println("Starting route search from " + fromAirport.getName() + " to " + toAirport.getName());
        System.out.println("Selected airplane: " + selectedAirplane.getMakeAndModel() + ", Tank size: "
                + selectedAirplane.getTankSize() + ", Fuel burn rate: " + selectedAirplane.getFuelBurnRate());

        while (!queue.isEmpty()) {
            Airport current = queue.poll();
            System.out.println("\nProcessing airport: " + current.getName());

            if (current.equals(toAirport)) {
                System.out.println("Destination airport reached: " + current.getName());
                break;
            }

            for (Edge edge : airportsGraph.getListings().getOrDefault(current, new ArrayList<>())) {
                Airport nextAirport = edge.getDestinationNode();
                double legDistance = edge.getDistance() * 1.852;

                double airspeed = selectedAirplane.getAirspeed(); // assumed in km/h
                double fuelBurnRate = selectedAirplane.getFuelBurnRate(); // in gallons/hour

                double flightTimeHours = legDistance / airspeed;
                double fuelRequired = flightTimeHours * fuelBurnRate;

                double currentFuel = fuelRemaining.get(current);

                System.out.println("  Checking route to: " + nextAirport.getName());
                System.out.println("    Distance: " + legDistance);
                System.out.println("    Fuel required: " + fuelRequired);
                System.out.println("    Fuel remaining: " + currentFuel);

                String requiredFuel = (selectedAirplane.getType() == 3) ? "AVGAS" : "JA-a";
                boolean canRefuel = Arrays.asList(nextAirport.getFuelTypes()).contains(requiredFuel);
                System.out.println("    Can refuel at destination? " + canRefuel);

                if (currentFuel < fuelRequired) {
                    System.out.println("    Not enough fuel for this leg.");
                    if (!canRefuel) {
                        System.out.println("    Cannot refuel at " + nextAirport.getName() + ". Skipping this route.");
                        continue;
                    }
                    System.out.println("    Refueling before leg.");
                    currentFuel = selectedAirplane.getTankSize(); // Refuel
                    if (currentFuel < fuelRequired) {
                        System.out.println("    Even after refueling, fuel is insufficient. Skipping.");
                        try{
                            
                        Thread.sleep(100000);
                        } catch(Exception e){

                        }
                        continue;
                    }
                }

                double remainingFuelAfterLeg = currentFuel - fuelRequired;
                double nextFuelLevel = canRefuel ? selectedAirplane.getTankSize() : remainingFuelAfterLeg;
                double newDistance = distanceMap.get(current) + legDistance;

                System.out.println("    New calculated distance to " + nextAirport.getName() + ": " + newDistance);
                System.out.println("    Fuel left after leg: " + nextFuelLevel);

                if (newDistance < distanceMap.getOrDefault(nextAirport, Double.MAX_VALUE)) {
                    System.out.println("    Updating route to " + nextAirport.getName());
                    distanceMap.put(nextAirport, newDistance);
                    fuelRemaining.put(nextAirport, nextFuelLevel);
                    previousEdges.put(nextAirport, edge);
                    queue.add(nextAirport);
                } else {
                    System.out.println("    Existing route to " + nextAirport.getName() + " is shorter. Skipping.");
                }
            }
        }

        System.out.println("\nReconstructing path...");
        Airport step = toAirport;
        while (previousEdges.containsKey(step)) {
            Edge edge = previousEdges.get(step);
            route.add(0, edge);
            System.out.println(
                    "  Step back: " + edge.getOriginNode().getName() + " -> " + edge.getDestinationNode().getName());
            step = edge.getOriginNode();
        }

        if (route.isEmpty()) {
            System.out.println("No route found.");
        } else {
            System.out.println("Route successfully found.");
        }

        return route;
    }

    private double calculateLegFlightTime(Edge leg, Airplane selectedAirplane) {
        double distanceInKm = leg.getDistance();
        double airspeed = selectedAirplane.getAirspeed();
        double timeInHours = (distanceInKm / KNOT_CONVERSION) / airspeed;
        return timeInHours;

    } // End of method calculateLegFlightTime

    private String displayFlightPlan(ArrayList<Edge> flightLegs, Airplane selectedAirplane) {
        StringBuilder flightPlan = new StringBuilder();
        flightPlan.append("Flight Plan:\n\n");

        for (int i = 0; i < flightLegs.size(); i++) {
            Edge leg = flightLegs.get(i);
            String from = leg.getOriginNode().getName();
            String to = leg.getDestinationNode().getName();
            double distance = leg.getDistance();
            double heading = leg.getHeading();
            double time = calculateLegFlightTime(leg, selectedAirplane);

            flightPlan.append((i + 1) + ". " + from + " to " + to + "\n" +
                    "   Distance: " + String.format("%.2f km", distance) + "\n" +
                    "   Heading: " + String.format("%.1f°", heading) + "\n" +
                    "   Time Taken: " + String.format("%.2f hours", time) + "\n" + LINE_SEPARATOR + "\n");
        }
        return flightPlan.toString();
    } // End of method displayFlightPlan
}
