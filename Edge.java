import java.lang.Math;

public class Edge {
    private Airport originNode;
    private Airport destinationNode;
    private double distance; // Knots
    private double heading;
    private final double KNOT_CONVERSION = 60;

    public Edge(Airport origin, Airport destination) {
        this.originNode = origin;
        this.destinationNode = destination;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    private double calculateDistance() {
        double lat1 = originNode.getLatitude();
        double lon1 = originNode.getLongitude();
        double lat2 = destinationNode.getLatitude();
        double lon2 = destinationNode.getLongitude();
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double degreeDistance = Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2));
        return degreeDistance * KNOT_CONVERSION; // Fixed to convert to distance in knots
    }

    private double calculateHeading() {
        double lat1 = Math.toRadians(originNode.getLatitude());
        double lon1 = Math.toRadians(originNode.getLongitude());
        double lat2 = Math.toRadians(destinationNode.getLatitude());
        double lon2 = Math.toRadians(destinationNode.getLongitude());
    
        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360; // Fixed to account for negative angle cases
                                                               // (normalization)
    }

    public Airport getOriginNode() {
        return originNode;
    }

    public void setOriginNode(Airport originNode) {
        this.originNode = originNode;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    public Airport getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(Airport destinationNode) {
        this.destinationNode = destinationNode;
        this.distance = calculateDistance();
        this.heading = calculateHeading();
    }

    public double getDistance() {
        return distance;
    }

    public double getHeading() {
        return heading;
    }
}
