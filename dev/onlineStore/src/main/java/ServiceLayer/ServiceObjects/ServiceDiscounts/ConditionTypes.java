package ServiceLayer.ServiceObjects.ServiceDiscounts;


public enum ConditionTypes {
    //BOOLEAN CONDITIONS-CONDITIONS THAT RETURN TRUE/FALSE
    BetweenDatesCondition,
    DateCondition,
    LocalHourRangeCondition,
    MaxBagPriceCondition,
    MinBagPriceCondition,
    MaxTotalProductAmountCondition,
    MinTotalProductAmountCondition,
    //FILTER CONDITIONS-CONDITIONS THAT RETURN ALL PRODUCTS WHO PASS THEM
    CategoryCondition,
    NameCondition,
    MaxPriceCondition,
    MinPriceCondition,
    MaxQuantityCondition,
    MinQuantityCondition,
    NoCondition,
    //BOOLEAN LOGIC CONDITIONS- AND/OR/XOR BETWEEN TO BOOLEAN CONDITIONS.
    AndCondition,
    OrCondition,
    XorCondition,
    //FILTER LOGIC CONDITIONS- APPLIES LOGIC TO FILTERS
    MultiAndCondition,
    MultiOrCondition,
    //COMPOSITE CONDITIONS- BUILDS 2 CONDITION ONE AFTER ANOTHER
    IfBooleanThenFilter, //all products who pass a filter only if the boolean passes, else returns nothing (if { } AND { } OR { } then all {FILTERED} products get 30% discount
    BooleanToFilteredCondition //applies a boolean to all products who pass a filter (if all dairy products cost more than 200 NIS - filter dairy and apply min price condition)


}
