package DomainLayer.Stores;

import DomainLayer.Response;

import java.util.Map;

public class Rating {
    int MaxRate;
    int MinRate;
    Map<Integer, Integer> UserRateForStore;
    Map<Integer, Integer> UserRateForeProduct;
    public Rating (){
        MaxRate=5;
        MinRate=0;
    }
    public Response<?> AddRateForProduct (int visitorID,int Rate){
       if (Rate > 5 || Rate < 0) {
           UserRateForeProduct.put(visitorID,Rate);
           return new Response<>("your Rate is saved ", false);
       }
       return new Response<>("Your Rate must be between 0-5",true);
    }
    public Response<?> AddRateForStore (int visitorID,int Rate){
        if (Rate > 5 || Rate < 0) {
            UserRateForStore.put(visitorID,Rate);
            return new Response<>("your Rate is saved ", false);
        }
        return new Response<>("Your Rate must be between 0-5",true);
    }
    public int getUserRateForProduct(int visitorID) {
        return UserRateForeProduct.get(visitorID);
    }
    public int getUserRateForStore(int visitorID) {
        return UserRateForStore.get(visitorID);
    }
}
