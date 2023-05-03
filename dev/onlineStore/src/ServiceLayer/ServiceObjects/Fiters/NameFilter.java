package ServiceLayer.ServiceObjects.Fiters;

import DomainLayer.Stores.Products.StoreProduct;

public class NameFilter implements Filter{
    private final String name;
    public NameFilter(String name){
        this.name=name;
    }
    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getName().equals(name);
    }

}
