package entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membres")
public class Membre extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", unique = true) // Clé étrangère et unique dans la table membres
    private Client client;

    // Relation bidirectionnelle
    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Abonnement> abonnements = new ArrayList<>();

    //private List<Seance> seances;


    public Membre(){

    }

    public Membre(int id, LocalDateTime dateInscription, Client client) {
        this.id = id;
        this.dateInscription = dateInscription;
        this.client = client;
    }
    public Membre( LocalDateTime dateInscription, Client client) {
        this.dateInscription = dateInscription;
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public List<Abonnement> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<Abonnement> abonnements) {
        this.abonnements = abonnements;
    }



    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Membre{" +
                "id=" + id +
                ", dateInscription=" + dateInscription +
                ", client=" + client.getNom() + " " + client.getPrenom() +
                '}';
    }


}
