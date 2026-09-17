package fr.univartois.butinfo.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NavigationController {
    @FXML
    private Button adminButton;
    @FXML
    private Button employeButton;
    @FXML
    private Button clientButton;
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        CompteBanque compte = SessionManager.getCurrentCompte();
        boolean hasAdmin = compte != null && compte.getRole() == CompteBanque.Role.ADMIN;
        boolean hasEmploye = compte != null && (compte.getRole() == CompteBanque.Role.EMPLOYE || compte.getRole() == CompteBanque.Role.ADMIN);
        boolean hasClient = compte != null;

        adminButton.setVisible(hasAdmin);
        employeButton.setVisible(hasEmploye);
        clientButton.setVisible(hasClient);
        logoutButton.setVisible(compte != null);
    }

    @FXML
    private void openAdmin() {
        if (!SessionManager.hasRole(CompteBanque.Role.ADMIN)) {
            return;
        }
        Stage stage = (Stage) adminButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "admin-dashboard-view.fxml");
    }

    @FXML
    private void openEmploye() {
        if (!SessionManager.hasRole(CompteBanque.Role.EMPLOYE)) {
            return;
        }
        Stage stage = (Stage) employeButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "employe-dashboard-view.fxml");
    }

    @FXML
    private void openClient() {
        if (!SessionManager.hasRole(CompteBanque.Role.CLIENT)) {
            return;
        }
        Stage stage = (Stage) clientButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "interface-view.fxml", SessionManager.getCurrentCompte());
    }

    @FXML
    private void logout() {
        SessionManager.clear();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "hello-view.fxml");
    }
}
