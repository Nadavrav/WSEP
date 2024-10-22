package DomainLayer;


import DAL.TestsFlags;
import DomainLayer.Config.ConfigParser;
import DomainLayer.Stores.Bid;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Purchases.InstantPurchase;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.*;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ExternalServices.PaymentProvider;
import ExternalServices.Supplier;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;

//DAL IMPORTS
import DAL.DALService;
import DAL.DTOs.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Facade {
    private static volatile Facade instanceFacade = null;
    private  static final Logger logger = Logger.getLogger("Facade Logger");
    private final Map<Integer, SiteVisitor> onlineList;//online
    private final Map<String, RegisteredUser> registeredUserList;
    private final Map<Integer, Store> storesList;
    private final Map<String, Map<Integer, Employment>> employmentList;
    private final Map<Integer,Map<RegisteredUser,LinkedList<RegisteredUser>>> appointmentsRequests;
    private Supplier supplier;
    private PaymentProvider paymentProvider;
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
    private Boolean dataLoaded=false;
    private final Object dataSyncObj=new Object();

    private Map<Date,Integer> siteVisitorsEntriesManager;
    private Map<Date,Integer> nonWorkersEntriesManager;
    private Map<Date,Integer> managersOnlyEntriesManager;
    private Map<Date,Integer> storeOwnersEntriesManager;
    private Map<Date,Integer> adminsEntriesManager;



    private DALService DS;

    private Facade(){
      //  boolean dbHasData = false;
        if(!TestsFlags.getInstance().isTests()) {
            try {
                DS = DALService.getInstance();
               //       DS.deleteDBData();
                Store.setStoreIdCounter(DS.getMaxStoreId() + 1);
                Store.setProductIdCounter(DS.getMaxProductId() + 1);
               // dbHasData = DS.dbNotEmpty();
            } catch (SQLException e) {
                Store.resetCounters();
            }
        }
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        onlineList = new ConcurrentHashMap<>();
        registeredUserList = new ConcurrentHashMap<>();
        storesList = new ConcurrentHashMap<>();
        employmentList = new ConcurrentHashMap<>();
        appointmentsRequests = new ConcurrentHashMap<>();
        supplier= Supplier.getInstance();
        paymentProvider= PaymentProvider.getInstance();
        siteVisitorsEntriesManager = new HashMap<>();
        nonWorkersEntriesManager = new HashMap<>();
        managersOnlyEntriesManager = new HashMap<>();
        storeOwnersEntriesManager = new HashMap<>();
        adminsEntriesManager = new HashMap<>();
        try {
                if (!ConfigParser.parse(this) && !DALService.getInstance().adminExists())
                    registerInitialAdmin("admin", "admin12345");
        }
        catch (SQLException e){
            logger.severe("ERROR WHILE FETCHING/SAVING ADMIN IN DB");
        }
    }
    /**
     * get instance uses double-checking to prevent synchronization when getting a non-null instance,
     * improving runtime.
     * if instance is null, synchronizes and checks again.
     * (for specific runtime cases when 2 threads passed the 1st if)
     * @return Facade singleton instance
     */
    public static Facade getInstance() {
        if (instanceFacade == null) {
            synchronized (Facade.class) {
                if(instanceFacade==null)
                    instanceFacade = new Facade();
            }
        }
        return instanceFacade;
    }


    public Map<String, RegisteredUser> getRegisteredUserList(int visitorId) throws Exception {
        if(!(onlineList.get(visitorId) instanceof Admin))
            throw new Exception("Only admin can fetch user list");
        return registeredUserList;
    }
    /**
     * Function used by test inorder to reset the data in the facade and make the tests independent of one another.
     * Synced on the same object as loadData
     */
    public void resetData() {
        synchronized (dataSyncObj) {
            Store.resetCounters();
            onlineList.clear();
            registeredUserList.clear();
            storesList.clear();
            employmentList.clear();
            dataLoaded = false;
            supplier = new Supplier();
            paymentProvider = new PaymentProvider();
            try {
                DS = DALService.getInstance();
                DS.deleteDBData();
            } catch (Exception e) {
                // do nothing
            }
            //registerInitialAdmin();

        }
    }
    public void loadData() throws Exception {
        //  synchronized (dataSyncObj) {
        //      if (dataLoaded)
        //          return;
        //      // resetData();
        //    //  if (!ConfigParser.parse(this))
        //      registerInitialAdmin("admin", "admin12345");
        //      try {
//                //New users
//                int nadavID = enterNewSiteVisitor();
//                int nadiaID = enterNewSiteVisitor();
//                int natalieID = enterNewSiteVisitor();
//                int majdID = enterNewSiteVisitor();
//                int denisID = enterNewSiteVisitor();
//                int nikitaID = enterNewSiteVisitor();
//
//
//                register(nadavID, "Nadav", "123456789");
//                register(nadiaID, "Nadia", "123456789");
//                register(natalieID, "Natalie", "123456789");
//                register(majdID, "Majd", "123456789");
//                register(denisID, "Denis", "123456789");
//                register(nikitaID, "Nikita", "123456789");
//
//                login(nadavID, "Nadav", "123456789");
//                login(nadiaID, "Nadia", "123456789");
//                login(natalieID, "Natalie", "123456789");
//                login(majdID, "Majd", "123456789");
//                login(denisID, "Denis", "123456789");
//                login(nikitaID, "Nikita", "123456789");
//
//                //New Stores
//                int nadavStoreID = OpenNewStore(nadavID, "NadavStore");
//                int nadiaStoreID = OpenNewStore(nadiaID, "NadiaStore");
//                int natalieStoreID = OpenNewStore(natalieID, "NatalieStore");
//                int majdStoreID = OpenNewStore(majdID, "MajdStore");
//                int denisStoreID = OpenNewStore(denisID, "DenisStore");
//                int nikitaStoreID = OpenNewStore(nikitaID, "NikitaStore");
//
//                //New Products
//
//                //new
//                AddProduct(nadavID, nadavStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(nadavID, nadavStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(nadavID, nadavStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(nadavID, nadavStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(nadavID, nadavStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                AddProduct(nadiaID, nadiaStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(nadiaID, nadiaStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(nadiaID, nadiaStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(nadiaID, nadiaStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(nadiaID, nadiaStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                AddProduct(natalieID, natalieStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(natalieID, natalieStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(natalieID, natalieStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(natalieID, natalieStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(natalieID, natalieStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                AddProduct(majdID, majdStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(majdID, majdStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(majdID, majdStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(majdID, majdStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(majdID, majdStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                AddProduct(denisID, denisStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(denisID, denisStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(denisID, denisStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(denisID, denisStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(denisID, denisStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                AddProduct(nikitaID, nikitaStoreID, "Milk 15%", 6, "Milk", 1, "Freshly milked");
//                AddProduct(nikitaID, nikitaStoreID, "Bread", 10, "Wheat Foods", 1, "Made of whole wheat");
//                AddProduct(nikitaID, nikitaStoreID, "Yogurt", 15, "Dairy", 1, "Extra chunky");
//                AddProduct(nikitaID, nikitaStoreID, "Chicken", 30, "Meat", 1, "40% chicken");
//                AddProduct(nikitaID, nikitaStoreID, "Steak", 100, "Meat", 1, "May contain peanuts");
//
//                //old
//                AddProduct(nadiaID, nadiaStoreID, "Orange Juice", 16, "Juice", 90, "Good juice");
//                AddProduct(natalieID, natalieStoreID, "Apples", 900, "Fruits", 1, "Good apples");
//                AddProduct(majdID, majdStoreID, "Milk", 6, "Milk", 30, "Good milk");
//                AddProduct(denisID, denisStoreID, "Milk", 6, "Milk", 30, "Good milk");
//                AddProduct(nikitaID, nikitaStoreID, "Milk", 6, "Milk", 30, "Good milk");
//                //new Discounts
//                // NadavStore discounts:
//                 addDiscount(new BasicDiscount("10% Discount on steaks!",1,10,new NameCondition("Steak")),nadavStoreID);
//                addDiscount(new BasicDiscount("5% Discount on milk!",2,10,new NameCondition("Milk")),nadavStoreID);// MinQuantityCondition minQuantityCondition=new MinQuantityCondition(2);
//                // MultiAndCondition nameAndMinQuantityCondition=new MultiAndCondition();
//                // nameAndMinQuantityCondition.addCondition(new NameCondition("Steak"));
//                // nameAndMinQuantityCondition.addCondition(minQuantityCondition);
//                // AdditiveDiscount nadavAdditiveDiscount=new AdditiveDiscount("10% Discount on steaks and another 50% discount on steaks if you buy 2",1);
//                // nadavAdditiveDiscount.addDiscount(new BasicDiscount("10% Discount on steaks!",1,10,new NameCondition("Steak")));
//                // nadavAdditiveDiscount.addDiscount(new BasicDiscount("50% Discount on steaks if you buy 2",1,50,nameAndMinQuantityCondition));
//                // addDiscount(nadavAdditiveDiscount,nadavStoreID);
//                // // NadiaStore discounts:
//                // BooleanAfterFilterCondition nadiabreadCondition=new BooleanAfterFilterCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
//                // BooleanAfterFilterCondition nadiadairyCondition=new BooleanAfterFilterCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
//                // AndCondition andCondition=new AndCondition(nadiabreadCondition,nadiadairyCondition);
//                // MultiAndCondition nadiaMultiAndCondition=new MultiAndCondition();
//                // nadiaMultiAndCondition.addCondition(andCondition);
//                // nadiaMultiAndCondition.addCondition(new CategoryCondition("Meat"));
//                // BasicDiscount nadiaDiscount =new BasicDiscount(" 50% discount on all meat products if you buy least 5 bread loafs and also at least 6 dairy products",1,50,nadiaMultiAndCondition);
//                // CategoryCondition dairyCategoryCondition=new CategoryCondition("Dairy");
//                // BasicDiscount storeDiscount=new BasicDiscount("20% on the whole store",1,20);
//                // BasicDiscount dairyDiscount =new BasicDiscount("50% discount dairy products",2,50,dairyCategoryCondition);
//                // AdditiveDiscount additiveDiscount=new AdditiveDiscount("20% discount on the whole store, and 5 discount on all dairy products",3);
//                // additiveDiscount.addDiscount(storeDiscount);
//                // additiveDiscount.addDiscount(dairyDiscount);
//                // addDiscount(nadiaDiscount,nadiaStoreID);
//                // addDiscount(additiveDiscount,nadiaStoreID);
//                // // NatalieStore discounts:
//                // BooleanAfterFilterCondition natalieBreadCondition=new BooleanAfterFilterCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
//                // BooleanAfterFilterCondition natalieDairyCondition=new BooleanAfterFilterCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
//                // OrCondition natalieorCondition=new OrCondition(natalieBreadCondition,natalieDairyCondition);
//                // MultiAndCondition NataliemultiAndCondition=new MultiAndCondition();
//                // NataliemultiAndCondition.addCondition(natalieorCondition);
//                // NataliemultiAndCondition.addCondition(new CategoryCondition("Meat"));
//                // BasicDiscount basicDiscount =new BasicDiscount(" 50% meat discount if you buy least 5 breads or at least 6 dairy products",1,50,NataliemultiAndCondition);
//                // addDiscount(basicDiscount,natalieStoreID);
////
//                // // MajdStore discounts:
//                // BooleanAfterFilterCondition majdBreadCondition=new BooleanAfterFilterCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
//                // BooleanAfterFilterCondition majdDairyCondition=new BooleanAfterFilterCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
//                // XorCondition majdXorCondition=new XorCondition(majdBreadCondition,majdDairyCondition);
//                // MultiAndCondition majdMultiAndCondition=new MultiAndCondition();
//                // majdMultiAndCondition.addCondition(majdXorCondition);
//                // majdMultiAndCondition.addCondition(new CategoryCondition("Meat"));
////
//                // BasicDiscount majdBasicDiscount =new BasicDiscount(" 50% meat discount if you buy least 5 breads or at least 6 dairy products, but not both",1,50,majdMultiAndCondition);
//
//                // addDiscount(majdBasicDiscount,majdStoreID);
//                // DenisStore discounts:
//                // BooleanAfterFilterCondition denisMinYogurtAmount=new BooleanAfterFilterCondition(new NameCondition("Yogurt"),new MinTotalProductAmountCondition(3));
//                // MinBagPriceCondition denisMinBagPriceCondition=new MinBagPriceCondition(200);
//                // AndCondition denisAndCondition=new AndCondition(denisMinBagPriceCondition,denisMinYogurtAmount);
//                // FilterOnlyIfCondition denisCondition=new FilterOnlyIfCondition(denisAndCondition,new CategoryCondition("Dairy"));
//                // BasicDiscount denisBasicDiscount =new BasicDiscount("If the value of the basket is higher than NIS 200" +
////
//                //        " and the basket also contains at least 3 yogurts, then there is a 50% discount on dairy products",1,50,denisCondition);
////
//                //addDiscount(denisBasicDiscount,denisStoreID);
//                // NikitaStore discounts:
//                NameCondition milkCondition = new NameCondition("Milk 15%");
//                CategoryCondition meatCategoryCondition = new CategoryCondition("Meat");
//                BasicDiscount meatsDiscount = new BasicDiscount("20% discount on all 15% milk cartons", 1, 20, milkCondition);
//                BasicDiscount milkDiscount = new BasicDiscount("25% discount on all meat products", 2, 25, meatCategoryCondition);
//                MaxSelectiveDiscount maxSelectiveDiscount = new MaxSelectiveDiscount("20% discount on all milk cartons or 25% discount on all meat products, the larger of them", 3);
//                maxSelectiveDiscount.addDiscount(meatsDiscount);
//                maxSelectiveDiscount.addDiscount(milkDiscount);
//                //add policies
//                //BooleanAfterFilterCondition policyBreadCondition=new BooleanAfterFilterCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(3));
//                //BooleanAfterFilterCondition policyDairyCondition=new BooleanAfterFilterCondition(new CategoryCondition("Dairy"),new MaxTotalProductAmountCondition(5));
//                //BooleanAfterFilterCondition policyMeatCondition=new BooleanAfterFilterCondition(new CategoryCondition("Steak"),new DateCondition(15));
//                //    appointNewStoreOwner(nadavID,"Denis",0);
//                //    appointNewStoreOwner(1,"Nadia",0);
//                //    acceptEmploymentRequest(denisID,0,"Nadia");
//                //Policy DairyPolicy=new Policy("you have to take at least 3 loafs of bread",policyDairyCondition);
//                //Policy BreadPolicy=new Policy("you can buy at most 5 dairy products",policyBreadCondition);
//                //Policy Meatpolicy=new Policy("you can buy steaks only on the 15th day of the month",policyMeatCondition);
//                //Policy BagPolicy=new Policy("you cart price must be above 50 or contains at least 5 products",new AndCondition(new MinBagPriceCondition(50),new MinTotalProductAmountCondition(5)));
//                //addPolicy(DairyPolicy,nadavStoreID);
//                //addPolicy(BreadPolicy,nadavStoreID);
//                //addPolicy(Meatpolicy,nadiaStoreID);
//                //addPolicy(BreadPolicy,denisStoreID);
//                //addPolicy(BreadPolicy,nadiaStoreID);
//                //addPolicy(BagPolicy,denisStoreID);
//                //addPolicy(BagPolicy,nadiaStoreID);
//                //addPolicy(BagPolicy,natalieStoreID);
//
//                logout(nadavID);
//                logout(nadiaID);
//                logout(natalieID);
//                logout(majdID);
//                logout(denisID);
//                logout(nikitaID);
//                dataLoaded = true;
//
//
//        }
//        catch(Exception e){
//            throw new Exception(e);
//        }

        //  }
    }
//------------UserPackege-----------------------
    public int enterNewSiteVisitor() throws Exception {//1.1
            SiteVisitor visitor = new SiteVisitor();
            onlineList.put(visitor.getVisitorId(), visitor);
            increaseEntry(siteVisitorsEntriesManager);
            logger.info("A new visitor with Id:" + visitor.getVisitorId() + "has Enter");
            return visitor.getVisitorId();
    }
    public boolean isLoggedIn(int visitorId) {
        SiteVisitor siteVisitor= onlineList.get(visitorId);
        if(siteVisitor==null){
            logger.severe("ID ERROR IN isLoggedIn- visitorId not online");
            throw new RuntimeException("User is not online");
        }
        return siteVisitor instanceof RegisteredUser;
    }
    public String getUserName(int visitorId) {
        if(!onlineList.containsKey(visitorId)){
            logger.severe("Error: get username called for not logged in user or logged in user not in online list");
            throw new RuntimeException("System Error: check error log");
        }
        else{
            return ((RegisteredUser)onlineList.get(visitorId)).getUserName();
        }
    }
    public void exitSiteVisitor(int id) throws Exception {//1.2
        if(onlineList.containsKey(id)) {
            SiteVisitor st = onlineList.get(id);
            if(!(st instanceof RegisteredUser))
                st.ExitSiteVisitor(id);
            onlineList.remove(id);
            logger.info("A  visitor with Id:" + id + "has Exit");
        }
        else {
            logger.info("Tried to exit A  visitor with Id:" + id + ", this id doesnt exist in online list");
            throw new IllegalArgumentException("No online user with this id");
        }
    }
    public void registerAdmin(int visitorid,String userName,String password) throws Exception{
        DS = DALService.getInstance();
        logger.info("Starting admin registration");
        if(!(onlineList.get(visitorid) instanceof Admin)){
            logger.info("Failed admin registration: only admin can register other admins");
            throw new Exception("Only admins can register another admin");
        }
        if(registeredUserList.containsKey(userName)) {
            logger.info("Failed admin registration: username already exists");
            throw new Exception("Username " + userName + " already exists");
        }
        else{
            Admin admin=new Admin(userName, password);

            //DB save
            DS.saveUser(admin.getUserName(), admin.getPassword());
            DS.saveAdmin(admin.getUserName());
            //DB end save
            registeredUserList.replace(userName,admin);
            logger.fine("New admin successfully registered");
            registeredUserList.get(userName).update("You are an Admin now!");
        }

    }

    public boolean isAdmin(int visitorID){
        return onlineList.get(visitorID) instanceof Admin;
    }

    public void registerInitialAdmin(String userName,String password) {
        logger.info("Starting initial admin registration");
        if(registeredUserList.containsKey(userName)) {
            logger.severe("Failed initial admin registration: username already exists." +
                    "should not happen");
            throw new RuntimeException("Username " + "admin" + " already exists");
        }
        else{
            try {
                DS = DALService.getInstance();
                Admin admin = new Admin(userName, password);

                //DB save
                DS.saveUser(admin.getUserName(), admin.getPassword());
                DS.saveAdmin(admin.getUserName());
                //DB end save

                registeredUserList.put(userName,admin);
            }
            catch (Exception e) {
                logger.severe("Unexpected error during initial admin registration");
                throw new RuntimeException(e);
            }
            logger.fine("Initial admin successfully registered");
        }

    }
    public void register(int visitorId, String userName, String password) throws Exception {//1.3
        DS = DALService.getInstance();
        //Valid visitorID
        if (!SiteVisitor.checkVisitorId(visitorId)) {
            logger.warning("maybe we have a null visitor!");
           throw  new Exception("Invalid Visitor ID");
        }
            //unique userName
            if (registeredUserList.get(userName) != null) {
                throw new Exception("This userName already taken");
            }
        //checking if the user exists in DB
        registeredUserDTO userDTO = DS.getUser(userName);
        if(userDTO != null) {
            if(DS.isAdmin(userName)) {
                Admin admin = new Admin(userDTO);
                registeredUserList.put(userName, admin);
            }
            else {
                RegisteredUser r = new RegisteredUser(userDTO);
                registeredUserList.put(userName, r);
            }
            throw new Exception("This userName already taken");
        }


        //get site visitor object
        //SiteVisitor visitor = onlineList.get(visitorId);
       // visitor =new RegisteredUser(visitor, userName, password);
        //onlineList.put(visitorId,visitor);
        // create new register user
        logger.info("new visitor has register");
        RegisteredUser r = new RegisteredUser(userName, password);

        //Save into Db
        DS.saveUser(r.getUserName(),r.getPassword());
        //End save

        //onlineList.replace(visitorId,r);
        registeredUserList.put(userName, r);
        registeredUserList.get(userName).update("Registered to Site");
    }

    public void login(int visitorId, String userName, String password) throws Exception {//1.4
        //
        RegisteredUser user;
        user = registeredUserList.get(userName);
        boolean b = registeredUserList.get("admin") != null;
        if (!SiteVisitor.checkVisitorId(visitorId)) {//check if the user is entered to the system
            logger.warning("User IS NOT Entered in the system");
            throw new Exception("Invalid Visitor ID");
        }
        if (user == null) {//check if he has account
            registeredUserDTO userDTO = DS.getUser(userName);
            if(userDTO != null) {
                if(DS.isAdmin(userName)){
                    user = new Admin(userDTO);
                }
                else {
                    user = new RegisteredUser(userDTO);
                }
                registeredUserList.put(userName, user);
            }
            else {
                logger.warning("User is already have an account");
                throw new Exception("UserName not Found");
            }
        }
        logger.info("User log in successfully");
        user.login(password, visitorId);
        onlineList.replace(visitorId, user);

        if(isOwner(user.getUserName())){
            increaseEntry(storeOwnersEntriesManager);
        }
        else if(isManagerButNotOwner(user.getUserName())){
            increaseEntry(managersOnlyEntriesManager);
        }
        else {
            increaseEntry(nonWorkersEntriesManager);
        }

        if(isAdmin(visitorId)){
            increaseEntry(adminsEntriesManager);
        }

    }

    private boolean isOwner(String userName) {
        if(employmentList.get(userName) == null)
        {
            return false;
        }
        for (Integer storeID:employmentList.get(userName).keySet()) {
            if(employmentList.get(userName).get(storeID).checkIfOwner()){
                return true;
            }
        }
        return false;
    }

    private boolean isManagerButNotOwner(String userName) {
        if(employmentList.get(userName) == null)
        {
            return false;
        }
        boolean isManager = false;
        for (Integer storeID:employmentList.get(userName).keySet()) {
            if(employmentList.get(userName).get(storeID).checkIfManager()){
                isManager = true;
            }
            if(employmentList.get(userName).get(storeID).checkIfOwner()){
                return false;
            }
        }
        return isManager;
    }

    public int logout(int visitorId) throws Exception {//3.1
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

     public void addProductToCart(int productId, int storeId, int amount, int visitorId) throws Exception {//2.3
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("trying to add product from null user");
            throw  new Exception("Invalid Visitor ID");
        }
        //Get product lock
        try {
            Store store = storesList.get(storeId);
            if(store==null)
                store=fetchStoreIfExists(storeId);
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
            if(user instanceof RegisteredUser)
                DALService.getInstance().saveCartProduct(productId,((RegisteredUser)user).getUserName(),amount);
            user.addProductToCart(storeId, product,amount, store.generateStoreCallback());
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
            if(store==null)
                fetchStoreIfExists(storeId);
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
            if(user instanceof RegisteredUser)
                DALService.getInstance().removeCartProduct(productId,((RegisteredUser)user).getUserName());
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
            if(store==null)
                store=fetchStoreIfExists(storeId);
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
            if(user instanceof RegisteredUser)
                DALService.getInstance().updateCartProduct(productId,((RegisteredUser)user).getUserName(),newAmount);
            user.changeCartProductQuantity(storeId, product,newAmount);
        } catch (Exception e) {
            //release lock
            logger.warning("Error occurred: " + e.getMessage());
            throw e;
        }
    }

private void fetchCartIfExists(RegisteredUser user){
        try {
            if (user.getCart().getBags().isEmpty()) {
                Collection<CartProductDTO> products = DS.getCartProducts(user.getUserName());
                for (CartProductDTO cartProductDTO : products) {
                    int storeId = cartProductDTO.getStoreProduct().getStoreId();
                    if (!storesList.containsKey(storeId)) {
                        StoreDTO storeDTO = DS.getStoreById(storeId);
                        if (storeDTO == null) {
                            throw new RuntimeException("User has product that belongs to a non existent store");
                        }
                        storesList.put(storeId, new Store(storeDTO));
                    }
                    user.addProductToCart(storeId,storesList.get(storeId).getProductByID(cartProductDTO.getProductId()), cartProductDTO.getAmount(),storesList.get(storeId).generateStoreCallback());
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
}
    public Cart getProductsInMyCart(int visitorId) throws Exception {//2.4
        SiteVisitor user = onlineList.get(visitorId);
        if(user instanceof RegisteredUser)
            fetchCartIfExists((RegisteredUser) user);
        if (user == null) {
            logger.warning("trying to add from a null user");
            throw  new Exception("Invalid Visitor ID");
        }

        return user.getCart();
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
        if(user instanceof RegisteredUser)
            fetchCartIfExists((RegisteredUser) user);
        return user.getCart();
    }

      public void appointNewStoreOwner(int appointerId, String appointedUserName, int storeId) throws Exception {//4.4
        DS = DALService.getInstance();
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
            //TODO get store from db - DONE
            StoreDTO storeDTO = DS.getStoreById(storeId);
            if(storeDTO != null) {
                store = new Store(storeDTO);
                storesList.put(storeId,store);
            }
            else {
                logger.warning("null store warning ");
                throw new Exception("inValid store Id");
            }
        }
        if(!store.getActive()){
            logger.warning("add to store that is closed warning");
            throw  new Exception("this is closed Store");
        }
        // check appointer is owner of storeId
        Employment appointerEmployment = null;
        if(employmentList.get(((RegisteredUser) appointer).getUserName()) != null)
        {
            appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
        }
        else {
            //TODO get employment from db by appointer username and storeId - done
            EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(((RegisteredUser) appointer).getUserName(),storeId);
            //TODO if the DTO we get is null then throw the exception else - done
            if(employmentDTO == null){
                logger.warning("the appointer is not owner of store id warning");
                throw new Exception("the appointer is not owner of store id");
            }
            //TODO create it in the employment list and set appointerEmployment to be the DTO - done
            else{
                appointerEmployment = new Employment(employmentDTO);
                Map<Integer, Employment> newEmploymentMap = new HashMap<>();
                employmentList.put(((RegisteredUser) appointer).getUserName(), newEmploymentMap);
                employmentList.get(((RegisteredUser) appointer).getUserName()).put(storeId,appointerEmployment);
            }
        }

        if (appointerEmployment == null || !appointerEmployment.canAppointOwner()) {
            //TODO get employment from db by appointer username and storeId(this could happen if the - done
            EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(((RegisteredUser) appointer).getUserName(),storeId);
            if(employmentDTO == null){
                logger.warning("the appointer is not owner of store id warning");
                throw new Exception("the appointer is not owner of store id");
            }
            else{
                appointerEmployment = new Employment(employmentDTO);
                employmentList.get(((RegisteredUser) appointer).getUserName()).put(storeId,appointerEmployment);
                if(!appointerEmployment.canAppointOwner())
                {
                    throw  new Exception("User cannot appoint store owner");
                }
            }
            //TODO appointer has an appointment but not at this store(in the runtime) so we need to pull from db to make sure) - done
        }
        // check if appointedUserName is registered
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if (appointed == null) {
            //TODO pull user from db to check if he exists if doesnt throw exception - done
            registeredUserDTO userDTO = DS.getUser(appointedUserName);
            if(userDTO != null) {
                if(DS.isAdmin(appointedUserName)){
                    appointed = new Admin(userDTO);
                }
                else {
                    appointed = new RegisteredUser(userDTO);
                }
                registeredUserList.put(appointedUserName, appointed);
            }
            else {
                logger.warning("null appointer ");
                throw  new Exception("inValid appointed UserName");
            }
        }
        // check if appointedUserName has appointment with storeId as storeOwner
        Employment appointedEmployment = null;
        boolean hadEmployment = false;
        if(employmentList.get(appointedUserName) != null)
        {
            appointedEmployment = employmentList.get(appointedUserName).get(storeId);
            if(appointedEmployment == null)
                hadEmployment = true;
        }
        else{
            //TODO get from db with apointedUsername and storeId to make sure he doesnt have an appointment at this store - done
            EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(appointedUserName,storeId);
            if(employmentDTO != null)
                appointedEmployment = new Employment(employmentDTO);
        }
        if(appointedEmployment!=null && appointedEmployment.checkIfOwner()){
            throw  new Exception("appointedUserName is already Owner of store Id");
        }
        //TODO load all storeowners of this storeId into the runtime -done
        LinkedList<registeredUserDTO> owners = DS.getStoreOwnersByStoreId(storeId);
        if(owners!=null && !owners.isEmpty())
        {
            for (registeredUserDTO userDTO: owners) {
                if(!(registeredUserList.containsKey(userDTO.getUserName()))){
                    //meaning this user isnt in the runtime
                    if(DS.isAdmin(userDTO.getUserName()))
                    {
                        Admin toAdd = new Admin(userDTO);
                        registeredUserList.put(toAdd.getUserName(),toAdd);
                        //todo set them as listeners - done
                        store.addNewListener(toAdd);
                        store.addNewOwnerListener(toAdd);
                    }
                    else {
                        RegisteredUser toAdd = new RegisteredUser(userDTO);
                        registeredUserList.put(toAdd.getUserName(), toAdd);
                        //todo set them as listeners - done
                        store.addNewListener(toAdd);
                        store.addNewOwnerListener(toAdd);
                    }
                }
                //todo load their employment if needed
                if(employmentList.get(userDTO.getUserName()) == null && employmentList.get(userDTO.getUserName()).get(storeId) == null)
                {
                    //meaning that he is an owner but his employment isnt loded
                    EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(userDTO.getUserName(),storeId);
                    if(employmentList.get(userDTO.getUserName()) == null)
                    {
                        Map<Integer, Employment> newEmploymentMap = new HashMap<>();
                        employmentList.put(((RegisteredUser) appointer).getUserName(), newEmploymentMap);
                    }
                    Employment employmenttoAdd = new Employment(employmentDTO);
                    employmentList.get(userDTO.getUserName()).put(storeId,employmenttoAdd);

                }
                //in the case the if is false that means the the employment is already loaded so we dont need to load it
            }
        }
        //add appointedUserName as store owner
          System.out.println("got past all the dal work");
          if(appointmentsRequests.get(storeId) == null){
              appointmentsRequests.put(storeId,new HashMap<>());
          }
          appointmentsRequests.get(storeId).put(appointed,new LinkedList<>());
          appointmentsRequests.get(storeId).get(appointed).add((RegisteredUser) appointer);

          appointmentsRequests.putIfAbsent(storeId, new HashMap<>());
          appointmentsRequests.get(storeId).put(appointed, new LinkedList<>());
          if(!appointerEmployment.checkIfManager()) {
              appointmentsRequests.get(storeId).get(appointed).add((RegisteredUser) appointer);
          }

          store.notifyOwnersAboutNewEmploymentRequests(((RegisteredUser) appointer).getUserName(),appointedUserName);

          System.out.println("got into this code");
          if(checkIfAllOwnersAgreedOnEmploymentRequest(storeId,appointedUserName)){
              appointedEmployment = new Employment(((RegisteredUser) appointer).getUserName(), appointed.getUserName(), store.getID(), Role.StoreOwner);
              if (employmentList.get(appointedUserName) == null) {
                  Map<Integer, Employment> newEmploymentMap = new HashMap<>();
                  employmentList.put(appointedUserName, newEmploymentMap);
              }

              if(hadEmployment){
                  //update Db
                  DS.updateEmployment(appointedEmployment.getEmployee(), appointedEmployment.getStore(), appointedEmployment.getAppointer(), appointedEmployment.getRole().ordinal(), appointedEmployment.getPermissionString());
                  //end update
              }
              else {
                  //save into db
                  DS.saveEmployment(appointedEmployment.getEmployee(), appointedEmployment.getStore(), appointedEmployment.getAppointer(), appointedEmployment.getRole().ordinal(), appointedEmployment.getPermissionString());
                  //end save into db
              }

              employmentList.get(appointedUserName).put(storeId, appointedEmployment);
              store.addNewListener(appointed);
              store.addNewOwnerListener(appointed);
              store.documentOwner(appointed.getUserName());
              appointmentsRequests.get(storeId).remove(appointed);
              store.notifyOwnersAboutNewAppointmentSucceed(appointedUserName);
              logger.fine("new store owner with name" + appointedUserName +" added successfully");
              registeredUserList.get(appointedUserName).update("You are Owner of the store '"+storesList.get(storeId).getName()+"'");
          }

//          System.out.println(appointmentsRequests.values());
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
       if (store == null) {
           //TODO get store from db - DONE
           StoreDTO storeDTO = DS.getStoreById(storeId);
           if(storeDTO != null) {
               store = new Store(storeDTO);
               storesList.put(storeId,store);
           }
           else {
               logger.warning("null store warning ");
               throw new Exception("inValid store Id");
           }
       }
        if(!store.getActive()){
            logger.warning("closed store you cant work while its close");
            throw  new Exception("this is closed Store");
        }
        // check if appointer is owner (or founder) of storeId
        Employment appointerEmployment =null;
       if(employmentList.get(((RegisteredUser) appointer).getUserName()) != null)
       {
           appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
       }
       else {
           //TODO get employment from db by appointer username and storeId - done
           EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(((RegisteredUser) appointer).getUserName(),storeId);
           //TODO if the DTO we get is null then throw the exception else - done
           if(employmentDTO == null){
               logger.warning("the appointer is not owner of store id warning");
               throw new Exception("the appointer is not owner of store id");
           }
           //TODO create it in the employment list and set appointerEmployment to be the DTO - done
           else{
               appointerEmployment = new Employment(employmentDTO);
               Map<Integer, Employment> newEmploymentMap = new HashMap<>();
               employmentList.put(((RegisteredUser) appointer).getUserName(), newEmploymentMap);
               employmentList.get(((RegisteredUser) appointer).getUserName()).put(storeId,appointerEmployment);
           }
       }

       if (appointerEmployment == null || !appointerEmployment.canAppointManager()) {
           //TODO get employment from db by appointer username and storeId(this could happen if the - done
           EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(((RegisteredUser) appointer).getUserName(),storeId);
           if(employmentDTO == null){
               logger.warning("the appointer is not owner of store id warning");
               throw new Exception("the appointer is not owner of store id");
           }
           else{
               appointerEmployment = new Employment(employmentDTO);
               employmentList.get(((RegisteredUser) appointer).getUserName()).put(storeId,appointerEmployment);
               if(!appointerEmployment.canAppointManager())
               {
                   throw  new Exception("User cannot appoint store owner");
               }
           }
           //TODO appointer has an appointment but not at this store(in the runtime) so we need to pull from db to make sure) - done
       }
        // check if appointedUserName is registered to system
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
       if (appointed == null) {
           //TODO pull user from db to check if he exists if doesnt throw exception - done
           registeredUserDTO userDTO = DS.getUser(appointedUserName);
           if(userDTO != null) {
               if(DS.isAdmin(appointedUserName)){
                   appointed = new Admin(userDTO);
               }
               else {
                   appointed = new RegisteredUser(userDTO);
               }
               registeredUserList.put(appointedUserName, appointed);
           }
           else {
               logger.warning("null appointer ");
               throw  new Exception("inValid appointed UserName");
           }
       }
        // check if appointedUserName has appointment with storeId as storeOwner or as storeManager
       Employment appointedEmployment=null;
       boolean hadEmployment = false;
       if(employmentList.get(appointedUserName) != null)
       {
           appointedEmployment = employmentList.get(appointedUserName).get(storeId);
           if(appointedEmployment == null)
               hadEmployment = true;
       }
       else{
           //TODO get from db with apointedUsername and storeId to make sure he doesnt have an appointment at this store - done
           EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(appointedUserName,storeId);
           if(employmentDTO != null)
               appointedEmployment = new Employment(employmentDTO);
       }

        if(appointedEmployment!=null && (appointedEmployment.checkIfOwner() || appointedEmployment.checkIfManager())){
            throw  new Exception("appointedUserName is already owner or manager of store Id");
        }
        //add appointedUserName as store manager
        appointedEmployment = new Employment(((RegisteredUser) appointer).getUserName(),appointed.getUserName(),store.getID(),Role.StoreManager);

       if(hadEmployment){
           //update Db
           DS.updateEmployment(appointedEmployment.getEmployee(), appointedEmployment.getStore(), appointedEmployment.getAppointer(), appointedEmployment.getRole().ordinal(), appointedEmployment.getPermissionString());
           //end update
       }
       else {
           //save into db
           DS.saveEmployment(appointedEmployment.getEmployee(), appointedEmployment.getStore(), appointedEmployment.getAppointer(), appointedEmployment.getRole().ordinal(), appointedEmployment.getPermissionString());
           //end save into db
       }

        if(employmentList.get(appointedUserName)== null) {
            Map<Integer, Employment> newEmploymentMap = new HashMap<>();
            employmentList.put(appointedUserName, newEmploymentMap);
        }
        employmentList.get(appointedUserName).put(storeId,appointedEmployment);
        store.addNewListener(appointed);
        logger.fine("new store manager with name" + appointedUserName +" added successfully");
       registeredUserList.get(appointedUserName).update("You are Manager of the store '"+storesList.get(storeId).getName()+"'");
//catch
        //release lock appointer
        //release lockappointed if locked
        //throw e

    }
    private void loadUserFromDb(String userName) throws Exception {
        //TODO pull user from db to check if he exists if doesnt throw exception - done
        if(!registeredUserList.containsKey(userName)) {
            registeredUserDTO userDTO = DS.getUser(userName);
            RegisteredUser user;
            if (userDTO != null) {
                if (DS.isAdmin(userName)) {
                    user = new Admin(userDTO);
                } else {
                    user = new RegisteredUser(userDTO);
                }
                registeredUserList.put(userName, user);
            }
        }
    }
    private Employment fetchEmploymentForDBIfExists(String username,int storeId) throws SQLException {
        Employment appointerEmployment =null;
        if(employmentList.get(username) != null)
        {
            appointerEmployment = employmentList.get(username).get(storeId);
            if(appointerEmployment != null)
                return appointerEmployment;
        }
        EmploymentDTO employmentDTO = DS.getEmploymentByUsernameAndStoreId(username,storeId);
        if(employmentDTO == null){
            return null;
        }
        else{
            appointerEmployment = new Employment(employmentDTO);
            return appointerEmployment;
        }
    }

    public void removeEmployee(int appointerId, String appointedUserName, int storeId) throws Exception{
        logger.info("Staring remove employee");
        SiteVisitor appointer = onlineList.get(appointerId);
        if (appointedUserName == null) {
            logger.severe("Null pointer username while removing user");
            throw new NullPointerException("null username");
        }
        if(!(appointer instanceof RegisteredUser)){
            logger.warning("remove failed: trying to remove from invalid user ");
            throw  new Exception("invalid remover Id");
        }
        // check if store id exist
        Store store=storesList.get(storeId);
        if(store == null)
            store = fetchStoreIfExists(storeId);
        if(store==null){
            logger.warning("remove failed: no store with "+storeId+" store id");
            throw  new Exception("invalid store Id");
        }
        if(!store.getActive()){
            logger.info("remove failed: store is closed");
            throw  new Exception("cant remove workers from closed store");
        }
       // Employment appointedEmployment = fetchEmploymentForDBIfExists(appointedUserName,storeId);
       //if(appointedEmployment != null)
       //{

       //}
       // else {
       //     throw new Exception("this userName not appointed to this store");
       // }
        try{
            Collection<EmploymentDTO> employmentDTOS=DALService.getInstance().getStoreEmployment(storeId);
            for(EmploymentDTO employmentDTO:employmentDTOS){
                if(!employmentList.containsKey(employmentDTO.getEmployee()))
                    employmentList.put(employmentDTO.getEmployee(),new HashMap<>());
                if(!employmentList.get(employmentDTO.getEmployee()).containsKey(employmentDTO.getStoreID()))
                    employmentList.get(employmentDTO.getEmployee()).put(employmentDTO.getStoreID(),new Employment(employmentDTO));
            }
            Employment appointedEmployment=employmentList.get(appointedUserName).get(storeId);
            if(appointedEmployment == null){
                throw new Exception("this userName not appointed to this store");
            }
            if(!appointedEmployment.getAppointer().equals(((RegisteredUser) appointer).getUserName())){
                throw new Exception("you are not the appointer to this user so you cant remove him");
            }
            DALService.getInstance().removeEmployee(appointedUserName,storeId);
            RemoveAllEmployee(appointedUserName,storeId);
            employmentList.get(appointedUserName).remove(storeId);
            if(appointedEmployment.checkIfOwner()){
                store.removeOwnerListener(registeredUserList.get(appointedUserName));
            }
            else {
                store.removeListener(registeredUserList.get(appointedUserName));
            }
            registeredUserList.get(appointedUserName).update("You are no longer an employee of store "+storesList.get(storeId).getName());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    private void RemoveAllEmployee(String appointerUserName, int storeId) {
        for(Map<Integer,Employment> employmentMap : employmentList.values()){
            Employment employment =employmentMap.get(storeId);
            if(employment!=null && employment.getAppointer()!=null && employment.getAppointer().equals(registeredUserList.get(appointerUserName))){
                String appointedUserName =employment.getEmployee();
                RemoveAllEmployee(appointedUserName,storeId);
                employmentList.get(appointedUserName).remove(storeId);
                if(employmentList.get(appointedUserName).isEmpty()){
                    employmentList.remove(appointerUserName);
                    storesList.get(storeId).addNewListener(registeredUserList.get(appointedUserName));
                    registeredUserList.get(appointedUserName).update("You are no longer employee of the store '"+storesList.get(storeId).getName()+"'");
                }
            }
        }
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
            throw  new Exception("the appointer is not owner of store");
        }

        if(appointerEmployment==null|| (!appointerEmployment.CanChangePermissionsForStoreManager())){
            throw  new Exception("the appointer is not owner of store");
        }

        //Check if username is registered to system
        RegisteredUser appointed = registeredUserList.get(username);
        if(appointed==null) {
            registeredUserDTO userDTO = DALService.getInstance().getUser(username);
            if (userDTO != null)
                if (DS.isAdmin(username)) {
                    appointed = new Admin(userDTO);
                } else {
                    appointed = new RegisteredUser(userDTO);
                }
            registeredUserList.put(username, appointed);
        }
        appointed = registeredUserList.get(username);
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
        DALService.getInstance().updateEmployment(appointedEmployment.getEmployee(),storeID,appointedEmployment.getAppointer(),0,appointedEmployment.getPermissionString());
        logger.config("all permission has been changed");
        logger.info("changes successfully");
        registeredUserList.get(username).update("Your permissions of store "+storesList.get(storeID).getName() + " has been changed!");
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
        if(!(appointer instanceof RegisteredUser user)){
            logger.warning("invalid visitor ID");
            throw  new Exception("invalid visitor Id");
        }
        Collection<EmploymentDTO> employmentDTOS=DALService.getInstance().getStoreEmployment(storeId);
        for(EmploymentDTO employmentDTO:employmentDTOS){
            if(!employmentList.containsKey(employmentDTO.getEmployee()))
                employmentList.put(employmentDTO.getEmployee(),new HashMap<>());
            if(!employmentList.get(employmentDTO.getEmployee()).containsKey(employmentDTO.getStoreID()))
                employmentList.get(employmentDTO.getEmployee()).put(employmentDTO.getStoreID(),new Employment(employmentDTO));
        }
        Employment appointerEmployment = employmentList.get(((RegisteredUser) appointer).getUserName()).get(storeId);
        if(!(appointer instanceof Admin)) {
            //is he storeowner
            try {

                if(!appointerEmployment.CanSeeStaffAndPermissions()){
                    throw new Exception("You do not have the permission to see staff and permissions.");
                }
            } catch (Exception e) {
                logger.warning("the appointer is not owner of store");
                throw  new Exception("the appointer is not owner of store id");
            }
        }
        //get employment
        String output="";
        for(Map<Integer,Employment> userEmployments : employmentList.values()){
            if(userEmployments.containsKey(storeId)){
               output+= userEmployments.get(storeId).toString() + '\n';        //employment.toString
            }
        }
        return output;
    }

    public synchronized LinkedList<String> purchaseCart(int visitorID,String holder,String visitorCard,String expireDate,int cvv,String id,String address, String city, String country, String zip) throws Exception{
        if(!supplier.handShake()){

            throw new Exception("supplier does not hand shake");

        }
        if(!paymentProvider.handShake()){
            throw new Exception("payment provider does not handshake");
        }


        //Validate visitorID
        SiteVisitor visitor = onlineList.get(visitorID);
        if (visitor == null) {
            logger.warning("visitor is null");
            throw new Exception("Invalid Visitor ID");
        }
        if(visitor instanceof RegisteredUser)
            fetchCartIfExists((RegisteredUser) visitor);
        LinkedList<String> failedPurchases = new LinkedList<>();

        Cart c1 = visitor.getCart();
        List<Bag> bags = c1.getBags().values().stream().toList();
        for(Bag b : bags) {

            //Calculate amount
            double amount = b.calculateTotalAmount();
            Store s = storesList.get(b.getStoreID());
            if(s==null)
                s=fetchStoreIfExists(b.getStoreID());
            if (!b.passesPolicy()) {
                failedPurchases.add("Some products' purchases were failed due to them not being passed the store policy. Those products are still in your cart.");
            }
            else {
                boolean foundProductWithLowQuantity = false;
                for (CartProduct p : b.getProducts()) {
                    if (s.getProduct(p).getQuantity()<p.getAmount()) {
                        foundProductWithLowQuantity = true;
                    }
                }
                if (foundProductWithLowQuantity) {
                    failedPurchases.add("Some products' purchases were failed due lack of quantity in the store. Those products are still in your cart.");
                } else {
                    //Check if possible to create a supply
                    if (supplier.supply(holder, address, city, country,zip).equals("-1")) {
                        logger.fine("we can avoid this supply");
                        failedPurchases.add("Some products' purchases were failed because the supplier didn't accept the request to supply those products. Those products are still in your cart.");
                    } else {
                        //Create a transaction for the store
                        if (paymentProvider.pay(holder,visitorCard,expireDate,cvv,id).equals("-1")) {

                            failedPurchases.add("Some products' purchases were failed because the payment provider didn't accept the request to pay. Those products are still in your cart.");
                        } else {
                            LinkedList<String> productsId = new LinkedList<>();
                            productsId.add(b.bagToString());
                            //Create a request to supply bag's product to customer

                            for (CartProduct p : b.getProducts()) {
                                s.ReduceProductQuantity(s.getProduct(p).getProductId(),p.getAmount());
                            }
                            InstantPurchase p = new InstantPurchase(visitor, b, amount);
                            if (visitor instanceof RegisteredUser user) {
                                user.addPurchaseToHistory(p);
                                storesList.get(b.getStoreID()).NewBuyNotification((user.getUserName()));
                                user.removeBag(b.getStoreID());
                            }
                            else{
                                storesList.get(b.getStoreID()).NewBuyNotification("A site visitor (with visitor ID :"+visitorID+")");
                                visitor.removeBag(b.getStoreID());
                            }
                            storesList.get(b.getStoreID()).addToStoreHistory(p);
                        }
                    }
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
        DS = DALService.getInstance();
        // check if register user
        SiteVisitor User = onlineList.get(visitorId);
        if(! (User instanceof RegisteredUser)){
            logger.severe("Invalid visitor Id: " + visitorId);
            throw  new Exception("invalid visitor Id");
        }
        //open new store ()
        Store store = new Store(storeName);
        store.addNewListener((RegisteredUser)User);
        store.addNewOwnerListener((RegisteredUser)User);
        store.documentOwner(((RegisteredUser) User).getUserName());
        DS.saveStore(store.getId(),store.getName(),store.getActive(),store.getRate());
        // add to store list

        //new Employment
        Employment employment = new Employment(((RegisteredUser) User).getUserName(),store.getID(),Role.StoreFounder);
        DS.saveEmployment(employment.getEmployee(),employment.getStore(),employment.getAppointer(),employment.getRole().ordinal(),employment.getPermissionString());

        storesList.put(store.getID(),store);
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
        if (employment.CanCloseStore()) {
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

        throw  new Exception("Only store founder can close the store ");
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
        if (!employment.CanManageStock()) {
            logger.warning("user are not allowed to add products");
            throw  new Exception("you are not allowed to add products to this store");
        }
        if(store.getActive())
            return store.AddNewProduct(productName,price,quantity,category,description);
        else {
            logger.warning("Store is closed, store id :"+storeId);
            throw new Exception("Store is closed");
        }
        //catch
        //release lock user
        //throw e
    }

    /**
     * test function
     * @param visitorId -
     * @param storeId -
     * @return -
     * @throws Exception -
     */
    public Integer AddProduct(int visitorId,int storeId,StoreProduct storeProduct) throws Exception {
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
        if (!employment.checkIfFounder() && !employment.checkIfOwner() && !employment.checkIfManager()) {
            logger.warning("user are not allowed to add products");
            throw  new Exception("you are not allowed to add products to this store");
        }
        if(store.getActive())
            return store.AddNewProduct(storeProduct);
        else {
            logger.warning("Store is closed, store id :"+storeId);
            throw new Exception("Store is closed");
        }
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
        if (employment.CanManageStock()) {
            Store store = storesList.get(storeId);
            if(store==null)
                store=fetchStoreIfExists(storeId);
            if (store == null) {
                throw  new Exception("there is no store with this id ");
            }
            if(!store.getActive())
                throw  new Exception("store is closed");
            store.RemoveProduct(ProductId);
            //return new Response<>("the Product is successfully added", false);
            return;
        }
        logger.warning("Only the owner can close the store: " + visitorId);
        logger.fine("Exiting method RemoveProduct()");
        throw  new Exception("You do not have permission to remove product from this store. ");

        //catch
        //release lock user
        //release productlock if locked
        //throw e
    }

    public void AddStorePolicy(int visitorId, int storeId, Policy policy) throws Exception {
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
        if (!employment.CanChangePolicyAndDiscounts()) {
            logger.warning("user are not allowed to add products");
            throw  new Exception("you are not allowed to add policies to this store");
        }
        if(store.getActive())
            store.addPolicy(policy);
        else {
            logger.warning("Store is closed, store id :"+storeId);
            throw new Exception("Store is closed");
        }
        //catch
        //release lock user
        //throw e
    }
    public Policy AddStorePolicy(int visitorId, int storeId, ServicePolicy policy) throws Exception {
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
        if (!employment.CanChangePolicyAndDiscounts()) {
            logger.warning("user are not allowed to add products");
            throw  new Exception("you are not allowed to add policies to this store");
        }
        if(store.getActive())
            return store.addPolicy(policy);
        else {
            logger.warning("Store is closed, store id :"+storeId);
            throw new Exception("Store is closed");
        }
        //catch
        //release lock user
        //throw e
    }
    public Policy removeStorePolicy(int visitorId, int storeId,int policyId) throws Exception {
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
        if (!employment.CanChangePolicyAndDiscounts()) {
            logger.warning("user are not allowed to add products");
            throw  new Exception("you are not allowed to add policies to this store");
        }
        if(store.getActive())
            return store.removePolicy(policyId);
        else {
            logger.warning("Store is closed, store id :"+storeId);
            throw new Exception("Store is closed");
        }
        //catch
        //release lock user
        //throw e
    }
    public void UpdateProductQuantity(int visitorId,int storeId, int productID,int quantity) throws Exception{
        //try
        logger.fine("Entering method UpdateProductQuantity() with visitorId: " + visitorId + ", productID: " + productID + ", quantity: " + quantity);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productID);
        Store store = storesList.get(storeId);
        if(store==null)
            store=fetchStoreIfExists(storeId);
        if(store == null)
            throw  new Exception("there is no store with this storeID:"+storeId);
        if(!store.getActive())
            throw  new Exception("store is closed");
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
        if(store==null)
            store=fetchStoreIfExists(storeId);
        if(store==null)
            throw  new Exception("there is no store with this storeID:"+storeId);
        if(store.getActive()) {
            store.IncreaseProductQuantity(productId, quantity);
            logger.fine("Exiting method IncreaseProductQuantity()");
        }
        else {
            logger.warning("Failed on method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", quantity: " + quantity);
            throw new Exception("Store is closed");
        }
    }

    public void UpdateProductName(int visitorId, int productId,int storeId,String Name) throws Exception{
        logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", name: " + Name);
        if(Name== null){
            throw new NullPointerException("Null name while updating product name");
        }
        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        if(store==null)
            store=fetchStoreIfExists(storeId);
        if(store == null)
            throw  new Exception("there is no store with this storeID:"+storeId);
        if(!store.getActive())
            throw  new Exception("store is closed");
        store.UpdateProductName(productId,Name);
        logger.fine("Exiting method UpdateProductName()");
    }

    public void UpdateProductPrice(int visitorId, int productId,int storeId,double price) throws Exception{
        //lock product (get product object)
        //try
        logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", price: " + price);

        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        if(store==null)
            store=fetchStoreIfExists(storeId);
        if(store == null)
            throw  new Exception("there is no store with this storeID:"+storeId);
        if(!store.getActive())
            throw  new Exception("store is closed");
        store.UpdateProductPrice(productId,price);
        logger.fine("Exiting method UpdateProductPrice()");

        //catch
        //release lock product
        //throw e

    }
    private Store fetchStoreIfExists(int storeId){
        try {
            StoreDTO storeDTO = DALService.getInstance().getStoreById(storeId);
            if (storeDTO != null) {
                Store store = new Store(storeDTO);
                storesList.put(storeId, store);
                return store;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            throw new RuntimeException("Database error");
        }
    }
    public void UpdateProductCategory(int visitorId, int productId,int storeId,String category) throws Exception{
        logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", category: " + category);
        if(category==null){
            throw new NullPointerException("Null category while updating product category");
        }
        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);

        Store store = storesList.get(storeId);
        if(store==null){
           store=fetchStoreIfExists(storeId);
        }
        if(store == null)
            throw  new Exception("there is no store with this storeID:"+storeId);
        if(!store.getActive())
            throw  new Exception("store is closed");
        store.UpdateProductCategory(productId,category);
        logger.fine("Exiting method UpdateProductCategory()");


    }

    public void UpdateProductDescription(int visitorId, int productId,int storeId,String description) throws Exception{
        logger.fine("Entering method IncreaseProductQuantity() with visitorId: " + visitorId + ", productID: " + productId + ", description: " + description);
        if(description==null){
            throw new NullPointerException("null description in UpdateProductDescription");
        }
        checkifUserCanUpdateStoreProduct(visitorId,storeId,productId);
        Store store = storesList.get(storeId);
        if(store==null){
            store=fetchStoreIfExists(storeId);
        }
        if(store != null) {
            if(!store.getActive())
                throw  new Exception("store is closed");
            store.UpdateProductDescription(productId, description);
            logger.fine("Exiting method UpdateProductDescription()");
        }
        else {
            logger.warning("Failed at UpdateProductDescription because there was no store with the requested store Id");
            throw new IllegalArgumentException("There is no store with this store id :"+storeId);
        }

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
        if (employment.CanManageStock()) {
            Store store = storesList.get(storeId);
            if (store == null) {
                throw new Exception("there is no store with this id ");
            }
            logger.info("Exiting method checkifUserCanUpdateStoreProduct() with success");
            //return new Response<>("the Product is successfully added", false);
            return;
        }
        logger.info("Exiting method checkifUserCanUpdateStoreProduct() with failure");
        throw new Exception("This user isn't allowed to update this product");
    }


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
        Employment employment = null;
        if(! (User instanceof Admin)){
            try{
                employment = employmentList.get(((RegisteredUser) User).getUserName()).get(StoreId);
            }catch (Exception e){
                logger.warning("there is no store to this user");
                throw  new Exception("this user dont have any store");
            }
            if(!employment.CanSeePurchaseHistory()) {
                logger.log(Level.SEVERE, "An error occurred while getting store purchase history for visitorId: " + visitorId);
                throw new Exception("this user cant view the store purchase history");
            }
        }
        Store store = storesList.get(StoreId);
        if (store == null) {
            logger.log(Level.SEVERE, "An error occurred while getting store purchase history. Store with id: " + StoreId + " not found.");
            throw new Exception("there is no store with this id ");
        }
        ArrayList<String> output=new ArrayList<>();
        LinkedList<Bag> history = store.GetStorePurchaseHistory();
        for(Bag bag:history)
            output.add(bag.bagToString());
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
     * using filters, filters stuff
     * @param storeFilters filters for store in which products are to be filtered
     * @param productFilters filters who the returned products have to pass
     * @return product list of all products who passed the filter in the store who passed the filters
     */
    public Map<Store,List<StoreProduct>> FilterProductSearch(List<StoreFilter> storeFilters,List<ProductFilter> productFilters) {
       // logger.info("Entering method FilterProductSearch with productFilters: " + productFilters.toString());
        HashMap<Store,List<StoreProduct>> storeProducts=new HashMap<>();
        try {
            if(DALService.getInstance().storeCounter()>storesList.keySet().size()) {
                DS = DALService.getInstance();
                for (StoreDTO storeDTO : DS.getStores())
                    if (!storesList.containsKey(storeDTO.getId()))
                        storesList.put(storeDTO.getId(), new Store(storeDTO));
            }
            for (Store store : storesList.values()) { //for each store
                boolean passStoreFilter = true;
                if (!storeFilters.isEmpty()) {
                    for (StoreFilter storeFilter : storeFilters) {
                        if (!storeFilter.PassFilter(store)) {
                            passStoreFilter = false;
                            break;
                        }
                    }
                }
                if (passStoreFilter) {
                    ArrayList<StoreProduct> products = new ArrayList<>(store.filterProducts(productFilters));
                    if (!products.isEmpty())
                        storeProducts.put(store, products);
                }
            }
            logger.info("Filtered products done, stores found: " + storeProducts.keySet().size());
            return storeProducts;
        }
        catch (SQLException e){
            throw new RuntimeException("Database error");
        }
    }

    public void deleteUser(int visitorId, String userName) throws Exception {
        logger.info("Starting user deletion");
        if(!(onlineList.get(visitorId) instanceof Admin)){
            logger.info("User deletion failed: "+visitorId+" not admin");
            throw new Exception("Only admins can delete users");
        }
        RegisteredUser registeredUser=registeredUserList.get(userName);
        if(registeredUser==null) {
            logger.info("User deletion failed: username "+userName+" does not exist");
            throw new Exception("username " + userName + " does not exists");
        }
        if(!(employmentList.get(userName) == null || employmentList.get(userName).size() == 0)){
            logger.info("User deletion failed: username "+userName+" has store");
            throw new Exception("username " + userName + " has store");
        }
        registeredUserList.remove(userName);
    }
//    public void addPolicy(Policy policy,int storeId){
//        storesList.get(storeId).addPolicy(policy);
//    }
    public LinkedList<Store> getStoresName() throws Exception {
        LinkedList<Store> storesName = new LinkedList<>();
        Collection<StoreDTO> storeDTOS=DALService.getInstance().getStores();
        for(StoreDTO storeDTO:storeDTOS){
            if(!storesList.containsKey(storeDTO.getId()))
                storesList.put(storeDTO.getId(),new Store(storeDTO));
        }
        for(Store store : storesList.values()){
            if(store.getActive()){
                storesName.add(store);
            }
        }
        if(storesName.size()==0){
            throw new Exception("there is no stores");
        }
        return storesName;
    }

    public LinkedList<Integer> getStoreProduct(int StoreId) throws Exception {
        Store store = storesList.get(StoreId);
        if(store== null || !store.getActive()){
            throw new Exception("wrong id");
        }
       return  store.getProductsID();
    }

    public HashMap<String, Double> getStoreRatingList(int storeId) throws Exception {
        Store store = storesList.get(storeId);
        if(store== null || !store.getActive()){
            throw new Exception("wrong id");
        }
        return store.getRatingList();
    }

    public HashMap<String, String> getProductRatingList(int storeId ,int productId) throws Exception {
        Store store = storesList.get(storeId);
        if(store== null || !store.getActive()){
            throw new Exception("wrong id");
        }
        return store.getProductRatingList(productId);
    }

    public void addDiscount(Discount discount,int storeId){
        storesList.get(storeId).addDiscount(discount);
    }
    public Discount addDiscount(ServiceDiscount discount, int storeId){
       return storesList.get(storeId).addDiscount(discount);
    }
//    public Discount addDiscount(ServiceBasicDiscount serviceDiscount, int storeId) {
//        return storesList.get(storeId).addDiscount(serviceDiscount);
//    }
    public Discount removeDiscount(int discountId, int storeId) {
        return storesList.get(storeId).removeDiscount(discountId);
    }

    public Collection<Discount> getStoreDiscounts(int storeId){
        return storesList.get(storeId).getDiscounts();
    }
    public List<Store> getStoresByUserName(int visitorId) throws Exception {
        DS=DALService.getInstance();
        SiteVisitor visitor = onlineList.get(visitorId);
        if(! (visitor instanceof RegisteredUser user)){
            throw new Exception("invalid visitor Id");
        }
        String userName=user.getUserName();
        Collection<EmploymentDTO> employmentDTOS=DS.getEmploymentsByName(userName);
        for(EmploymentDTO employmentDTO:employmentDTOS){
            if(!employmentList.containsKey(userName)){
                employmentList.put(userName,new HashMap<>());
            }
            employmentList.get(userName).put(employmentDTO.getStoreID(),new Employment(employmentDTO));
        }
        Map<Integer,Employment> employmentMap = employmentList.get(userName);
        for(StoreDTO storeDTO:DS.getStoresFromOwner(userName)) {
            if (!employmentMap.containsKey(storeDTO.getId()))
                employmentMap.put(storeDTO.getId(), new Employment(userName, storeDTO.getId(), Role.StoreOwner));
            if(!storesList.containsKey(storeDTO.getId())){
                storesList.put(storeDTO.getId(),new Store(storeDTO));
            }
        }

        LinkedList<Store> stores = new LinkedList<>();
        for(Integer i :employmentMap.keySet()){
            stores.add(storesList.get(i));
        }
        return stores;
    }
    public double getBagDiscountSavings(int visitorId,int storeId) throws Exception {
        return getUserBag(visitorId,storeId).calcDiscountSavings();
    }
    public boolean bagPassesPolicies(int visitorId,int storeId) throws Exception {
        return getUserBag(visitorId,storeId).passesPolicy();
    }
    private Bag getUserBag(int visitorId,int storeId) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(! (visitor instanceof RegisteredUser user)){
            throw new Exception("invalid visitor Id");
        }
        Bag bag=user.getCart().getBags().get(storeId);
        if(bag==null)
            throw new Exception("no bag exists in the user's cart for the specified store");
        return bag;
    }

    public Collection<Policy> getStorePolicies(int visitorId,int storeId) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if (!(visitor instanceof RegisteredUser user)) {
            throw new Exception("invalid visitor Id");
        }
        return storesList.get(storeId).getPolicies();
    }
    public HashMap<CartProduct,Double> getSavingsPerProduct(int visitorId,int storeId) throws Exception{
        return getUserBag(visitorId,storeId).getSavingsPerProducts();

    }
    public HashMap<CartProduct,Double> getCartDiscountInfo(int visitorId,int storeId) throws Exception{
        return getUserBag(visitorId,storeId).getSavingsPerProducts();
    }

    /**
     * A function to get the quantity of a product
     * @param storeId - the store that the product belongs to
     * @param productId - the id of the product
     * @return an integer that is the quantity of the product that the store has right now
     */
    public Integer getStoreProductQuantity(int storeId, int productId)
    {
        logger.info("Starting getStoreProductQuantity");
        if(!storesList.containsKey(storeId))
            throw new IllegalArgumentException("There is no store with this id");
        Store s =  storesList.get(storeId);
        if(s.getProductByID(productId) == null)
            throw new NullPointerException("There is no such product with this id:"+productId+" in the store with the id:"+storeId);
        Integer quantity = s.getProductByID(productId).getQuantity();
        return quantity;
    }

    public List<SiteVisitor> getOnlineUsers(){
        logger.info("Starting getOnlineUsers");
        LinkedList <SiteVisitor> onlineusers = new LinkedList<>();
        for (SiteVisitor sv : onlineList.values())
        {
            if(sv.getVisitorId()!=0) {
                if (sv instanceof RegisteredUser) {
                    onlineusers.add((RegisteredUser) sv);
                }
                else {
                    onlineusers.add(sv);
                }
            }
        }
        return onlineusers;
    }

    public List<RegisteredUser> getOfflineUsers(){
        logger.info("Starting getOfflineUsers");
        LinkedList<RegisteredUser> offlineUsersList = new LinkedList<>();
        for (RegisteredUser registeredUser:registeredUserList.values()) {
            if(!onlineList.containsValue(registeredUser)){
                offlineUsersList.add(registeredUser);
            }
        }
        return offlineUsersList;
    }

    public boolean checkForNewMessages(int visitorId) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        RegisteredUser user=(RegisteredUser) visitor;
        try{
             return registeredUserList.get(user.getUserName()).hasNewMessage();
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    public LinkedList<String> getNewMessages(int visitorId) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        RegisteredUser user=(RegisteredUser) visitor;
        try{
            registeredUserList.get(user.getUserName()).setHasNewMessage(false);
            return registeredUserList.get(user.getUserName()).getWaitingMessages();
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * A function to get the total amount of the current user's cart
     * @param visitorId - the id of the current user (session number)
     * @return the value of the products he has in his cart
     */
    public Double getTotalPrice(int visitorId) throws Exception {
        SiteVisitor user = onlineList.get(visitorId);
        if (user == null) {
            logger.warning("this User by  ID:"+ visitorId + "is null");
            throw new IllegalArgumentException("Invalid Visitor ID");
        }
        Double totalPrice = user.getCart().getTotalPrice();
        Cart userCart = user.getCart();
        if(user instanceof RegisteredUser && userCart.getBags().isEmpty())
            fetchCartIfExists((RegisteredUser) user);
        for (Bag currbag: userCart.getBags().values()) {
            Store s = storesList.get(currbag.getStoreID());
            if(s  == null)
                throw new NullPointerException("getTotalPriceError:Couldnt find a store with this store id:"+currbag.getStoreID());
            totalPrice -= s.calcSaved(currbag);
        }
        return totalPrice;
    }

    public Integer getDailyIncome(int day,int month,int year, int visitorId) throws Exception {

        //Check if VisitorID is admin
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }
        if(!(visitor instanceof Admin)){
            throw new Exception("Current user is not admin");
        }
        int totalAmount = 0;
        //Get all incomes
        for (Store s:storesList.values())
        {
            totalAmount += s.getDailyIncome(day, month, year);
        }
        return totalAmount;
    }

    public Integer getDailyIncomeByStore(int day,int month,int year,int storeId, int visitorId) throws Exception {

        //Check if VisitorID is admin
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }

        Store s = storesList.get(storeId);
        if(s == null)
        {
            throw new Exception("No store found with this store ID");
        }

        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }

        RegisteredUser user = (RegisteredUser)visitor;
        if(employmentList.get(user.getUserName()) == null || employmentList.get(user.getUserName()).get(storeId) == null){
            if(!(user instanceof Admin)){
                throw new Exception("This user has no store with this store ID");
            }
        }

        return s.getDailyIncome(day,month,year);
    }

    public void acceptEmploymentRequest(int visitorID,int storeID,String appointedUserName) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorID);
        if(visitor == null){
            throw new Exception("Invalid visitorID");
        }
        if(!(visitor instanceof RegisteredUser)){
            throw new Exception("This user is not registered");
        }
        RegisteredUser appointer = (RegisteredUser)visitor;

        Employment em;
        try {
            em = employmentList.get(appointer.getUserName()).get(storeID);
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
        if(em == null || !em.checkIfOwner()){
            throw new Exception("This user is not owner of this store");
        }
        //Add current user to list of accepted store owners
        RegisteredUser appointed = registeredUserList.get(appointedUserName);
        if(appointed == null){
            throw new Exception("Cannot find any user with the name "+appointedUserName);
        }

        LinkedList<RegisteredUser> list1 = appointmentsRequests.get(storeID).get(appointed);
        list1.add(appointer);
        //If all store owners accepted, create new employment
        Store store = storesList.get(storeID);
        if(checkIfAllOwnersAgreedOnEmploymentRequest(storeID,appointedUserName)){
            Employment appointedEmployment = new Employment(appointer.getUserName(), appointed.getUserName(), store.getID(), Role.StoreOwner);
            DS.saveEmployment(appointedEmployment.getEmployee(), appointedEmployment.getStore(), appointedEmployment.getAppointer(), appointedEmployment.getRole().ordinal(), appointedEmployment.getPermissionString());
            if (employmentList.get(appointedUserName) == null) {
                Map<Integer, Employment> newEmploymentMap = new HashMap<>();
                employmentList.put(appointedUserName, newEmploymentMap);
            }
            employmentList.get(appointedUserName).put(storeID, appointedEmployment);
            store.addNewListener(appointed);
            store.addNewOwnerListener(appointed);
            store.documentOwner(appointed.getUserName());
            appointmentsRequests.get(storeID).remove(appointed);
            store.notifyOwnersAboutNewAppointmentSucceed(appointedUserName);
            logger.fine("new store owner with name" + appointedUserName +" added successfully");
            registeredUserList.get(appointedUserName).update("You are Owner of the store '"+storesList.get(storeID).getName()+"'");
        }
    }

    private boolean checkIfAllOwnersAgreedOnEmploymentRequest(int storeID,String userName){
        LinkedList<RegisteredUser> storeOwners = new LinkedList<>();
        for (Map.Entry<String, Map<Integer, Employment>> e : employmentList.entrySet()){
            if(e.getValue() != null && e.getValue().get(storeID) != null && e.getValue().get(storeID).checkIfOwner()){
                storeOwners.add(registeredUserList.get(e.getKey()));
            }
        }

        return areListsIdentical(storeOwners,appointmentsRequests.get(storeID).get(registeredUserList.get(userName)));


    }

    private <T> boolean areListsIdentical(LinkedList<T> list1, LinkedList<T> list2) {
        if (list1 == null || list2 == null) {
            return false;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        // Create a copy of list2
        LinkedList<T> tempList = new LinkedList<>(list2);

        // Iterate over list1 and remove elements from tempList
        for (T element : list1) {
            if (!tempList.remove(element)) {
                return false; // If an element is not present in tempList, lists are not identical
            }
        }

        return tempList.isEmpty(); // If tempList is empty, lists are identical
    }

    public void declineEmploymentRequest(int visitorID,int storeID,String appointedUserName) throws Exception {

        SiteVisitor visitor = onlineList.get(visitorID);
        if(visitor == null){
            throw new Exception("Invalid visitorID");
        }
        if(!(visitor instanceof RegisteredUser)){
            throw new Exception("This user is not registered");
        }
        RegisteredUser user = (RegisteredUser)visitor;

        Employment em;
        try {
            em = employmentList.get(user.getUserName()).get(storeID);
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
        if(em == null || !em.checkIfOwner()){
            throw new Exception("This user is not owner of this store");
        }
        try{
            appointmentsRequests.get(storeID).remove(registeredUserList.get(appointedUserName));
        }
        catch (Exception e){
            throw new Exception("Something went wrong upon trying to decline employment request.");
        }

    }


    public Map<String,Integer> getVisitorsAmountBetweenDates(int visitorId, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {
        Map<String,Integer> outputMap = new HashMap<>();
        for (Date d:siteVisitorsEntriesManager.keySet()) {
            if(isBetweenDates(d,dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd)){
                outputMap.put(dateToString(d),siteVisitorsEntriesManager.get(d));
            }
        }
        return outputMap;
    }

    public Map<String,Integer> getUsersWithoutStoresAmountBetweenDates(int visitorId, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {
        Map<String,Integer> outputMap = new HashMap<>();
        for (Date d:nonWorkersEntriesManager.keySet()) {
            if(isBetweenDates(d,dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd)){
                outputMap.put(dateToString(d),nonWorkersEntriesManager.get(d));
            }
        }
        return outputMap;
    }

    public Map<String,Integer> getStoreManagersOnlyAmountBetweenDates(int visitorId, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {
        Map<String,Integer> outputMap = new HashMap<>();
        for (Date d:managersOnlyEntriesManager.keySet()) {
            if(isBetweenDates(d,dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd)){
                outputMap.put(dateToString(d),managersOnlyEntriesManager.get(d));
            }
        }
        return outputMap;
    }

    public Map<String,Integer> getStoreOwnersAmountBetweenDates(int visitorId, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {
        Map<String,Integer> outputMap = new HashMap<>();
        for (Date d:storeOwnersEntriesManager.keySet()) {
            if(isBetweenDates(d,dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd)){
                outputMap.put(dateToString(d),storeOwnersEntriesManager.get(d));
            }
        }
        return outputMap;
    }

    public Map<String,Integer> getAdminsAmountBetweenDates(int visitorId, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {
        Map<String,Integer> outputMap = new HashMap<>();
        for (Date d:adminsEntriesManager.keySet()) {
            if(isBetweenDates(d,dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd)){
                outputMap.put(dateToString(d),adminsEntriesManager.get(d));
            }
        }
        return outputMap;
    }

    private boolean isBetweenDates(Date d, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd) {

        Date startDate =new Date(yearStart-1900,monthStart-1,dayStart);
        Date endDate = new Date(yearEnd-1900,monthEnd-1,dayEnd);

        return (d.after(startDate) || (areDatesEquals(d,startDate)) ) && (d.before(endDate) || (areDatesEquals(d,endDate)));

    }

    private boolean areDatesEquals(Date d1,Date d2){
        return d1.getDate()==d2.getDate() && d1.getMonth()==d2.getMonth() && d1.getYear()==d2.getYear();
    }

    private void increaseEntry(Map<Date,Integer> entryMap){
        Date today = new Date();
        boolean foundToday = false;
        for (Date d : entryMap.keySet()) {
            if(areDatesEquals(d,today)){
                entryMap.put(d,entryMap.get(d) + 1);
                foundToday = true;
            }
        }
        if(!foundToday){
            entryMap.put(today,1);
        }
    }

    private String dateToString(Date d){
        return d.getDate()+"/"+(d.getMonth()+1)+"/"+(d.getYear()+1900);
    }
    public LinkedList<Permission> getPermissions(int visitorId, int storeId) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Invalid visitorID");
        }
        if(!(visitor instanceof RegisteredUser)){
            throw new Exception("This user is not registered");
        }
        RegisteredUser user = (RegisteredUser)visitor;
        Store s = storesList.get(storeId);
        if(s == null){
            throw new Exception("Invalid storeID");
        }
        Employment em;
        try {
            em = employmentList.get(user.getUserName()).get(storeId);
        }
        catch (Exception e){
            throw new Exception(e);
        }
        if(em == null){
            throw new Exception("This user has no employments in this store");
        }
        return new LinkedList<>(em.getPermisssions());
    }

    public Map<Integer,List<String>> getAppointmentRequests(int visitorId) throws Exception {

        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Invalid visitorID");
        }
        if(!(visitor instanceof RegisteredUser)){
            throw new Exception("Current user is not registered");
        }
        RegisteredUser user = (RegisteredUser) visitor;
        String userName = user.getUserName();
        Map<Integer,List<String>> outputMap = new HashMap<>();
        for (Integer storeId :appointmentsRequests.keySet()) { // For each store that has requests
            if(employmentList.get(userName) != null && employmentList.get(userName).get(storeId) != null && employmentList.get(userName).get(storeId).checkIfOwner()){ // Check if the username is owner of that store
                for (RegisteredUser appointed : appointmentsRequests.get(storeId).keySet()){ // If so, for each user that is waiting to be accepted to that store
                    if(appointmentsRequests.get(storeId) != null && appointmentsRequests.get(storeId).get(appointed) != null && !appointmentsRequests.get(storeId).get(appointed).contains(user)){ // Check if we already accepted or not

                        if(!outputMap.containsKey(storeId)){
                            outputMap.put(storeId,new LinkedList<String>());
                        }
                        outputMap.get(storeId).add(appointed.getUserName());

                    }
                }
            }
        }

        return outputMap;

    }
    public Map<StoreProduct,Bid> getUserBids(int visitorId) throws Exception{

        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Invalid visitorID");
        }
        if(!(visitor instanceof RegisteredUser)){
            throw new Exception("This user is not registered");
        }
        return ((RegisteredUser)visitor).getCounterOffers();
    }
    public Collection<Bid> getStoreBids(int storeId) throws Exception{

        Store store = storesList.get(storeId);
        if(store == null)
        {
            throw new Exception("No store found with this store ID");
        }
        return store.getPendingBids();
    }
    public Bid addBid(int visitorId, int productId, int storeId, int amount, int newPrice) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }

        Store store = storesList.get(storeId);
        if(store == null)
        {
            throw new Exception("No store found with this store ID");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        RegisteredUser user = (RegisteredUser)visitor;
        return store.addBid(productId,amount,newPrice,user.getUserName(), user.getVisitorId());
    }
    public Bid voteOnBid(int visitorId, int productId,String userName, int storeId, boolean vote) throws Exception {
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }

        Store store = storesList.get(storeId);
        if(store == null)
        {
            throw new Exception("No store found with this store ID");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        RegisteredUser user = (RegisteredUser)visitor;
        return store.voteOnBid(user.getUserName(),productId,registeredUserList.get(userName),vote);

    }
    public Bid counterOfferBid(int visitorId, int productId, int storeId, String userName,double newPrice,String message) throws Exception{
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }

        Store store = storesList.get(storeId);
        if(store == null)
        {
            throw new Exception("No store found with this store ID");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        RegisteredUser user=registeredUserList.get(userName);
        Bid bid=store.rejectBid(productId,user,message);
        bid.setNewPrice(newPrice);
        user.addCounterOffer(bid,store.getProducts().get(productId));
        return bid;
    }
    public void acceptCounterOffer(int visitorId, int productId, int storeId) throws Exception{
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }

        Store store = storesList.get(storeId);
        if(store == null)
        {
            throw new Exception("No store found with this store ID");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        ((RegisteredUser)visitor).acceptCounterOff(productId,store);
    }
    public void rejectCounterOffer(int visitorId, int productId,int storeId) throws Exception{
        SiteVisitor visitor = onlineList.get(visitorId);
        if(visitor == null){
            throw new Exception("Wrong visitorId");
        }
        if(!(visitor instanceof RegisteredUser))
        {
            throw new Exception("Current user is not registered to system");
        }
        Store store = storesList.get(storeId);
        if(store == null){
            throw new Exception("Wrong storeId");
        }
        ((RegisteredUser)visitor).rejectCounterOffer(productId,store);
    }

    public StoreProduct getProduct(int storeId, int productId) {
        return storesList.get(storeId).getProducts().get(productId);
    }



}

