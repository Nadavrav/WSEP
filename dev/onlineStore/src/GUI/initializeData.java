package GUI;

import DomainLayer.Stores.Store;
import DomainLayer.Users.Employment;
import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.SiteVisitor;

import static DomainLayer.Users.Role.StoreManager;
import static DomainLayer.Users.Role.StoreOwner;


public class initializeData {
    Store store1;
    Store store2;
    Store store3;

    public void initializeData() throws Exception {
        initializeStores();
        initializeEmployees();

    }

    private void initializeStores() {
         store1 =new Store("store1");
         store2 =new Store("store2");
         store3 =new Store("store3");
        store1.AddNewProduct("bamba",8.0,5,"Snack","peanut butter snack");
        store2.AddNewProduct("milk",10.0,20,"Dairy","milk from cow");
        store3.AddNewProduct("beer",15.0,15,"Alcohol","beer with 6% alcohol");
    }

    private void initializeEmployees() throws Exception {
        // give all store a store owner
        RegisteredUser registeredUser1 =new RegisteredUser("Majd","Momo1212");
        Employment employment1 = new Employment(registeredUser1,store1,StoreOwner);
        RegisteredUser registeredUser2 =new RegisteredUser("Nat","Nat1212");
        Employment employment2 = new Employment(registeredUser2,store2,StoreOwner);
        RegisteredUser registeredUser3 =new RegisteredUser("Mike","Momo1212");
        Employment employment3 = new Employment(registeredUser3,store3,StoreOwner);
        RegisteredUser registeredUser4 =new RegisteredUser("tot","tote1212");
        Employment employment4 = new Employment(registeredUser1,registeredUser4,store1,StoreManager);
        RegisteredUser registeredUser5 =new RegisteredUser("fot","fot1212");
        Employment employment5 = new Employment(registeredUser2,registeredUser5,store2,StoreManager);
        RegisteredUser registeredUser6 =new RegisteredUser("lot","lot1212");
        Employment employment6 = new Employment(registeredUser3,registeredUser6,store3,StoreManager);
        SiteVisitor siteVisitor1 = new SiteVisitor();
        SiteVisitor siteVisitor2 =new SiteVisitor();
    }

   public void show() throws Exception {
       System.out.println("Stores that might interest you:\n");
       System.out.println("1. "+ store1.getName()+ " About the store: "+ store1.getInfo() +"\n");
       System.out.println("2. "+ store2.getName()+ " About the store: "+ store2.getInfo() +"\n");
       System.out.println("3. "+ store3.getName()+ " About the store: "+ store3.getInfo() +"\n");

       System.out.println("Products that might interest you:\n");
       System.out.println("1. " + store1.getProducts().get("Bamba") + "\nAbout the Product: "
               + store1.getProducts().get("Bamba").getDescription() + "with Rate: "
               + store1.getProducts().get("Bamba").getRate() + "\n");
       System.out.println("2. "+ store2.getProducts().get("milk") + " About the Product: "
               + store2.getProducts().get("milk").getDescription() + "with Rate: "
               + store2.getProducts().get("milk").getRate() + "\n");
       System.out.println("3. "+ store3.getProducts().get("beer") + " About the Product: "
               + store3.getProducts().get("beer").getDescription() + "with Rate: "
               + store3.getProducts().get("beer").getRate() + "\n");

   }

}
