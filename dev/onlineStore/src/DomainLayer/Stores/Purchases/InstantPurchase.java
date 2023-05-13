package DomainLayer.Stores.Purchases;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.SiteVisitor;

import java.util.Date;
import java.util.LinkedList;

public class InstantPurchase extends Purchase {

    SiteVisitor buyer;
    LinkedList<String> productsList;
    double totalAmount;
    Date purchaseDate;

    public InstantPurchase(SiteVisitor buyer,LinkedList productsList,double totalAmount){
        this.buyer = buyer;
        this.productsList = productsList;
        this.totalAmount = totalAmount;
        purchaseDate = new Date(); // Date() constructor gives the current date

    }

    @Override
    public String toString(){
        String output="";
        output += "Buyer : "+buyer.toString()+" Products : ";
        for (String s:productsList){
            output += s+"\n";
        }
        output += " Total amount : "+totalAmount;
        return output;
    }
}
