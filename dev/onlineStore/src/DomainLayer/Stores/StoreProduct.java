package DomainLayer.Stores;
import DomainLayer.Users.RegisteredUser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreProduct {
    private static AtomicInteger ProductID_GENERATOR = new AtomicInteger(0);
    public String productId;
    public String Name ;
    public Double Price ;
    public int Quantity;
    public String Category;
    public String KeyWords ;
    public  String Desc;



    public Map<RegisteredUser, Map<String,Rating>> RateMap;
    public Double Rate ;
    public int NumberOfRates ;


    public StoreProduct(int storeid,String name, double price, String category, int quantity, String kws,String desc)
    {
        productId = getNewProductId(storeid);
        Name = name;
        Price = price;
        Category = category;
        Quantity = quantity;
        KeyWords = kws ;
        Desc=desc;
        RateMap=new HashMap<>();
    }

    private String getNewProductId(int storeid) {
        return storeid+"-"+ProductID_GENERATOR.getAndIncrement();
    }


    public double getRate(String productId){
        double sum =0;
        for (RegisteredUser r : RateMap.keySet()){
          sum +=  RateMap.get(r).get(productId).getUserRateForProduct(r.getVisitorId());
        }
        Rate =  sum / RateMap.size();
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
    public void setKeyWords(String keyWords) {
        KeyWords = keyWords;
    }
    public String getName() {
        return Name;
    }
    public String getDesc() {
        return Desc;
    }
    public String getId() {
        return productId;
    }
    public void setDesc(String desc) {
        Desc = desc;
    }
    public String getProductId() {
        return productId;
    }
    public Map<RegisteredUser, Map<String, Rating>> getRateMap() {
        return RateMap;
    }

    public void setRateMap(Map<RegisteredUser, Map<String, Rating>> rateMap) {
        RateMap = rateMap;
    }
}
