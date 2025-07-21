package entite;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_inscriptions")
public class DemandeInscription extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_de_demande", nullable = false)
    private LocalDateTime dateDeDemande; // Utilisation de LocalDateTime pour DATETIME

    @Column(name = "date_de_traitement")
    private LocalDateTime dateDeTraitement;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(nullable = false)
    private String statut; // Ex: EN_ATTENTE, VALIDEE, REJETEE

    public DemandeInscription(){
        this.statut = "EN_ATTENTE";
        this.dateDeDemande = LocalDateTime.now();
    }

    public DemandeInscription(LocalDateTime dateDeDemande, LocalDateTime dateDeTraitement, String nom, String prenom, String email, String motDePasse, LocalDate dateNaissance) {
        this.dateDeDemande = dateDeDemande;
        this.dateDeTraitement = dateDeTraitement;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.statut = "EN_ATTENTE";
        this.dateDeDemande = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateDeDemande() {
        return dateDeDemande;
    }

    public void setDateDeDemande(LocalDateTime dateDeDemande) {
        this.dateDeDemande = dateDeDemande;
    }

    public LocalDateTime getDateDeTraitement() {
        return dateDeTraitement;
    }

    public void setDateDeTraitement(LocalDateTime dateDeTraitement) {
        this.dateDeTraitement = dateDeTraitement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
