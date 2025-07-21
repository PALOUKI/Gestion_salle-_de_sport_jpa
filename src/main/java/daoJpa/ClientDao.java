package daoJpa;

import entite.Client;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ClientDao extends GenericDao<Client, Integer> {

    public ClientDao() {
        super(Client.class);
    }

    public ClientDao(EntityManagerFactory emf) {
        super(Client.class, emf);
    }

    @Override
    public Client trouver(Integer id) {
        return em.find(Client.class, id);
    }

    @Override
    public List<Client> listerTous() {
        return em.createQuery("SELECT c FROM Client c ORDER BY c.nom, c.prenom", Client.class).getResultList();
    }

    public List<Client> listerRecents(int maxResults) {
        return em.createQuery("SELECT c FROM Client c ORDER BY c.id DESC", Client.class)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public Client findByEmail(String email) {
        try {
            return em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) { // NoResultException ou NonUniqueResultException
            return null;
        }
    }
}