package ServiceLayer.ServiceObjects.Fiters;

import DomainLayer.Stores.StoreProduct;

public class CategoryFilter implements Filter{
    private final String category;

    public CategoryFilter(String category) {
        this.category = category;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getCategory().equals(category);
    }
}
