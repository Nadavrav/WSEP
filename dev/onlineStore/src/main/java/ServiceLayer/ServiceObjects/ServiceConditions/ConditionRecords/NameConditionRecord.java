package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Store;

public record NameConditionRecord(String name) implements ConditionRecord {
    @Override
    public void accept(Store store) {
        store.addDiscount(this);
    }
}
