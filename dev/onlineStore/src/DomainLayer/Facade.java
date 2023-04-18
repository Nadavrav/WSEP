package DomainLayer;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.*;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static DomainLayer.Stores.StoreProduct.getStoreIdByProductId;
import static DomainLayer.Stores.StoreProduct.isValidProductId;

public class Facade {
    private  static  Facade instanceFacade =null;
    private static AtomicInteger VisitorID_GENERATOR = new AtomicInteger(1);
    private LinkedList<AtomicInteger> FreeVisitorID;
    private Map<Integer,SiteVisitor> onlineList;//online
    private Map<String, RegisteredUser> registeredUserList;
    private Map<Integer,Store> storesList;
    private Map<String,Map<Integer, Employment>> employmentList;//

    private Facade(){
        FreeVisitorID = new LinkedList<>();
        onlineList = new HashMap<>();
        registeredUserList = new HashMap<>();
        storesList = new HashMap<>();
        employmentList = new HashMap<>();
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
        if(onlineList.get(visitorId)==null){//check if the user is entered to the system
            return new Response<>("Invalid Visitor ID",true);
        }
        if(user==null){//check if he has account
            return new Response<>("UserName not Found",true);
        }
        Response<?> response=user.login(password);
        if(response.isError){// try to login
            return response;
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
        SiteVisitor user = onlineList.get(visitorId);
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
    public Response<?> appointNewStoreOwner(int appointerId,String appointedUserName,int storeId){//4.4
        //check appointerId and registerd user
        SiteVisitor appointer = onlineList.get(appointerId);
        if(appointer==null || ! (appointer  instanceof RegisteredUser)){
            return new Response<>("inValid appointer Id",true);
        }
        // check if store id exist
        Store store=storesList.get(storeId);
        if(store==null){
            return new Response<>("inValid store Id",true);
        }
        // check appointer is owner of storeId
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);//check this
        }catch (Exception e){
            return new Response<>("the appointer is not owner of store id",true);
        }

        if(appointerEmployment==null|| !appointerEmployment.checkIfOwner()){
            return new Response<>("the appointer is not owner of store id",true);
        }
        // check if appointedUserName is registered
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if(appointed==null){
            return new Response<>("inValid appointed UserName",true);
        }
        // check if appointedUserName has appointment with storeId as storeOwner
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);//check this
        }catch (Exception e){

        }

        if(appointedEmployment!=null && appointedEmployment.checkIfOwner()){
            return new Response<>("appointedUserName is already Owner of store Id",true);
        }
        //add appointedUserName as store owner
        appointedEmployment = new Employment((RegisteredUser) appointer,appointed,store,Role.StoreOwner);
        if(employmentList.get(appointedUserName)== null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId,appointedEmployment);
        return  new Response<>("success");
    }

    public Response<?> appointNewStoreManager(int appointerId,String appointedUserName,int storeId){//4.6
        //check if appointerId is logged in and registered to system
        SiteVisitor appointer = onlineList.get(appointerId);
        if(appointer == null || ! (appointer  instanceof RegisteredUser)){
            return new Response<>("invalid appointer Id",true);
        }
        // check if store id exist
        Store store=storesList.get(storeId);
        if(store==null){
            return new Response<>("inValid store Id",true);
        }
        // check if appointer is owner (or founder) of storeId
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
        }catch (Exception e){
            return new Response<>("the appointer is not owner of store id",true);
        }

        if(appointerEmployment==null|| !appointerEmployment.checkIfOwner()){
            return new Response<>("the appointer is not owner of store id",true);
        }
        // check if appointedUserName is registered to system
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if(appointed==null){
            return new Response<>("inValid appointed UserName",true);
        }
        // check if appointedUserName has appointment with storeId as storeOwner or as storeManager
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);
        }catch (Exception e){

        }

        if(appointedEmployment!=null && (appointedEmployment.checkIfOwner() || appointedEmployment.checkIfManager())){
            return new Response<>("appointedUserName is already owner or manager of store Id",true);
        }
        //add appointedUserName as store manager
        appointedEmployment = new Employment((RegisteredUser) appointer,appointed,store,Role.StoreManager);
        if(employmentList.get(appointedUserName)== null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId,appointedEmployment);
        return new Response<>("success");
    }
//    private int getStoreIdByProductId(String productId) {
//        int index = productId.indexOf('-');
//        String storeId= productId.substring(0,index);
//        return Integer.parseInt(storeId);
//
//    }
//    private boolean isValidProductId(String productId) {
//        int index = productId.indexOf('-');
//        if(index<1 || index>= productId.length())
//            return false;
//        return checkIfNumber(productId.substring(0,index)) && checkIfNumber(productId.substring(index,productId.length()));
//    }
//    private boolean checkIfNumber(String s){
//        for(int i=0;i<s.length();i++){
//            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
//                return false;
//            }
//        }
//        return true;
//    }

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

    public Response<?> changeStoreManagerPermission(int visitorID,String username,int storeID,Permission permission){
        //Check if visitorID is logged in and registered to system
        SiteVisitor appointer = onlineList.get(visitorID);
        if(appointer == null || ! (appointer  instanceof RegisteredUser)){
            return new Response<>("invalid visitor Id",true);
        }

        //Check if storeID exists
        Store store=storesList.get(storeID);
        if(store==null){
            return new Response<>("invalid store Id",true);
        }

        //Check if visitorID is owner of storeID
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeID);
        }catch (Exception e){
            return new Response<>("the appointer is not owner of store id",true);
        }

        if(appointerEmployment==null|| !appointerEmployment.checkIfOwner()){
            return new Response<>("the appointer is not owner of store id",true);
        }

        //Check if username is registered to system
        RegisteredUser appointed = registeredUserList.get(username);
        if(appointed==null){
            return new Response<>("invalid appointed UserName",true);
        }

        //Check if username is manager of storeID
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(username).get(storeID);
        }catch (Exception e){
            return new Response<>("The given username is not associated with any store",true);
        }

        if(appointedEmployment == null ||!appointedEmployment.checkIfManager()){
            return new Response<>("The given username is not manager of the given store",true);
        }

        //Change permission
        appointedEmployment.togglePermission(permission);
        return new Response<>("success");
    }


}
