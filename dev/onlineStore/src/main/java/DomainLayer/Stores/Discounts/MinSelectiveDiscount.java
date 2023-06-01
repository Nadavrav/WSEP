package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashMap;
import java.util.StringJoiner;

public class MinSelectiveDiscount extends ManyDiscounts {

    public MinSelectiveDiscount(String description) {
        super(description);
    }
    @Override
    public double calcDiscountAmount(Bag bag) {
        double minDiscount=0;
        for(Discount discount:discounts){
            double discountAmount= discount.calcDiscountAmount(bag);
            if(discountAmount<minDiscount)
                minDiscount=discountAmount;
        }
        return minDiscount;
    }
    @Override
    public HashMap<CartProduct,Double> calcDiscountPerProduct(Bag bag) {
        if (bag == null)
            throw new NullPointerException("Null bag in discount calculation");
        double min = 0;
        HashMap<CartProduct,Double> currentBest = new HashMap<>();
        for (Discount discount : discounts) {
            HashMap<CartProduct,Double> currentMap = discount.calcDiscountPerProduct(bag);
            double currentSum = 0;
            for (double saving : currentMap.values())
                currentSum += saving;
            if (currentSum < min) {
                min = currentSum;
                currentBest = currentMap;
            }
        }
        return currentBest;
    }
    @Override
    public String toString(){
        if(discounts.isEmpty()){
            return "[ERROR: an minimum between discount list was defined by no discount was added to it]";
        }
        StringJoiner joiner = new StringJoiner(" and ","the minimal discount value between "," ");

        for (Discount discount : discounts) {
            joiner.add(discount.toString());
        }
        return joiner.toString();
    }
}
