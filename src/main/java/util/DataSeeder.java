package util;

import entite.MoyenDePaiement;
import entite.TypeAbonnement;
import entite.Salle;
import entite.Equipement;
import entite.Horaire;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DataSeeder {

    public static void seedInitialData(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            seedTypeAbonnement(em);
            seedMoyenDePaiement(em);
            seedSalle(em);
            seedEquipement(em);
            seedHoraire(em);
        } finally {
            em.close();
        }
    }

    private static void seedTypeAbonnement(EntityManager em) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM TypeAbonnement t", Long.class);
        if (query.getSingleResult() == 0) {
            em.getTransaction().begin();

            TypeAbonnement mensuel = new TypeAbonnement();
            mensuel.setCode("MENS");
            mensuel.setLibelle("Mensuel");
            mensuel.setMontant(20000);
            mensuel.setDureeMois(1);
            em.persist(mensuel);

            TypeAbonnement trimestriel = new TypeAbonnement();
            trimestriel.setCode("TRIM");
            trimestriel.setLibelle("Trimestriel");
            trimestriel.setMontant(55000);
            trimestriel.setDureeMois(3);
            em.persist(trimestriel);

            TypeAbonnement annuel = new TypeAbonnement();
            annuel.setCode("ANN");
            annuel.setLibelle("Annuel");
            annuel.setMontant(200000);
            annuel.setDureeMois(12);
            em.persist(annuel);

            em.getTransaction().commit();
            System.out.println("Seeded TypeAbonnement data.");
        }
    }

    private static void seedMoyenDePaiement(EntityManager em) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(m) FROM MoyenDePaiement m", Long.class);
        if (query.getSingleResult() == 0) {
            em.getTransaction().begin();

            MoyenDePaiement cb = new MoyenDePaiement();
            cb.setCode("CB");
            cb.setLibelle("Carte de crédit");
            em.persist(cb);

            MoyenDePaiement paypal = new MoyenDePaiement();
            paypal.setCode("PAYPAL");
            paypal.setLibelle("PayPal");
            em.persist(paypal);

            MoyenDePaiement virement = new MoyenDePaiement();
            virement.setCode("VIR");
            virement.setLibelle("Virement bancaire");
            em.persist(virement);

            em.getTransaction().commit();
            System.out.println("Seeded MoyenDePaiement data.");
        }
    }

    private static void seedSalle(EntityManager em) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(s) FROM Salle s", Long.class);
        if (query.getSingleResult() == 0) {
            em.getTransaction().begin();

            Salle muscu = new Salle();
            muscu.setLibelle("Salle de musculation");
            muscu.setDescription("Espace dédié au renforcement musculaire avec poids libres et machines guidées.");
            em.persist(muscu);

            Salle cardio = new Salle();
            cardio.setLibelle("Salle de cardio");
            cardio.setDescription("Espace équipé de tapis de course, vélos elliptiques, rameurs, etc.");
            em.persist(cardio);

            Salle yoga = new Salle();
            yoga.setLibelle("Studio de yoga");
            yoga.setDescription("Espace calme pour les cours de yoga, pilates et stretching.");
            em.persist(yoga);

            Salle coursCo = new Salle();
            coursCo.setLibelle("Salle de cours collectifs");
            coursCo.setDescription("Espace polyvalent pour les cours de groupe comme le Zumba, Body Pump, etc.");
            em.persist(coursCo);

            em.getTransaction().commit();
            System.out.println("Seeded Salle data.");
        }
    }

    private static void seedEquipement(EntityManager em) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Equipement e", Long.class);
        if (query.getSingleResult() == 0) {
            // On s'assure que les salles existent
            TypedQuery<Salle> salleQuery = em.createQuery("FROM Salle WHERE libelle = :libelle", Salle.class);
            salleQuery.setParameter("libelle", "Salle de musculation");
            Salle salleMuscu = salleQuery.getResultStream().findFirst().orElse(null);

            salleQuery.setParameter("libelle", "Salle de cardio");
            Salle salleCardio = salleQuery.getResultStream().findFirst().orElse(null);

            if (salleMuscu != null && salleCardio != null) {
                em.getTransaction().begin();

                Equipement halteres = new Equipement();
                halteres.setLibelle("Jeu d'haltères 5-50kg");
                halteres.setDescription("Ensemble complet d'haltères pour tous niveaux.");
                halteres.setSalle(salleMuscu);
                em.persist(halteres);

                Equipement tapis = new Equipement();
                tapis.setLibelle("Tapis de course ProForm 500");
                tapis.setDescription("Tapis de course avec inclinaison et programmes intégrés.");
                tapis.setSalle(salleCardio);
                em.persist(tapis);

                Equipement velo = new Equipement();
                velo.setLibelle("Vélo elliptique NordicTrack");
                velo.setDescription("Vélo elliptique pour un entraînement complet du corps.");
                velo.setSalle(salleCardio);
                em.persist(velo);

                em.getTransaction().commit();
                System.out.println("Seeded Equipement data.");
            }
        }
    }

    private static void seedHoraire(EntityManager em) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(h) FROM Horaire h", Long.class);
        if (query.getSingleResult() == 0) {
            em.getTransaction().begin();

            Horaire semaine = new Horaire();
            semaine.setDebut(LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0)));
            semaine.setFin(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0)));
            em.persist(semaine);

            Horaire weekend = new Horaire();
            weekend.setDebut(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
            weekend.setFin(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0)));
            em.persist(weekend);

            em.getTransaction().commit();
            System.out.println("Seeded Horaire data.");
        }
    }
}
