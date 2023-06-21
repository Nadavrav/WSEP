package DAL.DAOs;

import DAL.DTOs.StoreDTO;
import DAL.DTOs.StoreProductDTO;
import DAL.Entities.StoreEntity;
import DAL.Entities.StoreproductEntity;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class StoreProductDAO {
    private SessionFactory sessionFactory;
    public StoreProductDAO(SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }

    public void saveProduct(int productId, int storeId, String name, double price, String category, String desc,int quantity, double avgRating) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            StoreproductEntity storeproductEntity = new StoreproductEntity(productId, storeId, name, price,  category,  desc,quantity,avgRating);
            session.beginTransaction();
            session.save(storeproductEntity);
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

    public StoreProductDTO getProducts(int storeId) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<StoreproductEntity> query = session.createQuery("FROM StoreproductEntity WHERE storeId=:storeId");
            query.setParameter("storeId", storeId);
            StoreproductEntity storeProduct = query.uniqueResult();
            if(storeProduct != null) {
                session.close();
                return new StoreProductDTO(storeProduct);
                //to add: storeEntity.getProduct which executes another query??
            }
        }
        catch (Exception e) {
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

    public Integer getMaxID() throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<StoreproductEntity> query=  session.createQuery("FROM StoreproductEntity WHERE id = (SELECT MAX(id) FROM StoreEntity)");
            StoreproductEntity storeEntity = query.uniqueResult();
            if(storeEntity==null)
                return 0;
            return storeEntity.getProductId();
        }
        catch (Exception e) {
            throw new SQLException("SQL fail in get max store ID");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

    public void updateProduct(Integer productId, int storeId, String productName, Double price, String category, String description, int quantity, double averageRating) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            StoreproductEntity storeproductEntity = session.get(StoreproductEntity.class, productId);
            if (storeproductEntity != null) {
                // Update the fields with new values
                storeproductEntity.setStoreId(storeId);
                storeproductEntity.setName(productName);
                storeproductEntity.setPrice(price);
                storeproductEntity.setCategory(category);
                storeproductEntity.setDesc(description);
                storeproductEntity.setQuantity(quantity);
                storeproductEntity.setAvgRating(averageRating);
                session.update(storeproductEntity);
                session.getTransaction().commit();
            }
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
