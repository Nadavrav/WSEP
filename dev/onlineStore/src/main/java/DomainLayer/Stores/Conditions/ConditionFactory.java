package DomainLayer.Stores.Conditions;


import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.*;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.*;
import DomainLayer.Stores.Conditions.ComplexConditions.*;
import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Discounts.BasicDiscount;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Discounts.ManyDiscounts;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Store;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
import ServiceLayer.ServiceObjects.ServiceDiscounts.DiscountType;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceBasicDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceMultiDiscount;
import ServiceLayer.ServiceObjects.ServicePolicies.ServiceBasicPolicy;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class ConditionFactory {
    private final AtomicInteger DiscountID_GENERATOR = new AtomicInteger(0);
    private final AtomicInteger PolicyID_GENERATOR = new AtomicInteger(0);

    private Store store;

    public void setStore(Store store) {
        this.store = store;
    }
    public Discount addDiscount(ConditionRecord conditionRecord, String description, double discountAmount){
        return conditionRecord.acceptDiscount(this,description,discountAmount);
    }
    public Discount addDiscount(ServiceDiscount serviceDiscount){
        return serviceDiscount.accept(this);
    }
    public Discount addDiscount(ServiceBasicDiscount serviceBasicDiscount){
        return addDiscount(serviceBasicDiscount.conditionRecord,serviceBasicDiscount.description,serviceBasicDiscount.discountAmount);
    }
    public Discount addDiscount(ServiceMultiDiscount serviceMultiDiscount){
        return addDiscount(serviceMultiDiscount.getDiscountType(),serviceMultiDiscount.getDiscounts(),serviceMultiDiscount.description);
    }
    public Discount addDiscount(DiscountType discountType, Collection<Integer> discounts, String description) {
        ManyDiscounts manyDiscounts=discountType.getDiscount(description,DiscountID_GENERATOR.getAndIncrement());
        for(Integer discountId:discounts)
            if(store.containsDiscount(discountId))
                manyDiscounts.addDiscount(store.removeDiscount(discountId));
        return manyDiscounts;
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
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id2()))).getConditions();
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
            Condition c2 = ((BasicDiscount) (store.getDiscount(orConditionRecord.id2()))).getConditions();
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
            Condition c2 = ((BasicDiscount) (store.getDiscount(xorConditionRecord.id2()))).getConditions();
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
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id2()))).getConditions();
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
            Condition c2 = ((BasicDiscount) (store.getDiscount(andConditionRecord.id2()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new CheckForCondition((FilterCondition) c1,(BooleanCondition) c2));
            store.removeDiscount(andConditionRecord.id1());
            store.removeDiscount(andConditionRecord.id2());
            return discount;
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Invalid discount types or conditions detected. \nsystem message:\n"+e.getMessage());
        }
    }
    public Policy addPolicy(ServiceBasicPolicy servicePolicy){
        return servicePolicy.getConditionRecord().acceptPolicy(this,servicePolicy.getDescription());
    }
    public Policy addPolicy(ServicePolicy servicePolicy){
        return servicePolicy.accept(this);
    }
    public Policy addPolicy(ConditionRecord servicePolicy,String description){
        return servicePolicy.acceptPolicy(this,description);
    }
    public Policy addPolicy(NoConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new NoCondition());
    }
    public Policy addPolicy(NameConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new NameCondition(record.name()));
    }
    public Policy addPolicy(CategoryConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new CategoryCondition(record.category()));
    }
    public Policy addPolicy(BetweenDatesConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new BetweenDatesCondition(record.fromDay(),record.fromMonth(),record.fromYear(),record.untilDay(),record.untilMonth(),record.untilMonth()));
    }
    public Policy addPolicy(LocalHourRangeConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new LocalHourRangeCondition(record.lowerBoundaryHour(), record.lowerBoundaryMin(), record.upperBoundaryHour(), record.upperBoundaryMin()));
    }
    public Policy addPolicy(MaxBagPriceConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MaxBagPriceCondition(record.price()));
    }
    public Policy addPolicy(MinBagPriceConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MinBagPriceCondition(record.price()));
    }
    public Policy addPolicy(MaxPriceConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MaxPriceCondition(record.price()));
    }
    public Policy addPolicy(MinPriceConditionRecord record, String description ){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MinPriceCondition(record.price()));
    }
    public Policy addPolicy(MaxQuantityConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MaxQuantityCondition(record.quantity()));
    }
    public Policy addPolicy(MinQuantityConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MinQuantityCondition(record.quantity()));
    }
    public Policy addPolicy(MaxTotalProductAmountConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MaxTotalProductAmountCondition(record.amount()));
    }
    public Policy addPolicy(MinTotalProductAmountConditionRecord record, String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new MinTotalProductAmountCondition(record.amount()));
    }
    public Policy addPolicy(String description){
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new NoCondition());
    }
    public Policy addPolicy(AndConditionRecord andConditionRecord, String description) {
        if(!store.containsPolicy(andConditionRecord.id1()) || !store.containsPolicy(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        Condition c1=store.removePolicy(andConditionRecord.id1()).getCondition();
        Condition c2=store.removePolicy(andConditionRecord.id2()).getCondition();
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new AndCondition(c1,c2));
    }
    public Policy addPolicy(OrConditionRecord andConditionRecord, String description) {
        if(!store.containsPolicy(andConditionRecord.id1()) || !store.containsPolicy(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        Condition c1=store.removePolicy(andConditionRecord.id1()).getCondition();
        Condition c2=store.removePolicy(andConditionRecord.id2()).getCondition();
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new OrCondition(c1,c2));
    }
    public Policy addPolicy(XorConditionRecord andConditionRecord, String description) {
        if(!store.containsPolicy(andConditionRecord.id1()) || !store.containsPolicy(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        Condition c1=store.removePolicy(andConditionRecord.id1()).getCondition();
        Condition c2=store.removePolicy(andConditionRecord.id2()).getCondition();
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new XorCondition(c1,c2));
    }
    public Policy addPolicy(NotConditionRecord andConditionRecord, String description) {
        if(!store.containsPolicy(andConditionRecord.id()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        Condition c=store.removePolicy(andConditionRecord.id()).getCondition();
        return new Policy(description,PolicyID_GENERATOR.getAndIncrement(),new NotCondition(c));
    }
}