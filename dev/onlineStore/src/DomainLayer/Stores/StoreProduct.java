package DomainLayer.Stores;
import DomainLayer.Users.RegisteredUser;

import java.util.HashMap;
import java.util.Map;

public class StoreProduct {




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

    public StoreProduct(String Id ,String name, double price, String category, int quantity, String kws,String desc)
    {
        productId = Id;
        Name = name;
        Price = price;
        Category = category;
        Quantity = quantity;
        KeyWords = kws ;
        Desc=desc;
        RateMap=new HashMap<>();
    }
    public StoreProduct(String Id ,String name, double price, int quantity, String kws,String desc)
    {
        productId = Id;
        Name = name;
        Price = price;
        Desc=desc;
        Quantity = quantity;
        KeyWords = kws ;
        RateMap=new HashMap<>();
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
    public static boolean isValidProductId(String productId) {
        int index = productId.indexOf('-');
        if(index<1 || index>= productId.length())
            return false;
        return checkIfNumber(productId.substring(0,index)) && checkIfNumber(productId.substring(index,productId.length()));
    }
    public static boolean checkIfNumber(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
                return false;
            }
        }
        return true;
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
