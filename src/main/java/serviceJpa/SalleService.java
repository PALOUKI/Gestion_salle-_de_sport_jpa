package serviceJpa;

import entite.Salle;
import daoJpa.SalleDao;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class SalleService extends GenericService<Salle, Integer> {

    private SalleDao salleDao;

    public SalleService() {
        super(new SalleDao());
        this.salleDao = (SalleDao) dao;
    }

    public SalleService(EntityManagerFactory emf) {
        super(new SalleDao(emf));
        this.salleDao = (SalleDao) dao;
    }

    @Override
    public Salle trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Salle> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les salles dont le libellé ou la description contient le terme de recherche.
     * @param searchTerm Le terme à rechercher.
     * @return Une liste de Salle correspondant au critère.
     */
    public List<Salle> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(salle -> salle.getLibelle().toLowerCase().contains(lowerCaseSearchTerm) ||
                        (salle.getDescription() != null && salle.getDescription().toLowerCase().contains(lowerCaseSearchTerm)))
                .collect(Collectors.toList());
    }
}