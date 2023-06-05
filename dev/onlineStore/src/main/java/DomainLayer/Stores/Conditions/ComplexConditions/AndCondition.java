package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
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
        if(condition1Eligible.isEmpty() || condition2Eligible.isEmpty())
            return new HashSet<>();
        else{
                condition1Eligible.removeIf(cartProduct -> !condition2Eligible.contains(cartProduct));
                return condition1Eligible;
        }
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof AndCondition)
            return ((AndCondition)condition).getFirstCondition().equals(firstCondition) && ((LogicCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
