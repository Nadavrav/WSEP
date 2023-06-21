package DAL.DAOs;

import DAL.DTOs.employmentDTO;
import DAL.DTOs.registeredUserDTO;
import DAL.Entities.EmploymentEntity;
import DAL.Entities.RegistereduserEntity;
import org.hibernate.*;

import java.sql.SQLException;
import java.util.*;

public class employmentDAO {
    private SessionFactory sessionFactory;
    public employmentDAO(SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }

    public void deleteData() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(false);

            String deleteQuery = "DELETE FROM employment";
            Query query = session.createNativeQuery(deleteQuery);
            query.executeUpdate();

            transaction.commit();
        }catch (Exception e) {
            throw new SQLException("SQL fail in deleteData, employment");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

    /**
     * A function to save an employment status into the db
     * @param employee - the username of the employee
     * @param storeId - the store id that the employment belongs to
     * @param appointer - the username of who make the appointment
     * @param role - the role of the employee
     * @param permissions - the permissions the employee has as a string or numbers sepereted by a ','
     * @throws SQLException - if there was an error connecting to db
     */
    public void saveEmployment(String employee,int storeId, String appointer,int role, String permissions) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            EmploymentEntity employmentEntity = new EmploymentEntity(employee,storeId,appointer,role,permissions);

            session.beginTransaction();
            session.save(employmentEntity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new SQLException("Cant connect to DB,saveEmployment");
        }
        finally {
            if (session.isConnected()) {
                session.disconnect(); // Disconnect the session if it's still connected
            }
            session.close();
        }
    }

    /**
     * A function the get the unique employment by the employee and by storeId
     * @param employee - (the user name that got appointed)
     * @param storeId - the store the employment belongs to
     * @return an employmentDTO representing the employment from the db
     */
    public employmentDTO getEmploymentByUsernameAndStoreId(String employee,int storeId) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<EmploymentEntity> query = session.createQuery("FROM EmploymentEntity WHERE employee = :username AND storeId = :storeId");
            query.setParameter("username", employee);
            query.setParameter("storeId",storeId);
            EmploymentEntity employmentEntity = query.uniqueResult();

            if(employmentEntity != null) {
                session.close();
                return new employmentDTO(employmentEntity.getEmployee(),employmentEntity.getStoreId(),employmentEntity.getAppointer(),employmentEntity.getRole(),employmentEntity.getPermissions());
            }
        }catch (Exception e) {
            throw new SQLException("SQL fail in getEmploymentByUsernameAndStoreId");
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
     * A function to get the usernames of all the owners of the store
     * @param storeId - the id of said store
     * @return - a linked list containing all the usernames that are the owners of the store
     */
    public LinkedList<String> getStoreOwnerUserNamesByStoreId(int storeId) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<EmploymentEntity> query = session.createQuery("FROM EmploymentEntity WHERE storeId = :storeId AND role != 0");
            query.setParameter("storeId",storeId);
            LinkedList<EmploymentEntity> employmentEntities = (LinkedList<EmploymentEntity>) query.getResultList();

            if(employmentEntities != null && !employmentEntities.isEmpty()) {
                session.close();
                LinkedList<String> userNames = new LinkedList<>();
                for (EmploymentEntity EmEntity: employmentEntities) {
                    userNames.add(EmEntity.getEmployee());
                    }
                return userNames;
                }
        }catch (Exception e) {
            throw new SQLException("SQL fail in getEmploymentByUsernameAndStoreId");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
        return null;
    }

    public Collection<Integer> getStoresByEmployment(String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<EmploymentEntity> query = session.createQuery("FROM EmploymentEntity WHERE employee = :userName AND role != 0");
            query.setParameter("userName",userName);
            List<EmploymentEntity> employmentEntities = query.getResultList();

            if(employmentEntities != null && !employmentEntities.isEmpty()) {
                session.close();
                HashSet<Integer> stores = new HashSet<>();
                for (EmploymentEntity EmEntity: employmentEntities) {
                    stores.add(EmEntity.getStoreId());
                }
                return stores;
            }
        }catch (Exception e) {
            throw new SQLException("SQL fail in getEmploymentByUsernameAndStoreId");
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
