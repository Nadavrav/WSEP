package DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class CategoryCondition implements Condition {
    private final String category;

    public CategoryCondition(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts = new HashSet<>();
        for(CartProduct product:bag.getProducts())
            if(product.getCategory().equals(category))
                eligibleProducts.add(product);
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof CategoryCondition)
            return ((CategoryCondition)condition).getCategory().equals(category);
        return false;
    }
}
