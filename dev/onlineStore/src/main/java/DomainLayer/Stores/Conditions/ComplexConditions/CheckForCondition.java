package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;
/**
 * if a condition passes, runs a second condition on all the products that pass the first condition, in a new separate bag with its own price
 */
public class CheckForCondition implements BooleanCondition {
    protected FilterCondition firstCondition;
    protected BooleanCondition secondCondition;

    /**
     * if fistCondition passes, runs secondCondition on all the products that pass the first condition, in a new separate bag with its own price
     * basically: for all condition that are [something] check [something]
     * @param firstCondition first condition for products to pass
     * @param secondCondition second condition to run on products who pass the first condition
     */
    public CheckForCondition(FilterCondition firstCondition,BooleanCondition secondCondition){
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
        if(condition instanceof CheckForCondition)
            return ((CheckForCondition)condition).getFirstCondition().equals(firstCondition) && ((CheckForCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
