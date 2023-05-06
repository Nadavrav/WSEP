package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class DescriptionProductFilter implements ProductFilter {
    private final String description;

    public DescriptionProductFilter(String description) {
        this.description = description;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getDescription().equals(description);
    }

}
