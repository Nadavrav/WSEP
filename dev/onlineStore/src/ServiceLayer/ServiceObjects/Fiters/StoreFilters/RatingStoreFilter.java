package ServiceLayer.ServiceObjects.Fiters.StoreFilters;

import DomainLayer.Stores.Store;

public class RatingStoreFilter implements StoreFilter{
    public final double rating;

    public RatingStoreFilter(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean PassFilter(Store store) {
        if(rating<0 | rating>5)
            return false;
        return store.getRate() >= rating;
    }
}
