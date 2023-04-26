package ServiceLayer.ServiceObjects.Fiters;

import DomainLayer.Stores.StoreProduct;

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
