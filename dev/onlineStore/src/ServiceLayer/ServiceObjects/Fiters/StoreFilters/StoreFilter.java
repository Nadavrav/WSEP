package ServiceLayer.ServiceObjects.Fiters.StoreFilters;

import DomainLayer.Stores.Store;

public interface StoreFilter {
    boolean PassFilter(Store store);

}
