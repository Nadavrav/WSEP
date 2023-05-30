package DomainLayer.Stores.Conditions.BasicConditions.FilterConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class NoCondition implements BooleanCondition {
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
