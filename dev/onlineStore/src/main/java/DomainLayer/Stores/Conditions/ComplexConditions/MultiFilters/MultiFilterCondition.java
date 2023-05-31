package DomainLayer.Stores.Conditions.ComplexConditions.MultiFilters;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;

import java.util.Collection;
import java.util.HashSet;

public abstract class MultiFilterCondition implements FilterCondition {
    protected Collection<Condition> conditions;
    public MultiFilterCondition(){
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
        if(condition instanceof MultiFilterCondition)
            return ((MultiFilterCondition)condition).getConditions().equals(conditions);
        return false;
    }
    @Override
    public String toString(){
      //  return "conditi"+condition
        return "STUB";
    }
}
