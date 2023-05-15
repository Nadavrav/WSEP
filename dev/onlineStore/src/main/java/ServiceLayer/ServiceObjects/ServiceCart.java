package ServiceLayer.ServiceObjects;

import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;

import java.util.ArrayList;
import java.util.List;

public class ServiceCart {
    private final List<ServiceBag> bags;

    public ServiceCart(Cart cart){
        bags=new ArrayList<>();
        for(Bag bag: cart.getBags().values()){
            bags.add(new ServiceBag(bag));
        }
    }

    public List<ServiceBag> getBags() {
        return bags;
    }
}
