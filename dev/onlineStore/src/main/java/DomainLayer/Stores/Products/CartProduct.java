package DomainLayer.Stores.Products;

import DomainLayer.Users.Cart;

public abstract class CartProduct extends Product{
    private int amount;
    private final int id;
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
        id= storeProduct.getProductId();
        amount=1;
    }

    public int getId() {
        return id;
    }

    public CartProduct(StoreProduct storeProduct,int amount){
        super(storeProduct);
        this.amount=amount;
        this.id=storeProduct.getProductId();
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
    @Override
    public abstract String getDescription();
    @Override
    public abstract Double getPrice();
}
