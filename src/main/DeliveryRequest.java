package main;

public record DeliveryRequest(DeliveryPriority priority, double maxBudget,
                              int maxDeliveryMinutes, boolean ecoPreferred) {
    public DeliveryRequest {
        if (priority == null) throw new IllegalArgumentException("Priority is required");
        if (maxBudget < 0 || maxDeliveryMinutes < 0)
            throw new IllegalArgumentException("Budget and deadline cannot be negative");
    }
}
