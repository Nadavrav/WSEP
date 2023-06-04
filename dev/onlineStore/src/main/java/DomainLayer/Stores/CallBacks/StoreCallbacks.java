package DomainLayer.Stores.CallBacks;

import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashMap;
import java.util.HashSet;

public interface StoreCallbacks {
    boolean checkStorePolicies(Bag bag);
    double getDiscountAmount(Bag bag);
    public HashMap<CartProduct,Double> getSavingsPerProduct(Bag bag);
}
