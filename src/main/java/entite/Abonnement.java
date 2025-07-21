package entite;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "abonnements")
public class Abonnement extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id")
    private Membre membre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_abonnement_code")
    private TypeAbonnement typeAbonnement;

    // Relation bidirectionnelle
    @OneToMany(mappedBy = "abonnement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();

    public Abonnement(){

    }

    public Abonnement(int id, LocalDateTime dateDebut, LocalDateTime dateFin, Membre membre, TypeAbonnement typeAbonnement) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.membre = membre;
        this.typeAbonnement = typeAbonnement;
    }

    public Abonnement( LocalDateTime dateDebut, LocalDateTime dateFin, Membre membre, TypeAbonnement typeAbonnement) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.membre = membre;
        this.typeAbonnement = typeAbonnement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public TypeAbonnement getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    @Override
    public String toString() {
        return typeAbonnement.toString();
    }



}

