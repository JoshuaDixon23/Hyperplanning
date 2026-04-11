package fr.univtln.projet.planning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InternationalStudentModuleController {

    @FXML private Label studentNameLabel;
    @FXML private Label moduleCountLabel;
    @FXML private Label ectsCountLabel;
    @FXML private VBox promoContainer;
    @FXML private Button validateButton;

    private ModuleService moduleService;
    private PromoService promoService;
    private List<Language> selectedLanguages;
    private InternationalStudentEntity currentStudent;

    private final List<ModuleEntity> selectedModules = new ArrayList<>();
    private final Map<ModuleEntity, CheckBox> moduleCheckBoxMap = new HashMap<>();

    public void setContext(InternationalStudentEntity student,
                           List<UFREntity> ufrs,
                           List<Language> languages) {
        this.selectedLanguages = languages;
        this.currentStudent = student;
        this.moduleService = ServiceRegistry.getModuleService();
        this.promoService = ServiceRegistry.getPromoService();

        studentNameLabel.setText(
                student.getFirstName() + " " + student.getLastName().toUpperCase()
        );

        updateCounters();
        loadModules();
    }

    private void loadModules() {
        List<ModuleEntity> allModules = moduleService.findAll();

        List<ModuleEntity> filtered = allModules.stream()
                .filter(m -> selectedLanguages.contains(m.getLanguage()))
                .collect(Collectors.toList());

        Map<String, List<ModuleEntity>> grouped = new LinkedHashMap<>();
        for (ModuleEntity m : filtered) {
            String promoName = resolvePromoName(m);
            grouped.computeIfAbsent(promoName, k -> new ArrayList<>()).add(m);
        }

        displayModulesByPromo(grouped);
    }

    private String resolvePromoName(ModuleEntity module) {
        List<PromoEntity> promos = promoService.findByModuleCode(module.getCode());

        if (promos == null || promos.isEmpty()) return "Sans promo";

        return promos.stream()
                .map(PromoEntity::getName)
                .filter(Objects::nonNull)
                .sorted()
                .findFirst()
                .orElse("Sans promo");
    }

    private void displayModulesByPromo(Map<String, List<ModuleEntity>> grouped) {
        promoContainer.getChildren().clear();
        moduleCheckBoxMap.clear();

        List<String> sortedPromos = new ArrayList<>(grouped.keySet());
        Collections.sort(sortedPromos);

        for (String promo : sortedPromos) {

            // --- Carte promo ---
            VBox promoCard = new VBox(0);
            promoCard.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #e2e8f0;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
            );

            // En-tête promo
            HBox promoHeader = new HBox(10);
            promoHeader.setAlignment(Pos.CENTER_LEFT);
            promoHeader.setPadding(new Insets(14, 18, 14, 18));
            promoHeader.setStyle(
                    "-fx-background-color: #1e293b;" +
                            "-fx-background-radius: 12 12 0 0;"
            );

            Label promoIcon = new Label("🎓");
            promoIcon.setStyle("-fx-font-size: 16px;");

            Label promoLabel = new Label(promo);
            promoLabel.setStyle(
                    "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;"
            );

            Region spacerHeader = new Region();
            HBox.setHgrow(spacerHeader, Priority.ALWAYS);

            List<ModuleEntity> promoModules = grouped.get(promo);
            Label countBadge = new Label(promoModules.size() + " module" + (promoModules.size() > 1 ? "s" : ""));
            countBadge.setStyle(
                    "-fx-background-color: #334155;" +
                            "-fx-text-fill: #94a3b8;" +
                            "-fx-font-size: 11px;" +
                            "-fx-padding: 3 8;" +
                            "-fx-background-radius: 20;"
            );

            promoHeader.getChildren().addAll(promoIcon, promoLabel, spacerHeader, countBadge);

            // Corps : liste des modules
            VBox modulesList = new VBox(0);
            modulesList.setPadding(new Insets(8, 0, 8, 0));

            List<ModuleEntity> sortedModules = promoModules.stream()
                    .sorted(Comparator.comparing(ModuleEntity::getName))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedModules.size(); i++) {
                ModuleEntity m = sortedModules.get(i);
                final String rowBg = i % 2 == 0 ? "#f8fafc" : "white";

                HBox moduleRow = new HBox(12);
                moduleRow.setAlignment(Pos.CENTER_LEFT);
                moduleRow.setPadding(new Insets(10, 18, 10, 18));

                if (i % 2 == 0) {
                    moduleRow.setStyle("-fx-background-color: #f8fafc;");
                } else {
                    moduleRow.setStyle("-fx-background-color: white;");
                }

                // Hover effect
                moduleRow.setOnMouseEntered(e ->
                        moduleRow.setStyle("-fx-background-color: #eff6ff; -fx-cursor: hand;"));
                moduleRow.setOnMouseExited(e ->
                        moduleRow.setStyle("-fx-background-color: " + rowBg + ";"));

                CheckBox cb = new CheckBox();
                cb.setStyle("-fx-cursor: hand;");

                Label nameLabel = new Label(m.getName());
                nameLabel.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: 500;" +
                                "-fx-text-fill: #1e293b;"
                );
                HBox.setHgrow(nameLabel, Priority.ALWAYS);
                nameLabel.setMaxWidth(Double.MAX_VALUE);

                // Badge langue
                Label langBadge = new Label(m.getLanguage() != null ? m.getLanguage().name() : "");
                langBadge.setStyle(
                        "-fx-background-color: #dbeafe;" +
                                "-fx-text-fill: #1d4ed8;" +
                                "-fx-font-size: 10px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 2 7;" +
                                "-fx-background-radius: 20;"
                );

                // Badge ECTS
                Label ectsBadge = new Label(m.getECTS() + " ECTS");
                ectsBadge.setStyle(
                        "-fx-background-color: #dcfce7;" +
                                "-fx-text-fill: #15803d;" +
                                "-fx-font-size: 10px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 2 7;" +
                                "-fx-background-radius: 20;"
                );

                // Clic sur la ligne entière = toggle checkbox
                moduleRow.setOnMouseClicked(e -> cb.setSelected(!cb.isSelected()));
                cb.setOnAction(e -> {
                    if (cb.isSelected()) {
                        selectedModules.add(m);
                    } else {
                        selectedModules.remove(m);
                    }
                    updateCounters();
                });

                moduleCheckBoxMap.put(m, cb);
                moduleRow.getChildren().addAll(cb, nameLabel, langBadge, ectsBadge);
                modulesList.getChildren().add(moduleRow);
            }

            promoCard.getChildren().addAll(promoHeader, modulesList);
            promoContainer.getChildren().add(promoCard);
        }
    }

    private void updateCounters() {
        List<ModuleEntity> checked = moduleCheckBoxMap.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        selectedModules.clear();
        selectedModules.addAll(checked);

        int count = selectedModules.size();
        float ects = (float) selectedModules.stream()
                .mapToDouble(ModuleEntity::getECTS)
                .sum();

        if (moduleCountLabel != null) moduleCountLabel.setText(String.valueOf(count));
        if (ectsCountLabel != null) ectsCountLabel.setText(
                String.format("%.1f", ects)
        );
        if (validateButton != null) validateButton.setDisable(count == 0);
    }

    @FXML
    private void handleValidate() {
        if (selectedModules.isEmpty()) return;
        
        try {
            // 1. Récupération ou création du panier de l'étudiant
            fr.univtln.projet.planning.entity.international.BasketFinalEntity basket = currentStudent.getBasketFinal();
            
            // CORRECTION : Si le panier n'existe pas, on le crée !
            if (basket == null) {
                basket = new fr.univtln.projet.planning.entity.international.BasketFinalEntity();
                currentStudent.setBasketFinal(basket);
            } else {
                // On vide le panier au cas où l'agent serait revenu en arrière pour modifier ses choix
                basket.getModuleGroup().clear();
            }
            
            // 2. Remplissage avec les modules sélectionnés (les Groupes sont null pour l'instant)
            for (ModuleEntity module : selectedModules) {
                basket.addModuleGroup(module, null);
            }

            // 3. Chargement de la vue DRI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dri-view.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);

            scene.getStylesheets().addAll(
                getClass().getResource("/css/base.css").toExternalForm(),
                getClass().getResource("/css/sidebar.css").toExternalForm(),
                getClass().getResource("/css/components.css").toExternalForm(),
                getClass().getResource("/css/planning.css").toExternalForm(),
                getClass().getResource("/css/connexion.css").toExternalForm()
            );

            // 4. Transmission des données au contrôleur
            DRIController driController = loader.getController();
            
            driController.setCourseService(ServiceRegistry.getCourseService());
            driController.setBasketFinalService(ServiceRegistry.getBasketFinalService());
            driController.setInternationalStudentService(ServiceRegistry.getInternationalStudentService());
            driController.setGroupService(ServiceRegistry.getGroupService());
            driController.setModuleService(ServiceRegistry.getModuleService());
            
            // On passe l'entité avec son panier sécurisé !
            driController.loadStudentDRI(currentStudent);

            // 5. Changement de scène
            Stage stage = (Stage) validateButton.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible d'ouvrir le planning");
            alert.setContentText("Une erreur est survenue lors du chargement de la vue DRI : " + e.getMessage());
            alert.showAndWait();
        }
    }
}