package DomainLayer.Stores.Policies;

import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NoCondition;
import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Users.Bag;

public class Policy {
    private Condition condition;
    public Policy(Condition condition){
        this.condition=condition;
    }
    public Policy(){
        this.condition=new NoCondition();
    }
    public void SetCondition(Condition condition){
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
        return bag.getProducts().equals(condition.passCondition(bag));
    }
}
