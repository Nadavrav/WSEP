package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashMap;
import java.util.StringJoiner;

public class MaxSelectiveDiscount extends ManyDiscounts {

    public MaxSelectiveDiscount(String description) {
        super(description);
    }

    @Override
    public double calcDiscountAmount(Bag bag) {
        double maxDiscount=0;
        for(Discount discount:discounts){
            double discountAmount= discount.calcDiscountAmount(bag);
            if(discountAmount>maxDiscount)
                maxDiscount=discountAmount;
        }
        return maxDiscount;
    }
    @Override
    public HashMap<CartProduct,Double> calcDiscountPerProduct(Bag bag) {
        if (bag == null)
            throw new NullPointerException("Null bag in discount calculation");
        double max = 0;
        HashMap<CartProduct,Double> currentBest = new HashMap<>();
        for (Discount discount : discounts) {
            HashMap<CartProduct,Double> currentMap = discount.calcDiscountPerProduct(bag);
            double currentSum = 0;
            for (double saving : currentMap.values())
                currentSum += saving;
            if (currentSum > max) {
                max = currentSum;
                currentBest = currentMap;
            }
        }
        return currentBest;
    }
    @Override
    public String toString(){
        if(discounts.isEmpty()){
            return "[ERROR: an maximum between discount list was defined by no discount was added to it]";
        }
        StringJoiner joiner = new StringJoiner(" and ","the maximum discount value between "," ");

        for (Discount discount : discounts) {
            joiner.add(discount.toString());
        }

        return joiner.toString();
    }

}
