package DomainLayer.Stores;

import DomainLayer.Users.SiteVisitor;

import java.util.Date;
import java.util.LinkedList;

public class InstantPurchase extends Purchase {

    SiteVisitor buyer;
    LinkedList<String> productsList;
    int totalAmount;
    Date purchaseDate;

    public InstantPurchase(SiteVisitor buyer,LinkedList productsList,int totalAmount){
        this.buyer = buyer;
        this.productsList = productsList;
        this.totalAmount = totalAmount;
        purchaseDate = new Date(); // Date() constructor gives the current date

    }
}
