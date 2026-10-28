package main;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);
        List<Route> routes = List.of(
            new Route("R1", warehouse, customer, 12, 25, 3000, 2.8, 4, true),
            new Route("R2", warehouse, customer, 18, 20, 4200, 4.1, 2, true),
            new Route("R3", warehouse, customer, 9, 32, 2600, 3.0, 3, true),
            new Route("R4", warehouse, customer, 15, 29, 3300, 1.2, 1, true),
            new Route("R5", warehouse, customer, 22, 40, 2200, 5.0, 5, true),
            new Route("R6", warehouse, customer, 11, 27, 3500, 1.8, 2, true),
            new Route("R7", warehouse, customer, 14, 24, 3900, 2.2, 1, true),
            new Route("R8", warehouse, customer, 10, 36, 2400, 2.5, 2, false)
        );

        DeliveryRequest request = new DeliveryRequest(DeliveryPriority.STANDARD, 5000, 50, false);
        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());

        System.out.println("=== Runtime strategy switching ===");
        System.out.println(planner.plan(routes, request));
        planner.setStrategy(new CheapestRouteStrategy());
        System.out.println(planner.plan(routes, request));
        planner.setStrategy(new EcoFriendlyRouteStrategy());
        System.out.println(planner.plan(routes, request));
        planner.setStrategy(new BalancedRouteStrategy(0.40, 0.30, 0.10, 0.20));
        System.out.println(planner.plan(routes, request));

        System.out.println("\\n=== Automatic strategy selection ===");
        RouteStrategy auto = new StrategySelector().select(request);
        planner.setStrategy(auto);
        System.out.println(planner.plan(routes, request));

        System.out.println("\\n=== Strategy comparison ===");
        for (RouteStrategy strategy : List.of(new FastestRouteStrategy(), new CheapestRouteStrategy(),
                new EcoFriendlyRouteStrategy(), new BalancedRouteStrategy(0.40, 0.30, 0.10, 0.20),
                new DistanceFirstStrategy())) {
            System.out.println(strategy.selectRoute(routes, request));
        }
    }
}
