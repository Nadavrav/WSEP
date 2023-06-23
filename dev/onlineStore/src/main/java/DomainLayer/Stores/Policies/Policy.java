package DomainLayer.Stores.Policies;

import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NoCondition;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.BooleanAfterFilterCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Users.Bag;

public class Policy {
    private Condition condition;
    private final String description;
    private final int id;
    public Policy(String description,int id, Condition condition){
        this.condition=condition;
        this.description=description;
        this.id=id;
    }
    public Policy(String description,int id){
        this.description=description;
        this.condition=new NoCondition();
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public void SetCondition(BooleanCondition condition){
        this.condition=condition;
    }

    /**
     * checks if a policy passes the condition by checking if the condition returns an empoty list.
     * IMPORTANT NOTE: MAKE SURE ALL CONDITIONS ARE IN EITHER THE BooleanCondition PACKAGE OF IN LogicCondition PACKAGE. THE METHODS WON'T WORK IF FILTER CONDITIONS
     * ARE GIVEN TO IT! (don't have time to properly split condition types and test it, and don't want to risk breaking anything by doing it without testing)
     * @param bag bag to check the policy for.
     * @return boolean if the bag passes the policy.
     */
    public boolean passesPolicy(Bag bag){
        return !condition.passCondition(bag).isEmpty();
    }

    public String toString(){
        return description;
    }


}
