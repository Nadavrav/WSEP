package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class XorCondition extends LogicCondition {
    public XorCondition(BooleanCondition firstCondition, BooleanCondition secondCondition) {
        super(firstCondition, secondCondition);
    }

    /**
     * logical xor- exactly one of the conditions has to pass some products,
     * @param bag products
     * @return empty of condition is bot true, the whole bag otherwise
     */
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> condition1Eligible=firstCondition.passCondition(bag);
        HashSet<CartProduct> condition2Eligible=secondCondition.passCondition(bag);
        if(!condition1Eligible.isEmpty() && condition2Eligible.isEmpty())
            return new HashSet<>(bag.getProducts());
        else if(condition1Eligible.isEmpty() && !condition2Eligible.isEmpty())
            return new HashSet<>(bag.getProducts());
        else return new HashSet<>();
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof XorCondition)
            return ((XorCondition)condition).getFirstCondition().equals(firstCondition) && ((LogicCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
