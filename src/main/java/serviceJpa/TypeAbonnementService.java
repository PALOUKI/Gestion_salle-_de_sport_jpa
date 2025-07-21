package serviceJpa;

import entite.TypeAbonnement;
import daoJpa.TypeAbonnementDao;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors; // Import pour utiliser Stream API

public class TypeAbonnementService extends GenericService<TypeAbonnement, String> {

    // On garde une référence spécifique au DAO pour des méthodes spécifiques si besoin,
    // bien que pour 'rechercher' nous allons la gérer en mémoire ici pour simplifier.
    // Idéalement, la recherche avancée serait gérée au niveau du DAO avec des requêtes JPQL/SQL.
    private TypeAbonnementDao typeAbonnementDao;

    public TypeAbonnementService() {
        super(new TypeAbonnementDao());
        this.typeAbonnementDao = (TypeAbonnementDao) dao; // Cast pour l'accès aux méthodes spécifiques
    }

    public TypeAbonnementService(EntityManagerFactory emf) {
        super(new TypeAbonnementDao(emf));
        this.typeAbonnementDao = (TypeAbonnementDao) dao;
    }

    @Override
    public TypeAbonnement trouver(String code) {
        return dao.trouver(code);
    }

    @Override
    public java.util.List<TypeAbonnement> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les types d'abonnements dont le code ou le libellé contient le terme de recherche.
     * Si le terme est vide ou null, retourne tous les types d'abonnements.
     * @param searchTerm Le terme à rechercher (non sensible à la casse).
     * @return Une liste de TypeAbonnement correspondant au critère.
     */
    public List<TypeAbonnement> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous(); // Si le terme est vide, on retourne tout
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        // On récupère tous les abonnements et on les filtre en mémoire.
        // Pour de très grandes bases de données, cette logique devrait être dans le DAO (requête SQL/JPQL).
        return listerTous().stream()
                .filter(ta -> ta.getCode().toLowerCase().contains(lowerCaseSearchTerm) ||
                        ta.getLibelle().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }
}