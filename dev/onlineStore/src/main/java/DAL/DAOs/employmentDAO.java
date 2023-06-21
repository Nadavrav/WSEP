package DAL.DAOs;

import DAL.DTOs.employmentDTO;
import DAL.Entities.EmploymentEntity;
import DAL.Entities.RegistereduserEntity;
import org.hibernate.*;

import java.sql.SQLException;

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

    public employmentDTO getEmploymentByUsernameAndStoreId(){
        return null;
    }

}
