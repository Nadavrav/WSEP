package DomainLayer;


import DomainLayer.Stores.Purchases.InstantPurchase;
import DomainLayer.Logging.UniversalHandler;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.*;
import ServiceLayer.ServiceObjects.Fiters.Filter;
import ExternalServices.PaymentProvider;
import ExternalServices.Supplier;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
public class Facade {
    private static Facade instanceFacade = null;
    private  static final Logger logger = Logger.getLogger("Facade Logger");

    private Map<Integer, SiteVisitor> onlineList;//online


    private Map<String, RegisteredUser> registeredUserList;


    private Map<Integer, Store> storesList;
    //unit tests getters
    public Map<Integer, SiteVisitor> getOnlineList() {
        return onlineList;
    }
    public Map<String, RegisteredUser> getRegisteredUserList() {
        return registeredUserList;
    }
    public Map<Integer, Store> getStoresList() {
        return storesList;
    }
    public Map<String, Map<Integer, Employment>> getEmploymentList() {
        return employmentList;
    }

    private Map<String, Map<Integer, Employment>> employmentList;
    private Supplier supplier;
    private PaymentProvider paymentProvider;

    private Facade() {
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        onlineList = new HashMap<>();
        registeredUserList = new HashMap<>();
        storesList = new HashMap<>();
        employmentList = new HashMap<>();
        supplier= new Supplier();
        paymentProvider= new PaymentProvider();
    }

    /**
     * Function used by test inorder to reset the data in the facade and make the tests independent of one another.
     */
    public void resetData()
    {
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
        logger.info("A new visitor with Id:" + visitor.getVisitorId() + "has Enter");
        return visitor.getVisitorId();
    }

    public void ExitSiteVisitor(int id) throws Exception {//1.2
        SiteVisitor.ExitSiteVisitor(id);
        onlineList.remove(id);
        logger.info("A  visitor with Id:" + id + "has Exit");
    }

    public synchronized void Register(int visitorId, String userName, String password) throws Exception {//1.3
        //Valid visitorID
        if (!SiteVisitor.checkVisitorId(visitorId)) {
            logger.warning("maybe we have a null visitor!");
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
        logger.info("new visitor has register");
        registeredUserList.put(userName, new RegisteredUser(userName, password));
    }

    public synchronized void login(int visitorId, String userName, String password) throws Exception {//1.4

        RegisteredUser user = registeredUserList.get(userName);
        if (!SiteVisitor.checkVisitorId(visitorId)) {//check if the user is entered to the system
            logger.warning("User IS NOT Entered in the system");
            throw  new Exception("Invalid Visitor ID");
        }
        if (user == null) {//check if he has account
            logger.warning("User is already have an account");
            throw  new Exception("UserName not Found");
        }
        logger.info("User log in successfully");
         user.login(password,visitorId);

        onlineList.replace(visitorId, user);
    }

    public synchronized int logout(int visitorId) throws Exception {//3.1
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("this User by  ID:"+ visitorId + "is null");
            throw  new Exception("Invalid Visitor ID");
        }
        if (!(user instanceof RegisteredUser)) {
            logger.warning("this User by  ID:"+ visitorId + "has not logging in");
            throw  new Exception("not logged in");
        }
        //id=0
        ((RegisteredUser) user).logout();
        logger.info("this User by  ID:"+ visitorId + "has log out from the system");
        //removefrom online list
        user= new SiteVisitor(visitorId);
        onlineList.replace(visitorId,user );//RegisteredUser turns into SiteVisitor
        return visitorId;
    }

     public void addProductToCart(int productId,int storeId, int visitorId) throws Exception {//2.3
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("trying to add product from null user");
            throw  new Exception("Invalid Visitor ID");
        }
        //Get product lock
        try {
            Store store = storesList.get(storeId);
            if (store == null) {
                logger.warning("trying to add product to store that not exist");
                throw new Exception("Invalid product ID");
            }
            if (!store.getActive()) {
                logger.warning("Add product to close store");
                throw new Exception("this is closed Store");
            }
            StoreProduct product = store.getProductByID(productId);
            if (product == null) {
                logger.warning("trying to add a nul product");
                throw new Exception("Invalid product ID");
            }
            user.addProductToCart(storeId, product);
            logger.fine("new product by name:" + product.getName()+" added successful ");
        }
        catch (Exception e){
            //release lock
            throw e;
        }
    }
    

    public void removeProductFromCart(int productId,int storeId, int visitorId) throws Exception {
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("Invalid Visitor ID");
            throw new Exception("Invalid Visitor ID");
        }
        //Get product lock
        try {

            Store store = storesList.get(storeId);
            if (store == null) {
                logger.warning("Invalid product ID");
                throw new Exception("Invalid product ID");
            }
            if (!store.getActive()) {
                logger.warning("This is a closed Store");
                throw new Exception("This is a closed Store");
            }
            StoreProduct product = store.getProductByID(productId);//
            if (product == null) {
                logger.warning("Invalid product ID");
                throw new Exception("Invalid product ID");
            }
            user.removeProductFromCart(storeId, product);
        }
        catch (Exception e){
            //release lock
            logger.warning("Error occurred: " + e.getMessage());
            throw e;
        }
    }
    

   public void changeCartProductQuantity(int productId,int storeId,int newAmount, int visitorId) throws Exception {
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("Invalid Visitor ID");
            throw new Exception("Invalid Visitor ID");
        }
       // isValidProductId(productId);
        //Get product lock
        try {
       //     int storeId = getStoreIdByProductId(productId);

            Store store = storesList.get(storeId);
            if (store == null) {
                logger.warning("Invalid product ID");
                throw new Exception("Invalid product ID");
            }
            if (!store.getActive()) {
                logger.warning("This is a closed Store");
                throw new Exception("This is a closed Store");
            }
            StoreProduct product = store.getProductByID(productId);//TO-DO(majd)
            if (product == null) {
                logger.warning("Invalid product ID");
                throw new Exception("Invalid product ID");
            }
            user.changeCartProductQuantity(storeId, product,newAmount);
        } catch (Exception e) {
            //release lock
            logger.warning("Error occurred: " + e.getMessage());
            throw e;
        }
    }
    
    
    public String getProductsInMyCart(int visitorId) throws Exception {//2.4
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("trying to add from a null user");
            throw  new Exception("Invalid Visitor ID");
        }

        return user.cartToString();
        //return user.GetCart

    }

    /**
     * Returns a cart
     * @param visitorId - the id of the user whos cart we want
     * @return - A cart of the user.
     * @throws Exception - if user id doesnt exist
     */
    public Cart getCart(int visitorId) throws Exception
    {
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            throw  new Exception("Invalid Visitor ID");
        }
        return user.getCart();
    }

      public void appointNewStoreOwner(int appointerId, String appointedUserName, int storeId) throws Exception {//4.4
        //check appointerId and registerd user
        SiteVisitor appointer = onlineList.get(appointerId);
        //lock appointer
        //variable "appointedlock"
        //try
        if (!(appointer instanceof RegisteredUser)) {
            logger.warning("trying to appoint from invalide user ");
            throw  new Exception("inValid appointer Id");
        }
        // check if store id exist
        Store store = storesList.get(storeId);
        if (store == null) {
            logger.warning("null store warning ");
            throw  new Exception("inValid store Id");
        }
        if(!store.getActive()){
            logger.warning("add to store that is closed warning");
            throw  new Exception("this is closed Store");
        }
        // check appointer is owner of storeId
        Employment appointerEmployment = null;
        try {
            appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);//check this
            logger.warning("the appointer is not owner of store id warning");
        } catch (Exception e) {
            throw  new Exception("the appointer is not owner of store id");
        }

        if (appointerEmployment == null || !appointerEmployment.checkIfOwner()) {
            throw  new Exception("the appointer is not owner of store id");
        }
        // check if appointedUserName is registered
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if (appointed == null) {
            logger.warning("null appointer ");
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
        logger.fine("new store owner with name" + appointedUserName +" added successfully");
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
            logger.warning("trying to appoint from invalide user ");
            throw  new Exception("invalid appointer Id");
        }
        // check if store id exist
        Store store=storesList.get(storeId);
        if(store==null){
            logger.warning("null store warning ");
            throw  new Exception("inValid store Id");
        }
        if(!store.getActive()){
            logger.warning("closed store you cant work while its close");
            throw  new Exception("this is closed Store");
        }
        // check if appointer is owner (or founder) of storeId
        Employment appointerEmployment =null;
        try{
            appointerEmployment =employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
            logger.warning("the appointer is not owner of store id ");
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
        logger.fine("new store manager with name" + appointedUserName +" added successfully");
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
            logger.warning("invalid visitor warning ");
            throw  new Exception("invalid visitor Id");
        }

        //Check if storeID exists
        Store store=storesList.get(storeID);
        if(store==null){
            logger.warning("store is null ");
            throw  new Exception("invalid store Id");
        }
        if(!store.getActive()){
            logger.warning("store is closed ");
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
            logger.warning("null appointed ");
            throw  new Exception("invalid appointed UserName");
        }

        //Check if username is manager of storeID
        Employment appointedEmployment=null;
        try{
            appointedEmployment = employmentList.get(username).get(storeID);
        }catch (Exception e){
            logger.warning("invalid username warning ");
            throw  new Exception("The given username is not associated with any store");
        }

        if(appointedEmployment == null ||!appointedEmployment.checkIfManager()){
            throw  new Exception("The given username is not manager of the given store");
        }

        //Change permission
        for(Permission permission:permissions)
            appointedEmployment.togglePermission(permission);
        logger.config("all permission has been changed");
        logger.info("changes successfully");
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
            logger.warning("invalid visitor ID");
            throw  new Exception("invalid visitor Id");
        }

        if(!(appointer instanceof Admin)) {
            //is he storeowner
            try {
                employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
            } catch (Exception e) {
                logger.warning("the appointer is not owner of store");
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

    public synchronized LinkedList<String> purchaseCart(int visitorID,int visitorCard,String address) throws Exception{
        
        //Validate visitorID
        SiteVisitor visitor = onlineList.get(visitorID);
        if (visitor == null) {
            logger.warning("visitor is null");
            throw new Exception("Invalid Visitor ID");
        }

        LinkedList<String> failedPurchases = new LinkedList<>();


        for(Bag b : visitor.getCart().getBags().values()){

            //Calculate amount
            double amount = b.calculateTotalAmount();

            //Check if possible to create a supply
            if(!supplier.isValidAddress(address)){
                logger.fine("we can avoid this supply");
                failedPurchases.add(b.getStoreID().toString());
            }

            //Create a transaction for the store
            if(!paymentProvider.applyTransaction(amount,visitorCard)){
                failedPurchases.add(b.getStoreID().toString());
            }
            LinkedList<String> productsId = new LinkedList<>();
            productsId.add(b.bagToString());
            //Create a request to supply bag's product to customer
            if(!supplier.supplyProducts(productsId)){
                failedPurchases.add(b.getStoreID().toString());
            }
            else{
                if(visitor instanceof RegisteredUser){
                    ((RegisteredUser)visitor).addPurchaseToHistory(new InstantPurchase(visitor,productsId,amount));
                }
            }


       }
        return failedPurchases;

    }
    public void addStoreRate(int visitorID,int storeID,double rate) throws Exception {
        //Check if visitorID is logged in and registered to system
        logger.info("Entering method addStoreRate() with Visitor ID: " + visitorID +", Store ID: " + storeID + ", Rate: " + rate);

        SiteVisitor rater = onlineList.get(visitorID);
        if(!(rater instanceof RegisteredUser)){
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

        store.addRating(((RegisteredUser) rater).getUserName(),rate);
        logger.info("Successfully added store rate. Visitor ID: " + visitorID +", Store ID: " + storeID + ", Rate: " + rate);
    }
    
    
   
    public void addStoreRateAndComment(int visitorID,int storeID,int rate,String comment) throws Exception {
        //Check if visitorID is logged in and registered to system
        logger.info("Entering method addStoreRateAndComment() with visitorID: " + visitorID + ", storeID: " + storeID + ", rate: " + rate + ", and comment: " + comment);

        SiteVisitor rater = onlineList.get(visitorID);
        if(!(rater instanceof RegisteredUser)){
            logger.severe("Invalid visitor Id: " + visitorID);
            throw  new Exception("invalid visitor Id");
        }

        //Check if storeID exists
        Store store=storesList.get(storeID);
        if(store==null){
            logger.severe("Invalid Store Id: " + storeID);
            throw  new Exception("invalid store Id");
        }
        if(!store.getActive()){
            throw  new Exception("this is closed Store");
        }

        store.addRatingAndComment(((RegisteredUser) rater).getUserName(),rate,comment);
        logger.info("Exiting method addStoreRateAndComment() with success");

    }
   public void addProductRateAndComment(int visitorID,int productID,int storeId,int rate,String comment) throws Exception {
        //Check if visitorID is logged in and registered to system
        logger.info("Entering method addProductRateAndComment() with visitorID: " + visitorID + ", productID: " + productID + ", rate: " + rate + ", and comment: " + comment);

        SiteVisitor rater = onlineList.get(visitorID);
        if (!(rater instanceof RegisteredUser)) {
            logger.severe("Invalid visitor Id: " + visitorID);
            throw new Exception("invalid visitor Id");
        }

       // isValidProductId(productID);

       // int storeId = getStoreIdByProductId(productID);

        Store store = storesList.get(storeId);
        if (store == null) {
            logger.severe("Invalid product Id: " + productID);
            throw new Exception("Invalid product ID");
        }
        if (!store.getActive()) {
            throw new Exception("this is closed Store");
        }
        StoreProduct product = store.getProductByID(productID);//TO-DO(majd)
        if (product == null) {
            throw new Exception("Invalid product ID");
        }

        product.addRatingAndComment(((RegisteredUser) rater).getUserName(),rate,comment);
        logger.info("Exiting method addProductRateAndComment() with success");


    }



        //----------Store-----------
    // open Store
    public Integer OpenNewStore(int visitorId,String storeName) throws Exception {
        // check if register user
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof RegisteredUser)){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        //open new store ()
        Store store = new Store(storeName);
        // add to store list
        storesList.put(store.getID(),store);
        //new Employment
        Employment employment = new Employment((RegisteredUser) User,store,Role.StoreOwner);
        logger.config("adding new employment to the new store ");
        // andd to employment list
        if (employmentList.get(((RegisteredUser) User).getUserName()) == null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(((RegisteredUser) User).getUserName(), newEmploymentMap);
        }
        employmentList.get(((RegisteredUser) User).getUserName()).put(store.getID(),employment);
        logger.fine("open new store with name" + storeName+" done successfully");
        return store.getID();
    }
    
    //StoreRate
    public double GetStoreRate(int visitorId,int StoreId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(User==null){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        Store store = storesList.get(StoreId);
        if (store == null || !store.getActive()) {
            logger.warning(" store is null or the store is closed");
            throw  new Exception("there is no store with this id ");
        }
        return store.getRate();
    }

    //productRate
    public double GetStoreProductRate(int visitorId,Integer storeId,Integer productId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        if(User==null){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        Store store = storesList.get(storeId);
        if (store == null || !store.getActive()) {
            logger.warning(" store is null or the store is closed");
            throw  new Exception("there is no product with this id ");
        }

        return store.getProductByID(productId).getAverageRating();

    }
    //close store
     public void CloseStore(int visitorId, int StoreId) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //try
        if(! (User instanceof RegisteredUser)){
            logger.warning(" user is not registered");
            throw  new Exception("invalid visitor Id");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(((RegisteredUser) User).getUserName()).get(StoreId);
        }catch (Exception e){
            logger.warning("there is no store to this user");
            throw  new Exception("this user dont have any store");
        }

        if (employment == null) {
            logger.warning(" employment is null");
            throw new Exception("there is no employee with this id ");
        }
        if (employment.checkIfOwner()) {
            Store store = storesList.get(StoreId);
            if (store == null) {
                logger.warning(" store is null");
                throw  new Exception("there is no store with this id ");
            }
            logger.fine("store status changed from active to closed");
            logger.info("store is closed now");
            store.CloseStore();
           return;
        }

        throw  new Exception("Just the owner can Close the Store ");
        //catch
        //release lock user
        //throw e
    }
    
    // ניהול מלאי 4.1
    public Integer AddProduct(int visitorId,int storeId,String productName, double price, String category, int quantity,String description) throws Exception {
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //try
        if(! (User instanceof RegisteredUser)){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        Store store = storesList.get(storeId);
        if(store==null){
            logger.warning(" store is null");
            throw  new Exception("there is no store with this id ");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(((RegisteredUser) User).getUserName()).get(storeId);
        }catch (Exception e){
            logger.warning("user with no store");
            throw  new Exception("this user dont have any store");
        }
        if (employment == null){
            logger.warning("employment is null");
            throw  new Exception("there is no employee with this id ");
        }
        if (!employment.checkIfOwner()) {//check if need manager
            logger.warning("user are not the manager trying to add");
            throw  new Exception("you are not the owner of this store ");
        }
        return store.AddNewProduct(productName,price,quantity,category,description);
        //catch
        //release lock user
        //throw e
    }

    public void RemoveProduct(int visitorId,int storeId, int ProductId)  throws Exception {
        logger.fine("Entering method RemoveProduct() with visitorId: " + visitorId + ", ProductId: " + ProductId);
        SiteVisitor User = onlineList.get(visitorId);
        //lock user
        //variable productlock
        //try
        if(! (User instanceof RegisteredUser)){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(((RegisteredUser) User).getUserName()).get(storeId);
        }catch (Exception e){
            logger.severe("User does not have any store: " + visitorId);
            throw  new Exception("this user dont have any store");
        }
        if (employment == null)
            throw  new Exception("there is no employee with this id ");
        if (employment.checkIfOwner() || employment.checkIfStoreManager()) {
            Store store = storesList.get(storeId);
            if (store == null) {
                throw  new Exception("there is no store with this id ");
            }
            store.RemoveProduct(ProductId);
            //return new Response<>("the Product is successfully added", false);
        }
        logger.warning("Only the owner can close the store: " + visitorId);
        logger.fine("Exiting method RemoveProduct()");
        throw  new Exception("Just the owner can Close the Store ");

        //catch
        //release lock user
        //release productlock if locked
        //throw e
    }


    public void UpdateProductQuantity(int visitorId,int storeId, int productID,int quantity) throws Exception{
        //lock product (get product object)
        //try
                logger.fine("Entering method UpdateProductQuantity() with visitorId: " + visitorId + ", productID: " + productID + ", quantity: " + quantity);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productID);
        Store store = storesList.get(storeId);
        store.UpdateProductQuantity(productID,quantity);
                logger.fine("Exiting method UpdateProductQuantity()");

        //catch
        //release lock product
        //throw e

    }

    public void IncreaseProductQuantity(int visitorId, int productId,int storeId,int quantity) throws Exception{
        //lock product (get product object)
        //try
                logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", quantity: " + quantity);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        store.IncreaseProductQuantity(productId,quantity);
                logger.fine("Exiting method IncreaseProductQuantity()");

        //catch
        //release lock product
        //throw e

    }

    public void UpdateProductName(int visitorId, int productId,int storeId,String Name) throws Exception{
                logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", name: " + Name);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        store.UpdateProductName(productId,Name);
        logger.fine("Exiting method UpdateProductName()");

    }

    public void UpdateProductPrice(int visitorId, int productId,int storeId,double price) throws Exception{
        //lock product (get product object)
        //try
                logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", price: " + price);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        store.UpdateProductPrice(productId,price);
                logger.fine("Exiting method UpdateProductPrice()");

        //catch
        //release lock product
        //throw e

    }

    public void UpdateProductCategory(int visitorId, int productId,int storeId,String category) throws Exception{
                logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", category: " + category);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        store.UpdateProductCategory(productId,category);
                logger.fine("Exiting method UpdateProductCategory()");


    }

    public void UpdateProductDescription(int visitorId, int productId,int storeId,String description) throws Exception{
                logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", description: " + description);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(productId);
        store.UpdateProductDescription(productId,description);
        logger.fine("Exiting method UpdateProductDescription()");

    }

    private void checkifUserCanUpdateStoreProduct(int visitorId,int storeId, int productID) throws Exception {
        logger.info("Entering method checkifUserCanUpdateStoreProduct() with visitorId: " + visitorId + " and productID: " + productID);
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof RegisteredUser)){
            throw new Exception("invalid visitor Id");
        }
        Employment employment = null;
        try{
            employment = employmentList.get(((RegisteredUser) User).getUserName()).get(storeId);
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
            logger.info("Exiting method checkifUserCanUpdateStoreProduct() with success");
            //return new Response<>("the Product is successfully added", false);
        }
        logger.info("Exiting method checkifUserCanUpdateStoreProduct() with failure");

        throw new Exception("Just the owner can Close the Store ");
    }
    
    //2.2 search  product

    /**
     * can be done with filter search
     */
  //public String SearchProductByName( String Name) throws Exception{
  //      logger.info("Entering method SearchProductByName() with Name: " + Name);
  //      String output ="";
  //      try {
  //          for (Store store : storesList.values()) {
  //              output += store.SearchProductByName(Name).toString();
  //          }
  //      }
  //      catch (Exception e) {
  //          // Log the exception
  //          logger.log(Level.SEVERE, "An error occurred while searching product by name: " + Name, e);
  //          throw e; // Rethrow the exception
  //      }
  //      logger.info("Exiting method SearchProductByName() with output: " + output);
//
  //      return output;
  //  }
  //
    /**
     * can be done with filter search
     */
    //public String SearchProductByCategory( String Category) throws Exception {
    //    logger.info("Entering method SearchProductByCategory() with Category: " + Category);
    //    String output = "";
    //    try {
    //        for (Store store : storesList.values()) {
    //            output += store.SearchProductByCategory(Category).toString();
    //        }
    //    } catch (Exception e) {
    //        // Log the exception
    //        logger.log(Level.SEVERE, "An error occurred while searching product by category: " + Category, e);
    //        throw e; // Rethrow the exception
    //    }
    //    logger.info("Exiting method SearchProductByCategory() with output: " + output);
    //    return output;
    //}
    /**
     * can be done with filter search
     */
    //public List<String> SearchProductBykey( String key) {
//
    //    logger.info("Entering method SearchProductByKey() with Key: " + key);
//
    //    ArrayList<String> output = new ArrayList<>();
    //    try {
    //        for (Store store : storesList.values()) {
    //            for (StoreProduct product : store.SearchProductByKey(key)) {
    //                output.add(product.toString());
    //            }
    //        }
    //    } catch (Exception e) {
    //        // Log the exception
    //        logger.log(Level.SEVERE, "An error occurred while searching product by key: " + key, e);
    //    }
//
    //    logger.info("Exiting method SearchProductByKey() with output: " + output);
    //    return output;
//
    //}
    
//2.1
   public String GetInformation(int StoreId) throws Exception {
        logger.info("Entering method GetInformation() with StoreId: " + StoreId);
        Store store = storesList.get(StoreId);
        if (store == null) {
            logger.log(Level.SEVERE, "An error occurred while getting information for store with id: " + StoreId);
            throw new Exception("there is no store with this id ");
        }
        logger.info("Exiting method GetInformation()");
        return store.getInfo();
    }
    //6.4
   public List<String> GetStoreHistoryPurchase(int StoreId, int visitorId) throws Exception {
        logger.info("Entering method GetStoreHistoryPurchase() with StoreId: " + StoreId + " and visitorId: " + visitorId);
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof Admin)){
            logger.log(Level.SEVERE, "An error occurred while getting store purchase history for visitorId: " + visitorId);
            throw new Exception("invalid visitor Id");
        }
        Store store = storesList.get(StoreId);
        if (store == null) {
            logger.log(Level.SEVERE, "An error occurred while getting store purchase history. Store with id: " + StoreId + " not found.");
            throw new Exception("there is no store with this id ");
        }
        ArrayList<String> output=new ArrayList<>();
        LinkedList<Bag> history = store.GetStorePurchaseHistory();
        for(Bag bag:history)
            output.add(bag.toString());
        logger.info("Exiting method GetStoreHistoryPurchase() with output size: " + output.size());
        return output;
    }

    public String GetUserHistoryPurchase(String userName, int visitorId) throws Exception {
        logger.info("Entering method GetUserHistoryPurchase() with userName: " + userName + " and visitorId: " + visitorId);
        SiteVisitor admin = onlineList.get(visitorId);
        if(! (admin instanceof Admin)){
            logger.log(Level.SEVERE, "An error occurred while getting user purchase history for visitorId: " + visitorId);

            throw new Exception("invalid visitor Id");
        }
        RegisteredUser user = registeredUserList.get(userName);
        if(user==null){
            logger.log(Level.SEVERE, "An error occurred while getting user purchase history. User with userName: " + userName + " not found.");

            throw new Exception("There is no user with this user name");
        }

        logger.info("Exiting method GetUserHistoryPurchase() with purchase history: ");
        return user.getPurchaseHistory().getPurchases().toString();
    }
    /**
     *
     * @param filters list of filter object for whom each product has to pass all of them to be returned
     * @return list of products who passed the filter list
     */
    public List<StoreProduct> FilterProductSearch(List<Filter> filters) {
                logger.info("Entering method FilterProductSearch() with filters: " + filters.toString());

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
        logger.info("Exiting method FilterProductSearch() with filtered products: " + products.toString());
        return products;
    }
}
