package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MaxBagPriceCondition implements Condition {
    private final double price;

    public MaxBagPriceCondition(double price) {
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
        if(bagPrice >price)
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MaxBagPriceCondition)
            return ((MaxBagPriceCondition)condition).getPrice()==price;
        return false;
    }
}