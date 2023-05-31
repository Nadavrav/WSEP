package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class ContainsCategoryCondition implements BooleanCondition {
    private final String category;

    public ContainsCategoryCondition(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        for(Product product:bag.getProducts()){
            if(this.category.equals(product.getCategory())) {
                return new HashSet<>(bag.getProducts());
            }
        }
        return new HashSet<>();
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof ContainsCategoryCondition)
            return ((ContainsCategoryCondition)condition).getCategory().equals(category);
        return false;
    }
}
