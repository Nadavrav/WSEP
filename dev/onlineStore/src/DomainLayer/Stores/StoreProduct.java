package DomainLayer.Stores;

import java.util.HashMap;
import java.util.Map;

public class StoreProduct {

    private String productId;
    private String Name;
    private Double Price;
    private int Quantity;
    private String Category;
    private  String Description;
    private double Rate;
    private Map<String,Rating> RateMap;
    public int NumberOfRates;



    public StoreProduct(String productId,String name, double price, String category, int quantity,String desc)
    {
        this.productId = productId;
        Name = name;
        Price = price;
        Category = category;
        Quantity = quantity;
        Description =desc;
        RateMap=new HashMap<>();
    }




    private double setRate() {
        double sum = 0;
        for (Rating rating : RateMap.values()) {
            sum+=rating.getRate();
        }
        Rate = sum / RateMap.size();
        return Rate;
    }

    public static int getStoreIdByProductId(String productId) {
        int index = productId.indexOf('-');
        String storeId= productId.substring(0,index);
        return Integer.parseInt(storeId);

    }
    public static void isValidProductId(String productId) throws Exception {
        int index = productId.indexOf('-');
        if(index<1 || index>= productId.length())
            throw  new Exception("Invalid product ID");
        checkIfNumber(productId.substring(0,index));
        checkIfNumber(productId.substring(index+1));
    }
    private static void checkIfNumber(String s) throws Exception {
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
                throw  new Exception("Invalid product ID");
            }
        }
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        Name = name;
    }
    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
    public void setCategory(String category) {
        Category = category;
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
    }

    public int getQuantity() {
        return Quantity;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }
    public double GetAverageRating(){
        double ratingSum=0;
        for(Rating rating:RateMap.values()){
            ratingSum+=rating.getRate();
        }
        return ratingSum/getNumberOfRates();
    }

    public int getNumberOfRates() {
        return NumberOfRates;
    }

    public void setNumberOfRates(int numberOfRates) {
        NumberOfRates = numberOfRates;
    }

    public void setRateMap(Map<String, Rating> rateMap) {
        RateMap = rateMap;
    }

    public void UpdateQuantity(int quantity) {
        setQuantity(quantity);
    }
    public void IncreaseQuantity(int quantity) {
        setQuantity(quantity+this.Quantity);
    }

    public void setPrice(double price) {
        this.Price=price;
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
    }

    /**
     * Function to return the store product as a string for prints
     * @return a string for prints
     */
    public String toStringForCart()
    {
        return "Product Id: "+this.productId+" ,Product Name: "+this.Name+" ,Product Price: "+this.Price;
    }
}
