package DAL.DAOs;

import DAL.DTOs.CartProductDTO;
import DAL.DTOs.StoreProductDTO;
import DAL.Entities.CartproductEntity;
import DAL.Entities.CartproductEntityPK;
import DAL.Entities.StoreproductEntity;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CartProductDAO {
    private SessionFactory sessionFactory;
    public CartProductDAO(org.hibernate.SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }

    public void saveProduct(int productId, String userName,int amount) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(new CartproductEntity(productId,userName,amount));
            // session.save(storeproductEntity);
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            throw e;
        }
        finally {
            if (session.isConnected()) {
                session.disconnect(); // Disconnect the session if it's still connected
            }
            session.close();
        }
    }

    public Collection<CartProductDTO> getUserProducts(String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<CartproductEntity> query = session.createQuery("FROM CartproductEntity WHERE userName=:userName");
            query.setParameter("userName", userName);
            List<CartproductEntity> cartProducts = query.getResultList();
            if(cartProducts != null) {
                HashSet<CartProductDTO> cartProductDTOS=new HashSet<>();
                session.close();
                for(CartproductEntity cartProduct:cartProducts){
                    cartProductDTOS.add(new CartProductDTO(cartProduct));
                }
                return cartProductDTOS;
                //to add: storeEntity.getProduct which executes another query??
            }
        }
        catch (Exception e) {
            throw new SQLException("SQL fail in getProducts");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
        return null;
    }

    public void updateProduct(int productId, String userName, int amount) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            CartproductEntityPK key=new CartproductEntityPK(productId,userName);
            CartproductEntity cartproductEntity = session.get(CartproductEntity.class,key);
            if (cartproductEntity != null) {
                cartproductEntity.setAmount(amount);
                session.update(cartproductEntity);
            }
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

    public void remove(int productId, String userName) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            CartproductEntityPK key=new CartproductEntityPK(productId,userName);
            CartproductEntity cartproductEntity = session.get(CartproductEntity.class,key);
            if (cartproductEntity != null) {
                // Update the fields with new values
                //cartproductEntity.setProductId(productId);
                //cartproductEntity.setUserName(userName);
                session.delete(cartproductEntity);
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
