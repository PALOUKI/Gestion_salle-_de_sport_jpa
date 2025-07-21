package daoJpa;

import entite.TypeAbonnement;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TypeAbonnementDao extends GenericDao<TypeAbonnement, String> {

    public TypeAbonnementDao() {
        super(TypeAbonnement.class);
    }

    public TypeAbonnementDao(EntityManagerFactory emf) {
        super(TypeAbonnement.class, emf);
    }

    @Override
    public TypeAbonnement trouver(String code) { // Parameter type changed to String
        return em.find(TypeAbonnement.class, code);
    }

    @Override
    public List<TypeAbonnement> listerTous() {
        return em.createQuery("SELECT t FROM TypeAbonnement t", TypeAbonnement.class).getResultList();
    }
}