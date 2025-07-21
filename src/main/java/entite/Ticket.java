package entite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_de_seance", nullable = false)
    private Integer nombreDeSeance;

    @Column(name = "montant", nullable = false)
    private Integer montant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Ticket(){

    }

    public Ticket(int nombreDeSeance, int montant, Client client){
        this.nombreDeSeance = nombreDeSeance;
        this.montant = montant;
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNombreDeSeance() {
        return nombreDeSeance;
    }

    public void setNombreDeSeance(int nombreDeSeance) {
        this.nombreDeSeance = nombreDeSeance;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}

