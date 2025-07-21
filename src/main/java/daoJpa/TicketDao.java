package daoJpa;

import entite.Ticket;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TicketDao extends GenericDao<Ticket, Integer> {

    public TicketDao(EntityManagerFactory emf) {
        super(Ticket.class, emf);
    }

    public TicketDao() {
        super(Ticket.class);
    }

    @Override
    public Ticket trouver(Integer id) {
        return em.find(Ticket.class, id);
    }

    @Override
    public List<Ticket> listerTous() {
        // Trie les tickets par nom de client, puis par ID descendant (plus récent en premier)
        return em.createQuery("SELECT t FROM Ticket t ORDER BY t.client.nom, t.client.prenom, t.id DESC", Ticket.class).getResultList();
    }
}