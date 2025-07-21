package daoJpa;

import entite.Seance;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SeanceDao extends GenericDao<Seance, Integer> {

    public SeanceDao(EntityManagerFactory emf) {
        super(Seance.class, emf);
    }

    @Override
    public Seance trouver(Integer id) {
        return em.find(Seance.class, id);
    }

    @Override
    public List<Seance> listerTous() {
        // Trie les séances par date de début, de la plus récente à la plus ancienne
        return em.createQuery("SELECT s FROM Seance s ORDER BY s.dateDebut DESC", Seance.class).getResultList();
    }
}