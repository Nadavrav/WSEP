package DomainLayer.Stores.Conditions.BasicConditions.FilterConditions;


import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MaxQuantityCondition implements FilterCondition {
    private final int quantity;

    public MaxQuantityCondition(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(CartProduct product:bag.getProducts()){
            if(product.getAmount() <= quantity)
                eligibleProducts.add(product);
        }
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if (condition instanceof MaxQuantityCondition)
            return ((MaxQuantityCondition) condition).getQuantity()==quantity;
        return false;
    }

    public int getQuantity() {
        return quantity;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
