package daoJpa;

import entite.DemandeInscription;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class DemandeInscriptionDao extends GenericDao<DemandeInscription, Integer> {

    public DemandeInscriptionDao() {
        super(DemandeInscription.class);
    }

    public DemandeInscriptionDao(EntityManagerFactory emf) {
        super(DemandeInscription.class, emf);
    }

    @Override
    public DemandeInscription trouver(Integer id) {
        return em.find(DemandeInscription.class, id);
    }

    @Override
    public List<DemandeInscription> listerTous() {
        // Trie les demandes de la plus récente à la plus ancienne
        return em.createQuery("SELECT d FROM DemandeInscription d ORDER BY d.dateDeDemande DESC", DemandeInscription.class).getResultList();
    }

    public List<DemandeInscription> listerRecents(int maxResults) {
        return em.createQuery("SELECT d FROM DemandeInscription d ORDER BY d.dateDeDemande DESC", DemandeInscription.class)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public List<DemandeInscription> findByEmail(String email) {
        TypedQuery<DemandeInscription> query = getEntityManager().createQuery(
                "SELECT d FROM DemandeInscription d WHERE d.email LIKE :email", DemandeInscription.class);
        query.setParameter("email", "%" + email + "%");
        return query.getResultList();
    }
}