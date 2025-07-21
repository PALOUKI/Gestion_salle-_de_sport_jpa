package daoJpa;

import entite.Horaire;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class HoraireDao extends GenericDao<Horaire, Integer> {

    public HoraireDao(EntityManagerFactory emf) {
        super(Horaire.class, emf);
    }

    public HoraireDao() {
        super(Horaire.class);
    }

    @Override
    public Horaire trouver(Integer id) {
        return em.find(Horaire.class, id);
    }

    @Override
    public List<Horaire> listerTous() {
        // Trie les horaires par heure de début
        return em.createQuery("SELECT h FROM Horaire h ORDER BY h.debut", Horaire.class).getResultList();
    }
}