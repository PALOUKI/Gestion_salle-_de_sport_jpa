package entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paiements")
public class Paiement extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_de_paiement", nullable = false)
    private LocalDateTime dateDePaiement;

    @Column(name = "montant", nullable = false)
    private Integer montant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moyen_de_paiement_code")
    private MoyenDePaiement moyenDePaiement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abonnement_id")
    private Abonnement abonnement;

    public Paiement() {

    }

    public Paiement(int id, LocalDateTime dateDePaiement, int montant, MoyenDePaiement moyenDePaiement, Abonnement abonnement) {
        this.id = id;
        this.dateDePaiement = dateDePaiement;
        this.montant = montant;
        this.moyenDePaiement = moyenDePaiement;
        this.abonnement = abonnement;
    }

    public Paiement(LocalDateTime dateDePaiement, int montant, MoyenDePaiement moyenDePaiement, Abonnement abonnement) {
        this.dateDePaiement = dateDePaiement;
        this.montant = montant;
        this.moyenDePaiement = moyenDePaiement;
        this.abonnement = abonnement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateDePaiement() {
        return dateDePaiement;
    }

    public void setDateDePaiement(LocalDateTime dateDePaiement) {
        this.dateDePaiement = dateDePaiement;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public MoyenDePaiement getMoyenDePaiement() {
        return moyenDePaiement;
    }

    public void setMoyenDePaiement(MoyenDePaiement moyenDePaiement) {
        this.moyenDePaiement = moyenDePaiement;
    }

    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }
}
