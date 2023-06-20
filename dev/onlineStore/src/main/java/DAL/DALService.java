package DAL;

import DAL.DAOs.*;
import DAL.DTOs.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import java.sql.SQLException;


public class DALService {
    private static volatile DALService instance;
    private final registeredUserDAO userDAO;
    private final storeDAO storeDAO;
    private final adminDAO adminDAO;
    private SessionFactory sessionFactory;
    private DALService() throws SQLException {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }catch (Exception  e) {
            throw new SQLException("Cant connect to DB");
        }
        userDAO = new registeredUserDAO(sessionFactory);
        storeDAO = new storeDAO(sessionFactory);
        adminDAO = new adminDAO(sessionFactory);
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
    public void saveStore(int storeId, String name, boolean active, double rate) throws SQLException {
        try{
            storeDAO.saveStore(storeId, name, active, rate);
        }
        catch (Exception e)
        {
            throw new SQLException("SQL fail in saveUser");
        }
    }
}
