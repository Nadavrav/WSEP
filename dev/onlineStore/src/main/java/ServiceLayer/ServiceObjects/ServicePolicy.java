package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Policies.Policy;

public class ServicePolicy {
    private final String description;

    public ServicePolicy(Policy policy){
        description=policy.getDescription();
    }

    public String getDescription() {
        return description;
    }
}
