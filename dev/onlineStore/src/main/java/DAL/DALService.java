package DAL;

import DAL.DAOs.*;
import DAL.DTOs.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Collection;


public class DALService {
    private static volatile DALService instance;
    private final registeredUserDAO userDAO;
    private final storeDAO storeDAO;
    private final StoreProductDAO storeProductDAO;
    private final adminDAO adminDAO;
    private final employmentDAO employmentDAO;
    private SessionFactory sessionFactory;
    private DALService() throws SQLException {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }catch (Exception  e) {
            throw new SQLException("Cant connect to DB");
        }
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
        try{
            employmentDAO.deleteData();
            adminDAO.deleteData();
            userDAO.deleteData();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
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
            return userDAO.getUser(userName);
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
            return adminDAO.isAdmin(userName);
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
            employmentDAO.saveEmployment(employee,storeId,appointer,role,permissions);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    /**
     * A function the get the unique employment by the employee and by storeId
     * @param employee - (the user name that got appointed)
     * @param storeId - the store the employment belongs to
     * @return an employmentDTO representing the employment from the db
     * @throws SQLException if cant connect to db
     */
    public employmentDTO getEmploymentByUsernameAndStoreId(String employee,int storeId) throws SQLException {
        try{
            return employmentDAO.getEmploymentByUsernameAndStoreId(employee,storeId);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    public LinkedList<registeredUserDTO> getStoreOwnersByStoreId(int storeId) throws SQLException {
        try{
            LinkedList<String> userNameList = employmentDAO.getStoreOwnerUserNamesByStoreId(storeId);
            return userDAO.getUsersByUserNames(userNameList);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getEmployment, username and storeId");
        }
    }
    public void saveStore(int storeId, String name, boolean active, double rate) throws SQLException {
        try{
            storeDAO.saveStore(storeId, name, active, rate);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }

    public Collection<StoreDTO> getStores() throws SQLException {
        try{
            return storeDAO.getStores();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }

    public Integer getMaxStoreId() throws SQLException {
        try{
            return storeDAO.getMaxID();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
    public StoreDTO getStoreById(int storeId) throws SQLException {
        try{
            return storeDAO.getStore(storeId);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }

    public Integer getMaxProductId() throws SQLException {
        try{
            return storeProductDAO.getMaxID();
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }

    public void saveProduct(int productId,int storeId,String productName, Double price, int quantity, String category, String desc,double avgRating) throws SQLException {
        try{
            storeProductDAO.saveProduct(productId,storeId,productName,price,category,desc,quantity,avgRating);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in getStores");
        }
    }
}