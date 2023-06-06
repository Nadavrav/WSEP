package ServiceLayer.ServiceObjects.ServicePolicies;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Policies.Policy;

public class ServicePolicyInfo extends ServicePolicy{
    public ServicePolicyInfo(Policy policy) {
        super(policy);
    }

    public ServicePolicyInfo(String description, int id) {
        super(description, id);
    }
    @Override
    public Policy accept(ConditionFactory conditionFactory) {
        throw new IllegalArgumentException("Illegal call to addPolicy with PolicyInfo");
    }
}
