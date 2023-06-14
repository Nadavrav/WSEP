package DomainLayer.Stores;

public class Bid {
    private final int productId;
    private final double newPrice;
    private final String userName;
    private final int userId;
    private final int amount;

    public Bid(int productId, double newPrice, String userName,int amount, int userId) {
        this.productId = productId;
        this.newPrice = newPrice;
        this.userName = userName;
        this.userId = userId;
        this.amount=amount;
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
}
