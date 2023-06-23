package DomainLayer.Stores.Products;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import DAL.DALService;
import DAL.DTOs.StoreProductDTO;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.Rating;



public class StoreProduct extends Product {

    private final Integer productId;

    private int Quantity;
    private String Category;
    private final Map<String, Rating> RateMap;
    private double avgRating;
    private final Map<WeakReference<StoreProductObserver>, Object> observers = new WeakHashMap<>();
    private static final Logger logger=Logger.getLogger("StoreProduct logger");


    public StoreProduct(Integer productId,String name, double price, String category, int quantity,String desc)
    {
        super(name,price,category,desc);
       try {
           UniversalHandler.GetInstance().HandleError(logger);
           UniversalHandler.GetInstance().HandleInfo(logger);
        } catch (Exception e) {
           throw new RuntimeException(e);
       }
        //getStoreIdByProductId(productId);//Used to check if productId is valid
        if (name == null) {
            throw new NullPointerException("Product name cant be null");
        }
        if(productId == null)
        {
            throw new NullPointerException("Product id cant be null");
        }
        if(productId < 0)
        {
            throw new IllegalArgumentException("Product id cant be negative");
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
        this.name = name;
        this.price = price;
        Category = category;
        Quantity = quantity;
        description =desc;
        RateMap=new HashMap<>();
    }

    public StoreProduct(StoreProductDTO productDTO) {
        super(productDTO.getName(), productDTO.getPrice(), productDTO.getCategory(), productDTO.getDesc());
        this.productId= productDTO.getProductId();
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        Category = productDTO.getCategory();
        Quantity = productDTO.getQuantity();
        description =productDTO.getDesc();
        RateMap=new HashMap<>();
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }
    public void setQuantity(int quantity) {
        Quantity = quantity; //only relevant during purchase cart action, no need to notify cart products
    }
    public void setCategory(String category) {
        Category = category;
        notifyObservers();
    }
    public String getCategory() {
        return Category;
    }

    public void setDescription(String desc) {
        description = desc;

    }
    public Integer getProductId() {
        return productId;
    }
    public Map<String, Rating> getRateMap() {
        return RateMap;
    }
    public void setPrice(Double price) {
        if(price <= 0)
            throw new IllegalArgumentException("Price cant be negative");
        this.price = price;
        notifyObservers();
    }

    public int getQuantity() {
        return Quantity;
    }
    public void addRatingAndComment(String userName ,double rate,String comment) {
        if(userName == null)
            throw new NullPointerException("Username cant be null");
        if(rate < 0)
            throw new IllegalArgumentException("Rating cannot be negative");
        if(rate > 5)
            throw new IllegalArgumentException("Rating cannot be above 5");
        if(!RateMap.containsKey(userName)){
            RateMap.put(userName,new Rating(rate,comment));
        }else {
            RateMap.get(userName).setRating(rate);
            RateMap.get(userName).addComment(comment);
        }
        updateAvgRatings();
        logger.info("Rating and comment added for user: " + userName + ", Rate: " + rate + ", Comment: " + comment);

    }
    public double getAverageRating(){
        return avgRating;
    }

    /**
     * updates store average rating to (ratings sum)/ratings amount.
     * called each time a rating is added,removed or edited
     */
    public void updateAvgRatings(){
        if(RateMap.isEmpty())
            avgRating= 0.0;
        double ratingSum=0,ratingCount=0;
        for(Rating rating:RateMap.values()){
            ratingSum+=rating.getRating();
            ratingCount++;
        }
        avgRating = (ratingSum / ratingCount);
    }

    /**
     * get number of rating for the store
     * @return number of ratings
     */
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




    /**
     * add or edit rating
     * @param userName username of the user rating the product
     * @param rating his rating, ranging from 0-5. supports both int and double (although the average will almost always be a double)
     */
    public void EditRating(String userName ,double rating) {
        if(!RateMap.containsKey(userName)){
            RateMap.put(userName,new Rating(rating));
        }
        else{
            RateMap.get(userName).setRating(rating);
        }
        updateAvgRatings();
    }


    /**
     * Function to return the store product as a string for prints
     * @return a string for print
     */
    public String toStringForCart()
    {
        return "Product Id: "+this.productId+" ,Product Name: "+this.name +" ,Product Price: "+this.price;
    }

    /**
     * list of cart products that depend on the fields of this product
     * @param product product in a cart
     */
    public void addObserver(StoreProductObserver product) {
        if (product == null) {
            return;//Might need to throw an exception instead
        }
        observers.putIfAbsent(new WeakReference<>(product), new Object());
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
            }
            else {
                observer.updateFields(this);
            }
        }
    }

    /**
     * Getter for observers
     * @return observers
     */
    public Map<WeakReference<StoreProductObserver>, Object> getObservers() {
        return observers;
    }
    /**
     * changes all store product's fields to signify a removed product, and notifies
     * all cart products copy these changes.
     * most important-price changes to 0, a customer will never pay for a removed product
     * even in case of a bug during purchasing.
     */
    public void notifyRemoval(){
        name ="NOTICE "+ name +"WAS REMOVED FROM THE STORE";
        description ="PRODUCT REMOVED FROM STORE";
        category="";
        price = 0.0;
        notifyObservers();
    }

    public HashMap<String,String> getProductRatingList() {
        HashMap<String,String> ratingList = new HashMap<>();

        for (String userName:RateMap.keySet()) {
            ratingList.put(userName,RateMap.get(userName).toString());
        }
        return ratingList;
    }

    public void ReduceQuantity(int quantity) {
        if(quantity<0)
            throw new IllegalArgumentException("quantity reduction cant be negative");
        setQuantity(this.Quantity-quantity);
    }


}
