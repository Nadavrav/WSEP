package DomainLayer.Stores.Discounts.Conditions.ComplexConditions;

import DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions.MaxBagPriceCondition;
import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CompositeCondition implements Condition {
    protected Collection<Condition> conditions;
    public Collection<Condition> getConditions() {
        return conditions;
    }
    public void addCondition(Condition condition){
        conditions.add(condition);
    }
    public void removeCondition(Condition condition){
        conditions.remove(condition);
    }
    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof CompositeCondition)
            return ((CompositeCondition)condition).getConditions().equals(conditions);
        return false;
    }
}
