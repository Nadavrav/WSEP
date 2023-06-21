package DAL.DAOs;

import DAL.Entities.*;
import DAL.DTOs.*;
import org.hibernate.*;
import java.sql.SQLException;
import java.util.LinkedList;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class registeredUserDAO {
    private SessionFactory sessionFactory;
    public registeredUserDAO(SessionFactory sf) throws SQLException  {
        this.sessionFactory = sf;
    }

    public void deleteData() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(false);

            String deleteQuery = "DELETE FROM registereduser";
            Query query = session.createNativeQuery(deleteQuery);
            query.executeUpdate();

            transaction.commit();
        }catch (Exception e) {
            throw new SQLException("SQL fail in deleteData, registeredUser");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
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

    /**
     * A function to get all the user from a list of userNames
     * @param userNames - the list of user names
     * @return - a list of registeredUserDTO that represents each user
     * @throws SQLException - if cant connect to db
     */
    public LinkedList<registeredUserDTO> getUsersByUserNames(LinkedList<String> userNames) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            LinkedList<registeredUserDTO> userDTOS = new LinkedList<>();
            for (String username: userNames) {
                Query<RegistereduserEntity> query = session.createQuery("FROM RegistereduserEntity WHERE userName = :username");
                query.setParameter("username", username);
                RegistereduserEntity userEntity = query.uniqueResult();

                if(userEntity == null) {
                    session.close();
                    throw new SQLException("SQL fail in getUsersByUserNames");
                }
                else{
                    registeredUserDTO userDTO = new registeredUserDTO(userEntity.getUserName(), userEntity.getPassword());
                    userDTOS.add(userDTO);
                }
            }
            session.close();
            return userDTOS;

        }catch (Exception e) {
            throw new SQLException("SQL fail in saveUser");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

    /**
     * Checks if the user table is empty
     * @return - true if empty false if not
     * @throws SQLException - if cant connect to db
     */
    public boolean userDbNotEmpty() throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query query = session.createQuery("SELECT count(*) FROM RegistereduserEntity");
            Long rows = (Long) query.uniqueResult();
            if(rows == 0) {
                return false;
            }
            return true;
        }catch (Exception e) {
            throw new SQLException("SQL fail in saveUser");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

}
