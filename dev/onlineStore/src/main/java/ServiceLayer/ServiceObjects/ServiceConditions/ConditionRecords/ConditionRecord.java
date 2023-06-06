package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;

public interface ConditionRecord {
    Discount acceptDiscount(ConditionFactory factory, String description, double amount);
    Policy acceptPolicy(ConditionFactory factory, String description);

}
