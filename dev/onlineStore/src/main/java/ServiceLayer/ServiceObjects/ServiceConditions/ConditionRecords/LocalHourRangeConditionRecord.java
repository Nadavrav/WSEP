package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Store;

public record LocalHourRangeConditionRecord(int lowerBoundaryHour, int lowerBoundaryMin, int upperBoundaryHour, int upperBoundaryMin) implements ConditionRecord {

    @Override
    public Discount accept(Store store, String description, double amount) {
        return store.addDiscount(this,description,amount);
    }
}
