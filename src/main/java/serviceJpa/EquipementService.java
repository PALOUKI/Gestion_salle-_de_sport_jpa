package serviceJpa;

import entite.Equipement;
import daoJpa.EquipementDao;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class EquipementService extends GenericService<Equipement, Integer> {

    private EquipementDao equipementDao;

    public EquipementService(EntityManagerFactory emf) {
        super(new EquipementDao(emf));
        this.equipementDao = (EquipementDao) dao;
    }

    @Override
    public Equipement trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Equipement> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les équipements dont le libellé ou la description contient le terme de recherche.
     * @param searchTerm Le terme à rechercher.
     * @return Une liste d'Equipement correspondant au critère.
     */
    public List<Equipement> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(equipement ->
                        equipement.getLibelle().toLowerCase().contains(lowerCaseSearchTerm) ||
                                (equipement.getDescription() != null && equipement.getDescription().toLowerCase().contains(lowerCaseSearchTerm))
                )
                .collect(Collectors.toList());
    }
}