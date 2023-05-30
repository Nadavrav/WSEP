package DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions;

import DomainLayer.Stores.Conditions.ComplexConditions.BooleanLogicConditions.LogicCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import java.util.HashSet;

public class BooleanAfterFilterCondition implements BooleanCondition {
    protected FilterCondition firstCondition;
    protected BooleanCondition secondCondition;

    /**
     * boolean condition-either returns the whole bag if eligibility logic is true, returns an empty bag otherwise.
     * and/or filters can be done in multi or/and conditions. wrapper condition is an exception, but I am too lazy to separate it. (proper docs in wrapper)
     * @param firstCondition one of the conditions
     * @param secondCondition the second condition
     */
    public BooleanAfterFilterCondition(FilterCondition firstCondition,BooleanCondition secondCondition){
        this.firstCondition=firstCondition;
        this.secondCondition=secondCondition;
    }
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        Bag eligibleProductsBag=new Bag(bag);
        HashSet<CartProduct> firstConditionEligibleProducts=firstCondition.passCondition(bag);
        for(CartProduct cartProduct:firstConditionEligibleProducts)
            eligibleProductsBag.addProduct(cartProduct);
        return secondCondition.passCondition(eligibleProductsBag);
    }
    public FilterCondition getFirstCondition() {
        return firstCondition;
    }

    public BooleanCondition getSecondCondition() {
        return secondCondition;
    }

    public boolean equals(Condition condition) {
        if(condition instanceof BooleanAfterFilterCondition)
            return ((BooleanAfterFilterCondition)condition).getFirstCondition().equals(firstCondition) && ((BooleanAfterFilterCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
}
