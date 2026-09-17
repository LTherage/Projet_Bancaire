package fr.univartois.butinfo.ihm;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminDashboardController {
    @FXML
    private TableView<CompteBanque> clientsTable;
    @FXML
    private TableColumn<CompteBanque, Integer> idColumn;
    @FXML
    private TableColumn<CompteBanque, String> nomColumn;
    @FXML
    private TableColumn<CompteBanque, CompteBanque.Role> roleColumn;
    @FXML
    private TableColumn<CompteBanque, Double> soldeColumn;

    @FXML
    private void initialize() {
        if (!SessionManager.hasRole(CompteBanque.Role.ADMIN)) {
            Stage stage = (Stage) clientsTable.getScene().getWindow();
            ApplicationBanque.changerVue(stage, "hello-view.fxml");
            return;
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("identifiantCompte"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomProprietaire"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        clientsTable.setItems(FXCollections.observableArrayList(CompteBanque.getComptes()));
    }

    @FXML
    private void ajouterCompteTest() {
        if (!SessionManager.hasRole(CompteBanque.Role.ADMIN)) {
            return;
        }
        CompteBanque nouveau = new CompteBanque("Client test", 500, 999, 999999, 1234, 0, 0);
        nouveau.setRole(CompteBanque.Role.CLIENT);
        clientsTable.getItems().add(nouveau);
    }

    @FXML
    private void retourMenu() {
        Stage stage = (Stage) clientsTable.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "navigation-view.fxml");
    }
}
