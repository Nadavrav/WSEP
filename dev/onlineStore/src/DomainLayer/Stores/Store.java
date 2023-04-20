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
    public int Id;
    public String Name;
    public Boolean Active;
    public History History;
    public ConcurrentHashMap<RegisteredUser, Rating> RateMapForStore;
    public ConcurrentHashMap<String, StoreProduct> products;
    public Double Rate;


    public Store(String name) {
        Id = StoreID_GENERATOR.getAndIncrement();
        Name = name;
        History = new History();
        products = new ConcurrentHashMap<>();

    }

    public double getRate() {
        double sum = 0;
        for (Rating r : RateMapForStore.values()) {
            for (int i =0 ;i<RateMapForStore.size();i++) {
                RegisteredUser x =RateMapForStore.keys().nextElement();
                sum = +r.getUserRateForStore(x.getVisitorId());
            }
        }
        Rate = sum / RateMapForStore.size();
        return Rate;
    }

    //2.1
    public String getInfo() {
        String s = "Store Name is" + this.Name + "Store Rate is:" + getRate();
        for (StoreProduct i : products.values()) {
            s += " Product Name is :" + i.getName() + "The Rate is : " + i.getRate(i.getProductId()) + "/n";

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
    public Response<?> AddNewProduct(String productID, String productName, Double price, int Quantity, String category,String keyWords,String desc) {
        StoreProduct storeProduct = new StoreProduct(productID, productName, price, category, Quantity, keyWords,desc);
        products.put(productID, storeProduct);
        products.get(productID).Quantity++;
        return new Response<>("Success", false);
    }

    public Response<Object> EditProduct(String productID, String Id, String name, double price, String category, int quantity, String kws,String desc) {

        if (products.containsKey(productID)) {
            products.get(productID).setProductId(Id);
            products.get(productID).setCategory(category);
            products.get(productID).setName(name);
            products.get(productID).setQuantity(quantity);
            products.get(productID).setKeyWords(kws);
            products.get(productID).setDesc(desc);
            return new Response<>("there is no product with this ID to update", false);

        } else {
            return new Response<>("there is no product with this ID to update", true);
        }

    }

    public Response<Object> RemoveProduct(String productID) {
        StoreProduct sp = null;
        if (products.containsKey(productID)) {
            sp = products.get(productID);
        } else {
            return new Response<>("there is no product in our products with this ID", true);
        }
        products.remove(productID, sp);
        products.get(productID).Quantity++;
        return new Response<>("product Removed", false);
    }

    //2.2
    public LinkedList<StoreProduct> SearchProductByName(String Name) {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            searchResults = new LinkedList<StoreProduct>();
            for (StoreProduct product : this.products.values()) {
                if (product.Name == Name) {
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
                if (product.Category == category) {
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
                if (product.Name.contains(key)|| product.Category.contains(key)) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        }
        return searchResults;
    }

    private boolean CheckProduct(StoreProduct product) {
        if (product.Quantity > 0) {
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

}