package test;

import main.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RouteStrategyTest {
    private final Location a = new Location("A", 1, 1);
    private final Location b = new Location("B", 2, 2);
    private Route route(String id, double d, int t, double c, double e, int traffic, boolean available) {
        return new Route(id, a, b, d, t, c, e, traffic, available);
    }
    private List<Route> routes() {
        return List.of(route("fast", 10, 10, 400, 3, 2, true),
                route("cheap", 20, 25, 100, 4, 0, true),
                route("eco", 12, 20, 250, 0.5, 1, true),
                route("closed", 1, 1, 1, 0.1, 0, false));
    }
    private DeliveryRequest request() { return new DeliveryRequest(DeliveryPriority.STANDARD, 500, 40, false); }

    @Test void fastestChoosesLowestFinalTime() {
        assertEquals("fast", new FastestRouteStrategy().selectRoute(routes(), request()).route().id());
    }
    @Test void fastestIncludesTrafficPenalty() {
        assertEquals(12, new FastestRouteStrategy().selectRoute(routes(), request()).score());
    }
    @Test void cheapestChoosesLowestFormulaCost() {
        assertEquals("eco", new CheapestRouteStrategy().selectRoute(routes(), request()).route().id());
    }
    @Test void cheapestFormulaIncludesDistance() {
        assertEquals(490, new CheapestRouteStrategy().selectRoute(routes(), request()).score());
    }
    @Test void ecoChoosesLowestEcoScore() {
        assertEquals("eco", new EcoFriendlyRouteStrategy().selectRoute(routes(), request()).route().id());
    }
    @Test void balancedReturnsValidRoute() {
        assertTrue(routes().stream().anyMatch(r -> r.id().equals(
                new BalancedRouteStrategy(.4,.3,.1,.2).selectRoute(routes(), request()).route().id())));
    }
    @Test void balancedWeightsMustSumToOne() {
        assertThrows(IllegalArgumentException.class, () -> new BalancedRouteStrategy(.5,.5,.5,0));
    }
    @Test void balancedRejectsNegativeWeight() {
        assertThrows(IllegalArgumentException.class, () -> new BalancedRouteStrategy(-.1,.6,.3,.2));
    }
    @Test void distanceFirstChoosesShortestAvailableRoute() {
        assertEquals("fast", new DistanceFirstStrategy().selectRoute(routes(), request()).route().id());
    }
    @Test void plannerCanSwitchStrategyAtRuntime() {
        RoutePlanner p = new RoutePlanner(new FastestRouteStrategy());
        String first = p.plan(routes(), request()).strategyName();
        p.setStrategy(new CheapestRouteStrategy());
        assertNotEquals(first, p.plan(routes(), request()).strategyName());
    }
    @Test void selectorChoosesFastestForUrgent() {
        assertInstanceOf(FastestRouteStrategy.class, new StrategySelector().select(
                new DeliveryRequest(DeliveryPriority.URGENT, 500, 40, false)));
    }
    @Test void selectorChoosesCheapestForEconomy() {
        assertInstanceOf(CheapestRouteStrategy.class, new StrategySelector().select(
                new DeliveryRequest(DeliveryPriority.ECONOMY, 500, 40, false)));
    }
    @Test void selectorChoosesEcoStrategy() {
        assertInstanceOf(EcoFriendlyRouteStrategy.class, new StrategySelector().select(
                new DeliveryRequest(DeliveryPriority.ECO, 500, 40, true)));
    }
    @Test void selectorChoosesBalancedForStandard() {
        assertInstanceOf(BalancedRouteStrategy.class, new StrategySelector().select(request()));
    }
    @Test void budgetConstraintRejectsExpensiveRoutes() {
        assertEquals("cheap", new CheapestRouteStrategy().selectRoute(routes(),
                new DeliveryRequest(DeliveryPriority.ECONOMY, 150, 40, false)).route().id());
    }
    @Test void deadlineConstraintRejectsSlowRoutes() {
        assertEquals("fast", new FastestRouteStrategy().selectRoute(routes(),
                new DeliveryRequest(DeliveryPriority.URGENT, 500, 13, false)).route().id());
    }
    @Test void unavailableRouteCannotWin() {
        assertNotEquals("closed", new FastestRouteStrategy().selectRoute(routes(), request()).route().id());
    }
    @Test void emptyRouteListThrowsException() {
        assertThrows(NoSuitableRouteException.class, () -> new FastestRouteStrategy().selectRoute(List.of(), request()));
    }
    @Test void noSuitableRouteThrowsException() {
        assertThrows(NoSuitableRouteException.class, () -> new FastestRouteStrategy().selectRoute(routes(),
                new DeliveryRequest(DeliveryPriority.URGENT, 0, 0, false)));
    }
    @Test void nullStrategyRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RoutePlanner(null));
    }
    @Test void negativeRouteCostRejected() {
        assertThrows(IllegalArgumentException.class, () -> route("bad", 1, 1, -1, 1, 0, true));
    }
}
