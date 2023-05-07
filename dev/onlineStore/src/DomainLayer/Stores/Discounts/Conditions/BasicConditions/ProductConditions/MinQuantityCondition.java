package DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions;


import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MinQuantityCondition implements Condition {
    private final int quantity;

    public MinQuantityCondition(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(CartProduct product:bag.getProducts()){
            if(product.getAmount() >= quantity)
                eligibleProducts.add(product);
        }
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if (condition instanceof MinQuantityCondition)
            return ((MinQuantityCondition) condition).getQuantity()==quantity;
        return false;
    }

    public int getQuantity() {
        return quantity;
    }
}
