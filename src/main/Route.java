package main;

public record Route(String id, Location origin, Location destination, double distanceKm,
                    int estimatedMinutes, double cost, double carbonEmission,
                    int trafficPenaltyMinutes, boolean available) {
    public Route {
        if (id == null || id.isBlank() || origin == null || destination == null)
            throw new IllegalArgumentException("Route data is required");
        if (distanceKm < 0 || estimatedMinutes < 0 || cost < 0 || carbonEmission < 0 || trafficPenaltyMinutes < 0)
            throw new IllegalArgumentException("Route values cannot be negative");
    }
}
