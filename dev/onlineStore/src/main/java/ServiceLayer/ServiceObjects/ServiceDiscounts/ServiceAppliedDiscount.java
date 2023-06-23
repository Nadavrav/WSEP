package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Products.CartProduct;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceCartProduct;

import java.util.HashMap;
import java.util.HashSet;

public class ServiceAppliedDiscount {

    private final HashMap<ServiceCartProduct,Double> savingsPerProduct;


    /**
     * calculates the savings info after all store discounts are applied to a bag
     * note: this is info that includes many operations from many discounts,
     * so no description or percent is included since this isn't from one specific discount
     * @param savingsPerProduct amount of savings per product, meaning, money saved
     *                          per product after all store discounts are applied
     */
    public ServiceAppliedDiscount( HashMap<CartProduct,Double> savingsPerProduct) {
        this.savingsPerProduct=new HashMap<>();
        for(CartProduct cartProduct:savingsPerProduct.keySet())
            this.savingsPerProduct.put(new ServiceCartProduct(cartProduct),savingsPerProduct.get(cartProduct));
    }
    public double getDiscountPercent(ServiceCartProduct cartProduct){
        return (savingsPerProduct.get(cartProduct)/cartProduct.getCartProductPrice())*100;
    }
    public HashSet<ServiceCartProduct> getProductsInDiscount() {
        return new HashSet<>(savingsPerProduct.keySet());
    }
    public double getProductSavings(ServiceCartProduct cartProduct){
        return savingsPerProduct.get(cartProduct);
    }
    public double getProductPriceAfterDiscount(ServiceCartProduct cartProduct){
        return cartProduct.getCartProductPrice()-savingsPerProduct.get(cartProduct);
    }

    /**
     *  just a sum of all savings on all products
     * @return total amount of money saved when purchasing the bag
     */
    public double calcTotalSavings(){
        double sum=0;
        for(Double saving: savingsPerProduct.values())
            sum+=saving;
        return sum;
    }

}
