package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class MinPriceProductFilter implements ProductFilter {
    private final Integer min;

    public MinPriceProductFilter(Integer min) {
        this.min = min;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        if(min<=0){
            return true;
        }
        return product.getPrice() >=min;
    }

}
