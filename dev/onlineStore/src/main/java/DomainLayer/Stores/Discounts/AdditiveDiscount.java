package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.StringJoiner;

public class AdditiveDiscount extends ManyDiscounts {


    public AdditiveDiscount(String description,int id) {
        super(description,id);
    }


    @Override
    public double calcDiscountAmount(Bag bag) {
        double totalSaved=0;
        for(Discount discount:discounts){
            totalSaved+= discount.calcDiscountAmount(bag);
        }
        return totalSaved;
    }
    @Override
    public HashMap<CartProduct,Double> calcDiscountPerProduct(Bag bag) {
        if (bag == null)
            throw new NullPointerException("Null bag in discount calculation");
        HashMap<CartProduct,Double> totalMap = new HashMap<>();
        for (Discount discount : discounts) {
            HashMap<CartProduct,Double> currentMap = discount.calcDiscountPerProduct(bag);
            for(CartProduct cartProduct:currentMap.keySet()){
               if(totalMap.get(cartProduct)!=null){
                   totalMap.put(cartProduct,totalMap.get(cartProduct)+currentMap.get(cartProduct));
               }
                else totalMap.put(cartProduct,currentMap.get(cartProduct));
            }
        }
        return totalMap;
    }
    @Override
    public String toString(){
        if(discounts.isEmpty()){
            return "[ERROR: an additive discount list was defined by no discount was added to it]";
        }
        StringJoiner joiner = new StringJoiner(" and ","the sum of the following discounts: "," ");

        for (Discount discount : discounts) {
            joiner.add(discount.toString());
        }
        return joiner.toString();
    }
}
