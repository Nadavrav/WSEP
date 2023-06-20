package DAL.DAOs;

import DAL.Entities.*;
import DAL.DTOs.*;
import DomainLayer.Users.Admin;
import org.hibernate.*;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
public class adminDAO {

    private SessionFactory sessionFactory;

    public adminDAO(SessionFactory sf) throws SQLException  {
        this.sessionFactory = sf;
    }
    public void deleteData() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(false);

            String deleteQuery = "DELETE FROM admin";
            Query query = session.createNativeQuery(deleteQuery);
            query.executeUpdate();

            transaction.commit();
        }catch (Exception e) {
            throw new SQLException("SQL fail in deleteData, Admin");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

    /**
     * Function to save the admin user into the db
     * @param userName - the username
     */
    public void saveAdminUser(String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            AdminEntity adminEntity = new AdminEntity(userName);

            session.beginTransaction();
            session.save(adminEntity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new SQLException("Cant connect to DB");
        }
        finally {
            if (session.isConnected()) {
                session.disconnect(); // Disconnect the session if it's still connected
            }
            session.close();
        }
    }

    /**
     * A Function to check if the username is an admin
     * @param userName - the username of the user we want to get
     * @return - true if the userName is in admin table else false
     */
    public boolean isAdmin(String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<RegistereduserEntity> query = session.createQuery("FROM AdminEntity WHERE userName = :username");
            query.setParameter("username", userName);
            RegistereduserEntity userEntity = query.uniqueResult();

            if(userEntity != null) {
                session.close();
                return true;
            }
        }catch (Exception e) {
            throw new SQLException("SQL fail in saveUser");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
        return false;
    }
}
