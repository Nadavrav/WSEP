package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Cart;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceCartProduct;

import java.util.HashSet;

public class ServiceAppliedDiscount {
    private final double totalSaved;
    private final double discountPercent;
    private final String discountDescription;

    private final HashSet<ServiceCartProduct> productsInDiscount;

    public ServiceAppliedDiscount(String discountDescription, double discountPercent, HashSet<CartProduct> productsInDiscount) {
        this.discountDescription=discountDescription;
        this.discountPercent = discountPercent;
        this.productsInDiscount=new HashSet<>();
        for(CartProduct cartProduct:productsInDiscount){
            this.productsInDiscount.add(new ServiceCartProduct(cartProduct));
        }
        double accumulatedTotalAmount = 0;
        for(CartProduct product:productsInDiscount){
            accumulatedTotalAmount += product.getAmount() * (product.getPrice() * (discountPercent / 100));
        }
        totalSaved = accumulatedTotalAmount;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public double getTotalSaved() {
        return totalSaved;
    }

    public HashSet<ServiceCartProduct> getProductsInDiscount() {
        return productsInDiscount;
    }
    public double calculateProductSavings(ServiceCartProduct cartProduct){
        return (cartProduct.getAmount()*cartProduct.getPrice())*(discountPercent/100);
    }
}
