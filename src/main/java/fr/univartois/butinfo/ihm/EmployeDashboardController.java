package fr.univartois.butinfo.ihm;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmployeDashboardController {
    @FXML
    private ComboBox<CompteBanque> compteClientCombo;
    @FXML
    private TextField montantField;

    @FXML
    private void initialize() {
        if (!SessionManager.hasRole(CompteBanque.Role.EMPLOYE)) {
            Stage stage = (Stage) compteClientCombo.getScene().getWindow();
            ApplicationBanque.changerVue(stage, "hello-view.fxml");
            return;
        }

        compteClientCombo.setItems(FXCollections.observableArrayList(
                CompteBanque.getComptes().stream()
                        .filter(c -> c.getRole() == CompteBanque.Role.CLIENT)
                        .toList()
        ));
    }

    @FXML
    private void deposerArgent() {
        if (!SessionManager.hasRole(CompteBanque.Role.EMPLOYE)) {
            return;
        }
        CompteBanque compte = compteClientCombo.getValue();
        String montantStr = montantField.getText();
        if (compte == null || montantStr == null || montantStr.isBlank()) {
            return;
        }
        try {
            double montant = Double.parseDouble(montantStr);
            compte.depot(montant, compte);
        } catch (Exception e) {
            System.err.println("Erreur dépôt : " + e.getMessage());
        }
    }

    @FXML
    private void retirerArgent() {
        if (!SessionManager.hasRole(CompteBanque.Role.EMPLOYE)) {
            return;
        }
        CompteBanque compte = compteClientCombo.getValue();
        String montantStr = montantField.getText();
        if (compte == null || montantStr == null || montantStr.isBlank()) {
            return;
        }
        try {
            double montant = Double.parseDouble(montantStr);
            compte.retrait(montant, compte);
        } catch (Exception e) {
            System.err.println("Erreur retrait : " + e.getMessage());
        }
    }

    @FXML
    private void retourMenu() {
        Stage stage = (Stage) compteClientCombo.getScene().getWindow();
        ApplicationBanque.changerVue(stage, "navigation-view.fxml");
    }
}
