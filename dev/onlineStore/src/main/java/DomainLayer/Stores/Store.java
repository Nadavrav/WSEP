package DomainLayer.Stores;
import DAL.DTOs.StoreProductDTO;
import DAL.DTOs.StoreDTO;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Response;
import java.util.concurrent.locks.ReadWriteLock;
import DomainLayer.Stores.CallBacks.StoreCallbacks;
import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Purchases.InstantPurchase;
import DomainLayer.Users.Bag;
import DomainLayer.Users.RegisteredUser;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class Store {
    private static final AtomicInteger StoreID_GENERATOR = new AtomicInteger(0);
    private static final AtomicInteger ProductID_GENERATOR = new AtomicInteger(0);
    private final ConditionFactory conditionFactory = new ConditionFactory();

    private int Id;
    private String Name;
    private Boolean Active;
    private History History;
    private final HashMap<String, Rating> rateMapForStore;
    private final ConcurrentHashMap<Integer, StoreProduct> products;
    private final HashSet<Bid> pendingBids;
    private int ownersCount;
    private final HashMap<Bid,Integer> votingCounter;
    /**
     * note: be default all policies must pass for the bag to be valid, any other logic must be made in a policy with an OR/XOR/WRAP logic condition
     */
    private final HashMap<Integer, Policy> storePolicies;
    private final HashMap<Integer, Discount> storeDiscounts;
    private double Rate = 0.0;
    private static final Logger logger = Logger.getLogger("Store logger");
    private final LinkedList<RegisteredUser> listeners;
    private final LinkedList<RegisteredUser> ownerlisteners;

    private final HashSet<Integer> ownerIdSet;
    private final HashMap<Integer,Map<Bid,Boolean>> votingTracker;
    /**
     * each lock is for specific store functionality, used to lock read or read/write operations
     */
    private final ReadWriteLock ratingLock =new ReentrantReadWriteLock(true);
    private final ReadWriteLock productLock =new ReentrantReadWriteLock(true);
    private final ReadWriteLock policyLock =new ReentrantReadWriteLock(true);
    private final ReadWriteLock discountLock =new ReentrantReadWriteLock(true);
    private final ReadWriteLock bidLock =new ReentrantReadWriteLock(true);
    private final ReadWriteLock historyLock =new ReentrantReadWriteLock(true);




    public Store(String name) {
        ownerlisteners=new LinkedList<>();
        votingTracker=new HashMap<>();
        ownerIdSet=new HashSet<>();
        votingCounter=new HashMap<>();
        storeDiscounts = new HashMap<>();
        storePolicies = new HashMap<>();
        rateMapForStore = new HashMap<>();
        pendingBids = new HashSet<>();
        conditionFactory.setStore(this);
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        Id = StoreID_GENERATOR.getAndIncrement();
        Name = name;
        History = new History();
        products = new ConcurrentHashMap<>();
        listeners = new LinkedList<>();
        ownersCount=0;
        this.Active = true;
    }

    public Store(StoreDTO storeDTO) {
        ownerlisteners=new LinkedList<>();
        votingTracker=new HashMap<>();
        ownerIdSet=new HashSet<>();
        votingCounter=new HashMap<>();
        storeDiscounts = new HashMap<>();
        storePolicies = new HashMap<>();
        rateMapForStore = new HashMap<>();
        pendingBids = new HashSet<>();
        conditionFactory.setStore(this);
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        Id = storeDTO.getId();
        Name = storeDTO.getName();
        History = new History();
        products = new ConcurrentHashMap<>();
        for(StoreProductDTO productDTO: storeDTO.getProducts())
            products.put(productDTO.getProductId(),new StoreProduct(productDTO));
        listeners = new LinkedList<>();
        ownersCount=0;
        this.Active = true;
    }

    public static void setStoreIdCounter(Integer maxStoreId) {
        StoreID_GENERATOR.set(maxStoreId);
    }
    public static void setProductIdCounter(Integer maxProductId) {
        ProductID_GENERATOR.set(maxProductId);
    }

    public void addNewListener(RegisteredUser storeWorker) {
        listeners.add(storeWorker);
    }
    public void addNewOwnerListener(RegisteredUser storeOwner){
        ownerlisteners.add(storeOwner);
        ownersCount++;

    }
    public void documentOwner(Integer userId){
        ownerIdSet.add(userId);
        votingTracker.put(userId,new HashMap<>());

    }
    private Integer getNewProductId() {
        return ProductID_GENERATOR.getAndIncrement();
    }

    /**
     * called when rating is edited, to update the average rating to reduce the load on many rating getters
     * try lock - updateAvgRating should be part of another function for add/remove which already locks,
     * trylock just in case
     */
    private void updateAvgRating() {
        if(ratingLock.writeLock().tryLock())
            try {
                double sum = 0;
                for (Rating rating : rateMapForStore.values()) {
                    sum += rating.getRating();
                }
                Rate = sum / rateMapForStore.size();
            }
            finally {
                ratingLock.writeLock().unlock();
            }
    }

    public double getRate() {
        ratingLock.readLock().lock();
        try {
            return Rate;
        }
        finally {
            ratingLock.readLock().unlock();
        }
    }


    //2.1
    public String getInfo() throws Exception {
        if (!getActive()) {
            logger.warning("Store is closed: " + this.Name);
            throw new Exception(" this store is closed");
        }
        productLock.readLock().lock();
        try {
            StringBuilder s = new StringBuilder("Store Name: " + this.Name + "\nStore Rate: " + getRate() + "\n");
            for (StoreProduct i : products.values()) {
                s.append(" - Product Name:").append(i.getName()).append(", rating: ").append(i.getAverageRating()).append("\n");
            }
            return s.toString();
        }
        finally {
            productLock.readLock().unlock();
        }
    }

    public void addPolicy(Policy policy) {

        policyLock.writeLock().lock();
        try {
            storePolicies.put(policy.getId(), policy);
        } finally {
            policyLock.writeLock().unlock();
        }
    }

    public Policy addPolicy(ServicePolicy policy) {
        policyLock.writeLock().lock();
        try {
            Policy createdPolicy = conditionFactory.addPolicy(policy);
            storePolicies.put(createdPolicy.getId(), createdPolicy);
            return createdPolicy;
        } finally {
            policyLock.writeLock().unlock();
        }
    }

    /**
     * rare enough of an action to be done while fully while locking this object
     */
    public void CloseStore() {

        ratingLock.writeLock().lock();
        productLock.writeLock().lock();
        policyLock.writeLock().lock();
        discountLock.writeLock().lock();
        bidLock.writeLock().lock();
        try {
            Active = false;
            NotifyWorkers("The store " + Name + " is closed now.");
        } finally {
            ratingLock.writeLock().unlock();
            productLock.writeLock().unlock();
            policyLock.writeLock().unlock();
            discountLock.writeLock().unlock();
            bidLock.writeLock().unlock();
        }
    }

    public void OpenStore() {
        ratingLock.writeLock().lock();
        productLock.writeLock().lock();
        policyLock.writeLock().lock();
        discountLock.writeLock().lock();
        bidLock.writeLock().lock();
        try {
            Active = true;
            NotifyWorkers("The store " + Name + " is open now.");
        } finally {
            ratingLock.writeLock().unlock();
            productLock.writeLock().unlock();
            policyLock.writeLock().unlock();
            discountLock.writeLock().unlock();
            bidLock.writeLock().unlock();
        }
    }

    public void NewBuyNotification(String name) {
        NotifyWorkers(Name + " just bought from your shop (" + name + ").");
    }

    private void NotifyOwners(String message) {
        for (RegisteredUser listener : ownerlisteners) {
            listener.update(message);
        }
    }
    private void NotifyWorkers(String message) {
        for (RegisteredUser listener : listeners) {
            listener.update(message);
        }
    }

    /**
     * get history records on current store
      * @return pointer to bag enough from store, the returned object needs to be read only for
     * data correctness and concurrency.
     */
    public LinkedList<Bag> GetStorePurchaseHistory() {
        return this.History.getShoppingBags();
    }


    public StoreProduct getProduct(CartProduct product) {
        productLock.readLock().lock();
        try {
            for (StoreProduct storeProduct : getProducts().values()) {
                if (storeProduct.getName().equals(product.getName()) || storeProduct.getDescription().equals(product.getDescription())) {
                    return storeProduct;
                }
            }
            return null;
        } finally {
            productLock.readLock().unlock();
        }
    }

    public Integer AddNewProduct(String productName, Double price, int Quantity, String category, String desc) {
        productLock.writeLock().lock();
        try {
            StoreProduct storeProduct = new StoreProduct(getNewProductId(), productName, price, category, Quantity, desc);
            products.put(storeProduct.getProductId(), storeProduct);
            logger.info("New product added to store. Product ID: " + storeProduct.getProductId());
            return storeProduct.getProductId();
        } finally {
            productLock.writeLock().unlock();
        }
    }

    /**
     * for tests
     *
     * @param storeProduct --
     * @return --
     */
    public Integer AddNewProduct(StoreProduct storeProduct) {
        productLock.writeLock().lock();
        try {
            products.put(storeProduct.getProductId(), storeProduct);
            logger.info("New product added to store. Product ID: " + storeProduct.getProductId());
            return storeProduct.getProductId();
        } finally {
            productLock.writeLock().unlock();
        }
    }

    public Response<?> RemoveProduct(Integer productID) {
        productLock.writeLock().lock();
        try {
            if (!products.containsKey(productID)) {
                logger.warning("Product not found in store. Product ID: " + productID);
                throw new IllegalArgumentException("There is no product in our products with this ID");
                // return new Response<>("There is no product in our products with this ID", true);
            }
            products.get(productID).notifyRemoval();
            products.remove(productID);
            logger.info("Product removed from store. Product ID: " + productID);
            return new Response<>("Product removed", false);
        } finally {
            productLock.writeLock().unlock();
        }
    }

    //2.2
    public LinkedList<StoreProduct> SearchProductByName(String Name) throws Exception {
        LinkedList<StoreProduct> searchResults = new LinkedList<>();
        if (getActive()) {
            productLock.readLock().lock();
            try {
                for (StoreProduct product : this.products.values()) {
                    if (product.getName().equals(Name)) {
                        if (isInStock(product)) {
                            searchResults.add(product);
                        }
                    }
                }
                logger.info("Product search by name completed. Search keyword: " + Name + ", Number of results: " + searchResults.size());
                return searchResults;
            } finally {
                productLock.readLock().unlock();
            }
        }
        else {
            logger.warning("Search operation not allowed on an inactive store");
            throw new Exception("This store is closed");
        }
    }

    public void ReduceProductQuantity(Integer productId, int quantity) {
        products.get(productId).ReduceQuantity(quantity);
    }

    private boolean isInStock(StoreProduct product) {
        return product.getQuantity() > 0;
    }

    public void addToStoreHistory(InstantPurchase p) {
        History.AddPurchasedShoppingBag(p);
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

    /**
     * must enforce read-only on the returned list
     */
    public ConcurrentHashMap<Integer, StoreProduct> getProducts() {
        return products;
    }

    public void UpdateProductQuantity(Integer productId, int quantity) {
        productLock.readLock().lock();
        try {
            products.get(productId).UpdateQuantity(quantity);
        } finally {
            productLock.readLock().unlock();
        }
    }

    public void IncreaseProductQuantity(Integer productId, int quantity) {
        productLock.readLock().lock();

        try {
            products.get(productId).IncreaseQuantity(quantity);
        } finally {
            productLock.readLock().unlock();

        }
    }

    public void UpdateProductName(Integer productId, String name) {
        productLock.readLock().lock();
        try {
            products.get(productId).setName(name);
        } finally {
            productLock.readLock().unlock();

        }
    }

    public void UpdateProductPrice(Integer productId, double price) {
        productLock.readLock().lock();
        try {
            products.get(productId).setPrice(price);
        } finally {
            productLock.readLock().unlock();
        }
    }

    public void UpdateProductCategory(Integer productId, String category) {
        productLock.readLock().lock();
        try {
            products.get(productId).setCategory(category);
        } finally {
            productLock.readLock().unlock();

        }
    }

    public void UpdateProductDescription(Integer productId, String description) {
        productLock.readLock().lock();
        try {
            products.get(productId).setDescription(description);
        } finally {
            productLock.readLock().unlock();

        }
    }

    /**
     * determines if a bag passes all store policies.
     * note: be default all policies must pass for the bag to be valid, any other logic must be made in a policy with an OR/XOR/WRAP logic condition
     *
     * @param bag the bag we check
     * @return true if it passes, false otherwise.
     */
    public boolean passesPolicies(Bag bag) {
        for (Policy policy : storePolicies.values())
            if (!policy.passesPolicy(bag))
                return false;
        return true;
    }

    public void addRating(String userName, double rate) {
        if (!rateMapForStore.containsKey(userName)) {
            rateMapForStore.put(userName, new Rating(rate));
        } else {
            rateMapForStore.get(userName).setRating(rate);
        }
        updateAvgRating();
        logger.info("Rating added for user: " + userName + ", Rate: " + rate + ", Current store rate: " + this.Rate);
    }

    /**
     * @param productFilters filters who the returned products have to pass
     * @return product list of all products who passed the filter in the store
     */
    public List<StoreProduct> filterProducts(List<ProductFilter> productFilters) {
        productLock.readLock().lock();
        try {
            ArrayList<StoreProduct> filteredProducts = new ArrayList<>();
            for (StoreProduct product : products.values()) { //for each product in store
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
        } finally {
            productLock.readLock().unlock();
        }
    }

    public void addRatingAndComment(String userName, int rate, String comment) {
        try {
            if (!rateMapForStore.containsKey(userName)) {
                rateMapForStore.put(userName, new Rating(rate, comment));
            } else {
                rateMapForStore.get(userName).setRating(rate);
                rateMapForStore.get(userName).addComment(comment);
            }
            updateAvgRating();
        } finally {

        }
        logger.info("comment added for user: " + userName + ", comment: " + comment);
    }

    public LinkedList<Integer> getProductsID() {
        LinkedList<Integer> productsId = new LinkedList<>();
        for (StoreProduct product : products.values()) {
            if (product.getQuantity() > 0) {
                productsId.add(product.getProductId());
            }
        }
        return productsId;
    }

    public HashMap<String, Double> getRatingList() {
        ratingLock.readLock().lock();
        try {
            HashMap<String, Double> rates = new HashMap<>();
            for (String userName : rateMapForStore.keySet()) {
                rates.put(userName, rateMapForStore.get(userName).getRating());
            }
            return rates;
        } finally {
            ratingLock.readLock().unlock();
        }
    }

    public HashMap<String, String> getProductRatingList(int productId) {
        productLock.readLock().lock();
        try {
            return products.get(productId).getProductRatingList();
        } finally {
            productLock.readLock().unlock();
        }
    }

    public void addDiscount(Discount discount) {
        discountLock.writeLock().lock();
        try {
            storeDiscounts.put(discount.getId(), discount);
        } finally {
            discountLock.writeLock().unlock();
        }
    }

    public Discount addDiscount(ServiceDiscount serviceDiscount) {
        discountLock.writeLock().lock();
        try {
            Discount discount = conditionFactory.addDiscount(serviceDiscount);
            storeDiscounts.put(discount.getId(), discount);
            return discount;
        } finally {
            discountLock.writeLock().unlock();
        }
    }

    //    public Discount addDiscount(ServiceMultiDiscount serviceDiscount){
//        conditionFactory.addDiscount(serviceDiscount.getDiscountType(),serviceDiscount.getDiscounts(),serviceDiscount.description, serviceDiscount.id);
//        Discount discount= conditionFactory.addDiscount(serviceDiscount.conditionRecord,serviceDiscount.description,serviceDiscount.discountAmount);
//        storeDiscounts.put(discount.getId(),discount);
//        return discount;
//    }
    public boolean containsDiscount(int id) {
        discountLock.readLock().lock();
        try {
            return storeDiscounts.containsKey(id);
        } finally {
            discountLock.readLock().unlock();
        }
    }

    public Discount getDiscount(int id) {
        discountLock.readLock().lock();
        try {
            return storeDiscounts.get(id);
        } finally {
            discountLock.readLock().unlock();
        }
    }

    public Discount removeDiscount(int id) {
        discountLock.writeLock().lock();
        try {
            return storeDiscounts.remove(id);
        } finally {
            discountLock.writeLock().unlock();
        }
    }

//    public Discount removeDiscount(Discount discount) {
//        return storeDiscounts.remove(discount.getId());
//    }

    public double calcSaved(Bag bag) {
        discountLock.readLock().lock();
        try {
            double totalSaved = 0;
            for (Discount discount : storeDiscounts.values()) {
                totalSaved += discount.calcDiscountAmount(bag);
            }
            return totalSaved;
        } finally {
            discountLock.readLock().unlock();
        }
    }
/*
    public HashMap<Discount, HashSet<CartProduct>> getValidProducts(Bag bag) {
        HashMap<Discount, HashSet<CartProduct>> discounts = new HashMap<>();
        for (Discount discount : storeDiscounts.values()) {
            HashSet<CartProduct> validProducts = discount.getValidProducts(bag);
            if (!validProducts.isEmpty())
                discounts.put(discount, validProducts);
        }
        return discounts;
    }
*/

    public Collection<Policy> getPolicies() {
        policyLock.readLock().lock();
        try {
            return storePolicies.values();
        } finally {
            policyLock.readLock().unlock();
        }
    }

    public Collection<Discount> getDiscounts() {
        discountLock.readLock().lock();
        try {
            return storeDiscounts.values();
        } finally {
            discountLock.readLock().unlock();
        }
    }

    public HashMap<CartProduct, Double> getDiscountPerProduct(Bag bag) {
        if (bag == null)
            throw new NullPointerException("Null bag in discount calculation");
        HashMap<CartProduct, Double> totalMap = new HashMap<>();
        discountLock.readLock().lock();
        try {
            for (Discount discount : storeDiscounts.values()) {
                HashMap<CartProduct, Double> currentMap = discount.calcDiscountPerProduct(bag);
                for (CartProduct cartProduct : currentMap.keySet()) {
                    if (totalMap.get(cartProduct) != null) {
                        totalMap.put(cartProduct, totalMap.get(cartProduct) + currentMap.get(cartProduct));
                    }
                    else totalMap.put(cartProduct, currentMap.get(cartProduct));
                }
            }
        } finally {
            discountLock.readLock().unlock();
        }
        return totalMap;

    }

    public Policy getPolicy(int id) {
        policyLock.readLock().lock();
        try {
            return storePolicies.get(id);
        } finally {
            policyLock.readLock().unlock();
        }
    }

    public boolean containsPolicy(int id) {
        policyLock.readLock().lock();
        try {
            return storePolicies.containsKey(id);
        } finally {
            policyLock.readLock().unlock();
        }
    }


    public Policy removePolicy(int policyId) {
        policyLock.writeLock().lock();
        try {
            if (!storePolicies.containsKey(policyId))
                throw new RuntimeException("Id error: invalid policy id while trying to remove policy");
            return storePolicies.remove(policyId);
        } finally {
            policyLock.writeLock().unlock();
        }
    }

    public static void resetCounters() {
        StoreID_GENERATOR.getAndSet(0);
        ProductID_GENERATOR.getAndSet(0);

    }

    public int getDailyIncome(int day, int month, int year) {
        historyLock.readLock().lock();
        try {
            return getHistory().getDailyIncome(day, month, year);
        } finally {
            historyLock.readLock().unlock();
        }

    }
    public Collection<Bid> getPendingBids(){
        bidLock.readLock().lock();
        try {
            return pendingBids;
        } finally {
            bidLock.readLock().unlock();
        }
    }
    public Bid addBid(int productId, int amount, int newPrice, String userName, int userId) {
        bidLock.writeLock().lock();
        try {
            NotifyOwners(userName + " has submitted a new bid!");
            Bid bid = new Bid(productId, newPrice, userName, amount, userId,Id);
            pendingBids.add(bid);
            votingCounter.put(bid,0);
            for(Integer ownerId:ownerIdSet){
                votingTracker.get(ownerId).put(bid,false);
            }
            return bid;
        } finally {
            bidLock.writeLock().unlock();
        }
    }
    public Bid voteOnBid(int ownerId,int productId,RegisteredUser user,boolean vote) throws Exception{
        for(Bid bid:pendingBids) {
            if (bid.getProductId() == productId && bid.getUserName().equals(user.getUserName())) {
                if (!votingTracker.containsKey(ownerId) || !votingTracker.get(ownerId).containsKey(bid))
                    throw new RuntimeException("Invalid owner id or bid while voting on bid");
                if(votingTracker.get(ownerId).get(bid))
                    throw new Exception("User has already voted on bid and cannot vote again");
                if (vote) {
                    votingCounter.replace(bid,votingCounter.get(bid)+1);
                    if(votingCounter.get(bid)==ownersCount){
                        votingCounter.remove(bid);
                        votingTracker.get(ownerId).remove(bid);
                        acceptBid(bid,user);
                    }
                }
                else{
                    return rejectBid(bid,user,"your bid for "+products.get(bid.getProductId()).getName()+" failed to pass the vote between all store owners," +
                            "and was rejected");
                }
                return bid;
            }
        }
        throw new Exception("Bid for" +products.get(productId).getName()+" from "+user.getUserName()+" does not exist");
    }
    /*
    public void acceptBid(int productId, RegisteredUser user) {
        for (Bid bid : pendingBids) {
            if (bid.getProductId() == productId && bid.getUserId() == user.getVisitorId()) {
                user.addBidProduct(Id,bid, products.get(productId),generateStoreCallback());
                break;
            }
        }
    }
     */
    public void acceptBid(Bid bid,RegisteredUser user) {
        bidLock.readLock().lock();
        try {
            user.addBidProduct(Id,bid, products.get(bid.getProductId()),generateStoreCallback());
        } finally {
            bidLock.readLock().unlock();
        }
    }
    public Bid rejectBid(int productId, RegisteredUser user,String message) throws Exception{
        productLock.readLock().lock();
        try {
            if(!products.containsKey(productId)){
                throw new Exception("Invalid product id "+productId);
            }
        } finally {
            productLock.writeLock().unlock();
        }
        bidLock.writeLock().lock();
        try {
            for (Bid bid : pendingBids) {
                if (bid.getProductId() == productId && bid.getUserId() == user.getVisitorId()) {
                        pendingBids.remove(bid);
                        user.update(message);
                        return bid;
                }
            }
            throw new Exception("Bid for" +products.get(productId).getName()+" from "+user.getUserName()+" does not exist");
        } finally {
                bidLock.writeLock().unlock();
        }
    }
    public Bid rejectBid(Bid bid, RegisteredUser user,String message){

        bidLock.writeLock().lock();
        try {
            pendingBids.remove(bid);
        } finally {
            bidLock.writeLock().unlock();
        }
        user.update(message);
        return bid;
    }

    public void notifyOwnersAboutNewEmploymentRequests(String OriginAppointer, String appointed){
        NotifyOwners(OriginAppointer+" has opened a new appointment request to appoint "+appointed+" to be owner of the store "+getName());
    }

    public void notifyOwnersAboutNewAppointmentSucceed(String appointed){
        NotifyOwners(appointed+" is now owner of the store "+getName()+" since every owner of the store accepted the employment request");
    }

    public StoreCallbacks generateStoreCallback() {
        return new StoreCallbacks() {
            @Override
            public boolean checkStorePolicies(Bag bag) {
                return passesPolicies(bag);
            }

            @Override
            public double getDiscountAmount(Bag bag) {
                return calcSaved(bag);
            }

            @Override
            public HashMap<CartProduct, Double> getSavingsPerProduct(Bag bag) {
                return getDiscountPerProduct(bag);
            }
        };
    }





}
