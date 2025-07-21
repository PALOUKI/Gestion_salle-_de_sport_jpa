package serviceJpa;

import entite.Client;
import daoJpa.ClientDao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService extends GenericService<Client, Integer> {

    private ClientDao clientDao;

    public ClientService() {
        super(new ClientDao());
        this.clientDao = (ClientDao) dao;
    }

    public ClientService(EntityManagerFactory emf) {
        super(new ClientDao(emf));
        this.clientDao = (ClientDao) dao;
    }

    @Override
    public Client trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public java.util.List<Client> listerTous() {
        return dao.listerTous();
    }

    public List<Client> listerRecents(int maxResults) {
        return clientDao.listerRecents(maxResults);
    }

    public Client findByEmail(String email) {
        return super.executeInTransaction(em -> {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class);
            query.setParameter("email", email);
            List<Client> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }

    public boolean emailExiste(String email) {
        return findByEmail(email) != null;
    }

    public List<Client> searchClients(String searchTerm) {
        return super.executeInTransaction(em -> {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE LOWER(c.nom) LIKE LOWER(:term) OR LOWER(c.prenom) LIKE LOWER(:term) OR LOWER(c.email) LIKE LOWER(:term)",
                Client.class
            );
            query.setParameter("term", "%" + searchTerm + "%");
            return query.getResultList();
        });
    }

    /**
     * Recherche les clients dont le nom, le prénom ou l'email contient le terme de recherche.
     * Si le terme est vide ou null, retourne tous les clients.
     * @param searchTerm Le terme à rechercher (non sensible à la casse).
     * @return Une liste de Clients correspondant au critère.
     */
    public List<Client> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous(); // Si le terme est vide, on retourne tout
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        // Filtre en mémoire. Pour de grandes bases de données, une requête JPQL serait plus performante.
        return listerTous().stream()
                .filter(client -> client.getNom().toLowerCase().contains(lowerCaseSearchTerm) ||
                        client.getPrenom().toLowerCase().contains(lowerCaseSearchTerm) ||
                        client.getEmail().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }
}