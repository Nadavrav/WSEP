package DomainLayer.Stores;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;
import java.util.LinkedList;
import java.util.Map;
public class History {
    public LinkedList<Bag> getShoppingBags() {
        return ShoppingBags;
    }

    private LinkedList<Bag> ShoppingBags ;

    public History()
    {
        ShoppingBags = new LinkedList<Bag>();
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
}
