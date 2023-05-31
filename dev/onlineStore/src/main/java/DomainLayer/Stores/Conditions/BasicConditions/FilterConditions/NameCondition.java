package DomainLayer.Stores.Conditions.BasicConditions.FilterConditions;


import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class NameCondition implements FilterCondition {
    private final String name;

    public NameCondition(String name) {
        this.name = name;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        HashSet<CartProduct> eligibleProducts=new HashSet<>();
        for(CartProduct product:bag.getProducts()){
            if(product.getName().equals(name))
                eligibleProducts.add(product);
        }
        return eligibleProducts;
    }

    @Override
    public boolean equals(Condition condition) {
        if (condition instanceof NameCondition)
            return ((NameCondition) condition).getName().equals(name);
        return false;
    }

    public String getName() {
        return name;
    }
}
