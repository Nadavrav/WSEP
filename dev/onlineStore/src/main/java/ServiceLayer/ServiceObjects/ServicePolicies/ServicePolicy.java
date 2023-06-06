package ServiceLayer.ServiceObjects.ServicePolicies;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Policies.Policy;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public abstract class ServicePolicy {
    private final String description;
    private final int id;

    public ServicePolicy(Policy policy){
        description=policy.getDescription();
        this.id=policy.getId();
    }
    public ServicePolicy(String description,int id){
        this.description=description;
        this.id=id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
    public abstract Policy accept(ConditionFactory conditionFactory);
}
