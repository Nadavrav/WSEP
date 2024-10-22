package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class MaxPriceProductFilter implements ProductFilter {
    private final Integer max;

    public MaxPriceProductFilter(Integer max) {
        this.max = max;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        if(max<=0){
            return true;
        }
        return product.getPrice() <= max;
    }

}
