package DomainLayer.Stores;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;
import java.util.LinkedList;
import java.util.Map;
public class History {

    private LinkedList<Bag> ShoppingBags ;

    public History()
    {
        ShoppingBags = new LinkedList<Bag>();
    }

    public LinkedList<Bag> getShoppingBags() {
        return ShoppingBags;
    }

    public void AddPurchasedShoppingCart(Cart shoppingCart){
        if(shoppingCart == null){
            throw new IllegalArgumentException("ShoppingCart can not be null");
        }
        else{
            Map<Integer,Bag> bags = shoppingCart.getBags();
            for (Bag b: bags.values()) {
                ShoppingBags.addLast(b);
            }
        }
    }

    public void AddPurchasedShoppingBag(Bag shoppingBag)
    {
        ShoppingBags.addLast(shoppingBag);
    }

    public int getDailyIncome(int day, int month, int year) {
        return 0;
    }
}
