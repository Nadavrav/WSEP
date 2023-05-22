package DomainLayer.Stores.Products;

import DomainLayer.Users.Cart;

public class CartProduct extends Product{
    int amount;
    //public CartProduct(String name, double price, String category, String desc) {
    //    super(name, price, category, desc);
    //    amount=1;
    //}

    /**
     *
     * @param storeProduct
     */
    public CartProduct(StoreProduct storeProduct){
        super(storeProduct);
        amount=1;
    }
    public int getAmount() {
        return amount;
    }
    public void add(int amount){
        this.amount+=amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void remove(int amount){
        if(amount> this.amount)
            throw new RuntimeException("Product amount can't be negative");
        this.amount=amount;
    }
    @Override
    public String toString(){
        return "Name: "+getName()+" Description: "+getDescription()+" Category: "+getCategory()+" price per unit: "+getPrice()+" Amount: "+getAmount()+
                " total price: "+getPrice()*getAmount();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CartProduct cartProduct ){
            return super.equals(cartProduct) && amount==cartProduct.getAmount();
        }
        return false;
    }
}
