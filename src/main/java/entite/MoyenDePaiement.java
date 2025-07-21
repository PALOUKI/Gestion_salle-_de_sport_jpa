package entite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moyen_de_paiements")
public class MoyenDePaiement extends GenericEntity {

    @Id
    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "libelle", nullable = false, length = 255)
    private String libelle;

    // Relation bidirectionnelle
    @OneToMany(mappedBy = "moyenDePaiement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();


    public MoyenDePaiement() {}

    public MoyenDePaiement(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return code;
    }

}
