package DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions;


import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class DoesntContainProductCondition implements Condition {
    private final Product product;



    public DoesntContainProductCondition(Product product) {
        this.product = product;
    }
    public Product getProduct() {
        return product;
    }
    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        for(Product product:bag.getProducts()){
            if(this.product.equals(product)) {
                return new HashSet<>();
            }
        }
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof DoesntContainProductCondition)
            return ((DoesntContainProductCondition)condition).getProduct().equals(product);
        return false;
    }
}
