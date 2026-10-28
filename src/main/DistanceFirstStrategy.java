package main;

import java.util.List;

public class DistanceFirstStrategy extends AbstractRouteStrategy {
    @Override public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
        Route best = validRoutes(routes, request).stream()
                .min((a, b) -> Double.compare(a.distanceKm(), b.distanceKm())).orElseThrow();
        return decision(best, "minimum distance " + best.distanceKm() + " km", best.distanceKm());
    }
    @Override public String getStrategyName() { return "Distance-first"; }
}
