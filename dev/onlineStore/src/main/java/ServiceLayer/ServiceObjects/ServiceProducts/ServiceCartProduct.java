package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.CartProduct;

public class ServiceCartProduct extends ServiceProduct{
    private final int amount;

    private final int id;
    public ServiceCartProduct(String name, Double price, String category, String description ,int amount,int id) {

        super(name, price, category, description);
        this.amount = amount;
        this.id = id;
    }
    public int getAmount() {
        return amount;
    }


    public ServiceCartProduct(CartProduct product) {
        super(product);
        this.amount=product.getAmount();
        this.id=product.getId();
    }

    @Override
    public String toString(){
        return "Name: "+getName()+" Description: "+getDescription()+" Category: "+getCategory()+" price per unit: "+getPrice()+" Amount: "+amount+
                " total price: "+getPrice()*amount;
    }
}
