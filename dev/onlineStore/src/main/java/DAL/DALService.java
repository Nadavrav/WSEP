package DAL;

import DAL.DAOs.*;
import DAL.DTOs.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;


public class DALService {
    private static volatile DALService instance;
    private final registeredUserDAO userDAO;
    private final storeDAO storeDAO;
    private final StoreProductDAO storeProductDAO;
    private final adminDAO adminDAO;
    private final employmentDAO employmentDAO;
    private final CartProductDAO cartProductDAO;
    private SessionFactory sessionFactory;
    private DALService() throws SQLException {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }catch (Exception  e) {
            throw new SQLException("Cant connect to DB");
        }
        cartProductDAO=new CartProductDAO(sessionFactory);
        storeProductDAO = new StoreProductDAO(sessionFactory);
        userDAO = new registeredUserDAO(sessionFactory);
        storeDAO = new storeDAO(sessionFactory);
        adminDAO = new adminDAO(sessionFactory);
        employmentDAO = new employmentDAO(sessionFactory);
    }

    public static DALService getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DALService.class){
                if(instance == null)
                    instance = new DALService();
            }
        }
        return instance;
    }

    /**
     * A function to delete all the data in the db
     */
    public void deleteDBData() throws SQLException {
        if(!TestsFlags.getInstance().isTests()) {
            try {
                employmentDAO.deleteData();
                adminDAO.deleteData();
                userDAO.deleteData();
                storeDAO.deleteData();
                cartProductDAO.deleteData();
                storeProductDAO.deleteData();
            } catch (Exception e) {
                throw new SQLException("SQL fail in saveUser");
            }
        }
    }

    /**
     * Function to save a user in the db
     * @param userName - the username
     * @param password - his password
     * @throws SQLException - if cant connect to db
     */
    public void saveUser(String userName, byte[] password) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                userDAO.saveUser(userName, password);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    /**
     * A Function to get a user by the inputed username
     * @param userName - the username of the user we want to get
     * @return - the DTO object of the user or a null if it doesnt exist
     */
    public registeredUserDTO getUser(String userName) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return userDAO.getUser(userName);
            return null;
        }
            catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }
    public boolean adminExists() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return adminDAO.userDbNotEmpty();
            return false;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    /**
     * Function to save the admin user into the db
     * @param userName - the username
     * @throws SQLException if cant connect to db
     */
    public void saveAdmin(String userName) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                 adminDAO.saveAdminUser(userName);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    /**
     * A Function to check if the username is an admin
     * @param userName - the username of the user we want to get
     * @return - true if the userName is in admin table else false
     * @throws SQLException if cant connect to db
     */
    public boolean isAdmin(String userName) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return adminDAO.isAdmin(userName);
            return false;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    /**
     * A function to save the employment in the db
     * @param employee - employee username
     * @param storeId - the store id the employment was created fir
     * @param appointer - the appointers username
     * @param role - the role (0,1,2)
     * @param permissions - a string of permissions
     */
    public void saveEmployment(String employee,int storeId, String appointer,int role, String permissions) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                employmentDAO.saveEmployment(employee,storeId,appointer,role,permissions);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveEmployment");
        }
    }
    /**
     * A function to update the employment in the db
     * @param employee - employee username
     * @param storeId - the store id the employment was created fir
     * @param appointer - the appointers username
     * @param role - the role (0,1,2)
     * @param permissions - a string of permissions
     */
    public void updateEmployment(String employee,int storeId, String appointer,int role, String permissions) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                 employmentDAO.updateEmployment(employee,storeId,appointer,role,permissions);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveEmployment");
        }
    }

    /**
     * A function the get the unique employment by the employee and by storeId
     * @param employee - (the username that got appointed)
     * @param storeId - the store the employment belongs to
     * @return an employmentDTO representing the employment from the db
     * @throws SQLException if cant connect to db
     */
    public EmploymentDTO getEmploymentByUsernameAndStoreId(String employee, int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return employmentDAO.getEmploymentByUsernameAndStoreId(employee,storeId);
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }

    /**
     * A function to get the owner employments of a store
     * @param storeId - the id of said store
     * @return - a linked list containing all the dtos of employments of store owners
     * @throws SQLException if cant connect to db
     */
    public LinkedList<EmploymentDTO> getStoreOwnersEmployment(int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return employmentDAO.getStoreOwnersEmployment(storeId);
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    /**
     * A function to get the all employees of a given store
     * @param storeId - the id of said store
     * @return - a linked list containing all the dtos of employments of store owners
     * @throws SQLException if cant connect to db
     */
    public LinkedList<EmploymentDTO> getStoreEmployment(int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return employmentDAO.getStoreEmployment(storeId);
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    /**
     * Gets the store owners based on the id of the store
     * @param storeId id of said store
     * @return a linked list containing the dtos of the owners
     * @throws SQLException
     */
    public LinkedList<registeredUserDTO> getStoreOwnersByStoreId(int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests()) {
                LinkedList<String> userNameList = employmentDAO.getStoreOwnerUserNamesByStoreId(storeId);
                return userDAO.getUsersByUserNames(userNameList);
            }
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, getStoreOwnersByStoreId");
        }
    }

    /**
     * checks if the Db is empty
     * @return - true if not empty else false
     * @throws SQLException - if cant connect to db
     */
    public boolean dbNotEmpty() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return userDAO.userDbNotEmpty();
            return false;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }

    /**
     * given a username, gets all the stores that user is an owner of
     * @param userName a user's username
     * @return a collection of storeDTO, representing many stores
     * @throws SQLException if any data base errors occur
     */
    public Collection<StoreDTO> getStoresFromOwner(String userName) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests()) {
                Collection<Integer> stores = employmentDAO.getStoresByEmployment(userName);
                return storeDAO.getStoresByStoreId(stores);
            }
            return new ArrayList<>();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }

    /**
     * persists a store with the given data
     * @param storeId store's id
     * @param name store's name
     * @param active true if store is active, false otherwise
     * @param rate store's avarage rating
     * @throws SQLException if any data base errors occur
     */
    public void saveStore(int storeId, String name, boolean active, double rate) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                storeDAO.saveStore(storeId, name, active, rate);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in save store");
        }
    }

    /**
     * gets a list of all stores in the database
     * @return a collection of stores
     * @throws SQLException if any data base errors occur
     */
    public Collection<StoreDTO> getStores() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeDAO.getStores();
            return new ArrayList<>();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }



    /**
     * returns a store with the id of storeId, or null if no such store exists
     * @param storeId the store's id to look for
     * @return a storeDTO representing a store's data
     * @throws SQLException if any data base errors occur
     */
    public StoreDTO getStoreById(int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeDAO.getStore(storeId);
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    /**
     * returns the max id of a store stored, so the system will be able to add more stores with valid ids
     * @return the max store id
     * @throws SQLException if any data base errors occur
     */
    public Integer getMaxStoreId() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeDAO.getMaxID();
            return 0;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    /**
     * returns the max id of a product stored, so the system will be able to add more products with valid ids
     * @return the max product id
     * @throws SQLException if any data base errors occur
     */
    public Integer getMaxProductId() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeProductDAO.getMaxID();
            return 0;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    /**
     * saves the product with all the fields
     * @param productId product's id
     * @param storeId store's id
     * @param productName product's name
     * @param price product's price
     * @param quantity product's quantity
     * @param category product's category
     * @param desc product's description
     * @param avgRating product's avarage rating
     * @throws SQLException  if cant connect to db
     */
    public void saveProduct(int productId,int storeId,String productName, Double price, int quantity, String category, String desc,double avgRating) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                storeProductDAO.saveProduct(productId,storeId,productName,price,category,desc,quantity,avgRating);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * updates the store product with corresponding productId with all the fields
     * @param productId  productId
     * @param storeId storeId
     * @param productName productName
     * @param price price
     * @param category category
     * @param description description
     * @param quantity quantity
     * @param averageRating averageRating
     * @throws SQLException if any database errors occur
     */
    public void updateStoreProduct(Integer productId, int storeId, String productName, Double price, String category, String description, int quantity, double averageRating) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                storeProductDAO.updateProduct(productId,storeId,productName,price,category,description,quantity,averageRating);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    public void removeStoreProduct(Integer productId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                storeProductDAO.removeProduct(productId);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    public void saveCartProduct(int productId,String userName,int amount) throws SQLException{
        try{
            if(!TestsFlags.getInstance().isTests())
                cartProductDAO.saveProduct(productId,userName,amount);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saving cart product");
        }
    }
    public void updateCartProduct(int productId, String userName, int amount) throws SQLException{
        try{
            if(!TestsFlags.getInstance().isTests())
                cartProductDAO.updateProduct(productId,userName,amount);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saving cart product");
        }
    }
    public void removeCartProduct(int productId, String userName) throws SQLException{
        try{
            if(!TestsFlags.getInstance().isTests())
                cartProductDAO.remove(productId,userName);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saving cart product");
        }
    }
    public Collection<CartProductDTO> getCartProducts(String userName) throws SQLException{
        try{
            if(!TestsFlags.getInstance().isTests())
                return cartProductDAO.getUserProducts(userName);
            return new ArrayList<>();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saving cart product");
        }
    }
    public Long storeCounter() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeDAO.storeCounter();
            return 0L;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    public Long storeProductCounter() throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return storeProductDAO.storeCounter();
            return 0L;
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    public Collection<EmploymentDTO> getEmploymentsByName(String name) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                return employmentDAO.getEmploymentsFromName(name);
            return new ArrayList<>();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }

    public void removeEmployee(String appointedUserName, int storeId) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                employmentDAO.removeEmployee(appointedUserName,storeId);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }

    public void changeStoreProductAmount(int productId, int newAmount) throws SQLException {
        try{
            if(!TestsFlags.getInstance().isTests())
                storeProductDAO.changeStoreProductAmount(productId,newAmount);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
}
