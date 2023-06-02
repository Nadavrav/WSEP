package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class MinTotalProductAmountCondition implements BooleanCondition {
    private final double amount;

    public MinTotalProductAmountCondition(double amount) {
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
        if(totalAmount < amount)
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof MinTotalProductAmountCondition)
            return ((MinTotalProductAmountCondition)condition).getAmount()==amount;
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
