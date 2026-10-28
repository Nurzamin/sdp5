package main;

import java.util.List;

public class EcoFriendlyRouteStrategy extends AbstractRouteStrategy {
    @Override public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
        List<Route> valid = validRoutes(routes, request);
        Route best = valid.stream().min((a, b) -> Double.compare(ecoScore(a), ecoScore(b))).orElseThrow();
        return decision(best, "eco score = emissions * 0.7 + distance * 0.3", ecoScore(best));
    }
    private double ecoScore(Route r) { return r.carbonEmission() * 0.7 + r.distanceKm() * 0.3; }
    @Override public String getStrategyName() { return "Eco-friendly"; }
}
