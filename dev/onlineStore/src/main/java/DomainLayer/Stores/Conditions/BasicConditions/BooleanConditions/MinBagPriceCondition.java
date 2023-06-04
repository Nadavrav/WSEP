package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MinBagPriceCondition implements BooleanCondition {
    private final double price;

    public MinBagPriceCondition(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        int bagPrice=0;
        for(CartProduct product:bag.getProducts())
            bagPrice+=(product.getPrice()*product.getAmount());
        if(bagPrice < price)
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MinBagPriceCondition)
            return ((MinBagPriceCondition)condition).getPrice()==price;
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
