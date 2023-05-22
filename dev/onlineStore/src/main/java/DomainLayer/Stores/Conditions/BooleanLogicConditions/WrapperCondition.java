package DomainLayer.Stores.Conditions.BooleanLogicConditions;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class WrapperCondition extends LogicCondition {
    public WrapperCondition(Condition firstCondition, Condition secondCondition) {
        super(firstCondition, secondCondition);
    }

    /**
     * implements logic passing a 2 conditions one after another, products must pass the first condition, and then only products those who pass it are tested in the second condition
     * reasoning: a new bag is created for products that pass the first condition, so for example it makes a condition of "price of all dairy products >= X" possible if the
     * first condition is to filter dairy products, and the second condition is about bag price (alternative impl would be a condition that holds both a product and price, and thats
     * messier and less general than this). final note: intention is for the first condition to be a of filtering type, and the second to be of boolean condition (see: basic conditions).
     * other mixes may result in unintended results.
     * @param bag bag of products
     * @return list of products who pass the first condition, and then pass the second condition
     */
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        Bag eligibleProductsBag=new Bag(bag);
        HashSet<CartProduct> firstConditionEligibleProducts=firstCondition.passCondition(bag);
        for(CartProduct cartProduct:firstConditionEligibleProducts)
            eligibleProductsBag.addProduct(cartProduct);
        return secondCondition.passCondition(eligibleProductsBag);

    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof WrapperCondition)
            return ((WrapperCondition)condition).getFirstCondition().equals(firstCondition) && ((LogicCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
}
