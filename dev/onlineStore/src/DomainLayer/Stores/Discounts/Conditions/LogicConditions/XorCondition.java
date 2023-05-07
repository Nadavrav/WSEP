package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class XorCondition extends LogicCondition {
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> condition1Eligible=firstCondition.passCondition(bag);
        HashSet<CartProduct> condition2Eligible=secondCondition.passCondition(bag);
        condition1Eligible.removeIf(cartProduct -> !condition2Eligible.contains(cartProduct));
        return condition1Eligible;
    }
}
