package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.Condition;

import java.util.Collection;
import java.util.HashSet;

public abstract class CompositeCondition implements Condition {
    protected Collection<Condition> conditions;
    public CompositeCondition(){
        conditions=new HashSet<>();
    }
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
