package ServiceLayer.ServiceObjects.Fiters;

import DomainLayer.Stores.Products.StoreProduct;

public interface Filter {
    public  boolean PassFilter(StoreProduct product);

}