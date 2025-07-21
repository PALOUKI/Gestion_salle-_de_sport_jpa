package daoJpa;

import entite.Abonnement;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class AbonnementDao extends GenericDao<Abonnement, Integer> {

    public AbonnementDao() {
        super(Abonnement.class);
    }

    public AbonnementDao(EntityManagerFactory emf) {
        super(Abonnement.class, emf);
    }

    @Override
    public Abonnement trouver(Integer id) {
        return em.find(Abonnement.class, id);
    }

    @Override
    public List<Abonnement> listerTous() {
        // Trie les abonnements par nom de membre, puis par date de début
        return em.createQuery("SELECT a FROM Abonnement a ORDER BY a.membre.client.nom, a.dateDebut DESC", Abonnement.class)
                .getResultList();
    }

    public List<Abonnement> findByClientId(Integer clientId) {
        return em.createQuery("SELECT a FROM Abonnement a WHERE a.membre.client.id = :clientId ORDER BY a.dateDebut DESC", Abonnement.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }
}