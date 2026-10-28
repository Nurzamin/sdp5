package main;

import java.util.List;

public class BalancedRouteStrategy extends AbstractRouteStrategy {
    private final double timeWeight, costWeight, distanceWeight, emissionWeight;

    public BalancedRouteStrategy(double timeWeight, double costWeight, double distanceWeight, double emissionWeight) {
        if (timeWeight < 0 || costWeight < 0 || distanceWeight < 0 || emissionWeight < 0
                || Math.abs(timeWeight + costWeight + distanceWeight + emissionWeight - 1.0) > 0.000001)
            throw new IllegalArgumentException("Weights must be non-negative and sum to 1.0");
        this.timeWeight = timeWeight; this.costWeight = costWeight;
        this.distanceWeight = distanceWeight; this.emissionWeight = emissionWeight;
    }

    @Override public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
        List<Route> valid = validRoutes(routes, request);
        double maxTime = valid.stream().mapToDouble(r -> r.estimatedMinutes() + r.trafficPenaltyMinutes()).max().orElse(1);
        double maxCost = valid.stream().mapToDouble(Route::cost).max().orElse(1);
        double maxDistance = valid.stream().mapToDouble(Route::distanceKm).max().orElse(1);
        double maxEmission = valid.stream().mapToDouble(Route::carbonEmission).max().orElse(1);
        Route best = valid.stream().min((a, b) -> Double.compare(score(a, maxTime, maxCost, maxDistance, maxEmission),
                score(b, maxTime, maxCost, maxDistance, maxEmission))).orElseThrow();
        double score = score(best, maxTime, maxCost, maxDistance, maxEmission);
        return decision(best, String.format("balanced normalized score %.3f", score), score);
    }

    private double score(Route r, double mt, double mc, double md, double me) {
        return ((r.estimatedMinutes() + r.trafficPenaltyMinutes()) / mt) * timeWeight
                + (r.cost() / mc) * costWeight + (r.distanceKm() / md) * distanceWeight
                + (r.carbonEmission() / me) * emissionWeight;
    }
    @Override public String getStrategyName() { return "Balanced"; }
}
