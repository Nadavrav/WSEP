package DomainLayer.Stores;

public class Bid {
    private final int productId;
    private  double newPrice;
    private final String userName;
    private final int userId;
    private final int amount;
    private final int storeId;
    public Bid(int productId, double newPrice, String userName,int amount, int userId,int storeId) {
        this.productId = productId;
        this.newPrice = newPrice;
        this.userName = userName;
        this.userId = userId;
        this.amount=amount;
        this.storeId=storeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }

    public String getUserName() {
        return userName;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    @Override
    public boolean equals(Object obj){
        if(obj==this)
            return true;
        else if(obj instanceof Bid b)
            return b.userId==userId && b.productId==productId && b.newPrice==newPrice && b.amount==amount;
        else return false;
    }
}
