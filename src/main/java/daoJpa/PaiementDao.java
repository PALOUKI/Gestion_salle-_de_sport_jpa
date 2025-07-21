package daoJpa;

import entite.Paiement;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PaiementDao extends GenericDao<Paiement, Integer> {

    public PaiementDao(EntityManagerFactory emf) {
        super(Paiement.class, emf);
    }

    @Override
    public Paiement trouver(Integer id) {
        return em.find(Paiement.class, id);
    }

    @Override
    public List<Paiement> listerTous() {
        // Trie les paiements par date, du plus récent au plus ancien
        return em.createQuery("SELECT p FROM Paiement p ORDER BY p.dateDePaiement DESC", Paiement.class).getResultList();
    }

    /**
     * Récupère les N paiements les plus récents.
     * @param maxResults Le nombre maximum de paiements à retourner.
     * @return Une liste des paiements les plus récents.
     */
    public List<Paiement> listerRecents(int maxResults) {
        return em.createQuery("SELECT p FROM Paiement p ORDER BY p.dateDePaiement DESC", Paiement.class)
                .setMaxResults(maxResults)
                .getResultList();
    }
}