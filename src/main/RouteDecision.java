package main;

public record RouteDecision(Route route, String strategyName, String explanation, double score) {
    @Override public String toString() {
        return strategyName + " selected " + route.id() + " | " + explanation;
    }
}
