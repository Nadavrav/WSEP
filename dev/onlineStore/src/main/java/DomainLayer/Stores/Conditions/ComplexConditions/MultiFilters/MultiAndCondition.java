package DomainLayer.Stores.Conditions.ComplexConditions.MultiFilters;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MultiAndCondition extends MultiFilterCondition {

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>(bag.getProducts());
        for(Condition condition:conditions) {
            HashSet<CartProduct> passConditionList=condition.passCondition(bag);
            eligibleProducts.removeIf(eligibleProduct -> !passConditionList.contains(eligibleProduct));
        }
        return eligibleProducts;
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MultiAndCondition)
            return ((MultiAndCondition)condition).getConditions().equals(conditions);
        return false;
    }
}
