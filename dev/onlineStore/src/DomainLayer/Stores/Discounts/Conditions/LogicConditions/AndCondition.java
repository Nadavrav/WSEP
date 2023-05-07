package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class AndCondition extends LogicCondition {
    public AndCondition(Condition firstCondition, Condition secondCondition) {
        super(firstCondition, secondCondition);
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> condition1Eligible=firstCondition.passCondition(bag);
        HashSet<CartProduct> condition2Eligible=secondCondition.passCondition(bag);
        condition1Eligible.removeIf(cartProduct -> !condition2Eligible.contains(cartProduct));
        return condition1Eligible;
    }
}
