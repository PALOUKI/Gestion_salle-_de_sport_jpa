package serviceJpa;

import entite.MoyenDePaiement;
import daoJpa.MoyenDePaiementDao;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class MoyenDePaiementService extends GenericService<MoyenDePaiement, String> {

    private MoyenDePaiementDao moyenDePaiementDao;

    public MoyenDePaiementService() {
        super(new MoyenDePaiementDao());
        this.moyenDePaiementDao = (MoyenDePaiementDao) dao;
    }

    public MoyenDePaiementService(EntityManagerFactory emf) {
        super(new MoyenDePaiementDao(emf));
        this.moyenDePaiementDao = (MoyenDePaiementDao) dao;
    }

    @Override
    public MoyenDePaiement trouver(String code) {
        return dao.trouver(code);
    }

    @Override
    public java.util.List<MoyenDePaiement> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les moyens de paiement dont le code ou le libellé contient le terme de recherche.
     * Si le terme est vide ou null, retourne tous les moyens de paiement.
     * @param searchTerm Le terme à rechercher (non sensible à la casse).
     * @return Une liste de MoyenDePaiement correspondant au critère.
     */
    public List<MoyenDePaiement> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(mdp -> mdp.getCode().toLowerCase().contains(lowerCaseSearchTerm) ||
                        mdp.getLibelle().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }
}