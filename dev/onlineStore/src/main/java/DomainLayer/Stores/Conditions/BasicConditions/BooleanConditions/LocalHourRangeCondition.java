package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.time.LocalTime;
import java.util.HashSet;

public class LocalHourRangeCondition implements BooleanCondition {

    private final int lowerBoundaryHour;
    private final int lowerBoundaryMin;
    private final int upperBoundaryHour;
    private final int upperBoundaryMin;

    public LocalHourRangeCondition(int lowerBoundaryHour, int lowerBoundaryMin, int upperBoundaryHour, int upperBoundaryMin) {
        if(lowerBoundaryHour<0 || lowerBoundaryHour>23 || upperBoundaryHour<0 || upperBoundaryHour>23 || upperBoundaryMin <0 ||
            upperBoundaryMin>59 || lowerBoundaryMin <0 || lowerBoundaryMin>59)
            throw new IllegalArgumentException("Invalid hour parameters");
        this.lowerBoundaryHour = lowerBoundaryHour;
        this.lowerBoundaryMin = lowerBoundaryMin;
        this.upperBoundaryHour = upperBoundaryHour;
        this.upperBoundaryMin = upperBoundaryMin;
    }


    public int getUpperBoundaryMin() {
        return upperBoundaryMin;
    }
    public int getUpperBoundaryHour() {
        return upperBoundaryHour;
    }

    public int getLowerBoundaryMin() {
        return lowerBoundaryMin;
    }
    public int getLowerBoundaryHour() {
        return lowerBoundaryHour;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        int currMin= LocalTime.now().getMinute();
        int currHour=LocalTime.now().getHour();
        if(currHour>=lowerBoundaryMin && currHour<=upperBoundaryHour && currMin>=lowerBoundaryMin && currMin<=upperBoundaryMin)
            return new HashSet<>(bag.getProducts());
        else return new HashSet<>();
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof LocalHourRangeCondition localHourRangeCondition) {
            return localHourRangeCondition.getUpperBoundaryHour()==upperBoundaryHour && localHourRangeCondition.getUpperBoundaryMin()==upperBoundaryMin
                    && localHourRangeCondition.getLowerBoundaryHour()==lowerBoundaryHour && localHourRangeCondition.getLowerBoundaryMin()==lowerBoundaryMin;
        }
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
