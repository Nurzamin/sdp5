package main;

import java.util.List;

public class RoutePlanner {
    private RouteStrategy strategy;
    public RoutePlanner(RouteStrategy strategy) { setStrategy(strategy); }
    public void setStrategy(RouteStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("Strategy is required");
        this.strategy = strategy;
    }
    public RouteDecision plan(List<Route> routes, DeliveryRequest request) {
        if (request == null) throw new IllegalArgumentException("Delivery request is required");
        return strategy.selectRoute(routes, request);
    }
}
