package DomainLayer.Stores;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Response;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Purchases.Purchase;
import DomainLayer.Users.Bag;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.logging.Logger;

public class Store {
    private static final AtomicInteger StoreID_GENERATOR = new AtomicInteger(0);
    private final AtomicInteger ProductID_GENERATOR = new AtomicInteger(0);
    private int Id;
    private String Name;
    private Boolean Active;
    private History History;
    private final HashMap<String, Rating> rateMapForStore;
    private final ConcurrentHashMap<Integer, StoreProduct> products;
    private Double Rate=0.0;
    private static final Logger logger=Logger.getLogger("Store logger");
    
    public Store(String name) {
        rateMapForStore=new HashMap<>();
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        Id = StoreID_GENERATOR.getAndIncrement();
        Name = name;
        History = new History();
        products = new ConcurrentHashMap<>();

        this.Active=true;

    }

    private Integer getNewProductId() {
        return ProductID_GENERATOR.getAndIncrement();
    }

    /**
     * called when rating is edited, to update the average rating to reduce load on many rating getters
     */
    private void updateAvgRating() {
        double sum = 0;
        for (Rating rating:rateMapForStore.values()) {
            sum+=rating.getRating();
        }
        Rate = sum / rateMapForStore.size();
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
        StringBuilder s = new StringBuilder("Store Name is " + this.Name + "Store Rate is:" + getRate());
        for (StoreProduct i : products.values()) {
            s.append(" Product Name is :").append(i.getName()).append(" The rating is : ").append(i.getAverageRating()).append("\n");
        }
        return s.toString();
    }

    public void CloseStore() {
        Active = false;
    }

    // history 6.4
    public LinkedList<Bag> GetStorePurchaseHistory() {
        return this.History.getShoppingBags();
    }


    public Integer AddNewProduct( String productName, Double price, int Quantity, String category,String desc) {
        StoreProduct storeProduct = new StoreProduct(getNewProductId(), productName, price, category, Quantity,desc);
        products.put(storeProduct.getProductId(), storeProduct);
        logger.info("New product added to store. Product ID: " + storeProduct.getProductId());
        return storeProduct.getProductId();
    }

     public Response<?> RemoveProduct(Integer productID) {
        if (!products.containsKey(productID)) {
            logger.warning("Product not found in store. Product ID: " + productID);
            throw new IllegalArgumentException("There is no product in our products with this ID");
           // return new Response<>("There is no product in our products with this ID", true);
        }
        products.get(productID).notifyRemoval();
        products.remove(productID);
        logger.info("Product removed from store. Product ID: " + productID);
        return new Response<>("Product removed", false);
    }

    //2.2
   public LinkedList<StoreProduct> SearchProductByName(String Name) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            for (StoreProduct product : this.products.values()) {
                if (product.getName().equals (Name)) {
                    if (isInStock(product)) {
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

    private boolean isInStock(StoreProduct product) {
        return product.getQuantity() > 0;
    }

    public void updateAvgRating(Double rate) {
        Rate = rate;
    }

    public void addToStoreHistory(Bag b)
    {
        History.AddPurchasedShoppingBag(b);
    }

    public StoreProduct getProductByID(Integer productId) {
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
    public ConcurrentHashMap<Integer, StoreProduct> getProducts() {
        return products;
    }
    public void UpdateProductQuantity(Integer productId, int quantity) {
        products.get(productId).UpdateQuantity(quantity);
    }
    public void IncreaseProductQuantity(Integer productId, int quantity) {
        products.get(productId).IncreaseQuantity(quantity);
    }

    public void UpdateProductName(Integer productId, String name) {
        products.get(productId).setName(name);
    }

    public void UpdateProductPrice(Integer productId, double price) {
        products.get(productId).setPrice(price);
    }

    public void UpdateProductCategory(Integer productId, String category) {
        products.get(productId).setCategory(category);
    }

    public void UpdateProductDescription(Integer productId, String description) {
        products.get(productId).setDescription(description);
    }
    public List<StoreProduct> filterProducts(List<ProductFilter> productFilters){
        ArrayList<StoreProduct> filteredProducts=new ArrayList<>();
        for (StoreProduct product: products.values()) { //for each product in store
            boolean passedFilter = true;
            for (ProductFilter productFilter : productFilters) { //for each productFilter
                if (!productFilter.PassFilter(product)) { //product has to pass all productFilters
                    passedFilter = false;
                    break; //if we don't pass a productFilter, we exit from the productFilter loop-no need to check the rest
                }
            }
            if (passedFilter)
                filteredProducts.add(product);
        }
        return filteredProducts;
    }
   public void addRating(String userName ,double rate) {
        if(!rateMapForStore.containsKey(userName)){
            rateMapForStore.put(userName,new Rating(rate));
        }else{
            rateMapForStore.get(userName).setRating(rate);
        }
        updateAvgRating();
        logger.info("Rating added for user: " + userName + ", Rate: " + rate + ", Current store rate: " + this.Rate);
    }
    public void addRatingAndComment(String userName ,int rate,String comment) {
        if(!rateMapForStore.containsKey(userName)){
            rateMapForStore.put(userName,new Rating(rate,comment));
        }else {
            rateMapForStore.get(userName).setRating(rate);
            rateMapForStore.get(userName).addComment(comment);
        }
        updateAvgRating();
        logger.info("comment added for user: " + userName + ", comment: " + comment);
    }

    public LinkedList<Integer> getProductsID() {
        LinkedList<Integer> productsId = new LinkedList<>();
        for(StoreProduct product : products.values()){
            if(product.getQuantity()>0){
                productsId.add(product.getProductId());
            }
        }
        return productsId;
    }

    public HashMap<String, Double> getRatingList() {
        HashMap<String,Double> rates = new HashMap<>();
        for (String userName : rateMapForStore.keySet()){
            rates.put(userName,rateMapForStore.get(userName).getRating());
        }
        return rates;
    }

    public HashMap<String, String> getProductRatingList(int productId) {
        return products.get(productId).getProductRatingList();
    }
}
