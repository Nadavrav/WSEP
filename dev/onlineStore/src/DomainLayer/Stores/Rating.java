package DomainLayer.Stores;

import DomainLayer.Response;

public class Rating {
    int MaxRate;
    int MinRate;
    int UserRate;
    public Rating (){
        MaxRate=5;
        MinRate=0;
    }
    public Response<?> UserRate (int Rate){
       if (Rate > 5 || Rate < 0) {
           UserRate = Rate;
           return new Response<>("your Rate is saved ", false);
       }
       return new Response<>("Your Rate must be between 0-5",true);
    }
    public int getUserRate() {
        return UserRate;
    }
}
