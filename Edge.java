import java.lang.Math;

/**
 * Represents an edge between two airports, with properties such as distance and heading.
 * This class calculates the distance and heading between two airports based on their
 * geographic coordinates.
 */
public class Edge {
    private Airport originNode;
    private Airport destinationNode;
    private double distance; // Distance in knots
    private double heading; // Heading in degrees
    private final double KNOT_CONVERSION = 60; // Conversion factor from degrees to nautical miles

    /**
     * Constructs an Edge object between two airports and calculates the distance and heading.
     *
     * @param origin The origin airport.
     * @param destination The destination airport.
     */
    public Edge(Airport origin, Airport destination) {
        this.originNode = origin;
        this.destinationNode = destination;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    /**
     * Calculates the great-circle distance between the two airports in nautical miles (knots).
     * 
     * @return The distance between the origin and destination airports in knots.
     */
    private double calculateDistance() {
        double lat1 = originNode.getLatitude();
        double lon1 = originNode.getLongitude();
        double lat2 = destinationNode.getLatitude();
        double lon2 = destinationNode.getLongitude();
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double degreeDistance = Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2));
        return degreeDistance * KNOT_CONVERSION; // Converts the degree distance to nautical miles
    }

    /**
     * Calculates the heading between the two airports in degrees.
     * 
     * @return The heading in degrees from the origin airport to the destination airport.
     */
    private double calculateHeading() {
        double lat1 = Math.toRadians(originNode.getLatitude());
        double lon1 = Math.toRadians(originNode.getLongitude());
        double lat2 = Math.toRadians(destinationNode.getLatitude());
        double lon2 = Math.toRadians(destinationNode.getLongitude());
    
        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360; // Normalizes the heading to a range of 0-360 degrees
    }

    /**
     * Gets the origin airport of this edge.
     * 
     * @return The origin airport.
     */
    public Airport getOriginNode() {
        return originNode;
    }

    /**
     * Sets the origin airport of this edge, recalculating the distance and heading.
     *
     * @param originNode The new origin airport.
     */
    public void setOriginNode(Airport originNode) {
        this.originNode = originNode;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    /**
     * Gets the destination airport of this edge.
     * 
     * @return The destination airport.
     */
    public Airport getDestinationNode() {
        return destinationNode;
    }

    /**
     * Sets the destination airport of this edge, recalculating the distance and heading.
     *
     * @param destinationNode The new destination airport.
     */
    public void setDestinationNode(Airport destinationNode) {
        this.destinationNode = destinationNode;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    /**
     * Gets the distance between the origin and destination airports.
     * 
     * @return The distance in nautical miles (knots).
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Gets the heading between the origin and destination airports.
     * 
     * @return The heading in degrees.
     */
    public double getHeading() {
        return heading;
    }
}
