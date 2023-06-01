package DomainLayer.Stores.Conditions.ComplexConditions.BooleanLogicConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import java.util.HashSet;

public class OrCondition extends LogicCondition {
    public OrCondition(BooleanCondition firstCondition, BooleanCondition secondCondition) {
        super(firstCondition, secondCondition);
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts1=firstCondition.passCondition(bag);
        HashSet<CartProduct> eligibleProducts2=secondCondition.passCondition(bag);
        if(eligibleProducts1.isEmpty() && eligibleProducts2.isEmpty())
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof OrCondition)
            return ((OrCondition)condition).getFirstCondition().equals(firstCondition) && ((LogicCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
