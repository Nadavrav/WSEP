package ServiceLayer.ServiceObjects.ServicePolicies;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Policies.Policy;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public abstract class ServicePolicy {
    private final String description;

    public ServicePolicy(Policy policy){
        description=policy.getDescription();
    }
    public ServicePolicy(String description){
        this.description=description;
    }

    public String getDescription() {
        return description;
    }


    public abstract Policy accept(ConditionFactory conditionFactory);
}
