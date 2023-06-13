package DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions;

import DomainLayer.Stores.Conditions.ConditionTypes.BooleanCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashSet;

public class DateCondition implements BooleanCondition {
    LocalDate date;

    public DateCondition(int day, int month, int year) {
        try{
            date=LocalDate.of(year,month,day);
        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }
    }
    public DateCondition(int day, int month) {
        try{
            date=LocalDate.of(LocalDate.now().getYear(), month,day);
        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }

    }
    public DateCondition(int day) {
        try{
            date=LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), day);
        }
        catch (DateTimeException e){
            throw new IllegalArgumentException("Invalid date arguments");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public HashSet<CartProduct> passCondition(Bag bag) {
        if(!date.equals(LocalDate.now()))
            return new HashSet<>();
        return new HashSet<>(bag.getProducts());
    }

    @Override
    public boolean equals(Condition condition) {
        if(condition instanceof DateCondition dateCondition) {
           return date.equals(dateCondition.getDate());
        }
        return false;
    }
    @Override
    public String toString(){
        return "TODO";
    }
}
