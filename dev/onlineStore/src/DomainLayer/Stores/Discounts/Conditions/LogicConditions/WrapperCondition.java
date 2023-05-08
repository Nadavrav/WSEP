package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class WrapperCondition extends LogicCondition {
    public WrapperCondition(Condition firstCondition, Condition secondCondition) {
        super(firstCondition, secondCondition);
    }

    /**
     * implements logic passing a 2 conditions one after another, products must pass the first condition, and then only products that passed it get tested in the second condition
     * reasoning: a new bag is created for products that pass the first condition, so for example it makes a condition of "price of all dairy products > X" possible if the
     * first condition is to filter dairy products, and the second condition is about bag price
     * @param bag bag of products
     * @return list of products
     */
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        Bag eligibleProductsBag=new Bag(bag.getStoreID());
        HashSet<CartProduct> firstConditionEligibleProducts=firstCondition.passCondition(bag);
        for(CartProduct cartProduct:firstConditionEligibleProducts)
            eligibleProductsBag.addProduct(cartProduct);
        return secondCondition.passCondition(eligibleProductsBag);

    }
}
