package DomainLayer.Stores.Discounts.Conditions.BasicConditions.FilterConditions;


import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MaxPriceCondition implements Condition {
    private final double price;

    public MaxPriceCondition(double price) {
        this.price = price;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(CartProduct product:bag.getProducts()){
            if(product.getPrice() <= price)
                eligibleProducts.add(product);
        }
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if (condition instanceof MaxPriceCondition)
            return ((MaxPriceCondition) condition).getPrice()==price;
        return false;
    }

    public double getPrice() {
        return price;
    }
}