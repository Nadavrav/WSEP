package DomainLayer.Stores.Conditions.ComplexConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;

public abstract class LogicCondition implements Condition {
    protected Condition firstCondition;
    protected Condition secondCondition;


    public LogicCondition(Condition firstCondition,Condition secondCondition){
        this.firstCondition=firstCondition;
        this.secondCondition=secondCondition;
    }
    public Condition getFirstCondition() {
        return firstCondition;
    }

    public Condition getSecondCondition() {
        return secondCondition;
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof LogicCondition)
            return ((LogicCondition)condition).getFirstCondition().equals(firstCondition) && ((LogicCondition)condition).getSecondCondition().equals(secondCondition);
        return false;
    }
    @Override
    public String toString(){
        return "ERROR: THIS DESCRIPTION SHOULD NOT BE REACHABLE";
    }
}
