package serviceJpa;

import entite.Seance;
import daoJpa.SeanceDao;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class SeanceService extends GenericService<Seance, Integer> {

    private SeanceDao seanceDao;

    public SeanceService(EntityManagerFactory emf) {
        super(new SeanceDao(emf));
        this.seanceDao = (SeanceDao) dao;
    }

    @Override
    public Seance trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Seance> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les séances en fonction du libellé de la salle associée.
     * @param searchTerm Le nom de la salle à rechercher.
     * @return Une liste de Seance correspondant au critère.
     */
    public List<Seance> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(seance -> seance.getSalle() != null &&
                        seance.getSalle().getLibelle().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }

    public List<Seance> findSeancesByClientId(int clientId) {
        return super.executeInTransaction(em -> {
            TypedQuery<Seance> query = em.createQuery(
                "SELECT s FROM Seance s JOIN s.inscriptions i WHERE i.membre.client.id = :clientId ORDER BY s.horaire.jour DESC, s.horaire.heureDebut DESC",
                Seance.class
            );
            query.setParameter("clientId", clientId);
            return query.getResultList();
        });
    }
}