package daoJpa;

import entite.MoyenDePaiement;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class MoyenDePaiementDao extends GenericDao<MoyenDePaiement, String> {

    public MoyenDePaiementDao() {
        super(MoyenDePaiement.class);
    }

    public MoyenDePaiementDao(EntityManagerFactory emf) {
        super(MoyenDePaiement.class, emf);
    }

    @Override
    public MoyenDePaiement trouver(String code) {
        return em.find(MoyenDePaiement.class, code);
    }

    @Override
    public List<MoyenDePaiement> listerTous() {
        // Ajout d'un tri par libellé pour un affichage ordonné
        return em.createQuery("SELECT mdp FROM MoyenDePaiement mdp ORDER BY mdp.libelle", MoyenDePaiement.class).getResultList();
    }
}