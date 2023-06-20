package DAL.DAOs;

import DAL.Entities.*;
import DAL.DTOs.*;
import org.hibernate.*;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class storeDAO {
    private SessionFactory sessionFactory;
    public storeDAO(SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }

    public void saveStore(int storeId, String name, boolean active, double rate) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            StoreEntity storeEntity = new StoreEntity(storeId,name,active,rate);

            session.beginTransaction();
            session.save(storeEntity);
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
