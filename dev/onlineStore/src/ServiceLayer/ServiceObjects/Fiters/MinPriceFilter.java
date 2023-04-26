package ServiceLayer.ServiceObjects.Fiters;

import DomainLayer.Stores.StoreProduct;

public class MinPriceFilter implements Filter {
    private final Integer min;

    public MinPriceFilter(Integer min) {
        this.min = min;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getPrice() >=min;
    }

}
