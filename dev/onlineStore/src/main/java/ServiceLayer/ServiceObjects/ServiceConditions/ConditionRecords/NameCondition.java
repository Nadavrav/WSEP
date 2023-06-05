package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

public record NameCondition(String name) implements ConditionRecord {
    @Override
    public ConditionRecord getInstance() {
        return this;
    }
}
