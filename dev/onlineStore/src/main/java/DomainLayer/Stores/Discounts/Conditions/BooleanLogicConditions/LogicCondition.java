package DomainLayer.Stores.Discounts.Conditions.BooleanLogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;

public abstract class LogicCondition implements Condition {
    protected Condition firstCondition;
    protected Condition secondCondition;

    /**
     * boolean condition-either returns the whole bag if eligibility logic is true, returns an empty bag otherwise.
     * and/or filters can be done in multi or/and conditions. wrapper condition is an exception, but I am too lazy to separate it. (proper docs in wrapper)
     * @param firstCondition one of the conditions
     * @param secondCondition the second condition
     */
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
}
