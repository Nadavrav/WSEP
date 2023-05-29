package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MaxTotalProductAmountCondition implements Condition {
    private final double amount;

    public MaxTotalProductAmountCondition(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        int totalAmount=0;
        for(CartProduct product:bag.getProducts())
            totalAmount+=product.getAmount();
        if(totalAmount > amount)
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MaxTotalProductAmountCondition)
            return ((MaxTotalProductAmountCondition)condition).getAmount()==amount;
        return false;
    }
}
