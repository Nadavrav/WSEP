package ServiceLayer.ServiceObjects;

import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceCart {
    private final Set<ServiceBag> bags;
    public ServiceCart(Cart cart){
        bags=new HashSet<>();
        for(Bag bag: cart.getBags().values()){
            bags.add(new ServiceBag(bag));
        }
    }

    public Set<ServiceBag> getBags() {
        return bags;
    }

    @Override
    public String toString () {
        String s = "";
        for (ServiceBag serviceBag:bags) {
            s += "Store Id : " + serviceBag.getStoreId() + "\n" + bags.toString();

        }
        return s;
    }
}
