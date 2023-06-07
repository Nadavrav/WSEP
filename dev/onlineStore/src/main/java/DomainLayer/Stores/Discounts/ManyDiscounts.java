package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

import java.util.HashSet;
import java.util.Set;

public abstract class ManyDiscounts extends Discount{
    protected final Set<Discount> discounts;

    public ManyDiscounts(String description,int id) {
        super(description,id);
        discounts=new HashSet<>();
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void addDiscount(Discount discount){
        discounts.add(discount);
    }
    public boolean removeDiscount(Discount discount){
        return discounts.remove(discount);
    }

    @Override
    public HashSet<CartProduct> getValidProducts(Bag bag) {
        HashSet<CartProduct> products=new HashSet<>();
        for(Discount discount:discounts)
            products.addAll(discount.getValidProducts(bag));
        return products;
    }
}
