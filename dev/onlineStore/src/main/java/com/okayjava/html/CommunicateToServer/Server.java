package com.okayjava.html.CommunicateToServer;

import DomainLayer.Response;
import DomainLayer.Users.Permission;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceBid;
import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceAppliedDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscountInfo;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicyInfo;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class Server {
    private static Server server = null;
    private final HashMap<String,Service> activeSessions;

    public static Server getInstance(){
        if (server == null){
            server = new Server();
        }
        return server;
    }

    private Server(){
      //  service = new Service();
        activeSessions=new HashMap<>();

    }
    public Service getSession(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        if(!activeSessions.containsKey(sessionId))
            activeSessions.put(sessionId,new Service());
        return activeSessions.get(sessionId);
    }
    public boolean isLogged(HttpServletRequest request){
        Response<Boolean> r= getSession(request).isLoggedIn();
        if(r.isError())
            return false;
        return r.getValue();
    }

    public String getUsername(HttpServletRequest request){
        Response<String> r= getSession(request).getUserName();
        if(r.isError())
            return "GUEST";
        return r.getValue();
    }
    public Response<Integer> EnterNewSiteVisitor(HttpServletRequest request) {
        return getSession(request).EnterNewSiteVisitor();
    }

    public Response<?> login(HttpServletRequest request,String username, String password) {
        return getSession(request).login(username, password);
    }


    public Response<?> Register(HttpServletRequest request,String username, String password) {
        return getSession(request).Register(username, password);
    }

    public Response<?> logout(HttpServletRequest request) {
        return getSession(request).logout();
    }

    public Response<Integer> OpenStore(HttpServletRequest request,String storeName) {
        return getSession(request).OpenStore(storeName);
    }

    public Response<?> loadData(HttpServletRequest request) throws Exception {
        return getSession(request).loadData();
    }

    public Response<ServiceCart> getProductsInMyCart(HttpServletRequest request) { //it should return list of strings
        return getSession(request).getProductsInMyCart();
    }

    public Response<?> removeProductFromCart(HttpServletRequest request,int productId, int storeId){
        return getSession(request).removeProductFromCart(productId, storeId);
    }

    public Response<?> changeCartProductQuantity(HttpServletRequest request,int productId, int storeId, int newAmount){
        return getSession(request).changeCartProductQuantity(productId, storeId, newAmount);
    }

    public Response<Integer> AddProduct(HttpServletRequest request,int storeID, String productName, double price, String category, int quantity, String description) {
        return getSession(request).AddProduct(storeID, productName, price, category, quantity,description);
    }


    public Response<?> RemoveProduct(HttpServletRequest request,int productID, int storeID) {
        return getSession(request).RemoveProduct(productID,storeID);
    }


    public Response<?> UpdateProductName(HttpServletRequest request,int productID, int storeID, String productName) {
        return getSession(request).UpdateProductName(productID, storeID, productName);
    }

    public Response<?> UpdateProductPrice(HttpServletRequest request,int productID, int storeID, double price) {
        return getSession(request).UpdateProductPrice(productID, storeID, price);
    }

    public Response<?> UpdateProductQuantity(HttpServletRequest request,int productID, int storeID, int quantity) {
        return getSession(request).UpdateProductQuantity(productID, storeID, quantity);
    }

    public Response<?> UpdateProductCategory(HttpServletRequest request,int productID, int storeID, String category) {
        return getSession(request).UpdateProductCategory(productID, storeID, category);
    }

    public Response<?> UpdateProductDescription(HttpServletRequest request,int productID, int storeID, String description) {
        return getSession(request).UpdateProductDescription(productID, storeID, description);
    }


    public Response<List<String>> GetStoreHistoryPurchase(HttpServletRequest request,int storeID) {
        return getSession(request).GetStoreHistoryPurchase(storeID);
    }

    public Response<?> CloseStore(HttpServletRequest request,int storeID) {
        return getSession(request).CloseStore(storeID);
    }

    public Response<?> appointNewStoreOwner(HttpServletRequest request,String ownerName, int storeID) {
        return getSession(request).appointNewStoreOwner(ownerName, storeID);
    }

    public Response<?> appointNewStoreManager(HttpServletRequest request,String managerName, int storeID) {
        return getSession(request).appointNewStoreManager(managerName, storeID);
    }

    public Response<?> changeStoreManagerPermission(HttpServletRequest request,String managerName, int storeID, List<Permission> permissions) {
        return getSession(request).changeStoreManagerPermission(managerName, storeID, permissions);
    }

    public Response<List<ServiceStore>> FilterProductSearch(HttpServletRequest request,List<ProductFilter> productFilter, List<StoreFilter> storeFilter) {
        return getSession(request).FilterProductSearch(productFilter, storeFilter);
    }

    public Response<?> addProductToCart(HttpServletRequest request,int productId, int storeId, int quantity) { //update - quantity
        return getSession(request).addProductToCart(productId, storeId, quantity);
    }

    public Response<?> getStoresName(HttpServletRequest request) {
        return getSession(request).getStoresName();
    }

    public Response<?> addStoreRateAndComment(HttpServletRequest request,int storeID, int rating, String comment) {
        return getSession(request).addStoreRateAndComment(storeID, rating, comment);
    }

    public Response<?> addProductRateAndComment(HttpServletRequest request,int productId, int storeId, int rate, String comment){
        return getSession(request).addProductRateAndComment(productId, storeId, rate, comment);
    }

    public Response<?> GetInformation(HttpServletRequest request,int StoreId){
        return getSession(request).GetInformation(StoreId);
    }

    public Response<Boolean> isAdmin(HttpServletRequest request){
        return getSession(request).isAdmin();
    }

    public Response<?> getRolesData(HttpServletRequest request,int storeId){
        return getSession(request).getRolesData(storeId);
    }

    public Response<?> getProductRatingList(HttpServletRequest request,int storeID, int productID) {
        return getSession(request).getProductRatingList(storeID, productID);
    }

    public Response<?> GetStoreRate(HttpServletRequest request,int storeID){
        return getSession(request).GetStoreRate(storeID);
    }

    public Response<?> deleteUser(HttpServletRequest request,String userName){
        return getSession(request).deleteUser(userName);
    }

    public Response<List<ServiceUser>> getRegisteredUsersInfo(HttpServletRequest request){
        return getSession(request).getRegisteredUsersInfo();
    }

    public Response<?> removeEmployee(HttpServletRequest request,int storeId,String userName){
        return getSession(request).removeEmployee(storeId, userName);
    }

    public Response<ServiceAppliedDiscount> getBagDiscountInfo(HttpServletRequest request,int storeId){
        return getSession(request).getBagDiscountInfo(storeId);
    }

    public Response<List<ServiceUser>> getOnlineUsers(HttpServletRequest request){
        return getSession(request).getOnlineUsers();
    }

    public Response<List<ServiceUser>> getOfflineUsers(HttpServletRequest request){
        return getSession(request).getOfflineUsers();
    }

    public Response<Collection<ServiceDiscountInfo>> getStoreDiscountInfo(HttpServletRequest request,int storeId){
        return getSession(request).getStoreDiscountInfo(storeId);
    }

    public Response<Collection<ServiceStore>> getMyStores(HttpServletRequest request){
        return getSession(request).getStoresByUserName();
    }

    public Response<List<String>> PurchaseCart(HttpServletRequest request,String holder,String visitorCard,String expireDate,int cvv,String id,String address, String city, String country, String zip){
        return getSession(request).PurchaseCart(holder,visitorCard, expireDate,cvv, id,address, city, country, zip);
    }

    public Response<LinkedList<String>> getNewMessages(HttpServletRequest request) throws Exception {
        return getSession(request).getNewMessages();
    }

    public Response<Boolean> checkForNewMessages(HttpServletRequest request) throws Exception {
        return getSession(request).checkForNewMessages();
    }

    public Response<Double> getTotalPrice(HttpServletRequest request){
        return getSession(request).getTotalPrice();
    }

    public Response<HashSet<ServicePolicy>> getStorePolicy(HttpServletRequest request, int storeId){
        return getSession(request).getStorePolicy(storeId);
    }

    public Response<ServiceDiscountInfo> addDiscount(HttpServletRequest request, ServiceDiscount serviceDiscount, int storeId){
        return getSession(request).addDiscount(serviceDiscount, storeId);
    }
    public Response<ServicePolicyInfo> addPolicy(HttpServletRequest request, ServicePolicy servicePolicy, int storeId){
        return getSession(request).addPolicy(servicePolicy, storeId);
    }
    public Response<LinkedList<Permission>> getPermissions(HttpServletRequest request, int storeId){
        return getSession(request).getPermissions(storeId);
    }

    public Response<?> declineAppointment(HttpServletRequest request, int storeId, String appointedUserName) {
        return getSession(request).declineAppointment(storeId, appointedUserName);
    }

    public Response<?> acceptAppointment(HttpServletRequest request, int storeId, String appointedUserName){
        return getSession(request).acceptAppointment(storeId, appointedUserName);
    }

    public Response<Integer> getDailyIncomeByStore(HttpServletRequest request, int day,int month,int year,int storeId){
        return getSession(request).getDailyIncomeByStore(day, month, year, storeId);
    }

    public Response<Integer> getDailyIncome(HttpServletRequest request, int day,int month,int year){
        return getSession(request).getDailyIncome(day, month, year);
    }

    public Response<Map<Integer, List<String>>> getAppointmentRequests(HttpServletRequest request){
        return getSession(request).getAppointmentRequests();
    }

    public Response<ServiceBid> addNewBid(HttpServletRequest request, int productId, int storeId, int amount, int newPrice){
        return getSession(request).addNewBid(productId, storeId, amount, newPrice);
    }

    public Response<ServiceBid> counterOfferBid(HttpServletRequest request, int productId, int storeId, String userName, double newPrice, String message){
        return getSession(request).counterOfferBid(productId, storeId, userName, newPrice, message);
    }

    public Response<?> acceptCounterOffer(HttpServletRequest request, int productId, int storeId){
        return getSession(request).acceptCounterOffer(productId, storeId);
    }

    public Response<?> rejectCounterOffer(HttpServletRequest request, int productId, int storeId){
        return getSession(request).rejectCounterOffer(productId, storeId);
    }

    public Response<?> voteOnBid(HttpServletRequest request, int productId, int storeId, String userName, boolean vote){
        return getSession(request).voteOnBid(productId, storeId, userName, vote);
    }

    public Response<Collection<ServiceBid>> geStoreBids(HttpServletRequest request, int storeId){
        return getSession(request).geStoreBids(storeId);
    }

    public Response<Collection<ServiceBid>> getUserBids(HttpServletRequest request){
        return getSession(request).getUserBids();
    }

    public Response<Map<String,Integer>> getVisitorsAmountBetweenDates(HttpServletRequest request, int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return getSession(request).getVisitorsAmountBetweenDates(dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
    }

    public Response<Map<String,Integer>> getUsersWithoutStoresAmountBetweenDates(HttpServletRequest request, int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd) {
        return getSession(request).getUsersWithoutStoresAmountBetweenDates(dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
    }

    public Response<Map<String,Integer>> getStoreManagersOnlyAmountBetweenDates(HttpServletRequest request, int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd) {
        return getSession(request).getStoreManagersOnlyAmountBetweenDates(dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
    }

    public Response<Map<String,Integer>> getStoreOwnersAmountBetweenDates(HttpServletRequest request, int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd) {
        return getSession(request).getStoreOwnersAmountBetweenDates(dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
    }

    public Response<Map<String,Integer>> getAdminsAmountBetweenDates(HttpServletRequest request, int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd) {
        return getSession(request).getAdminsAmountBetweenDates(dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
    }
}

