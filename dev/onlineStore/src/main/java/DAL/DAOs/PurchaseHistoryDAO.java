package DAL.DAOs;

import DAL.Entities.PurchaseHistoryEntity;
import DAL.Entities.RegistereduserEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;

public class PurchaseHistoryDAO {
    private SessionFactory sessionFactory;
    public PurchaseHistoryDAO(SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }
    /**
     * TODO AM LAZY
     */
    public void saveRecord(int storeId, String userName, LocalDateTime purchaseDate,int totalAmount,String products) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
       //     PurchaseHistoryEntity rUserEntity = new PurchaseHistoryEntity(storeId,userName,new Timestamp(purchaseDate.getLong()),totalAmount,products);

            session.beginTransaction();
         //   session.save(rUserEntity);
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
}
