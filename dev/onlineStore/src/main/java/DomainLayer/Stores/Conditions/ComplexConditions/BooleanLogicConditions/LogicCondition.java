package DomainLayer.Stores.Conditions.ComplexConditions.BooleanLogicConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;

public abstract class LogicCondition implements BooleanCondition {
    protected BooleanCondition firstCondition;
    protected BooleanCondition secondCondition;

    /**
     * boolean condition-either returns the whole bag if eligibility logic is true, returns an empty bag otherwise.
     * and/or filters can be done in multi or/and conditions. wrapper condition is an exception, but I am too lazy to separate it. (proper docs in wrapper)
     * @param firstCondition one of the conditions
     * @param secondCondition the second condition
     */
    public LogicCondition(BooleanCondition firstCondition,BooleanCondition secondCondition){
        this.firstCondition=firstCondition;
        this.secondCondition=secondCondition;
    }
    public BooleanCondition getFirstCondition() {
        return firstCondition;
    }

    public BooleanCondition getSecondCondition() {
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
