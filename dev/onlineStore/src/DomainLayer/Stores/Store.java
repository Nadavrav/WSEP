package DomainLayer.Stores;
import DomainLayer.Response;
import DomainLayer.Users.Bag;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import DomainLayer.Users.RegisteredUser;

public class Store {
    private static AtomicInteger StoreID_GENERATOR = new AtomicInteger(0);
    private int Id;
    private String Name;
    private Boolean Active;
    private History History;
    private ConcurrentHashMap<String, Rating> RateMapForStore;
    private ConcurrentHashMap<String, StoreProduct> products;
    private Double Rate;


    public Store(String name) {
        Id = StoreID_GENERATOR.getAndIncrement();
        Name = name;
        History = new History();
        products = new ConcurrentHashMap<>();

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
        return this.History.ShoppingBags;
    }


    // ADD , UPDATE, REMOVE, SEARCH PRODUCT IS DONE ניהול מלאי 4.1
    public String AddNewProduct( String productName, Double price, int Quantity, String category,String desc) {
        StoreProduct storeProduct = new StoreProduct(Id, productName, price, category, Quantity,desc);
        products.put(storeProduct.getId(), storeProduct);
        return storeProduct.getId();

    }


    public Response<Object> RemoveProduct(String productID) {
        if (!products.containsKey(productID)) {
            return new Response<>("There is no product in our products with this ID", true);
        }
        products.remove(productID);
        return new Response<>("Product removed", false);
    }

    //2.2
    public LinkedList<StoreProduct> SearchProductByName(String Name) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            searchResults = new LinkedList<StoreProduct>();
            for (StoreProduct product : this.products.values()) {
                if (product.getName().equals(Name)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }

        }
        return searchResults;
    }
    public LinkedList<StoreProduct> SearchProductByCategory(String category) {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            searchResults = new LinkedList<StoreProduct>();
            for (StoreProduct product : this.products.values()) {
                if (product.getCategory().equals(category)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        }
        return searchResults;
    }
    public LinkedList<StoreProduct> SearchProductByKey(String key) {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            searchResults = new LinkedList<StoreProduct>();
            for (StoreProduct product : this.products.values()) {
                if (product.getName().contains(key)|| product.getCategory().contains(key)||product.getDescription().contains(key)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        }
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
    }
    public void addRatingAndComment(String userName ,int rate,String comment) throws Exception {
        if(!RateMapForStore.containsKey(userName)){
            RateMapForStore.put(userName,new Rating(rate,comment));
        }else {
            RateMapForStore.get(userName).addRate(rate);
            RateMapForStore.get(userName).addComment(comment);
        }
        setRate();
    }

}