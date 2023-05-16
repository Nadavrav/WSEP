package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class CategoryProductFilter implements ProductFilter {
    private final String category;

    public CategoryProductFilter(String category) {
        this.category = category;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        if(category.equals(""))
            return true;
        return product.getCategory().contains(category);
    }

}
