package fr.univartois.butinfo.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label summaryLabel1;
    @FXML
    private Label summaryLabel2;
    @FXML
    private Label summaryLabel3;
    @FXML
    private Label summaryLabel4;
    @FXML
    private Button profileButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        CompteBanque compte = SessionManager.getCurrentCompte();
        if (compte == null) {
            return;
        }

        try {
            if (compte.getRole() == CompteBanque.Role.ADMIN) {
                titleLabel.setText("Dashboard Admin");
                summaryLabel1.setText("Comptes total : " + CompteBanque.getComptes().size());
                summaryLabel2.setText("Clients : " + CompteBanque.getComptes().stream().filter(c -> c.getRole() == CompteBanque.Role.CLIENT).count());
                summaryLabel3.setText("Employés : " + CompteBanque.getComptes().stream().filter(c -> c.getRole() == CompteBanque.Role.EMPLOYE).count());
                summaryLabel4.setText("Solde système : " + CompteBanque.getComptes().stream().mapToDouble(c -> {
                    try {
                        return c.getSolde(c);
                    } catch (CompteException e) {
                        return 0;
                    }
                }).sum() + " €");
            } else if (compte.getRole() == CompteBanque.Role.EMPLOYE) {
                titleLabel.setText("Dashboard Employé");
                summaryLabel1.setText("Clients gérés : " + CompteBanque.getComptes().stream().filter(c -> c.getRole() == CompteBanque.Role.CLIENT).count());
                summaryLabel2.setText("Maximum de virement : " + CompteBanque.MAX_VIREMENT + " €");
                summaryLabel3.setText("Actions autorisées : dépôt / retrait / virement");
                summaryLabel4.setText("Compte courant : " + compte.getNomProprietaire());
            } else {
                titleLabel.setText("Dashboard Client");
                summaryLabel1.setText("Solde : " + compte.getSolde(compte) + " €");
                summaryLabel2.setText("Virement restant : " + (CompteBanque.MAX_VIREMENT - compte.getVirement()) + " €");
                summaryLabel3.setText("Opérations : " + compte.getOperations().size());
                summaryLabel4.setText("Rôle : " + compte.getRole());
            }
        } catch (CompteException e) {
            summaryLabel1.setText("Erreur de lecture du compte");
            summaryLabel2.setText("");
            summaryLabel3.setText("");
            summaryLabel4.setText("");
        }
    }

    @FXML
    private void openProfile() {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "profile-view.fxml");
    }

    @FXML
    private void goToDashboard() {
        Stage stage = (Stage) dashboardButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "dashboard-view.fxml");
    }

    @FXML
    private void logout() {
        SessionManager.clear();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "hello-view.fxml");
    }
}
