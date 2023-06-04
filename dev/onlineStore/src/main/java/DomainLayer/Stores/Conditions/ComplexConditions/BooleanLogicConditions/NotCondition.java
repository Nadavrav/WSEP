package DomainLayer.Stores.Conditions.ComplexConditions.BooleanLogicConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class NotCondition implements BooleanCondition {
    private final BooleanCondition condition;

    public NotCondition(BooleanCondition condition) {
        this.condition = condition;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        if(condition.passCondition(bag).isEmpty())
            return new HashSet<>(bag.getProducts());
        else return new HashSet<>();
    }

    @Override
    public boolean equals(Condition condition) {
        return false;
    }
}
