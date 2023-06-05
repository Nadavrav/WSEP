package ServiceLayer.ServiceObjects.ServiceDiscounts;


import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public class ServiceDiscount {
    public final String description;
    public final ConditionRecord conditionRecord;
    public final double discountAmount;


    public ServiceDiscount(String description,ConditionRecord conditionRecord,double discountAmount){
        this.description=description;
        this.conditionRecord=conditionRecord;
        this.discountAmount=discountAmount;
    }
}
