package fr.univtln.projet.planning;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.person.Student;
import fr.univtln.projet.planning.modele.planning.Course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("⏳ Démarrage d'Hibernate et connexion à MySQL...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        try {
            // suppression des tables déjà existantes
            em.getTransaction().begin();

            // 1. Supprimer la table de jointure ManyToMany
            // em.createQuery("DELETE FROM Group_Course").executeUpdate(); // si existante

            // 2. Supprimer les cours
            em.createQuery("DELETE FROM Course").executeUpdate();

            // 3. Supprimer les groupes
            em.createQuery("DELETE FROM Group").executeUpdate();

            // 4. Supprimer les promos
            em.createQuery("DELETE FROM Promo").executeUpdate();

            // 5. Supprimer les modules
            em.createQuery("DELETE FROM Module").executeUpdate();

            // 6. Supprimer les salles
            em.createQuery("DELETE FROM Room").executeUpdate();

            // 7. Supprimer les bâtiments
            em.createQuery("DELETE FROM Building").executeUpdate();

            // 8. Supprimer les UFR
            em.createQuery("DELETE FROM UFR").executeUpdate();

            // 9. Supprimer les utilisateurs
            em.createQuery("DELETE FROM Student").executeUpdate();
            em.createQuery("DELETE FROM Professor").executeUpdate();

            em.getTransaction().commit();

            em.getTransaction().begin();

            // ==========================================
            // 1. INFRASTRUCTURES
            // ==========================================
            UFR ufr = new UFR();
            ufr.setName("UFR Sciences et Techniques");
            ufr.setCampus("Campus La Garde");
            em.persist(ufr);

            Building batimentK = new Building();
            batimentK.setName("Bâtiment K (Amphis)");
            em.persist(batimentK);

            Building batimentU = new Building();
            batimentU.setName("Bâtiment U (Informatique)");
            em.persist(batimentU);

            Room amphiK1 = new Room();
            amphiK1.setNumber("Amphi K1");
            amphiK1.setCapacity(200);
            amphiK1.setType("AMPHI");
            amphiK1.setBuilding(batimentK);
            em.persist(amphiK1);

            Room salleTP = new Room();
            salleTP.setNumber("U014");
            salleTP.setCapacity(30);
            salleTP.setType("TP");
            salleTP.setBuilding(batimentU);
            em.persist(salleTP);

            // ==========================================
            // 2. UTILISATEURS (Profs et Étudiants)
            // ==========================================
            Professor profJava = new Professor();
            profJava.setFirstName("Alan");
            profJava.setLastName("Turing");
            profJava.setEmailUniv("alan.turing@univ-tln.fr");
            em.persist(profJava);

            Professor profBdd = new Professor();
            profBdd.setFirstName("E.F.");
            profBdd.setLastName("Codd");
            profBdd.setEmailUniv("edgar.codd@univ-tln.fr");
            em.persist(profBdd);

            Student etudiant1 = new Student();
            etudiant1.setFirstName("Alice");
            etudiant1.setLastName("Liddell");
            etudiant1.setEmailUniv("alice.liddell@etu.univ-tln.fr");
            etudiant1.setEmailPersonal("alice@gmail.com");
            em.persist(etudiant1);

            Student etudiant2 = new Student();
            etudiant2.setFirstName("Bob");
            etudiant2.setLastName("L'éponge");
            etudiant2.setEmailUniv("bob.eponge@etu.univ-tln.fr");
            etudiant2.setEmailPersonal("bob@gmail.com");
            em.persist(etudiant2);

            // ==========================================
            // 3. STRUCTURE ACADÉMIQUE (Modules, Promo et Groupes)
            // ==========================================
            Module modJava = new Module();
            modJava.setCode("M-JAVA-01");
            modJava.setName("Programmation Orientée Objet");
            modJava.setLanguage("Java");
            modJava.setEcts(6);
            modJava.setResponsible(profJava);
            em.persist(modJava);

            Promo promoL3 = new Promo();
            promoL3.setName("Licence 3 Informatique");
            promoL3.setYear(2026);
            promoL3.setUfr(ufr);
            em.persist(promoL3);

            // Création des 3 groupes de la promotion
            Group groupePromoComplete = new Group();
            groupePromoComplete.setType("CM"); // Groupe contenant toute la promo
            groupePromoComplete.setPromo(promoL3);
            em.persist(groupePromoComplete);

            Group groupeTP1 = new Group();
            groupeTP1.setType("TP1"); // Sous-groupe 1
            groupeTP1.setPromo(promoL3);
            em.persist(groupeTP1);

            Group groupeTP2 = new Group();
            groupeTP2.setType("TP2"); // Sous-groupe 2
            groupeTP2.setPromo(promoL3);
            em.persist(groupeTP2);

            // ==========================================
            // 4. LE PLANNING (Création des cours)
            // ==========================================
            
            // Événement 1 : Cours Magistral pour TOUTE la promo dans l'Amphi
            Course coursMagistral = new Course();
            coursMagistral.setDate(LocalDate.of(2026, 9, 14));
            coursMagistral.setStartTime(LocalTime.of(8, 0));
            coursMagistral.setDuration(120); // 2 heures
            coursMagistral.setCourseType("CM");
            coursMagistral.setRoom(amphiK1);
            coursMagistral.setModule(modJava);
            coursMagistral.getGroups().add(groupePromoComplete); // Assigné au groupe Global
            em.persist(coursMagistral);

            // Événement 2 : TP de Java uniquement pour le TP1 dans la petite salle
            Course coursTP = new Course();
            coursTP.setDate(LocalDate.of(2026, 9, 14));
            coursTP.setStartTime(LocalTime.of(10, 30));
            coursTP.setDuration(180); // 3 heures
            coursTP.setCourseType("TP");
            coursTP.setRoom(salleTP);
            coursTP.setModule(modJava);
            coursTP.getGroups().add(groupeTP1); // Assigné UNIQUEMENT au groupe TP1
            em.persist(coursTP);

            // Validation de la transaction
            em.getTransaction().commit();
            
            System.out.println("\n✅ SUCCÈS : La base de données a été générée et peuplée !");
            System.out.println("🎓 1. " + coursMagistral.getCourseType() + " de " + modJava.getName() + " généré pour la " + groupePromoComplete.getPromo().getName() + " en " + amphiK1.getNumber());
            System.out.println("💻 2. " + coursTP.getCourseType() + " de " + modJava.getName() + " généré uniquement pour le groupe " + groupeTP1.getType() + " en salle " + salleTP.getNumber());

        } catch (Exception e) {
            System.err.println("❌ ERREUR : Un problème est survenu.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}