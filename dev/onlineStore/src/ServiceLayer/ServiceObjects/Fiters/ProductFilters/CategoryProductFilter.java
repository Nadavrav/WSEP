package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class CategoryProductFilter implements ProductFilter {
    private final String category;

    public CategoryProductFilter(String category) {
        this.category = category;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getCategory().equals(category);
    }

}
