package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RoomsController {

    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ListView<String> roomsListView;
    @FXML private Label resultLabel;

    private RoomService roomService;

    @FXML
    public void initialize() {
        roomService = ServiceRegistry.getRoomService();

        datePicker.setValue(LocalDate.now());
        startTimeField.setText("08:00");
        endTimeField.setText("10:00");
        resultLabel.setText("Choisissez une date et un créneau, puis lancez la recherche.");
    }

    @FXML
    private void handleSearchRooms() {
        try {
            LocalDate date = datePicker.getValue();
            LocalTime start = LocalTime.parse(startTimeField.getText().trim());
            LocalTime end = LocalTime.parse(endTimeField.getText().trim());

            List<RoomEntity> freeRooms = roomService.findAvailableRooms(date, start, end);

            List<String> displayRooms = freeRooms.stream()
                    .map(this::formatRoom)
                    .sorted()
                    .toList();

            roomsListView.setItems(FXCollections.observableArrayList(displayRooms));

            if (displayRooms.isEmpty()) {
                resultLabel.setText("Aucune salle libre pour ce créneau.");
            } else {
                resultLabel.setText(displayRooms.size() + " salle(s) libre(s) trouvée(s).");
            }

        } catch (DateTimeParseException e) {
            resultLabel.setText("Format d'heure invalide");
            roomsListView.getItems().clear();
        } catch (IllegalArgumentException e) {
            resultLabel.setText(e.getMessage());
            roomsListView.getItems().clear();
        } catch (Exception e) {
            resultLabel.setText("Erreur lors de la recherche des salles.");
            roomsListView.getItems().clear();
            e.printStackTrace();
        }
    }

    private String formatRoom(RoomEntity room) {
        String buildingName = room.getBuilding() != null ? room.getBuilding().getName() : "Bâtiment inconnu";
        String roomNumber = room.getNumber() != null ? room.getNumber() : "Salle inconnue";
        return buildingName + " - " + roomNumber;
    }


}
