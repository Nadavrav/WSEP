package DomainLayer.Stores.Products;

public class BidProduct extends CartProduct{
    private final double newPrice;

    public BidProduct(StoreProduct storeProduct,double newPrice) {
        super(storeProduct);
        this.newPrice=newPrice;
    }

    public BidProduct(StoreProduct storeProduct, int amount,double newPrice) {
        super(storeProduct, amount);
        this.newPrice=newPrice;
    }
    @Override
    public Double getPrice() {
        return newPrice;
    }
    public Double getOldPrice(){
        return super.getPrice();
    }
}
