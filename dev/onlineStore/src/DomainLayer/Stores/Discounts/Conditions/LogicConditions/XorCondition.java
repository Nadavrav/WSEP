package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class XorCondition extends LogicCondition {
    public XorCondition(Condition firstCondition, Condition secondCondition) {
        super(firstCondition, secondCondition);
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> condition1Eligible=firstCondition.passCondition(bag);
        HashSet<CartProduct> condition2Eligible=secondCondition.passCondition(bag);
        HashSet<CartProduct> merged=new HashSet<>(condition1Eligible);
        merged.addAll(condition2Eligible);
        merged.removeIf(cartProduct -> condition1Eligible.contains(cartProduct) && condition2Eligible.contains(cartProduct));
        return merged;
    }
}
