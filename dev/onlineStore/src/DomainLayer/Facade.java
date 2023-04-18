package DomainLayer;

import DomainLayer.Stores.History;
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
    private static Facade instanceFacade = null;
    private static AtomicInteger VisitorID_GENERATOR = new AtomicInteger(1);
    private LinkedList<AtomicInteger> FreeVisitorID;
    private Map<Integer, SiteVisitor> onlineList;//online
    private Map<String, RegisteredUser> registeredUserList;
    private Map<Integer, Store> storesList;
    private Map<String, Map<Integer, Employment>> employmentList;//

    private Facade() {
        FreeVisitorID = new LinkedList<>();
        onlineList = new HashMap<>();
        registeredUserList = new HashMap<>();
        storesList = new HashMap<>();
        employmentList = new HashMap<>();
    }

    public static synchronized Facade getInstance() {
        if (instanceFacade == null) {
            instanceFacade = new Facade();
        }
        return instanceFacade;
    }


    public Response<Integer> EnterNewSiteVisitor() {//1.1
        int id = getNewVisitorId();
        onlineList.put(id, new SiteVisitor(id));
        return new Response<>(id);
    }

    public Response<?> ExitSiteVisitor(int id) {//1.2
        AtomicInteger atomicId = new AtomicInteger(id);
        FreeVisitorID.add(atomicId);
        onlineList.remove(id);
        return new Response<>("Success", false);
    }

    public Response<?> Register(int visitorId, String userName, String password) {//1.3
        //Valid visitorID
        if (onlineList.get(visitorId) == null) {
            return new Response<>("Invalid Visitor ID", true);
        }
        //unique userName
        if (registeredUserList.get(userName) != null) {
            return new Response<>("This userName already taken", true);
        }
        //valid password with more than 8 letters
        Response<?> e;
        if ((e = isValidPassword(password)).isError) {
            return e;
        }

        //get site visitor object
        SiteVisitor visitor = onlineList.get(visitorId);

        // create new register user
        registeredUserList.put(userName, new RegisteredUser(visitor, userName, password));

        return new Response<>("Success to register", false);
    }

    public Response<?> login(int visitorId, String userName, String password) {//1.4
        RegisteredUser user = registeredUserList.get(userName);
        if (onlineList.get(visitorId) == null) {//check if the user is entered to the system
            return new Response<>("Invalid Visitor ID", true);
        }
        if (user == null) {//check if he has account
            return new Response<>("UserName not Found", true);
        }
        Response<?> response = user.login(password);
        if (response.isError) {// try to login
            return response;
        }
        //
        user.setVisitorId(visitorId);// online
        onlineList.replace(visitorId, user);
        return new Response<>(user.getVisitorId());
    }

    public Response<?> logout(int visitorId) {//3.1
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            return new Response<>("Invalid Visitor ID", true);
        }
        if (!(user instanceof RegisteredUser)) {
            return new Response<>("not logged in", true);
        }
        //id=0
        onlineList.get(visitorId).setVisitorId(0);
        //removefrom online list
        onlineList.replace(visitorId, new SiteVisitor(visitorId));//RegisteredUser turns into SiteVisitor
        return new Response<>(visitorId);

    }

    public Response<?> addProductToCart(String productId, int visitorId) {//2.3
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            return new Response<>("Invalid Visitor ID", true);
        }
        if (!isValidProductId(productId)) {
            return new Response<>("Invalid product ID", true);
        }
        int storeId = getStoreIdByProductId(productId);
        Store store = storesList.get(storeId);
        if (store == null) {
            return new Response<>("Invalid product ID", true);
        }
        StoreProduct product = store.getProductByID(productId);//TO-DO(majd)
        if (product == null) {
            return new Response<>("Invalid product ID", true);
        }
        user.addProductToCart(storeId, product);
        return new Response<>("success", false);
    }

    public Response<?> getProductsInMyCart(int visitorId) {//2.4
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            return new Response<>("Invalid Visitor ID", true);
        }
        return new Response<>(user.cartToString());
    }

    public Response<?> appointNewStoreOwner(int appointerId, String appointedUserName, int storeId) {//4.4
        //check appointerId and registerd user
        SiteVisitor appointer = onlineList.get(appointerId);
        if (appointer == null || !(appointer instanceof RegisteredUser)) {
            return new Response<>("inValid appointer Id", true);
        }
        // check if store id exist
        Store store = storesList.get(storeId);
        if (store == null) {
            return new Response<>("inValid store Id", true);
        }
        // check appointer is owner of storeId
        Employment appointerEmployment = null;
        try {
            appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);//check this
        } catch (Exception e) {
            return new Response<>("the appointer is not owner of store id", true);
        }

        if (appointerEmployment == null || !appointerEmployment.checkIfOwner()) {
            return new Response<>("the appointer is not owner of store id", true);
        }
        // check if appointedUserName is registered
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if (appointed == null) {
            return new Response<>("inValid appointed UserName", true);
        }
        // check if appointedUserName has appointment with storeId as storeOwner
        Employment appointedEmployment = null;
        try {
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);//check this
        } catch (Exception e) {

        }

        if (appointedEmployment != null && appointedEmployment.checkIfOwner()) {
            return new Response<>("appointedUserName is already Owner of stor Id", true);
        }
        //add appointedUserName as store owner
        appointedEmployment = new Employment((RegisteredUser) appointer, appointed, store, Role.StoreOwner);
        if (employmentList.get(appointedUserName) == null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId, appointedEmployment);
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
        if (password.length() < 8) {
            return new Response<>("password is too short", true);
        }
        return new Response<>("success", false);
    }

    private int getNewVisitorId() {
        if (FreeVisitorID.size() != 0) {
            return FreeVisitorID.removeFirst().get();
        }
        return VisitorID_GENERATOR.getAndIncrement();
    }

    // open Store
    public Response<?> OpenStore(String UserId, int StoreId) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            store.OpenStore();
            return new Response<>("the store is open now", false);
        }

        return new Response<>("Just the owner can open the Store ", true);
    }

    //close store
    public Response<?> CloseStore(String UserId, int StoreId) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            store.CloseStore();
            return new Response<>("the store is Close now", false);
        }

        return new Response<>("Just the owner can Close the Store ", true);
    }


    // ניהול מלאי 4.1
    public Response<?> AddProduct(String UserId, int StoreId, StoreProduct storeProduct) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            store.AddNewProduct(storeProduct.productId, storeProduct.Name, storeProduct.Price, storeProduct.Quantity, storeProduct.Category, storeProduct.KeyWords);
            return new Response<>("the Product is successfully added", false);
        }

        return new Response<>("Just the owner can Close the Store ", true);
    }

    public Response<?> RemoveProduct(String UserId, int StoreId, String ProductId) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            store.RemoveProduct(ProductId);
            return new Response<>("the Product is successfully added", false);
        }

        return new Response<>("Just the owner can Close the Store ", true);
    }

    public Response<?> UpdateStore(String UserId, int StoreId, String productID, String Id, String name, double price, String category, int quantity, LinkedList<String> kws) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            store.EditProduct(productID, Id, name, price, category, quantity, kws);
            return new Response<>("the Product is successfully added", false);
        }

        return new Response<>("Just the owner can Close the Store ", true);
    }

    //2.2 search  product
    public Response<?> SearchProduct(int StoreId, String Pid) {
        Store store = storesList.get(StoreId);
        if (store == null) {
            return new Response<>("there is no store with this id ", true);
        }
        if (!store.getActive()) {
            return new Response<>("the Store is closed now you cant search ", true);
        }
        LinkedList<StoreProduct> ls = store.SearchProduct(Pid);
        return new Response<>(ls.toString());
    }
//2.1
    public Response<?> GetInformation(int StoreId) {
        Store store = storesList.get(StoreId);
        if (store == null) {
            return new Response<>("there is no store with this id ", true);
        }
        if (!store.getActive()) {
            return new Response<>("the Store is closed now you cant look for information ", true);
        }
        return new Response<>(store.getInfo());
    }

    //6.4
    public Response<?> GetIHistoryPurchase(int StoreId, String UserId) {
        Employment employment = employmentList.get(UserId).get(StoreId);
        LinkedList<Bag> history = null;
        if (employment == null)
            return new Response<>("there is no employee with this id ", true);
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                return new Response<>("there is no store with this id ", true);
            }
            history = store.GetStorePurchaseHistory();
        }
        return new Response<>(history);
    }
}
