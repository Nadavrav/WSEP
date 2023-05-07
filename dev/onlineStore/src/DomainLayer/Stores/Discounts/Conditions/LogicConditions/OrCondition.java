package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import java.util.HashSet;

public class OrCondition extends LogicCondition {
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=firstCondition.passCondition(bag);
        eligibleProducts.addAll(secondCondition.passCondition(bag));
        return eligibleProducts;
    }
}
