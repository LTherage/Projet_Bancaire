package fr.univartois.butinfo.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfileController {
    @FXML
    private Label profileTitle;
    @FXML
    private Label profileName;
    @FXML
    private Label profileRole;
    @FXML
    private Label profileId;
    @FXML
    private Label profileBalance;
    @FXML
    private Button goDashboardButton;
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        CompteBanque compte = SessionManager.getCurrentCompte();
        if (compte == null) {
            return;
        }

        profileTitle.setText("Profil utilisateur");
        profileName.setText("Nom : " + compte.getNomProprietaire());
        profileRole.setText("Rôle : " + compte.getRole());
        profileId.setText("Identifiant : " + compte.getIdentifiantCompte());

        try {
            profileBalance.setText("Solde : " + compte.getSolde(compte) + " €");
        } catch (CompteException e) {
            profileBalance.setText("Solde : indisponible");
        }
    }

    @FXML
    private void goDashboard() {
        Stage stage = (Stage) goDashboardButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "dashboard-view.fxml");
    }

    @FXML
    private void logout() {
        SessionManager.clear();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "hello-view.fxml");
    }
}
