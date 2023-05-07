package DomainLayer.Stores.Products;

import DomainLayer.Logging.UniversalHandler;

import java.util.logging.Logger;

public class Product implements StoreProductObserver {
    private static final Logger logger=Logger.getLogger("Product logger");
    protected String name;
    protected Double price;

    protected String description;
    protected String category;
    private boolean stillInCart;
    /**
     * called as super when a new store product is created in the store, with all the product's parameters
     * @param name product's name
     * @param price product's price
     * @param category product's category
     * @param desc product's description
     */
    public Product(String name, double price, String category,String desc) {

            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);

            if (name == null) {
                throw new NullPointerException("Product name cant be null");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Product price cant be negative");
            }
            if (category == null) {
                throw new NullPointerException("Product category cant be null");
            }
            if (desc == null) {
                throw new NullPointerException("Product desc cant be null");
            }
            stillInCart=false;
    }


    public Product(StoreProduct product) {
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        name =product.getName();
        price = product.getPrice();
        description =product.getDescription();
        category=product.getCategory();
        product.addObserver(this);
        stillInCart=true;
    }

    public String getName() {
        return name;
    }
    public void removedFromCart(){
        stillInCart=false;
    }
    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
    public void updateFields(Product product){
        name=product.getName();
        description=product.getDescription();
        category=product.getCategory();
        price=product.getPrice();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product eq ){
            return name.equals(eq.name) && description.equals(eq.description);
        }
        return false;
    }
    public boolean stillInCart(){
        return stillInCart;
    }
}
