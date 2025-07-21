package serviceJpa;

import entite.Horaire;
import daoJpa.HoraireDao;
import javax.persistence.EntityManagerFactory;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HoraireService extends GenericService<Horaire, Integer> {

    private HoraireDao horaireDao;
    // Formatter utilisé pour la recherche
    private static final DateTimeFormatter DATE_FORMATTER_FOR_SEARCH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HoraireService(EntityManagerFactory emf) {
        super(new HoraireDao(emf));
        this.horaireDao = (HoraireDao) dao;
    }

    @Override
    public Horaire trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Horaire> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les horaires dont la date de début ou de fin (formatée) contient le terme de recherche.
     * @param searchTerm Le terme à rechercher (ex: "25/06/2024" ou "18:00").
     * @return Une liste d'Horaire correspondant au critère.
     */
    public List<Horaire> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(horaire ->
                        (horaire.getDebut() != null && horaire.getDebut().format(DATE_FORMATTER_FOR_SEARCH).contains(lowerCaseSearchTerm)) ||
                                (horaire.getFin() != null && horaire.getFin().format(DATE_FORMATTER_FOR_SEARCH).contains(lowerCaseSearchTerm))
                )
                .collect(Collectors.toList());
    }
}