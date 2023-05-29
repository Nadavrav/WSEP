package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;
import java.util.Set;

public abstract class ManyDiscounts extends Discount{
    protected final Set<Discount> discounts;

    public ManyDiscounts(String description) {
        super(description);
        discounts=new HashSet<>();
    }
    public void addDiscount(Discount discount){
        discounts.add(discount);
    }
    public boolean removeDiscount(Discount discount){
        return discounts.remove(discount);
    }

    public ManyDiscounts(String description, Condition condition) {
        super(description, condition);
        discounts=new HashSet<>();
    }
    @Override
    public HashSet<CartProduct> getValidProducts(Bag bag) {
        HashSet<CartProduct> products=new HashSet<>();
        for(Discount discount:discounts)
            products.addAll(discount.getValidProducts(bag));
        return products;
    }
}
