package serviceJpa;

import daoJpa.AbonnementDao;
import daoJpa.PaiementDao;
import entite.Abonnement;
import entite.Membre;
import entite.Paiement;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class AbonnementService extends GenericService<Abonnement, Integer> {

    private AbonnementDao abonnementDao;

    public AbonnementService() {
        super(new AbonnementDao());
        this.abonnementDao = (AbonnementDao) dao;
    }

    public AbonnementService(EntityManagerFactory emf) {
        super(new AbonnementDao(emf));
        this.abonnementDao = (AbonnementDao) dao;
    }

    public void creerAbonnementEtPaiement(Abonnement abonnement, Paiement paiement) throws Exception {
        EntityManager em = abonnementDao.getEntityManager(); // Obtenir l'EntityManager du DAO
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Le membre doit être managé par le contexte de persistance actuel
            Membre membreManage = em.merge(abonnement.getMembre());
            abonnement.setMembre(membreManage);

            em.persist(abonnement);

            // Lier le paiement à l'abonnement qui vient d'être persisté (et a donc un ID)
            paiement.setAbonnement(abonnement);
            em.persist(paiement);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            // Relancer l'exception pour que le contrôleur puisse l'attraper
            throw new Exception("Erreur lors de la création de l'abonnement et du paiement: " + e.getMessage(), e);
        }
    }

    @Override
    public Abonnement trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Abonnement> listerTous() {
        return dao.listerTous();
    }

    public List<Abonnement> findByClientId(Integer clientId) {
        return abonnementDao.findByClientId(clientId);
    }

    public List<Abonnement> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(abonnement -> abonnement.getMembre() != null &&
                        (abonnement.getMembre().getClient().getNom().toLowerCase().contains(lowerCaseSearchTerm) ||
                                abonnement.getMembre().getClient().getPrenom().toLowerCase().contains(lowerCaseSearchTerm)))
                .collect(Collectors.toList());
    }
}