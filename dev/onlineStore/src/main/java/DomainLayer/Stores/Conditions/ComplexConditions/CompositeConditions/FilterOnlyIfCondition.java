package DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class FilterOnlyIfCondition implements FilterCondition {
    protected BooleanCondition firstCondition;
    protected FilterCondition secondCondition;


    public FilterOnlyIfCondition(BooleanCondition firstCondition,FilterCondition secondCondition){
        this.firstCondition=firstCondition;
        this.secondCondition=secondCondition;
    }
    public BooleanCondition getFirstCondition() {
        return firstCondition;
    }

    public FilterCondition getSecondCondition() {
        return secondCondition;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> firstConditionEligibleProducts=firstCondition.passCondition(bag);
        if(!firstConditionEligibleProducts.isEmpty()) {
            return secondCondition.passCondition(bag);
        }
        return firstConditionEligibleProducts;
    }

    public boolean equals(Condition condition) {
        if(condition instanceof FilterOnlyIfCondition)
            return ((FilterOnlyIfCondition)condition).getFirstCondition().equals(firstCondition) && ((FilterOnlyIfCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
