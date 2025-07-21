package serviceJpa;

import daoJpa.DemandeInscriptionDao;
import daoJpa.ClientDao;
import daoJpa.MembreDao;
import daoJpa.TypeAbonnementDao;
import daoJpa.AbonnementDao;
import entite.Abonnement;
import entite.Client;
import entite.DemandeInscription;
import entite.Membre;
import entite.TypeAbonnement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DemandeInscriptionService extends GenericService<DemandeInscription, Integer> {

    private DemandeInscriptionDao demandeInscriptionDao;
    private ClientDao clientDao;
    private MembreDao membreDao;
    private TypeAbonnementDao typeAbonnementDao;
    private AbonnementDao abonnementDao;

    public DemandeInscriptionService() {
        super(new DemandeInscriptionDao());
        this.demandeInscriptionDao = (DemandeInscriptionDao) dao;
        this.clientDao = new ClientDao();
        this.membreDao = new MembreDao();
        this.typeAbonnementDao = new TypeAbonnementDao();
        this.abonnementDao = new AbonnementDao();
    }

    public DemandeInscriptionService(EntityManagerFactory emf) {
        super(new DemandeInscriptionDao(emf));
        this.demandeInscriptionDao = (DemandeInscriptionDao) dao;
        this.clientDao = new ClientDao(emf);
        this.membreDao = new MembreDao(emf);
        this.typeAbonnementDao = new TypeAbonnementDao(emf);
        this.abonnementDao = new AbonnementDao(emf);
    }

    @Override
    public DemandeInscription trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public java.util.List<DemandeInscription> listerTous() {
        return dao.listerTous();
    }

    public List<DemandeInscription> listerRecents(int maxResults) {
        return demandeInscriptionDao.listerRecents(maxResults);
    }

    /**
     * Recherche les demandes d'inscription par email.
     * Si le terme de recherche est vide, retourne toutes les demandes.
     * @param email L'email à rechercher.
     * @return Une liste de DemandeInscription correspondant à l'email.
     */
    public List<DemandeInscription> rechercherParEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return listerTous();
        }
        return demandeInscriptionDao.findByEmail(email);
    }

    public int getUnprocessedRequestCount() {
        EntityManager em = demandeInscriptionDao.getEntityManager();

            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM DemandeInscription r WHERE r.statut = :statut", Long.class
            );
            query.setParameter("statut", "EN_ATTENTE");
            return query.getSingleResult().intValue();

    }
}