package DomainLayer.Stores.Discounts.Conditions.BasicConditions.FilterConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class NoCondition implements Condition {
    public NoCondition(){

    }
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        return new HashSet<>(bag.getProducts());
    }

    @Override
    public boolean equals(Condition condition) {
        return (condition instanceof NoCondition);
    }
}
