package DomainLayer.Stores;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Response;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NameCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.AndCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Discounts.BasicDiscount;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.RegisteredUser;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.AndConditionRecord;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.NameConditionRecord;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceBasicDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.logging.Logger;

public class Store {
    private static final AtomicInteger StoreID_GENERATOR = new AtomicInteger(0);
    private final AtomicInteger ProductID_GENERATOR = new AtomicInteger(0);
    private final AtomicInteger DiscountID_GENERATOR = new AtomicInteger(0);

    private int Id;
    private String Name;
    private Boolean Active;
    private History History;
    private final HashMap<String, Rating> rateMapForStore;
    private final ConcurrentHashMap<Integer, StoreProduct> products;
    /**
     * note: be default all policies must pass for the bag to be valid, any other logic must be made in a policy with an OR/XOR/WRAP logic condition
     */
    private final HashSet<Policy> storePolicies;
    private final HashMap<Integer,Discount> storeDiscounts;

    private Double Rate=0.0;
    private static final Logger logger=Logger.getLogger("Store logger");

    private LinkedList<RegisteredUser> listeners;

    public Store(String name) {
        storeDiscounts=new HashMap<>();
        storePolicies=new HashSet<>();
        rateMapForStore=new HashMap<>();
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        Id = StoreID_GENERATOR.getAndIncrement();
        Name = name;
        History = new History();
        products = new ConcurrentHashMap<>();
        listeners = new LinkedList<>();

        this.Active=true;
    }

    public void addNewListener(RegisteredUser storeowner){
        listeners.add(storeowner);
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
        StringBuilder s = new StringBuilder("Store Name: " + this.Name + "\nStore Rate: " + getRate() + "\n");
        for (StoreProduct i : products.values()) {
            s.append(" - Product Name:").append(i.getName()).append(", rating: ").append(i.getAverageRating()).append("\n");
        }
        return s.toString();
    }
    public boolean addPolicy(Policy policy){
        return storePolicies.add(policy);
    }
    public void CloseStore() {
        Active = false;
        NotifyOwners("The store "+Name+" is closed now.");
    }

    public void OpenStore() {
        Active = true;
        NotifyOwners("The store "+Name+" is open now.");
    }

    public void NewBuyNotification(String name){
        NotifyOwners(Name+" just bought from your shop ("+name+").");
    }

    private void NotifyOwners(String message){
        for (RegisteredUser listener : listeners) {
            listener.update(message);
        }
    }

    // history 6.4
    public LinkedList<Bag> GetStorePurchaseHistory() {
        return this.History.getShoppingBags();
    }



    public StoreProduct getProduct(CartProduct product){
        for(StoreProduct storeProduct:getProducts().values()){
            if(storeProduct.getName().equals(product.getName()) || storeProduct.getDescription().equals(product.getDescription())){
                return storeProduct;
            }
        }
        return null;
    }
    public Integer AddNewProduct( String productName, Double price, int Quantity, String category,String desc) {
        StoreProduct storeProduct = new StoreProduct(getNewProductId(), productName, price, category, Quantity,desc);
        products.put(storeProduct.getProductId(), storeProduct);
        logger.info("New product added to store. Product ID: " + storeProduct.getProductId());
        return storeProduct.getProductId();
    }

    /**
     * for tests
     * @param storeProduct --
     * @return --
     */
    public Integer AddNewProduct(StoreProduct storeProduct){
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
    public void ReduceProductQuantity(Integer productId, int quantity) {
        products.get(productId).ReduceQuantity(quantity);
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
    /**
     *
     * @param productFilters filters who the returned products have to pass
     * @return product list of all products who passed the filter in the store
     */
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

    /**
     * determines if a bag passes all store policies.
     * note: be default all policies must pass for the bag to be valid, any other logic must be made in a policy with an OR/XOR/WRAP logic condition
     * @param bag the bag we check
     * @return true if it passes, false otherwise.
     */
    public boolean passesPolicies(Bag bag){
        for(Policy policy:storePolicies)
            if(!policy.passesPolicy(bag))
                return false;
        return true;
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
    public void addDiscount(Discount discount){
        storeDiscounts.put(discount.getId(), discount);
    }
    public Discount addDiscount(ServiceBasicDiscount discount){
        return addDiscount(discount.conditionRecord,discount.description,discount.discountAmount);
    }
    public Discount addDiscount(ConditionRecord conditionRecord,String description,double discountAmount){
       return conditionRecord.accept(this,description,discountAmount);
    }
    public Discount addDiscount(NameConditionRecord nameConditionRecord,String description,double discountAmount){
        BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new NameCondition(nameConditionRecord.name()));
        storeDiscounts.put(discount.getId(),discount);
        return discount;
    }
    public Discount addDiscount(AndConditionRecord andConditionRecord, String description, double discountAmount){
        if(!storeDiscounts.containsKey(andConditionRecord.id1()) || !storeDiscounts.containsKey(andConditionRecord.id2()))
            throw new RuntimeException("ID ERROR WHILE ADDING 'AND' DISCOUNT");
        try {
            Condition c1 = ((BasicDiscount) (storeDiscounts.get(andConditionRecord.id1()))).getConditions();
            Condition c2 = ((BasicDiscount) (storeDiscounts.get(andConditionRecord.id2()))).getConditions();
            BasicDiscount discount=new BasicDiscount(description,DiscountID_GENERATOR.getAndIncrement(),discountAmount,new AndCondition(c1,c2));
            storeDiscounts.put(discount.getId(),discount);
            return discount;
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid discount types detected: multi discount detected while trying to build an 'AND conditioned discount.\nsystem message:\n"+e.getMessage());
        }
    }
    public Discount removeDiscount(Discount discount){
        return storeDiscounts.remove(discount.getId());
    }
    public double calcSaved(Bag bag){
        double totalSaved=0;
        for(Discount discount:storeDiscounts.values()){
            totalSaved+=discount.calcDiscountAmount(bag);
        }
        return totalSaved;
    }
    public HashMap<Discount,HashSet<CartProduct>> getValidProducts(Bag bag){
        HashMap<Discount,HashSet<CartProduct>> discounts=new HashMap<>();
        for(Discount discount:storeDiscounts.values()){
            HashSet<CartProduct> validProducts=discount.getValidProducts(bag);
            if(!validProducts.isEmpty())
                discounts.put(discount,validProducts);
        }
        return discounts;
    }


    public Collection<Policy> getPolicies() {
        return storePolicies;
    }

    public Collection<Discount> getDiscounts() {
        return storeDiscounts.values();
    }

    public HashMap<CartProduct,Double> getDiscountPerProduct(Bag bag) {
        if (bag == null)
            throw new NullPointerException("Null bag in discount calculation");
        HashMap<CartProduct,Double> totalMap = new HashMap<>();
        for (Discount discount : storeDiscounts.values()) {
            HashMap<CartProduct,Double> currentMap = discount.calcDiscountPerProduct(bag);
            for(CartProduct cartProduct:currentMap.keySet()){
                if(totalMap.get(cartProduct)!=null){
                    totalMap.put(cartProduct,totalMap.get(cartProduct)+currentMap.get(cartProduct));
                }
                else totalMap.put(cartProduct,currentMap.get(cartProduct));
            }
        }
        return totalMap;

    }
}
