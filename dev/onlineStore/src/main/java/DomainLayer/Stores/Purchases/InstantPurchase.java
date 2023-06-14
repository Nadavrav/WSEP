package DomainLayer.Stores.Purchases;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.SiteVisitor;

import java.util.Date;
import java.util.LinkedList;

public class InstantPurchase extends Purchase {

    SiteVisitor buyer;
    Bag productsList;
    double totalAmount;
    Date purchaseDate;

    public InstantPurchase(SiteVisitor buyer,Bag productsList,double totalAmount){
        this.buyer = buyer;
        this.productsList = productsList;
        this.totalAmount = totalAmount;
        purchaseDate = new Date(); // Date() constructor gives the current date
    }

    public Bag getProductsList() {
        return productsList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getPurchaseDate(){
        return purchaseDate;
    }
    public String toString()
    {
        String output="Items that were purchased are:\n";
        output += productsList.toString()+"\n";
        output += "The total price was :"+totalAmount+" The date of the purchase was:"+purchaseDate.toString()+"\n";

        return output;
    }
}
