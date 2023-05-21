package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

public class BetweenDatesCondition implements Condition {
    private final LocalDate fromDate;

    private final LocalDate untilDate;

    public BetweenDatesCondition(int fromDay, int fromMonth, int fromYear,int untilDay, int untilMonth, int untilYear) {
        try{
            fromDate=LocalDate.of(fromYear,fromMonth,fromDay);
            untilDate=LocalDate.of(untilYear,untilMonth,untilDay);

        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }
    }
    public BetweenDatesCondition(int fromDay, int fromMonth,int untilDay, int untilMonth) {
        try{
            fromDate=LocalDate.of(LocalDate.now().getYear(), fromMonth,fromDay);
            untilDate=LocalDate.of(LocalDate.now().getYear(),untilMonth,untilDay);
        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }

    }
    public BetweenDatesCondition(int fromDay,int untilDay) {
        try{
            fromDate=LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), fromDay);
            untilDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(),untilDay);
        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        if(LocalDate.now().isBefore(untilDate) && LocalDate.now().isAfter(fromDate))
            return new HashSet<>(bag.getProducts());
        return new HashSet<>();
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof BetweenDatesCondition dateCondition) {
            return fromDate.equals(dateCondition.getFromDate()) && untilDate.equals(dateCondition.getUntilDate());
        }
        return false;
    }
}
