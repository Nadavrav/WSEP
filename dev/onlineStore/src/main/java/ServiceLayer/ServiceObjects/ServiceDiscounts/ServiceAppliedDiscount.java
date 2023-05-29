package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Products.CartProduct;

import java.util.HashSet;

public class ServiceAppliedDiscount {
    private final double totalSaved;
    private final double discountPercent;
    private final String discountDescription;

    private final HashSet<CartProduct> productsInDiscount;

    public ServiceAppliedDiscount(String discountDescription, double discountPercent, HashSet<CartProduct> productsInDiscount) {
        this.discountDescription=discountDescription;
        this.discountPercent = discountPercent;
        this.productsInDiscount = productsInDiscount;
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

    public HashSet<CartProduct> getProductsInDiscount() {
        return productsInDiscount;
    }
    public double calculateProductSavings(CartProduct cartProduct){
        return (cartProduct.getAmount()*cartProduct.getPrice())*(discountPercent/100);
    }
}
