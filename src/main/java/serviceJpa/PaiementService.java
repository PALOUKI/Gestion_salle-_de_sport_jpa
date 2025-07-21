package serviceJpa;

import entite.Paiement;
import daoJpa.PaiementDao;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class PaiementService extends GenericService<Paiement, Integer> {

    private PaiementDao paiementDao;

    public PaiementService(EntityManagerFactory emf) {
        super(new PaiementDao(emf));
        this.paiementDao = (PaiementDao) dao;
    }

    @Override
    public Paiement trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Paiement> listerTous() {
        return dao.listerTous();
    }

    public List<Paiement> listerRecents(int maxResults) {
        return paiementDao.listerRecents(maxResults);
    }

    public List<Paiement> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(p -> p.getAbonnement() != null &&
                        p.getAbonnement().getMembre() != null &&
                        p.getAbonnement().getMembre().getClient() != null &&
                        (p.getAbonnement().getMembre().getClient().getNom().toLowerCase().contains(lowerCaseSearchTerm) ||
                                p.getAbonnement().getMembre().getClient().getPrenom().toLowerCase().contains(lowerCaseSearchTerm)))
                .collect(Collectors.toList());
    }
}