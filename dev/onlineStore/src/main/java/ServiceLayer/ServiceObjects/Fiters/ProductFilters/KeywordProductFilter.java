package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class KeywordProductFilter implements ProductFilter{
    public final String keyword;

    public KeywordProductFilter(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        if(keyword.equals(""))
            return true;
        return product.getName().contains(keyword) ||
                product.getDescription().contains(keyword) ||
                product.getCategory().contains(keyword) ||
                product.getPrice().toString().contains(keyword);
    }
}
