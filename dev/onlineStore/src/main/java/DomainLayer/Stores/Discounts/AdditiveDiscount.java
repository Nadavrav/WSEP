package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Users.Bag;

import java.util.HashSet;
import java.util.Set;

public class AdditiveDiscount extends Discount{

    private final Set<Discount> discounts;

    public AdditiveDiscount(String description) {
        super(description);
        discounts=new HashSet<>();
    }

    public AdditiveDiscount(String description, Condition condition) {
        super(description, condition);
        discounts=new HashSet<>();
    }
    public void addDiscount(Discount discount){
        discounts.add(discount);
    }
    public boolean removeDiscount(Discount discount){
        return discounts.remove(discount);
    }


    @Override
    public double calcDiscountAmount(Bag bag) {
        double totalSaved=0;
        for(Discount discount:discounts){
            totalSaved+= discount.calcDiscountAmount(bag);
        }
        return totalSaved;
    }
}
