package DomainLayer.Stores;
import DomainLayer.Response;
import DomainLayer.Users.Bag;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import DomainLayer.Users.RegisteredUser;

public class Store {
    public  String Id ;
    public String Name;
    public Boolean Active;

    public History History ;

    public ConcurrentHashMap <RegisteredUser,Rating> RateMap;
    public ConcurrentHashMap <String,StoreProduct> products;
    public Double Rate ;


    public Store(String StoreId,String name )
    {
        Id=StoreId;
        Name = name;
        History = new History();
        products= new ConcurrentHashMap<>();
    }




    public double getRate(){
        double sum =0;
        for (Rating r : RateMap.values()){
            sum =+ r.getUserRate();
        }
        Rate =  sum / RateMap.size();
        return Rate;
    }
    //2.1
    public String getInfo(String StoreID){
        String s="Store Name is"+ this.Name + "Store Rate is:"+ this.Rate;
        for (StoreProduct i :products.values()) {
            s+= " Product Name is :" + i.getName() + "The Rate is : "+i.getRate()+ "/n";

        }
        return s;
    }
    // 3.2 4.9
    public void OpenStore(String UserID){

           Active = true;
    }
    public void CloseStore(String UserID){

            Active = false;

    }
   // history 6.4
    public LinkedList<Bag> GetStorePurchaseHistory(String userID)
    {

            return this.History.ShoppingBags;

    }



    // ADD , UPDATE, REMOVE, SEARCH PRODUCT IS DONE ניהול מלאי 4.1
    public Response<?> AddNewProduct( String productID, String productName, Double price, int initialQuantity, String category, LinkedList<String> keyWords)
    {
            StoreProduct storeProduct= new StoreProduct(productID,productName,price,category,initialQuantity,keyWords);
            products.put(productID,storeProduct);
            products.get(productID).Quantity++;
            return new Response<>("Success",false);
    }

    public Response<Object> EditProduct( String productID, String Id , String name, double price, String category, int quantity, LinkedList<String> kws)
    {

            if (products.containsKey(productID)){
                products.get(productID).setProductId(Id);
                products.get(productID).setCategory(category);
                products.get(productID).setName(name);
                products.get(productID).setQuantity(quantity);
                products.get(productID).setKeyWords(kws);
                return new Response<>("there is no product with this ID to update", false);

            }
            else {
                return new Response<>("there is no product with this ID to update", true);
            }

    }
    public Response<Object> RemoveProduct(String productID)
    {
            StoreProduct sp = null;
            if (products.containsKey(productID)) {
                sp = products.get(productID);
            } else {
                return new Response<>("there is no product in our products with this ID", true);
            }
            products.remove(productID, sp);
            products.get(productID).Quantity++;
            return new Response<>("product Removed", false);
    }
    //2.2
    public List<StoreProduct> SearchProduct(String Search)
    {
        LinkedList<StoreProduct> searchResults =null;
        if (getActive()) {
             searchResults = new LinkedList<StoreProduct>();
            for (StoreProduct product : this.products.values()) {
                if (product.Name == Search || product.Category == Search) {
                    if (CheckProduct(product)) {
                        searchResults.add(product);
                    }
                }
            }
        }
      return searchResults;
    }
   private boolean CheckProduct(StoreProduct product) {
        if(product.Quantity > 0){
            return true;
        }
        return false;
    }
    public void setRate(Double rate) {
        Rate = rate;
    }

    public void SetRateMap(RegisteredUser registeredUser,Rating rating){
       RateMap.put(registeredUser,rating);
    }
        public StoreProduct getProductByID(String productId) {
        return products.get(productId);

    }
    public Boolean getActive() {
        return Active;
    }
}