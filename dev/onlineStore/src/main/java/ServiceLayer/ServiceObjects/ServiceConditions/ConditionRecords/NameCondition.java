package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

public class NameCondition implements ConditionRecord{
    public final String name;
    public NameCondition(String name){
        this.name=name;
    }
    @Override
    public ConditionRecord getInstance() {
        return this;
    }
}
