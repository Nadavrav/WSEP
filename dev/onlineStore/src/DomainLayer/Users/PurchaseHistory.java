package DomainLayer.Users;

import DomainLayer.Stores.Purchase;

import java.util.LinkedList;

public class PurchaseHistory {
    private LinkedList<Purchase> purchases;

    public PurchaseHistory(){
        purchases = new LinkedList<>();
    }

    public void addPurchaseToHistory(Purchase purchase){
        purchases.add(purchase);
    }

    public LinkedList<Purchase> getPurchases(){
        return purchases;
    }


}
