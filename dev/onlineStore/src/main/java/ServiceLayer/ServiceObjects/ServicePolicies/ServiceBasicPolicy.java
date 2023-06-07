package ServiceLayer.ServiceObjects.ServicePolicies;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Policies.Policy;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public class ServiceBasicPolicy extends ServicePolicy{
    private final ConditionRecord conditionRecord;

    public ServiceBasicPolicy(String description,int id,ConditionRecord conditionRecord) {
        super(description,id);
        this.conditionRecord=conditionRecord;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public Policy accept(ConditionFactory conditionFactory) {
        return conditionFactory.addPolicy(this);
    }

    public ConditionRecord getConditionRecord() {
        return conditionRecord;
    }
}
