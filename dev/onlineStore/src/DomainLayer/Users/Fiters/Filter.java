package DomainLayer.Users.Fiters;

import DomainLayer.Stores.StoreProduct;

public interface Filter {
    public  boolean PassFilter(StoreProduct product);

}
