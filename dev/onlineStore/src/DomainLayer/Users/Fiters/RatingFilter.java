package DomainLayer.Users.Fiters;


import DomainLayer.Stores.Products.StoreProduct;

public class RatingFilter implements Filter {
    private final int rating;
    public RatingFilter(int rating) {
        if(rating>5 || rating<0)
            throw new IllegalArgumentException("ERROR: Invalid Rating Filter Value");
        this.rating = rating;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getAverageRating() > rating;
    }

}
