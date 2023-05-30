package DomainLayer.Stores.Conditions.ConditionTypes;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public interface Condition {
    /**
     * basic condition which iterates over all the products in eligibleProducts and removes products that done pass a condition
     * @return at most all bag products, or a smaller list (can be empty) of products from eligible products that pass the condition.
     * low priority todo: split condition to filter condition and boolean condition (no new methods for either, see WrapperCondition for reasoning)
     */
    HashSet<CartProduct> passCondition(Bag bag);

    /**
     * compares two conditions and returns true if they are the same condition,
     * made for ease in removing/adding/editing complex and logic conditions
     * @param condition condition to compare
     * @return true or false
     */
    boolean equals(Condition condition);
}
