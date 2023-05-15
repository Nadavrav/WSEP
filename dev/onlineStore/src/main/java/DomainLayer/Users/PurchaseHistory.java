package DomainLayer.Users;

import DomainLayer.Stores.Purchases.Purchase;

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

    @Override
    public String toString(){
        String output="";
        for (Purchase p:purchases) {
            output += p.toString()+"\n";
        }
        return output;
    }


}
