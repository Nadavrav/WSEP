package DomainLayer.Stores.Discounts.Conditions.ComplexConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MultiAndCondition extends CompositeCondition{
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>(bag.getProducts());
        for(Condition condition:conditions) {
            HashSet<CartProduct> passConditionList=condition.passCondition(bag);
            eligibleProducts.removeIf(eligibleProduct -> !passConditionList.contains(eligibleProduct));
        }
        return eligibleProducts;
    }
}
