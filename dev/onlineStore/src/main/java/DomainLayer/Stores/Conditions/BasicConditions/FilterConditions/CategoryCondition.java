package DomainLayer.Stores.Conditions.BasicConditions.FilterConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class CategoryCondition implements FilterCondition {
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
    @Override
    public String toString(){
        return "TODO";
    }
}
