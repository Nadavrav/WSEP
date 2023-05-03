package DomainLayer.Users.Fiters;

import DomainLayer.Stores.Products.StoreProduct;

public class MaxPriceFilter implements Filter {
    private final Integer max;

    public MaxPriceFilter(Integer max) {
        this.max = max;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getPrice() <= max;
    }

}
