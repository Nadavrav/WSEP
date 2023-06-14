package DomainLayer.Stores;
import DomainLayer.Stores.Purchases.InstantPurchase;
import DomainLayer.Stores.Purchases.Purchase;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
public class History {

    private LinkedList<InstantPurchase> ShoppingBags ;

    public History()
    {
        ShoppingBags = new LinkedList<InstantPurchase>();
    }

    public LinkedList<Bag> getShoppingBags() {
        LinkedList<Bag> bagsList = new LinkedList<>();
        for (InstantPurchase IP:ShoppingBags)
        {
            bagsList.add(IP.getProductsList());
        }
        return bagsList;
    }

    public void AddPurchasedShoppingCart(Cart shoppingCart){
        if(shoppingCart == null){
            throw new IllegalArgumentException("ShoppingCart can not be null");
        }
        else{
            Map<Integer,Bag> bags = shoppingCart.getBags();
            for (Bag b: bags.values()) {
                //ShoppingBags.addLast(b);
            }
        }
    }


    public void AddPurchasedShoppingBag(InstantPurchase purchase)
    {
        ShoppingBags.addLast(purchase);
    }

    public int getDailyIncome(int day, int month, int year) {
        int totalAmount = 0;
        for (InstantPurchase IP : ShoppingBags) {
            Date purchaseDate = IP.getPurchaseDate();
            if(purchaseDate.getDay() == day && purchaseDate.getMonth() == month && purchaseDate.getYear() == year)
            {
                totalAmount += IP.getTotalAmount();
            }
        }
        return totalAmount;
    }
}
