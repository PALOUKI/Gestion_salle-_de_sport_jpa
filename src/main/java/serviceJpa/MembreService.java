package serviceJpa;

import entite.Membre;
import daoJpa.MembreDao;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class MembreService extends GenericService<Membre, Integer> {

    private MembreDao membreDao;

    public MembreService() {
        super(new MembreDao());
        this.membreDao = (MembreDao) dao;
    }

    public MembreService(EntityManagerFactory emf) {
        super(new MembreDao(emf));
        this.membreDao = (MembreDao) dao;
    }

    @Override
    public List<Membre> listerTous() {
        return super.listerTous();
    }

    public List<Membre> rechercherParNomOuPrenomClient(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }
        return listerTous().stream()
                .filter(m -> m.getClient() != null &&
                        (m.getClient().getNom().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                m.getClient().getPrenom().toLowerCase().contains(searchTerm.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public Membre trouver(Integer id) {
        return super.trouver(id);
    }

    public Membre findByClientId(Integer clientId) {
        return membreDao.findByClientId(clientId);
    }

    public boolean membreExistePourClient(Integer clientId) {
        return findByClientId(clientId) != null;
    }

    /**
     * Recherche des membres en fonction du nom ou du prénom du client associé.
     * @param searchTerm Le terme à rechercher.
     * @return Une liste de Membre correspondant au critère.
     */
    public List<Membre> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(membre -> membre.getClient() != null &&
                        (membre.getClient().getNom().toLowerCase().contains(lowerCaseSearchTerm) ||
                                membre.getClient().getPrenom().toLowerCase().contains(lowerCaseSearchTerm)))
                .collect(Collectors.toList());
    }
}