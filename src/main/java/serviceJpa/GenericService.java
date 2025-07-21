package serviceJpa;

import daoJpa.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GenericService <Entity, Key>{

    protected GenericDao<Entity, Key> dao; // Make dao generic

    public GenericService (GenericDao<Entity, Key> dao){ // Make constructor parameter generic
        this.dao = dao;
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager em = dao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            R result = action.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void ajouter(Entity entite){
        dao.ajouter(entite);
    }
    public void modifier(Entity entite){
        dao.modifier(entite);
    }
    public void supprimer(Entity entite){
        dao.supprimer(entite);

    }
    public Entity trouver(Key id){
        return dao.trouver(id); // Call dao's trouver method
    }
    public List<Entity> listerTous(){
        return dao.listerTous(); // Call dao's listerTous method
    }

    public long countAll() {
        return dao.countAll();
    }
}