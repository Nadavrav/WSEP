package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NoCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;

import java.util.HashMap;
import java.util.HashSet;

public class BasicDiscount extends Discount {
    protected Condition condition;

    /**
     * 30 for a 30% discount, 55.5 for a 55.5% discount etc...
     */
    private double discount;

    public BasicDiscount(String description,int id, double discount){
        super(description,id);
        this.discount=discount;
        this.condition=new NoCondition();
    }
    public BasicDiscount(String description,int id, double discount, Condition condition){
        super(description,id);
        this.discount=discount;
        this.condition=condition;
    }
    public void SetCondition(Condition condition){
        this.condition=condition;
    }
    public Condition getConditions() {
        return condition;
    }


    @Override
    public double calcDiscountAmount(Bag bag){
        if(bag==null)
            throw new NullPointerException("Null bag in discount calculation");
        double saved=0.0;
            for (CartProduct product : condition.passCondition(bag)) {
                saved += product.getAmount() * (product.getPrice() * (discount / 100)); //for price 40 with a 25% discount, 40*(25/100)=10 was saved by the discount
            }
            return saved;

    }
    @Override
    public HashMap<CartProduct,Double> calcDiscountPerProduct(Bag bag){
        if(bag==null)
            throw new NullPointerException("Null bag in discount calculation");
        HashMap<CartProduct,Double> map=new HashMap<>();
        for (CartProduct product : condition.passCondition(bag)) {
            map.put(product,product.getAmount() * (product.getPrice() * (discount / 100))); //for price 40 with a 25% discount, 40*(25/100)=10 was saved by the discount
        }
        return map;
    }

    public double getDiscount() {
        return discount;
    }
    public HashSet<CartProduct> getValidProducts(Bag bag) {
        return condition.passCondition(bag);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    @Override
    public String toString(){
        return this.description;
//        return "A "+discount+"% discount "+condition;
    }



}
