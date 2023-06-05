package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;

public interface ConditionRecord {
    Discount accept(ConditionFactory factory, String description, double amount);
}
