package DAL.DAOs;

import DAL.DTOs.StoreDTO;
import DAL.DTOs.registeredUserDTO;
import DAL.Entities.*;
import org.hibernate.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class storeDAO {
    private SessionFactory sessionFactory;
    public storeDAO(SessionFactory sf) throws SQLException {
        this.sessionFactory = sf;
    }
    public void deleteData() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(false);

            String deleteQuery = "DELETE FROM store";
            Query query = session.createNativeQuery(deleteQuery);
            query.executeUpdate();

            transaction.commit();
        }catch (Exception e) {
            throw new SQLException("SQL fail in deleteData, store");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
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
    /**
     * A Function to get a user by the inputted storeName
     * @param storeName - the NAME of the store we want to get
     * @return - the DTO object of the store or a null if it doesnt exist
     */
    public StoreDTO getStores(String storeName) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<StoreEntity> query = session.createQuery("FROM StoreEntity se LEFT JOIN FETCH se.products WHERE se.name = :storeName");
            query.setParameter("storeName", storeName);
            StoreEntity storeEntity = query.uniqueResult();

            if(storeEntity != null) {
                session.close();
                return new StoreDTO(storeEntity);
                //to add: storeEntity.getProduct which executes another query??
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
     * A Function to get a user by the inputted id
     * @param storeId - the id of the store we want to get
     * @return - the DTO object of the store or a null if it doesnt exist
     */
    public StoreDTO getStore(int storeId) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<StoreEntity> query = session.createQuery("FROM StoreEntity se LEFT JOIN FETCH se.products WHERE se.id = :storeId");
            query.setParameter("storeId", storeId);
            StoreEntity storeEntity = query.uniqueResult();
            if(storeEntity != null) {
                session.close();
                return new StoreDTO(storeEntity);
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
    public Collection<StoreDTO> getStores() throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query<StoreEntity> query = session.createQuery("FROM StoreEntity se LEFT JOIN FETCH se.products");
          //  Query<StoreEntity> query = session.createQuery("FROM StoreEntity");

            Collection<StoreEntity> storeEntities = query.getResultList();

            if(storeEntities != null) {
                session.close();
                HashSet<StoreDTO> storeDTOs=new HashSet<>();
                for(StoreEntity store:storeEntities)
                    storeDTOs.add(new StoreDTO(store));
                return storeDTOs;
                //to add: storeEntity.getProduct which executes another query??
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
    public Integer getMaxID() throws SQLException {
        Session session = sessionFactory.openSession();
        try{
          //  Query<StoreEntity> query = session.createQuery("SELECT id FROM StoreEntity");
            Query<StoreEntity> query=  session.createQuery("FROM StoreEntity WHERE id = (SELECT MAX(id) FROM StoreEntity)");
            StoreEntity storeEntity = query.uniqueResult();
            if(storeEntity==null)
                return 0;
            return storeEntity.getId();
        }catch (Exception e) {
            throw new SQLException("SQL fail in get max store ID");
        }
        finally {
            if (session != null && session.isOpen()) {
                session.disconnect(); // Disconnect the session if it's still connected
                session.close();
            }
        }
    }

    public Collection<StoreDTO> getStoresByStoreId(Collection<Integer> stores) throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            HashSet<StoreDTO> storeDTOS = new HashSet<>();
            for (Integer id: stores) {
                Query<StoreEntity> query = session.createQuery("FROM StoreEntity WHERE id = :id");
                query.setParameter("id", id);
                StoreEntity store = query.uniqueResult();

                if(store == null) {
                    session.close();
                    throw new SQLException("SQL fail in getUsersByUserNames");
                }
                else{
                    StoreDTO storeDTO = new StoreDTO(store);
                    storeDTOS.add(storeDTO);
                }
            }
            session.close();
            return storeDTOS;

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

    public Long storeCounter() throws SQLException {
        Session session = sessionFactory.openSession();
        try{
            Query query = session.createQuery("SELECT count(*) FROM StoreEntity ");
            return (Long) query.uniqueResult();

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
