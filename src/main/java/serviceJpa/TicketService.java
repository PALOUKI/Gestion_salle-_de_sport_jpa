package serviceJpa;

import entite.Ticket;
import daoJpa.TicketDao;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class TicketService extends GenericService<Ticket, Integer> {

    private TicketDao ticketDao;

    public TicketService() {
        super(new TicketDao());
        this.ticketDao = (TicketDao) dao;
    }

    public TicketService(EntityManagerFactory emf) {
        super(new TicketDao(emf));
        this.ticketDao = (TicketDao) dao;
    }

    @Override
    public Ticket trouver(Integer id) {
        return dao.trouver(id);
    }

    @Override
    public List<Ticket> listerTous() {
        return dao.listerTous();
    }

    /**
     * Recherche les tickets en fonction du nom ou du prénom du client associé.
     * @param searchTerm Le terme à rechercher.
     * @return Une liste de Ticket correspondant au critère.
     */
    public List<Ticket> rechercher(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listerTous();
        }

        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();

        return listerTous().stream()
                .filter(ticket -> ticket.getClient() != null &&
                        (ticket.getClient().getNom().toLowerCase().contains(lowerCaseSearchTerm) ||
                                ticket.getClient().getPrenom().toLowerCase().contains(lowerCaseSearchTerm)))
                .collect(Collectors.toList());
    }
}