package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Products.StoreProduct;

public class ServiceProduct {

    private final String Name;
    private final double Price;
    private final String Category;
    private final  String Description;
    private final double Rating;

    public ServiceProduct(String name, Double price, String category, String description, double rating) {
        Name = name;
        Price = price;
        Category = category;
        Description = description;
        Rating = rating;
    }
    public ServiceProduct(StoreProduct product){
        Name = product.getName();
        Price = product.getPrice();
        Category = product.getCategory();
        Description = product.getDescription();
        Rating = product.getRate();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ServiceProduct eq){
            return Name.equals(eq.Name) && Description.equals(eq.Description);
        }
        return false;
    }
}
