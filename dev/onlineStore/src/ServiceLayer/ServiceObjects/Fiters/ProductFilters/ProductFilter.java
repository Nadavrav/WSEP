package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public interface ProductFilter {
    boolean PassFilter(StoreProduct product);

}
