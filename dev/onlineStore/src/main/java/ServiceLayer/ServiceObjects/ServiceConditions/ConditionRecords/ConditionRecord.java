package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Store;

public interface ConditionRecord {
    Discount accept(Store store, String description, double amount);
}
