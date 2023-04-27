package DomainLayer.Stores;
import DomainLayer.Response;
import DomainLayer.Users.Bag;

import DomainLayer.Logging.UniversalHandler;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import DomainLayer.Users.RegisteredUser;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Store {
    private static AtomicInteger StoreID_GENERATOR = new AtomicInteger(0);
    private  AtomicInteger ProductID_GENERATOR = new AtomicInteger(0);
    private int Id;
    private String Name;
    private Boolean Active;
    private History History;
    private ConcurrentHashMap<String, Rating> RateMapForStore;
    private ConcurrentHashMap<String, StoreProduct> products;
    private Double Rate; 
    private static final Logger logger=Logger.getLogger("Store logger");

    public Store(String name) {
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            Id = StoreID_GENERATOR.getAndIncrement();
            Name = name;
            History = new History();
            products = new ConcurrentHashMap<>();
            Active=true;
      
    }
    private String getNewProductId() {
        return Id+"-"+ProductID_GENERATOR.getAndIncrement();
    }

    private double setRate() {
        double sum = 0;
        for (Rating rating:RateMapForStore.values()) {
            sum+=rating.getRate();
        }
        Rate = sum / RateMapForStore.size();
        return Rate;
    }
    public double getRate(){
        return Rate;
    }

    //2.1
   public String getInfo() throws Exception {

        if(!getActive()){
            logger.warning("Store is closed: " + this.Name);
            throw new Exception(" this store is closed");
        }
        String s = "Store Name is" + this.Name + "Store Rate is:" + getRate();
        for (StoreProduct i : products.values()) {
            s += " Product Name is :" + i.getName() + "The Rate is : " + i.getRate() + "/n";
        }
        return s;
    }

    public void CloseStore() {
        Active = false;
    }

    // history 6.4
    public LinkedList<Bag> GetStorePurchaseHistory() {
        return this.History.getShoppingBags();
    }


    // ADD , UPDATE, REMOVE, SEARCH PRODUCT IS DONE ניהול מלאי 4.1
    public String AddNewProduct( String productName, Double price, int Quantity, String category,String desc) {
        StoreProduct storeProduct = new StoreProduct(getNewProductId(), productName, price, category, Quantity,desc);
        products.put(storeProduct.getId(), storeProduct);
        logger.info("New product added to store. Product ID: " + storeProduct.getId());
        return storeProduct.getId();
    }




     public Response<Object> RemoveProduct(String productID) {
        if (!products.containsKey(productID)) {
            logger.warning("Product not found in store. Product ID: " + productID);
            return new Response<>("There is no product in our products with this ID", true);
        }
        products.remove(productID);
        logger.info("Product removed from store. Product ID: " + productID);
        return new Response<>("Product removed", false);
    }

    //2.2
   public LinkedList<StoreProduct> SearchProductByName(String Name) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<StoreProduct>();
        if (getActive()) {
            for (StoreProduct product : this.products.values()) {
                if (product.getName().equals (Name)) {
                    if (CheckProduct(product)) {
                        logger.info("New product added to store");
                        searchResults.add(product);
                    }
                }
            }

        }  else {
            logger.warning("Search operation not allowed on an inactive store");
            throw new Exception("This store is closed");
        }
        logger.info("Product search by name completed. Search keyword: " + Name + ", Number of results: " + searchResults.size());

        return searchResults;
    }
    
   public LinkedList<StoreProduct> SearchProductByCategory(String category) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<StoreProduct>();
        if (getActive()) {
            for (StoreProduct product : this.products.values()) {
                if (Objects.equals(product.getCategory(), category)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        }
        else {
            logger.warning("Search operation not allowed on an inactive store");
            throw new Exception("This store is closed");
        }
        logger.info("Product search by Category completed. Search keyword: " + Name + ", Number of results: " + searchResults.size());

        return searchResults;
    }
    
   public List<StoreProduct> SearchProductByKey(String key) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<StoreProduct>();
        if (getActive()) {
            for (StoreProduct product : this.products.values()) {
                if (product.getName().contains(key)|| product.getCategory().contains(key)||product.getDescription().contains(key)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        } else {
            logger.warning("Search operation not allowed on an inactive store");
            throw new Exception("This store is closed");
        }
        logger.info("Product search by Key completed. Search keyword: " + Name + ", Number of results: " + searchResults.size());
        return searchResults;
    }

    private boolean CheckProduct(StoreProduct product) {
        if (product.getQuantity() > 0) {
            return true;
        }
        return false;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }




    public StoreProduct getProductByID(String productId) {
        return products.get(productId);
    }

    public Boolean getActive() {
        return Active;
    }

    public Integer getID() {
        return Id;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

    public DomainLayer.Stores.History getHistory() {
        return History;
    }

    public void setHistory(DomainLayer.Stores.History history) {
        History = history;
    }
    public ConcurrentHashMap<String, StoreProduct> getProducts() {
        return products;
    }

    public void setProducts(ConcurrentHashMap<String, StoreProduct> products) {
        this.products = products;
    }

    public void addProduct(StoreProduct product) {
    }

    public void UpdateProductQuantity(String productID, int quantity) {
        products.get(productID).UpdateQuantity(quantity);
    }
    public void IncreaseProductQuantity(String productID, int quantity) {
        products.get(productID).IncreaseQuantity(quantity);
    }

    public void UpdateProductName(String productID, String name) {
        products.get(productID).setName(name);
    }

    public void UpdateProductPrice(String productID, double price) {
        products.get(productID).setPrice(price);
    }

    public void UpdateProductCategory(String productID, String category) {
        products.get(productID).setCategory(category);
    }

    public void UpdateProductDescription(String productID, String description) {
        products.get(productID).setDescription(description);
    }
   public void addRating(String userName ,int rate) throws Exception {
        if(!RateMapForStore.containsKey(userName)){
            RateMapForStore.put(userName,new Rating(rate));
        }else{
            RateMapForStore.get(userName).addRate(rate);
        }
        setRate();
        logger.info("Rating added for user: " + userName + ", Rate: " + rate + ", Current store rate: " + this.Rate);

    }
    public void addRatingAndComment(String userName ,int rate,String comment) throws Exception {
        if(!RateMapForStore.containsKey(userName)){
            RateMapForStore.put(userName,new Rating(rate,comment));
        }else {
            RateMapForStore.get(userName).addRate(rate);
            RateMapForStore.get(userName).addComment(comment);
        }
        setRate();
        logger.info("comment added for user: " + userName + ", comment: " + comment);
    }

}
