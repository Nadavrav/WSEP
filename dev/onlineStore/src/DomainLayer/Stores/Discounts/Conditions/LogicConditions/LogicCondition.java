package DomainLayer.Stores.Discounts.Conditions.LogicConditions;

import DomainLayer.Stores.Discounts.Conditions.Condition;

public abstract class LogicCondition implements Condition {
    protected Condition firstCondition;
    protected Condition secondCondition;


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
