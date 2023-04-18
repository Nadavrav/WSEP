package DomainLayer.Stores;
import DomainLayer.Users.RegisteredUser;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class StoreProduct {



    public String productId;
    public String Name ;
    public Double Price ;
    public int Quantity;
    public String Category;
    public LinkedList<String> KeyWords ;

    public ConcurrentHashMap<RegisteredUser,Rating> RateMap;
    public Double Rate ;
    public int NumberOfRates ;

    public StoreProduct(String Id ,String name, double price, String category, int quantity, LinkedList<String> kws)
    {
        productId = Id;
        Name = name;
        Price = price;
        Category = category;
        Quantity = quantity;
        KeyWords = kws == null ? new LinkedList<String>() : kws;
    }


    
    public double getRate(){
        double sum =0;
        for (Rating r : RateMap.values()){
            sum =+ r.getUserRate();
        }
        Rate =  sum / RateMap.size();
        return Rate;
    }
    public void SetRateMap(RegisteredUser registeredUser,Rating rating){
        RateMap.put(registeredUser,rating);
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
    public void setKeyWords(LinkedList<String> keyWords) {
        KeyWords = keyWords;
    }
    public String getName() {
        return Name;
    }

    public String getId() {
        return productId;
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
}
