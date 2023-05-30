package DomainLayer.Stores.Conditions.BasicConditions.FilterConditions;


import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MinPriceCondition implements FilterCondition {
    private final double price;

    public MinPriceCondition(double price) {
        this.price = price;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(CartProduct product:bag.getProducts()){
            if(product.getPrice() >= price)
                eligibleProducts.add(product);
        }
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if (condition instanceof MinPriceCondition)
            return ((MinPriceCondition) condition).getPrice()==price;
        return false;
    }

    public double getPrice() {
        return price;
    }
}
