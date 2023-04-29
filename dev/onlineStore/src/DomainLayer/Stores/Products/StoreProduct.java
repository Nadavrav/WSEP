package DomainLayer.Stores.Products;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.Rating;


public class StoreProduct extends Product {

    private String productId;

    private int Quantity;
    private String Category;
    private double Rate;
    private Map<String, Rating> RateMap;
    private final Map<WeakReference<StoreProductObserver>, Object> observers = new WeakHashMap<>();
    private static final Logger logger=Logger.getLogger("StoreProduct logger");


    public StoreProduct(String productId,String name, double price, String category, int quantity,String desc)
    {
        super(name,price,category,desc);
       try {
           UniversalHandler.GetInstance().HandleError(logger);
           UniversalHandler.GetInstance().HandleInfo(logger);

        getStoreIdByProductId(productId);//Used to check if productId is valid
        if (name == null) {
            throw new NullPointerException("Product name cant be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price cant be negative");
        }
        if (category == null) {
            throw new NullPointerException("Product category cant be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Product quantity cant be negative");
        }
        if (desc == null) {
            throw new NullPointerException("Product desc cant be null");
        }
        this.productId = productId;
        Name = name;
        Price = price;
        Category = category;
        Quantity = quantity;
        Description =desc;
        RateMap=new HashMap<>();
       } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    private void setRate() {
        double sum = 0;
        for (Rating rating : RateMap.values()) {
            sum+=rating.getRate();
        }
        Rate = sum / RateMap.size();
    }

    public static int getStoreIdByProductId(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new NullPointerException("productId cannot be null or empty");
        }
        int index = productId.indexOf('-');
        if (index == -1) {
            throw new IllegalArgumentException("productId does not contain '-'");
        }
        if (index == 0) {
            throw new IllegalArgumentException("productId is invalid");
        }
         try {
            String storeId= productId.substring(0,index);
             return Integer.parseInt(storeId);
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse store ID from product ID: " + productId);
             throw new IllegalArgumentException(e);
        }
    }
    
    public static void isValidProductId(String productId) throws Exception {
        if (productId == null || productId.isEmpty()) {
            throw new NullPointerException("productId cannot be null or empty");
        }
        int index = productId.indexOf('-');
        if(index<1 || index>= productId.length())
            throw  new Exception("Invalid product ID");
        checkIfNumber(productId.substring(0,index));
        checkIfNumber(productId.substring(index+1));
        logger.info("valideProductId");
    }
    
    private static void checkIfNumber(String s) throws Exception {
        if(s==null||s.length()==0){
            logger.warning("null");
            throw new NullPointerException("Cant check null number");
        }
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
               logger.warning("Invalid product ID " + s);
                throw  new Exception("Invalid product ID");
            }
        }
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        Name = name;
        notifyObservers();
    }
    public void setQuantity(int quantity) {
        Quantity = quantity; //only relevant during purchase cart action, no need to notify cart products
    }
    public void setCategory(String category) {
        Category = category;
        notifyObservers();
    }

    public String getName() {
        return Name;
    }
    public Double getPrice() {
        return Price;
    }
    public String getDescription() {
        return Description;
    }
    public String getId() {
        return productId;
    }
    public void setDescription(String desc) {
        Description = desc;
    }
    public String getProductId() {
        return productId;
    }
    public Map<String, Rating> getRateMap() {
        return RateMap;
    }
    public void setPrice(Double price) {
        Price = price;
        notifyObservers();
    }

    public int getQuantity() {
        return Quantity;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        //only relevant when viewing product in its store, no need to notify cart products
        Rate = rate;
    }
    public double GetAverageRating(){
        double ratingSum=0;
        if (getNumberOfRates() == 0) {
            logger.warning("No ratings found for store.");
            return 0.0;
        }
        for(Rating rating:RateMap.values()){
            ratingSum+=rating.getRate();
        }
        return ratingSum/getNumberOfRates();
    }   

    public int getNumberOfRates() {
        return RateMap.keySet().size();
    }

    public void UpdateQuantity(int quantity) {
        if(quantity<0)
            throw new IllegalArgumentException("quantity cant be negative");
        setQuantity(quantity);
    }
    public void IncreaseQuantity(int quantity) {
        if(quantity<0)
            throw new IllegalArgumentException("quantity cant be negative");
        setQuantity(quantity+this.Quantity);
    }



    public String getCategory() {
        return Category;
    }

    public void addRating(String userName ,int rate) throws Exception {
        if(!RateMap.containsKey(userName)){
            RateMap.put(userName,new Rating(rate));
        }else{
            RateMap.get(userName).addRate(rate);
        }
        setRate();
    }
    public void addRatingAndComment(String userName ,int rate,String comment) throws Exception {
        if(!RateMap.containsKey(userName)){
            RateMap.put(userName,new Rating(rate,comment));
        }else {
            RateMap.get(userName).addRate(rate);
            RateMap.get(userName).addComment(comment);
        }
        setRate();
        logger.info("Rating and comment added for user: " + userName + ", Rate: " + rate + ", Comment: " + comment);

    }

    /**
     * Function to return the store product as a string for prints
     * @return a string for print
     */
    public String toStringForCart()
    {
        return "Product Id: "+this.productId+" ,Product Name: "+this.Name+" ,Product Price: "+this.Price;
    }

    /**
     * list of cart products that depend on the fields of this product
     * @param product product in a cart
     */
    public void addObserver(StoreProductObserver product) {
        observers.put(new WeakReference<>(product), new Object());
    }

    /**
     * notify all cart objects to update their fields to be equal to this store product,
     * called when store name,price,description or category are changed-can be expanded to include discounts and the like if needed
     * uses weak hash map, if I understood correctly, doesn't prevent CartProduct from being deleted by the garbage collector when all strong references to it
     * (e.g. references from a user's cart) when notifying cart products if a cart product was deleted it should be null,
     * stillInCart() is called too in order to double-check and not throw an error
     */
    public void notifyObservers() {
        for (Map.Entry<WeakReference<StoreProductObserver>, Object> entry : observers.entrySet()) {
            WeakReference<StoreProductObserver> observerRef = entry.getKey();
            StoreProductObserver observer = observerRef.get();
            if (observer==null || !observer.stillInCart()) {
                observers.remove(observerRef);
            } else {
                observer.updateFields(this);
            }
        }
    }
}
