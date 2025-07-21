package daoJpa;

import entite.Equipement;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class EquipementDao extends GenericDao<Equipement, Integer> {

    public EquipementDao(EntityManagerFactory emf) {
        super(Equipement.class, emf);
    }

    @Override
    public Equipement trouver(Integer id) {
        return em.find(Equipement.class, id);
    }

    @Override
    public List<Equipement> listerTous() {
        return em.createQuery("SELECT e FROM Equipement e ORDER BY e.libelle", Equipement.class).getResultList();
    }
}