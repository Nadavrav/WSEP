package ServiceLayer.ServiceObjects.ServicePolicies;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Policies.Policy;

public class ServicePolicyInfo extends ServicePolicy{
    private final int id;

    public ServicePolicyInfo(Policy policy) {
        super(policy);
        this.id=policy.getId();
    }

    public ServicePolicyInfo(String description, int id) {
        super(description);
        this.id=id;
    }
    @Override
    public Policy accept(ConditionFactory conditionFactory) {
        throw new IllegalArgumentException("Illegal call to addPolicy with PolicyInfo");
    }
    public int getId() {
        return id;
    }
}
