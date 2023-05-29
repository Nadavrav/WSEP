package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;


import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class ContainsProductCondition implements Condition {
    private final Product product;

    public ContainsProductCondition(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        for(Product product:bag.getProducts()){
            if(this.product.equals(product)) {
                return new HashSet<>(bag.getProducts());
            }
        }
        return new HashSet<>();
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof ContainsProductCondition)
            return ((ContainsProductCondition)condition).getProduct().equals(product);
        return false;
    }
}
