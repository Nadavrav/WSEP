package ServiceLayer.ServiceObjects.Fiters.ProductFilters;

import DomainLayer.Stores.Products.StoreProduct;

public class NameProductFilter implements ProductFilter {
    private final String name;
    public NameProductFilter(String name){
        this.name=name;
    }
    @Override
    public boolean PassFilter(StoreProduct product) {
        if(name.equals(""))
            return true;
        return product.getName().equals(name);
    }

}
