package DomainLayer.Stores.CallBacks;

import DomainLayer.Users.Bag;

public interface CheckStorePolicyCallback {
    boolean checkBag(Bag bag);
}
