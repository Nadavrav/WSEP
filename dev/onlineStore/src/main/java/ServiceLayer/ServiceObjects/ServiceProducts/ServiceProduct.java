package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;

public class ServiceProduct {

    protected final String Name;
    protected final double Price;
    protected final String Category;
    protected final  String Description;

    public ServiceProduct(String name, Double price, String category, String description) {
        Name = name;
        Price = price;
        Category = category;
        Description = description;
    }
    public ServiceProduct(StoreProduct product){
        Name = product.getName();
        Price = product.getPrice();
        Category = product.getCategory();
        Description = product.getDescription();
    }
    public ServiceProduct(CartProduct product){
        Name = product.getName();
        Price = product.getPrice();
        Category = product.getCategory();
        Description = product.getDescription();
    }
    public String getCategory() {
        return Category;
    }

    public String getName() {
        return Name;
    }

    public double getPrice() {
        return Price;
    }

    public String getDescription() {
        return Description;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ServiceProduct eq){
            return Name.equals(eq.Name) && Description.equals(eq.Description);
        }
        return false;
    }


}
