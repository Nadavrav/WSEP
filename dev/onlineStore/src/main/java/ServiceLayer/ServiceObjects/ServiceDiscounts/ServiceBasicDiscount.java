package ServiceLayer.ServiceObjects.ServiceDiscounts;

import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public class ServiceBasicDiscount extends ServiceDiscount{
    public final ConditionRecord conditionRecord;
    public final double discountAmount;


    public ServiceBasicDiscount(String description,int discountAmount,ConditionRecord conditionRecord) {
        super(description,-1);
        this.discountAmount=discountAmount;
        this.conditionRecord = conditionRecord;
    }
    public ServiceBasicDiscount(String description,int id,int discountAmount,ConditionRecord conditionRecord) {
        super(description,id);
        this.discountAmount=discountAmount;
        this.conditionRecord = conditionRecord;
    }
}
