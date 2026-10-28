package main;

public record Location(String name, double latitude, double longitude) {
    public Location {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Location name is required");
    }
}
