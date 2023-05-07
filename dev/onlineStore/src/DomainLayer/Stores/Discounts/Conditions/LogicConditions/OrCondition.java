package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import java.util.HashSet;

public class OrCondition extends LogicCondition {
    public OrCondition(Condition firstCondition, Condition secondCondition) {
        super(firstCondition, secondCondition);
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=firstCondition.passCondition(bag);
        eligibleProducts.addAll(secondCondition.passCondition(bag));
        return eligibleProducts;
    }
}
