package ServiceLayer.ServiceObjects.Fiters.ProductFilters;


import DomainLayer.Stores.Products.StoreProduct;

public class RatingProductFilter implements ProductFilter {
    private final int rating;
    public RatingProductFilter(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        if(rating>5 || rating<0)
           return true;
        return product.getAverageRating() > rating;
    }

}
