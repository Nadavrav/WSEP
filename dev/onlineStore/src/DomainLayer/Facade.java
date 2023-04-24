package DomainLayer;

import DomainLayer.Stores.History;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.*;
import DomainLayer.Users.Fiters.Filter;
import ExternalServices.PaymentProvider;
import ExternalServices.Supplier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static DomainLayer.Stores.StoreProduct.getStoreIdByProductId;
import static DomainLayer.Stores.StoreProduct.isValidProductId;

public class Facade {
    private static Facade instanceFacade = null;
    private Map<Integer, SiteVisitor> onlineList;//online
    private Map<String, RegisteredUser> registeredUserList;
    private Map<Integer, Store> storesList;
    private Map<String, Map<Integer, Employment>> employmentList;//
    private Supplier supplier;
    private PaymentProvider paymentProvider;

    private Facade() {

        onlineList = new HashMap<>();
        registeredUserList = new HashMap<>();
        storesList = new HashMap<>();
        employmentList = new HashMap<>();
        supplier= new Supplier();
        paymentProvider= new PaymentProvider();

    }

    public static synchronized Facade getInstance() {
        if (instanceFacade == null) {
            instanceFacade = new Facade();
        }
        return instanceFacade;
    }

//------------UserPackege-----------------------
    public int EnterNewSiteVisitor() throws Exception {//1.1
        SiteVisitor visitor = new SiteVisitor();
        onlineList.put(visitor.getVisitorId(), new SiteVisitor());
        return visitor.getVisitorId();
    }

    public void ExitSiteVisitor(int id) throws Exception {//1.2
        SiteVisitor.ExitSiteVisitor(id);
        onlineList.remove(id);
    }

    public synchronized void Register(int visitorId, String userName, String password) throws Exception {//1.3
        //Valid visitorID
        if (!SiteVisitor.checkVisitorId(visitorId)) {
           throw  new Exception("Invalid Visitor ID");
        }
        //unique userName
        if (registeredUserList.get(userName) != null) {
            throw  new Exception("This userName already taken");
        }
        //get site visitor object
        //SiteVisitor visitor = onlineList.get(visitorId);
       // visitor =new RegisteredUser(visitor, userName, password);
        //onlineList.put(visitorId,visitor);
        // create new register user

        registeredUserList.put(userName, new RegisteredUser(userName, password));

    }

    public synchronized void login(int visitorId, String userName, String password) throws Exception {//1.4
        RegisteredUser user = registeredUserList.get(userName);
        if (!SiteVisitor.checkVisitorId(visitorId)) {//check if the user is entered to the system
            throw  new Exception("Invalid Visitor ID");
        }
        if (user == null) {//check if he has account
            throw  new Exception("UserName not Found");
        }
         user.login(password,visitorId);

        onlineList.replace(visitorId, user);
    }

    public synchronized int logout(int visitorId) throws Exception {//3.1
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            throw  new Exception("Invalid Visitor ID");
        }
        if (!(user instanceof RegisteredUser)) {
            throw  new Exception("not logged in");
        }
        //id=0
        ((RegisteredUser) user).logout();
        //removefrom online list
        user= new SiteVisitor(visitorId);

        onlineList.replace(visitorId,user );//RegisteredUser turns into SiteVisitor
        return visitorId;
    }

    public void addProductToCart(String productId, int visitorId) throws Exception {//2.3
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            throw  new Exception("Invalid Visitor ID");
        }
        isValidProductId(productId);
        //Get product lock
        try {
            int storeId = getStoreIdByProductId(productId);

            Store store = storesList.get(storeId);
            if (store == null) {
                throw new Exception("Invalid product ID");
            }
            if (!store.getActive()) {
                throw new Exception("this is closed Store");
            }
            StoreProduct product = store.getProductByID(productId);//TO-DO(majd)
            if (product == null) {
                throw new Exception("Invalid product ID");
            }
            user.addProductToCart(storeId, product);
        }
        catch (Exception e){
            //release lock
            throw e;
        }
    }

    public String getProductsInMyCart(int visitorId) throws Exception {//2.4
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            throw  new Exception("Invalid Visitor ID");
        }

        return user.cartToString();

    }

    public void appointNewStoreOwner(int appointerId, String appointedUserName, int storeId) throws Exception {//4.4
        //check appointerId and registerd user
        SiteVisitor appointer = onlineList.get(appointerId);
        //lock appointer
        //variable "appointedlock"
        //try
        if (!(appointer instanceof RegisteredUser)) {
            throw  new Exception("inValid appointer Id");
        }
        // check if store id exist
        Store store = storesList.get(storeId);
        if (store == null) {
            throw  new Exception("inValid store Id");
        }
        if(!store.getActive()){
            throw  new Exception("this is closed Store");
        }
        // check appointer is owner of storeId
        Employment appointerEmployment = null;
        try {
            appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);//check this
        } catch (Exception e) {
            throw  new Exception("the appointer is not owner of store id");
        }

        if (appointerEmployment == null || !appointerEmployment.checkIfOwner()) {
            throw  new Exception("the appointer is not owner of store id");
        }
        // check if appointedUserName is registered
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if (appointed == null) {
            throw  new Exception("inValid appointed UserName");
        }
        // check if appointedUserName has appointment with storeId as storeOwner
        Employment appointedEmployment = null;
        try {
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);//check this
            //lock appointedlock
        } catch (Exception e) {

        }
        if(appointedEmployment!=null && appointedEmployment.checkIfOwner()){
            throw  new Exception("appointedUserName is already Owner of store Id");
        }
        //add appointedUserName as store owner
        appointedEmployment = new Employment((RegisteredUser) appointer, appointed, store, Role.StoreOwner);
        if (employmentList.get(appointedUserName) == null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId, appointedEmployment);
        //catch
        //release lock appointer
        //release lockappointed if locked
        //throw e
    }

    public void appointNewStoreManager(int appointerId,String appointedUserName,int storeId) throws Exception {//4.6
        //check if appointerId is logged in and registered to system
        SiteVisitor appointer = onlineList.get(appointerId);
        //lock appointer
        //variable "appointedlock"
        //try
        if(!(appointer instanceof RegisteredUser)){
            throw  new Exception("invalid appointer Id");
        }
        // check if store id exist
        Store store=storesList.get(storeId);
        if(store==null){
            throw  new Exception("inValid store Id");
        }
        if(!store.getActive()){
            throw  new Exception("this is closed Store");
        }
        // check if appointer is owner (or founder) of storeId
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
        }catch (Exception e){
            throw  new Exception("the appointer is not owner of store id");
        }

        if(appointerEmployment==null|| !appointerEmployment.checkIfOwner()){
            throw  new Exception("the appointer is not owner of store id");
        }
        // check if appointedUserName is registered to system
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if(appointed==null){
            throw  new Exception("inValid appointed UserName");
        }
        // check if appointedUserName has appointment with storeId as storeOwner or as storeManager
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);
        }catch (Exception e){

        }

        if(appointedEmployment!=null && (appointedEmployment.checkIfOwner() || appointedEmployment.checkIfManager())){
            throw  new Exception("appointedUserName is already owner or manager of store Id");
        }
        //add appointedUserName as store manager
        appointedEmployment = new Employment((RegisteredUser) appointer,appointed,store,Role.StoreManager);
        if(employmentList.get(appointedUserName)== null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId,appointedEmployment);
        //catch
        //release lock appointer
        //release lockappointed if locked
        //throw e

    }

    public Employment changeStoreManagerPermission(int visitorID, String username, int storeID, List<Permission> permissions) throws Exception {
        //Check if visitorID is logged in and registered to system
        SiteVisitor appointer = onlineList.get(visitorID);
        //lock appointer
        //variable "appointedlock"
        //try
        if(!(appointer instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }

        //Check if storeID exists
        Store store=storesList.get(storeID);
        if(store==null){
            throw  new Exception("invalid store Id");
        }
        if(!store.getActive()){
            throw  new Exception("this is closed Store");
        }

        //Check if visitorID is owner of storeID
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeID);
        }catch (Exception e){
            throw  new Exception("the appointer is not owner of store id");
        }

        if(appointerEmployment==null|| !appointerEmployment.checkIfOwner()){
            throw  new Exception("the appointer is not owner of store id");
        }

        //Check if username is registered to system
        RegisteredUser appointed = registeredUserList.get(username);
        if(appointed==null){
            throw  new Exception("invalid appointed UserName");
        }

        //Check if username is manager of storeID
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(username).get(storeID);
        }catch (Exception e){
            throw  new Exception("The given username is not associated with any store");
        }

        if(appointedEmployment == null ||!appointedEmployment.checkIfManager()){
            throw  new Exception("The given username is not manager of the given store");
        }

        //Change permission
        for(Permission permission:permissions)
            appointedEmployment.togglePermission(permission);
        return appointedEmployment;
        //catch
        //release lock appointer
        //release lockappointed if locked
        //throw e
    }

    /**
     *
     * @param visitorId
     * @param storeId
     * @return
     * @throws Exception
     */
    public String getRolesData(int visitorId,int storeId) throws Exception {//4.11
        //Check if visitorID is logged in and registered to system
        SiteVisitor appointer = onlineList.get(visitorId);
        if(!(appointer instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }

        if(!(appointer instanceof Admin)) {
            //is he storeowner
            try {
                employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
            } catch (Exception e) {
                throw  new Exception("the appointer is not owner of store id");
            }
        }
        //get employment
        String output="";
        for(Map<Integer,Employment> userEmployments : employmentList.values()){
            if(userEmployments.containsKey(storeId)){
               output+= userEmployments.get(storeId).toString()+"/n";        //employment.toString
            }
        }
        return output;
    }

    public LinkedList<String> purchaseCart(int visitorID,int visitorCard,String address) throws Exception{
        
        //Validate visitorID
        SiteVisitor visitor = onlineList.get(visitorID);
        if (visitor == null) {
            throw new Exception("Invalid Visitor ID");
        }
                
        LinkedList<String> failedPurchases = new LinkedList<>();


        for(Bag b : visitor.getCart().getBag().values()){
           
            //Calculate amount
            int amount = b.calculateTotalAmount();
           
            //Check if possible to create a supply
            if(!supplier.isValidAddress(address)){
                failedPurchases.add(b.getStoreID());
            }
            
            //Create a transaction for the store
            if(!paymentProvider.applyTransaction(amount,visitorCard)){
                failedPurchases.add(b.getStoreID());
            }
            LinkedList<String> productsId = new LinkedList<>();
            productsId.add(b.bagToString());
            //Create a request to supply bag's product to customer
            if(!supplier.supplyProducts(productsId)){
                failedPurchases.add(b.getStoreID());
            }
         
       }

        return failedPurchases;
        
    }
    //----------Store-----------
    // open Store
    public Integer OpenNewStore(int visitorId,String storeName) throws Exception {
        // check if register user
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }
        //open new store ()
        Store store = new Store(storeName);
        // add to store list
        storesList.put(store.getID(),store);
        //new Employment
        Employment employment = new Employment((RegisteredUser) User,store,Role.StoreOwner);
        // andd to employment list
        if (employmentList.get(((RegisteredUser) User).getUserName()) == null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(((RegisteredUser) User).getUserName(), newEmploymentMap);
        }
        employmentList.get(((RegisteredUser) User).getUserName()).put(store.getID(),employment);
        return store.getID();
    }
    //StoreRate
    public double GetStoreRate(int visitorId,int StoreId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(User==null){
            throw  new Exception("invalid visitor Id");
        }
        Store store = storesList.get(StoreId);
        if (store == null || !store.getActive()) {
            throw  new Exception("there is no store with this id ");
        }
        return store.getRate();
    }

    //productRate
    public double GetStoreProductRate(int visitorId,String ProductId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(User==null){
            throw  new Exception("invalid visitor Id");
        }
        int StoreId = StoreProduct.getStoreIdByProductId(ProductId);
        Store store = storesList.get(StoreId);
        if (store == null || !store.getActive()) {
            throw  new Exception("there is no product with this id ");
        }

         Double D =store.getProductByID(ProductId).getRate();
        return D;

    }
    //close store
    public void CloseStore(int visitorId, int StoreId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //try
        if(! (User instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(visitorId).get(StoreId);
        }catch (Exception e){
            throw  new Exception("this user dont have any store");
        }

        if (employment == null)
            throw  new Exception("there is no employee with this id ");
        if (employment.checkIfOwner()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                throw  new Exception("there is no store with this id ");
            }
            store.CloseStore();
           return;
        }

        throw  new Exception("Just the owner can Close the Store ");
        //catch
        //release lock user
        //throw e
    }
    // ניהול מלאי 4.1
    public String AddProduct(int visitorId,int storeId,String productName, double price, String category, int quantity,String description) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //try
        if(! (User instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }
        Store store = storesList.get(storeId);
        if(store==null){
            throw  new Exception("there is no store with this id ");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(visitorId).get(storeId);
        }catch (Exception e){
            throw  new Exception("this user dont have any store");
        }
        if (employment == null)
            throw  new Exception("there is no employee with this id ");
        if (!employment.checkIfOwner()) {//check if need manager
            throw  new Exception("you are not the owner of this store ");
        }
        return store.AddNewProduct(productName,price,quantity,category,description);
        //catch
        //release lock user
        //throw e
    }

    public void RemoveProduct(int visitorId, String ProductId)  throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //variable productlock
        //try
        if(! (User instanceof RegisteredUser)){
            throw  new Exception("invalid visitor Id");
        }
        int StoreId=StoreProduct.getStoreIdByProductId(ProductId);
        Employment employment = null;
        try{
            employment = employmentList.get(visitorId).get(StoreId);
        }catch (Exception e){
            throw  new Exception("this user dont have any store");
        }
        if (employment == null)
            throw  new Exception("there is no employee with this id ");
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                throw  new Exception("there is no store with this id ");
            }
            store.RemoveProduct(ProductId);
            //return new Response<>("the Product is successfully added", false);
        }

        throw  new Exception("Just the owner can Close the Store ");
        //catch
        //release lock user
        //release productlock if locked
        //throw e
    }

    public void UpdateProductQuantity(int visitorId, String productID,int quantity) throws Exception{
        //lock product (get product object)
        //try
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(StoreProduct.getStoreIdByProductId(productID));
        store.UpdateProductQuantity(productID,quantity);
        //catch
        //release lock product
        //throw e

    }

    public void IncreaseProductQuantity(int visitorId, String productID,int quantity) throws Exception{
        //lock product (get product object)
        //try
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(StoreProduct.getStoreIdByProductId(productID));
        store.IncreaseProductQuantity(productID,quantity);
        //catch
        //release lock product
        //throw e

    }

    public void UpdateProductName(int visitorId, String productID,String Name) throws Exception{
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(StoreProduct.getStoreIdByProductId(productID));
        store.UpdateProductName(productID,Name);

    }

    public void UpdateProductPrice(int visitorId, String productID,double price) throws Exception{
        //lock product (get product object)
        //try
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(getStoreIdByProductId(productID));
        store.UpdateProductPrice(productID,price);
        //catch
        //release lock product
        //throw e

    }

    public void UpdateProductCategory(int visitorId, String productID,String category) throws Exception{
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(getStoreIdByProductId(productID));
        store.UpdateProductCategory(productID,category);

    }

    public void UpdateProductDescription(int visitorId, String productID,String description) throws Exception{
        checkifUserCanUpdateStoreProduct(visitorId,productID);
        Store store = storesList.get(getStoreIdByProductId(productID));
        store.UpdateProductDescription(productID,description);

    }

    private void checkifUserCanUpdateStoreProduct(int visitorId, String productID) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof RegisteredUser)){
            throw new Exception("invalid visitor Id");
        }
        Employment employment = null;
        int storeId =StoreProduct.getStoreIdByProductId(productID);
        try{
            employment = employmentList.get(visitorId).get(storeId);
        }catch (Exception e){
            throw new Exception("This user don't have any store");
        }
        if (employment == null)
            throw new Exception("invalid store id ");
        if (employment.checkIfOwner()) {
            Store store = storesList.get(storeId);
            if (store == null) {
                throw new Exception("there is no store with this id ");
            }
            return;

            //return new Response<>("the Product is successfully added", false);
        }

        throw new Exception("Just the owner can Close the Store ");
    }
    //2.2 search  product
    public String SearchProductByName( String Name) throws Exception{
        String output ="";
        for (Store store :storesList.values() ) {
            output+=store.SearchProductByName(Name).toString();
        }
        return output;
    }

    public String SearchProductByCategory( String Category) throws Exception {
        String output ="";
        for (Store store :storesList.values() ) {
            output+=store.SearchProductByCategory(Category).toString();
        }
        return output;
    }

    public List<String> SearchProductBykey( String key) {
        ArrayList<String> output =new ArrayList<>();
        for (Store store :storesList.values() ) {
            for(StoreProduct product: store.SearchProductByKey(key))
                output.add(product.toString());
        }
        return output;

    }
//2.1
    public String GetInformation(int StoreId) throws Exception {
        Store store = storesList.get(StoreId);
        if (store == null) {
            throw new Exception("there is no store with this id ");
        }
        return store.getInfo();
    }
    //6.4
    public List<String> GetStoreHistoryPurchase(int StoreId, int visitorId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof Admin)){
            throw new Exception("invalid visitor Id");
        }
        Store store = storesList.get(StoreId);
        if (store == null) {
            throw new Exception("there is no store with this id ");
        }
        ArrayList<String> output=new ArrayList<>();
        LinkedList<Bag> history = store.GetStorePurchaseHistory();
        for(Bag bag:history)
            output.add(bag.toString());
        return output;
    }

    public String GetUserHistoryPurchase(String userName, int visitorId) throws Exception {
        SiteVisitor admin = onlineList.get(visitorId);
        if(! (admin instanceof Admin)){
            throw new Exception("invalid visitor Id");
        }
        RegisteredUser user = registeredUserList.get(userName);
        if(user==null){
            throw new Exception("There is no user with this user name");
        }

        return user.getPurchaseHistory().getPurchases().toString();
    }

    /**
     *
     * @param filters list of filter object for whom each product has to pass all of them to be returned
     * @return list of strings describing product info of products who passed the filter list
     */
    public List<StoreProduct> FilterProductSearch(List<Filter> filters) {
        ArrayList<StoreProduct> products=new ArrayList<>();
        for(Store store: storesList.values()){ //for each store
            for(StoreProduct product:store.getProducts().values()){ //for each product in store
                boolean passedFilter=true;
                for(Filter filter: filters){ //for each filter
                    if(!filter.PassFilter(product)) { //product has to pass all filters
                        passedFilter = false;
                        break; //if we don't pass a filter, we exit from the filter loop-no need to check the rest
                    }
                }
                if (passedFilter)
                    products.add(product);
            }
        }
        return products;
    }
}
