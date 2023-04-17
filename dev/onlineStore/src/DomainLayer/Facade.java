package DomainLayer;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.Admin;
import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.SiteVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Facade {
    private  static  Facade instanceFacade =null;
    private static AtomicInteger VisitorID_GENERATOR = new AtomicInteger(1);
    private LinkedList<AtomicInteger> FreeVisitorID;
    private Map<Integer,SiteVisitor> onlineList;//online
    private Map<String, RegisteredUser> registeredUserList;
    private Map<Integer,Store> storesList;

    private Facade(){
        FreeVisitorID = new LinkedList<>();
        onlineList = new HashMap<>();
        registeredUserList = new HashMap<>();
        storesList = new HashMap<>();
    }
    public static synchronized Facade getInstance(){
        if(instanceFacade==null){
            instanceFacade= new Facade();
        }
        return instanceFacade;
    }





    public Response<Integer> EnterNewSiteVisitor(){//1.1
        int id = getNewVisitorId();
        onlineList.put(id,new SiteVisitor(id));
        return new Response<>(id);
    }
    public Response<?> ExitSiteVisitor(int id){//1.2
        AtomicInteger atomicId =new AtomicInteger(id);
        FreeVisitorID.add(atomicId);
        onlineList.remove(id);
        return new Response<>("Success",false);
    }
    public Response<?> Register(int visitorId, String userName, String password){//1.3
        //Valid visitorID
        if(onlineList.get(visitorId)==null){
            return new Response<>("Invalid Visitor ID",true);
        }
        //unique userName
        if(registeredUserList.get(userName)!= null){
            return new Response<>("This userName already taken",true);
        }
        //valid password with more than 8 letters
        Response<?> e;
        if(( e =isValidPassword(password)).isError){
            return e;
        }

        //get site visitor object
        SiteVisitor visitor = onlineList.get(visitorId);

        // create new register user
        registeredUserList.put(userName,new RegisteredUser(visitor,userName,password));

        return new Response<>("Success to register",false);
    }
    public Response<?> login(int visitorId, String userName, String password){//1.4
        RegisteredUser user=registeredUserList.get(userName);
        if(onlineList.get(visitorId)==null){
            return new Response<>("Invalid Visitor ID",true);
        }
        if(user==null){
            return new Response<>("UserName not Found",true);
        }
        if(!(user.getPassword().equals(password))){
            return new Response<>("wrong password",true);
        }
        if(user.getVisitorId()!=0){//visitorId =0 mean the user is logout
            return new Response<>("this user is already login",true);
        }
        //
        user.setVisitorId(visitorId);// online
        onlineList.replace(visitorId,user);
        return new Response<>(user.getVisitorId());
    }

    public Response<?> logout(int visitorId){//3.1
        SiteVisitor user=onlineList.get(visitorId);
        if(user==null){
            return new Response<>("Invalid Visitor ID",true);
        }
        if(!(user instanceof RegisteredUser)){
            return new Response<>("not logged in",true);
        }
        //id=0
        onlineList.get(visitorId).setVisitorId(0);
        //removefrom online list
        onlineList.replace(visitorId,new SiteVisitor(visitorId));//RegisteredUser turns into SiteVisitor
        return new Response<>(visitorId);

    }
    public Response<?> addProductToCart(String productId,int visitorId){//2.3
        SiteVisitor user= onlineList.get(visitorId);
        if(user==null){
            return new Response<>("Invalid Visitor ID",true);
        }
        if(!isValidProductId(productId)){
            return new Response<>("Invalid product ID",true);
        }
        int storeId= getStoreIdByProductId(productId);
        Store store = storesList.get(storeId);
        if(store==null){
            return new Response<>("Invalid product ID",true);
        }
        StoreProduct product =store.getProductByID(productId);//TO-DO(majd)
        if(product==null){
            return new Response<>("Invalid product ID",true);
        }
        user.addProductToCart(storeId,product);
        return new Response<>("success",false);
    }
    public Response<?> getProductsInMyCart(int visitorId){//2.4
        SiteVisitor user= onlineList.get(visitorId);
        if(user==null){
            return new Response<>("Invalid Visitor ID",true);
        }
       return new Response<>(user.cartToString());
    }

    private int getStoreIdByProductId(String productId) {
        int index = productId.indexOf('-');
        String storeId= productId.substring(0,index);
        return Integer.parseInt(storeId);

    }
    private boolean isValidProductId(String productId) {
        int index = productId.indexOf('-');
        if(index<1 || index>= productId.length())
            return false;
        return checkIfNumber(productId.substring(0,index)) && checkIfNumber(productId.substring(index,productId.length()));
    }
    private boolean checkIfNumber(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
                return false;
            }
        }
        return true;
    }

    private Response<?> isValidPassword(String password) {
        if(password.length()<8){
            return  new Response<>("password is too short",true);
        }
        return new Response<>("success",false);
    }

    private int getNewVisitorId(){
        if(FreeVisitorID.size()!=0){
            return FreeVisitorID.removeFirst().get();
        }
        return VisitorID_GENERATOR.getAndIncrement();
    }


}
