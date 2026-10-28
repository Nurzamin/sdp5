package main;

public class StrategySelector {
    public RouteStrategy select(DeliveryRequest request) {
        if (request == null) throw new IllegalArgumentException("Delivery request is required");
        return switch (request.priority()) {
            case URGENT -> new FastestRouteStrategy();
            case ECONOMY -> new CheapestRouteStrategy();
            case ECO -> new EcoFriendlyRouteStrategy();
            case STANDARD -> new BalancedRouteStrategy(0.40, 0.30, 0.10, 0.20);
        };
    }
}
