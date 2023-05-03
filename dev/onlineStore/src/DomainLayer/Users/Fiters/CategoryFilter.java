package DomainLayer.Users.Fiters;

import DomainLayer.Stores.Products.StoreProduct;

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
