package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InternationalStudentModuleController {

    @FXML private Label studentNameLabel;
    @FXML private VBox promoContainer;

    private ModuleService moduleService;
    private List<Language> selectedLanguages;

    // 🔥 appelée depuis le controller précédent
    public void setContext(InternationalStudentEntity student,
                           List<UFREntity> ufrs,
                           List<Language> languages) {

        this.selectedLanguages = languages;
        this.moduleService = ServiceRegistry.getModuleService();

        // Nom étudiant
        studentNameLabel.setText(
                student.getFirstName() + " " + student.getLastName().toUpperCase()
        );

        loadModules();
    }

    // 🔥 charge + filtre
    private void loadModules() {

        List<ModuleEntity> allModules = moduleService.findAll();

        // filtre par langues sélectionnées
        List<ModuleEntity> filtered = allModules.stream()
                .filter(m -> selectedLanguages.contains(m.getLanguage()))
                .collect(Collectors.toList());

        displayModulesByPromo(filtered);
    }

    // 🔥 affichage groupé par promo
    private void displayModulesByPromo(List<ModuleEntity> modules) {

        promoContainer.getChildren().clear();

        // groupement par promoName (nouveau champ)
        Map<String, List<ModuleEntity>> grouped = modules.stream()
                .collect(Collectors.groupingBy(ModuleEntity::getPromoName));

        for (String promo : grouped.keySet()) {

            // titre promo
            Label promoLabel = new Label("🎓 " + promo);
            promoLabel.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:10 0 5 0;");
            promoContainer.getChildren().add(promoLabel);

            // modules de la promo
            for (ModuleEntity m : grouped.get(promo)) {

                Label moduleLabel = new Label(
                        "   - " + m.getName()
                                + " | " + m.getECTS() + " ECTS"
                                + " | " + m.getLanguage()
                );

                moduleLabel.setStyle("-fx-font-size:14px; -fx-padding:2 0 2 20;");
                promoContainer.getChildren().add(moduleLabel);
            }
        }
    }
}