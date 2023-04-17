package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

public class SiteVisitor {
    private Cart cart;
    private int visitorId;

    public SiteVisitor(int visitorId){
        this.visitorId=visitorId;
        cart = new Cart();
    }
    public SiteVisitor(SiteVisitor other){
        visitorId=other.getVisitorId();
        cart= other.getCart();
    }

    private Cart getCart() {
        return cart;
    }

    public int getVisitorId(){
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public void addProductToCart(int storeId, StoreProduct product) {
        cart.addProductToCart(storeId,product);
    }

    public String cartToString() {
        return cart.cartToString();
    }
}
