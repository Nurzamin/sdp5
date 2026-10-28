package main;

import java.util.List;

public abstract class AbstractRouteStrategy implements RouteStrategy {
    protected List<Route> validRoutes(List<Route> routes, DeliveryRequest request) {
        if (routes == null || routes.isEmpty())
            throw new NoSuitableRouteException("Route list is empty.");
        List<Route> valid = routes.stream()
                .filter(Route::available)
                .filter(r -> r.cost() <= request.maxBudget())
                .filter(r -> r.estimatedMinutes() + r.trafficPenaltyMinutes() <= request.maxDeliveryMinutes())
                .toList();
        if (valid.isEmpty())
            throw new NoSuitableRouteException("No route satisfies availability, budget and deadline constraints.");
        return valid;
    }

    protected RouteDecision decision(Route route, String explanation, double score) {
        return new RouteDecision(route, getStrategyName(), explanation, score);
    }
}
