package DomainLayer.Stores.Discounts;


import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.*;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.*;
import DomainLayer.Stores.Conditions.ComplexConditions.*;
import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Store;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConditionFactory {
    private final AtomicInteger DiscountID_GENERATOR = new AtomicInteger(0);
    private Store store;

    public void setStore(Store store) {
        this.store = store;
    }
    public Discount addDiscount(ConditionRecord conditionRecord, String description, double discountAmount){
        return conditionRecord.accept(this,description,discountAmount);
    }
    public Discount addDiscount(NameConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new NameCondition(record.name()));
    }
    public Discount addDiscount(CategoryConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new CategoryCondition(record.category()));
    }
    public Discount addDiscount(BetweenDatesConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new BetweenDatesCondition(record.fromDay(),record.fromMonth(),record.fromYear(),record.untilDay(),record.untilMonth(),record.untilMonth()));
    }
    public Discount addDiscount(LocalHourRangeConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new LocalHourRangeCondition(record.lowerBoundaryHour(), record.lowerBoundaryMin(), record.upperBoundaryHour(), record.upperBoundaryMin()));
    }
    public Discount addDiscount(MaxBagPriceConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MaxBagPriceCondition(record.price()));
    }
    public Discount addDiscount(MinBagPriceConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MinBagPriceCondition(record.price()));
    }
    public Discount addDiscount(MaxPriceConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MaxPriceCondition(record.price()));
    }
    public Discount addDiscount(MinPriceConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MinPriceCondition(record.price()));
    }
    public Discount addDiscount(MaxQuantityConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MaxQuantityCondition(record.quantity()));
    }
    public Discount addDiscount(MinQuantityConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MinQuantityCondition(record.quantity()));
    }
    public Discount addDiscount(MaxTotalProductAmountConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MaxTotalProductAmountCondition(record.amount()));
    }
    public Discount addDiscount(MinTotalProductAmountConditionRecord record, String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new MinTotalProductAmountCondition(record.amount()));
    }
    public Discount addDiscount(String description, double discountAmount){
        return new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new NoCondition());
    }
    public Discount addDiscount(AndConditionRecord andConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(andConditionRecord.id1()) || !store.containsDiscount(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new AndCondition(c1,c2));
            store.removeDiscount(andConditionRecord.id1());
            store.removeDiscount(andConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Discount addDiscount(NotConditionRecord notConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(notConditionRecord.id()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c = ((BasicDiscount) (store.getDiscount(notConditionRecord.id()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new NotCondition(c));
            store.removeDiscount(notConditionRecord.id());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Discount addDiscount(OrConditionRecord orConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(orConditionRecord.id1()) || !store.containsDiscount(orConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (store.getDiscount(orConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (store.getDiscount(orConditionRecord.id1()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new OrCondition(c1,c2));
            store.removeDiscount(orConditionRecord.id1());
            store.removeDiscount(orConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Discount addDiscount(XorConditionRecord xorConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(xorConditionRecord.id1()) || !store.containsDiscount(xorConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (store.getDiscount(xorConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (store.getDiscount(xorConditionRecord.id1()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new XorCondition(c1,c2));
            store.removeDiscount(xorConditionRecord.id1());
            store.removeDiscount(xorConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Discount addDiscount(CheckIfConditionRecord andConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(andConditionRecord.id1()) || !store.containsDiscount(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new CheckIfCondition(c1,c2));
            store.removeDiscount(andConditionRecord.id1());
            store.removeDiscount(andConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Discount addDiscount(CheckForConditionRecord andConditionRecord, String description, double discountAmount){
        if(!store.containsDiscount(andConditionRecord.id1()) || !store.containsDiscount(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id1()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new CheckForCondition((FilterCondition) c1,(BooleanCondition) c2));
            store.removeDiscount(andConditionRecord.id1());
            store.removeDiscount(andConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }

}
