package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class CheckIfCondition implements FilterCondition {
    protected Condition firstCondition;
    protected Condition secondCondition;

    /**
     * if a condition passes, runs a second condition on all the products in the bag, basically if [something] check [something]
     */
    public CheckIfCondition(Condition firstCondition, Condition secondCondition){
        this.firstCondition=firstCondition;
        this.secondCondition=secondCondition;
    }
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> firstConditionEligibleProducts=firstCondition.passCondition(bag);
        if(!firstConditionEligibleProducts.isEmpty())
            return secondCondition.passCondition(bag);
        return new HashSet<>();
    }
    public Condition getFirstCondition() {
        return firstCondition;
    }

    public Condition getSecondCondition() {
        return secondCondition;
    }

    public boolean equals(Condition condition) {
        if(condition instanceof CheckIfCondition)
            return ((CheckIfCondition)condition).getFirstCondition().equals(firstCondition) && ((CheckIfCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
