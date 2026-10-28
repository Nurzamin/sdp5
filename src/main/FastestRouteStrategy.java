package main;

import java.util.List;

public class FastestRouteStrategy extends AbstractRouteStrategy {
    @Override public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
        List<Route> valid = validRoutes(routes, request);
        Route best = valid.stream().min((a, b) ->
                Integer.compare(a.estimatedMinutes() + a.trafficPenaltyMinutes(),
                                b.estimatedMinutes() + b.trafficPenaltyMinutes())).orElseThrow();
        int finalTime = best.estimatedMinutes() + best.trafficPenaltyMinutes();
        return decision(best, "final time " + finalTime + " min (includes traffic penalty)", finalTime);
    }
    @Override public String getStrategyName() { return "Fastest"; }
}
