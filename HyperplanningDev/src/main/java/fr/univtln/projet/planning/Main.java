package fr.univtln.projet.planning;

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
import java.util.List;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("⏳ Démarrage d'Hibernate et connexion à la base de données...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        try {
            // ==========================================
            // PARTIE 1 : VÉRIFICATION ET PEUPLEMENT
            // ==========================================
            Long existingUsers = em.createQuery("select count(u) from User u", Long.class).getSingleResult();
            if (existingUsers != null && existingUsers > 0) {
                System.out.println("ℹ️ La base contient déjà des données. Le peuplement initial est sauté.");
            } else {
                System.out.println("⚠️ Base vide, insertion des données de test...");
                em.getTransaction().begin();

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

                Group groupePromoComplete = new Group();
                groupePromoComplete.setType("CM"); 
                groupePromoComplete.setPromo(promoL3);
                em.persist(groupePromoComplete);

                Group groupeTP1 = new Group();
                groupeTP1.setType("TP1"); 
                groupeTP1.setPromo(promoL3);
                em.persist(groupeTP1);

                Group groupeTP2 = new Group();
                groupeTP2.setType("TP2"); 
                groupeTP2.setPromo(promoL3);
                em.persist(groupeTP2);

                Course coursMagistral = new Course();
                coursMagistral.setDate(LocalDate.of(2026, 9, 14));
                coursMagistral.setStartTime(LocalTime.of(8, 0));
                coursMagistral.setDuration(120); 
                coursMagistral.setCourseType("CM");
                coursMagistral.setRoom(amphiK1);
                coursMagistral.setModule(modJava);
                coursMagistral.getGroups().add(groupePromoComplete); 
                em.persist(coursMagistral);

                Course coursTP = new Course();
                coursTP.setDate(LocalDate.of(2026, 9, 14));
                coursTP.setStartTime(LocalTime.of(10, 30));
                coursTP.setDuration(180); 
                coursTP.setCourseType("TP");
                coursTP.setRoom(salleTP);
                coursTP.setModule(modJava);
                coursTP.getGroups().add(groupeTP1); 
                em.persist(coursTP);

                em.getTransaction().commit();
                System.out.println("✅ Données insérées avec succès !");
            }

            // ==========================================
            // PARTIE 2 : LECTURE ET AFFICHAGE DES DONNÉES
            // ==========================================
            System.out.println("\n=====================================================");
            System.out.println("📊 INTERROGATION DE LA BASE DE DONNÉES NEON FETCH");
            System.out.println("=====================================================");

            // 1. Récupération du Planning (Les Cours)
            List<Course> courses = em.createQuery(
                "SELECT c FROM Course c ORDER BY c.date ASC, c.startTime ASC", Course.class)
                .getResultList();

            System.out.println("\n📅 PLANNING DES COURS :");
            if (courses.isEmpty()) {
                System.out.println("Aucun cours trouvé dans la base.");
            } else {
                for (Course c : courses) {
                    System.out.println("▶ " + c.getDate() + " à " + c.getStartTime() + " (" + c.getDuration() + " min)");
                    System.out.println("   Type   : " + c.getCourseType());
                    System.out.println("   Module : " + c.getModule().getName() + " (Géré par " + c.getModule().getResponsible().getLastName() + ")");
                    System.out.println("   Salle  : " + c.getRoom().getNumber() + " (Capacité: " + c.getRoom().getCapacity() + ")");
                    System.out.println("   -------------------------------------------------");
                }
            }

            // 2. Récupération des Professeurs
            List<Professor> profs = em.createQuery("SELECT p FROM Professor p", Professor.class).getResultList();
            System.out.println("\n👨‍🏫 LISTE DES PROFESSEURS :");
            for (Professor p : profs) {
                System.out.println("- " + p.getFirstName() + " " + p.getLastName() + " (" + p.getEmailUniv() + ")");
            }

            // 3. Récupération des Étudiants
            List<Student> students = em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
            System.out.println("\n🎓 LISTE DES ÉTUDIANTS :");
            for (Student s : students) {
                System.out.println("- " + s.getFirstName() + " " + s.getLastName() + " (" + s.getEmailUniv() + ")");
            }

            System.out.println("\n✅ Test de lecture terminé avec succès !");

        } catch (Exception e) {
            System.err.println("❌ ERREUR : Un problème est survenu lors de l'exécution.");
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