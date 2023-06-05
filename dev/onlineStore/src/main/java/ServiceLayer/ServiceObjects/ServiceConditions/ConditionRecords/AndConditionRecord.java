package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;


import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Store;

public record AndConditionRecord(int id1, int id2) implements ConditionRecord {

    @Override
    public Discount accept(Store store, String description, double amount) {
        return store.addDiscount(this,description,amount);
    }
}
