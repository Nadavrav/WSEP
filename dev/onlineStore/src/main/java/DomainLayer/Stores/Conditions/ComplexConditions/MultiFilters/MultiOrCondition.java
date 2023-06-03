package DomainLayer.Stores.Conditions.ComplexConditions.MultiFilters;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MultiOrCondition extends MultiFilterCondition {
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(Condition condition:conditions)
            eligibleProducts.addAll(condition.passCondition(bag)); //set has no duplicates, so this works for OR
        return eligibleProducts;
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MultiOrCondition)
            return ((MultiOrCondition)condition).getConditions().equals(conditions);
        return false;
    }
    @Override
    public String toString(){
        StringBuilder ret= new StringBuilder();
        for(Condition condition:conditions){
            ret.append(condition.toString()).append(" or ");
        }
        return ret.toString();
    }
}
