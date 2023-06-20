package DAL.DAOs;

import DAL.Entities.*;
import DAL.DTOs.*;
import org.hibernate.*;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class registeredUserDAO {
    private SessionFactory sessionFactory;
    public registeredUserDAO(SessionFactory sf) throws SQLException  {
        this.sessionFactory = sf;
    }

    /**
     * Function to save the user into the db
     * @param userName - the username
     * @param password - his password
     */
    public void saveUser(String userName, byte[] password) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            RegistereduserEntity rUserEntity = new RegistereduserEntity(userName,password);

            session.beginTransaction();
            session.save(rUserEntity);
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
     * A Function to get a user by the inputed username
     * @param userName - the username of the user we want to get
     * @return - the DTO object of the user or a null if it doesnt exist
     */
    public registeredUserDTO getUser(String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<RegistereduserEntity> query = session.createQuery("FROM RegistereduserEntity WHERE userName = :username");
            query.setParameter("username", userName);
            RegistereduserEntity userEntity = query.uniqueResult();

            if(userEntity != null) {
                session.close();
                return new registeredUserDTO(userEntity.getUserName(), userEntity.getPassword());
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
        return null;
    }

}
