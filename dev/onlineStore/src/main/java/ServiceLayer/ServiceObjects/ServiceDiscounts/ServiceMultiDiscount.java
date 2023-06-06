package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Discounts.ManyDiscounts;

import java.util.Collection;
import java.util.HashSet;

public class ServiceMultiDiscount extends ServiceDiscount{
    private final Collection<Integer> discounts;
    private final DiscountType discountType;
    public ServiceMultiDiscount(DiscountType discountType,String description) {
        super(description);
        this.discountType=discountType;
        discounts=new HashSet<>();
    }

    public ServiceMultiDiscount(DiscountType discountType,String description,Collection<Integer> ids) {
        super(description);
        discounts=new HashSet<>();
        discounts.addAll(ids);
        this.discountType=discountType;
    }
    public void addDiscount(int id){
        discounts.add(id);
    }
    public boolean removeDiscount(int id){
        return discounts.remove(id);
    }
    public Collection<Integer> getDiscounts() {
        return discounts;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    @Override
    public Discount accept(ConditionFactory conditionFactory) {
        return conditionFactory.addDiscount(this);
    }
}
