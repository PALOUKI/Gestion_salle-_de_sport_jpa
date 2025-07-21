package daoJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class GenericDao <Entity, Key> {
    protected EntityManager em ;
    private Class<Entity> entityClass;
    public GenericDao(Class<Entity> entityClass){
        this.entityClass = entityClass;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("salle_de_sport");
        em = emf.createEntityManager();
    }

    public GenericDao(Class<Entity> entityClass, EntityManagerFactory emf) {
        this.entityClass = entityClass;
        this.em = emf.createEntityManager();
    }

    public void ajouter(Entity entite){
        em.getTransaction().begin();
        em.persist(entite);
        em.getTransaction().commit();
    }
    public void modifier(Entity entite){
        em.getTransaction().begin();
        em.merge(entite);
        em.getTransaction().commit();

    }
    public void supprimer(Entity entite){
        em.getTransaction().begin();
        em.remove(entite);
        em.getTransaction().commit();

    }
    public Entity trouver (Key id){
        // Cette méthode sera surchargée, mais une implémentation par défaut est bonne pour la compilation.
        return null;
    }
    public List<Entity> listerTous (){
        // Cette méthode sera surchargée, mais une implémentation par défaut est bonne pour la compilation.
        return null;
    }

    public long countAll() {
        return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }

    public EntityManager getEntityManager() {
        return em;
    }
}