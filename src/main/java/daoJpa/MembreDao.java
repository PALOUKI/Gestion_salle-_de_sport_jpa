package daoJpa;

import entite.Membre;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class MembreDao extends GenericDao<Membre, Integer> {

    public MembreDao() {
        super(Membre.class);
    }

    public MembreDao(EntityManagerFactory emf) {
        super(Membre.class, emf);
    }

    @Override
    public Membre trouver(Integer id) {
        return em.find(Membre.class, id);
    }

    @Override
    public List<Membre> listerTous() {
        // Trie les membres par le nom de famille du client associé
        return em.createQuery("SELECT m FROM Membre m ORDER BY m.client.nom, m.client.prenom", Membre.class).getResultList();
    }

    public Membre findByClientId(Integer clientId) {
        try {
            return em.createQuery("SELECT m FROM Membre m WHERE m.client.id = :clientId", Membre.class)
                    .setParameter("clientId", clientId)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Retourne null si aucun membre n'est trouvé
        }
    }
}