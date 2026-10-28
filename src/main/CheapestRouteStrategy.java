package main;

import java.util.List;

public class CheapestRouteStrategy extends AbstractRouteStrategy {
    @Override public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
        List<Route> valid = validRoutes(routes, request);
        Route best = valid.stream().min((a, b) -> Double.compare(totalCost(a), totalCost(b))).orElseThrow();
        return decision(best, "total cost = base cost + distance cost (shown cost + 20 per km)", totalCost(best));
    }
    private double totalCost(Route r) { return r.cost() + r.distanceKm() * 20.0; }
    @Override public String getStrategyName() { return "Cheapest"; }
}
