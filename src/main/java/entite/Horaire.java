package entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horaires")
public class Horaire extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "debut", nullable = false)
    private LocalDateTime debut;

    @Column(name = "fin", nullable = false)
    private LocalDateTime fin;

    // Relation ManyToMany avec Salle via la table de jointure salle_horaires
    // mappedBy indique que la relation est gérée par l'entité Salle
    @ManyToMany(mappedBy = "horaires", fetch = FetchType.LAZY)
    private List<Salle> salles = new ArrayList<>();

    public Horaire(){

    }
    public Horaire(LocalDateTime debut, LocalDateTime fin, List<Salle> salles) {
        this.debut = debut;
        this.fin = fin;
        this.salles = salles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalle(List<Salle> salles) {
        this.salles = salles;
    }

}