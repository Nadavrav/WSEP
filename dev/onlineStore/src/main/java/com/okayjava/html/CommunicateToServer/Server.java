package com.okayjava.html.CommunicateToServer;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Users.Permission;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceAppliedDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;

import java.util.Collection;
import java.util.List;

public class Server {
    private boolean logged = false;
    private String username = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLogged(){
        return logged;
    }

    public void setLogged(boolean status){
        logged = status;
    }
    private static Server server = null;
    private Service service;

    public static Server getInstance(){
        if (server == null){
            server = new Server();
        }
        return server;
    }

    private Server(){
        service = new Service();
    }

    public Response<Integer> EnterNewSiteVisitor() {
        return service.EnterNewSiteVisitor();
    }

    public Response<?> login(String username, String password) {
        this.username = username;
        return service.login(username, password);
    }


    public Response<?> Register(String username, String password) {
        return service.Register(username, password);
    }

    public Response<?> logout() {
        logged = false;
        return service.logout();
    }

    public Response<Integer> OpenStore(String storeName) {
        return service.OpenStore(storeName);
    }

    public Response<?> loadData() throws Exception {
        return service.loadData();
    }

    public Response<ServiceCart> getProductsInMyCart() { //it should return list of strings
        return service.getProductsInMyCart();
    }

    public Response<?> removeProductFromCart(int productId, int storeId){
        return service.removeProductFromCart(productId, storeId);
    }

    public Response<?> changeCartProductQuantity(int productId, int storeId, int newAmount){
        return service.changeCartProductQuantity(productId, storeId, newAmount);
    }

    public Response<Integer> AddProduct(int storeID, String productName, double price, String category, int quantity, String description) {
        return service.AddProduct(storeID, productName, price, category, quantity,description);
    }


    public Response<?> RemoveProduct(int productID, int storeID) {
        return service.RemoveProduct(productID,storeID);
    }


    public Response<?> UpdateProductName(int productID, int storeID, String productName) {
        return service.UpdateProductName(productID, storeID, productName);
    }

    public Response<?> UpdateProductPrice(int productID, int storeID, double price) {
        return service.UpdateProductPrice(productID, storeID, price);
    }

    public Response<?> UpdateProductQuantity(int productID, int storeID, int quantity) {
        return service.UpdateProductQuantity(productID, storeID, quantity);
    }

    public Response<?> UpdateProductCategory(int productID, int storeID, String category) {
        return service.UpdateProductCategory(productID, storeID, category);
    }

    public Response<?> UpdateProductDescription(int productID, int storeID, String description) {
        return service.UpdateProductDescription(productID, storeID, description);
    }


    public Response<List<String>> GetStoreHistoryPurchase(int storeID) {
        return service.GetStoreHistoryPurchase(storeID);
    }

    public Response<?> CloseStore(int storeID) {
        return service.CloseStore(storeID);
    }

    public Response<?> appointNewStoreOwner(String ownerName, int storeID) {
        return service.appointNewStoreOwner(ownerName, storeID);
    }

    public Response<?> appointNewStoreManager(String managerName, int storeID) {
        return service.appointNewStoreManager(managerName, storeID);
    }

    public Response<?> changeStoreManagerPermission(String managerName, int storeID, List<Permission> permissions) {
        return service.changeStoreManagerPermission(managerName, storeID, permissions);
    }

    public Response<List<ServiceStore>> FilterProductSearch(List<ProductFilter> productFilter, List<StoreFilter> storeFilter) {
        return service.FilterProductSearch(productFilter, storeFilter);
    }

    public Response<?> addProductToCart(int productId, int storeId, int quantity) { //update - quantity
        return service.addProductToCart(productId, storeId, quantity);
    }

    public Response<?> getStoresName() {
        return service.getStoresName();
    }

    public Response<?> addStoreRateAndComment(int storeID, int rating, String comment) {
        return service.addStoreRateAndComment(storeID, rating, comment);
    }

    public Response<?> addProductRateAndComment(int productId, int storeId, int rate, String comment){
        return service.addProductRateAndComment(productId, storeId, rate, comment);
    }

    public Response<?> GetInformation(int StoreId){
        return service.GetInformation(StoreId);
    }

    public Response<Boolean> isAdmin(){
        return service.isAdmin();
    }

    public Response<?> getRolesData(int storeId){
        return service.getRolesData(storeId);
    }

    public Response<?> getProductRatingList(int storeID, int productID) {
        return service.getProductRatingList(storeID, productID);
    }

    public Response<?> GetStoreRate(int storeID){
        return service.GetStoreRate(storeID);
    }

    public Response<?> deleteUser(String userName){
        return service.deleteUser(userName);
    }

    public Response<List<ServiceUser>> getRegisteredUsersInfo(){
        return service.getRegisteredUsersInfo();
    }

    public Response<?> removeEmployee(int storeId,String userName){
        return service.removeEmployee(storeId, userName);
    }

    public Response<ServiceAppliedDiscount> getBagDiscountInfo(int storeId){
        return service.getBagDiscountInfo(storeId);
    }

    public Response<List<ServiceUser>> getOnlineUsers(){
        return service.getOnlineUsers();
    }

    public Response<List<ServiceUser>> getOfflineUsers(){
        return service.getOfflineUsers();
    }

    public Response<Collection<ServiceDiscount>> getStoreDiscountInfo(int storeId){
        return service.getStoreDiscountInfo(storeId);
    }

    public Response<Collection<ServiceStore>> getStoresByUserName(String userName){
        return service.getStoresByUserName(userName);
    }

    public Response<List<String>> PurchaseCart(int visitorCard, String address){
        return service.PurchaseCart(visitorCard, address);
    }
}

