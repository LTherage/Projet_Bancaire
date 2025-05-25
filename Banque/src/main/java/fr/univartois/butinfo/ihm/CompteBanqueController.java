package fr.univartois.butinfo.ihm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class CompteBanqueController extends ApplicationBanque{
    @FXML
    private Label nomTitulaireLabel, soldeCompteLabel, plafondVirementLabel;

    @FXML
    private TextField montantVirementField, motifVirementField;

    @FXML
    private Button confirmerVirementButton, annulerVirementButton;

    @FXML
    private TableView<Operation> historiqueTable;
    @FXML
    private TableColumn<Operation, String> dateColonne;
    @FXML
    private TableColumn<Operation, String> typeColonne;
    @FXML
    private TableColumn<Operation, Double> montantColonne;
    @FXML
    private TableColumn<Operation, Double> soldeColonne;
    @FXML
    private TableColumn<Operation, String> motifColonne;

    @FXML
    private ComboBox<CompteBanque> compteDestinataireCombo;

    private CompteBanque compteActuel;
    private ObservableList<Operation> historique = FXCollections.observableArrayList();
    private ObservableList<CompteBanque> comptesDisponibles;


    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Charger le FXML avec un chemin absolu
            String fxmlPath = "/fr/univartois/butinfo/ihm/interface-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (fxmlLoader.getLocation() == null) {
                throw new IOException("Impossible de trouver le fichier : " + fxmlPath);
            }

            Parent viewContent = fxmlLoader.load();
            Scene scene = new Scene(viewContent, 900, 540);

            // Charger le CSS avec un chemin absolu
            String cssPath = "/fr/univartois/butinfo/ihm/style.css";
            String cssUrl = getClass().getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(cssUrl);

            stage.setScene(scene);
            stage.setTitle("Votre Application de banque");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur au démarrage : " + e.getMessage(), e);
        }
    }

    @FXML
    private void initialize() {
        // Initialisation des colonnes du tableau
        dateColonne.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColonne.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantColonne.setCellValueFactory(new PropertyValueFactory<>("montant"));
        soldeColonne.setCellValueFactory(new PropertyValueFactory<>("solde"));
        motifColonne.setCellValueFactory(new PropertyValueFactory<>("motif"));

        // Configuration des écouteurs
        montantVirementField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                montantVirementField.setText(oldVal);
            }
            validateVirementForm();
        });

        // Initialiser l'historique
        historique = FXCollections.observableArrayList();
        historiqueTable.setItems(historique);

        // Configurer les boutons
        confirmerVirementButton.setOnAction(e -> handleVirement());
        annulerVirementButton.setOnAction(e -> clearVirementForm());
    }


    private void validateVirementForm() {
        boolean isValid = compteDestinataireCombo.getValue() != null &&
                !montantVirementField.getText().isEmpty() &&
                montantVirementField.getText().matches("\\d*(\\.\\d{0,2})?") &&
                Double.parseDouble(montantVirementField.getText()) > 0;
        confirmerVirementButton.setDisable(!isValid);
    }

    @FXML
    private void handleVirement() {
        try {
            CompteBanque destinataire = compteDestinataireCombo.getValue();
            double montant = Double.parseDouble(montantVirementField.getText());
            String motif = motifVirementField.getText();

            if (compteActuel == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun compte connecté");
                return;
            }

            if (destinataire == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un destinataire");
                return;
            }

            // Vérification du plafond
            if (compteActuel.getVirement() + montant > CompteBanque.MAX_VIREMENT) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Le plafond de virement serait dépassé");
                return;
            }

            // Effectuer le virement
            compteActuel.virement(compteActuel, destinataire, montant);

            // Ajouter à l'historique
            addOperationToHistorique("Virement vers " + destinataire.getNomProprietaire(),
                    -montant, motif);

            // Mise à jour de l'interface
            updateInterface();
            clearVirementForm();

            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Virement effectué avec succès");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Montant invalide");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    private void addOperationToHistorique(String type, double montant, String motif) {
        try {
            Operation operation = new Operation(type, montant, compteActuel.getSolde(compteActuel), motif);
            historique.add(operation);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ajouter l'opération à l'historique");
        }
    }


    private void updateInterface() {
        try {
            if (compteActuel != null) {
                soldeCompteLabel.setText(String.format("%,.2f €",
                        compteActuel.getSolde(compteActuel)));
                plafondVirementLabel.setText(String.format("%,.2f €",
                        CompteBanque.MAX_VIREMENT - compteActuel.getVirement()));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    private void clearVirementForm() {
        compteDestinataireCombo.setValue(null);
        montantVirementField.clear();
        motifVirementField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Ajouter une méthode pour définir le compte actuel
    public void setCompteActuel(CompteBanque compte) {
        this.compteActuel = compte;
        updateInterface();
    }

    // Ajouter une méthode pour définir les comptes disponibles
    public void setComptesDisponibles(List<CompteBanque> comptes) {
        comptesDisponibles = FXCollections.observableArrayList(comptes);
        compteDestinataireCombo.setItems(comptesDisponibles);
    }



    public static void main(String[] args) {launch();}

}
