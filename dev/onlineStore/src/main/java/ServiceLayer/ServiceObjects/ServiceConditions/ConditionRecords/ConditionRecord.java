package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Store;

public interface ConditionRecord {
    void accept(Store store);
}
